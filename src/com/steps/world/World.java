package com.steps.world;

import java.awt.Graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.steps.entities.*;
import com.steps.graphics.Spritesheet;
import com.steps.main.Game;

public class World {
	
	public static int WIDTH,HEIGHT;
	public static Tile[] tiles;
	public static final int TILE_SIZE = 16;
	
	public World(String path){
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path));
			int[] pixels = new int[map.getWidth() * map.getHeight()];
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			tiles = new Tile[map.getWidth() * map.getHeight()];
			
			map.getRGB(0, 0, map.getWidth(),map.getHeight(), pixels, 0, map.getWidth());
			
			for(int xx = 0; xx < map.getWidth();xx++){
				
				for(int yy = 0; yy < map.getHeight();yy++){
					
					int pixelAtual = (pixels[xx + (yy*map.getWidth())]);
					
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR); 
					
					if(pixelAtual == 0xFF000000){
						//CHÃO
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
						
					}else if(pixelAtual == 0xFFFFFFFF){
						//PAREDE
						tiles[xx + (yy*WIDTH)] = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
						
					}else if(pixelAtual == 0xFF0000FF){
						//PLAYER
							Game.player.setX(xx*16);
							Game.player.setY(yy*16);
					
					}else if(pixelAtual == 0xFFFF0000){
						//ENEMY
							Enemy en = new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN);
							Game.entities.add(en);
							Game.enemies.add(en);
					}else if(pixelAtual == 0xFF00FF00){
						//WEAPON
							Weapon weap = new Weapon(xx*16,yy*16,16,16,Entity.WEAPON_EN);
							weap.setMask(3,3,12,12);
							Game.entities.add(weap);
					}else if(pixelAtual == 0xFFFF00FF){
						//LIFE PACK
							Lifepack pack = new Lifepack(xx*16,yy*16,16,16,Entity.LIFEPACK_EN);
							pack.setMask(3,3,12,12);
							Game.entities.add(pack);
					}else if(pixelAtual == 0xFFFFFF00){
						//AMMO
							Ammo ammo = new Ammo(xx*16,yy*16,16,16,Entity.AMMO_EN);
							ammo.setMask(3,3,12,12);
							Game.entities.add(ammo);
					}else if(pixelAtual == 0xFF191919){
						//CHÃO 2
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR_2);
					}else if(pixelAtual == 0xFF7F7F7F){
						//MURO
						tiles[xx + (yy*WIDTH)] = new BlockTile(xx*16,yy*16,Tile.TILE_WALL_2);
					}else if(pixelAtual == 0xFF00FFFF){
						//MURO
						Portal portal = new Portal(xx*16,yy*16,16,16,Entity.PORTAL_EN);
						portal.setMask(0,0,12,12);
						Game.entities.add(portal);
					}
				}
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
		public static boolean isFree(int xnext,int ynext,int zplayer){
			int x1 = xnext / TILE_SIZE;
			int y1 = ynext / TILE_SIZE;
			
			int x2 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
			int y2 = ynext / TILE_SIZE;
			
			int x3 = xnext / TILE_SIZE;
			int y3 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
			
			int x4 = (xnext + TILE_SIZE - 1) / TILE_SIZE;
			int y4 = (ynext + TILE_SIZE - 1) / TILE_SIZE;
			
			if(!((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile ||
					tiles[x2 + (y2*World.WIDTH)] instanceof WallTile ||
					tiles[x3 + (y3*World.WIDTH)] instanceof WallTile ||
					tiles[x4 + (y4*World.WIDTH)] instanceof WallTile ||
					tiles[x1 + (y1*World.WIDTH)] instanceof BlockTile ||
					tiles[x2 + (y2*World.WIDTH)] instanceof BlockTile ||
					tiles[x3 + (y3*World.WIDTH)] instanceof BlockTile ||
					tiles[x4 + (y4*World.WIDTH)] instanceof BlockTile))){
					return true;
			}
			if(zplayer > 0 && (!((tiles[x1 + (y1*World.WIDTH)] instanceof BlockTile ||
					tiles[x2 + (y2*World.WIDTH)] instanceof BlockTile ||
					tiles[x3 + (y3*World.WIDTH)] instanceof BlockTile ||
					tiles[x4 + (y4*World.WIDTH)] instanceof BlockTile)))){
				return true;
			}
			return false;
	}
		
		public static void restartGame(String level){
			Game.entities.clear();
			Game.enemies.clear();
			Game.entities = new ArrayList<Entity>();
			Game.enemies = new ArrayList<Enemy>();
			Game.spritesheet = new Spritesheet("/spritesheet4.png");
			Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32,0,16,16));
			Game.entities.add(Game.player);
			Game.world = new World("/"+level); 
			return;
		}
	
	public void render(Graphics g){
		int xstart = Camera.x >> 4;
		int ystart = Camera.y >> 4;
		
		int xfinal = xstart + (Game.WIDTH >> 4);
		int yfinal = ystart + (Game.HEIGHT >> 4);
		
		for(int xx = xstart; xx <= xfinal; xx++){
			for(int yy = ystart; yy <= yfinal; yy++){
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT)
					continue;
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);
			}
			
		}
	}
	
}
