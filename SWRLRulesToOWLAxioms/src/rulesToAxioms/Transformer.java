package rulesToAxioms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLVariable;

import uk.ac.manchester.cs.owl.owlapi.OWLSubClassOfAxiomImpl;
import uk.ac.manchester.cs.owl.owlapi.SWRLRuleImpl;

public class Transformer {

	public static OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();
	public static boolean isExpressible;

	public static Set<OWLAxiom> ruleToAxioms(SWRLRule rule) {

		isExpressible = true;

		Set<SWRLAtom> body = rule.getBody();
		Set<SWRLAtom> head = rule.getHead();
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();

		if (head.isEmpty())
			axioms.add(lessThanOneVarRule(body, head));
		else
			for (Set<SWRLAtom> headSplit : HeadSplitter.splitHead(head)) {
				System.out.println(Srd.toString(headSplit));
				if (Srd.getVariablesToSet(headSplit).size() <= 1)
					axioms.add(lessThanOneVarRule(body, headSplit));
				else
					axioms.addAll(twoVarRuleToAxioms(body, headSplit));
			}
		return axioms;
	}

	private static OWLAxiom lessThanOneVarRule(Set<SWRLAtom> body, Set<SWRLAtom> head) {

		Set<SWRLVariable> bodyVariables = Srd.getVariablesToSet(body);
		Set<SWRLVariable> headVariables = Srd.getVariablesToSet(head);

		if (body.isEmpty()) {
			SWRLVariable bodyVariable;
			if (headVariables.isEmpty())
				bodyVariable = factory.getSWRLVariable(IRI.create("vF"));
			else
				bodyVariable = headVariables.iterator().next();
			body.add(factory.getSWRLClassAtom(factory.getOWLThing(), bodyVariable));
			bodyVariables.add(bodyVariable);
		}

		if (head.isEmpty()) {
			SWRLVariable headVariable;
			if (bodyVariables.isEmpty()) {
				headVariable = factory.getSWRLVariable(IRI.create("vF"));
				body.add(factory.getSWRLClassAtom(factory.getOWLThing(), headVariable));
				bodyVariables.add(headVariable);
			} else
				headVariable = chooseARootVariable(body);
			head.add(factory.getSWRLClassAtom(factory.getOWLNothing(), headVariable));
			headVariables.add(headVariable);
		}

		if (bodyVariables.isEmpty() && headVariables.isEmpty()) {
			SWRLVariable freshVariable = factory.getSWRLVariable(IRI.create("vF"));
			body.add(factory.getSWRLClassAtom(factory.getOWLThing(), freshVariable));
			bodyVariables.add(freshVariable);
			head.add(factory.getSWRLClassAtom(factory.getOWLThing(), freshVariable));
			headVariables.add(freshVariable);
		}

		if (!headVariables.isEmpty()) {
			SWRLVariable headVar = headVariables.iterator().next();
			if (!bodyVariables.contains(headVar)) {
				body.add(factory.getSWRLClassAtom(factory.getOWLThing(), headVar));
				bodyVariables.add(headVar);
			}
		} else {
			SWRLVariable rootVariable = chooseARootVariable(body);
			head.add(factory.getSWRLClassAtom(factory.getOWLThing(), rootVariable));
			headVariables.add(rootVariable);
			XGraph headGraph = new XGraph(head);
			addConnection(head, headGraph, rootVariable);
		}

		Set<SWRLVariable> intersectionVariables = new HashSet<SWRLVariable>();
		intersectionVariables.addAll(bodyVariables);
		intersectionVariables.retainAll(headVariables);

		SWRLVariable rootVariable = null;
		if (intersectionVariables.size() != 1)
			System.out.println("WARNING!!! Unable to find root variable.");
		else
			rootVariable = intersectionVariables.iterator().next();

		XGraph bodyGraph = new XGraph(body);
		while (!bodyGraph.returnUnreachableVariables(rootVariable).isEmpty())
			addConnection(body, bodyGraph, rootVariable);

		if (bodyGraph.containsCycleOverVariables()) {
			isExpressible = false;
			System.out.println(" > Cannot transform input rule: The shape of the body is not acyclic.");
			return new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>());
		}

		return new OWLSubClassOfAxiomImpl(ClassExpBuilder.atomsToExp(body, rootVariable), ClassExpBuilder.atomsToExp(head, rootVariable), new HashSet<OWLAnnotation>());
	}

	private static Set<OWLAxiom> twoVarRuleToAxioms(Set<SWRLAtom> body, Set<SWRLAtom> head) {
		Set<OWLAxiom> axioms = new HashSet<OWLAxiom>();
		return axioms;
	}

	private static SWRLVariable chooseARootVariable(Set<SWRLAtom> body) {
		Set<SWRLVariable> nonRootVariables = new HashSet<SWRLVariable>();
		for (SWRLAtom bodyAtom : body) {
			ArrayList<SWRLVariable> atomVariables = Srd.getVariablesToArrayList(bodyAtom);
			if (atomVariables.size() == 2)
				nonRootVariables.add(atomVariables.get(1));
		}

		Set<SWRLVariable> variables = Srd.getVariablesToSet(body);
		for (SWRLVariable variable : variables)
			if (!nonRootVariables.contains(variable))
				return variable;

		return variables.iterator().next();
	}

	private static void addConnection(Set<SWRLAtom> atoms, XGraph graph, SWRLVariable rootVariable) {
		Set<SWRLArgument> inconvenientVertices = new HashSet<SWRLArgument>();
		for (SWRLAtom atom : atoms) {
			ArrayList<SWRLArgument> arguments = Srd.getArgumentsToArrayList(atom);
			if (arguments.size() == 2)
				inconvenientVertices.add(arguments.get(1));
		}

		Set<SWRLIArgument> unreachableVertices = graph.returnUnreachableVariables(rootVariable);
		SWRLIArgument vertexToConnect = null;
		for (SWRLIArgument unreachableVertex : unreachableVertices)
			if (!inconvenientVertices.contains(unreachableVertex))
				vertexToConnect = unreachableVertex;
		if (vertexToConnect == null)
			vertexToConnect = unreachableVertices.iterator().next();

		atoms.add(factory.getSWRLObjectPropertyAtom(factory.getOWLTopObjectProperty(), rootVariable, (SWRLIArgument) vertexToConnect));
		SWRLIArgument[] edge1 = { rootVariable, (SWRLIArgument) vertexToConnect };
		graph.edges.add(edge1);
		SWRLIArgument[] edge2 = { (SWRLIArgument) vertexToConnect, rootVariable };
		graph.edges.add(edge2);
	}

}

//
//			SWRLVariable rootVariable;
//			XGraph bodyGraph;
//			OWLClassExpression subClass;
//			OWLClassExpression superClass;
//
//			switch (intersectionVariables.size()) {
//			case 0:
//				System.out.println("WARNING!!! No varible is shared between the body and the head of the rule.");
//				break;
//
//			case 1:
//				rootVariable = intersectionVariables.iterator().next();
//				if (!body.isEmpty()) {
//					bodyGraph = new XGraph(body);
//					while (!bodyGraph.returnUnreachableVariables(rootVariable).isEmpty())
//						addConnection(body, bodyGraph, rootVariable);
//
//					if (bodyGraph.containsCycle()) {
//						System.out.println(" > Cannot transform input rule: The shape of the body is not acyclic.");
//						isExpressible = false;
//						break;
//					}
//
//					subClass = atomsToAxiom(body, rootVariable);
//				} else
//					subClass = factory.getOWLThing();
//
//				// Building SuperClass from Head
//				superClass = atomsToAxiom(splitHead, rootVariable);
//
//				OWLSubClassOfAxiom newAxiom = new OWLSubClassOfAxiomImpl(subClass, superClass, new HashSet<OWLAnnotation>());
//				System.out.println(" > Subclass:" + "\t" + newAxiom.getSubClass());
//				System.out.println(" > Superclass:" + "\t" + newAxiom.getSuperClass());
//				axioms.add(newAxiom);
//				break;
//
//			case 2:
//				System.out.println(" > Unimplemented Functionality: 2 variables are shared between the body of the head of the rule.");
//				break;
//			default:
//				System.out.println(" > Cannot transform input rule: 0 or more than 2 variables are shared between the body of the head of the rule.");
//				break;
//			}
//
//			//		axioms.add(rule);
//			//		return axioms;
//			System.out.println();
//		}
//		return axioms;
//				rootVariable = chooseRootVariable(body);
//				bodyGraph = new XGraph(body);
//				while (!bodyGraph.returnUnreachableVariables(rootVariable).isEmpty())
//					addConnection(body, bodyGraph, rootVariable);
//
//				if (bodyGraph.containsCycle()) {
//					System.out.println(" > Cannot transform input rule: The shape of the body is not acyclic.");
//					isExpressible = false;
//				}
//				subClass = atomsToAxiom(body, rootVariable);
//
//				Set<OWLClassExpression> conjuncts = new HashSet<OWLClassExpression>();
//				ArrayList<SWRLArgument> headAtomArguments = Srd.getArgumentsToArrayList(headAtom);
//				if (headAtomArguments.size() == 1) {
//					Set<OWLIndividual> individualSet = new HashSet<OWLIndividual>();
//					individualSet.add(factory.getOWLNamedIndividual(IRI.create(headAtomArguments.get(0).toString())));
//					conjuncts.add(new OWLObjectOneOfImpl(individualSet));
//					conjuncts.add(factory.getOWLClass(IRI.create(headAtom.getPredicate().toString())));
//				} else {
//					Set<OWLIndividual> individualSeta = new HashSet<OWLIndividual>();
//					individualSeta.add(factory.getOWLNamedIndividual(IRI.create(headAtomArguments.get(0).toString())));
//					conjuncts.add(new OWLObjectOneOfImpl(individualSeta));
//					Set<OWLIndividual> individualSetb = new HashSet<OWLIndividual>();
//					individualSetb.add(factory.getOWLNamedIndividual(IRI.create(headAtomArguments.get(1).toString())));
//					conjuncts.add(new OWLObjectSomeValuesFromImpl(factory.getOWLObjectProperty(IRI.create(headAtom.getPredicate().toString())),
//							new OWLObjectOneOfImpl(individualSetb)));
//				}
//				superClass = new OWLObjectSomeValuesFromImpl(factory.getOWLTopObjectProperty(), new OWLObjectIntersectionOfImpl(conjuncts));
//
//				OWLSubClassOfAxiom newAxiom = new OWLSubClassOfAxiomImpl(subClass, superClass, new HashSet<OWLAnnotation>());
//				System.out.println(" > Subclass:" + "\t" + newAxiom.getSubClass());
//				System.out.println(" > Superclass:" + "\t" + newAxiom.getSuperClass());
//				axioms.add(newAxiom);
