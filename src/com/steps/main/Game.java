package com.steps.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.steps.entities.Bullet;
import com.steps.entities.Enemy;
import com.steps.entities.Entity;
import com.steps.entities.Player;
import com.steps.graphics.Spritesheet;
import com.steps.graphics.UI;
import com.steps.world.Camera;
import com.steps.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener,MouseMotionListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean rodando = true;
	public static final int WIDTH = 240;
	public static final int HEIGHT = 160;
	public static final int SCALE = 4;
	
	public boolean saveGame = false;
	
	private int CUR_LEVEL = 1,MAX_LEVEL = 2;
	
	public static String gameState = "MENU";
	private boolean showMessageGameOver = true;
	private int framesGameOver = 0;
	private boolean restartGame = false;
	
	private BufferedImage image;
	
	public static List<Entity> entities;
	
	public static List<Enemy> enemies;
	
	public static List<Bullet> bullets;
	
	public static Spritesheet spritesheet;
	
	public static World world;
	
	public static Player player;
	
	public static Random rand;
	
	public Menu menu;
	
	public UI ui;
	
	public static int mx, my;
	
	public int[] pixels;
	
	public Game() {
		rand = new Random();
		addKeyListener(this);
		addMouseListener(this);
		addMouseMotionListener(this);
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//inicializar objetos.
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		bullets = new ArrayList<Bullet>();
		spritesheet = new Spritesheet("/spritesheet4.png");
		player = new Player(0,0,16,16,spritesheet.getSprite(32,0,16,16));
		entities.add(player);
		world = new World("/level1.png"); 
		menu = new Menu();
	}
	
	public void initFrame(){
		frame = new JFrame("Meu Joguinho #1");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start(){
		thread = new Thread(this);
		rodando = true;
		thread.start();
	}
	
	public synchronized void stop(){
		rodando = false;
		try {
			thread.join();
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args){
		Game game = new Game();
		game.start();
	}
	
	public void update(){
		if(gameState == "NORMAL"){
		if(this.saveGame){
			saveGame = false;
			String[] opt1 = {"level", "life","pos"};
			int[] opt2 = {this.CUR_LEVEL, (int)player.life,player.getX() - Camera.x,player.getY() - Camera.y - player.z};
			Menu.saveGame(opt1,opt2,10);
			System.out.println("JOGO SALVO!");
		}	
		restartGame = false;
		for(int i = 0; i < entities.size();i++){
			Entity e = entities.get(i);
			e.update();
		}
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).update();
		}
		if(enemies.size() == 0){
			CUR_LEVEL++;
			if(CUR_LEVEL > MAX_LEVEL){
				CUR_LEVEL = 1;
			}
			String newWorld = "level" + CUR_LEVEL + ".png";
			World.restartGame(newWorld);
		}
	}else if(gameState == "GAME_OVER"){
		framesGameOver++;
		if(framesGameOver == 30){
			framesGameOver = 0;
			if(showMessageGameOver)
				showMessageGameOver = false;
				else
				showMessageGameOver = true;
		}
		if(restartGame){
			restartGame = false;
			gameState = "NORMAL";
			CUR_LEVEL = 1;
			String newWorld = "level" + CUR_LEVEL + ".png";
			World.restartGame(newWorld);
		}
	}else if(gameState == "MENU"){
		menu.update();
	}
}

	
	public void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		Graphics g  = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0,0,WIDTH*SCALE,HEIGHT*SCALE);
		//RENDERIZAÇAO DO GAME!
		world.render(g);
		for(int i = 0; i < entities.size();i++){
			Entity e = entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).render(g);
		}
		ui.render(g);
		//
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		g.setFont(new Font("calibri",Font.BOLD,20));
		g.setColor(Color.white);
		g.drawString("AMMO: "+player.ammo,580,64);
			if(gameState == "GAME_OVER"){
				Graphics2D g2 = (Graphics2D) g;
				g2.setColor(new Color(0,0,0,180));
				g2.fillRect(0, (HEIGHT*SCALE) /2 -100, (WIDTH*SCALE), (HEIGHT*SCALE) /4);
				g.setColor(Color.RED);
				g.setFont(new Font("calibri",Font.BOLD,80));
				g.drawString("VOCÊ MORREU",(WIDTH*SCALE) /4 ,(HEIGHT*SCALE) /2);
				g.setColor(Color.WHITE);
				g.setFont(new Font("calibri",Font.BOLD,30));
			if(showMessageGameOver)
				g.drawString(">Pressione ENTER para reiniciar!<",(WIDTH*SCALE) /2 -200 ,(HEIGHT*SCALE) /2 +90);
		}else if(gameState == "MENU"){ 
			menu.render(g);
		}
		bs.show();
	}

	public void run(){
		requestFocus();
		long lastTime = System.nanoTime();
		double amountOfTicks = 60.0;
		double ns = 1000000000 / amountOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(rodando){
			long now = System.nanoTime();
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1) {
				update();
				render();
				frames++;
				delta--;
			}
			
			if(System.currentTimeMillis() - timer >= 1000){
				System.out.println("FPS: "+frames);
				frames = 0;
				timer += 1000;
			}
		}
	
		stop();
	}
	@Override
	
	public void keyPressed(KeyEvent e){
		
		if(e.getKeyCode() == KeyEvent.VK_Z ||
			e.getKeyCode() == KeyEvent.VK_SPACE){
			player.jump = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
			e.getKeyCode() == KeyEvent.VK_D){
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
			e.getKeyCode() == KeyEvent.VK_A){
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP ||
			e.getKeyCode() == KeyEvent.VK_W){
			player.up = true;
			if(gameState == "MENU"){
				menu.up = true;
				Sound.selectFx.play();
			}
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
			e.getKeyCode() == KeyEvent.VK_S){
			player.down = true;
			if(gameState == "MENU"){
				menu.down = true;
				Sound.selectFx.play();
			}
		}
		if(e.getKeyCode()== KeyEvent.VK_X){
			player.shoot = true;
	}
		if(e.getKeyCode()== KeyEvent.VK_ENTER){
			restartGame = true;
			if(gameState == "MENU"){
				menu.enter = true;
				/*if(menu.enter == true){
					Sound.pickup2Fx.play();
					Sound.musicBackground.loop();
				}*/
			}if(gameState == "GAME_OVER"){
				Sound.musicBackground.loop();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			if(gameState == "NORMAL"){
			gameState = "MENU";
			menu.pause = true;
		}
			if(gameState == "MENU"){
				Sound.musicBackground.stop();
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ALT){
			if(gameState == "NORMAL"){
				saveGame = true;
			}
		}
	}
	@Override
	
	public void keyReleased(KeyEvent e){	
			if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D){
				player.right = false;
			}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A){
				player.left = false;
			}
			if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W){
				player.up = false;
				
			}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S){
				player.down = false;
			}
			if(e.getKeyCode()== KeyEvent.VK_X){
				player.shoot = false;
			}
		}
	@Override
	public void keyTyped(KeyEvent arg0){
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		player.mouseShoot = true;
		player.mx = (e.getX()/SCALE);
		player.my = (e.getY()/SCALE);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		this.mx = e.getX();
		this.my = e.getY();
	}
	
}

