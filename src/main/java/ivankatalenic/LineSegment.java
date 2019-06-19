package ivankatalenic;

import java.util.List;
import java.util.Stack;

public class LineSegment extends AbstractGraphicalObject {

    public LineSegment(Point start, Point end) {
        super();
        hotPoints = new Point[] {start, end};
        hotPointsSelected = new boolean[hotPoints.length];
        for (int i = 0; i < hotPoints.length; i++) {
            hotPointsSelected[i] = false;
        }
    }

    public LineSegment() {
        this(new Point(0.0, 0.0), new Point(50.0, 50.0));
    }

    @Override
    public double selectionDistance(Point mousePoint) {
        return GeometryUtil.distanceFromLineSegment(hotPoints[0], hotPoints[1], mousePoint);
    }

    @Override
    public String getShapeName() {
        return "Line";
    }

    @Override
    public void translate(Point delta) {
        for (int i = 0; i < hotPoints.length; i++) {
            hotPoints[i] = hotPoints[i].translate(delta);
        }
        notifyListeners();
    }

    @Override
    public Rectangle getBoundingBox() {
        double maxX = Double.MIN_VALUE, maxY = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE;
        for (Point p : hotPoints) {
            if (p.getX() > maxX) {
                maxX = p.getX();
            }
            if (p.getX() < minX) {
                minX = p.getX();
            }
            if (p.getY() > maxY) {
                maxY = p.getY();
            }
            if (p.getY() < minY) {
                minY = p.getY();
            }
        }
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    @Override
    public GraphicalObject duplicate() {
        return new LineSegment(hotPoints[0].duplicate(), hotPoints[1].duplicate());
    }

    @Override
    public void render(Renderer r) {
        r.drawLine(hotPoints[0], hotPoints[1]);
    }

    @Override
    public void load(Stack<GraphicalObject> stack, String data) {
        throw new Error("Not implemented!");
    }

    @Override
    public void save(List<String> rows) {
        throw new Error("Not implemented!");
    }

    @Override
    public void documentChange() {

    }
}
