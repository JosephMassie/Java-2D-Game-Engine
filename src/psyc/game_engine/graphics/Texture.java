package psyc.game_engine.graphics;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Texture {
	
	private String filePath;
	private int width;
	private int height;
	
	private BufferedImage imageData;
	private int[] pixels;
	
	private static final int MASK = 0xffff00ff;
	
	public Texture(String target_filePath) {
		imageData = null;
		
		try {
			imageData = ImageIO.read(Texture.class.getResourceAsStream(target_filePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (imageData == null)
			return;
		
		filePath = target_filePath;
		width = imageData.getWidth();
		height = imageData.getHeight();
		
		pixels = imageData.getRGB(0, 0, width, height, null, 0, width);
		
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
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
}
