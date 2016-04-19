package center;

import rulesToAxioms.Srd;
import rulesToAxioms.Transformer;

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

import uk.ac.manchester.cs.owl.owlapi.SWRLRuleImpl;

public class Center {
	static OWLDataFactory factory = OWLManager.createOWLOntologyManager().getOWLDataFactory();

	public static void main(String[] arguments) throws Exception {

		ArrayList<SWRLRule> testRules = new ArrayList<SWRLRule>();

		String x = "vx";
		String y = "vy";
		String z = "vz";
		String v = "vv";

		String a = "a";
		String b = "b";
		String c = "c";
		String d = "d";
		String e = "e";

		String A = "A";
		String B = "B";
		String C = "C";
		String D = "D";

		String R = "R";
		String S = "S";
		String V = "V";

		// D(x), R(x, b), B(b), S(b, c), D(d) :- R(x, y), S(y, a).
		Set<SWRLAtom> body0 = new HashSet<SWRLAtom>();
		body0.add(createObjectPropertyAtom(R, x, y));
		//		body0.add(createObjectPropertyAtom(R, x, a));
		//		body0.add(createObjectPropertyAtom(S, y, a));
		//		body0.add(createAtom(A, a));
		Set<SWRLAtom> head0 = new HashSet<SWRLAtom>();
		head0.add(createObjectPropertyAtom(R, x, b));
		//		head0.add(createObjectPropertyAtom(S, x, b));
		head0.add(createObjectPropertyAtom(S, c, a));
		head0.add(createObjectPropertyAtom(S, a, b));
		head0.add(createObjectPropertyAtom(S, b, c));
		head0.add(createAtom(D, d));
		head0.add(createObjectPropertyAtom(S, d, e));
		testRules.add(new SWRLRuleImpl(body0, head0, new HashSet<OWLAnnotation>()));

		// D(x) :- A(x), R(x, y), B(y), S(y, z), C(z), V(z, z).
		Set<SWRLAtom> body1 = new HashSet<SWRLAtom>();
		body1.add(createAtom(A, x));
		body1.add(createObjectPropertyAtom(R, x, y));
		body1.add(createAtom(B, y));
		body1.add(createObjectPropertyAtom(S, y, z));
		body1.add(createAtom(C, z));
		body1.add(createObjectPropertyAtom(V, z, z));
		Set<SWRLAtom> head1 = new HashSet<SWRLAtom>();
		head1.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body1, head1, new HashSet<OWLAnnotation>()));

		// D(x) :- R(x, y), S(y, z), V(x, z).
		Set<SWRLAtom> body2 = new HashSet<SWRLAtom>();
		body2.add(createObjectPropertyAtom(R, x, y));
		body2.add(createObjectPropertyAtom(S, y, z));
		body2.add(createObjectPropertyAtom(V, x, z));
		Set<SWRLAtom> head2 = new HashSet<SWRLAtom>();
		head2.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body2, head2, new HashSet<OWLAnnotation>()));

		// V(x, y) :- A(x), R(x, y), B(y), S(y, z), C(z).
		Set<SWRLAtom> body3 = new HashSet<SWRLAtom>();
		body3.add(createAtom(A, x));
		body3.add(createObjectPropertyAtom(R, x, y));
		body3.add(createAtom(B, y));
		body3.add(createObjectPropertyAtom(S, y, z));
		body3.add(createAtom(C, z));
		Set<SWRLAtom> head3 = new HashSet<SWRLAtom>();
		head3.add(createObjectPropertyAtom(V, x, y));
		testRules.add(new SWRLRuleImpl(body3, head3, new HashSet<OWLAnnotation>()));

		// D(x) :- A(x), R(x, y), B(y), S(y, z), C(z), R1(x, y1), B1(y1), S1(y1, z1), C1(z1),
		Set<SWRLAtom> body4 = new HashSet<SWRLAtom>();
		body4.add(createAtom(A, x));
		body4.add(createObjectPropertyAtom(R, x, y));
		body4.add(createAtom(B, y));
		body4.add(createObjectPropertyAtom(S, y, z));
		body4.add(createAtom(C, z));
		body4.add(createAtom(A + "1", x));
		body4.add(createObjectPropertyAtom(R + "1", x, y + "1"));
		body4.add(createAtom(B + "1", y + "1"));
		body4.add(createObjectPropertyAtom(S + "1", y + "1", z + "1"));
		body4.add(createAtom(C + "1", z + "1"));
		Set<SWRLAtom> head4 = new HashSet<SWRLAtom>();
		head4.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body4, head4, new HashSet<OWLAnnotation>()));

		// D(x) :- R(x, y), S(y, z), V(x, z).
		Set<SWRLAtom> body5 = new HashSet<SWRLAtom>();
		body5.add(createObjectPropertyAtom(R, x, y));
		body5.add(createObjectPropertyAtom(R, y, z));
		body5.add(createObjectPropertyAtom(R, z, v));
		body5.add(createObjectPropertyAtom(R, x, v));
		Set<SWRLAtom> head5 = new HashSet<SWRLAtom>();
		head5.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body5, head5, new HashSet<OWLAnnotation>()));

		// D(x) :- A(x), B(y), S(y, z), C(z).
		Set<SWRLAtom> body6 = new HashSet<SWRLAtom>();
		body6.add(createAtom(A, x));
		body6.add(createAtom(B, y));
		body6.add(createObjectPropertyAtom(S, y, z));
		body6.add(createAtom(C, z));
		Set<SWRLAtom> head6 = new HashSet<SWRLAtom>();
		head6.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body6, head6, new HashSet<OWLAnnotation>()));

		// D(x) :- A(x), B(y), S-(y, z), C(z).
		Set<SWRLAtom> body7 = new HashSet<SWRLAtom>();
		body7.add(createAtom(A, x));
		body7.add(createAtom(B, y));
		body7.add(createObjectPropertyAtom(S, z, y));
		body7.add(createAtom(C, z));
		Set<SWRLAtom> head7 = new HashSet<SWRLAtom>();
		head7.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body7, head7, new HashSet<OWLAnnotation>()));

		// D(x) :- A(x), B(y), C(z).
		Set<SWRLAtom> body8 = new HashSet<SWRLAtom>();
		body8.add(createAtom(A, x));
		body8.add(createAtom(B, y));
		body8.add(createAtom(C, z));
		Set<SWRLAtom> head8 = new HashSet<SWRLAtom>();
		head8.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body8, head8, new HashSet<OWLAnnotation>()));

		// D(y) :- A(x).
		Set<SWRLAtom> body9 = new HashSet<SWRLAtom>();
		body9.add(createAtom(A, x));
		Set<SWRLAtom> head9 = new HashSet<SWRLAtom>();
		head9.add(createAtom(D, y));
		testRules.add(new SWRLRuleImpl(body9, head9, new HashSet<OWLAnnotation>()));

		// D(x) :- R(x, y), R(y, a), R(a, z).
		Set<SWRLAtom> body10 = new HashSet<SWRLAtom>();
		body10.add(createObjectPropertyAtom(R, x, y));
		body10.add(createObjectPropertyAtom(R, y, a));
		body10.add(createObjectPropertyAtom(R, a, b));
		body10.add(createObjectPropertyAtom(R, b, c));
		body10.add(createObjectPropertyAtom(R, c, a));
		Set<SWRLAtom> head10 = new HashSet<SWRLAtom>();
		head10.add(createAtom(D, x));
		testRules.add(new SWRLRuleImpl(body10, head10, new HashSet<OWLAnnotation>()));

		// R(x, y):- .
		Set<SWRLAtom> body11 = new HashSet<SWRLAtom>();
		Set<SWRLAtom> head11 = new HashSet<SWRLAtom>();
		head11.add(createObjectPropertyAtom(R, x, y));
		testRules.add(new SWRLRuleImpl(body11, head11, new HashSet<OWLAnnotation>()));

		// D(x):- .
		Set<SWRLAtom> head12 = new HashSet<SWRLAtom>();
		head12.add(createAtom(C, x));
		testRules.add(new SWRLRuleImpl(new HashSet<SWRLAtom>(), head12, new HashSet<OWLAnnotation>()));

		// :- D(x).
		Set<SWRLAtom> body13 = new HashSet<SWRLAtom>();
		body13.add(createAtom(C, x));
		testRules.add(new SWRLRuleImpl(body13, new HashSet<SWRLAtom>(), new HashSet<OWLAnnotation>()));

		for (SWRLRule testRule : testRules) {
			System.out.println("Rule: " + Srd.toString(testRule));
			Set<OWLAxiom> equivalentAxioms = Transformer.ruleToAxioms(testRule);

			for (OWLAxiom equivalentAxiom : equivalentAxioms)
				System.out.println(equivalentAxiom);
			System.out.println();
		}

	}

	public static SWRLAtom createObjectPropertyAtom(String roleStr, String arg0Str, String arg1Str) {
		SWRLIArgument arg0;
		if (arg0Str.startsWith("v"))
			arg0 = factory.getSWRLVariable(IRI.create(arg0Str));
		else
			arg0 = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create(arg0Str)));
		SWRLIArgument arg1;
		if (arg1Str.startsWith("v"))
			arg1 = factory.getSWRLVariable(IRI.create(arg1Str));
		else
			arg1 = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create(arg1Str)));
		return factory.getSWRLObjectPropertyAtom(factory.getOWLObjectProperty(IRI.create(roleStr)), arg0, arg1);
	}

	public static SWRLAtom createAtom(String conceptStr, String argStr) {
		SWRLArgument arg;
		if (argStr.startsWith("v"))
			arg = factory.getSWRLVariable(IRI.create(argStr));
		else
			arg = factory.getSWRLIndividualArgument(factory.getOWLNamedIndividual(IRI.create(argStr)));
		return factory.getSWRLClassAtom(factory.getOWLClass(IRI.create(conceptStr)), (SWRLIArgument) arg);
	}

}
