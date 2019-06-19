package ivankatalenic;

import javafx.scene.input.KeyCode;

public class IdleState implements State {

    private GUI gui;

    public IdleState(GUI gui) {
        this.gui = gui;
    }

    @Override
    public void mouseDown(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseUp(Point mousePoint, boolean shiftDown, boolean ctrlDown) {

    }

    @Override
    public void mouseDragged(Point mousePoint) {

    }

    @Override
    public void keyPressed(int keyCode) {
        if (keyCode == KeyCode.CONTROL.getCode()) {
            gui.currentState = new SelectShapeState(gui);
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
