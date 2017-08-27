package engine.game_engine.graphics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class OBJLoader {
	
	public static Mesh loadModel(String fileName) {
		File file = new File("src/resources/models/" + fileName);
		Scanner reader = null;
		
		List<Vector3f> vertices = null;
		List<Vector3f> normals = null;
		List<Vector2f> textCoords = null;
		List<Face> faces = null;
		List<Integer> indices = null;
		
		try {
			
			reader = new Scanner(file);
			vertices = new ArrayList<Vector3f>();
			normals = new ArrayList<Vector3f>();
			textCoords = new ArrayList<Vector2f>();
			faces = new ArrayList<Face>();
			indices = new ArrayList<Integer>();
			
			while(reader.hasNextLine()) {
				
				String[] data = reader.nextLine().split(" ");
				
				for (String l : data) {
					System.out.println(l);
				}
				
				if (data[0].equals("v")) {
					Vector3f vertex = new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), 
							Float.parseFloat(data[3]));
					vertices.add(vertex);
				} else if (data[0].equals("vn")) {
					Vector3f normal = new Vector3f(Float.parseFloat(data[1]), Float.parseFloat(data[2]), 
							Float.parseFloat(data[3]));
					normals.add(normal);
				} else if (data[0].equals("vt")) {
					Vector2f textCoord = new Vector2f(Float.parseFloat(data[1]), Float.parseFloat(data[2]));
					textCoords.add(textCoord);
				} else if (data[0].equals("f")) {
					faces.add(new Face(data[1], data[2], data[3], (textCoords.size() != 0)));
				}
			}
			
			readList(vertices);
			readList2(textCoords);
			readList(normals);
			//System.out.println(normals.get(-1));
			reader.close();		
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return computeMesh(vertices, normals, textCoords, indices, faces);
	}
	
	private static Mesh computeMesh(List<Vector3f> verticesList, List<Vector3f> normalsList, List<Vector2f> textCoordsList, 
			List<Integer> indicesList, List<Face> facesList) {
		
		int vertSize = verticesList.size();
		float [] positions = new float[vertSize * 3];
		float [] normals = new float[vertSize * 3];
		float [] textCoords = new float[vertSize * 2];
		int [] indices = null;
		
		// Fill the positions array 
		int vertIndex = 0;
		for (Vector3f vertex : verticesList) {
			positions[vertIndex++] = vertex.x;
			positions[vertIndex++] = vertex.y;
			positions[vertIndex++] = vertex.z;
		}
		
		for (Face face : facesList) {
			if (face.isTextured()) {
				computeFace(face.v1, normalsList, textCoordsList, indicesList, normals, textCoords, indices);
				computeFace(face.v2, normalsList, textCoordsList, indicesList, normals, textCoords, indices);
				computeFace(face.v3, normalsList, textCoordsList, indicesList, normals, textCoords, indices);
			} else {
				computeFace(face.v1, normalsList, indicesList, normals, indices);
				computeFace(face.v2, normalsList, indicesList, normals, indices);
				computeFace(face.v3, normalsList, indicesList, normals, indices);
			}	
		}
		
		// Fill the indices array
		int len = indicesList.size();
		indices = new int[len];
		for (int i = 0; i < len; i++) {
			indices[i] = indicesList.get(i);
		}
		
		readArray(positions);
		readArray(textCoords);
		readArray(normals);
		readArray(indices);
		
		return new Mesh(positions, textCoords, normals, indices);
		
	}
	
	private static void readArray(float[] array) {
		
		for (int i = 0; i < array.length; i++) {
			System.out.println("i: " + array[i]);
		} 
		System.out.println("----------");
	}
	
	private static void readArray(int[] array) {
		
		for (int i = 0; i < array.length; i++) {
			System.out.println("i: " + array[i]);
		}
		System.out.println("----------");
	}
	
	private static void readList(List<Vector3f> list) {
		for (Vector3f v : list) {
			System.out.println(v.x);
			System.out.println(v.y);
			System.out.println(v.z);
		}
		System.out.println(list.size());
	}
	
	private static void readList2(List<Vector2f> list) {
		for (Vector2f v : list) {
			System.out.println(v.x);
			System.out.println(v.y);
			
		}
		System.out.println(list.size());
	}
	
	private static void computeFace(int[] faceVertex, List<Vector3f> normalsList, List<Vector2f> textCoordsList, 
			List<Integer> indicesList, float[] normals, float[] textCoords, int[] indices) {
		
		int index = faceVertex[0] - 1;
		int texIndex = faceVertex[1] - 1;
		int normIndex = faceVertex[2] - 1;
		
		Vector3f normal = normalsList.get(normIndex);
		Vector2f textCoord = textCoordsList.get(texIndex);
		
		indicesList.add(index);
		
		normals[index * 3] = normal.x;
		normals[index * 3 + 1] = normal.y;
		normals[index * 3 + 2] = normal.z;
		
		textCoords[index * 2] = textCoord.x;
		textCoords[index * 2 + 1] = 1 - textCoord.y;
	}
	
	private static void computeFace(int[] faceVertex, List<Vector3f> normalsList, List<Integer> indicesList, 
			float[] normals, int[] indices) {
		
		int index = faceVertex[0] - 1;
		int normIndex = faceVertex[1] - 1;
		
		Vector3f normal = normalsList.get(normIndex);
		
		indicesList.add(index);
		
		normals[index * 3] = normal.x;
		normals[index * 3 + 1] = normal.y;
		normals[index * 3 + 2] = normal.z;
	}
	
	// Face class
	protected static class Face {
		public int[] v1;
		public int[] v2;
		public int[] v3;
		private boolean hasTexture;

		public Face(String v1, String v2, String v3, boolean hasTexture) {
			this.hasTexture = hasTexture;
			this.v1 = computeData(v1);
			this.v2 = computeData(v2);
			this.v3 = computeData(v3);			
		}

		private int[] computeData(String vert) {
			String[] vertData = vert.split("/");
			int[] array = null;
			if (hasTexture) {  
				array = new int[3];  // [v, vt, vn]
				for (int i = 0; i < 3; i++) {
					array[i] = Integer.parseInt(vertData[i]);
				}

			} else {
				array = new int[2];  // [v, vn]
				
				array[0] = Integer.parseInt(vertData[0]);
				array[1] = Integer.parseInt(vertData[2]);

			}
			return array; 		
		}

		public boolean isTextured() {
			return hasTexture;
		}
	}
}

