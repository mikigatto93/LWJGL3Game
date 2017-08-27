package engine.game_engine;

import engine.window.Window;

public class GameEngine implements Runnable {
	
	public static final int TARGET_FPS = 75;

    public static final int TARGET_UPS = 30;

    @SuppressWarnings("unused")
	private Thread gameLoopThread;
    
    private final Window window;
    
    private final Timer timer;

    private final IGameLogic gameLogic;
    
    private final MouseInput mouseInput;
    

    public GameEngine(Window window, IGameLogic gameLogic) throws Exception {
    	this.window = window;
    	this.gameLogic = gameLogic;
    	timer = new Timer();
    	mouseInput = new MouseInput();
    	//gameLoopThread = null; 
    }

    public void init() throws Exception {
    	gameLoopThread = new Thread(this);
    	timer.init();
        gameLogic.init();
        mouseInput.init(window);
    }
    
    /*public void start() {
    	gameLoopThread.start();
    }*/

    protected void loop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        boolean running = true;
        while (running && !window.windowShouldClose()) {
            elapsedTime = timer.getElapsedTime();
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            } 

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = timer.getLastLoopTime() + loopSlot;
        while (timer.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
            	ie.printStackTrace();
            }
        }
    }

    protected void input() {
    	mouseInput.input(window);
    	gameLogic.input(window, mouseInput);
    }

    protected void update(float interval) {
        gameLogic.update(interval, mouseInput);
    }

    protected void render() {
        gameLogic.render(window);
        window.update();
    }
    
    protected void cleanUp() {
    	gameLogic.cleanUp();
    }
    
    
    @Override
    public void run() {
        try {
            //init();
            loop();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
        	cleanUp();
        	System.out.println("Renderer's resources cleaned correctly!");
        }

    }
}
