package rulesToAxioms;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.semanticweb.owlapi.model.SWRLArgument;
import org.semanticweb.owlapi.model.SWRLAtom;
import org.semanticweb.owlapi.model.SWRLVariable;

public class HeadSplitter {

	protected static Set<Set<SWRLAtom>> splitHead(Set<SWRLAtom> head) {
		Set<Set<SWRLAtom>> headSplits = new HashSet<Set<SWRLAtom>>();

		Set<SWRLAtom> atomsWith1Var = new HashSet<SWRLAtom>();
		Set<SWRLAtom> atomsWith2Var = new HashSet<SWRLAtom>();
		Set<SWRLAtom> atomsWithoutVar = new HashSet<SWRLAtom>();

		for (SWRLAtom headAtom : head) {
			ArrayList<SWRLArgument> arguments = Srd.getArgumentsToArrayList(headAtom);
			String arg0Str = arguments.get(0).toString();
			if (arguments.size() == 1) {
				if (arg0Str.contains("Variable"))
					atomsWith1Var.add(headAtom);
				else
					atomsWithoutVar.add(headAtom);
			} else {
				String arg1Str = arguments.get(1).toString();
				if (arg0Str.contains("Variable") && arg1Str.contains("Variable"))
					atomsWith2Var.add(headAtom);
				else if (!arg0Str.contains("Variable") && !arg1Str.contains("Variable"))
					atomsWithoutVar.add(headAtom);
				else
					atomsWith1Var.add(headAtom);
			}
		}

		// Splitting atoms containing 2 variables
		for (SWRLAtom atomWith2Var : atomsWith2Var) {
			Set<SWRLAtom> headSplit = new HashSet<SWRLAtom>();
			headSplit.add(atomWith2Var);
			headSplits.add(headSplit);
		}

		// Splitting atoms with 1 variables
		Set<SWRLVariable> variables = Srd.getVariablesToSet(head);
		for (SWRLVariable variable : variables) {
			Set<SWRLAtom> headSplit = new HashSet<SWRLAtom>();
			for (SWRLAtom atomWith1Var : atomsWith1Var) {
				if (Srd.getVariablesToSet(atomWith1Var).contains(variable))
					headSplit.add(atomWith1Var);
			}
			if (!headSplit.isEmpty())
				headSplits.add(headSplit);
		}

		// Splitting atoms without variables 
		boolean modified = true;
		while (modified) {
			modified = false;
			for (SWRLAtom atomWithoutVar : atomsWithoutVar) {
				ArrayList<SWRLArgument> argsOfAtomWOVar = Srd.getArgumentsToArrayList(atomWithoutVar);
				for (Set<SWRLAtom> headSplit : headSplits) {
					Set<SWRLArgument> headSplitArgs = Srd.getArgumentsToSet(headSplit);
					for (SWRLArgument argOfAtomWOVar : argsOfAtomWOVar)
						if (headSplitArgs.contains(argOfAtomWOVar))
							modified = headSplit.add(atomWithoutVar);
				}
			}
		}

		Set<SWRLAtom> undistributedAtoms = computeUndistributedAtoms(headSplits, atomsWithoutVar);
		while (!undistributedAtoms.isEmpty()) {
			Set<SWRLAtom> headSplit = new HashSet<SWRLAtom>();
			headSplit.add(undistributedAtoms.iterator().next());
			boolean modified2 = true;
			while (modified2) {
				modified2 = false;
				for (SWRLAtom undistributedAtom : undistributedAtoms) {
					Set<SWRLArgument> headSplitArgs = Srd.getArgumentsToSet(headSplit);
					for (SWRLArgument undistributedAtomArg : undistributedAtom.getAllArguments())
						if (headSplitArgs.contains(undistributedAtomArg))
							modified2 = headSplit.add(undistributedAtom);
				}
			}
			headSplits.add(headSplit);
			undistributedAtoms = computeUndistributedAtoms(headSplits, atomsWithoutVar);
		}

		return headSplits;
	}

	private static Set<SWRLAtom> computeUndistributedAtoms(Set<Set<SWRLAtom>> headSplits, Set<SWRLAtom> atomsWithoutVar) {
		Set<SWRLAtom> undistributedAtoms = new HashSet<SWRLAtom>();
		for (SWRLAtom atomWOVar : atomsWithoutVar) {
			boolean belongsToSplit = false;
			for (Set<SWRLAtom> headSplit : headSplits)
				if (headSplit.contains(atomWOVar))
					belongsToSplit = true;
			if (!belongsToSplit)
				undistributedAtoms.add(atomWOVar);
		}
		return undistributedAtoms;
	}
}
