package com.packt.gamejamone;

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
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

class StartScreen extends ScreenAdapter {

    /*
    Dimensions -- Units the screen has
     */
    private static final float WORLD_WIDTH = 480;
    private static final float WORLD_HEIGHT = 320;

    private Stage menu;     //Main screen buttons that choose if you're playing against a bot or not
    private Stage tutorial;   //Button that allows the player to leave the turorial screen

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
    private DistanceBar playerBar;      //Bar that tells the player how far they are from opponent
    private DistanceBar opponentBar;    //Bar that tells the opponent how far they are from player
    private StatusBar playerHealth;     //Displays player health, numeric visual and player name
    private StatusBar opponentHealth;   //Displays the opponent health bar, numeric visual and name
    private StatusBar playerStamina;    //Displays the players stamina bar and numeric value for it
    private StatusBar opponentStamina;  //Displays the opponent stamina bar and numeric value of it
    private AttackBox playerAttackBox;  //Displays the player attack box which shows off the move they have and attack details
    private AttackBox opponentAttackBox;//Displays the opponent attack box which shows off the move they have and attack details
    private Fighter player;             //The player unit through which all operations go through and from which info is drawn from
    private Fighter opponent;           //The opponent unit through which all operations go through and from which info is drawn from

    //Timing variables
    //Rate at which the stamina is ticked up
    private static float STA_TIME = 0.5f;
    private float staTimer = STA_TIME;

    //Rate at which the hitFlag, missFlag, dodgeFlag,
    // playerDrawAttackFlag, opponentDrawAttackFlag, waitFlag
    //are reset
    private static float FLAG_TIME = 0.5f;
    private float flagTimer = FLAG_TIME;

    //Wait period between the player or user hitting and the visual display showing the hit or miss sign
    private static float WAIT_FLAG_TIME = 0.3f;
    private float waitFlagTimer = WAIT_FLAG_TIME;

    //Time between the player or opponent winning and the game returning to the main menu
    private static float RESTART_TIME = 2f;
    private float restartTimer = RESTART_TIME;

    //Textures
    private Texture healthTexture;
    private Texture healthFrameTexture;
    private Texture tutorialTexture;
    private Texture staminaTexture;
    private Texture staminaFrameTexture;
    private Texture pointerTexture;
    private Texture pointerBarTexture;
    private Texture attackFrameTexture;
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
    private boolean computerFlag = false;       //Tells us if it's a computer or human as opponent
    private boolean announceFlag = false;
    private boolean waitFlag = false;           //Flag that displays the
    private boolean hitFlag = false;            //Flag that displays the hit sign
    private boolean dodgeFlag = false;          //Flag that display the dodge sign
    private boolean missFlag = false;           //Flag that displays the miss sign
    private boolean menuFlag = true;            //Flag that displays the main menu
    private boolean endFlag = false;            //Flag that tells the game is over
    private boolean catWinFlag = false;         //Flag that displays the cat win sign
    private boolean dummyWinFlag = false;       //Flag that displays the dummy win sign
    private boolean tutorialFlag = false;       //Flag that displays the tutorial page
    private boolean playerDrawAttackFlag = false;   //Flag that tells to display player attacking
    private boolean opponentDrawAttackFlag = false; //Flag that tells to display opponent attacking
    private int playerAttackSelection = 0;          //Player's current attack
    private int lockedInPlayerAttackSelection = playerAttackSelection;  //Player's attack that being performed
    private int opponentAttackSelection = 0;        //Opponent's current attack
    private int lockedInOpponentAttackSelection = opponentAttackSelection;  //Opponent's attack that's being currently performed

    StartScreen() {}

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
        showMenuButton();       //Draws the main menu and it's buttons
        tutorialButton();       //Draws the tutorial and it's buttons
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
   Purpose: Function that initializes the major components that are updated through the game
   */
    private void showObjects(){
        showPlayer();           //Sets up the player and it's attacks
        showPlayerUI();         //Sets up the UI that draw from the player
        showOpponent();         //Sets up the opponent and it's UI
        showOpponentUI();       //Sets up the UI that draws from the opponent
    }

    /*
    Input: Void
    Output: Void
    Purpose: Create the player and all of the attacks he posses
    */
    private void showPlayer(){
        //Create the player
        player = new Fighter(playerTexture);
        //Set the players stats
        player.setStats(5,5,30,5,120,30);
        //Places the player on the left side of the screen
        player.setPosition(120 - player.getWidth(),120);
        //Give the player a sprite sheet to use
        player.setTexture(spriteSheetTexture);

        //Sets up the three attacks
        setUpAttack(fireHitTexture, "Fire Ball", 5, 10, 10, player.getX()+player.getWidth(),
                player.getY()+player.getHeight()/2, 60, 60, player);
        setUpAttack(bulletHitTexture, "Cheating Gun", 15, 1, 15,player.getX()+player.getWidth(),
                player.getY()+player.getHeight()/2, 10, 10 , player);
        setUpAttack(fistHitTexture, "Fist", 20, 15, 20, player.getX()+player.getWidth(),
                player.getY()+player.getHeight()/2, 30, 30, player);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Create the opponent and all of the attacks its posses
    */
    private void showOpponent(){
        opponent = new Fighter(opponentTexture);
        opponent.setPosition(360,120);
        opponent.setStats(10, 2, 60, 2, 200, 20);

        setUpAttack(scaryEyeTexture, "Evil Eye", 30, 50, 50, opponent.getX(),
                opponent.getY()+3*opponent.getHeight()/4, 60, 60, opponent );
        setUpAttack(scaryEyeTexture, "Evil Eye", 30, 50, 50, opponent.getX(),
                opponent.getY()+3*opponent.getHeight()/4, 60, 60, opponent );
        setUpAttack(scaryEyeTexture, "Evil Eye", 30, 50, 50, opponent.getX(),
                opponent.getY()+3*opponent.getHeight()/4, 60, 60, opponent );
    }

    /*
    Input: Texture of attack, Stats: Name, STR, ACC, and STA, position where it spawns,
        width and height of the attack, and who it belongs to
    Output: Void
    Purpose: Creates the attack for to be equipped to designated opponent
    */
    private void setUpAttack(Texture texture, String name, int STR, int ACC, int  STA,
                             float x ,float y, float width, float height, Fighter fighter){
        //Sets up the attack with texture
        Attack attack = new Attack(texture);
        //Sets up the attack stats
        attack.setAttributes(name, STR, ACC, STA);
        //Sets up where the attack will spawn
        attack.setPosition(x, y);
        //Sets the size of the attack image
        attack.setSize(width,height);
        //Adds the attack to the user
        fighter.addAttack(attack);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Set up all of the visual components that are updated form the user character
    */
    private void showPlayerUI(){
        //Sets up the bar that tells distance between the player and opponent
        playerBar = setUpDistanceBar(pointerBarTexture, pointerTexture, 10);
        //Sets up the health bar
        playerHealth = setUpStatusBar(40, 5, true, healthFrameTexture, healthTexture,
                10, 260, player.getHealthFull(), 10, 255);
        //Sets up the stamina bar
        playerStamina = setUpStatusBar(15, 3, false, staminaFrameTexture, staminaTexture,
                10, 10, 100, 215, 25);
        //Sets up the attackBox that displays attacks and their stats
        playerAttackBox = setUpAttackBox(attackFrameTexture, player.getAttackArray(), highlightTexture, 10,
                215, 215);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Set up all of the visual components that are updated form the user character
    */
    private void showOpponentUI(){
        //Sets up the bar that tells distance between the player and opponent
        opponentBar = setUpDistanceBar(pointerBarTexture, pointerTexture, 270);
        //Sets up the health bar
        opponentHealth = setUpStatusBar(40, 5, true, healthFrameTexture, healthTexture,
                270, 260, opponent.getHealthFull(), 440, 255);
        //Sets up the stamina bar
        opponentStamina = setUpStatusBar(15, 3, false, staminaFrameTexture, staminaTexture,
                270, 10, 100, 255, 25);
        //Sets up the attackBox that displays attacks and their stats
        opponentAttackBox = setUpAttackBox(attackFrameTexture, opponent.getAttackArray(), highlightTexture, 270,
                255, 255);
    }

    /*
    Input: Texture that shows background frame, Texture that shows the pointer
            position of the frame
    Output: DistanceBar
    Purpose: Sets up the distance bar
    */
    private DistanceBar setUpDistanceBar(Texture pointerBarTexture, Texture pointerTexture,
                                         float x){
        //Creates the moving bar that tells the distance between user and opponent and allows
        //defense which move the user can use
        DistanceBar distanceBar = new DistanceBar(pointerBarTexture, pointerTexture);
        distanceBar.setInitialPosition(x, (float) 100);
        return distanceBar;
    }

    /*
    Input: Height of the frame, offset between frame and the inner bar, flag that tells if the bar
        is filed empty, texture fo the frame and texture of the, moving bar, maxFill which tell the
        full value of bar used for conversion between sta and pixel distance, and position of the text.
    Output: StatusBar
    Purpose: Sets up the health and stamina bars for player and opponent
    */
    private StatusBar setUpStatusBar(float height, float offset, boolean fullFlag, Texture barTexture,
                                     Texture innerTexture, float x, float y, int maxFill,
                                     float xText, float yText){
        //Creates the bar with it's dimensions and texture
        StatusBar statusBar = new StatusBar(height, offset, fullFlag, barTexture, innerTexture);
        //Places the bar on screen
        statusBar.setPosition(x, y);
        //Finds out the conversion between user stats and screen size
        statusBar.setHealth(maxFill);
        //Places the text on screen
        statusBar.setTextPosition(xText, yText);
        return statusBar;
    }

    /*
    Input: Frame Texture, Array of attacks of the user to grab the textures from, position,
            Text positions
    Output: AttackBox
    Purpose: Sets attack box UI which shows which attacks the user has and their stats
    */
    private AttackBox setUpAttackBox(Texture frameTexture, Array<Attack> attackArray, Texture highlightTexture,
                                     float x, float xDMG, float xCost){
        //Sets up the box with all of the necessary textures
        AttackBox attackBox = new AttackBox(frameTexture, attackArray.get(0).getTexture(), attackArray.get(1).getTexture(), attackArray.get(2).getTexture(), highlightTexture);
        //Sets up the position
        attackBox.setPosition(x, (float) 35);
        //Sets up the text positions
        attackBox.setTextPosition(xDMG, (float) 105,xCost, (float) 65);
        return attackBox;
    }

    /*
    Input: Void
    Output: Void
    Purpose: Shows the main menu
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

        //Turns off the main menu and turns on the tutorial, setting up the opponent to be a robot
        menuButton.addListener(new ActorGestureListener() {@Override
            public void tap(InputEvent event, float x, float y, int count, int button) {
                super.tap(event, x, y, count, button);
                menuFlag = false;
                tutorialFlag = true;
                computerFlag = true;
                Gdx.input.setInputProcessor(tutorial);
            }
            });

        //Creates button and position
        ImageButton twoPlayer = new ImageButton(new TextureRegionDrawable(new TextureRegion(menuUpTexture)), new TextureRegionDrawable(menuDownTexture));
        twoPlayer.setPosition(WORLD_WIDTH/2, WORLD_HEIGHT/2 - 80, Align.center);
        menu.addActor(twoPlayer);

        //Turns off the main menu and turns on the tutorial, setting up the opponent to be a human
        twoPlayer.addListener(new ActorGestureListener() {@Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            super.tap(event, x, y, count, button);
            menuFlag = false;
            tutorialFlag = true;
            computerFlag = false;
            Gdx.input.setInputProcessor(tutorial);
        }
        });
    }

    /*
    Input: Void
    Output: Void
    Purpose: Sets up the tutorial screen
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

        //Turns off the tutorial screen and begins the game
        tutorialButton.addListener(new ActorGestureListener() {@Override
        public void tap(InputEvent event, float x, float y, int count, int button) {
            super.tap(event, x, y, count, button);
            tutorialFlag = false;
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
        //Health Bar Textures
        healthTexture = new Texture(Gdx.files.internal("Health.png"));
        healthFrameTexture = new Texture(Gdx.files.internal("HealthFrame.png"));

        //Stamina Bar Textures
        staminaTexture = new Texture(Gdx.files.internal("STA.png"));
        staminaFrameTexture = new Texture(Gdx.files.internal("STAFrame.png"));

        //Distance Bar Textures
        pointerBarTexture = new Texture(Gdx.files.internal("PointerBar.png"));
        pointerTexture = new Texture(Gdx.files.internal("Pointer.png"));

        //Attack Textures
        attackFrameTexture = new Texture(Gdx.files.internal("AttackFrame.png"));
        fireHitTexture = new Texture(Gdx.files.internal("FireEffect.png"));
        fistHitTexture = new Texture(Gdx.files.internal("HandEffect.png"));
        bulletHitTexture = new Texture(Gdx.files.internal("BulletEffect.png"));
        scaryEyeTexture = new Texture(Gdx.files.internal("EyeEffect.png"));
        highlightTexture = new Texture(Gdx.files.internal("HighLight.png"));

        //Character Textures
        playerTexture = new Texture(Gdx.files.internal("Player.png"));
        spriteSheetTexture = new Texture(Gdx.files.internal("PlayerSpriteSheet.png"));
        opponentTexture = new Texture(Gdx.files.internal("Opponent.png"));

        //Sign Textures
        hitTexture = new Texture(Gdx.files.internal("Hit.png"));
        missTexture = new Texture(Gdx.files.internal("Miss.png"));
        dodgeTexture = new Texture(Gdx.files.internal("Dodge.png"));
        catWinTexture = new Texture(Gdx.files.internal("PawsWin.png"));
        dummyWinTexture = new Texture(Gdx.files.internal("DummyWin.png"));

        //Background Texture
        backgroundTexture = new Texture(Gdx.files.internal("Background.png"));

        //Tutorial Screen Texture
        tutorialTexture = new Texture(Gdx.files.internal("Tutorial.png"));

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
        //Wipes screen black
        clearScreen();
        if(!menuFlag && !tutorialFlag) {
            //Draws the game
            drawGameScreen();
            //As long as the game hasn't ended update all variables
            if(!endFlag){update(delta);}
            //If game ended wait RESTART_TIME before going back to main menu and resetting the game
            else{
                restartTimer -= delta;
                if (restartTimer <= 0) {
                    restartTimer = RESTART_TIME;
                    restart();
                }
            }
        }
        //Draws tutorial screen
        else if(tutorialFlag){ drawTutorialScreen();}
        //Draws main menu
        else{ drawMainMenu(); }

        //setDebugMode();                 //Checks if user changed the status of the debugModeFlag
        if(false) {                     //If debugMode is on ShapeRender will drawing lines
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
    Input: Delta, timing
    Output: Void
    Purpose: Central function which updates all of the variables of the game
    */
    private void update(float delta){
        //Checks if we need to remove the sign off the screen
        if(hitFlag || missFlag || dodgeFlag){resetFlags(delta);}
        //Checks for user or opponent button presses
        checkForCharacterAction(delta);
        //Updates the animation and bounds in which the character cna move in
        updateCharacterBounds(delta);
        //If attack was started waits till it displays the result
        if(announceFlag){checkForAnnouncement(delta);}
        //Update the strength value
        updateCharacterStrength(delta);
        //Updates where the slider is based on position of characters
        updateSliderPositions();
        //Updates what attacks the characters can use based on their position
        updateCharacterAttack();
        //Restarts the position of the character objects
        resetAttacksPositions();
        //Update the current health of characters
        updateHealth();
        //Updates the current stamina of the characters
        updateStamina(delta);
        //Checks if either of the characters is dead
        checkIfDead();
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Checks if either the player or computer is initiating an attack
    */
    private void checkForCharacterAction(float delta){
        updatePlayerAction(delta);
        if(computerFlag){
            opponent.updateAI(delta);                   //AI Moves
            if(!missFlag && !hitFlag && !dodgeFlag) {initiateAttack(false, delta);}      //AI Attacks
        }
        else { updateOpponentAction(delta);}                     //Human
    }

    /*
    Input: Delta for timing
    Output: Void
    Purpose: Checks if player clicked any of the buttons
    */
    private void updatePlayerAction(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)) {player.updatePlayer(false);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)) {player.updatePlayer(true);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.W) && !missFlag && !hitFlag && !dodgeFlag) {initiateAttack(true, delta);}
    }

    /*
    Input: Delta for timing
    Output: Void
    Purpose: Checks if opponent clicked any of the buttons
    */
    private void updateOpponentAction(float delta){
        if(Gdx.input.isKeyJustPressed(Input.Keys.LEFT)) {opponent.updatePlayer(false);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {opponent.updatePlayer(true);}
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP) && !missFlag && !hitFlag && !dodgeFlag) {initiateAttack(false, delta);}
    }

    /*
    Input: userAttack, used to determine who initiated the attack
    Output: Void
    Purpose: Sets up the info to attack the other character
    */
    private void initiateAttack(boolean userAttack, float delta){
        if(userAttack) {
            lockedInPlayerAttackSelection = playerAttackSelection;
            playerDrawAttackFlag = attack(player, opponent, lockedInPlayerAttackSelection, delta);
        }
        else{
            lockedInOpponentAttackSelection = opponentAttackSelection;
            opponentDrawAttackFlag = attack(opponent, player, lockedInOpponentAttackSelection, delta);
        }
    }

    /*
    Input: Attack is the person who's attack stats we pull, defender is who's defense stats we pull
            and attack selection tells us which move the attacker is using
    Output: Tells us to turn on the attack flag for that character
    Purpose: Sets up the info to attack the other character
    */
    private boolean attack(Fighter fighterAttacker, Fighter fighterDefender, int attackSelection, float delta){
        //Sets announce flag on to say that there will be a sign poping up soon
        announceFlag = true;
        //Checks if the attacker has enough stamina to attack
        if(fighterAttacker.getSpendStamina(attackSelection)){
            //Lowers the attackers stamina by the cost
            fighterAttacker.updateStamina(fighterAttacker.getCurrentAttackCost(attackSelection), attackSelection);
            //Does a roll of die to see if the attack hits or misses
            System.out.println(attackSelection);
            System.out.println(fighterAttacker.getAccuracy(attackSelection));
            int diceRoll = MathUtils.random(fighterAttacker.getAccuracy(attackSelection),100);
            if(diceRoll > 50){
                //Attacks the defender, if successful displays hit, if not displays dodge
                if(fighterDefender.updateHealth(fighterAttacker.getStrength(attackSelection, delta))){hitFlag = true;}
                else{dodgeFlag = true;}
            }
            //If they missed displays miss
            else { missFlag = true;}
            //Tells the attacker to draw the attack animation
            return true;
        }
        //Tells attacker to stay as they are
        else { return false; }
    }

    /*
    Input: Delta for timing
    Output: Void
    Purpose: Updates the character the bound to which they can walk to
    */
    private void updateCharacterBounds(float delta){
        player.updateBoundsAndAnimation(0, opponent.getX(), delta);
        opponent.updateBoundsAndAnimation(player.getX()+player.getWidth(), 480, delta);
    }

    /*
    Input: Delta for timing
    Output: Void
    Purpose: Updates the strength roll for this tick
    */
    private void updateCharacterStrength(float delta){
        playerAttackBox.setText(player.getStrength(playerAttackSelection, delta), player.getCurrentAttackCost(playerAttackSelection));
        opponentAttackBox.setText(opponent.getStrength(opponentAttackSelection, delta), opponent.getCurrentAttackCost(opponentAttackSelection));
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates the position of the slider
    */
    private void updateSliderPositions(){
        playerBar.updateSliderPositionPlayer(getDistanceBetweenFighters());
        opponentBar.updateSliderPositionOpponent(getDistanceBetweenFighters());
    }

    /*
    Input: Void
    Output: Distance between the characters
    Purpose: Gets the distance between characters to update the slider position
    */
    private float getDistanceBetweenFighters() {return (opponent.getX() - player.getX() - player.getWidth()); }

    /*
    Input: Void
    Output: Void
    Purpose: Updates what attack the characters can use based on their position
    */
    private void updateCharacterAttack(){
        playerAttackSelection = playerAttackBox.updateHighlight(playerBar.getPositionOfSlider());
        opponentAttackSelection = opponentAttackBox.updateHighlight(opponentBar.getPositionOfSlider());
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Ticks down for time till show the result of attack
    */
    private void checkForAnnouncement(float delta){
        waitFlagTimer -= delta;
        if (waitFlagTimer <= 0) {
            waitFlagTimer = WAIT_FLAG_TIME;
            announceFlag = false;       //Stop waiting
            waitFlag = true;            //Display
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: If the player or opponents aren't being drawn then resets the position of their attacks
    */
    private void resetAttacksPositions(){
        if(!playerDrawAttackFlag){
            player.restartAttackPosition(player.getX()+player.getWidth(), player.getY()+player.getHeight()/2);
        }
        if(!opponentDrawAttackFlag){
            opponent.restartAttackPosition(opponent.getX(), opponent.getY()+2*opponent.getHeight()/3);
        }
    }

    /*
    Input: Delta for timing
    Output: Void
    Purpose: Updates the stamina increase and the visual representation every STA_TIME tick
    */
    private void updateStamina(float delta){
        staTimer -= delta;
        if (staTimer <= 0) {
            staTimer = STA_TIME;
            //Update the stamina in the characters
            player.updateStamina();
            opponent.updateStamina();
            //Get that new stamina in the stamina bar
            playerStamina.setStaminaPlayer(player.getStamina());
            opponentStamina.setStaminaOpponent(opponent.getStamina());
            //Updates the stamina Text
            playerStamina.setStaminaText((int) player.getStamina());
            opponentStamina.setStaminaText((int) opponent.getStamina());
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Updates the health bar visual and the numeric representation
    */
    private void updateHealth(){
        //Update the bar
        playerHealth.setPlayerHealth(player.getHealthCurrent());
        opponentHealth.setOpponentHealth(opponent.getHealthCurrent());
        //Update the text
        opponentHealth.setHealthText(opponent.getHealthCurrent(), opponent.getHealthFull());
        playerHealth.setHealthText(player.getHealthCurrent(), player.getHealthFull());
    }

    /*
    Input: Delta, timing
    Output: Void
    Purpose: Turns off all the flags that have signs draw ontop
    */
    private void resetFlags(float delta){
        flagTimer -= delta;
        if (flagTimer <= 0) {
            flagTimer = FLAG_TIME;
            //Attack Signs
            hitFlag = false;
            missFlag = false;
            dodgeFlag = false;
            //Character pose flags
            playerDrawAttackFlag = false;
            opponentDrawAttackFlag = false;
            //Pause between drawing flag
            waitFlag = false;
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Checks if either of the characters is dead, if so end game
    */
    private void checkIfDead(){
        //Check if player died
        if(player.getHealthCurrent() == 0) {
            dummyWinFlag = true;        //Puts out opponent victory sign
            hitFlag = false;            //Turns off hit sign
            endFlag = true;             //Says to end game
        }
        //Check if opponent died
        else if(opponent.getHealthCurrent() == 0){
            catWinFlag = true;          //Puts out player victory sign
            hitFlag = false;            //Turns off hit sign
            endFlag = true;             //Say to end game
        }
    }

    /*
    Input: Void
    Output: Void
    Purpose: Restart the variables to initial position and removes all flowers from array.
    */
    private void restart(){
        //Turns on the menu
        menuFlag = true;
        //Gives input power to menu
        Gdx.input.setInputProcessor(menu);
        //Resets the Character stats and position
        restartCharacter(player, 120 - player.getWidth());
        restartCharacter(opponent,360);

        //Restarts drawing flags
        playerDrawAttackFlag = false;
        opponentDrawAttackFlag = false;
        catWinFlag = false;
        dummyWinFlag = false;

        //Restarts the game end flag
        endFlag = false;
    }

    /*
    Input: Fighter, position
    Output: Void
    Purpose: Restarts the given character to a position, make their health full and stamina 0
    */
    private void restartCharacter(Fighter fighter, float x){
        fighter.setPosition(x, (float) 120);
        fighter.setHealth(fighter.getHealthFull());
        fighter.setStamina();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the main menu and it's buttons
    */
    private void drawMainMenu(){
        menu.draw();
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture
        batch.begin();
        //Draws text on buttons
        drawText("Practice",WORLD_WIDTH/2 - glyphLayout.width/2, WORLD_HEIGHT/2 + glyphLayout.height/2 );
        drawText("2 Player", WORLD_WIDTH/2 - glyphLayout.width/2, WORLD_HEIGHT/2 - 80 + glyphLayout.height/2);
        batch.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the tutorial screen
    */
    private void drawTutorialScreen(){
        //Set up camera
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);

        //Draw background
        batch.begin();
        batch.draw(tutorialTexture, 0 ,0, WORLD_WIDTH, WORLD_HEIGHT);
        batch.end();

        //Draw button on background
        tutorial.draw();

        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Draw text on button
        batch.begin();
        drawText("Play", WORLD_WIDTH/2 - glyphLayout.width/2, WORLD_HEIGHT/4 + glyphLayout.height/2);
        batch.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Central function that draws the textures
    */
    private void drawGameScreen() {
        //Viewport/Camera projection
        batch.setProjectionMatrix(camera.projection);
        batch.setTransformMatrix(camera.view);
        //Batch setting up texture
        batch.begin();
        //Draw background
        batch.draw(backgroundTexture, 0,0, WORLD_WIDTH, WORLD_HEIGHT);
        //Draw characters
        player.draw(batch);
        opponent.draw(batch);
        //Draws all the boxes that show of player stats
        drawUI();
        //Draws text to clarify those boxes
        bitmapFont.getData().setScale(0.4f, 1f);
        drawTextUI();
        //Draws the attack being performed
        drawAttack();
        //Signs that overlay on top of the game
        drawSigns();
        batch.end();
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the attack results and victory result signs
    */
    private void drawSigns(){
        //Attack signs
        if(waitFlag) {
            if (hitFlag) { batch.draw(hitTexture,   WORLD_WIDTH / 2 - (float) hitTexture.getWidth() / 2, WORLD_HEIGHT / 2 - (float) hitTexture.getHeight() / 2); }
            if (missFlag) { batch.draw(missTexture, WORLD_WIDTH / 2 - (float) missTexture.getWidth() / 2, WORLD_HEIGHT / 2 - (float) missTexture.getHeight() / 2); }
            if (dodgeFlag) { batch.draw(dodgeTexture, WORLD_WIDTH / 2 - (float) dodgeTexture.getWidth() / 2, WORLD_HEIGHT / 2 - (float) dodgeTexture.getHeight() / 2); }
        }

        //Victory signs
        if(catWinFlag){batch.draw(catWinTexture, WORLD_WIDTH/2 - (float) catWinTexture.getWidth()/2, WORLD_HEIGHT/2 - (float) catWinTexture.getHeight()/2);}
        else if(dummyWinFlag){batch.draw(dummyWinTexture, WORLD_WIDTH/2 - (float) dummyWinTexture.getWidth()/2, WORLD_HEIGHT/2 - (float) dummyWinTexture.getHeight()/2);}
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws the attack that the character performs
    */
    private void drawAttack(){
        if(playerDrawAttackFlag){player.drawAttack(lockedInPlayerAttackSelection, true, batch);}
        if(opponentDrawAttackFlag){opponent.drawAttack(lockedInOpponentAttackSelection, false, batch);}
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws UI that represents player stats
    */
    private void drawUI(){
        //Health Bar
        playerHealth.draw(batch);
        opponentHealth.draw(batch);

        //Stamina BAr
        playerStamina.draw(batch);
        opponentStamina.draw(batch);

        //Attack Box
        playerAttackBox.draw(batch);
        opponentAttackBox.draw(batch);

        //Distance Bar
        playerBar.draw(batch);
        opponentBar.draw(batch);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws Text UI that represents player stats
    */
    private void drawTextUI(){
        //Health Text
        opponentHealth.drawText(glyphLayout, bitmapFont, batch);
        playerHealth.drawText(glyphLayout,bitmapFont,batch);

        //Stamina Text
        playerStamina.drawText(glyphLayout, bitmapFont, batch);
        opponentStamina.drawText(glyphLayout, bitmapFont, batch);

        //Attack Box Texts
        playerAttackBox.drawText(glyphLayout, bitmapFont, batch);
        opponentAttackBox.drawText(glyphLayout, bitmapFont, batch);

        //Clarifying Text
        drawText("DMG", WORLD_WIDTH/2 - glyphLayout.width/2, 105);
        drawText("COST", WORLD_WIDTH/2 - glyphLayout.width/2, 65);
        drawText("STA", WORLD_WIDTH/2 - glyphLayout.width/2, 25);
    }

    /*
    Input: Void
    Output: Void
    Purpose: Draws given text on screen
    */
    private void drawText(String string, float x, float y){
        glyphLayout.setText(bitmapFont, string);
        bitmapFont.draw(batch, string, x, y);
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
