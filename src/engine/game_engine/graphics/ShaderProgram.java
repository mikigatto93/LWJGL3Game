package engine.game_engine.graphics;

import static org.lwjgl.opengl.GL20.*;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;

public class ShaderProgram {
	
	private final int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	private boolean debug = true;
	private Map<String, Integer> uniforms;
	
	public ShaderProgram() throws Exception {
		programID = glCreateProgram();
		if (programID == 0) {
			throw new Exception("Could not create Shader");
		}
		
		uniforms = new HashMap<>();
	}
	
	public void getUniform(String uniformName) throws Exception {
		int uniformLocation = glGetUniformLocation(programID, uniformName);
		if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
		
		uniforms.put(uniformName, uniformLocation);
		
	}
	
	public void setUniform(String uniformName, Matrix4f matrix) {
		try (MemoryStack stack = MemoryStack.stackPush()) {
			FloatBuffer buffer = stack.mallocFloat(16);
			matrix.get(buffer);
			glUniformMatrix4fv(uniforms.get(uniformName), false, buffer);
		}
	}
	
	public void setUniform(String uniformName, int value) {
		glUniform1i(uniforms.get(uniformName), value);
		
	}
	
	public void setUniform(String uniformName, Vector3f vec) {
		glUniform3f(uniforms.get(uniformName), vec.x, vec.y, vec.z);
	}
	
	public void createVertexShader(String shaderCode) throws Exception {
		vertexShaderID = createShader(shaderCode, GL_VERTEX_SHADER);
	}
	
	public void createFragmentShader(String shaderCode) throws Exception {
		fragmentShaderID = createShader(shaderCode, GL_FRAGMENT_SHADER);
	}
	
	private int createShader(String shaderCode, int shaderType) throws Exception {
		int shaderId = glCreateShader(shaderType);
		if (shaderId == 0) {
			throw new Exception("Error creating shader. Type: " + shaderType);
		}
		
		glShaderSource(shaderId, shaderCode);
		glCompileShader(shaderId);
		
		if (glGetShaderi(shaderId,  GL_COMPILE_STATUS) == 0) {
			throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
		}
		
		glAttachShader(programID, shaderId);
		
		return shaderId;
		
	}
	
	public void link() throws Exception {
		glLinkProgram(programID);
		
		if (glGetProgrami(programID, GL_LINK_STATUS) == 0) {
			throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programID, 1024));
		}
		
		if (debug) {
			glValidateProgram(programID);

			if (glGetProgrami(programID, GL_VALIDATE_STATUS) == 0) {
				System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programID, 1024));
			}
		}
		
		if (vertexShaderID != 0) {
			glDetachShader(programID, vertexShaderID);
		}
		
		if (fragmentShaderID != 0) {
			glDetachShader(programID, fragmentShaderID);
		}
	}
	
	public void bind() {
		glUseProgram(programID);
	}
	
	public void unbind() {
		glUseProgram(0);
	}
	
	public void cleanUp() {
		unbind();
		
		if (programID != 0) {
			glDeleteProgram(programID);
		}
		
		if (vertexShaderID != 0) {
			glDeleteShader(vertexShaderID);
		}
		
		if (fragmentShaderID != 0) {
			glDeleteShader(fragmentShaderID);
		}
	}


}
