package engine.game_engine;

import engine.window.Window;

public interface IGameLogic {
	
	void init() throws Exception;

	void input(Window window, MouseInput mouseInput);

	void update(float interval, MouseInput mouseInput);
 
	void render(Window window);
	
	void cleanUp();

}