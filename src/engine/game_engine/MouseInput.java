package engine.game_engine;

import org.joml.Vector2d;
import org.joml.Vector2f;

import engine.window.Window;
import static org.lwjgl.glfw.GLFW.*;

public class MouseInput {
	private Vector2d previousPos;
	private Vector2d currentPos;
	private Vector2f mouseDelta;
	
	private boolean inWindow = false;
	private boolean leftButtonPressed = false;
	private boolean rightButtonPressed = false;
	
	public MouseInput() {
		previousPos = new Vector2d(-1, -1);
		currentPos = new Vector2d(0, 0);
		mouseDelta = new Vector2f();
	}
	
	public void init(Window window) {
		// Set a mouse move event callback
		glfwSetCursorPosCallback(window.getID(), (windowID, xpos, ypos) -> {
			currentPos.x = xpos;
			currentPos.y = ypos;
		});
		
		// Set a mouse enter event callback
		glfwSetCursorEnterCallback(window.getID(), (windowID, isEntered) -> {
			inWindow = isEntered;
		});
		
		// Set a mouse botton click callback
		glfwSetMouseButtonCallback(window.getID(), (windowID, button, action, mode) -> {
			leftButtonPressed = (button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS);
			rightButtonPressed = (button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS);
		});
		
	}
	
	public void input(Window window) {
		mouseDelta.x = 0;
		mouseDelta.y = 0; 
		
		if (previousPos.x > 0 && previousPos.y > 0 && inWindow) {
			double dx = currentPos.x - previousPos.x;
			double dy = currentPos.y - previousPos.y;

			boolean rotateX = (dx != 0);
			boolean rotateY = (dy != 0);
			
			if (rotateX) {
				mouseDelta.y = (float) dx;
			}

			if (rotateY) {
				mouseDelta.x = (float) dy;
			}
		}

		previousPos.x = currentPos.x;
		previousPos.y = currentPos.y;	 
	}
	
	public Vector2f getMouseDelta() {
		return mouseDelta;
	}

	public boolean isLeftButtonPressed() {
		return leftButtonPressed;
	}

	public boolean isRightButtonPressed() {
		return rightButtonPressed;
	}
	
	public Vector2d getPreviousPos() {
		return previousPos;
	}

	public Vector2d getCurrentPos() {
		return currentPos;
	}

	public boolean isInWindow() {
		return inWindow;
	}
	
}
