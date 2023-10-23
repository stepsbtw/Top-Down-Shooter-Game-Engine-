package com.steps.graphics;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.steps.main.Game;

public class UI {

	
	public void render(Graphics g){
		g.setColor(Color.red);
		g.fillRect(180,8,50,8);
		g.setColor(Color.green);
		g.fillRect(180,8,(int)((Game.player.life/Game.player.max_life)*50),8);
		g.setColor(Color.WHITE);
		g.setFont(new Font("calibri",Font.BOLD,8));
		g.drawString((int)Game.player.life+"/"+(int)Game.player.max_life,192,14);

		/*g.setColor(Color.yellow);
		g.fillRect(180,24,(Player.ammo),8);
		g.setColor(Color.WHITE);
		g.setFont(new Font("calibri",Font.BOLD,8));
		g.drawString(Player.ammo+"/"+20,192,30);*/
	}
	
	
}
