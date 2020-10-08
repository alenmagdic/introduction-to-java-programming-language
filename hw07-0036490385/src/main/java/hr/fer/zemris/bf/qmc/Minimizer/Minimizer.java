package hr.fer.zemris.bf.qmc.Minimizer;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;
import hr.fer.zemris.bf.qmc.Mask;

/**
 * Minimizes the specified logical function using Quine–McCluskey algorithm. The
 * function is specified by its minterm set, 'dont care' set and list of
 * variables. The class provides methods for getting a tree of the minimal
 * expression and also a method for getting the minimal expression in the form
 * of string (for example A AND NOT B OR NOT A AND B).
 *
 * @author Alen Magdić
 *
 */
public class Minimizer {
	/** Set of minterms. **/
	private Set<Integer> mintermSet;
	/** Set of 'dont cares'. **/
	private Set<Integer> dontCareSet;
	/** List of variables **/
	private List<String> variables;
	/** List of minimal forms **/
	private List<Set<Mask>> minimalForms;
	/** Logger **/
	private static final Logger LOG = Logger.getLogger("hr.fer.zemris.bf.qmc");

	/**
	 * Constructor.
	 *
	 * @param mintermSet
	 *            set of minterms
	 * @param dontCareSet
	 *            set of 'dont cares'
	 * @param variables
	 *            list of variables
	 */
	public Minimizer(Set<Integer> mintermSet, Set<Integer> dontCareSet, List<String> variables) {
		validateConstructorArguments(mintermSet, dontCareSet, variables);

		this.mintermSet = new TreeSet<>(mintermSet);
		this.dontCareSet = new TreeSet<>(dontCareSet);
		this.variables = new ArrayList<>(variables);

		Set<Mask> primCover = findPrimaryImplicants();
		minimalForms = chooseMinimalCover(primCover);
	}

	/**
	 * An implementation of the second step of the minimization process. Chooses
	 * the minimal cover and returns all results that are equally minimal.
	 *
	 * @param primCover
	 *            set of primary implicants
	 * @return list of mask sets
	 */
	private List<Set<Mask>> chooseMinimalCover(Set<Mask> primCover) {
		Mask[] implicants = primCover.toArray(new Mask[primCover.size()]);
		Integer[] minterms = mintermSet.toArray(new Integer[mintermSet.size()]);

		Map<Integer, Integer> mintermToColumnMap = new HashMap<>();
		for (int i = 0; i < minterms.length; i++) {
			Integer index = minterms[i];
			mintermToColumnMap.put(index, i);
		}

		boolean[][] table = buildCoverTable(implicants, minterms, mintermToColumnMap);
		boolean[] coveredMinterms = new boolean[minterms.length];

		Set<Mask> importantSet = selectImportantPrimaryImplicants(implicants, mintermToColumnMap, table,
				coveredMinterms);
		writeImportantPrimImplToLog(importantSet);
		List<Set<BitSet>> pFunction = buildPFunction(table, coveredMinterms);

		writePFunctionToLog(pFunction, "p function is: ");

		Set<BitSet> minset = findMinimalSet(pFunction);

		List<Set<Mask>> minimalForms = new ArrayList<>();
		if (minset.isEmpty()) {
			minimalForms.add(importantSet);
		}

		for (BitSet bs : minset) {
			Set<Mask> set = new LinkedHashSet<>(importantSet);
			bs.stream().forEach(i -> set.add(implicants[i]));
			minimalForms.add(set);
		}

		writeMinimalFormsToLog(minimalForms);
		return minimalForms;
	}

	/**
	 * Writes the results of minimizitation to the log, i.e. writes the minimal
	 * forms of the function specified in constructor .
	 *
	 * @param minimalForms
	 *            minimal forms of the function specified in constructor
	 */
	private void writeMinimalFormsToLog(List<Set<Mask>> minimalForms) {
		if (!LOG.isLoggable(Level.FINE)) {
			return;
		}
		LOG.log(Level.FINE, "");
		LOG.log(Level.FINE, "Minimal forms: ");
		for (int i = 0, n = minimalForms.size(); i < n; i++) {
			LOG.log(Level.FINE, String.format("%d. %s", i + 1, minimalForms.get(i).toString()));
		}

	}

	/**
	 * Writes the p-function to the log.
	 *
	 * @param pFunction
	 *            p-function
	 * @param message
	 *            a message that is to be written to the log before writing the
	 *            p-function
	 */
	private void writePFunctionToLog(List<Set<BitSet>> pFunction, String message) {
		if (!LOG.isLoggable(Level.FINER)) {
			return;
		}
		LOG.log(Level.FINER, "");
		LOG.log(Level.FINER, message);
		LOG.log(Level.FINER, pFunction.toString());
	}

	/**
	 * Writes the results of the second step of minimization process to the log.
	 *
	 * @param minset
	 *            results of the second step of minimization process
	 *
	 */
	private void writeMinsetToLog(Set<BitSet> minset) {
		if (!LOG.isLoggable(Level.FINER)) {
			return;
		}
		LOG.log(Level.FINER, "");
		LOG.log(Level.FINER, "Minimal covers need: ");
		LOG.log(Level.FINER, minset.toString());
	}

	/**
	 * Writes the important primary implicants to log.
	 *
	 * @param importantSet
	 *            set of important primary implicants
	 */
	private void writeImportantPrimImplToLog(Set<Mask> importantSet) {
		if (!LOG.isLoggable(Level.FINE)) {
			return;
		}
		LOG.log(Level.FINE, "");
		if (importantSet.size() == 0) {
			LOG.log(Level.FINE, "There is no any important primary implicant.");
		} else {
			LOG.log(Level.FINE, "Important primary implicants:");
			for (Mask mask : importantSet) {
				LOG.log(Level.FINE, mask.toString());
			}
		}
	}

	/**
	 * Executes the second step of minimization process, converting the
	 * specified p-function to sum of products and returning only the
	 * combinitions with minimal number of p-variables.
	 *
	 * @param pFunction
	 *            p-function
	 * @return set of products, containing only the minimal number of
	 *         p-variables
	 */
	private Set<BitSet> findMinimalSet(List<Set<BitSet>> pFunction) {
		if (pFunction.size() == 0) {
			return new LinkedHashSet<>();
		}

		Set<BitSet> tempSet = new LinkedHashSet<>();
		List<Set<BitSet>> f = new ArrayList<>(pFunction);

		while (f.size() > 1) {
			for (BitSet bs1 : f.get(0)) {
				for (BitSet bs2 : f.get(1)) {
					BitSet product = new BitSet();
					product.or(bs1);
					product.or(bs2);
					tempSet.add(product);
				}
			}

			f.remove(0);
			f.remove(0);
			f.add(0, new LinkedHashSet<>(tempSet));
			tempSet.clear();
		}

		writePFunctionToLog(f, "p function after conversion to sum of products: ");

		int minNumOfPrimImpl = Collections.min(f.get(0), (bs1, bs2) -> bs1.cardinality() - bs2.cardinality())
				.cardinality();

		Set<BitSet> resultSet = new LinkedHashSet<>();
		for (BitSet bs : f.get(0)) {
			if (bs.cardinality() == minNumOfPrimImpl) {
				resultSet.add(bs);
			}
		}

		writeMinsetToLog(resultSet);

		return resultSet;
	}

	/**
	 * Gets the minimal forms as expression trees.
	 *
	 * @return miminal forms as expression trees
	 */
	public List<Node> getMinimalFormsAsExpressions() {
		List<Node> expressions = new ArrayList<>();

		for (Set<Mask> form : minimalForms) {
			if (minimalForms.size() == 1 && form.size() == 0) {
				expressions.add(new ConstantNode(false));
				return expressions;
			}

			List<Node> childrenOfOr = new ArrayList<>();
			for (Mask mask : form) {
				List<Node> childrenOfAnd = new ArrayList<>();
				for (int i = 0, n = mask.size(); i < n; i++) {
					if (mask.getValueAt(i) == 1) {
						childrenOfAnd.add(new VariableNode(variables.get(i)));
					} else if (mask.getValueAt(i) == 0) {
						childrenOfAnd.add(new UnaryOperatorNode("NOT", new VariableNode(variables.get(i)), op -> !op));
					}
				}

				if (childrenOfAnd.size() == 0) {
					expressions.add(new ConstantNode(true));
					return expressions;
				} else if (childrenOfAnd.size() == 1) {
					childrenOfOr.add(childrenOfAnd.get(0));
				} else {
					childrenOfOr.add(new BinaryOperatorNode("AND", childrenOfAnd, (op1, op2) -> op1 && op2));
				}
			}

			if (childrenOfOr.size() == 1) {
				expressions.add(childrenOfOr.get(0));
			} else {
				expressions.add(new BinaryOperatorNode("OR", childrenOfOr, (op1, op2) -> op1 || op2));
			}
		}
		return expressions;
	}

	/**
	 * Gets the minimal forms as strings.
	 *
	 * @return miminal forms as strings
	 */
	public List<String> getMinimalFormsAsString() {
		List<Node> expressions = getMinimalFormsAsExpressions();
		List<String> stringExpressions = new ArrayList<>();
		for (Node expr : expressions) {
			stringExpressions.add(buildFormAsString(expr));
		}
		return stringExpressions;
	}

	/**
	 * Builds a single minimal form as a string.
	 *
	 * @param expr
	 *            a tree of a minimal form
	 * @return a single miminal form as a string
	 */
	private String buildFormAsString(Node expr) {
		if (expr instanceof BinaryOperatorNode) {
			StringBuilder stringB = new StringBuilder();
			BinaryOperatorNode binOp = (BinaryOperatorNode) expr;
			List<Node> children = binOp.getChildren();

			for (int i = 0, n = children.size(); i < n; i++) {
				stringB.append(buildFormAsString(children.get(i)));
				if (i < n - 1) {
					stringB.append(binOp.getName() + " ");
				}
			}
			return stringB.toString();
		} else if (expr instanceof UnaryOperatorNode) {
			UnaryOperatorNode unOp = (UnaryOperatorNode) expr;
			return unOp.getName() + " " + buildFormAsString(unOp.getChild()) + " ";
		} else if (expr instanceof VariableNode) {
			return ((VariableNode) expr).getName() + " ";
		} else {
			return Boolean.toString(((ConstantNode) expr).getValue()) + " ";
		}
	}

	/**
	 * Builds the p-function.
	 *
	 * @param table
	 *            table of implicants and minterms
	 * @param coveredMinterms
	 *            array showing which minterms have been covered
	 * @return p-function
	 */
	private List<Set<BitSet>> buildPFunction(boolean[][] table, boolean[] coveredMinterms) {

		List<Set<BitSet>> list = new ArrayList<>();
		for (int j = 0; j < coveredMinterms.length; j++) {

			if (!coveredMinterms[j]) {
				Set<BitSet> newSet = new LinkedHashSet<>();
				for (int i = 0; i < table.length; i++) {
					if (table[i][j]) {
						BitSet newBitset = new BitSet();
						newBitset.set(i);
						newSet.add(newBitset);
					}
				}
				list.add(newSet);
			}
		}
		return list;
	}

	/**
	 * Selects the important primary implicants.
	 *
	 * @param implicants
	 *            mask array of implicants
	 * @param mintermToColumnMap
	 *            map that associates a minterm with the index of its column in
	 *            the table
	 * @param table
	 *            table of implicants and minterms
	 * @param coveredMinterms
	 *            array showing which minterms have been covered
	 * @return set of the important primary implicants
	 */
	private Set<Mask> selectImportantPrimaryImplicants(Mask[] implicants, Map<Integer, Integer> mintermToColumnMap,
			boolean[][] table, boolean[] coveredMinterms) {
		Set<Mask> importantPrimImpls = new LinkedHashSet<>();

		for (int j = 0; j < coveredMinterms.length; j++) {
			// number of times that a minterm has been covered
			int numOfCovers = 0;
			int indexOfImpl = 0;
			for (int i = 0; i < implicants.length; i++) {
				if (table[i][j]) {
					numOfCovers++;
					indexOfImpl = i;
					if (numOfCovers == 2) {
						break;
					}
				}
			}

			if (numOfCovers == 1) {
				importantPrimImpls.add(implicants[indexOfImpl]);
				for (int k = 0; k < coveredMinterms.length; k++) {
					if (table[indexOfImpl][k]) {
						coveredMinterms[k] = true;
					}
				}
			}

		}
		return importantPrimImpls;
	}

	/**
	 * Builds the cover table.
	 *
	 * @param implicants
	 *            mask array of implicants
	 * @param minterms
	 *            integer array of minterms
	 * @param mintermToColumnMap
	 *            map that associates a minterm with the index of its column in
	 *            the table
	 * @return
	 */
	private boolean[][] buildCoverTable(Mask[] implicants, Integer[] minterms,
			Map<Integer, Integer> mintermToColumnMap) {
		boolean[][] table = new boolean[implicants.length][minterms.length];

		for (int i = 0; i < implicants.length; i++) {
			for (int minterm : implicants[i].getIndexes()) {
				Integer j = mintermToColumnMap.get(minterm);
				if (j != null) {
					table[i][j] = true;
				}
			}
		}
		return table;
	}

	/**
	 * Finds the primary implicants.
	 *
	 * @return set of primary implicants
	 */
	private Set<Mask> findPrimaryImplicants() {
		Set<Mask> primaryImplicants = new LinkedHashSet<>();
		List<Set<Mask>> currentColumn = createFirstColumn();

		do {
			List<Set<Mask>> newColumn = new ArrayList<>();
			for (int groupIndex = 0, n = currentColumn.size(); groupIndex < n - 1; groupIndex++) {
				Set<Mask> newGroup = new LinkedHashSet<>();

				for (Mask mask1 : currentColumn.get(groupIndex)) {
					for (Mask mask2 : currentColumn.get(groupIndex + 1)) {
						Optional<Mask> comb = mask1.combineWith(mask2);

						if (comb.isPresent()) {
							mask1.setCombined(true);
							mask2.setCombined(true);
							newGroup.add(comb.get());
						}
					}
				}

				if (newGroup.size() > 0) {
					newColumn.add(newGroup);
				}
			}

			Set<Mask> primaryImplFound = extractPrimaryImplicants(currentColumn);
			primaryImplicants.addAll(primaryImplFound);

			writeColumnToLog(currentColumn);
			writePrimImplToLog(primaryImplFound);

			currentColumn = newColumn;
		} while (currentColumn.size() > 0);

		return primaryImplicants;
	}

	/**
	 * Writes the primary implicants to the log.
	 *
	 * @param primaryImplFound
	 *            set of primary implicants found
	 */
	private void writePrimImplToLog(Set<Mask> primaryImplFound) {
		if (primaryImplFound.size() == 0 || !LOG.isLoggable(Level.FINEST)) {
			return;
		}
		for (Mask mask : primaryImplFound) {
			LOG.log(Level.FINEST, "Primary implicant found: " + mask);
		}
		LOG.log(Level.FINEST, "");
	}

	/**
	 * Writes a single column to the log.
	 *
	 * @param column
	 *            a column that is to be written to the log
	 */
	private void writeColumnToLog(List<Set<Mask>> column) {
		if (!LOG.isLoggable(Level.FINER)) {
			return;
		}
		LOG.log(Level.FINER, "Table row: ");
		LOG.log(Level.FINER, "=================================");

		boolean isFirstGroup = true;
		for (Set<Mask> group : column) {
			if (!isFirstGroup) {
				LOG.log(Level.FINER, "-------------------------------");
			}
			for (Mask mask : group) {
				LOG.log(Level.FINER, mask.toString());
			}
			isFirstGroup = false;
		}
		LOG.log(Level.FINER, "");
	}

	/**
	 * Extracts the primary implicants from the specified column.
	 *
	 * @param currentColumn
	 *            a column from the first step of minimization process
	 * @return set of primary implicants
	 */
	private Set<Mask> extractPrimaryImplicants(List<Set<Mask>> currentColumn) {
		Set<Mask> primImplicants = new LinkedHashSet<>();
		for (Set<Mask> set : currentColumn) {
			for (Mask mask : set) {
				if (!mask.isCombined() && !mask.isDontCare()) {
					primImplicants.add(mask);
				}
			}
		}
		return primImplicants;
	}

	/**
	 * Creates the first column for the first step of minimization process.
	 *
	 * @return the first column, i.e. a list of mask sets
	 */
	private List<Set<Mask>> createFirstColumn() {
		List<Set<Mask>> groups = new ArrayList<>();
		int numOfVars = variables.size();
		for (int i = 0; i <= numOfVars; i++) {
			groups.add(new LinkedHashSet<>());
		}

		for (Integer index : mintermSet) {
			Mask mask = new Mask(index, numOfVars, false);
			groups.get(mask.countOfOnes()).add(mask);
		}
		for (Integer index : dontCareSet) {
			Mask mask = new Mask(index, numOfVars, true);
			groups.get(mask.countOfOnes()).add(mask);
		}
		return groups;
	}

	/**
	 * Validates constructor arguments. If the arguments are not legal, throws
	 * IllegalArgumentException.
	 *
	 * @param mintermSet
	 *            set of minterms
	 * @param dontCareSet
	 *            set of 'dont cares'
	 * @param variables
	 *            list of variables
	 */
	private void validateConstructorArguments(Set<Integer> mintermSet, Set<Integer> dontCareSet,
			List<String> variables) {
		if (mintermSet == null || dontCareSet == null || variables == null) {
			throw new IllegalArgumentException("null is not a legal argument");
		}

		int maxValidIndex = (int) Math.pow(2, variables.size()) - 1;
		for (Integer index : mintermSet) {
			if (dontCareSet.contains(index)) {
				throw new IllegalArgumentException("There are some overlaps in minterm and 'dont care' set.");
			}
			if (index > maxValidIndex || index < 0) {
				throw new IllegalArgumentException("There are some invalid indices in minterm set.");
			}
		}

		for (Integer index : dontCareSet) {
			if (index > maxValidIndex || index < 0) {
				throw new IllegalArgumentException("There are some invalid indices in 'dont care' set.");
			}
		}
	}

	/**
	 * Gets the list of minimal forms.
	 *
	 * @return list of minimal forms
	 */
	public List<Set<Mask>> getMinimalForms() {
		return minimalForms;
	}

}
