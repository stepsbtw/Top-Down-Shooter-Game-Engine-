package com.steps.main;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound("/musica do jogo2(certo).wav");
	
	public static final Sound hurtFx = new Sound("/hurt.wav");
	
	public static final Sound deathFx = new Sound("/death.wav");
	
	public static final Sound pickupFx = new Sound("/item.wav");
	
	public static final Sound pickup2Fx = new Sound("/item2.wav");
	
	public static final Sound selectFx = new Sound("/select.wav");
	
	public static final Sound shootFx = new Sound("/shoot.wav");
	
	public static final Sound jumpFx = new Sound("/jump.wav");
	
	private Sound(String name){
		try{
			clip = Applet.newAudioClip(Sound.class.getResource(name));
		}catch(Throwable e){}
	}
	
	public void play(){
		try{
			new Thread(){
				public void run(){
					clip.play();
				}
			}.start();
		}catch(Throwable e) {}
	}
	
	public void loop(){
		try{
			new Thread(){
				public void run(){
					clip.loop();
				}
			}.start();
		}catch(Throwable e) {}
	}
		
	public void stop(){
		try{
			new Thread(){
				public void run(){
					clip.stop();
				}
			}.start();
		}catch(Throwable e) {}
	}

}
	