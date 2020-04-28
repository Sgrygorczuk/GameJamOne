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
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class StartScreen extends ScreenAdapter {

    /*
    Dimensions -- Units the screen has
     */
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;

    private Stage menu;     //Button that leads to the pause menu
    private Stage tutorial;

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
    private DistanceBar playerBar;
    private DistanceBar opponentBar;
    private StatusBar playerHealth;
    private StatusBar opponentHealth;
    private StatusBar playerStamina;
    private StatusBar opponentStamina;
    private AttackBox playerAttackBox;
    private AttackBox opponentAttackBox;
    private Fighter player;
    private Fighter opponent;

    //Timing variable for how long it's idling
    private static float MOVE_TIME = 0.5f;
    private float moveTimer = MOVE_TIME;

    private static float FLAG_TIME = 0.5f;
    private float flagTimer = FLAG_TIME;

    private static float WAIT_FLAG_TIME = 0.3f;
    private float waitFlagTimer = WAIT_FLAG_TIME;


    private Texture healthTexture;
    private Texture healthFrameTexture;
    private Texture tutorialTexture;
    private Texture staminaTexture;
    private Texture staminaFrameTexture;
    private Texture pointerTexture;
    private Texture pointerBarTexture;
    private Texture attackFrameTexture;
    private Texture attackOneTexture;
    private Texture attackTwoTexture;
    private Texture attackThreeTexture;
    private Texture attackEvilEyeTexture;
    private Texture highlightTexture;
    private Texture playerTexture;
    private Texture opponentTexture;
    private Texture backgroundTexture;
    private Texture dodgeTexture;
    private Texture missTexture;
    private Texture hitTexture;
    private Texture fireHitTexture;
    private Texture bulletHitTexture;
    private Texture fistHitTexture;
    private Texture scaryEyeTexture;
    private Texture spriteSheetTexture;
    private Texture catWinTexture;
    private Texture dummyWinTexture;

    /*
    Bitmap and GlyphLayout
    */
    private BitmapFont bitmapFont;
    private GlyphLayout glyphLayout;

    //Flags
    private boolean debugFlag = false;          //Tells screen to draw debug wireframe
    private boolean textureFlag = true;         //Tells screen to draw textures
    private boolean announceFlag = false;
    private boolean waitFlag = false;
    private boolean hitFlag = false;
    private boolean dodgeFlag = false;
    private boolean missFlag = false;
    private boolean menuFlag = true;
    private boolean endFlag = false;
    private boolean catWinFlag = false;
    private boolean dummyWinFlag = false;
    private boolean tutorialFlag = false;
    private boolean playerDrawAttackFlag = false;
    private boolean opponentDrawAttackFlag = false;
    private int playerAttackSelection = 0;
    private int lockedInPlayerAttackSelection = playerAttackSelection;
    private int opponentAttackSelection = 0;
    private int lockedInOpponentAttackSelection = opponentAttackSelection;

    private static float STR_TIME = .2f;
    private float strTimer = STR_TIME;

    private static float RESTART_TIME = 1f;
    private float restartTimer = RESTART_TIME;

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
        showMenuButton();
        tutorialButton();
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
        player = new Fighter(false, playerTexture);
        player.setStats(5,5,30,5,120,30);
        player.setPosition(120 - player.getWidth(),120);
        player.setTexture(spriteSheetTexture);

        Attack attack = new Attack(fireHitTexture);
        attack.setAttributes("Short Attack", 5, 10, 10);
        attack.setPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight()/2);
        attack.setSize(60,60);
        player.addAttack(attack);

        Attack attackTwo = new Attack(bulletHitTexture);
        attackTwo.setAttributes("Medium Attack", 15, 1, 15);
        attackTwo.setPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight()/2);
        attackTwo.setSize(10,10);
        player.addAttack(attackTwo);

        Attack attackThree = new Attack(fistHitTexture);
        attackThree.setAttributes("Long Attack", 20, 15, 20);
        attackThree.setPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight()/2);
        attackThree.setSize(30,30);
        player.addAttack(attackThree);

        opponent = new Fighter(true, opponentTexture);
        opponent.setPosition(360,120);
        opponent.setStats(10, 2, 60, 2, 200, 20);
        opponent.setAI(2);

        Attack attack1 = new Attack(scaryEyeTexture);
        attack1.setAttributes("Evil eye", 30, 50, 50);
        attack1.setPosition(opponent.getX(), opponent.getY()+3*opponent.getHeight()/4);
        attack1.setSize(60,60);
        opponent.addAttack(attack1);
        opponent.addAttack(attack1);
        opponent.addAttack(attack1);

        playerBar = new DistanceBar(pointerBarTexture, pointerTexture);
        playerBar.setInitialPosition(10, 100);

        playerHealth = new StatusBar(40,5, true, healthFrameTexture, healthTexture);
        playerHealth.setPosition(10, 260);
        playerHealth.setHealth(player.getHealthFull());
        playerHealth.setTextPosition(10, 255);


        playerStamina = new StatusBar(15, 3, false, staminaFrameTexture, staminaTexture);
        playerStamina.setPosition(10, 10);
        playerStamina.setPositionPlayerStamina();
        playerStamina.setHealth(100);
        playerStamina.setTextPosition(215, 25);

        playerAttackBox = new AttackBox(attackFrameTexture, attackThreeTexture, attackTwoTexture, attackOneTexture, highlightTexture);
        playerAttackBox.setPosition(10, 35);
        playerAttackBox.setTextPosition(215,105,215,65);

        opponentBar = new DistanceBar(pointerBarTexture, pointerTexture);
        opponentBar.setInitialPosition(270, 100);

        opponentHealth = new StatusBar(40, 5, true, healthFrameTexture, healthTexture);
        opponentHealth.setPosition(270, 260);
        opponentHealth.setHealth(opponent.getHealthFull());
        opponentHealth.setTextPosition(440, 255);

        opponentStamina = new StatusBar(15, 3, false, staminaFrameTexture, staminaTexture);
        opponentStamina.setPosition(270, 10);
        opponentStamina.setHealth(100);
        opponentStamina.setTextPosition(255,25);

        opponentAttackBox = new AttackBox(attackFrameTexture, attackEvilEyeTexture, attackEvilEyeTexture, attackEvilEyeTexture, highlightTexture);
        opponentAttackBox.setPosition(270, 35);
        opponentAttackBox.setTextPosition(255,105,255,65);

    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the button that will pause the game and bring up the menu
    */
    private void showMenuButton(){
        //Sets up stage to be screen size
        menu = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));
        Gdx.input.setInputProcessor(menu);    //Give it the control

        //Sets up textures used by the button
        Texture menuUpTexture = new Texture(Gdx.files.internal("ButtonUnpressed.png"));
        Texture menuDownTexture = new Texture(Gdx.files.internal("ButtonPressed.png"));

        //Creates button and position
        ImageButton menuButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(menuUpTexture)), new TextureRegionDrawable(menuDownTexture));
        menuButton.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT/2, Align.center);
        menu.addActor(menuButton);

            menuButton.addListener(new ActorGestureListener() {@Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                menuFlag = false;
                tutorialFlag = true;
                opponent.setUserOrAI(true);
                Gdx.input.setInputProcessor(tutorial);
            }
            });

        //Creates button and position
        ImageButton twoPlayer = new ImageButton(new TextureRegionDrawable(new TextureRegion(menuUpTexture)), new TextureRegionDrawable(menuDownTexture));
        twoPlayer.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT/2 - 80, Align.center);
        menu.addActor(twoPlayer);

        twoPlayer.addListener(new ActorGestureListener() {@Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            super.tap(event, x, y, count, button);
            menuFlag = false;
            tutorialFlag = true;
            opponent.setUserOrAI(false);
            Gdx.input.setInputProcessor(tutorial);
        }
        });
    }

    /*
Input: Void
Output: Void
Purpose: Sets up the button that will pause the game and bring up the menu
*/
    private void tutorialButton(){
        //Sets up stage to be screen size
        tutorial = new Stage(new FitViewport(WORLD_WIDTH,WORLD_HEIGHT));

        //Sets up textures used by the button
        Texture menuUpTexture = new Texture(Gdx.files.internal("ButtonUnpressed.png"));
        Texture menuDownTexture = new Texture(Gdx.files.internal("ButtonPressed.png"));

        //Creates button and position
        ImageButton tutorialButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(menuUpTexture)), new TextureRegionDrawable(menuDownTexture));
        tutorialButton.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT/4, Align.center);
        tutorial.addActor(tutorialButton);

        tutorialButton.addListener(new ActorGestureListener() {@Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            super.tap(event, x, y, count, button);
            tutorialFlag = false;
            menuFlag = false;
        }
        });

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
        attackEvilEyeTexture = new Texture(Gdx.files.internal("EvilEyeFrame.png"));
        highlightTexture = new Texture(Gdx.files.internal("HighLight.png"));

        playerTexture = new Texture(Gdx.files.internal("Player.png"));
        spriteSheetTexture = new Texture(Gdx.files.internal("PlayerSpriteSheet.png"));
        opponentTexture = new Texture(Gdx.files.internal("Opponent.png"));

        hitTexture = new Texture(Gdx.files.internal("Hit.png"));
        missTexture = new Texture(Gdx.files.internal("Miss.png"));
        dodgeTexture = new Texture(Gdx.files.internal("Dodge.png"));

        backgroundTexture = new Texture(Gdx.files.internal("Background.png"));

        fireHitTexture = new Texture(Gdx.files.internal("FireEffect.png"));
        fistHitTexture = new Texture(Gdx.files.internal("HandEffect.png"));
        bulletHitTexture = new Texture(Gdx.files.internal("BulletEffect.png"));
        scaryEyeTexture = new Texture(Gdx.files.internal("EyeEffect.png"));

        tutorialTexture = new Texture(Gdx.files.internal("Tutorial.png"));

        catWinTexture = new Texture(Gdx.files.internal("PawsWin.png"));
        dummyWinTexture = new Texture(Gdx.files.internal("DummyWin.png"));
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
        //setTextureMode();               //Checks if the user changed the status of texturesOnFlag
        if(textureFlag && !menuFlag && !tutorialFlag) {
            draw();
            if(!endFlag){update(delta);}
            else{
                restartTimer -= delta;
                if (restartTimer <= 0) {
                    restartTimer = RESTART_TIME;
                    restart();
                }
            }
        }	    //Draws the textures
        else if(tutorialFlag){
            batch.setProjectionMatrix(camera.projection);
            batch.setTransformMatrix(camera.view);
            //Batch setting up texture
            batch.begin();
            batch.draw(tutorialTexture, 0 ,0, WORLD_WIDTH, WORLD_HEIGHT);
            batch.end();

            tutorial.draw();

            batch.setProjectionMatrix(camera.projection);
            batch.setTransformMatrix(camera.view);
            //Batch setting up texture
            batch.begin();
            String One = "Play";
            glyphLayout.setText(bitmapFont, One);
            bitmapFont.draw(batch, One, WORLD_WIDTH/2 - glyphLayout.width/2, WORLD_HEIGHT/4 + glyphLayout.height/2);
            batch.end();
        }
        else{
            menu.draw();
            batch.setProjectionMatrix(camera.projection);
            batch.setTransformMatrix(camera.view);
            //Batch setting up texture
            batch.begin();
            String One = "Practice";
            glyphLayout.setText(bitmapFont, One);
            bitmapFont.draw(batch, One, WORLD_WIDTH/2 - glyphLayout.width/2, WORLD_HEIGHT/2 + glyphLayout.height/2);
            String Two = "2 Player";
            glyphLayout.setText(bitmapFont, Two);
            bitmapFont.draw(batch, Two, WORLD_WIDTH/2 - glyphLayout.width/2, WORLD_HEIGHT/2 - 80 + glyphLayout.height/2);
            batch.end();
        }

        //setDebugMode();                 //Checks if user changed the status of the debugModeFlag
        if(debugFlag) {                 //If debugMode is on ShapeRender will drawing lines
            renderEnemy();              //Draws Enemy Wire Frame
            renderUser();               //Draws User Wire Frame
            renderBackground();         //Draws Background Wire Frame
            renderCollectible();        //Draws Collectible Wire Frame
        }
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
        opponent.drawDebug(shapeRendererEnemy);
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
        player.drawDebug(shapeRendererUser);
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
        updateUserAction(delta);
        updatePlayerTwoAction(delta);
        player.update(-1,0, opponent.getX(), delta);
        opponent.update(-1,player.getX()+player.getWidth(), 480, delta);
        playerHealth.setPlayerHealth(player.getHealthCurrent());
        if(hitFlag || missFlag || dodgeFlag){updateFlags(delta);}
        if(announceFlag){
            waitFlagTimer -= delta;
            if (waitFlagTimer <= 0) {
                waitFlagTimer = WAIT_FLAG_TIME;
                announceFlag = false;
                waitFlag = true;
            }
        }
        if(true && !missFlag && !hitFlag && !dodgeFlag){attackPlayerTwo();}
        strTimer -= delta;
        if (strTimer <= 0) {
            strTimer = STR_TIME;
            playerAttackBox.setText((int) player.getStrength(playerAttackSelection), player.getCurrentAttackCost(playerAttackSelection));
            opponentAttackBox.setText((int) opponent.getStrength(opponentAttackSelection), opponent.getCurrentAttackCost(opponentAttackSelection));
        }
        playerStamina.setStaminaText((int) player.getStamina());
        opponentStamina.setStaminaText((int) opponent.getStamina());
        opponentHealth.setOpponentHealth(opponent.getHealthCurrent());
        opponentHealth.setHealthText(opponent.getHealthCurrent(), opponent.getHealthFull());
        playerHealth.setHealthText(player.getHealthCurrent(), player.getHealthFull());
        playerBar.updateSliderPositionPlayer(getDistanceBetweenFighters());
        opponentBar.updateSliderPositionOpponent(getDistanceBetweenFighters());
        playerAttackSelection = playerAttackBox.updateHighlight(playerBar.getPositionOfSlider());
        opponentAttackSelection = opponentAttackBox.updateHighlight(opponentBar.getPositionOfSlider());
        if(!playerDrawAttackFlag){
            player.restartAttackPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight()/2);
        }
        if(!opponentDrawAttackFlag){
            opponent.restartAttackPosition(opponent.getX(), opponent.getY()+2*opponent.getHeight()/3);
        }
        moveTimer -= delta;
        if (moveTimer <= 0) {
            moveTimer = MOVE_TIME;
            player.updateStamina();
            opponent.updateStamina();
            playerStamina.setStaminaPlayer(player.getStamina());
            opponentStamina.setStaminaOpponent(opponent.getStamina());
        }
        checkIfDead();
    }

    void checkIfDead(){
        if(player.getHealthCurrent() == 0) {
            dummyWinFlag = true;
            hitFlag = false;
            endFlag = true;
        }
        else if(opponent.getHealthCurrent() == 0){
            catWinFlag = true;
            hitFlag = false;
            endFlag = true;
        }
    }

    float getDistanceBetweenFighters() { return (opponent.getX() - player.getX() - player.getWidth()); }

    private void updateUserAction(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {player.update(0, 0, opponent.getX(), delta);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {player.update(1, 0, opponent.getX(), delta);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && !missFlag && !hitFlag && !dodgeFlag) {attackPlayerOne();}
    }

    private void updatePlayerTwoAction(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {opponent.update(0, player.getX()+player.getWidth(), 480, delta);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {opponent.update(1, player.getX()+player.getWidth(), 480, delta);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && !missFlag && !hitFlag && !dodgeFlag) {attackPlayerTwo();}
    }

    private void attackPlayerOne(){
        float staminaCost = player.getCurrentAttackCost(playerAttackSelection);
        announceFlag = true;
        lockedInPlayerAttackSelection = playerAttackSelection;
        if(player.getStamina() > staminaCost){
            playerDrawAttackFlag = player.updateStamina(staminaCost, playerAttackSelection);
            int diceRoll = MathUtils.random((int)player.getAccuracy(playerAttackSelection),100);
            if(diceRoll > 50){
                System.out.println("Hit");
                boolean flag = opponent.updateHealth((int) player.getStrength(playerAttackSelection));
                if(flag){hitFlag = true;}
                else{dodgeFlag = true;}
            }
            else { missFlag = true;}
        }
    }

    private void attackPlayerTwo(){
        float staminaCost = opponent.getCurrentAttackCost(opponentAttackSelection);
        announceFlag = true;
        lockedInOpponentAttackSelection = opponentAttackSelection;
        if(opponent.getStamina() > staminaCost){
            opponentDrawAttackFlag = opponent.updateStamina(staminaCost, opponentAttackSelection);
            int diceRoll = MathUtils.random((int)player.getAccuracy(opponentAttackSelection),100);
            if(diceRoll > 50){
                System.out.println("Hit");
                boolean flag = player.updateHealth((int) opponent.getStrength(opponentAttackSelection));
                if(flag){hitFlag = true;}
                else{dodgeFlag = true;}
            }
            else { missFlag = true;}
        }
    }

    void updateFlags(float delta){
        flagTimer -= delta;
        if (flagTimer <= 0) {
            flagTimer = FLAG_TIME;
            hitFlag = false;
            missFlag = false;
            dodgeFlag = false;
            playerDrawAttackFlag = false;
            opponentDrawAttackFlag = false;
            waitFlag = false;
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Restart the variables to initial position and removes all flowers from array.
    */
    private void restart(){
        menuFlag = true;
        Gdx.input.setInputProcessor(menu);
        player.setPosition(120 - player.getWidth(),120);
        player.setHealth(120);
        player.setStamina(0);

        playerHealth.setPosition(10, 260);
        playerHealth.setHealth(player.getHealthFull());
        playerHealth.setTextPosition(10, 255);

        playerDrawAttackFlag = false;
        opponentDrawAttackFlag = false;
        endFlag = false;
        catWinFlag = false;
        dummyWinFlag = false;
        opponent.setPosition(360,120);
        opponent.setHealth(200);
        opponent.setStamina(0);
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
        player.draw(batch);
        opponent.draw(batch);
        if(waitFlag) {
            if (hitFlag) { batch.draw(hitTexture, WORLD_WIDTH / 2 - hitTexture.getWidth() / 2, WORLD_HEIGHT / 2 - hitTexture.getHeight() / 2); }
            if (missFlag) { batch.draw(missTexture, WORLD_WIDTH / 2 - missTexture.getWidth() / 2, WORLD_HEIGHT / 2 - missTexture.getHeight() / 2); }
            if (dodgeFlag) { batch.draw(dodgeTexture, WORLD_WIDTH / 2 - dodgeTexture.getWidth() / 2, WORLD_HEIGHT / 2 - dodgeTexture.getHeight() / 2); }
        }
        if(playerDrawAttackFlag){player.drawAttack(lockedInPlayerAttackSelection, true, batch);}
        if(opponentDrawAttackFlag){opponent.drawAttack(lockedInOpponentAttackSelection, false, batch);}
        playerStamina.draw(batch);
        playerAttackBox.draw(batch);
        playerBar.draw(batch);
        opponentHealth.draw(batch);
        playerAttackBox.drawText(glyphLayout, bitmapFont, batch);
        opponentAttackBox.drawText(glyphLayout, bitmapFont, batch);
        opponentStamina.drawText(glyphLayout, bitmapFont, batch);
        playerStamina.drawText(glyphLayout, bitmapFont, batch);
        opponentHealth.drawText(glyphLayout, bitmapFont, batch);
        playerHealth.drawText(glyphLayout,bitmapFont,batch);
        opponentStamina.draw(batch);
        opponentAttackBox.draw(batch);
        String DMGAsString = "DMG";
        glyphLayout.setText(bitmapFont, DMGAsString);
        bitmapFont.draw(batch, DMGAsString, WORLD_WIDTH/2 - glyphLayout.width/2, 105);
        String ACCAsString = "COST";
        glyphLayout.setText(bitmapFont, ACCAsString);
        bitmapFont.draw(batch, ACCAsString, WORLD_WIDTH/2 - glyphLayout.width/2, 65);
        String STAAsString = "STA";
        glyphLayout.setText(bitmapFont, STAAsString);
        bitmapFont.getData().setScale(0.4f, 1f);
        bitmapFont.draw(batch, STAAsString, WORLD_WIDTH/2 - glyphLayout.width/2, 25);
        opponentBar.draw(batch);
        if(catWinFlag){batch.draw(catWinTexture, WORLD_WIDTH/2 - catWinTexture.getWidth()/2, WORLD_HEIGHT/2 - catWinTexture.getHeight()/2);}
        else if(dummyWinFlag){batch.draw(dummyWinTexture, WORLD_WIDTH/2 - dummyWinTexture.getWidth()/2, WORLD_HEIGHT/2 - dummyWinTexture.getHeight()/2);}
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
