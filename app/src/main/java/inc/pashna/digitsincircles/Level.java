package inc.pashna.digitsincircles;

import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

/**
 * Created by Admin on 25.03.2015.
 */
public class Level {
    public static int BLUE = 1;
    public static int ORANGE = 2;

    private RoundObject[] roundObjects;

    private TiledTextureRegion blue, orange;

    public Level(int type[], int value[], int startX, int startY, VertexBufferObjectManager vertexBufferObjectManager, TiledTextureRegion blue, TiledTextureRegion orange, Font font) {
        roundObjects = new RoundObject[value.length];
        this.blue = blue;
        this.orange = orange;

        roundObjects[0] = new RoundObject(value[0], startX, startY, getTexture(type[0]),font, vertexBufferObjectManager);
        for (int i=0; i < value.length; i++) {
            roundObjects[i] = new RoundObject(value[i], startX, startY - i * (roundObjects[0].getHeight()+10), getTexture(type[i]), font, vertexBufferObjectManager);
        }
    }

    private TiledTextureRegion getTexture(int type) {
        if (type == BLUE) return blue;
        if (type == ORANGE) return orange;
        return null;
    }




}
