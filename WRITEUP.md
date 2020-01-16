Patrick Daley and Nick Nasta
Multiplayer Checkers GUI
CS 314 Final Project
12 May 2019


Motivation behind the project:

Event-Driven Programming is a very common aspect of modern software, and most of the projects during our university experience
do not focus much on this paradigm. One of the aspects we found interesting throughout the project was the idea of understanding
that the key to a functional and smooth UI involves the process of threading, as well as the importance of program state, which
is especially important when implementing a game.

Approach to project presented:

Unfortunately, because we started with a networking approach (see Challenges) we decided to not use SceneBuilder/FXML loading, 
as we were not sure what type of object to send over the socket (we were still troubleshooting the Serializable issue, see Challenges), that's why all UI components are coded directly into the main class.

Challenges Faced:

NOTE: Source code for the networking attempt is in the NETWORKING folder.

Originally in our proposal, the Checker's game was meant to be played over a socket on potentially 2 different machines. 
On launch, the initial connection is made, the first Player's window would display the newly generated checker board, 
and the second player's would display a simple Text object reading "Waiting for Red's move", then would launch a thread 
that awaits an object from the first player, once the first player submits his move. To "send the move", because most
JavaFX elements are not Serializable, we made a 2D array representation of the board, in addition to the GridPane.
We could successfully send this array over the connection (with the input move reflected properly) to the second player.
However, at this point, the first player's window will freeze (while it waits for the second player's move) and the
second player's window will go blank, even though primaryStage.setScene(newScene); is executed (where newScene is a
new Scene created from the data provided by the 2D array), the stage will not change to anything. Even if newScene
is set to be a simple Text display, as opposed to a reconstructed board (showing that the issue is not from creating the
GridPane). Below is a print statement showing that all information needed to recreate a scene was properly sent over:

	Adding Square at 0, 0 with a 0xffffffff colored square and null to the board
	Adding Square at 0, 1 with a 0x000000ff colored square and blue checker. to the board
	Adding Square at 0, 2 with a 0xffffffff colored square and null to the board
	Adding Square at 0, 3 with a 0x000000ff colored square and null to the board
	Adding Square at 0, 4 with a 0xffffffff colored square and null to the board
	Adding Square at 0, 5 with a 0x000000ff colored square and red checker. to the board
	....
	Adding Square at 7, 4 with a 0x000000ff colored square and null to the board
	Adding Square at 7, 5 with a 0xffffffff colored square and null to the board
	Adding Square at 7, 6 with a 0x000000ff colored square and red checker. to the board
	Adding Square at 7, 7 with a 0xffffffff colored square and null to the board

After a few days of troubleshooting, I came to the conclusion that it had to do with the Application thread being blocked
before changing the Scene. To try and free up the Application thread we created threads for every single operation taking place
though blocks of code like:

    Platform.runLater(new Runnable() {//This one blocks player 1.
	    @Override
	    public void run() {
	        //Code to change scene to checkers game.
	       recvMove();
	    }
	});

    Runnable task = new Runnable()
    {
        public void run()
        {
            sendMove();
        }
    };
    Thread back
     
    Thread backgroundThread = new Thread(task);
    
    backgroundThread.setDaemon(true);
    backgroundThread.start();

To run the source code for the networking attempt use the following inputs for args:
red 7777 8888
blue 8888 7777

