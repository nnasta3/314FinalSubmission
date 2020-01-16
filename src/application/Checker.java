package application;
//Nicholas Nasta & Patrick Daley
import java.io.Serializable;

import javafx.scene.shape.Circle;

public class Checker extends Circle implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6873449814653119992L;
	public int xPos;
	public int yPos;
	public String team;
	public boolean isKing;
	
	/**
	 * Checker object, represents a piece in the game of checkers, extends the Circle class 
	 * @param centerX Defines the horizontal position of the center of the circle in pixels.
	 * @param centerY Defines the vertical position of the center of the circle in pixels.
	 * @param radius Defines the radius of the circle in pixels.
	 * @param xPos Defines the horizontal position of the checker in relation to the board
	 * @param yPos Defines the vertical position of the checker in relation to the board
	 */
	public Checker(double centerX, double centerY, double radius, int xPos, int yPos){
		super(centerX, centerY, radius);
		this.setxPos(xPos);
		this.setyPos(yPos);
	}
	/**
	 * Returns the team that the checker belongs to
	 * @return Returns the team that the checker belongs to
	 */
	public String getTeam() {
		return team;
	}
	/**
	 * Assign a checker to a team based on its color
	 * @param team
	 */
	public void setTeam(String team) {
		this.team = team;
	}

	/**
	 * Returns if the checker is a king
	 * @return Returns if the checker is a king
	 */
	public boolean isKing() {
		return isKing;
	}
	/**
	 * Set the checker to be a king
	 * @param isKing
	 */
	public void setKing(boolean isKing) {
		this.isKing = isKing;
	}
	/**
	 * Returns the current x position of the checker
	 * @return Returns the current x position of the checker
	 */
	public int getxPos() {
		return xPos;
	}
	/**
	 * Set the x position of a checker
	 * @param xPos
	 */
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	/**
	 * Returns the current y position of the checker
	 * @return current y position of the checker
	 */
	public int getyPos() {
		return yPos;
	}
	/**
	 * Set the y position of a checker
	 * @param yPos
	 */
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}

	public String toString(){
		return team+" checker";
	}
	
	
	/**
	 * Checks all possible moves for the selected checker, returns true if there is a possible move, false otherwise
	 * @param selectedChecker
	 * @return true if there is a possible move, false if there is no possible move
	 */
	public boolean canMove(Checker selectedChecker, Square [][] board){
		int x = selectedChecker.xPos;
		int y = selectedChecker.yPos;
		//Red Regular, check up right and up left
		if(selectedChecker.getTeam()=="red" && !selectedChecker.isKing) {
			try{
				//Check if the checker can move up left or up right
				if(board[x-1][y-1].getChecker()==null || board[x+1][y-1].getChecker()==null){
					return true;
				}
				//Check if the checker can jump another piece
				else if(board[x-2][y-2].getChecker()==null || board[x+2][y-2].getChecker()==null){
					if( (board[x-1][y-1].getChecker()!=null && board[x-1][y-1].getChecker().getTeam()=="blue")  
							|| (board[x+1][y-1].getChecker()!=null && board[x+1][y-1].getChecker().getTeam()=="blue")){
						return true;
					}
				}
				else{
					return false;
				}
				
			}
			catch(Exception e){
				return false;
			}
			
		
		}
		//Red King, check up right, up left, down right, and down left
		else if (selectedChecker.getTeam()=="red" && selectedChecker.isKing){
			try{
				//Check if the checker can move in all diagonal directions
				if(board[x-1][y-1].getChecker()==null || board[x+1][y-1].getChecker()==null ||  board[x-1][y+1].getChecker()==null ||  board[x+1][y+1].getChecker()==null){
					return true;
				}
				//Check if the checker can jump another piece in any direction
				else if(board[x-2][y-2].getChecker()==null || board[x+2][y-2].getChecker()==null || board[x-2][y+2].getChecker()==null || board[x+2][y+2].getChecker()==null){
					if( (board[x-1][y-1].getChecker()!=null && board[x-1][y-1].getChecker().getTeam()=="blue")  
							|| (board[x+1][y-1].getChecker()!=null && board[x+1][y-1].getChecker().getTeam()=="blue")
							|| (board[x-1][y+1].getChecker()!=null && board[x-1][y+1].getChecker().getTeam()=="blue")
							|| (board[x+1][y+1].getChecker()!=null && board[x+1][y+1].getChecker().getTeam()=="blue") ){
						return true;
					}
				}
				else{
					return false;
				}
			}
			catch(Exception e){
				return false;
			}
			
			
		}
		//Blue Regular, check down right and down left
		else if (selectedChecker.getTeam()=="blue" && !selectedChecker.isKing){
			try{
				//Check if the checker can move down left or down right
				if(board[x-1][y+1].getChecker()==null || board[x+1][y+1].getChecker()==null){
					return true;
				}
				//Check if the checker can jump another piece
				else if(board[x-2][y+2].getChecker()==null || board[x+2][y+2].getChecker()==null){
					if( (board[x-1][y+1].getChecker()!=null && board[x-1][y+1].getChecker().getTeam()=="blue")  
							|| (board[x+1][y+1].getChecker()!=null && board[x+1][y+1].getChecker().getTeam()=="blue")){
						return true;
					}
				}
				else{
					return false;
				}
			}
			catch(Exception e){
				return false;
			}
			
		}
		//Blue King, check up right, up left, down right, and down left
		else if (selectedChecker.getTeam()=="blue" && selectedChecker.isKing){
			try{
				//Check if the checker can move in all diagonal directions
				if(board[x-1][y-1].getChecker()==null || board[x+1][y-1].getChecker()==null ||  board[x-1][y+1].getChecker()==null ||  board[x+1][y+1].getChecker()==null){
					return true;
				}
				//Check if the checker can jump another piece in any direction
				else if(board[x-2][y-2].getChecker()==null || board[x+2][y-2].getChecker()==null || board[x-2][y+2].getChecker()==null || board[x+2][y+2].getChecker()==null){
					if( (board[x-1][y-1].getChecker()!=null && board[x-1][y-1].getChecker().getTeam()=="red")  
							|| (board[x+1][y-1].getChecker()!=null && board[x+1][y-1].getChecker().getTeam()=="red")
							|| (board[x-1][y+1].getChecker()!=null && board[x-1][y+1].getChecker().getTeam()=="red")
							|| (board[x+1][y+1].getChecker()!=null && board[x+1][y+1].getChecker().getTeam()=="red") ){
						return true;
					}
				}
				else{
					return false;
				}
			}
			catch(Exception e){
				return false;
			}
			
		}
		return true;
	}
	
	/**
	 * Check if the selected checker can move to the target square, will also check for a valid jump
	 * @param x target square x position
	 * @param y target square y position
	 * @param targetSquare square the user wants to move the selected checker to
	 * @param selectedChecker currently selected checker
	 * @param board 2d array representation of the board
	 * @param jumpMade Boolean value, true if a checker has already made jump before this method call
	 * @return true if move is valid, false otherwise
	 */
	public boolean validMove(int x, int y, Square targetSquare, Checker selectedChecker, Square [][] board, boolean jumpMade){
		int sx = selectedChecker.getxPos();
		int sy = selectedChecker.getyPos();
		
		//Red Regular
		if(selectedChecker.getTeam()=="red" && !selectedChecker.isKing) {
			if((sx-1) == x && (sy-1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx+1) == x && (sy-1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			//Jumping Check
			else if ((sx-2) == x && (sy-2)==y && targetSquare.getChecker()==null){
				if( board[sx-1][sy-1].getChecker()!=null && board[sx-1][sy-1].getChecker().getTeam()=="blue"){
					return true;
				}
				else{
					return false;
				}
			}
			else if((sx+2) == x && (sy-2)==y && targetSquare.getChecker()==null){
				if( board[sx+1][sy-1].getChecker()!=null && board[sx+1][sy-1].getChecker().getTeam()=="blue"){
					return true;
				}
				else{
					return false;
				}
			}
			else {
				return false;
			}
		}
		//Red King
		else if (selectedChecker.getTeam()=="red" && selectedChecker.isKing){
			if((sx-1) == x && (sy+1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx-1) == x && (sy-1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx+1) == x && (sy+1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx+1) == x && (sy-1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			//Jumping Check
			else if ((sx-2) == x && (sy-2)==y && targetSquare.getChecker()==null){
				if(board[sx-1][sy-1].getChecker()!=null && board[sx-1][sy-1].getChecker().getTeam()=="blue"){
					return true;
				}
				else{
					return false;
				}
			}
			else if((sx+2) == x && (sy-2)==y && targetSquare.getChecker()==null){
				if(board[sx+1][sy-1].getChecker() != null && board[sx+1][sy-1].getChecker().getTeam()=="blue"){
					return true;
				}
				else{
					return false;
				}
			}
			else if ((sx+2) == x && (sy+2)==y && targetSquare.getChecker()==null){
				if(board[sx+1][sy+1].getChecker() != null && board[sx+1][sy+1].getChecker().getTeam()=="blue"){
					return true;
				}
				else{
					return false;
				}
			}
			else if((sx-2) == x && (sy+2)==y && targetSquare.getChecker()==null){
				if(board[sx-1][sy+1].getChecker() != null && board[sx-1][sy+1].getChecker().getTeam()=="blue"){
					return true;
				}
				else{
					return false;
				}
			}
			else {
				return false;
			}
		}
		//Blue regular
		else if (selectedChecker.getTeam()=="blue" && !selectedChecker.isKing){
			if((sx-1) == x && (sy+1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx+1) == x && (sy+1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			//Jumping Check
			else if ((sx-2) == x && (sy+2)==y && targetSquare.getChecker()==null){
				if(board[sx-1][sy+1].getChecker() != null && board[sx-1][sy+1].getChecker().getTeam()=="red"){
					return true;
				}
				else{
					return false;
				}
			}
			else if((sx+2) == x && (sy+2)==y && targetSquare.getChecker()==null){
				if(board[sx+1][sy+1].getChecker() != null && board[sx+1][sy+1].getChecker().getTeam()=="red"){
					return true;
				}
				else{
					return false;
				}
			}
			else {
				return false;
			}
		}
		//Blue King
		else if (selectedChecker.getTeam()=="blue" && selectedChecker.isKing){
			if((sx-1) == x && (sy+1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx-1) == x && (sy-1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx+1) == x && (sy+1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			else if((sx+1) == x && (sy-1)==y && targetSquare.getChecker()==null){
				if (jumpMade){
					return false;
				}
				return true;
			}
			//Jumping Check
			else if ((sx-2) == x && (sy-2)==y && targetSquare.getChecker()==null){
				if(board[sx-1][sy-1].getChecker()!=null && board[sx-1][sy-1].getChecker().getTeam()=="red"){
					return true;
				}
				else{
					return false;
				}
			}
			else if((sx+2) == x && (sy-2)==y && targetSquare.getChecker()==null){
				if(board[sx+1][sy-1].getChecker()!=null && board[sx+1][sy-1].getChecker().getTeam()=="red"){
					return true;
				}
				else{
					return false;
				}
			}
			else if ((sx+2) == x && (sy+2)==y && targetSquare.getChecker()==null){
				if(board[sx+1][sy+1].getChecker()!=null && board[sx+1][sy+1].getChecker().getTeam()=="red"){
					return true;
				}
				else{
					return false;
				}
			}
			else if((sx-2) == x && (sy+2)==y && targetSquare.getChecker()==null){
				if(board[sx-1][sy+1].getChecker()!=null && board[sx-1][sy+1].getChecker().getTeam()=="red"){
					return true;
				}
				else{
					return false;
				}
			}
			else {
				return false;
			}
		}
		return true;
	}
	
}