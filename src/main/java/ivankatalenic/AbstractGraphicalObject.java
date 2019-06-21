package ivankatalenic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractGraphicalObject implements GraphicalObject {

	protected Point[] hotPoints;
	protected boolean[] hotPointsSelected;
	private boolean selected;
	private List<GraphicalObjectListener> listeners = new ArrayList<>();

	public AbstractGraphicalObject() {
	}

	protected void notifyListeners() {
		for (GraphicalObjectListener l : listeners) {
			l.graphicalObjectChanged(this);
		}
	}

	private void notifySelectionListeners() {
		for (GraphicalObjectListener l : listeners) {
			l.graphicalObjectSelectionChanged(this);
		}
	}

	@Override
	public boolean isSelected() {
		return selected;
	}

	@Override
	public void setSelected(boolean selected) {
		this.selected = selected;
		notifySelectionListeners();
	}

	@Override
	public int getNumberOfHotPoints() {
		return hotPoints.length;
	}

	@Override
	public Point getHotPoint(int index) {
		return hotPoints[index];
	}

	@Override
	public void setHotPoint(int index, Point point) {
		hotPoints[index] = point;
		notifyListeners();
	}

	@Override
	public boolean isHotPointSelected(int index) {
		return hotPointsSelected[index];
	}

	@Override
	public void setHotPointSelected(int index, boolean selected) {
		hotPointsSelected[index] = selected;
	}

	@Override
	public double getHotPointDistance(int index, Point mousePoint) {
		return GeometryUtil.distanceFromPoint(hotPoints[index], mousePoint);
	}

	@Override
	public void addGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.add(l);
	}

	@Override
	public void removeGraphicalObjectListener(GraphicalObjectListener l) {
		listeners.remove(l);
	}

	@Override
	public String getShapeID() {
		return getShapeName().toUpperCase();
	}

	@Override
	public void save(List<String> rows) {
		StringBuilder sb = new StringBuilder("@");
		sb.append(getShapeID());
		for (int i = 0; i < getNumberOfHotPoints(); i++) {
			sb.append(" ");
			sb.append(hotPoints[i].getX());
			sb.append(" ");
			sb.append(hotPoints[i].getY());
		}
		rows.add(sb.toString());
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		for (int i = 0; i < hotPoints.length; i++) {
			String[] params = data.split(" ");
			Point p = new Point(Double.parseDouble(params[2 * i]),
					Double.parseDouble(params[2 * i + 1]));
			hotPoints[i] = p;
		}
		stack.push(this);
	}

}
