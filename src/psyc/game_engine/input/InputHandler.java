package psyc.game_engine.input;

import java.awt.MouseInfo;
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
		
		public int mouseX = 0;
		public int mouseY = 0;
		
		public boolean mouseOnScreen = false;
		
		public boolean[] mouseState; 
		
		public int mouseWheelMomentum = 0;
		
		public MouseHandler(Screen window) {
			window.addMouseListener(this);
			window.addMouseMotionListener(this);
			window.addMouseWheelListener(this);
			
			mouseState = new boolean[MouseInfo.getNumberOfButtons() + 1];
		}
		
		@Override
		public void mouseMoved(MouseEvent e) {
			// update mouse position
			mouseX = e.getX();
			mouseY = e.getY();
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {}
		
		@Override
		public void mouseDragged(MouseEvent e) {}
		
		@Override
		public void mouseEntered(MouseEvent e) {
			mouseOnScreen = true;
		}
		
		@Override
		public void mouseExited(MouseEvent e) {
			mouseOnScreen = false;
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			// update relevant mouse button states
			mouseState[e.getButton()] = true;
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {
			// update relevant mouse button states
			mouseState[e.getButton()] = false;
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
		return mouse.mouseX;
	}
	
	public int getMouseYPos() {
		return mouse.mouseY;
	}
	
	public int getMouseWheelPosition() {
		return mouse.mouseWheelMomentum;
	}
	
	public boolean isMouseOnScreen() {
		return mouse.mouseOnScreen;
	}
	
	public boolean checkMouseButton(int btnID) {
		if (btnID < 0 || btnID > (MouseInfo.getNumberOfButtons() + 1))
			return false;
		
		return mouse.mouseState[btnID];
	}
}
