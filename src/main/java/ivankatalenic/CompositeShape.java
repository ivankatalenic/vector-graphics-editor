package ivankatalenic;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class CompositeShape extends AbstractGraphicalObject {

	private List<GraphicalObject> children;

	public CompositeShape(List<GraphicalObject> children) {
		super();
		hotPoints = new Point[0];
		hotPointsSelected = new boolean[0];
		this.children = children;
	}

	public List<GraphicalObject> getChildren() {
		return children;
	}

	public void setChildren(List<GraphicalObject> children) {
		this.children = children;
	}

	@Override
	public void translate(Point delta) {
		for (GraphicalObject o : children) {
			o.translate(delta);
		}
		notifyListeners();
	}

	@Override
	public Rectangle getBoundingBox() {
		double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
		double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
		for (GraphicalObject o : children) {
			Rectangle rect = o.getBoundingBox();
			minX = rect.getX() < minX ? rect.getX() : minX;
			minY = rect.getY() < minY ? rect.getY() : minY;
			maxX = rect.getX() + rect.getWidth() > maxX ? rect.getX() + rect.getWidth() : maxX;
			maxY = rect.getY() + rect.getHeight() > maxY ? rect.getY() + rect.getHeight() : maxY;
		}
		return new Rectangle(minX, minY, maxX - minX, maxY - minY);
	}

	@Override
	public double selectionDistance(Point mousePoint) {
		double minDistance = Double.MAX_VALUE;
		for (GraphicalObject o : children) {
			double distance = o.selectionDistance(mousePoint);
			if (distance < minDistance) {
				minDistance = distance;
			}
		}
		return minDistance;
	}

	@Override
	public void render(Renderer r) {
		for (GraphicalObject o : children) {
			o.render(r);
		}
	}

	@Override
	public String getShapeName() {
		return "Composite";
	}

	@Override
	public GraphicalObject duplicate() {
		List<GraphicalObject> duplicates = new ArrayList<>();
		if (children != null) {
			for (GraphicalObject o : children) {
				duplicates.add(o.duplicate());
			}
		}
		return new CompositeShape(duplicates);
	}

	@Override
	public void load(Stack<GraphicalObject> stack, String data) {
		int count = Integer.parseInt(data);
		List<GraphicalObject> children = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			children.add(stack.pop());
		}
		this.children = children;
		stack.push(this);
	}

	@Override
	public void save(List<String> rows) {
		for (GraphicalObject o : children) {
			o.save(rows);
		}
		rows.add("@" + getShapeID() + " " + children.size());
	}
}
