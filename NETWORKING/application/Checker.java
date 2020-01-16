package application;

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
	
	public Checker(double centerX, double centerY, double radius, int xPos, int yPos){
		super(centerX, centerY, radius);
		this.setxPos(xPos);
		this.setyPos(yPos);
	}
	
	public String getTeam() {
		return team;
	}

	public void setTeam(String team) {
		this.team = team;
	}

	public boolean isKing() {
		return isKing;
	}
	public void setKing(boolean isKing) {
		this.isKing = isKing;
	}
	public int getxPos() {
		return xPos;
	}
	public void setxPos(int xPos) {
		this.xPos = xPos;
	}
	public int getyPos() {
		return yPos;
	}
	public void setyPos(int yPos) {
		this.yPos = yPos;
	}
	public String toString(){
		return team+" checker";
	}
	
}
