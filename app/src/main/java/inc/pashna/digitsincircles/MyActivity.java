package inc.pashna.digitsincircles;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;

import org.andengine.engine.Engine;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
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
import org.andengine.ui.IGameInterface;
import org.andengine.ui.activity.BaseGameActivity;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.adt.io.in.IInputStreamOpener;
import org.andengine.util.color.Color;
import org.andengine.util.debug.Debug;

import android.graphics.Typeface;
import android.os.Bundle;

import java.io.IOException;
import java.io.InputStream;
import java.util.Stack;

public class MyActivity extends SimpleBaseGameActivity {

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

    private BitmapTextureAtlas mBitmapTextureAtlas;
    private TiledTextureRegion mPlayerTiledTextureRegion;
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
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 200, 200);
        this.mPlayerTiledTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "gfx/ring1.png", 0, 0, 1, 1);
        this.mBitmapTextureAtlas.load();
    }

    @Override
    protected Scene onCreateScene() {
        this.mEngine.registerUpdateHandler(new FPSLogger()); // logs the frame rate

        // Create Scene and set background colour to (1, 1, 1) = white
        this.mMainScene = new Scene();
        this.mMainScene.setBackground(new Background(50, 0, 0));

        // Centre the player on the camera.
        final float centerX = (CAMERA_WIDTH - this.mPlayerTiledTextureRegion.getWidth()) / 2;
        final float centerY = -50;

        final ITexture fontTexture = new BitmapTextureAtlas(this.getTextureManager(), 256, 256, TextureOptions.BILINEAR);
        this.mFont = new Font(this.getFontManager(), fontTexture, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 15, true, new Color(1f, 0, 0.1f));
        this.mFont.load();

        // Create the sprite and add it to the scene.
        final Player oPlayer1 = new Player(centerX, centerY, this.mPlayerTiledTextureRegion, this.getVertexBufferObjectManager(), mFont);
        final Player oPlayer2 = new Player(centerX, centerY+oPlayer1.getHeight()+10, this.mPlayerTiledTextureRegion, this.getVertexBufferObjectManager(), mFont);
        final Player oPlayer3 = new Player(centerX, centerY+2*(oPlayer1.getHeight()+10), this.mPlayerTiledTextureRegion, this.getVertexBufferObjectManager(), mFont);

        this.mMainScene.attachChild(oPlayer1);
        this.mMainScene.attachChild(oPlayer2);
        this.mMainScene.attachChild(oPlayer3);

        return this.mMainScene;
    }
}