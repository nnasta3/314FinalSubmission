package application;
	
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;


public class Main extends Application {
	
double SQUARE_SIZE=50;
double CHECKER_RADIUS=22;
static int sendPort;
static int recvPort;
static String player;
ArrayList<Checker> redCheckers, blueCheckers;
Square[][] boardAsSquares;
Checker selectedChecker;
Square targetSquare, originSquare;
static Stage primStage;
static ServerSocket serverSocket;
static Socket sendSocket, recvSocket;


	@Override
	public void start(Stage primaryStage) {
		try {
			

			redCheckers=new ArrayList<Checker>();
			blueCheckers=new ArrayList<Checker>();
			
			primaryStage.setTitle(player);

			primStage=primaryStage;
			
			if(player.equals("red")){//Initializes GameBoard + Checkers
				
				primaryStage.setScene(createScene());
				primaryStage.show();
				
			}
			
			else{ //Player 2 Will launch with a scene of just text and wait to receive new Scene.
				BorderPane root=new BorderPane();
				Text text=new Text("Waiting for Red to make a move...");
				root.setCenter(text);
				Scene scene = new Scene(root,400,400); //Waiting room scene
				
				primaryStage.setScene(scene);
				primaryStage.show();

				makeRecvThread();
				
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String[] args) {
		
		player=args[0];
		sendPort=Integer.parseInt(args[1]);
		recvPort=Integer.parseInt(args[2]);

		try {
			
			if(player.equals("red")){
				
				serverSocket=new ServerSocket(recvPort);
				recvSocket=serverSocket.accept();
				sendSocket=new Socket("localhost", sendPort);
				System.out.println("Socket connected "+recvSocket);
			}
			else{
				sendSocket=new Socket("localhost", sendPort);
				serverSocket=new ServerSocket(recvPort);
				recvSocket=serverSocket.accept();
				System.out.println("Socket connected "+recvSocket);
			}
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(args[0]+ " "+args[1]+ " "+args[2]);
	
		launch(args);
	}

	public void sendMove(){

		OutputStream outputStream;
		System.out.println("Before Send");
		
		try {
			
			outputStream = sendSocket.getOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
			objectOutputStream.writeObject(boardAsSquares);	//Scenes are not send-able
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("After Send");

		makeRecvThread();
		
	}
	
	public void recvMove(){

        InputStream inputStream;
        
        System.out.println("Before Recv");
        
		try {
			
			inputStream = recvSocket.getInputStream();
	        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
	        
	        
	        final Square[][] recvdArray = (Square[][]) objectInputStream.readObject();
	        Platform.runLater(new Runnable() {
			    @Override
			    public void run() {
			        //Code to change scene to checkers game.
			       showNewScene(convertArrayToScene(recvdArray));
			    }
			});

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("After Recv");
		
	}
	
	public void showNewScene(Scene newScene){
		primStage.setScene(newScene);
		primStage.show();
	}
	
	public Scene createScene(){
		
		Scene scene=null;
		BorderPane root=new BorderPane();
		GridPane board=new GridPane();
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
		
		root.getChildren().add(board);
		root.getChildren().addAll(redCheckers);
		root.getChildren().addAll(blueCheckers);
		
		scene = new Scene(root,400,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
	
	public void makeRecvThread(){

        Platform.runLater(new Runnable() {//This one blocks player 1.
		    @Override
		    public void run() {
		        //Code to change scene to checkers game.
		       recvMove();
		    }
		});
        

	}
	
	public void handleSquareClick(MouseEvent event){
		
        targetSquare=(Square)event.getSource();
        
    	if(selectedChecker==null){
    		System.out.println("Checker Not Selected First");
    		return;
    	}

        System.out.println("Moving checker at "+selectedChecker.getxPos()+" "+selectedChecker.getyPos()
        	+" to "+targetSquare.getxPos()+" "+targetSquare.getyPos());

        Platform.runLater(new Runnable() {
		    @Override
		    public void run() {
		       sampleMove();
		    }
		});
 
	}
	
	public void handleCheckerClick(MouseEvent event){
    	
    	if (selectedChecker==null){
            selectedChecker=(Checker)event.getSource();   
        	if (!(selectedChecker.getTeam().equals(player))){
        		selectedChecker=null;
        		System.out.println("This is not your checker.");
        		return;
        	}
            selectedChecker.setFill(Color.GREEN);
            originSquare=findOrigin(selectedChecker);
    	}
    	
    	else{
    		selectedChecker=null;
    		resetCheckerColors();
    		System.out.println("Space Occupied");
        }
    	
	}
	
	public double indexToPixel(double index){return index*50+25;}
	
	public Square findOrigin(Checker check){
		Square origin=null;
		
		for(int i=0; i<8; i++){
			
			for(int j=0; j<8; j++){
				
				if(boardAsSquares[i][j].getxPos()==check.getxPos() && boardAsSquares[i][j].getyPos()==check.getyPos()){
					origin=boardAsSquares[i][j];
				}
			
			}
		}
		
		return origin;
	}
	
	public void sampleMove(){

        resetCheckerColors();
        
        originSquare.setChecker(null);
        selectedChecker.setCenterX(indexToPixel(targetSquare.getxPos()));
        selectedChecker.setCenterY(indexToPixel(targetSquare.getyPos()));
        selectedChecker.setxPos(targetSquare.getxPos());
        selectedChecker.setyPos(targetSquare.getyPos());
        targetSquare.setChecker(selectedChecker);
        selectedChecker=null;
        
        Runnable task = new Runnable()
        {
            public void run()
            {
                sendMove();
            }
        };
         
        Thread backgroundThread = new Thread(task);
        
        backgroundThread.setDaemon(true);
        backgroundThread.start();

	}
	
	public void resetCheckerColors(){
		
        for(Checker c: redCheckers){
        	c.setFill(Color.RED);
        }
        
        for(Checker c: blueCheckers){
        	c.setFill(Color.BLUE);
        }
	}

	public Scene convertArrayToScene(Square[][] recvdArray){
		Scene scene=null;
		BorderPane root=new BorderPane();
		GridPane board=new GridPane();
		
		//Supposed to take a 2d array of squares and populate, create and return the scene.
		
		for(int i=0; i<8; i++){
			
			for(int j=0; j<8; j++){
				
				if(recvdArray[i][j].getChecker()!=null){
					String color=recvdArray[i][j].getChecker().getTeam();
					if (color.equals("red")){
						recvdArray[i][j].getChecker().setFill(Color.RED);
					}
					if (color.equals("blue")){
						recvdArray[i][j].getChecker().setFill(Color.BLUE);
					}
					
				}
				
				if((i%2==0 && j%2==0) || (i%2!=0 && j%2!=0)){
					//white square	
					recvdArray[i][j].setColor("w");
				}
				else{
					recvdArray[i][j].setColor("b");
				}

				recvdArray[i][j].setOnMousePressed(new EventHandler<MouseEvent>() {	 
			        @Override
			        public void handle(MouseEvent event) {
			        	handleSquareClick(event);
			        }
			    });
			    
				if(recvdArray[i][j].getChecker()!=null){
				    recvdArray[i][j].getChecker().setOnMousePressed(new EventHandler<MouseEvent>() {
				        @Override
				        public void handle(MouseEvent event) {
				        	handleCheckerClick(event);
				        }
				    });
				}
				
				System.out.println("Adding "+recvdArray[i][j]+" to the board");
				board.add(recvdArray[i][j], i, j);
				
			}
		}
		
		root.getChildren().add(board);
		
		scene = new Scene(root,400,400);
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		
		return scene;
	}
	
}
