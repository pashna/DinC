package inc.pashna.digitsincircles;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Admin on 25.03.2015.
 */
public class Cloud extends Sprite {

    private float mParallaxSpeed;
    private float mOffsetX = 0;

    public Cloud(float pX, float pY, float mParallaxSpeed, ITextureRegion pTextureRegion, VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTextureRegion, pVertexBufferObjectManager);

        this.mParallaxSpeed = mParallaxSpeed;
        this.mOffsetX = pX - (pX * mParallaxSpeed);
    }

    @Override
    public void onManagedUpdate(float pSecondsElapsed){
        super.onManagedUpdate(pSecondsElapsed);
        this.mOffsetX += this.mParallaxSpeed * pSecondsElapsed;
    }

    @Override
    protected void onManagedDraw(GLState pGLState, Camera pCamera) {
        pGLState.pushModelViewGLMatrix();
        {
            final float shapeWidthScaled = this.getWidthScaled();
            final float cameraWidth = pCamera.getWidth();
            float baseOffsetX = (this.mOffsetX * this.mParallaxSpeed);

            pGLState.translateModelViewGLMatrixf(baseOffsetX, 0, 0);

            float currentMaxX = baseOffsetX;

                this.preDraw(pGLState, pCamera);
                this.draw(pGLState, pCamera);
                this.postDraw(pGLState, pCamera);
                pGLState.translateModelViewGLMatrixf(shapeWidthScaled, 0, 0);
                currentMaxX += shapeWidthScaled;
        }
        pGLState.popModelViewGLMatrix();
    }

    public void setSpeed(float pSpeed){
        this.mParallaxSpeed = pSpeed;
    }

    public float getSpeed(float pSpeed){
        return this.mParallaxSpeed;
    }
}
