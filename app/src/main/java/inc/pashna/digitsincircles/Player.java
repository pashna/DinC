package inc.pashna.digitsincircles;

import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.color.Color;

import java.util.Random;

/**
 * Created by Admin on 25.03.2015.
 */
public class Player extends GameObject {

    public Player(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager, Font pFont) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);

        Text buttonText = new Text(0, 0, pFont, "TEXT", pVertexBufferObjectManager);
        buttonText.setText(new Random().nextInt()%30 + "");
        buttonText.setPosition(this.getWidth()/2 - buttonText.getWidth()/2, this.getHeight()/2 - buttonText.getHeight()/2);
        this.attachChild(buttonText);
    }

    @Override
    public void move() {
        this.mPhysicsHandler.setVelocityY(50);
    }
}
