package com.steps.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.steps.world.Camera;

public class Bullet extends Entity {
	
	private double dx;
	private double dy;
	private double spd = 4;

	public Bullet(double x, double y, int width, int height, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, sprite);
				this.dx = dx;
				this.dy = dy;
	}
	

	
	public void update(){
		x+=dx*spd;
		y+=dy*spd;
	}
	
	public void render(Graphics g){
		g.setColor(Color.yellow);
		g.fillRect(this.getX()- Camera.x,this.getY()- Camera.y,width,height);
	}
}
