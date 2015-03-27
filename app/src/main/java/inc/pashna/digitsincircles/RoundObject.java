package inc.pashna.digitsincircles;

import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import java.util.Random;

/**
 * Created by Admin on 25.03.2015.
 */
public class RoundObject extends GameObject {
    private Text mButtonText;
    private int value;
    LoopEntityModifier loopEntityModifier;

    /*
    Конструктор игровых объектов. Заполняет поле value заданным значением
     */
    public RoundObject(int value, final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.value = value;

        mButtonText = new Text(0, 0, pFont, " " +value + " ", pVertexBufferObjectManager);
        mButtonText.setText(value + "");
        mButtonText.setPosition(this.getWidth() / 2 - mButtonText.getWidth() / 2, this.getHeight() / 2 - mButtonText.getHeight() / 2);
        this.attachChild(mButtonText);
    }

    /*
    Конструктор игровых объектов. Заполняет поле value значением -1.
     */
    public RoundObject(final float pX, final float pY, final TiledTextureRegion pTiledTextureRegion, Font pFont, final VertexBufferObjectManager pVertexBufferObjectManager) {
        super(pX, pY, pTiledTextureRegion, pVertexBufferObjectManager);
        this.value = -1;

        mButtonText = new Text(0, 0, pFont, " ? ", pVertexBufferObjectManager);
        //mButtonText.setText(value + "");
        mButtonText.setPosition(this.getWidth() / 2 - mButtonText.getWidth() / 2, this.getHeight() / 2 - mButtonText.getHeight() / 2);
        loopEntityModifier = new LoopEntityModifier((new SequenceEntityModifier(new FadeOutModifier(0.5f), new FadeInModifier(0.5f))));
        mButtonText.registerEntityModifier(loopEntityModifier);
        this.attachChild(mButtonText);
    }

    @Override
    public void move() {
        this.mPhysicsHandler.setVelocityY(150);
    }

    /*
    Устанавливает значение value на экран
     */
    public void setValue(int value) {
        this.mButtonText.setText(value + "");
        this.value = value;
        mButtonText.setPosition(this.getWidth()/2 - mButtonText.getWidth()/2, this.getHeight()/2 - mButtonText.getHeight()/2);
    }

    public void incrementValue() {
        this.value = this.value + 1;
        setValue(this.value);
    }

    /*
    При касании делаем инкремент значения value
     */
    @Override
    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
        if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_UP) {
            this.incrementValue();
            mButtonText.unregisterEntityModifier(loopEntityModifier);
            mButtonText.setAlpha(1);
            Debug.d("Value = " + value);
        }
        return true;
    }

    public int getValue() {
        return this.value;
    }
}
