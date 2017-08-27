package engine.game_engine.graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector3f;
import org.lwjgl.system.MemoryUtil;

public class Mesh {
	
	private static final Vector3f DEFAULT_COLOR = new Vector3f(1.0f, 1.0f, 1.0f);
	private int vaoID;
	private List<Integer> vboIdList;
	private int vertexCount;
	private Texture texture;
	private Vector3f color;
	
	public Mesh(float[] positions, float[] textCoords, float[] normals, int[] indices) {
		vertexCount = indices.length;
		vboIdList = new ArrayList<Integer>();
		color = DEFAULT_COLOR;
		
		// Create the VAO and bind to it
		vaoID = glGenVertexArrays();
		glBindVertexArray(vaoID);
		
		createFloatVBO(positions, 0);
		
		createFloatVBO(textCoords, 1, 2, false, 0, 0);
		
		createFloatVBO(normals, 2);
		
		createIndexBuffer(indices);
		
		// Unbind the VBO
		//glBindBuffer(GL_ARRAY_BUFFER, 0);
		// Unbind the VAO
		//glBindVertexArray(0);			
	}
	
	private void createFloatVBO(float[] data, int location) {
		// Create a float buffer for positions
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		// Put the data into the just-created buffer
		buffer.put(data).flip();

		// Create the positions VBO and bind it
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		// Put the data into the VBO
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		// Define structure of the data
		glVertexAttribPointer(
			location, // Location in the shader
			3, // Size  
			GL_FLOAT, // Data-type
			false, // normalization
			0, // stride: Specifies the byte offset between consecutive generic vertex attributes
			0 // offset: Specifies an offset to the first component in the buffer
		);
		// Free the memory allocated
		if (buffer != null) {
			MemoryUtil.memFree(buffer);
		}
		
		vboIdList.add(vboID);
	}
	
	private void createFloatVBO(float[] data, int location, int size, boolean norm, int stride, int offset) {
		// Create a float buffer for positions
		FloatBuffer buffer = MemoryUtil.memAllocFloat(data.length);
		// Put the data into the just-created buffer
		buffer.put(data).flip();

		// Create the positions VBO and bind it
		int vboID = glGenBuffers();
		glBindBuffer(GL_ARRAY_BUFFER, vboID);
		// Put the data into the VBO
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		// Define structure of the data
		glVertexAttribPointer(
			location, // Location in the shader
			size, // Size  
			GL_FLOAT, // Data-type
			norm, // normalization
			stride, // stride: Specifies the byte offset between consecutive generic vertex attributes
			offset // offset: Specifies an offset to the first component in the buffer
		);
		// Free the memory allocated
		if (buffer != null) {
			MemoryUtil.memFree(buffer);
		}
		
		vboIdList.add(vboID);
	}
	
	private void createIndexBuffer(int[] indices) {
		// Create a int buffer for indices
		IntBuffer indexBuffer = MemoryUtil.memAllocInt(indices.length);
		// Put the data into the just-created buffer
		indexBuffer.put(indices).flip();
		// Create the index buffer
		int indexVboID = glGenBuffers();
		glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, indexVboID);
		// Put the data into the VBO
		glBufferData(GL_ELEMENT_ARRAY_BUFFER, indexBuffer, GL_STATIC_DRAW);
		// Free the memory allocated
		if (indexBuffer != null) {
			MemoryUtil.memFree(indexBuffer);
		}
		
		vboIdList.add(indexVboID);
	}
	
	public int getVaoID() {
		return vaoID;
	}

	public int getVertexCount() {
		return vertexCount;
	}
	
	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}

	public Vector3f getColor() {
		return color;
	}

	public void setColor(Vector3f color) {
		this.color = color;
	}
	
	public boolean isTextured() {
		return (texture != null);
	}
	
	public void render() {
		if (isTextured()) {
			// Activate first texture bank
			glActiveTexture(GL_TEXTURE0);
			// Bind the texture
			glBindTexture(GL_TEXTURE_2D, texture.getId());
		}
		// Bind to the VAO
        glBindVertexArray(getVaoID());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        // Draw the mesh
       
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        
        glBindTexture(GL_TEXTURE_2D, 0);
        glBindVertexArray(0);
	}

	public void cleanUp() {
		glDisableVertexAttribArray(0);
		glDisableVertexAttribArray(1);
		
		// Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
		for (int vboId : vboIdList) {
			 glDeleteBuffers(vboId);
		}
		
		if(isTextured()) {
			texture.cleanUp();
		}
		
		// Delete the VAO
		glBindVertexArray(0);
		glDeleteVertexArrays(vaoID);
	}
	
}
