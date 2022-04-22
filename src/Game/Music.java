package Game;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Music {
	public Clip backgroundMusic, gameMusic1, gameMusic2;
	public Clip effect1, effect2, effect3;
	
	public Music() {
		
		load();
	}
	
	public void load() {
		try {
			backgroundMusic = AudioSystem.getClip();
			File audioFile = new File("src/music/backGround.wav");
			AudioInputStream audioStream1 = AudioSystem.getAudioInputStream(audioFile);
			backgroundMusic.open(audioStream1);
				
			effect1 = AudioSystem.getClip();
			audioFile = new File("src/music/die.wav");
			AudioInputStream audioStream4 = AudioSystem.getAudioInputStream(audioFile);
			effect1.open(audioStream4);
			
			effect2 = AudioSystem.getClip();
			audioFile = new File("src/music/get.wav");
			AudioInputStream audioStream5 = AudioSystem.getAudioInputStream(audioFile);
			effect2.open(audioStream5);
	
		}
		catch(LineUnavailableException e) {e.printStackTrace();} 
		catch (UnsupportedAudioFileException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void play(String list) {
		
		switch(list) {
		case "bg":
			backgroundMusic.setFramePosition(0);
			backgroundMusic.start();
			break;
		case "die":
			effect1.setFramePosition(0);
			effect1.start();
			break;
		case "get":
			effect2.setFramePosition(0);
			effect2.start();
			break;

		}
	}
	
	public void allStop() {
	
	}
	
	public void stop(String list) {
		
		switch(list) {
		case "bg":
			backgroundMusic.stop();
			break;
		case "game1":
			gameMusic1.stop();
		case "game2":
			gameMusic2.stop();
		
		}
	}
	
}
