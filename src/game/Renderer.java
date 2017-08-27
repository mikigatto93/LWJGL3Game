package game;

import engine.game_engine.Entity;
import engine.game_engine.Utils;
import engine.game_engine.graphics.Camera;
import engine.game_engine.graphics.Mesh;
import engine.game_engine.graphics.ShaderProgram;
import engine.game_engine.graphics.Transformation;
import engine.window.Window;
import static org.lwjgl.opengl.GL11.*;
import org.joml.Matrix4f;

public class Renderer {
	
	private static final float FOV = (float) Math.toRadians(60.0f);
	private static final float Z_NEAR = 0.1f;
	private static final float Z_FAR = 1000.0f;
	private ShaderProgram program;
	private Transformation transformation; 
	
	
	public Renderer() {
		transformation = new Transformation();
	}
	
	public void init(Window window) throws Exception {
		
		program = new ShaderProgram();
		program.createVertexShader(Utils.loadShader("vert.vs"));
		program.createFragmentShader(Utils.loadShader("frag.fs"));
		program.link();
		
        program.getUniform("projection");
        program.getUniform("modelView");
        program.getUniform("textureSampler");
        program.getUniform("useColor");
        program.getUniform("color");
	
	}
	
	public void render(Window window, Camera camera, Entity[] entities) {
		clear();
		
		if ( window.isResized() ) {
            glViewport(0, 0, window.getWidth(), window.getHeight());
            window.setResized(false);
        }
		
		program.bind();
		
		Matrix4f projectionMatrix = transformation.getProjectionMatrix(FOV, window.getWidth(), window.getHeight(), 
				Z_NEAR, Z_FAR);

		program.setUniform("projection", projectionMatrix);
		
		// Update view Matrix
		Matrix4f viewMatrix = transformation.getViewMatrix(camera);
				
		program.setUniform("textureSampler", 0);
		
		for (Entity entity : entities) {
	        // Create the world matrix for this entity
			Mesh mesh = entity.getMesh();
			
			// Create the world matrix for this entity
			Matrix4f modelViewMatrix = transformation.getModelViewMatrix(entity, viewMatrix);
			program.setUniform("modelView", modelViewMatrix);
			
			program.setUniform("useColor", (mesh.isTextured() ? 0 : 1));
			program.setUniform("color", mesh.getColor());
			
			// Render the mesh for this game entity
			mesh.render();
		}
        
		program.unbind();
        
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
	}
	
	public void cleanUp() {
		if (program != null) {
            program.cleanUp();
        }
	}
	
	
}
