package psyc.game_engine.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Sprite {
	
	private String filePath;
	private int width;
	private int height;
	
	private int[] pixels;
	
	private static final int MASK = 0xffff00ff;
	
	public Sprite(String target_filePath) {
		BufferedImage imageFile = null;
		
		try {
			imageFile = ImageIO.read(Sprite.class.getResourceAsStream(target_filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (imageFile == null)
			return;
		
		filePath = target_filePath;
		width = imageFile.getWidth();
		height = imageFile.getHeight();
		
		pixels = imageFile.getRGB(0, 0, width, height, null, 0, width);
		
		for (int i = 0; i < pixels.length; i++) {
			// Replace mask color with null pixel data with no alpha
			pixels[i] = pixels[i] == MASK ? 0 : pixels[i];
		}
	}
	
	public void Render(Screen screen, int x, int y) {
		screen.RenderObject(pixels, x, y, width, height);
	}
	
	public String GetFilePath() {
		return filePath;
	}
}
