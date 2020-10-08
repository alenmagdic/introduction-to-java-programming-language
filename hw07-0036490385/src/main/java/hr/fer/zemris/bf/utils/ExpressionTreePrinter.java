package hr.fer.zemris.bf.utils;

import hr.fer.zemris.bf.model.BinaryOperatorNode;
import hr.fer.zemris.bf.model.ConstantNode;
import hr.fer.zemris.bf.model.Node;
import hr.fer.zemris.bf.model.NodeVisitor;
import hr.fer.zemris.bf.model.UnaryOperatorNode;
import hr.fer.zemris.bf.model.VariableNode;

/**
 * A node visitor that prints a tree.
 *
 * @author Alen Magdić
 *
 */
public class ExpressionTreePrinter implements NodeVisitor {
	/** Current indetation **/
	int indentation;

	@Override
	public void visit(ConstantNode node) {
		System.out.printf("%" + (indentation != 0 ? indentation : "") + "s", "");
		System.out.println(node.getValue() == true ? 1 : 0);
	}

	@Override
	public void visit(VariableNode node) {
		System.out.printf("%" + (indentation != 0 ? indentation : "") + "s", "");
		System.out.println(node.getName());
	}

	@Override
	public void visit(UnaryOperatorNode node) {
		System.out.printf("%" + (indentation != 0 ? indentation : "") + "s", "");
		System.out.println(node.getName());

		indentation += 2;
		node.getChild().accept(this);
		indentation -= 2;
	}

	@Override
	public void visit(BinaryOperatorNode node) {
		System.out.printf("%" + (indentation != 0 ? indentation : "") + "s", "");
		System.out.println(node.getName());

		indentation += 2;
		for (Node child : node.getChildren()) {
			child.accept(this);
		}
		indentation -= 2;
	}

}
