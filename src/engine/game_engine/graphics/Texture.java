package engine.game_engine.graphics;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

public class Texture {
	private int id;
	
	public Texture(String fileName) throws Exception {
		this(loadTexture(fileName));
	}
	
	public Texture(int id) {
		this.id = id;
	}
	
	private static int loadTexture(String fileName) throws Exception{
		// Load Texture file
		InputStream textureFile = new FileInputStream("src/resources/textures/" + fileName);
        PNGDecoder texture = new PNGDecoder(textureFile);
        
        // Load texture contents into a byte buffer
        ByteBuffer buffer = ByteBuffer.allocateDirect(4 * texture.getHeight() * texture.getWidth());
        texture.decode(buffer, 4 * texture.getWidth(), Format.RGBA);
        buffer.flip();
	
        // Create a new OpenGL texture 
        int textureId = glGenTextures();
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, textureId);
        
        // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
	
        // Upload the texture data
        glTexImage2D(
			 GL_TEXTURE_2D, // Specifies the target texture (its type).
			 0,  // Specifies the level-of-detail number.
			 GL_RGBA, // Specifies the number of color components in the texture
			 texture.getWidth(), 
			 texture.getHeight(), 
			 0,  // Border (MUST BE 0) 
			 GL_RGBA, // Specifies the format of the pixel data
			 GL_UNSIGNED_BYTE, // Specifies the data type of the pixel data
			 buffer  // The buffer that stores our data
         ); 
        
        // Generate mipmap
        glGenerateMipmap(GL_TEXTURE_2D);
        
        return textureId;
        
	}
	
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, id);
	}
	
	
	public int getId() {
		return id;
	}
	
	public void cleanUp() {
		glDeleteTextures(id);
	}
	
}
