package ivankatalenic;

import javafx.scene.input.KeyCode;

public class AddShapeState implements State {

    private GUI gui;
    private GraphicalObject prototype;

    public AddShapeState(GUI gui, GraphicalObject prototype) {
        this.prototype = prototype;
        this.gui = gui;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {
        GraphicalObject o = prototype.duplicate();
        o.translate(mousePoint);
        gui.dm.addGraphicalObject(o);
    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {

    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyCode.ESCAPE.getCode()) {
            gui.currentState = gui.idleState;
        }
    }

    @Override
    public void keyReleased(int keyCode) {

    }

    @Override
    public void afterDraw(Renderer r, GraphicalObject go) {

    }

    @Override
    public void afterDraw(Renderer r) {

    }

    @Override
    public void onLeaving() {

    }

}
