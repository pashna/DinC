package inc.pashna.digitsincircles;

import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Admin on 25.03.2015.
 */
public class LevelsFactory {

    public static int BLUE = 1;
    public static int ORANGE = 2;

    private TiledTextureRegion blue, orange;
    private Font font;
    private VertexBufferObjectManager vertexBufferObjectManager;
    OnPositionChangedListener positionChangedListener;
    private RoundObject[] roundObjects;
    private int[] mValue;


    public LevelsFactory(TiledTextureRegion blue, TiledTextureRegion orange, Font font, VertexBufferObjectManager vertexBufferObjectManager, OnPositionChangedListener listener) {
        this.blue = blue;
        this.orange = orange;
        this.font = font;
        this.vertexBufferObjectManager = vertexBufferObjectManager;
        this.positionChangedListener = listener;
    }

    public void generateLevel(int number, Scene scene) {
        switch (number) {
            case 0:
                int type[] = {ORANGE, ORANGE, ORANGE, BLUE, ORANGE, ORANGE, ORANGE};
                int value[] = {1, 2, 3, 5, 8, 13, 21};
                createRoundObjects(type, value, (int)(MyActivity.CAMERA_WIDTH/2-blue.getWidth()/2), 0);
                attachToScene(type, scene);

            case 1:
            case 2:
            case 3:
            case 4:
        }
    }

    private void createRoundObjects(int type[], int value[], int startX, int startY) {
        mValue = value;
        roundObjects = new RoundObject[type.length];
        roundObjects[0] = new RoundObject(value[0], startX, startY, getTexture(type[0]),font, vertexBufferObjectManager);
        for (int i=1; i < type.length; i++) {
            if (type[i] == ORANGE) roundObjects[i] = new RoundObject(value[i], startX, startY - i * (roundObjects[0].getHeight()+10), getTexture(type[i]), font, vertexBufferObjectManager);
            if (type[i] == BLUE) roundObjects[i] = new RoundObject(startX, startY - i * (roundObjects[0].getHeight()+10), getTexture(type[i]), font, vertexBufferObjectManager);
        }

        roundObjects[roundObjects.length-1].setPositionListener(positionChangedListener);
    }

    private TiledTextureRegion getTexture(int type) {
        if (type == BLUE) return blue;
        if (type == ORANGE) return orange;
        return null;
    }

    private void attachToScene(int type[], Scene scene) {
        for (int i=0; i<roundObjects.length; i++) {
            scene.attachChild(roundObjects[i]);
            if (type[i] == BLUE) scene.registerTouchArea(roundObjects[i]);
        }
    }

    public boolean isCorrectAnswer() {
        for (int i=0; i<mValue.length; i++) {
            if (roundObjects[i].getValue() != mValue[i]) return false;
        }
        return true;
    }
}
