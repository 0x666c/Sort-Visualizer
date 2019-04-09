package sound;

import java.io.ByteArrayInputStream;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class Oscillator {

	private static Clip clip;
	
	private static final float SAMPLE_RATE = 22000;
	
	public static void beep(int duration, int scalar) {
		try {
			generateTone(100 + (int)(scalar * 2));
			loop(duration);
		} catch (LineUnavailableException e) {
			e.printStackTrace();
		}
	}

	private static void generateTone(int freq) throws LineUnavailableException {
		clip = AudioSystem.getClip();
		
		int intFPW = getFpw(freq);
		
		int wavelengths = 20;
		byte[] buf = new byte[2 * intFPW * wavelengths];
		AudioFormat af = new AudioFormat(SAMPLE_RATE, 8, 2, true, false);

		int maxVol = 127;
		for (int i = 0; i < intFPW * wavelengths; i++) {
			double angle = ((float) (i * 2) / ((float) intFPW)) * (Math.PI);
			buf[i * 2] = getByteValue(angle);
			buf[(i * 2) + 1] = buf[i * 2];
		}

		try {
			byte[] b = buf;
			AudioInputStream ais = new AudioInputStream(new ByteArrayInputStream(b), af, buf.length / 2);

			clip.open(ais);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private static int getFpw(int freq) {
		return (int) (SAMPLE_RATE / freq);
		// 6 / x = 2 => 6 / 2 = x => sr / freq = fpw
	}

	private static void loopSound(boolean commence) {
		if (commence) {
			clip.setFramePosition(0);
			clip.loop(Clip.LOOP_CONTINUOUSLY);
		} else {
			clip.stop();
		}
	}
	
	private static void loop(int loops) {
		clip.loop(loops);
	}

	private  static byte getByteValue(double angle) {
		int maxVol = 127;
		return (new Integer((int) Math.round(Math.sin(angle) * maxVol))).byteValue();
	}
}
