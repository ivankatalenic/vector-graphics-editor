package ivankatalenic;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MyRenderer extends Canvas implements Renderer, DocumentModelListener {

    private GUI gui;

    private final Color backgroundFill = Color.WHITE;

    public MyRenderer(GUI gui, double width, double height) {
        super(width, height);
        this.gui = gui;
        gui.dm.addDocumentModelListener(this);
        clearCanvas();
    }

    @Override
    public void drawLine(Point s, Point e) {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setStroke(Color.BLUE);
        gc.strokeLine(s.getX(), s.getY(),
                e.getX(), e.getY());
    }

    @Override
    public void fillPolygon(Point[] points) {
        int nPoints = points.length;
        double[] xPoints = new double[nPoints];
        double[] yPoints = new double[nPoints];
        for (int i = 0; i < nPoints; i++) {
            xPoints[i] = points[i].getX();
            yPoints[i] = points[i].getY();
        }
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(Color.BLACK);
        gc.fillPolygon(xPoints, yPoints, nPoints);
    }

    private void clearCanvas() {
        GraphicsContext gc = this.getGraphicsContext2D();
        gc.setFill(backgroundFill);
        gc.fillRect(0.0, 0.0, getWidth(), getHeight());
    }

    @Override
    public void documentChange() {
        clearCanvas();
        for (GraphicalObject obj : gui.dm.list()) {
            obj.render(this);
            gui.getCurrentState().afterDraw(this,obj);
        }
        gui.getCurrentState().afterDraw(this);
    }

}
