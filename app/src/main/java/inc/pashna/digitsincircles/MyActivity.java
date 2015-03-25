package inc.pashna.digitsincircles;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;

import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
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
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

public class MyActivity extends SimpleBaseGameActivity implements OnPositionChangedListener {

    // ===========================================================
    // Constants
    // ===========================================================

    public static final int CAMERA_WIDTH = 480;
    public static final int CAMERA_HEIGHT = 800;

    // ===========================================================
    // Fields
    // ===========================================================

    private Camera mCamera;
    private Scene mMainScene;

    private BitmapTextureAtlas mBitmapTextureAtlas1;
    private BitmapTextureAtlas mBitmapTextureAtlas2;
    private TiledTextureRegion orangeRoundTexture;
    private TiledTextureRegion blueRoundTexture;
    private ITextureRegion mBackgroundTextureRegion;
    private Font mFont;

    // ===========================================================
    // Constructors
    // ===========================================================

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    // ===========================================================
    // Methods for/from SuperClass/Interfaces
    // ===========================================================

    @Override
    public EngineOptions onCreateEngineOptions() {
        this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
        return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), this.mCamera);
    }

    @Override
    protected void onCreateResources() {
        // Load all the textures this game needs.
        this.mBitmapTextureAtlas1 = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.mBitmapTextureAtlas2 = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.blueRoundTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas1, this, "gfx/blue_opacity.png", 0, 0, 1, 1);
        this.orangeRoundTexture = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas2, this, "gfx/orange_opacity.png", 0, 0, 1, 1);
        this.mBitmapTextureAtlas1.load();
        this.mBitmapTextureAtlas2.load();

        final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        this.mFont = new Font(this.getFontManager(), fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 45, true, new Color(254, 200, 250));
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
        } catch (IOException e) {}
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger()); // logs the frame rate
        generateNewLevel();
        return this.mMainScene;
    }

    @Override
    public void onPositionChanged(float curX, float curY) {
        if (curY > this.mCamera.getHeight()) {
            stopLevel();
            generateNewLevel();
        }
    }

    public void stopLevel() {
        this.mMainScene = null; // set your scene to null
        this.mMainScene = new Scene(); // initialize scene again, recreate
        this.mEngine.setScene(mMainScene); // set scene
    }

    public void generateNewLevel() {
        this.mMainScene = new Scene();
        Sprite backgroundSprite = new Sprite(0, 0, this.mBackgroundTextureRegion, getVertexBufferObjectManager());
        this.mMainScene.attachChild(backgroundSprite);

        // Centre the player on the camera.
        final float startX = (CAMERA_WIDTH - this.orangeRoundTexture.getWidth()) / 2;
        final float startY = -50;

        // Create the sprite and add it to the scene.
        final RoundObject orangeRoundObject = new RoundObject(startX, startY, this.orangeRoundTexture, mFont, getVertexBufferObjectManager()) {
            @Override
            public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
                    this.setValue(new Random().nextInt(25) + 25);
                }
                return true;
            }
        };

        final RoundObject blueRoundObject1 = new RoundObject(startX, startY-(orangeRoundObject.getHeight()+10), this.blueRoundTexture, mFont, getVertexBufferObjectManager());
        final RoundObject blueRoundObject2 = new RoundObject(startX, startY-2*(orangeRoundObject.getHeight()+10), this.blueRoundTexture, mFont, getVertexBufferObjectManager());
        final RoundObject blueRoundObject3 = new RoundObject(startX, startY-3*(orangeRoundObject.getHeight()+10), this.blueRoundTexture, mFont, getVertexBufferObjectManager());
        final RoundObject blueRoundObject4 = new RoundObject(startX, startY-4*(orangeRoundObject.getHeight()+10), this.blueRoundTexture, mFont, getVertexBufferObjectManager());
        blueRoundObject4.setPositionListener(this);
        //final Player oPlayer3 = new Player(centerX, centerY+2*(oPlayer1.getHeight()+10), this.orangeRoundTexture, this.getVertexBufferObjectManager(), mFont);

        this.mMainScene.attachChild(blueRoundObject1);
        this.mMainScene.attachChild(blueRoundObject2);
        this.mMainScene.attachChild(blueRoundObject3);
        this.mMainScene.attachChild(blueRoundObject4);


        this.mMainScene.attachChild(orangeRoundObject);
        this.mMainScene.registerTouchArea(orangeRoundObject);
    }

}