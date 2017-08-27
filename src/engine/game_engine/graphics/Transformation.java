package engine.game_engine.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import engine.game_engine.Entity;

public class Transformation {
	private Matrix4f projectionMatrix;
	private Matrix4f modelViewMatrix;
	private Matrix4f viewMatrix;
	
	public Transformation() {
		projectionMatrix = new Matrix4f();
		modelViewMatrix = new Matrix4f();
		viewMatrix = new Matrix4f();
	}
	
	public Matrix4f getProjectionMatrix(float fov, float width, float height, float zNear, float zFar) {
		float aspectRatio = width / height;
		projectionMatrix.identity();
		projectionMatrix.perspective(fov, aspectRatio, zNear, zFar);
		return projectionMatrix;
	}
	
	public Matrix4f getViewMatrix(Camera camera) {
		Vector3f cameraPos = camera.getPosition();
		Vector3f rotation = camera.getRotation();
		// First do the rotation so camera rotates over its position
		viewMatrix.identity();
		viewMatrix.rotate((float) Math.toRadians(rotation.x), new Vector3f(1, 0, 0));
		viewMatrix.rotate((float) Math.toRadians(rotation.y), new Vector3f(0, 1, 0));
		// Then do translation
		viewMatrix.translate(-cameraPos.x, -cameraPos.y, -cameraPos.z);
		return viewMatrix;
	}
	
	public Matrix4f getModelViewMatrix(Entity entity, Matrix4f viewMatrix) {
		Vector3f rotation = entity.getRotation();
		
		modelViewMatrix.identity();
		modelViewMatrix.translate(entity.getPosition())
				   	   .rotateX((float) Math.toRadians(-rotation.x))
				       .rotateY((float) Math.toRadians(-rotation.y))
				       .rotateZ((float) Math.toRadians(-rotation.z))
				       .scale(entity.getScale());
		
		Matrix4f viewCurr = new Matrix4f(viewMatrix);
		return viewCurr.mul(modelViewMatrix);		
	}
	
}
