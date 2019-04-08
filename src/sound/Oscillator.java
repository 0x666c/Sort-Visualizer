package sound;

import java.applet.AudioClip;
import java.io.ByteArrayInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Oscillator {
	
	private static Clip soundClip = null;
	
	private static void _beep(float bound) throws Exception { // From MIN_HEIGHT (0f) to MAX_HEIGHT (1f)
		if(soundClip != null) {
			soundClip.stop();
			soundClip.close();
		}
		soundClip = AudioSystem.getClip();
		
		int sampleRate = 22050;
		int framesPerWl = 100;
		
		byte[] buf = new byte[2 * sampleRate * 20];
		AudioFormat f = new AudioFormat(sampleRate, 8, 2, true, false);
		
		int mvol = 127;
		for (int i = 0; i < framesPerWl * 20; i++) {
			double angl = ((float)(i * 2) / ((float)framesPerWl)) * Math.PI;
			buf[i * 2] = getByteValue(angl);
			
			buf[(i * 2) + 1] = buf[i * 2];
		}
		
		try {
			byte[] b = buf;
			AudioInputStream in = new AudioInputStream(new ByteArrayInputStream(b), f, buf.length / 2);
			
			soundClip.open(in);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void beep(float bound) { // try-catch wrapper for better looking code 
		try {
			_beep(bound);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static byte getByteValue(double angle) {
		int maxVol = 127;
		return (new Integer((int) Math.round(Math.sin(angle) * maxVol))).byteValue();
	}
}
