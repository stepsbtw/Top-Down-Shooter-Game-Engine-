package com.steps.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.steps.world.Camera;
import com.steps.world.World;

public class Menu {

	
	public String[] options = {"NOVO JOGO","CARREGAR JOGO","SAIR"};
	
	public int currentOption = 0;
	
	public int maxOption = options.length -1;
	
	public boolean up,down,enter;
	
	public static boolean pause = false;
	
	public static boolean saveExists = false;
	
	public static boolean saveGame = false;
	
	public void update(){
		File file = new File("save.txt");
		if(file.exists()){
			saveExists = true;
		}else{
			saveExists = false;
		}
		if(up){
			up = false;
			currentOption--;
			if(currentOption < 0)
				currentOption = maxOption;
		}else if(down){
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if(enter){
			enter = false;
			if(options[currentOption] == "NOVO JOGO"){
				Game.gameState = "NORMAL";
				Sound.pickup2Fx.play();
				Sound.musicBackground.loop();
				pause = false;
				file = new File("save.txt");
				file.delete();
			}else if(options[currentOption] == "SAIR"){
				System.exit(1);	
			}else if(options[currentOption] == "CARREGAR JOGO"){
				file = new File("save.txt");
				if(file.exists()){
					Sound.pickup2Fx.play();
					Sound.musicBackground.loop();
					String saver = loadGame(10);
					applySave(saver);
				}
			}
		}
	}
	
	public static void applySave(String str){
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++){
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]){
			case "level":
				World.restartGame("level" + spl2[1] + ".png");
				Game.gameState = "NORMAL";
				pause = false;
				break;
			case "life":
				Game.player.life = Integer.parseInt(spl2[1]);
				break;
			}	
		}
	}
	
	public static String loadGame(int encode){
		
		String line = "";
		File file = new File("save.txt");
		if(file.exists()){
			try{
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try{
					while((singleLine = reader.readLine()) != null){
						String[] trans = singleLine.split(":");
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++){
							val[i]-= encode;
							trans[1]+= val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				}catch(IOException e){}
			}catch(FileNotFoundException e){}
		}
		
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode){
		BufferedWriter write = null;
		try{
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e){
			e.printStackTrace();
		}
		for(int i = 0; i < val1.length;i++){
			String current = val1[i];
			current += ":";
			char[] value = Integer.toString(val2[i]).toCharArray();
				for(int n = 0; n < value.length;n++){
					value [n] += encode;
					current += value[n];
				}
			try{
				write.write(current);
				if(i < val1.length - 1 ){
					write.newLine();
				}
			}catch(IOException e) {}
		}
		try{
			write.flush();
			write.close();
		}catch(IOException e){}
	}
	
	public void render(Graphics g){
		if(pause == false){
		g.setColor(Color.WHITE);
		g.fillRect(0,0, Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE);
		g.setColor(Color.BLACK);
		g.setFont(new Font("calibri",Font.BOLD,80));
		g.drawString("> Jogo Brabo <", (Game.WIDTH*Game.SCALE) /4 , (Game.HEIGHT*Game.SCALE) /4);
		//OPÇÕES
		g.setColor(Color.BLACK);
		g.setFont(new Font("calibri",Font.BOLD,40));
		g.drawString("Novo Jogo", (Game.WIDTH*Game.SCALE) /4 + 150 , (Game.HEIGHT*Game.SCALE) /3 + 20);
		g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) /4 + 125 , (Game.HEIGHT*Game.SCALE) /3 + 80);
		g.drawString("Sair", (Game.WIDTH*Game.SCALE) /4 + 200 , (Game.HEIGHT*Game.SCALE) /3 + 140);	
		}else{
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color(0,0,0,180));
			g2.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
			g.setColor(Color.WHITE);
			g.setFont(new Font("calibri",Font.BOLD,80));
			g.drawString("> Jogo Brabo <", (Game.WIDTH*Game.SCALE) /4 , (Game.HEIGHT*Game.SCALE) /4);
			g.setFont(new Font("calibri",Font.BOLD,40));
			g.drawString("Continuar", (Game.WIDTH*Game.SCALE) /4 + 155 , (Game.HEIGHT*Game.SCALE) /3 + 20);
			g.drawString("Carregar Jogo", (Game.WIDTH*Game.SCALE) /4 + 125 , (Game.HEIGHT*Game.SCALE) /3 + 80);
			g.drawString("Sair", (Game.WIDTH*Game.SCALE) /4 + 200 , (Game.HEIGHT*Game.SCALE) /3 + 140);	
		}
		if(options[currentOption] == "NOVO JOGO"){
			g.drawString(">", (Game.WIDTH*Game.SCALE) /4 + 100 , (Game.HEIGHT*Game.SCALE) /3 + 20);
		}else if(options[currentOption] == "CARREGAR JOGO"){
			g.drawString(">", (Game.WIDTH*Game.SCALE) /4 + 75 , (Game.HEIGHT*Game.SCALE) /3 + 80);
		}else if(options[currentOption] == "SAIR"){
			g.drawString(">", (Game.WIDTH*Game.SCALE) /4 + 150 , (Game.HEIGHT*Game.SCALE) /3 + 140);
		}
	} 



}
