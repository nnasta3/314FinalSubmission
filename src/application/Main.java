package application;
//Nicholas Nasta & Patrick Daley
import java.util.ArrayList;
import java.util.Stack;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class Main extends Application {
	
double SQUARE_SIZE=50;
double CHECKER_RADIUS=22;
double WINDOWX=550;
double WINDOWY=400;
boolean jumpMade=false;
static String player;
static Stage primStage;
ArrayList<Checker> redCheckers, blueCheckers;
Square[][] boardAsSquares;
Checker selectedChecker;
Square targetSquare, originSquare;
GridPane board;
Text statusText, errorText, redLeft, blueLeft;
HBox bottomBox;
VBox rightBox;
Button reset;

	@Override
	public void start(Stage primaryStage) {

			primaryStage.setTitle("Checkers");
			primaryStage.setScene(createScene());
			//primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("./checkerPic.png")));
			primaryStage.setResizable(false);
			primaryStage.show();
			primStage=primaryStage;

	}
	
	
	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Instantiates the game board with all checkers and squares, as well as UI
	 * @return The gridpane that contains all checkers and square objects that make up the game. A new game of checkers.
	 */
	public Scene createScene(){
		
		player="red";			
		redCheckers=new ArrayList<Checker>();
		blueCheckers=new ArrayList<Checker>();
		
		redLeft=new Text("");
		blueLeft=new Text("");
		
		Scene scene=null;
		BorderPane root=new BorderPane();
		board=createBoard();
		
	    rightBox=new VBox(10);
		rightBox.setPadding(new Insets(10,10,10,10));
		rightBox.setAlignment(Pos.CENTER);
		redLeft.setText(new String("Red Checkers Left: "+redCheckers.size()));
		blueLeft.setText(new String("Blue Checkers Left: "+blueCheckers.size()));
		rightBox.getChildren().add(redLeft);
		rightBox.getChildren().add(blueLeft);
		reset=new Button("Reset Game");
		
		reset.setOnAction(new EventHandler<ActionEvent>() {
		    @Override public void handle(ActionEvent e) {
		        primStage.setScene(createScene());
		    }
		});
		
		reset.setAlignment(Pos.CENTER);
		rightBox.getChildren().add(reset);	
		statusText=new Text("Red's Turn!");
		errorText=new Text("");
		rightBox.getChildren().add(statusText);
		rightBox.getChildren().add(errorText);
	    
		//root.getChildren().add(board);
		root.setCenter(board);
		root.setRight(rightBox);
		root.getChildren().addAll(redCheckers);
		root.getChildren().addAll(blueCheckers);
		
		scene = new Scene(root,WINDOWX,WINDOWY);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
		
	}
	
	/**
	 * Creates the 2d array representation of the gridPane board, also sets the mouseClick listeners for each square and checker.
	 * 
	 * @return 2d representation of the game board
	 */
	public GridPane createBoard(){
		
		board=new GridPane();
		boardAsSquares=new Square[8][8];
		
		for(int i=0; i<8; i++){
			
			for(int j=0; j<8; j++){

				Square square=new Square(SQUARE_SIZE,SQUARE_SIZE, i, j);

				if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)){
					//white square	
					square.setColor("w");
					boardAsSquares[i][j]=square;
					board.add(square, i, j);
				}
				
				else{
					//black square
					square.setColor("b");
					
					Checker checker=new Checker(indexToPixel(i), indexToPixel(j), CHECKER_RADIUS, i , j);
					
				    square.setOnMousePressed(new EventHandler<MouseEvent>() {	//When Square Selected
				        @Override
				        public void handle(MouseEvent event) {
				        	
				        	handleSquareClick(event);

				        }
				    });
				    
				    checker.setOnMousePressed(new EventHandler<MouseEvent>() {
				        @Override
				        public void handle(MouseEvent event) {
				        	
				        	handleCheckerClick(event);

				        }
				    });
				    
					if(j<3){//Blue Team
						
						checker.setTeam("blue");
						checker.setFill(Color.BLUE);
						blueCheckers.add(checker);
						square.setChecker(checker);

					}
					
					if (j>4){//Red Team
						
						checker.setTeam("red");
						checker.setFill(Color.RED);
						redCheckers.add(checker);
						square.setChecker(checker);

					}
					boardAsSquares[i][j]=square;
					board.add(square, i, j);
				}
				
			}
		}
		return board;
	}
	
	/**
	 * When a user selects a square before selecting a checker, will display an error message.
	 * @param event On a user clicking a square
	 */
	public void handleSquareClick(MouseEvent event){
		
		errorText.setText("");
		
        targetSquare=(Square)event.getSource();
        targetSquare=boardAsSquares[targetSquare.getxPos()][targetSquare.getyPos()];
        
    	if(selectedChecker==null){
    		System.out.println("Checker Not Selected First");
    		return;
    	}

        System.out.println("Moving checker at "+selectedChecker.getxPos()+" "+selectedChecker.getyPos()
        	+" to "+targetSquare.getxPos()+" "+targetSquare.getyPos());

		proposeMove();

	}
	
	/**
	 * When a checker is clicked, this method changes its color to indicate to the user their currently selected checker, will also display an error message
	 * if the checker selected does not belong to the current player. Also allows for a player to click the selected checker again to deselect it.
	 * @param event Checker being clicked
	 */
	public void handleCheckerClick(MouseEvent event){
    	
		errorText.setText("");
		
    	if (selectedChecker==null){
            selectedChecker=(Checker)event.getSource();  
            
        	if (!(selectedChecker.getTeam().equals(player))){
        		selectedChecker=null;
        		//System.out.println("This is not your checker.");
        		errorText.setText("This is not your Checker.");
        		return;
        	}
        	
            selectedChecker.setFill(Color.GREEN);
            originSquare=boardAsSquares[selectedChecker.getxPos()][selectedChecker.getyPos()];
    	}
    	
    	else if(selectedChecker.equals((Checker)event.getSource())){
    		//Should end turn if a jump has been made.
    		//System.out.println("Trying to move to itself.");
    		if (jumpMade){
        		selectedChecker=null;
    			finishTurn();
    		}else{
        		selectedChecker=null;
        		errorText.setText("Invalid Move.");
        		resetCheckerColors();
    		}
    	}

    	else{
    		selectedChecker=null;
    		errorText.setText("Invalid Move.");
    		resetCheckerColors();
    		//System.out.println("Space Occupied");
        }
    	
	}
	
	/**
	 * Used to orient the position of a checker after a move
	 * @param index Target square x or y position
	 * @return 
	 */
	public double indexToPixel(double index){return index*50+25;}

	
	/**
	 * Updates the scene after each move, also checks if the game should be ended
	 */
	public void refreshScene(){
		
		Scene newScene;
		BorderPane root=new BorderPane();
		
		if (redCheckers.isEmpty()){
			statusText.setText("Blue Wins");
			errorText.setText("Game Over");
			endGame();
		}
		if (blueCheckers.isEmpty()){
			statusText.setText("Red Wins");
			errorText.setText("Game Over");
			endGame();
		}
		
		//TODO NO VALID MOVES CHECK
		if(!checkMovesLeft(redCheckers) && !checkMovesLeft(blueCheckers)){
			statusText.setText("Stalemate");
			errorText.setText("Game Over");
			endGame();
		}
		if(!checkMovesLeft(redCheckers)){
			statusText.setText("Blue Wins");
			errorText.setText("Game Over");
			endGame();
		}
		if(!checkMovesLeft(blueCheckers)){
			statusText.setText("Red Wins");
			errorText.setText("Game Over");
			endGame();
		}
		
		
		redLeft.setText(new String("Red Checkers Left: "+redCheckers.size()));
		blueLeft.setText(new String("Blue Checkers Left: "+blueCheckers.size()));
		
		root.setCenter(board);
		root.setRight(rightBox);
		root.getChildren().addAll(redCheckers);
		root.getChildren().addAll(blueCheckers);
		newScene=new Scene(root,WINDOWX,WINDOWY);
		primStage.setScene(newScene);
		
	}
	
	/**
	 * Checks every checker in a checkerList if there is a checker that can move the method will return true, false otherwise
	 * @param checkerList red or blue
	 * @return true if there is a piece that can move, false otherwise
	 */
	boolean checkMovesLeft(ArrayList<Checker> checkerList){
		for(Checker c: checkerList){
			if(c.canMove(c,boardAsSquares)){
				return true;
			}
			else{
				continue;
			}				
		}
		
		return false;
	}
	

	/**
	 * Changes the players turn, and updates the UI to match whose turn it is
	 */
	public void finishTurn(){
		
		if (player.equals("red")){
			statusText.setText("Blue's Turn!");
			player="blue";
		}
		
		else{
			statusText.setText("Red's Turn!");
			player="red";
		}
		
		System.out.println("Ending Turn");
		resetCheckerColors();
		selectedChecker=null;
		targetSquare=null;
		originSquare=null;	
		jumpMade=false;
		
	}
	
	/**
	 * Checks if the currently selected checker can move to the selected square. This method also will move the checker and call the removeChecker method
	 * to remove any checker that has been jumped. This method will also "King" checkers that make it to the opposite end of the board. Will report
	 * an error if the move is invalid.
	 */
	public void proposeMove(){
	    
	    if(selectedChecker.validMove(targetSquare.getxPos(), targetSquare.getyPos(), targetSquare, selectedChecker,boardAsSquares, jumpMade)){
	    	//MOVE CHECKER
	    	int x = selectedChecker.getxPos();
	    	int y = selectedChecker.getyPos();
	    	
	    	//Move Checker, must set the originSquare to the targetSquare or checker will be invisible after jumping because the origin is lost
	    	originSquare.setChecker(null);
		    selectedChecker.setCenterX(indexToPixel(targetSquare.getxPos()));
		    selectedChecker.setCenterY(indexToPixel(targetSquare.getyPos()));
		    selectedChecker.setxPos(targetSquare.getxPos());
		    selectedChecker.setyPos(targetSquare.getyPos());
		    targetSquare.setChecker(selectedChecker);
		    originSquare = targetSquare;


	        int a = targetSquare.getChecker().getxPos();
	        int b = targetSquare.getChecker().getyPos();
	        
			jumpMade=false;

	        //CHECK IF MOVE WAS A JUMP AND REMOVE JUMPED CHECKER
			//UP LEFT
	        if( x-2 == a && y-2==b && (!targetSquare.getChecker().getTeam().equals(boardAsSquares[x-1][y-1].getChecker().getTeam()))){
	        	removeChecker(boardAsSquares[x-1][y-1]);
	        }
	        //UP RIGHT
	        if(x+2==a && y-2==b && (!targetSquare.getChecker().getTeam().equals(boardAsSquares[x+1][y-1].getChecker().getTeam()))){
	        	removeChecker(boardAsSquares[x+1][y-1]);	
	        }
	        //DOWN LEFT
	        if( x-2 == a && y+2==b && (!targetSquare.getChecker().getTeam().equals(boardAsSquares[x-1][y+1].getChecker().getTeam()))){
        		removeChecker(boardAsSquares[x-1][y+1]);
        		
	        }
	        //DOWN RIGHT
	        if(x+2==a && y+2==b && (!targetSquare.getChecker().getTeam().equals(boardAsSquares[x+1][y+1].getChecker().getTeam()))){
	        	removeChecker(boardAsSquares[x+1][y+1]);
	        		
	        }	         
	        
	        //CHECK FOR KING
	        if(targetSquare.getChecker().getTeam() == "red" && b == 0 && targetSquare.getChecker().isKing()==false){
	        	targetSquare.getChecker().setKing(true);
	        	targetSquare.getChecker().setFill(Color.DARKRED);
	        }
	        
	        if(targetSquare.getChecker().getTeam() == "blue" && b == 7 && targetSquare.getChecker().isKing()==false){
	        	targetSquare.getChecker().setKing(true);
	        	targetSquare.getChecker().setFill(Color.DEEPSKYBLUE);
	        }

	        refreshScene();
			
			if (!jumpMade){
		        finishTurn();
			}
			
			//Clear event handlers for all pieces that are not the piece that made the jump.
			else{

		        for(Checker c: redCheckers){
		        	if(!c.equals(selectedChecker)){
					    c.setOnMousePressed(new EventHandler<MouseEvent>() {
					        @Override
					        public void handle(MouseEvent event) {}});
		        	}
		        }
		        
		        for(Checker c: blueCheckers){
		        	if(!c.equals(selectedChecker)){
					    c.setOnMousePressed(new EventHandler<MouseEvent>() {
					        @Override
					        public void handle(MouseEvent event) {}});
		        	}
		        }

			}
			

	    }
	    else{
    		errorText.setText("Invalid Move.");
	    	System.out.println("Invalid Move");
	    }

	}
	
	
	/**
	 * Removes a jumped checker from a board, updates the global jumpMade variable to true
	 * @param s The square on the board that holds the checker that has been jumped/needs to be removed
	 */
	public void removeChecker(Square s){
		
		System.out.println("Removing checker at "+s.getxPos()+" "+s.getyPos());
		board.getChildren().remove(s.getChecker());
		redCheckers.remove(s.getChecker());
		blueCheckers.remove(s.getChecker());
	    s.setChecker(null);
	    jumpMade=true;
	    
	}
	
	/**
	 * Resets the color of the selected checker, used after a player deselects a checker and when a turn is over
	 */
	public void resetCheckerColors(){
		
        for(Checker c: redCheckers){
        	if(c.isKing){
        		c.setFill(Color.DARKRED);
        	}
        	else{
            	c.setFill(Color.RED);
        	}
		    c.setOnMousePressed(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {
		        	
		        	handleCheckerClick(event);

		        }
		    });
        }
        
        for(Checker c: blueCheckers){
        	if(c.isKing){
        		c.setFill(Color.DEEPSKYBLUE);
        	}else{

            	c.setFill(Color.BLUE);
        	}
		    c.setOnMousePressed(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {
		        	
		        	handleCheckerClick(event);

		        }
		    });
        }
	}
	
	/**
	 * Ends the game, does not allow for any checker to be clicked.
	 */
	public void endGame(){
		
		resetCheckerColors();
		
        for(Checker c: blueCheckers){
		    c.setOnMousePressed(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {}});
        }
        
        for(Checker c: redCheckers){
		    c.setOnMousePressed(new EventHandler<MouseEvent>() {
		        @Override
		        public void handle(MouseEvent event) {}});
        }
        
	}

}
