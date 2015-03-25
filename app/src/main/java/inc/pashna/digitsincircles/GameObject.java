package inc.pashna.digitsincircles;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.text.Text;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;

/**
 * Created by Admin on 25.03.2015.
 */
abstract public class GameObject extends AnimatedSprite {

    public PhysicsHandler mPhysicsHandler;

    public GameObject(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.mPhysicsHandler = new PhysicsHandler(this);
        this.registerUpdateHandler(this.mPhysicsHandler);
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        move();
        super.onManagedUpdate(pSecondsElapsed);
    }

    public abstract void move();

}
