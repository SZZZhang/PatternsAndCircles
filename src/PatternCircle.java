/* Name: Shirley Zhang 
 * Course code: ICS3U
 * Date: Nov 28th
 * Instructor: Jeff Radulovic
 * Assignment name: Circle Patterns Assignment
 * Description: A graphical user interface that takes in user input
 * and displays patterns drawn on a large circle. One field of input, N, is
 * the number of dots drawn on the circle. To draw the pattern, the i-th
 * dot is connected with the (i * M)-th dot (M is another field of input) with a line.
 * There are many interesting patterns made with different values of N and M. 
 */

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;

public class PatternCircle extends Application {
	
	//dimensions 
	private int windowX = 950, windowY = 800; //window dimensions
	private int centerX = 400, centerY = 400;// center of the large circle
	private int radius = 300;//radius of large circle 
	private int dotRadius = 3;
	private int buttonLayoutWidth = 150;
	private int navBarSpacing = 10;
	private int bigCircleBoxWidth = 800;
	
	//default values:
	private int defaultN = 10, defaultEndN = 100;
	private int defaultStM = 2, defaultEndM = 20;
	private double defaultMIncrement = 0.1;
	
	//current values:
	private	int tenthSeconds = 0; //how many 1/10 of a second has gone by since timer.start()
	private int stN, endN;
	private int endM;
	private int curN; //current N value
	private double MIncrement;
	private double curM; //current M value
	
	//animation timers:
	AnimationTimer NTimer;
	AnimationTimer MTimer;
	
	//Labels that display current N & M values
	Label currentN;
	Label currentM;
	
	//Text fields 
	TextField NField;
	TextField endNField;
	TextField MField; 
	TextField MIncrementField;
	TextField endMField;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void start(Stage primaryStage) throws Exception {
		
		//====Layout=====
		HBox main = new HBox(); // HBox for all elements
		VBox navigationBar = new VBox();
		navigationBar.setSpacing(navBarSpacing);
		navigationBar.setPrefWidth(buttonLayoutWidth);
		Scene scene = new Scene(main, windowX, windowY);	
		Group patterns = new Group(); // group that contains lines and dots
		Group circleGroup = new Group(); // contains big circle and patterns group
		
		//box for big circle
		HBox circleBox = new HBox();
		circleBox.setAlignment(Pos.CENTER);
		circleBox.setPrefWidth(bigCircleBoxWidth);
		
		//creates big circle
		Circle circle = new Circle(centerX, centerY, radius);
		circle.setFill(Color.WHITE);
		circle.setStroke(Color.BLACK);
		circle.setStrokeWidth(1.0);
		
		//adds big circle and "patterns" group which contains lines and dots
		circleGroup.getChildren().addAll(circle, patterns);
		circleBox.getChildren().add(circleGroup);
		
		//adds everything to "main" HBox
		main.getChildren().add(navigationBar);
		main.getChildren().add(circleBox);		
		
		//====== Navigation Bar ======
		//parts 1 & 2 navigation bar
		Label NText = new Label("N: ");
		NField = new TextField();
		Button drawPattern = new Button("Draw pattern");
		navigationBar.getChildren().addAll(NText, NField, drawPattern);
		
		//part 3 navigation bar
		Label endNText = new Label("End N: ");
		endNField = new TextField();
		Button drawNToEnd = new Button("Draw N to end");
		navigationBar.getChildren().addAll(endNText, endNField, drawNToEnd);
		
		//part 4 navigation bar
		Label MText = new Label("M: ");
		MField = new TextField();
		Label MIncrementText = new Label("M increment: ");
		MIncrementField = new TextField();
		Button drawToNextM = new Button("Draw to next M");
		navigationBar.getChildren().addAll(MText, MField,
				MIncrementText, MIncrementField, drawToNextM);		
		
		//part 5
		Label endMText = new Label("End M: ");
		endMField = new TextField();
		Button endMButton = new Button("Draw to Changing M");
		navigationBar.getChildren().addAll(endMText,
				endMField, endMButton);
		
		//displays current values to navigation bar
		currentN = new Label("Current N: ");
		currentM = new Label("Current M: ");
		navigationBar.getChildren().addAll(currentN, currentM);
		
		//======animation timers=======
		//part 3
		//animation timer
		NTimer = new AnimationTimer() {
	        private long lastTime = 0;
	
	        public void handle(long now) {
	        	//while the current N is less than the ending N
	        	if (lastTime != 0 && tenthSeconds < endN - stN) {
	        		//if 1/10 of a second has gone by
	                if (now > lastTime + 100000000) {
	                    tenthSeconds++;
	                    patterns.getChildren().clear();
	                    curN = stN + tenthSeconds;
	                    updateText();
	                    lastTime = now;
	                    drawPattern(curN, curM, patterns);
	                }
	                else if(tenthSeconds > endN){
	                	stop();
	                }
	            }
	            else
	                lastTime = now;
	        }
	        public void stop() {
	            super.stop();
	            lastTime = 0;
	            tenthSeconds = 0;
	        }
		};
		
		//animation timer for animating m value
		MTimer = new AnimationTimer() {
	        private long lastTime = 0;
	        public void handle(long now) {
	        	//while the current M is less than or equal to the ending M
	        	if (lastTime != 0 && curM <= endM) {
	        		//if 1/10 of a second has gone by
	                if (now > lastTime + 100000000) {
	                    lastTime = now;
	                    patterns.getChildren().clear();
	                    drawPattern(curN, curM, patterns);
	                    curM += MIncrement;
	                    updateText();
	                }
	                else if(curM > endM) {
	                	stop();
	                }
	            }
	            else
	                lastTime = now;
	        }

	        public void stop() {
	            super.stop();
	            lastTime = 0;
	        }
		};
		
		//======buttons=====
		//part 1 & 2
		//when button is pressed
		drawPattern.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				resetTimers();
				patterns.getChildren().clear();
				getInput();

				drawPattern(curN, curM, patterns);
				updateText();
			}
		});
			
		//Part 3 button
		drawNToEnd.setOnAction(new EventHandler<ActionEvent>(){
			public void handle(ActionEvent arg0) {
				resetTimers();
				//takes in input				
				getInput();
				stN = Integer.parseInt(NField.getText());
				//starts timer
				NTimer.start();
			}
		});	
		
		//part 4 button
		drawToNextM.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				resetTimers();
				//gets input
				getInput();
				endM = (int) curM + 1;
				MTimer.start(); //starts timer
				updateText();
			}
		});
		
		//part 5 button
		endMButton.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				resetTimers();
				//gets input
				getInput();
				MTimer.start(); //starts timer
				updateText();
			}
		});
		
		//sets default values to text fields
		NField.setText(Integer.toString(defaultN));
		endNField.setText(Integer.toString(defaultEndN));
		MField.setText(Integer.toString(defaultStM));
		MIncrementField.setText(Double.toString(defaultMIncrement));
		endMField.setText(Integer.toString(defaultEndM));
		
		//displays stage
		primaryStage.setTitle("Circle");
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	/*Takes in an integer n, the number of dots to be drawn,
	 * returns array of coordinates of n dots on the circle
	 */
	private int[][] dotPositions(int n) {
		double angleIncrements = 2 * Math.PI / n;
		double angle = Math.PI;
		int[][] dotPos = new int [n][2]; 
		for(int dot = 0; dot < n; dot++) {
			int x = (int) (radius * Math.cos(angle));
			int y = (int) (radius * Math.sin(angle));
			dotPos[dot][0] = x + centerX; dotPos[dot][1] = y + centerY; 
			angle += angleIncrements;
		}
		return dotPos;
	}
	
	//Draws pattern to "group" using the provided n and m values
	private void drawPattern(int n, double m, Group group) {
		int[][] dotPos = dotPositions(n);
		
		//draws dots
		for(int dot = 0; dot < n; dot++) {
			Circle newDot = new Circle(dotPos[dot][0], dotPos[dot][1], dotRadius);
			group.getChildren().add(newDot);
		}
		
		//draws lines
		for(int dot = 0; dot < n; dot++) {
			Line line = new Line(dotPos[dot][0], dotPos[dot][1],
					dotPos[((int) Math.round(((dot * m))) % n)][0],
					dotPos[((int) Math.round((dot * m))) % n][1]);
			line.setStrokeWidth(1);
			group.getChildren().add(line);
		}
	}
	
	//displays the current values of N and M on the screen
	private void updateText() {
		currentN.setText("Current N: " + curN);
		currentM.setText("Current M: " + curM);
	}
	
	//resets timers 
	private void resetTimers() {
		MTimer.stop();
		NTimer.stop();
	}
	
	//gets input from text fields
	private void getInput() {
		curN = Integer.parseInt(NField.getText());
		endN = Integer.parseInt(endNField.getText());
		curM = Double.parseDouble(MField.getText());
		MIncrement = Double.parseDouble(MIncrementField.getText());
		endM = Integer.parseInt(endMField.getText());
	}
	
}












