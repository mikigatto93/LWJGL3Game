package engine.game_engine;

import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Utils {
	
	public static String loadShader(String fileName) {
		StringBuilder fileData = new StringBuilder();
		
		File file = new File("src/resources/shaders/" + fileName);
		
		try {
			
			Scanner reader = new Scanner(file);
			
			while(reader.hasNextLine()) {
				fileData.append(reader.nextLine()).append("\n");
			}
			
			reader.close();
			
		} catch (FileNotFoundException ex) {
			ex.printStackTrace();
		}
		
		return fileData.toString();
	}
	
	/*public static void main(String[] args) {
		String file = Utils.loadFile("src/resources/vert.vs");
		
		System.out.println(file);
	}*/
	
}
