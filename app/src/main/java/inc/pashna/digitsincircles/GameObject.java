package inc.pashna.digitsincircles;

import org.andengine.engine.handler.physics.PhysicsHandler;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Admin on 25.03.2015.
 */
abstract public class GameObject extends AnimatedSprite {

    public PhysicsHandler mPhysicsHandler;
    private OnPositionChangedListener positionListener;

    public GameObject(final float pX, final float pY, final ITiledTextureRegion pTiledTextureRegion, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.mPhysicsHandler = new PhysicsHandler(this);
        this.registerUpdateHandler(this.mPhysicsHandler);
        positionListener = null;
    }

    public void setPositionListener(OnPositionChangedListener positionListener) {
        this.positionListener = positionListener;
    }

    @Override
    protected void onManagedUpdate(float pSecondsElapsed) {
        move();
        if (positionListener != null) positionListener.onPositionChanged(getX(), getY());
        super.onManagedUpdate(pSecondsElapsed);
    }

    public abstract void move();

}
