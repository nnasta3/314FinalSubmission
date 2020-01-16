package application;

import java.io.Serializable;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Square extends Rectangle implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3817749063560916723L;
	Checker checker;
	int xPos,yPos;
	String color;
	
	public Square(double width, double height, int xPos, int yPos){
		super(width, height);
		this.setxPos(xPos);
		this.setyPos(yPos);
	}
	
	public Square(double width, double height, int xPos, int yPos, Checker checker){
		super(width, height);
		this.setxPos(xPos);
		this.setyPos(yPos);
		this.checker=checker;
	}


	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		if(color.equals("w")){
			this.setFill(Color.WHITE);
		}
		if(color.equals("b")){
			this.setFill(Color.BLACK);
		}
		this.color = color;
	}

	public Square() {
		// TODO Auto-generated constructor stub
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

	public Checker getChecker() {
		return checker;
	}

	public void setChecker(Checker checker) {
		this.checker = checker;
	}
	
	public String toString(){
		
		return "Square at "+xPos+", "+yPos+" with a "+getFill()+" colored square and "+checker;
		
	}
	
}
