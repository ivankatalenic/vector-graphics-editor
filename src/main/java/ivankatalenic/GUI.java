package ivankatalenic;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.*;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

public class GUI {

	private final double CANVAS_WIDTH = 800.0;
	private final double CANVAS_HEIGHT = 600.0;

	public CanvasRenderer renderer;
	public DocumentModel dm;

	public State currentState;
	public State idleState;

	private Map<String, GraphicalObject> prototypeMap;

	private List<GraphicalObject> initialObjects;

	public GUI(Stage stage, List<GraphicalObject> objects) {
		initialObjects = objects;
		dm = new DocumentModel();
		idleState = new IdleState(this);
		currentState = idleState;

		prototypeMap = new HashMap<>(initialObjects.size() + 1);
		for (GraphicalObject o : initialObjects) {
			prototypeMap.put(o.getShapeID(), o);
		}
		CompositeShape c = new CompositeShape(null);
		prototypeMap.put(c.getShapeID(), c);

		renderer = new CanvasRenderer(this, CANVAS_WIDTH, CANVAS_HEIGHT);

		Button loadButton = new Button("Load");
		loadButton.setFocusTraversable(false);
		loadButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Open a saved drawing");
				File selectedFile = fileChooser.showOpenDialog(stage);
				String message = "Your drawing was loaded successfully!";
				if (selectedFile != null) {
					try (BufferedReader reader = Files.newBufferedReader(selectedFile.toPath())) {
						List<String> lines = new ArrayList<>();
						Stack<GraphicalObject> objectStack = new Stack<>();
						String line = null;
						while ((line = reader.readLine()) != null) {
							if (line.length() <= 1) {
								continue;
							}
							String[] params = line.split(" ", 2);
							String id = params[0].substring(1);
							GraphicalObject o = prototypeMap.get(id).duplicate();
							o.load(objectStack, params[1]);
						}
						GraphicalObject[] objs = objectStack.toArray(new GraphicalObject[0]);
						for (GraphicalObject o : objs) {
							dm.addGraphicalObject(o);
						}
					} catch (IOException e) {
						message = "There was an error with loading your drawing!";
					}
				} else {
					message = "There was an error with loading your drawing!";
				}
				InformationDialog d = new InformationDialog(stage, message);
				d.show();
			}
		});

		Button saveButton = new Button("Save");
		saveButton.setFocusTraversable(false);
		saveButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Where to save your drawing?");
				File selectedFile = fileChooser.showSaveDialog(stage);
				String message = "Your drawing was saved successfully!";
				if (selectedFile != null) {
					try {
						Writer out = new BufferedWriter(new FileWriter(selectedFile));
						List<String> rows = new ArrayList<>(dm.list().size());
						for (GraphicalObject o : dm.list()) {
							o.save(rows);
						}
						for (String row : rows) {
							out.write(row + "\n");
						}
						out.close();
					} catch (IOException e) {
						message = "There was an error with saving your drawing!";
					}
				} else {
					message = "There was an error with saving your drawing!";
				}
				InformationDialog d = new InformationDialog(stage, message);
				d.show();
			}
		});

		Button svgButton = new Button("SVG Export");
		svgButton.setFocusTraversable(false);
		svgButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Where to save your drawing?");
				File selectedFile = fileChooser.showSaveDialog(stage);
				String message = "Your drawing was saved successfully!";
				if (selectedFile != null) {
					SVGRendererImpl r = new SVGRendererImpl(selectedFile.toString());
					for (GraphicalObject o : dm.list()) {
						o.render(r);
					}
					try {
						r.close();
					} catch (IOException e) {
						message = "There was an error with saving your drawing!";
					}
				} else {
					message = "There was an error with saving your drawing!";
				}
				InformationDialog d = new InformationDialog(stage, message);
				d.show();
			}
		});

		Button selectButton = new Button("Select");
		selectButton.setFocusTraversable(false);
		selectButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				currentState = new SelectShapeState(GUI.this);
			}
		});

		Button deleteButton = new Button("Delete");
		deleteButton.setFocusTraversable(false);

		HBox toolBar = new HBox();
		toolBar.setFocusTraversable(false);
		toolBar.setPadding(new Insets(5.0));
		toolBar.setSpacing(5);

		toolBar.getChildren().addAll(loadButton, saveButton,
				svgButton,
				selectButton, deleteButton);

		FlowPane objectPane = new FlowPane();
		objectPane.setFocusTraversable(false);
		objectPane.setPadding(new Insets(5.0));
		objectPane.setVgap(5);
		objectPane.setHgap(5);
		objectPane.setPrefWrapLength(CANVAS_WIDTH);

		for (GraphicalObject o : initialObjects) {
			Button but = new Button(o.getShapeName());
			but.setFocusTraversable(false);
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
		pane.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				if (event.isControlDown() && event.getCode() == KeyCode.W) {
					Platform.exit();
				}
			}
		});
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

}
