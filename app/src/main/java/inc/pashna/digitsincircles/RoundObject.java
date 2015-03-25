package inc.pashna.digitsincircles;

import org.andengine.entity.text.Text;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import java.util.Random;

/**
 * Created by Admin on 25.03.2015.
 */
public class RoundObject extends GameObject {
    private Text mButtonText;
    private int value;

    public RoundObject(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

        mButtonText = new Text(0, 0, pFont, "TEXT", pVertexBufferObjectManager);
        mButtonText.setText(new Random().nextInt(25) + "");
        mButtonText.setPosition(this.getWidth() / 2 - mButtonText.getWidth() / 2, this.getHeight() / 2 - mButtonText.getHeight() / 2);
        this.attachChild(mButtonText);
    }

    @Override
    public void move() {
        this.mPhysicsHandler.setVelocityY(150);
    }

    public void setValue(int value) {
        this.mButtonText.setText(value + "");
        this.value = value;
        mButtonText.setPosition(this.getWidth()/2 - mButtonText.getWidth()/2, this.getHeight()/2 - mButtonText.getHeight()/2);
    }

    public void incrementValue() {
        setValue(++this.value);
    }
}
