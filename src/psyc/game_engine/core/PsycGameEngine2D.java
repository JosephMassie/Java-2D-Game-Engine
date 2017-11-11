package psyc.game_engine.core;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;

import psyc.game_engine.graphics.Screen;
import psyc.game_engine.graphics.Texture;
import psyc.game_engine.input.InputHandler;

public class PsycGameEngine2D implements Runnable {
	
	private static final double NANO_PER_SEC = 1000000000D;
	private static final double FPS_LIMIT = 60D;
	
	public static final int WIDTH = 500;
	public static final int HEIGHT = WIDTH / 16 * 9;
	public static final int SCALE = 1;
	public static final String TITLE = "PsyCode 2D Game Engine - Test";
	
	private JFrame frame;
	private InputHandler input;
	private Screen screen;
	
	private boolean running = false;
	private int updateCount = 0;
	
	private Texture test;
	private Rectangle clickTest;
	private Texture test2;
	private int testX = 0;
	private int testY = 0;
	private int cameraXPos = 0;
	private int cameraYPos = 0;
	
	public PsycGameEngine2D() {
		screen = new Screen(WIDTH, HEIGHT, SCALE);
		input = new InputHandler(screen);
		frame = new JFrame(TITLE);
		
		test = new Texture("/test_sprite.png");
		test2 = new Texture("/test_background.png");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		
		frame.add(screen, BorderLayout.CENTER);
		frame.pack();
		
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}
	
	public synchronized void init() {
		
	}
	
	public synchronized void start() {
		running = true;
		new Thread(this).start();
	}
	
	public void stop() {
		running = false;
	}

	@Override
	public void run() {
		long lastTime = System.nanoTime();
		double nsPerUpdate = NANO_PER_SEC / FPS_LIMIT;
		
		int updates = 0;
		int frames = 0;
		
		long lastTimer = System.currentTimeMillis();
		double timeDelta = 0;
		
		init();
		
		while (running) {
			long now = System.nanoTime();
			timeDelta += (now - lastTime) / nsPerUpdate;
			lastTime = now;
			boolean shouldRender = false;
			
			while (timeDelta >= 1) {
				updates++;
				update(timeDelta);
				timeDelta--;
				shouldRender = true;
			}
			
			if (shouldRender) {
				// after updating but before rendering clean up input data
				input.cleanUp();
				frames++;
				render();
			}
			
			// Refresh frame and update counts every second
			if (System.currentTimeMillis() - lastTimer >= 1000) {
				lastTimer += 1000;
				
				if (frames != 60 || updates != 60)
					System.out.println("fps: " + frames + " || updates: " + updates);
				
				frames = 0;
				updates = 0;
			}
		}
	}
	
	public void update(double timePassed) {
		updateCount++;
		
		if (timePassed > 1.5D)
			System.out.println("Total Updates: " + updateCount + " ns: " + timePassed);
		
		int spd = 5, camSpd = 2;
		
		// test entity controls
		if (input.checkKey(KeyEvent.VK_W))
			testY -= spd;
		
		if (input.checkKey(KeyEvent.VK_S))
			testY += spd;
		
		if (input.checkKey(KeyEvent.VK_A))
			testX -= spd;
		
		if (input.checkKey(KeyEvent.VK_D))
			testX += spd;
		
		// camera controls
		if (input.checkKey(KeyEvent.VK_UP))
			cameraYPos += camSpd;
		
		if(input.checkKey(KeyEvent.VK_DOWN))
			cameraYPos -= camSpd;
		
		if(input.checkKey(KeyEvent.VK_LEFT))
			cameraXPos += camSpd;
		
		if(input.checkKey(KeyEvent.VK_RIGHT))
			cameraXPos -= camSpd;
		
		screen.setCameraPosition(cameraXPos, cameraYPos);
		
		clickTest = new Rectangle(testX, testY, test.getWidth() + 1, test.getHeight() + 1);
		
		int clickOccured = input.checkForMouseClick(clickTest);
		
		if (clickOccured == 1)
			System.out.println("Test click worked!");
		else if (clickOccured == 0)
			System.out.println("Test click missed.");
	}
	
	public void render() {
		screen.ClearBuffer();
		
		test2.Render(screen, 0, 0);
		test.Render(screen, testX, testY);
		
		screen.Render();
	}
	
	public static void main(String[] args) {
		new PsycGameEngine2D().start();
	}
}
