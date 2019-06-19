package ivankatalenic;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class AbstractGraphicalObject implements GraphicalObject, DocumentModelListener {

    private boolean selected;

    private static AtomicInteger globalId = new AtomicInteger();
    private int id;

    protected Point[] hotPoints;
    protected boolean[] hotPointsSelected;

    private List<GraphicalObjectListener> listeners = new ArrayList<>();

    public AbstractGraphicalObject() {
        id = globalId.getAndIncrement();
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
//        notifySelectionListeners();
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
        return getShapeName() + Integer.toString(id);
    }

}
