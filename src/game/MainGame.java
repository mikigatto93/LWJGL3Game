package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_X;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_Z;

import org.joml.Vector2f;
import org.joml.Vector3f;

import engine.game_engine.Entity;
import engine.game_engine.GameEngine;
import engine.game_engine.IGameLogic;
import engine.game_engine.MouseInput;
import engine.game_engine.graphics.Camera;
import engine.game_engine.graphics.Mesh;
import engine.game_engine.graphics.OBJLoader;
import engine.game_engine.graphics.Texture;
import engine.window.Window;


public class MainGame implements IGameLogic {
	
	private static final float CAMERA_DISP_FACTOR = 0.05f;
	private static final float MOUSE_SENSITIVITY = 0.2f;
	public Renderer renderer;
	public static Window window;
	public Mesh mesh;
	private Entity[] entities;
	private Camera camera;
	private Vector3f camMovement;
	
	public MainGame() {
		renderer = new Renderer();
		camera = new Camera();
		camMovement = new Vector3f(0, 0, 0);
	}

	@Override
	public void init() throws Exception {
		renderer.init(window);

		/*float[] positions = new float[] {
				// V0
				-0.5f, 0.5f, 0.5f,
				// V1
				-0.5f, -0.5f, 0.5f,
				// V2
				0.5f, -0.5f, 0.5f,
				// V3
				0.5f, 0.5f, 0.5f,
				// V4
				-0.5f, 0.5f, -0.5f,
				// V5
				0.5f, 0.5f, -0.5f,
				// V6
				-0.5f, -0.5f, -0.5f,
				// V7
				0.5f, -0.5f, -0.5f,

				// For text coords in top face
				// V8: V4 repeated
				-0.5f, 0.5f, -0.5f,
				// V9: V5 repeated
				0.5f, 0.5f, -0.5f,
				// V10: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V11: V3 repeated
				0.5f, 0.5f, 0.5f,

				// For text coords in right face
				// V12: V3 repeated
				0.5f, 0.5f, 0.5f,
				// V13: V2 repeated
				0.5f, -0.5f, 0.5f,

				// For text coords in left face
				// V14: V0 repeated
				-0.5f, 0.5f, 0.5f,
				// V15: V1 repeated
				-0.5f, -0.5f, 0.5f,

				// For text coords in bottom face
				// V16: V6 repeated
				-0.5f, -0.5f, -0.5f,
				// V17: V7 repeated
				0.5f, -0.5f, -0.5f,
				// V18: V1 repeated
				-0.5f, -0.5f, 0.5f,
				// V19: V2 repeated
				0.5f, -0.5f, 0.5f,
		};
		float[] textCoords = new float[]{
				0.0f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.5f, 0.0f,

				0.0f, 0.0f,
				0.5f, 0.0f,
				0.0f, 0.5f,
				0.5f, 0.5f,

				// For text coords in top face
				0.0f, 0.5f,
				0.5f, 0.5f,
				0.0f, 1.0f,
				0.5f, 1.0f,

				// For text coords in right face
				0.0f, 0.0f,
				0.0f, 0.5f,

				// For text coords in left face
				0.5f, 0.0f,
				0.5f, 0.5f,

				// For text coords in bottom face
				0.5f, 0.0f,
				1.0f, 0.0f,
				0.5f, 0.5f,
				1.0f, 0.5f,
		};
		int[] indices = new int[]{
				// Front face
				0, 1, 3, 3, 1, 2,
				// Top Face
				8, 10, 11, 9, 8, 11,
				// Right face
				12, 13, 7, 5, 12, 7,
				// Left face
				14, 15, 6, 4, 14, 6,
				// Bottom face
				16, 18, 19, 17, 16, 19,
				// Back face
				4, 6, 7, 5, 4, 7
		};
		
		float[] normals = new float[] {
				0.5f, 0.0f, 0.1f
		};*/

		Texture texture = new Texture("grassblock.png");
		//mesh = new Mesh(positions, textCoords, normals, indices);
		mesh = OBJLoader.loadModel("cube.obj");
		mesh.setTexture(texture);
		Entity quad = new Entity(mesh);
		quad.setPosition(0, 0, -2);
		entities = new Entity[] { quad };
	}

	@Override
	public void input(Window window, MouseInput mouseInput) {
		camMovement.set(0, 0, 0);
        
		if (window.isKeyPressed(GLFW_KEY_W)) {
            camMovement.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            camMovement.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            camMovement.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            camMovement.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            camMovement.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            camMovement.y = 1;
        }
	}

	@Override
	public void update(float interval, MouseInput mouseInput) {
		// Update camera position
		camera.movePosition(camMovement.x * CAMERA_DISP_FACTOR,
				camMovement.y * CAMERA_DISP_FACTOR,
				camMovement.z * CAMERA_DISP_FACTOR);
		//System.out.println("camera update");
		
		// Update camera based on mouse            
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getMouseDelta();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
            //System.out.println(rotVec);
            //System.out.println("current" + mouseInput.getCurrentPos());
            //System.out.println("previous" + mouseInput.getPreviousPos());
        }
	}

	@Override
	public void render(Window window) {
		window.setClearColor(1.0f, 1.0f, 0.8f, 1.0f);
		renderer.render(window, camera, entities);
	}
	
	@Override
	public void cleanUp() {
		for (Entity entity : entities) {
			entity.getMesh().cleanUp();
		}
		
		renderer.cleanUp();
	}
	
	public static void main(String[] args) {
		try {
            boolean vSync = true;
            window = new Window("LWJGL3_Game", 800, 600, vSync);
            
            window.createWindow();
            
            IGameLogic gameLogic = new MainGame();
            GameEngine gameEng = new GameEngine(window, gameLogic);
            //window.setClearColor(0.3f, 0.6f, 0f, 1.0f);
            
            //Start the game
            gameEng.init();
            gameEng.run();
            
            
		} catch (Exception ex) {
            ex.printStackTrace();
            System.exit(-1);
        
		} finally {
        	// Clear up GLFW and window
            window.cleanUp();
            System.out.println("Terminated!");
        }

	}

	

}
