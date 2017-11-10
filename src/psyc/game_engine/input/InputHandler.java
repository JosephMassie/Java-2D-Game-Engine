package psyc.game_engine.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

import psyc.game_engine.graphics.Screen;

public class InputHandler implements KeyListener {
	
	public InputHandler(Screen window) {
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
