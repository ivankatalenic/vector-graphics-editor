package ivankatalenic;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.List;

public class GUI {

    private final double CANVAS_WIDTH = 800.0;
    private final double CANVAS_HEIGHT = 600.0;

    private List<GraphicalObject> initialObjects;
    public MyRenderer renderer;
    public DocumentModel dm;

    public State currentState;
    public State idleState;

    public GUI(Stage stage, List<GraphicalObject> objects) {
        initialObjects = objects;
        dm = new DocumentModel();
        idleState = new IdleState(this);
        currentState = idleState;

        renderer = new MyRenderer(this, CANVAS_WIDTH, CANVAS_HEIGHT);

        Button loadButton = new Button("Load");
        Button saveButton = new Button("Save");
        Button svgButton = new Button("SVG Export");
        Button selectButton = new Button("Select");
        Button deleteButton = new Button("Delete");

        HBox toolBar = new HBox();
        toolBar.setPadding(new Insets(5.0));
        toolBar.setSpacing(5);

        toolBar.getChildren().addAll(loadButton, saveButton,
                svgButton,
                selectButton, deleteButton);

        FlowPane objectPane = new FlowPane();
        objectPane.setPadding(new Insets(5.0));
        objectPane.setVgap(5);
        objectPane.setHgap(5);
        objectPane.setPrefWrapLength(CANVAS_WIDTH);

        for (GraphicalObject o : initialObjects) {
            Button but = new Button(o.getShapeName());
            but.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    currentState = new AddShapeState(GUI.this, o);
                }
            });
            objectPane.getChildren().add(but);
        }

        dm.notifyListeners();

        BorderPane pane = new BorderPane();
        pane.setTop(toolBar);
        pane.setCenter(renderer);
        pane.setBottom(objectPane);

        Scene scene = new Scene(pane);
        stage.setResizable(false);
        stage.setScene(scene);
        stage.setTitle("Vector Graphics Editor");
        stage.show();

        renderer.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                renderer.requestFocus();
            }
        });
        renderer.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                currentState.keyPressed(event.getCode().getCode());
            }
        });
        renderer.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                currentState.keyReleased(event.getCode().getCode());
            }
        });
        renderer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentState.mouseDown(new Point(event.getX(), event.getY()),
                        event.isShiftDown(), event.isControlDown());
            }
        });
        renderer.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentState.mouseUp(new Point(event.getX(), event.getY()),
                        event.isShiftDown(), event.isControlDown());
            }
        });
        renderer.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                currentState.mouseDragged(new Point(event.getX(), event.getY()));
            }
        });

    }

    public State getCurrentState() {
        return currentState;
    }

}
