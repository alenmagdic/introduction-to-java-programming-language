package hr.fer.zemris.java.hw16.jvdraw.modeling;

import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw16.jvdraw.JVDraw;
import hr.fer.zemris.java.hw16.jvdraw.geomobjects.GeometricalObject;

/**
 * Implementacija {@link DrawingModel} sučelja za {@link JVDraw} aplikaciju.
 *
 * @author Alen Magdić
 *
 */
public class JVDrawingModel implements DrawingModel {
	/**
	 * Lista nacrtanih objekata.
	 */
	private List<GeometricalObject> objects = new ArrayList<>();
	/**
	 * Lista promatrača.
	 */
	private List<DrawingModelListener> listeners = new ArrayList<>();

	@Override
	public int getSize() {
		return objects.size();
	}

	@Override
	public GeometricalObject getObject(int index) {
		return objects.get(index);
	}

	@Override
	public void add(GeometricalObject object) {
		objects.add(object);

		for (DrawingModelListener l : listeners) {
			l.objectsAdded(this, objects.size(), objects.size());
		}
	}

	@Override
	public void remove(GeometricalObject object) {
		objects.remove(object);

		for (DrawingModelListener l : listeners) {
			l.objectsChanged(this, 0, objects.size());
		}
	}

	@Override
	public void addDrawingModelListener(DrawingModelListener l) {
		if (l == null) {
			throw new IllegalArgumentException("Listener can not be null.");
		}

		listeners.add(l);
	}

	@Override
	public void removeDrawingModelListener(DrawingModelListener l) {
		listeners.remove(l);
	}

}
