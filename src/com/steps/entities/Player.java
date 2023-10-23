package com.steps.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

import com.steps.main.Game;
import com.steps.main.Menu;
import com.steps.main.Sound;
import com.steps.world.Camera;
import com.steps.world.World;

public class Player extends Entity{
	
	//private int maskx = 8,masky = 8,maskw = 10,maskh = 10;
	
	public boolean right,up,left,down;
	public int right_dir = 2,up_dir = 3,left_dir = 1,down_dir = 0;
	public int dir = down_dir;
	public int speed = 1;
	
	public boolean jump = false;
	
	public boolean isJumping = false;
	
	public boolean jumpUp = false;
	
	public boolean jumpDown = false;
	
	public int jumpSpd = 1;
	
	public int z = 0;
	
	public int jumpFrames = 40, jumpCur = 0;
	
	private int frames = 0, maxFrames = 10,index = 0,maxIndex = 3;
	private boolean moved = false;
	
	private BufferedImage[] downPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	
	private BufferedImage playerDamage;
	
	public int ammo = 5;
	
	public boolean shoot = false,mouseShoot = false;
	
	public int mx,my;
	
	public boolean isDamaged = false;
	private int damageFrames = 0;
	
	public boolean gunInHand = false;
	public double life = 50,max_life = 100;
	
	public Player(int x, int y, int width, int height, BufferedImage sprite){
		super(x, y, width, height, sprite);
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(96,32,16,16);
		
		
		for(int i = 0; i < 4; i++){
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),0,16,16);
		}
		for(int i = 0; i < 4; i++){
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),32,16,16);
		}
		for(int i = 0; i < 4; i++){
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),80,16,16);
		}
		for(int i = 0; i < 4; i++){
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16),64,16,16);
		}
	}

	public void update(){
		
		//maskx = 8;masky = 8;maskw = 10;maskh = 10;
		depth = 1;
		 moved = false;
		if(right && World.isFree((int)(x+speed),this.getY(), z)){
			moved = true;
			dir = right_dir;
			x+=speed;
		}
		else if(left && World.isFree((int)(x-speed),this.getY(), z)){
			moved = true;
			dir = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed), z)){
			moved = true;
			dir = up_dir;
			y-=speed;
		}
		else if(down && World.isFree(this.getX(),(int)(y+speed), z)){
			moved = true;
			dir = down_dir;
			y+=speed;
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
			checkCollisionWithAmmo();
			checkCollisionWithLife();
			checkCollisionWithWeapon();
			
			if(isDamaged){
				this.damageFrames++;
				if(this.damageFrames == 8){
					this.damageFrames = 0;
					isDamaged = false;
				}
			}
				
			if(shoot){
				shoot = false;
				//CRIAR BALA E ATIRAR !
				if(gunInHand && ammo > 0  ){
					Sound.shootFx.play();
					ammo --;
				int dx = 0;
				int px = 0;
				int py = 8;
				int dy = 0;
				if(dir == right_dir){
					 dx = 1;
					 px = 16;
				}else if(dir == left_dir){
					 dx = -1;
					 px = 0;
				}else if(dir == up_dir){
					px = 7;
					py = 2;
					dy = -1;
				}else if(dir == down_dir){
					px = 13;
					py = 16;
					dy = 1;
				}
			
				Bullet bullet = new Bullet(this.getX()+px,this.getY()+py,2,2,null,dx,dy);
				Game.bullets.add(bullet);
				}
			}
			if(mouseShoot){
				mouseShoot = false;
				//CRIAR BALA E ATIRAR !
				if(gunInHand && ammo > 0){
					Sound.shootFx.play();
					ammo --;
				int px = 0;
				int py = 8;
				double angle = 0;
				if(dir == right_dir){
					 px = 16;
						angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
				}else if(dir == left_dir){
					 px = 0;
					 	angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
				}if(dir == up_dir){
					 px = 8;
					 py = 0;
						angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
				}else if(dir == down_dir){
					 px = 14;
					 py = 16;
					 	angle = Math.atan2(my - (this.getY()+py - Camera.y),mx - (this.getX()+px - Camera.x));
				}
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				Bullet bullet = new Bullet(this.getX()+px,this.getY()+py,2,2,null,dx,dy);	
				Game.bullets.add(bullet);
				}
			}
			if(life<=0){
				life = 0;
				Game.gameState = "GAME_OVER";
				Sound.musicBackground.stop();
			}
			
			if(jump){
				if(isJumping == false){
				jump = false;
				isJumping = true;
				jumpUp = true;
				}
			}
				if(isJumping){
						if(jumpUp){
							jumpCur+=2;
						}else if(jumpDown){
							jumpCur-=2;
							if(jumpCur <= 0){
								isJumping = false;
								jumpDown = false;
								jumpUp = false;
							}
						}
						z = jumpCur;
						if(jumpCur >= jumpFrames){
							jumpUp = false;
							jumpDown = true;
						}
				}
			
			
			
			Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
			Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
		}
	
	
	public void checkCollisionWithAmmo(){
		for(int i = 0; i < Game.entities.size();i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Ammo){
				if(Entity.isColliding(this, atual)){
					ammo += 1000;
					//System.out.println("MUNIÇÃO : "+ammo);
					Sound.pickupFx.play();
					Game.entities.remove(atual);
					}
				}
			}
		}
	
	public void checkCollisionWithPortal(){
		for(int i = 0; i < Game.entities.size();i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Portal){
				if(Entity.isColliding(this, atual)){
				}
			}
		}
	}
	
	public void checkCollisionWithLife(){
		for(int i = 0; i < Game.entities.size();i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack){
				if(Entity.isColliding(this, atual)){
					life+=10;
					if(life > 100){
						life = 100;
					}
					Sound.pickupFx.play();
					Game.entities.remove(atual);
				}
			}
		}
	}
	
	public void checkCollisionWithWeapon(){
		for(int i = 0; i < Game.entities.size();i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon){
				if(Entity.isColliding(this, atual)){
					gunInHand = true;
					Sound.pickup2Fx.play();
					//System.out.println("Pegou a ARMA!");
					Game.entities.remove(atual);
					}
				}
			}
		}
		

	public void render(Graphics g){
		/*Graphics2D g2 = (Graphics2D) g;
		double angleMouse = Math.atan2(this.getX()- Camera.x - Game.mx,this.getY() - Camera.y - Game.my);
		g2.rotate(angleMouse, this.getX()- Camera.x, this.getY() - Camera.y);*/
		if(!isDamaged){
			if(dir == down_dir){
			g.drawImage(downPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y - z,null);
		if(gunInHand){
			g.drawImage(Entity.WEAPON_DOWN,this.getX()+5- Camera.x,this.getY()+6- Camera.y - z,null);
		}
			}else if(dir == up_dir){
			g.drawImage(upPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y - z,null);
		if(gunInHand){
		}
			}else if(dir == right_dir){
			g.drawImage(rightPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y - z,null);
		if(gunInHand){
				g.drawImage(Entity.WEAPON_RIGHT,this.getX()+6 - Camera.x,this.getY()+1 - Camera.y - z,null);
		}
			}else if(dir == left_dir){
			g.drawImage(leftPlayer[index],this.getX() - Camera.x,this.getY() - Camera.y - z,null);	
		if(gunInHand){
			g.drawImage(Entity.WEAPON_LEFT,this.getX()-6- Camera.x,this.getY()+1 - Camera.y - z,null);
		}
			}
		}else{
		g.drawImage(playerDamage, this.getX()- Camera.x, this.getY()-Camera.y - z,null);
			if(gunInHand){
			}
		}
			if(isJumping){
				g.setColor(Color.black);
				g.fillOval(this.getX() - Camera.x + 3 ,this.getY() - Camera.y + 16, 10, 5);
			}
	
	}
}
