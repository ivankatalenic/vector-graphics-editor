package ivankatalenic;

import java.util.List;
import java.util.Stack;

public interface GraphicalObject {

	// Support for an object editing
	boolean isSelected();

	void setSelected(boolean selected);

	int getNumberOfHotPoints();

	Point getHotPoint(int index);

	void setHotPoint(int index, Point point);

	boolean isHotPointSelected(int index);

	void setHotPointSelected(int index, boolean selected);

	double getHotPointDistance(int index, Point mousePoint);

	// Geometric operation on an object
	void translate(Point delta);

	Rectangle getBoundingBox();

	double selectionDistance(Point mousePoint);

	// Support for painting
	void render(Renderer r);

	// Observer for notifying about change
	void addGraphicalObjectListener(GraphicalObjectListener l);

	void removeGraphicalObjectListener(GraphicalObjectListener l);

	// Support for prototype design pattern (tool bar, object creatino, ...)
	String getShapeName();

	GraphicalObject duplicate();

	// Support for loading and saving an object
	String getShapeID();

	void load(Stack<GraphicalObject> stack, String data);

	void save(List<String> rows);

}
