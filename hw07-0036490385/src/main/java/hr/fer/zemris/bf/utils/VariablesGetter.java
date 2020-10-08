package hr.fer.zemris.bf.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * A node visitor that collects variable names from an expression tree.
 *
 * @author Alen Magdić
 *
 */
public class VariablesGetter implements NodeVisitor {
	/** Variable names **/
	private Set<String> variables;

	/** Default constructor. **/
	public VariablesGetter() {
		variables = new TreeSet<>();
	}

	@Override
	public void visit(ConstantNode node) {
	}

	@Override
	public void visit(VariableNode node) {
		variables.add(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		node.getChild().accept(this);
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		for (Node child : node.getChildren()) {
			child.accept(this);
		}
	}

	/** Gets the list of variable names **/
	public List<String> getVariables() {
		return new ArrayList<String>(variables);
	}

}
