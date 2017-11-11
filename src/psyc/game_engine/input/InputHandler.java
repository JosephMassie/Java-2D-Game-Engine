package psyc.game_engine.input;

import java.awt.MouseInfo;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import psyc.game_engine.graphics.Screen;

public class InputHandler {
	
	private class KeyBoardHandler implements KeyListener {
		
		public KeyBoardHandler(Screen window) {
			window.addKeyListener(this);
		}
		
		private class key {
			private boolean isPressed = false;
			
			public int keyCode = -1;
			
			public key(int a_keyCode) {
				keyCode = a_keyCode;
				isPressed = false;
			}
			
			public void toggle(boolean a_isPressed) {
				isPressed = a_isPressed;
			}
			
			public boolean checkState() {
				return isPressed;
			}
		}
		
		public List<key> keys = new ArrayList<key>();
		
		@Override
		public void keyTyped(KeyEvent e) {}
	
		@Override
		public void keyPressed(KeyEvent e) {
			toggleKey(e.getKeyCode(), true);
		}
	
		@Override
		public void keyReleased(KeyEvent e) {
			toggleKey(e.getKeyCode(), false);
		}
		
		private void toggleKey(int keyCode, boolean isPressed) {
			key targetKey = checkIfKeyExists(keyCode);
			
			if (targetKey != null)
				targetKey.toggle(isPressed);
			else {
				keys.add(new key(keyCode));
				targetKey = checkIfKeyExists(keyCode);
				targetKey.toggle(isPressed);
			}
		}
		
		private key checkIfKeyExists(int keyCode) {
			if (keys.isEmpty())
				return null;
			
			for (int i = 0; i < keys.size(); i++) {
				if (keys.get(i).keyCode == keyCode)
					return keys.get(i);
			}
			
			return null;
		}
		
		public boolean checkKey(int keyCode) {
			key targetKey = checkIfKeyExists(keyCode);
			
			if (targetKey != null)
				return targetKey.checkState();
			else
				return false;
		}
	}
	
	private class MouseHandler extends MouseAdapter {
		
		public int x = 0;
		public int y = 0;
		
		public boolean onScreen = false;
		
		public boolean clickOccurred = false;
		public int clickX = -1;
		public int clickY = -1;
		
		public boolean[] buttonStates; 
		
		public int mouseWheelMomentum = 0;
		
		public MouseHandler(Screen window) {
			window.addMouseListener(this);
			window.addMouseMotionListener(this);
			window.addMouseWheelListener(this);
			
			buttonStates = new boolean[MouseInfo.getNumberOfButtons() + 1];
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// update mouse position
			x = e.getX();
			y = e.getY();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			clickOccurred = true;
			// save the specific location the click occurred at just in case the mouse is moved
			clickX = e.getX();
			clickY = e.getY();
			System.out.println("Clicked! " + clickOccurred + " at x:" + clickX + " y:" + clickY);
		}
		
		@Override
		public void mouseDragged(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			onScreen = true;
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			onScreen = false;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// update relevant mouse button states
			buttonStates[e.getButton()] = true;
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// update relevant mouse button states
			buttonStates[e.getButton()] = false;
		}
		
		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			mouseWheelMomentum = e.getWheelRotation();
		}
	}
	
	private KeyBoardHandler keyboard;
	private MouseHandler mouse;
	
	public InputHandler(Screen window) {
		keyboard = new KeyBoardHandler(window);
		mouse = new MouseHandler(window);
	}
	
	public boolean checkKey(int keyCode) {
		return keyboard.checkKey(keyCode);
	}
	
	public int getMouseXPos() {
		return mouse.x;
	}
	
	public int getMouseYPos() {
		return mouse.y;
	}
	
	public int getMouseWheelPosition() {
		return mouse.mouseWheelMomentum;
	}
	
	public boolean isMouseOnScreen() {
		return mouse.onScreen;
	}
	
	public boolean checkMouseButton(int btnID) {
		if (btnID < 0 || btnID > (MouseInfo.getNumberOfButtons() + 1))
			return false;
		
		return mouse.buttonStates[btnID];
	}
	
	// Returns -1 if no click occurred, 0 if a click occurred but was not in the provided area, and 1 if one occurred in the provided area
	public int checkForMouseClick(Rectangle screen_location) {
		// No click occurred or the mouse is out of bounds
		if (!mouse.clickOccurred || !mouse.onScreen)
			return -1;
		
		// A click occurred so check if it was with the provided area
		if (screen_location.contains(mouse.clickX, mouse.clickY))
			return 1;
		
		// A click occurred but was not within the given area
		return 0;
	}
	
	// Used to clear single tick/update data before moving on, make sure this is called at the end of the main update function
	public void cleanUp() {
		mouse.clickOccurred = false;
	}
}
