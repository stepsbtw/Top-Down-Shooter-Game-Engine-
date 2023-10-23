package com.steps.entities;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.steps.main.Game;
import com.steps.main.Sound;
import com.steps.world.Camera;
import com.steps.world.World;

public class Enemy extends Entity{
	
	private double speed = 0.6;
	
	private int maskx = 8,masky = 8,maskw = 10,maskh = 10;
	
	private int frames = 0, maxFrames = 10,index = 0,maxIndex = 2;
	
	public int down_dir = 0, up_dir = 1, right_dir = 3,left_dir = 4;
	
	public int dir = down_dir;
	
	private int life = 10;
	
	public boolean isDamaged = false;
	private int damageFrames = 10,damageCurrent = 0;
	
	private BufferedImage[] downEnemy;
	private BufferedImage[] upEnemy;
	private BufferedImage[] leftEnemy;
	private BufferedImage[] rightEnemy;
	
	private boolean moved = false;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, null);
		downEnemy = new BufferedImage[3];
		upEnemy = new BufferedImage[3];
		leftEnemy = new BufferedImage[3];
		rightEnemy = new BufferedImage[3];
		
		for(int i = 0; i < 3; i++){
			downEnemy[i] = Game.spritesheet.getSprite(112 + (i*16),16,16,16);
		}
		for(int i = 0; i < 3; i++){
			upEnemy[i] = Game.spritesheet.getSprite(112 + (i*16),32,16,16);
		}
		for(int i = 0; i < 3; i++){
			leftEnemy[i] = Game.spritesheet.getSprite(112 + (i*16),64,16,16);
		}
		for(int i = 0; i < 3; i++){
			rightEnemy[i] = Game.spritesheet.getSprite(112 + (i*16),48,16,16);
		}
	}

	public void update(){
		depth = 0;
		moved = false;
		maskx = 3;masky = 3;maskw = 10;maskh = 13;
		if(this.calculateDistance(this.getX(),this.getY(),Game.player.getX(), Game.player.getY()) < 100){
		if(this.isCollidingWithPlayer() == false){
		if((int)x < Game.player.getX() && World.isFree((int)(x+speed), this.getY(), z) && !isColliding((int)(x+speed), this.getY())){
			moved = true;
			dir = right_dir;
			x+=speed;
		}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), this.getY(), z) && !isColliding((int)(x-speed), this.getY())){
			moved = true;
			dir = left_dir;
			x-=speed;
		}if((int)y < Game.player.getY() && World.isFree(this.getX(),(int)(y+speed), z) && !isColliding(this.getX(),(int)(y+speed))){
			dir = down_dir;
			moved = true;
			y+=speed;
		}else if((int)y > Game.player.getY() && World.isFree(this.getX(),(int)(y-speed), z) && !isColliding(this.getX(),(int)(y-speed))){
			dir = up_dir;
			moved = true;
			y-=speed;
		}
		}else{
			if(Game.rand.nextInt(100)< 10) {
			Game.player.life-= Game.rand.nextInt(3);
			Game.player.isDamaged = true;
			Sound.hurtFx.play();
				//System.out.println("VIDA :" + Game.player.life);
			if(Game.player.life <= 0){
				//System.out.println("GAME OVER!");
				//System.exit(1);
			}}
		}
		}else{
			System.out.println("esperando");
		}
		if(moved){
			frames++;
			if(frames == maxFrames){
				frames = 0;
				index++;
				if(index > maxIndex){
					index = 0;
					}
			}
		}
			isCollidingWithBullet();
			
			if(life <= 0){
				selfDestroy();
				return;
			}
			if(isDamaged){
				this.damageCurrent++;
				if(this.damageCurrent == this.damageFrames){
					this.damageCurrent = 0;
							this.isDamaged = false;
				}
			}
		}
		public void selfDestroy(){
			Game.enemies.remove(this);
			Game.entities.remove(this);
			Sound.deathFx.play();
			}
		
		public void isCollidingWithBullet(){
			for(int i = 0; i < Game.bullets.size(); i++){
				Entity e = Game.bullets.get(i);
				if(e instanceof Bullet){
					if(Entity.isColliding(this, e)){
						isDamaged = true;
						life--;
					Game.bullets.remove(i);
					//System.out.println("tomando dano");
						return;
					}
			}
		}
	}
	
	public boolean isCollidingWithPlayer(){
		Rectangle enemyCurrent = new Rectangle(this.getX()+maskx,this.getY()+masky,maskw,maskh);
		Rectangle player = new Rectangle(Game.player.getX(),Game.player.getY(),16,16);
		return enemyCurrent.intersects(player);
	}

	public boolean isColliding(int xnext, int ynext){
		Rectangle enemyCurrent = new Rectangle(xnext+maskx,ynext+masky,maskw,maskh);
		for(int i = 0; i < Game.enemies.size(); i++){
			Enemy e = Game.enemies.get(i);
			if(e == this)
				continue;
			Rectangle targetEnemy = new Rectangle(e.getX()+maskx,e.getY()+masky,maskw,maskh);
			if(enemyCurrent.intersects(targetEnemy)) {
				return true;
			}
		}
		return false;
	}
	
	public void render(Graphics g){
		if(!isDamaged) {
		if(dir == down_dir){
			g.drawImage(downEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			
		}else if(dir == up_dir){
			g.drawImage(upEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			
		}else if(dir == left_dir){
			g.drawImage(leftEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
			
		}else if(dir == right_dir){
			g.drawImage(rightEnemy[index],this.getX() - Camera.x,this.getY() - Camera.y,null);
		}
		}if(isDamaged){
		g.drawImage(Entity.ENEMY_FEEDBACK, this.getX() - Camera.x,this.getY() - Camera.y,null);	
	}
}
}
