package hr.fer.zemris.java.gui.prim;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;

import org.junit.Assert;
import org.junit.Test;

public class PrimListModelTest {

	@Test
	public void testInitialListContent() {
		PrimListModel model = new PrimListModel();
		Assert.assertEquals(1, model.getSize());
		Assert.assertEquals(Integer.valueOf(1), model.getElementAt(0));
	}

	@Test
	public void testAddingNextNumber() {
		PrimListModel model = new PrimListModel();
		model.next();
		Assert.assertEquals(2, model.getSize());
		Assert.assertEquals(Integer.valueOf(1), model.getElementAt(0));
		Assert.assertEquals(Integer.valueOf(2), model.getElementAt(1));
	}

	@Test
	public void testAddingNextNumbers() {
		PrimListModel model = new PrimListModel();
		model.next();
		model.next();
		model.next();
		model.next();
		Assert.assertEquals(5, model.getSize());
		Assert.assertEquals(Integer.valueOf(1), model.getElementAt(0));
		Assert.assertEquals(Integer.valueOf(2), model.getElementAt(1));
		Assert.assertEquals(Integer.valueOf(3), model.getElementAt(2));
		Assert.assertEquals(Integer.valueOf(5), model.getElementAt(3));
		Assert.assertEquals(Integer.valueOf(7), model.getElementAt(4));
	}

	@Test
	public void testListenerNotificationWhenAdded() {
		PrimListModel model = new PrimListModel();
		Listener l = new Listener();
		model.addListDataListener(l);
		model.next();
		Assert.assertEquals(true, l.isWasNotified());
	}

	@Test
	public void testListenerNotificationAfterRemoved() {
		PrimListModel model = new PrimListModel();
		Listener l = new Listener();
		model.addListDataListener(l);
		model.removeListDataListener(l);
		model.next();
		Assert.assertEquals(false, l.isWasNotified());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void gettingElementFromIndexOutOfBounds() {
		PrimListModel model = new PrimListModel();
		model.getElementAt(2);
	}

	private static class Listener implements ListDataListener {

		private boolean wasNotified = false;

		@Override
		public void intervalRemoved(ListDataEvent e) {
		}

		@Override
		public void intervalAdded(ListDataEvent e) {
			wasNotified = true;
		}

		@Override
		public void contentsChanged(ListDataEvent e) {

		}

		public boolean isWasNotified() {
			return wasNotified;
		}

	}
}
