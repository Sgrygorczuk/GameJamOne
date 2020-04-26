package com.packt.gamejamone;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class StartScreen extends ScreenAdapter {

    /*
    Dimensions -- Units the screen has
     */
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;


    /*
    Image processing -- Objects that modify the view and textures
    */
    private ShapeRenderer shapeRendererEnemy;           //Creates wire frame for enemy objects
    private ShapeRenderer shapeRendererUser;            //Creates wire frame for player object
    private ShapeRenderer shapeRendererBackground;      //Creates wire frame for background objects
    private ShapeRenderer shapeRendererCollectible;     //Creates wire frame for collectible objects

    private Viewport viewport;			 //The screen where we display things
    private Camera camera;				 //The camera viewing the viewport
    private SpriteBatch batch;			 //Batch that holds all of the textures

    //Objects
    private FighterList fighterList;
    private DistanceBar playerBar;
    private DistanceBar opponentBar;
    private StatusBar playerHealth;
    private StatusBar opponentHealth;
    private StatusBar playerStamina;
    private StatusBar opponentStamina;
    private AttackBox playerAttackBox;
    private AttackBox opponentAttackBox;

    //Timing variable for how long it's idling
    private static float MOVE_TIME = 0.2f;
    private float moveTimer = MOVE_TIME;

    private Texture healthTexture;
    private Texture healthFrameTexture;
    private Texture staminaTexture;
    private Texture staminaFrameTexture;
    private Texture pointerTexture;
    private Texture pointerBarTexture;
    private Texture attackFrameTexture;
    private Texture attackOneTexture;
    private Texture attackTwoTexture;
    private Texture attackThreeTexture;
    private Texture highlightTexture;
    private Texture playerTexture;
    private Texture opponentTexture;
    private Texture backgroundTexture;

    /*
    Bitmap and GlyphLayout
    */
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    //Flags
    private boolean debugFlag = true;          //Tells screen to draw debug wireframe
    private boolean textureFlag = true;         //Tells screen to draw textures
    private int playerAttackSelection = 0;
    private int opponentAttackSelection = 0;

    private final Game game;
    StartScreen(Game game) { this.game = game; }

    /*
    Input: The width and height of the screen
    Output: Void
    Purpose: Updates the dimensions of the screen
    */
    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Initializes all the variables that are going to be displayed
    */
    @Override
    public void show() {
        showCamera();           //Sets up camera through which objects are draw through
        showTexture();          //Connects textures to the images
        showObjects();          //Creates object and passes them the dimensions and textures
        showRender();           //Sets up renders that will draw the debug of objects

        //Sets up the texture with the images
        batch = new SpriteBatch();

        //BitmapFont and GlyphLayout
        bitmapFont = new BitmapFont();
        glyphLayout = new GlyphLayout();
    }

    /*
   Input: Void
   Output: Void
   Purpose: Initializes the objects that are going to be displayed.
   Mostly giving objects dimension, position and connecting them to textures.
   */
    private void showObjects(){
        fighterList = new FighterList(playerTexture, opponentTexture);

        playerBar = new DistanceBar(pointerBarTexture, pointerTexture);
        playerBar.setInitialPosition(10, 100);

        playerHealth = new StatusBar(40,5, true, healthFrameTexture, healthTexture);
        playerHealth.setPosition(10, 260);
        playerHealth.setHealth(fighterList.getPlayerHealthFull());

        playerStamina = new StatusBar(15, 3, false, staminaFrameTexture, staminaTexture);
        playerStamina.setPosition(10, 10);
        playerStamina.setPositionPlayerStamina();
        playerStamina.setHealth(100);

        playerAttackBox = new AttackBox(attackFrameTexture, attackThreeTexture, attackTwoTexture, attackOneTexture, highlightTexture);
        playerAttackBox.setPosition(10, 35);

        opponentBar = new DistanceBar(pointerBarTexture, pointerTexture);
        opponentBar.setInitialPosition(270, 100);

        opponentHealth = new StatusBar(40, 5, true, healthFrameTexture, healthTexture);
        opponentHealth.setPosition(270, 260);
        opponentHealth.setHealth(fighterList.getOpponentHealthFull());

        opponentStamina = new StatusBar(15, 3, false, staminaFrameTexture, staminaTexture);
        opponentStamina.setPosition(270, 10);
        opponentStamina.setHealth(100);

        opponentAttackBox = new AttackBox(attackFrameTexture, attackOneTexture, attackTwoTexture, attackThreeTexture, highlightTexture);
        opponentAttackBox.setPosition(270, 35);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the camera through which all the objects are view through
    */
    private void showCamera(){
        camera = new OrthographicCamera();									//Sets a 2D view
        camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);	//Places the camera in the center of the view port
        camera.update();													//Updates the camera
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);		//
    }


    /*
    Input: Void
    Output: Void
    Purpose: Connects the images to the Texture objects
    */
    private void showTexture(){
        healthTexture = new Texture(Gdx.files.internal("Health.png"));
        healthFrameTexture = new Texture(Gdx.files.internal("HealthFrame.png"));

        staminaTexture = new Texture(Gdx.files.internal("STA.png"));
        staminaFrameTexture = new Texture(Gdx.files.internal("STAFrame.png"));

        pointerBarTexture = new Texture(Gdx.files.internal("PointerBar.png"));
        pointerTexture = new Texture(Gdx.files.internal("Pointer.png"));

        attackFrameTexture = new Texture(Gdx.files.internal("AttackFrame.png"));
        attackOneTexture = new Texture(Gdx.files.internal("AttackFrameClose.png"));
        attackTwoTexture = new Texture(Gdx.files.internal("AttackFrameMedium.png"));
        attackThreeTexture = new Texture(Gdx.files.internal("AttackFrameLong.png"));
        highlightTexture = new Texture(Gdx.files.internal("HighLight.png"));

        playerTexture = new Texture(Gdx.files.internal("Player.png"));
        opponentTexture = new Texture(Gdx.files.internal("Opponent.png"));

        backgroundTexture = new Texture(Gdx.files.internal("Background.png"));
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the different renders to draw objects in wireframe
    */
    private void showRender(){
        //Enemy
        shapeRendererEnemy = new ShapeRenderer();
        shapeRendererEnemy.setColor(Color.RED);

        //User
        shapeRendererUser = new ShapeRenderer();
        shapeRendererUser.setColor(Color.GREEN);

        //Background
        shapeRendererBackground = new ShapeRenderer();
        shapeRendererBackground.setColor(Color.WHITE);

        //Intractable
        shapeRendererCollectible = new ShapeRenderer();
        shapeRendererCollectible.setColor(Color.BLUE);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws all of the variables on the screen
    */
    @Override
    public void render(float delta) {
        clearScreen();	                //Wipes screen
        setTextureMode();               //Checks if the user changed the status of texturesOnFlag
        if(textureFlag) {draw();}	    //Draws the textures

        setDebugMode();                 //Checks if user changed the status of the debugModeFlag
        if(debugFlag) {                 //If debugMode is on ShapeRender will drawing lines
            renderEnemy();              //Draws Enemy Wire Frame
            renderUser();               //Draws User Wire Frame
            renderBackground();         //Draws Background Wire Frame
            renderCollectible();        //Draws Collectible Wire Frame
        }
        update(delta);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the enemy/obstacle wireframe
    */
    private void renderEnemy(){
        shapeRendererEnemy.setProjectionMatrix(camera.projection);      		                 //Screen set up camera
        shapeRendererEnemy.setTransformMatrix(camera.view);            			                 //Screen set up camera
        shapeRendererEnemy.begin(ShapeRenderer.ShapeType.Line);         		                 //Sets up to draw lines
        fighterList.drawDebug(shapeRendererEnemy, false);
        shapeRendererEnemy.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws user wireframe
    */
    private void renderUser(){
        shapeRendererUser.setProjectionMatrix(camera.projection);    //Screen set up camera
        shapeRendererUser.setTransformMatrix(camera.view);           //Screen set up camera
        shapeRendererUser.begin(ShapeRenderer.ShapeType.Line);       //Sets up to draw lines
        fighterList.drawDebug(shapeRendererUser, true);
        shapeRendererUser.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the background object and UI wireframes
    */
    private void renderBackground(){
        shapeRendererBackground.setProjectionMatrix(camera.projection);                 //Screen set up camera
        shapeRendererBackground.setTransformMatrix(camera.view);                        //Screen set up camera
        shapeRendererBackground.begin(ShapeRenderer.ShapeType.Line);                    //Starts to draw
        playerBar.drawDebug(shapeRendererBackground);
        playerHealth.drawDebug(shapeRendererBackground);
        playerStamina.drawDebug(shapeRendererBackground);
        playerAttackBox.drawDebug(shapeRendererBackground);
        opponentBar.drawDebug(shapeRendererBackground);
        opponentHealth.drawDebug(shapeRendererBackground);
        opponentStamina.drawDebug(shapeRendererBackground);
        opponentAttackBox.drawDebug(shapeRendererBackground);
        shapeRendererBackground.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws wireframe of the collectibles -- needs to be redone along with collectible objects
    */
    private void renderCollectible(){
        shapeRendererCollectible.setProjectionMatrix(camera.projection);
        shapeRendererCollectible.setTransformMatrix(camera.view);
        shapeRendererCollectible.begin(ShapeRenderer.ShapeType.Line);
        shapeRendererCollectible.end();
    }


    /*
    Input: Void
    Output: Void
    Purpose: Checks for user input if the user clicks turns the debugMode flag on and off
    */
    private void setDebugMode() { if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) { debugFlag = !debugFlag;} }

    /*
    Input: Void
    Output: Void
    Purpose: Checks for user input if the user clicks turns the debugMode flag on and off
    */
    private void setTextureMode() { if(Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {textureFlag = !textureFlag;} }

    private void update(float delta){
        updateUserAction();
        fighterList.updateOpponent();
        playerHealth.setPlayerHealth(fighterList.getPlayerHealthCurrent());
        opponentHealth.setOpponentHealth(fighterList.getOpponentHealthCurrent());
        playerBar.updateSliderPositionPlayer(fighterList.getDistanceBetweenFighters());
        opponentBar.updateSliderPositionOpponent(fighterList.getDistanceBetweenFighters());
        playerAttackSelection = playerAttackBox.updateHighlight(playerBar.getPositionOfSlider());
        opponentAttackSelection = opponentAttackBox.updateHighlight(opponentBar.getPositionOfSlider());
        moveTimer -= delta;
        if (moveTimer <= 0) {
            moveTimer = MOVE_TIME;
            fighterList.updateStamina();
            playerStamina.setStaminaPlayer(fighterList.getPlayerStamina());
            opponentStamina.setStaminaOpponent(fighterList.getOpponentStamina());
        }
    }

    private void updateUserAction(){
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {fighterList.updatePlayer(0);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {fighterList.updatePlayer(1);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {attack();}
    }

    private void attack(){
        float staminaCost = fighterList.getPlayerCurrentAttackCost(playerAttackSelection);
        if(fighterList.getPlayerStamina() > staminaCost){
            fighterList.updateStaminaPay(staminaCost);
            int diceRoll = MathUtils.random((int)fighterList.getPlayerAccuracy(playerAttackSelection),100);
            if(diceRoll > 50){
                System.out.println("Hit");
                fighterList.damageOpponent((int) fighterList.getPlayerStrength(playerAttackSelection));
            }
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Restart the variables to initial position and removes all flowers from array.
    */
    private void restart(){

    }

    /*
    Input: Void
    Output: Void
    Purpose: Central function that draws the textures
    */
    private void draw() {
        //Viewport/Camera projection
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture
        batch.begin();
        batch.draw(backgroundTexture, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        playerHealth.draw(batch);
        fighterList.draw(batch);
        playerStamina.draw(batch);
        playerAttackBox.draw(batch);
        playerBar.draw(batch);
        opponentHealth.draw(batch);
        opponentStamina.draw(batch);
        opponentAttackBox.draw(batch);
        opponentBar.draw(batch);
        batch.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates all the variables on the screen
    */
    private void clearScreen() {
        Gdx.gl.glClearColor(Color.BLACK.r, Color.BLACK.g, Color.BLACK.b, Color.BLACK.a); //Sets color to black
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);										 //Sends it to the buffer
    }

    /*
Input: Void
Output: Void
Purpose: Destroys everything once we move onto the new screen
*/
    @Override
    public void dispose() {
    }


}
