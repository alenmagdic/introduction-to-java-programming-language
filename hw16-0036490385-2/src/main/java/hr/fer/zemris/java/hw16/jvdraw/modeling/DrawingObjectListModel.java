package hr.fer.zemris.java.hw16.jvdraw.modeling;

import javax.swing.AbstractListModel;

import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Model liste crtanih objekata. Model podatke preuzima iz zadanog
 * {@link DrawingModel} modela.
 *
 * @author Alen Magdić
 *
 */
public class DrawingObjectListModel extends AbstractListModel<GeometricalObject> {
	private static final long serialVersionUID = 1L;
	/**
	 * Model za crtanje.
	 */
	private DrawingModel drawingModel;

	/**
	 * Konstruktor.
	 *
	 * @param drawingModel
	 *            model u kojem su pohranjeni nacrtani objekti
	 */
	public DrawingObjectListModel(DrawingModel drawingModel) {
		this.drawingModel = drawingModel;
		drawingModel.addDrawingModelListener(new DrawingModelListener() {

			@Override
			public void objectsRemoved(DrawingModel source, int index0, int index1) {
				fireIntervalRemoved(source, index0, index1);
			}

			@Override
			public void objectsChanged(DrawingModel source, int index0, int index1) {
				fireContentsChanged(source, index0, index1);
			}

			@Override
			public void objectsAdded(DrawingModel source, int index0, int index1) {
				fireIntervalAdded(source, index0, index1);
			}
		});
	}

	@Override
	public int getSize() {
		return drawingModel.getSize();
	}

	@Override
	public GeometricalObject getElementAt(int index) {
		return drawingModel.getObject(index);
	}

}
