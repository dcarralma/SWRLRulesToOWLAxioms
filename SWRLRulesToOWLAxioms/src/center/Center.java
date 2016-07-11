package center;

import rulesToAxioms.HeadPreprocessor;
import rulesToAxioms.Srd;
import rulesToAxioms.Translator;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAnnotation;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataRange;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLClassAtom;
import org.semanticweb.owlapi.model.SWRLDataPropertyAtom;
import org.semanticweb.owlapi.model.SWRLDataRangeAtom;
import org.semanticweb.owlapi.model.SWRLIArgument;
import org.semanticweb.owlapi.model.SWRLIndividualArgument;
import org.semanticweb.owlapi.model.SWRLLiteralArgument;
import org.semanticweb.owlapi.model.SWRLObjectPropertyAtom;
import org.semanticweb.owlapi.model.SWRLRule;
import org.semanticweb.owlapi.model.SWRLSameIndividualAtom;
import org.semanticweb.owlapi.model.SWRLVariable;

import uk.ac.manchester.cs.owl.owlapi.SWRLClassAtomImpl;
import uk.ac.manchester.cs.owl.owlapi.SWRLDataPropertyAtomImpl;
import uk.ac.manchester.cs.owl.owlapi.SWRLDataRangeAtomImpl;
import uk.ac.manchester.cs.owl.owlapi.SWRLObjectPropertyAtomImpl;
import uk.ac.manchester.cs.owl.owlapi.SWRLRuleImpl;
import uk.ac.manchester.cs.owl.owlapi.SWRLSameIndividualAtomImpl;

public class Center {
	static OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

	public static void main(String[] arguments) throws Exception {

		OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

		OWLClass C = factory.getOWLClass(IRI.create("C"));
		OWLClass C2 = factory.getOWLClass(IRI.create("C2"));
		OWLClass C3 = factory.getOWLClass(IRI.create("C3"));
		OWLClass C4 = factory.getOWLClass(IRI.create("C4"));
		// OWLClass C5 = factory.getOWLClass(IRI.create("C4"));

		OWLDataRange D = factory.getOWLDatatype(IRI.create("D"));
		OWLDataRange D2 = factory.getOWLDatatype(IRI.create("D2"));
		// OWLDataRange D3 = factory.getOWLDatatype(IRI.create("D3"));
		// OWLDataRange D4 = factory.getOWLDatatype(IRI.create("D4"));
		// OWLDataRange D5 = factory.getOWLDatatype(IRI.create("D5"));

		OWLObjectProperty R = factory.getOWLObjectProperty(IRI.create("R"));
		OWLObjectProperty R2 = factory.getOWLObjectProperty(IRI.create("R2"));
		OWLObjectProperty R3 = factory.getOWLObjectProperty(IRI.create("R3"));
		OWLObjectProperty R4 = factory.getOWLObjectProperty(IRI.create("R4"));
		OWLObjectProperty R5 = factory.getOWLObjectProperty(IRI.create("R5"));

		OWLDataProperty S = factory.getOWLDataProperty(IRI.create("S"));
		OWLDataProperty S2 = factory.getOWLDataProperty(IRI.create("S2"));
		// OWLDataProperty S3 = factory.getOWLDataProperty(IRI.create("S3"));
		// OWLDataProperty S4 = factory.getOWLDataProperty(IRI.create("S4"));
		// OWLDataProperty S5 = factory.getOWLDataProperty(IRI.create("S5"));

		SWRLVariable x = factory.getSWRLVariable(IRI.create("x"));
		SWRLVariable y = factory.getSWRLVariable(IRI.create("y"));
		SWRLVariable z = factory.getSWRLVariable(IRI.create("z"));
		SWRLVariable v = factory.getSWRLVariable(IRI.create("v"));
		SWRLVariable w = factory.getSWRLVariable(IRI.create("w"));
		// SWRLVariable u = factory.getSWRLVariable(IRI.create("u"));

		SWRLIndividualArgument a = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create("a")));
		SWRLIndividualArgument b = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create("b")));
		SWRLIndividualArgument c = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create("c")));
		// SWRLIndividualArgument d = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create("d")));
		// SWRLIndividualArgument e = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create("d")));

		SWRLLiteralArgument l = factory.getSWRLLiteralArgument(factory.getOWLLiteral("l1"));
		// SWRLLiteralArgument l2 = factory.getSWRLLiteralArgument(factory.getOWLLiteral("l2"));
		// SWRLLiteralArgument l3 = factory.getSWRLLiteralArgument(factory.getOWLLiteral("l3"));
		// SWRLLiteralArgument l4 = factory.getSWRLLiteralArgument(factory.getOWLLiteral("l4"));
		// SWRLLiteralArgument l5 = factory.getSWRLLiteralArgument(factory.getOWLLiteral("l5"));

		ArrayList<SWRLRule> testRules = new ArrayList<SWRLRule>();
		Set<SWRLAtom> head = new HashSet<SWRLAtom>();
		Set<SWRLAtom> body = new HashSet<SWRLAtom>();

		// Rule 0
		// C(x), R(z, b) :- C2(x), R2(x, a), S(x, v), D(v), D2(v), C3(a), R3(a, z), C4(z), R4(x, z).
		// C2 sqcap exists R2.({a} sqcap C3) sqcap exists R4.(C4 sqcap exists R3-.{a}) sqcap exists S.D sqsubs C
		// C4 sqcap exists R4-.(C2 sqcap exists R2.{a} sqcap exists S.(D sqcap D2)) sqcap exists R3-.({a} sqcap C3) sqsubs exists R.{b}
		head.add(new SWRLClassAtomImpl(C, x));
		head.add(new SWRLObjectPropertyAtomImpl(R, z, b));
		body.add(new SWRLClassAtomImpl(C, x));
		body.add(new SWRLObjectPropertyAtomImpl(R2, x, a));
		body.add(new SWRLDataPropertyAtomImpl(S, x, v));
		body.add(new SWRLDataRangeAtomImpl(D, v));
		body.add(new SWRLDataRangeAtomImpl(D2, v));
		body.add(new SWRLClassAtomImpl(C3, a));
		body.add(new SWRLObjectPropertyAtomImpl(R3, a, z));
		body.add(new SWRLClassAtomImpl(C4, z));
		body.add(new SWRLObjectPropertyAtomImpl(R4, x, z));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 1
		// C(x) :- C2(x), R(x, y), C3(y), R2(y, z), C3(z), R4(x, v), C4(v), S(v, w), D(w), S2(v, l), D2(l).
		// C2 sqcap exists R.(C3 sqcap exists R2.C3) sqcap exists R4.(C4 sqcap exists S.D sqcap exists S2.({l} sqcap D2)) sqsubs C
		head.add(new SWRLClassAtomImpl(C, x));
		body.add(new SWRLClassAtomImpl(C2, x));
		body.add(new SWRLObjectPropertyAtomImpl(R, x, y));
		body.add(new SWRLClassAtomImpl(C3, y));
		body.add(new SWRLObjectPropertyAtomImpl(R2, y, z));
		body.add(new SWRLClassAtomImpl(C3, z));
		body.add(new SWRLObjectPropertyAtomImpl(R4, x, v));
		body.add(new SWRLClassAtomImpl(C4, v));
		body.add(new SWRLDataPropertyAtomImpl(S, v, w));
		body.add(new SWRLDataRangeAtomImpl(D, w));
		body.add(new SWRLDataPropertyAtomImpl(S2, v, l));
		body.add(new SWRLDataRangeAtomImpl(D2, l));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 2
		// R(x, y) :- C(x), C2(y), C3(z).
		// C sqs exists Rf.Self
		// C2 sqs exists Rf2.Self
		// C3 sqs exists Rf3.Self
		// Rf o U o Rf2 o U o Rf3 sqs R
		head.add(new SWRLObjectPropertyAtomImpl(R, x, y));
		body.add(new SWRLClassAtomImpl(C, x));
		body.add(new SWRLClassAtomImpl(C2, y));
		body.add(new SWRLClassAtomImpl(C3, z));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 3
		//  :- R(x, a), R2(a, b), R3(b, c), R4(c, a).
		// exists R.({a} sqcap exists R2.({b} sqcap exists R3.{c} sqcap exists R4-.{a})) sqsubs bot
		body.add(new SWRLObjectPropertyAtomImpl(R, x, a));
		body.add(new SWRLObjectPropertyAtomImpl(R2, a, b));
		body.add(new SWRLObjectPropertyAtomImpl(R3, b, c));
		body.add(new SWRLObjectPropertyAtomImpl(R4, c, a));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		body.clear();

		// Rule 4
		// C(x) :- .
		// Top sqsubs C
		head.add(new SWRLClassAtomImpl(C, x));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();

		// Rule 5
		// S(x, y) :- S2(x, y).
		// S sqs S2
		body.add(new SWRLDataPropertyAtomImpl(S, x, y));
		head.add(new SWRLDataPropertyAtomImpl(S2, x, y));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 6
		// D(y) :- C(x), S(x, y).
		// C sqs forall S.D
		head.add(new SWRLDataRangeAtomImpl(D, y));
		body.add(new SWRLClassAtomImpl(C, x));
		body.add(new SWRLDataPropertyAtomImpl(S, x, y));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 7
		// R(x, v) :- R2(x, y), R3(y, a), C(a), R4(y, z), R5(z, v), R(v, a).
		// exists R3.(C sqcap {a}) sqsubs exists Rf4.Self
		// exists R.{a} sqsubs Rf5.Self
		// R2 o Rf4 o R4 o R5 o Rf5 sqsubs R
		head.add(new SWRLObjectPropertyAtomImpl(R, x, v));
		body.add(new SWRLObjectPropertyAtomImpl(R2, x, y));
		body.add(new SWRLObjectPropertyAtomImpl(R3, y, a));
		body.add(new SWRLClassAtomImpl(C, a));
		body.add(new SWRLObjectPropertyAtomImpl(R4, y, z));
		body.add(new SWRLObjectPropertyAtomImpl(R5, z, v));
		body.add(new SWRLObjectPropertyAtomImpl(R, v, a));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 8
		// :- .
		// top sqs bot
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));

		//		// Rule 9
		//		// y = z :- C(x), R(x, y), R3(y, v), C2(v), R2(x, z), R3(z, w), C2(w).
		//		// R sqs Rf6
		//		// R2 sqs Rf6
		//		// C sqs < 2 Rf6.(exists R3.C2)
		//		head.add(new SWRLSameIndividualAtomImpl(factory, y, z));
		//		body.add(new SWRLClassAtomImpl(C, x));
		//		body.add(new SWRLObjectPropertyAtomImpl(R, x, y));
		//		body.add(new SWRLObjectPropertyAtomImpl(R2, x, z));
		//		body.add(new SWRLObjectPropertyAtomImpl(R3, z, w));
		//		body.add(new SWRLClassAtomImpl(C2, w));
		//		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		//		head.clear();
		//		body.clear();

		// Rule 10
		// C(x) :- R(x, y), R2(y, z), R3(z, x).
		head.add(new SWRLClassAtomImpl(C, x));
		body.add(new SWRLObjectPropertyAtomImpl(R, x, y));
		body.add(new SWRLObjectPropertyAtomImpl(R2, y, z));
		body.add(new SWRLObjectPropertyAtomImpl(R3, z, x));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 11
		// C(x) :- S(x, y), S2(z, y).
		head.add(new SWRLClassAtomImpl(C, x));
		body.add(new SWRLDataPropertyAtomImpl(S, x, y));
		body.add(new SWRLDataPropertyAtomImpl(S2, z, y));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		// Rule 12
		// C(x) :- S(x, y), R(x, y).
		head.add(new SWRLClassAtomImpl(C, x));
		body.add(new SWRLDataPropertyAtomImpl(S, x, y));
		body.add(new SWRLObjectPropertyAtomImpl(R, x, y));
		testRules.add(new SWRLRuleImpl(body, head, new HashSet<OWLAnnotation>()));
		head.clear();
		body.clear();

		for (int i = 0; i < testRules.size(); i++)
			if (areObjAndDataVarsDisj(testRules.get(i)))
				for (SWRLRule splittedRule : HeadPreprocessor.computeRuleHeadSplits(testRules.get(i))) {
					System.out.println("Rule " + i + " : ");
					System.out.println(" > Head: " + Srd.shortStr(splittedRule.getHead().toString()));
					System.out.println(" > Body: " + Srd.shortStr(splittedRule.getBody().toString()));
					Translator translator = new Translator(splittedRule);
					translator.ruleToAxioms();
					if (!translator.resultingAxioms.isEmpty())
						for (OWLAxiom resultingAxiom : translator.resultingAxioms)
							System.out.println("   o " + Srd.shortStr(resultingAxiom.toString()));
					System.out.println();
				}
			else
				System.out.println("Illegal SWRL Rule." + "\n");
	}

	private static boolean areObjAndDataVarsDisj(SWRLRule testRule) {
		Set<SWRLAtom> ruleAtoms = new HashSet<SWRLAtom>();
		ruleAtoms.addAll(testRule.getHead());
		ruleAtoms.addAll(testRule.getBody());

		Set<String> dataVars = new HashSet<String>();
		Set<String> objectVars = new HashSet<String>();

		for (SWRLAtom atom : ruleAtoms)
			if (atom instanceof SWRLClassAtom) {
				SWRLClassAtom classAtom = (SWRLClassAtom) atom;
				if (classAtom.getArgument() instanceof SWRLVariable)
					objectVars.add(classAtom.getArgument().toString());

			} else if (atom instanceof SWRLObjectPropertyAtom) {
				SWRLObjectPropertyAtom objPropAtom = (SWRLObjectPropertyAtom) atom;
				if (objPropAtom.getFirstArgument() instanceof SWRLVariable)
					objectVars.add(objPropAtom.getFirstArgument().toString());
				if (objPropAtom.getSecondArgument() instanceof SWRLVariable)
					objectVars.add(objPropAtom.getSecondArgument().toString());

			} else if (atom instanceof SWRLDataPropertyAtom) {
				SWRLDataPropertyAtom dataPropAtom = (SWRLDataPropertyAtom) atom;
				if (dataPropAtom.getFirstArgument() instanceof SWRLVariable)
					objectVars.add(dataPropAtom.getFirstArgument().toString());
				if (dataPropAtom.getSecondArgument() instanceof SWRLVariable)
					dataVars.add(dataPropAtom.getSecondArgument().toString());

			} else if (atom instanceof SWRLDataRangeAtom) {
				SWRLDataRangeAtom dataRangeAtom = (SWRLDataRangeAtom) atom;
				if (dataRangeAtom.getArgument() instanceof SWRLVariable)
					dataVars.add(dataRangeAtom.getArgument().toString());

			} else if (atom instanceof SWRLSameIndividualAtom) {
				SWRLSameIndividualAtom sameIndAtom = (SWRLSameIndividualAtom) atom;
				if (sameIndAtom.getFirstArgument() instanceof SWRLVariable)
					objectVars.add(sameIndAtom.getFirstArgument().toString());
				if (sameIndAtom.getSecondArgument() instanceof SWRLVariable)
					objectVars.add(sameIndAtom.getSecondArgument().toString());
			}

		Set<String> intersectionVars = new HashSet<String>();
		intersectionVars.addAll(objectVars);
		intersectionVars.retainAll(dataVars);
		return intersectionVars.isEmpty();
	}
}
