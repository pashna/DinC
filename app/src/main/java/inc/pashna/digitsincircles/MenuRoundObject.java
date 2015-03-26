package inc.pashna.digitsincircles;

import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

/**
 * Created by Admin on 25.03.2015.
 */
public class MenuRoundObject extends GameObject {
    private float velocityX;
    private float velocityY;

    public MenuRoundObject(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        velocityY = 0;
        velocityX = 0;
    }


    @Override
    public void move() {
        this.mPhysicsHandler.setVelocityY(velocityY);
        this.mPhysicsHandler.setVelocityX(velocityX);
    }

    public void setVelocity(float vX, float vY) {
        velocityX = vX;
        velocityY = vY;
    }

}