package psyc.game_engine.graphics;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class Screen extends Canvas {
	
	private static final long serialVersionUID = 1L;
	
	private int width = 0;
	private int height = 0;
	private int scale = 0;
	
	private BufferedImage buffer;
	private int[] pixel_data;
	private BufferStrategy bufferStrat;
	private Graphics stylus;
	
	public Screen(int a_width, int a_height, int a_scale) {
		width = a_width;
		height = a_height;
		scale = a_scale;
		
		buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		pixel_data = ((DataBufferInt) buffer.getRaster().getDataBuffer()).getData();
		
		setMinimumSize(new Dimension(width * scale, height * scale));
		setMaximumSize(new Dimension(width * scale, height * scale));
		setPreferredSize(new Dimension(width * scale, height * scale));
	}
	
	// Clear the back buffer
	public void ClearBuffer() {
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				pixel_data[x + y * width] = 0xff000000;
			}
		}
	}
	
	// Render a given set of pixel data
	public void RenderObject(int[] obj_pixel_data, int obj_xPos, int obj_yPos, int obj_width, int obj_height) {
		// Go through the objects pixel data blending it with the current screen back buffer pixel data
		for (int y = 0; y < obj_height; y++) {
			int screenY = y + obj_yPos;
			for (int x = 0; x < obj_width; x++) {
				int screenX = x + obj_xPos;
				// calculate the pixel indexes for both the obj and the back buffer
				int obj_pixelIndex = x + y * obj_width,
					buff_pixelIndex = screenX + screenY * width;
				// make sure this value is within the screen
				if ((screenY >= 0 && screenY < height && screenX >= 0 && screenX < width)) {
					// extra sanity check to make sure the whole buffer is not exceeded
					if (buff_pixelIndex >= 0 && buff_pixelIndex < pixel_data.length) {
						pixel_data[buff_pixelIndex] = Blend(pixel_data[buff_pixelIndex], obj_pixel_data[obj_pixelIndex]);
					}
				}
			}
		}
	}
	
	// Render the current back buffer to the screen
	public void Render() {
		bufferStrat = getBufferStrategy();
		if (bufferStrat == null) {
			createBufferStrategy(3);
			return;
		}
		
		stylus = bufferStrat.getDrawGraphics();
		
		stylus.drawImage(buffer, 0, 0, getWidth(), getHeight(), null);
		
		stylus.dispose();
		bufferStrat.show();
	}
	
	private int Blend(int originalColor, int newColor) {
		int topAlpha = 0xff000000 & newColor;
		int result = 0;
		
		if (topAlpha != 0xff000000)
			result = originalColor;
		else
			result = newColor;
		
		return result;
	}
}
