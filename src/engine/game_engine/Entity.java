package engine.game_engine;

import org.joml.Vector3f;
import engine.game_engine.graphics.Mesh;

public class Entity {
	private Mesh mesh;
	private Vector3f position;
	private Vector3f rotation;
	private float scale;
	
	public Entity(Mesh mesh) {
		this.mesh = mesh;
		position = new Vector3f(0, 0, 0);
		rotation = new Vector3f(0, 0, 0);
		scale = 1;
	}
	
	public Mesh getMesh() {
		return mesh;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}
	
	public void setPosition(float x, float y, float z) {
		this.position.x = x;
		this.position.y = y;
		this.position.z = z;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}
	
	public void setRotation(float x, float y, float z) {
		this.rotation.x = x;
		this.rotation.y = y;
		this.rotation.z = z;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}
	
	
	

}
