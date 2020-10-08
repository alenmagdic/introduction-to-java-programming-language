package hr.fer.zemris.java.hw01;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import hr.fer.zemris.java.hw01.UniqueNumbers.TreeNode;

public class UniqueNumbersTest {

    @Test
    public void addingNode() {
	TreeNode head = null;
	head = UniqueNumbers.addNode(head, 42);
	head = UniqueNumbers.addNode(head, 76);
	head = UniqueNumbers.addNode(head, 21);
	head = UniqueNumbers.addNode(head, 76);
	head = UniqueNumbers.addNode(head, 35);

	assertEquals(head.value, 42);
	assertEquals(head.left.value, 21);
	assertEquals(head.left.right.value, 35);
	assertEquals(head.right.value, 76);
    }

    @Test
    public void treeSize() {
	TreeNode head = null;
	assertEquals(UniqueNumbers.treeSize(head), 0);

	head = UniqueNumbers.addNode(head, 42);
	assertEquals(UniqueNumbers.treeSize(head), 1);

	head = UniqueNumbers.addNode(head, 76);
	assertEquals(UniqueNumbers.treeSize(head), 2);

	head = UniqueNumbers.addNode(head, 21);
	assertEquals(UniqueNumbers.treeSize(head), 3);

	head = UniqueNumbers.addNode(head, 76);
	assertEquals(UniqueNumbers.treeSize(head), 3);

	head = UniqueNumbers.addNode(head, 35);
	assertEquals(UniqueNumbers.treeSize(head), 4);
    }

    @Test
    public void containsValue() {
	TreeNode head = null;
	head = UniqueNumbers.addNode(head, 42);
	head = UniqueNumbers.addNode(head, 76);
	head = UniqueNumbers.addNode(head, 21);
	head = UniqueNumbers.addNode(head, 76);
	head = UniqueNumbers.addNode(head, 35);

	assertEquals(UniqueNumbers.containsValue(head, 42), true);
	assertEquals(UniqueNumbers.containsValue(head, 76), true);
	assertEquals(UniqueNumbers.containsValue(head, 21), true);
	assertEquals(UniqueNumbers.containsValue(head, 35), true);
	assertEquals(UniqueNumbers.containsValue(head, 25), false);
    }

}
