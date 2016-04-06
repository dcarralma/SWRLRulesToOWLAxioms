package rulesToAxioms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyExpression;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLPredicate;

import uk.ac.manchester.cs.owl.owlapi.OWLObjectHasSelfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectIntersectionOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectOneOfImpl;
import uk.ac.manchester.cs.owl.owlapi.OWLObjectSomeValuesFromImpl;

public class ClassExpBuilder {

	private static OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

	protected static OWLClassExpression atomsToExp(Set<SWRLAtom> atoms, SWRLArgument rootArgument) {

		boolean rootArgVar = rootArgument.toString().contains("Variable");
		Set<OWLClassExpression> classExpressionConjuncts = new HashSet<OWLClassExpression>();

		if (!rootArgVar) {
			Set<OWLIndividual> individualSet = new HashSet<OWLIndividual>();
			individualSet.add(factory.getOWLNamedIndividual(IRI.create(rootArgument.toString().replace("<", "").replace(">", ""))));
			classExpressionConjuncts.add(new OWLObjectOneOfImpl(individualSet));
		}

		for (SWRLAtom atom : atoms) {
			ArrayList<SWRLArgument> arguments = new ArrayList<SWRLArgument>();
			for (SWRLArgument argument : atom.getAllArguments())
				arguments.add(argument);

			if (arguments.contains(rootArgument)) {
				if (atom.toString().contains("ClassAtom"))
					classExpressionConjuncts.add(factory.getOWLClass(IRI.create(atom.getPredicate().toString().replace("<", "").replace(">", ""))));
				else if (atom.toString().contains("ObjectPropertyAtom")) {

					OWLObjectPropertyExpression objectRole;
					SWRLArgument arg0;
					SWRLArgument arg1;

					if (arguments.get(0).equals(rootArgument)) {
						objectRole = swrlPredicateToObjectProperty(atom.getPredicate());
						arg0 = arguments.get(0);
						arg1 = arguments.get(1);
					} else {
						objectRole = Srd.invert(swrlPredicateToObjectProperty(atom.getPredicate()));
						arg0 = arguments.get(1);
						arg1 = arguments.get(0);
					}

					if (arg0.equals(arg1))
						classExpressionConjuncts.add(new OWLObjectHasSelfImpl(objectRole));
					else {
						Set<SWRLAtom> atomsSubset = new HashSet<SWRLAtom>();
						for (SWRLAtom atom2 : atoms)
							if (!atom2.toString().contains(rootArgument.toString()))
								atomsSubset.add(atom2);

						if (rootArgVar || !arg1.toString().contains("Variable"))
							classExpressionConjuncts.add(new OWLObjectSomeValuesFromImpl(objectRole, atomsToExp(atomsSubset, arg1)));
					}

				} else {
					System.out.println("WARNING!!! Unimplemented Functionality: Unrecognized Type of Atom" + "\t" + atom);
				}
			}
		}

		if (classExpressionConjuncts.size() == 1)
			return classExpressionConjuncts.iterator().next();
		else if (classExpressionConjuncts.size() > 1)
			return new OWLObjectIntersectionOfImpl(classExpressionConjuncts);
		else
			return factory.getOWLThing();

	}

	private static OWLObjectProperty swrlPredicateToObjectProperty(SWRLPredicate predicate) {
		return factory.getOWLObjectProperty(IRI.create(predicate.toString().replace("<", "").replace(">", "")));
	}
}
