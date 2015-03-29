package inc.pashna.digitsincircles;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;

import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.Entity;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.IEntityModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.modifier.IModifier;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MyActivity extends SimpleBaseGameActivity implements OnPositionChangedListener {

    // ===========================================================
    // Constants
    // ===========================================================

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;

    private Camera mCamera;
    public Scene mainScene;

    private BitmapTextureAtlas mBitmapTextureAtlasBlue;
    private BitmapTextureAtlas mBitmapTextureAtlasOrange;

    private BitmapTextureAtlas mBitmapTextureAtlasMenuRound1;
    private BitmapTextureAtlas mBitmapTextureAtlasMenuRound2;
    private BitmapTextureAtlas mBitmapTextureAtlasMenuRound3;

    private TiledTextureRegion orangeRoundTexture;
    private TiledTextureRegion blueRoundTexture;

    private TiledTextureRegion menuRoundTexture1;
    private TiledTextureRegion menuRoundTexture2;
    private TiledTextureRegion menuRoundTexture3;


    private ITextureRegion mBackgroundTextureRegion;
    private ITextureRegion mMenuTextureRegion;
    private Font mFont;

    ITexture fontTextureForAwesome;
    private Font mFontAwesome;

    private int mNumberOfLevel = 1;
    private final String NEW_PLAYER = "NEW_PLAYER";
    private LevelsFactory levelsFactory;

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        SharedPreferences sPref = getPreferences(MODE_PRIVATE);
        if (sPref.getBoolean(NEW_PLAYER, true)) {
            mNumberOfLevel = 0;
        }
    }

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
    }

    @Override
    protected void onCreateResources() {
        // Load all the textures this game needs.
        this.mBitmapTextureAtlasBlue = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.mBitmapTextureAtlasOrange = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.blueRoundTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasBlue, this, "gfx/blue_opacity.png", 0, 0, 1, 1);
        this.orangeRoundTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasOrange, this, "gfx/orange_opacity.png", 0, 0, 1, 1);

        this.mBitmapTextureAtlasBlue.load();
        this.mBitmapTextureAtlasOrange.load();

        // ======= MENU
        this.mBitmapTextureAtlasMenuRound1 = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.mBitmapTextureAtlasMenuRound2 = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.mBitmapTextureAtlasMenuRound3 = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.menuRoundTexture1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenuRound1, this, "gfx/blue_opacity.png", 0, 0, 1, 1);
        this.menuRoundTexture2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenuRound2, this, "gfx/orange.png", 0, 0, 1, 1);
        this.menuRoundTexture3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenuRound3, this, "gfx/orange_opacity.png", 0, 0, 1, 1);

        this.mBitmapTextureAtlasMenuRound1.load();
        this.mBitmapTextureAtlasMenuRound2.load();
        this.mBitmapTextureAtlasMenuRound3.load();
        //

        fontTextureForAwesome = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        this.mFontAwesome = new Font(this.getFontManager(), fontTextureForAwesome, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 45, true, new Color(100, 0, 100));
        this.mFontAwesome.load();

        final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        this.mFont = new Font(this.getFontManager(), fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 45, true, new Color(100, 100, 100));
        this.mFont.load();

        try {
            ITexture backgroundTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/back.png");
                }
            });
            backgroundTexture.load();
            this.mBackgroundTextureRegion = TextureRegionFactory.extractFromTexture(backgroundTexture);

            ITexture menuTexture = new BitmapTexture(this.getTextureManager(), new IInputStreamOpener() {
                @Override
                public InputStream open() throws IOException {
                    return getAssets().open("gfx/background.png");
                }
            });
            menuTexture.load();
            this.mMenuTextureRegion = TextureRegionFactory.extractFromTexture(menuTexture);
        } catch (IOException e) {}
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger()); // logs the frame rate
        levelsFactory = new LevelsFactory(blueRoundTexture, orangeRoundTexture, mFont, getVertexBufferObjectManager(), this);
        stopLevel();
        generateCurrentLevel();
        return this.mainScene;
    }

    @Override
    public void onPositionChanged(float curX, float curY) {
        // Когда последний шарик покинет пределы экрана, пройдем по true
        if (curY > this.mCamera.getHeight()) {
            stopLevel();
            generateMenu(levelsFactory.isCorrectAnswer());
        }
    }

    /*
    Обнуляет текущую сцену, создает бэк для новой
     */
    public void stopLevel() {
        this.mainScene = null; // set your scene to null
        this.mainScene = new Scene(); // initialize scene again, recreate
        this.mEngine.setScene(mainScene); // set scene
        Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
        this.mainScene.attachChild(backgroundSprite);
    }

    /*
    Генерирует новый уровень. По идее, должен быть параметр текущего уровня.
     */
    public void generateNewLevel() {
        mNumberOfLevel++;
        levelsFactory.generateLevel(mNumberOfLevel, mainScene);
    }

    public void generateCurrentLevel() {
        levelsFactory.generateLevel(mNumberOfLevel, mainScene);
    }

    /*
    Создает меню в конце игры
     */
    public void generateMenu(boolean result) {
        if (result) {
            generateWinScreen();
        } else {
//            Sprite backgroundSprite = new Sprite(0, 0, this.mMenuTextureRegion, getVertexBufferObjectManager());
//            this.mainScene.attachChild(backgroundSprite);
            generateLoseMenu();


        }
    }

    private void generateLoseMenu() {
        final MenuRoundObject[] menuRoundObjects = new MenuRoundObject[35]; // Создает кружки

        // Заполняем кружки
        for (int i=0; i<menuRoundObjects.length; i++) {
            int x = new Random().nextInt()%(CAMERA_WIDTH/2) + CAMERA_WIDTH/2;
            int y =  new Random().nextInt()%(CAMERA_HEIGHT/2) + CAMERA_HEIGHT/2;
            switch (i%3) {
                case 0:
                    menuRoundObjects[i] = new MenuRoundObject(x, y, menuRoundTexture1, getVertexBufferObjectManager());
                    break;
                case 1:
                    menuRoundObjects[i] = new MenuRoundObject(x, y, menuRoundTexture2, getVertexBufferObjectManager());
                    break;
                case 2:
                    menuRoundObjects[i] = new MenuRoundObject(x, y, menuRoundTexture3, getVertexBufferObjectManager());
                    break;
            }
            this.mainScene.attachChild(menuRoundObjects[i]);
        }

        // Моргание текста
        LoopEntityModifier loopEntityModifier = new LoopEntityModifier((new SequenceEntityModifier(new FadeOutModifier(0.5f), new FadeInModifier(0.5f))));
        final MyActivity activity = this;
        Text menuText = new Text(0, 0, this.mFont, "      " +"Try Again" + "       ", getVertexBufferObjectManager()) {
            //Обработка касания текста
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    // Заполняем скорости шариков меню
                    for (int i=0; i<menuRoundObjects.length; i++) {
                        int Vx = new Random().nextInt()%500 + 500;
                        int Vy = new Random().nextInt()%500 + 500;
                        menuRoundObjects[i].setVelocity(Vx,Vy);
                    }
                    // Ставим таймер. Через 1.5 секунды нарисовать новый уровень
                    activity.mainScene.registerUpdateHandler(new TimerHandler(1.5f, true, new ITimerCallback() {
                        @Override
                        public void onTimePassed(final TimerHandler pTimerHandler) {
                            activity.stopLevel();
                            activity.generateCurrentLevel();
                        }
                    }));

                }
                return true;
            }

        };

        menuText.setPosition(CAMERA_WIDTH/2-menuText.getWidth()/2, CAMERA_HEIGHT/2-menuText.getHeight()/2);
        menuText.registerEntityModifier(loopEntityModifier);

        this.mainScene.attachChild(menuText);
        this.mainScene.registerTouchArea(menuText);
    }

    private void generateWinScreen() {
        Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
        this.mainScene.attachChild(backgroundSprite);

        //Font font = new Font(this.getFontManager(), , Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 45, true, new Color(100, 0, 100));
        final Text menuText = new Text(0, 0, mFontAwesome, " " +"Awesome" + " ", getVertexBufferObjectManager());
        menuText.setPosition(CAMERA_WIDTH/2-menuText.getWidth()/2, CAMERA_HEIGHT/2-menuText.getHeight()/2);
        setAppearence(menuText, 1, 1);

        mainScene.registerUpdateHandler(new TimerHandler(2.5f, true, new ITimerCallback() {
            @Override
            public void onTimePassed(final TimerHandler pTimerHandler) {
                stopLevel();
                generateNewLevel();
            }
        }));

        this.mainScene.attachChild(menuText);
    }

    public Text createText(String text) {
        final Text menuText = new Text(0, 0, mFontAwesome, text, getVertexBufferObjectManager());
        return menuText;
    }

    public void setAppearence (Entity entity, float timeIn, final float timeOut) {
        final Entity finalEntrity = entity;
        finalEntrity.setAlpha(0);

        FadeInModifier fadeIn = new FadeInModifier(timeIn, new IEntityModifier.IEntityModifierListener() {
            public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
            @Override
            public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                //finalEntrity.setAlpha(1);
                if (timeOut > 0) {
                    FadeOutModifier fadeOut = new FadeOutModifier(timeOut, new IEntityModifier.IEntityModifierListener() {
                        public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
                        @Override
                        public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
                            finalEntrity.setAlpha(0);
                        }
                    });
                    finalEntrity.registerEntityModifier(fadeOut);
                }
            }
        });
        finalEntrity.registerEntityModifier(fadeIn);
    }

    public void dissapearEntity (Entity entity, float timeOut) {
        FadeOutModifier fadeOut = new FadeOutModifier(timeOut);
        entity.registerEntityModifier(fadeOut);
    }

}