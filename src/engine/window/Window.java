package engine.window;

import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.Callbacks.*;

import org.lwjgl.Version;

public class Window {
		
		// Properties
		private long windowID;
		private String title;
		private int height, width;
		private boolean vSync, resized;
		
		// Constructors
		public Window(String title, int width, int height, boolean vSync) {
			this.title = title;
			this.width = width;
			this.height = height;
			this.vSync = vSync;
			this.resized = false;
		}
		
		public Window() {
			this.title = "No title";
			this.width = 600;
			this.height = 400;
			this.vSync = false;
			this.resized = false;
		}
		
		// Methods
		public void createWindow() {
			GLFWErrorCallback.createPrint(System.err).set();

			// Initialize GLFW. Most GLFW functions will not work before doing this.
			if ( !glfwInit() )
				throw new IllegalStateException("Unable to initialize GLFW");

			// Configure GLFW
			glfwDefaultWindowHints(); // optional, the current window hints are already the default
			glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE); // the window will stay hidden after creation
			glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE); // the window will be resizable
			
			// Create the window
			windowID = glfwCreateWindow(width, height, title, NULL, NULL);
			if ( windowID == NULL )
				throw new RuntimeException("Failed to create the GLFW window");

			// Setup a key callback. It will be called every time a key is pressed, repeated or released.
			glfwSetKeyCallback(windowID, (window, key, scancode, action, mods) -> {
				if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
					glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
			});
	        
			glfwSetWindowPos(windowID, 50, 50);

	        // Make the OpenGL context current
	        glfwMakeContextCurrent(windowID);

	        if (isvSync()) {
	            // Enable v-sync
	            glfwSwapInterval(1);
	        }

	        // Make the window visible
	        glfwShowWindow(windowID);

	        GL.createCapabilities();

	        // Set the clear color
	        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
	        
	        // glEnable
	        glEnable(GL_DEPTH_TEST);
	        
	        
	        System.out.println(Version.getVersion());
	    }
		
		public void update() {
	        glfwSwapBuffers(windowID);
	        glfwPollEvents();  
	    }

		public void setClearColor(float r, float g, float b, float a) {
			glClearColor(r, g, b, a);
		}
		
		public boolean isKeyPressed(int keyCode) {
	        return glfwGetKey(windowID, keyCode) == GLFW_PRESS;
	    }
		
		public boolean windowShouldClose() {
	        return glfwWindowShouldClose(windowID);
	    }
		
		public void setWindowPosition(int x, int y) {
			glfwSetWindowPos(windowID, x, y);
		}
		
		// Getters and setters
		public long getID() {
			return windowID;
		}
		
		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public boolean isvSync() {
			return vSync;
		}

		public void setvSync(boolean vSync) {
			this.vSync = vSync;
		}

		public boolean isResized() {
			return resized;
		}

		public void setResized(boolean resized) {
			this.resized = resized;
		}

		public String getTitle() {
			return title;
		}
		
		public void setTitle(String title) {
			this.title = title;
		}
		
		public void cleanUp() {
			// Free the window callbacks and destroy the window
			glfwFreeCallbacks(windowID);
			glfwDestroyWindow(windowID);

			// Terminate GLFW and free the error callback
			glfwTerminate();
			glfwSetErrorCallback(null).free();
		}
		
}
		