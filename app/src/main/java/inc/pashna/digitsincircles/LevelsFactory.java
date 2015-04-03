package inc.pashna.digitsincircles;

import org.andengine.entity.Entity;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
    private MyActivity myActivity;
    private Text instruction;
    private boolean flagToDemoLevelFirstTap = true;

    private static final ScheduledExecutorService worker = Executors.newSingleThreadScheduledExecutor();


    void appearEntrityWithDelay(final Entity entity, long delay) {
        Runnable task = new Runnable() {
            public void run() {
                myActivity.setAppearence(entity, 1f, 0);
                stopAllRound();
            }
        };
        worker.schedule(task, delay, TimeUnit.SECONDS);
    }

    public LevelsFactory(TiledTextureRegion blue, TiledTextureRegion orange, Font font, VertexBufferObjectManager vertexBufferObjectManager, OnPositionChangedListener listener) {
        this.blue = blue;
        this.orange = orange;
        this.font = font;
        this.vertexBufferObjectManager = vertexBufferObjectManager;
        this.positionChangedListener = listener;
        this.myActivity = (MyActivity) listener;

    }

    public void generateLevel(int number, Scene scene) {

        switch (number) {
            case 0: {
                flagToDemoLevelFirstTap = true;
                int type[] = {ORANGE, ORANGE, BLUE, ORANGE, ORANGE};
                int value[] = {2, 2, 2, 2, 2};
                createRoundObjectsForDemoLevel(type, value, (int) (MyActivity.CAMERA_WIDTH / 2 - blue.getWidth() / 2), (int) (-blue.getHeight() * 1.5));
                attachToScene(type, scene);

                instruction = myActivity.createText("Tap on blue round \n to increase the number");
                instruction.setPosition(MyActivity.CAMERA_WIDTH/2-instruction.getWidth()/2, MyActivity.CAMERA_HEIGHT-instruction.getHeight()*2);
                instruction.setHorizontalAlign(HorizontalAlign.CENTER);
                instruction.setAlpha(0);

                //myActivity.setAppearence(instruction, 1f, 0);
                myActivity.mainScene.attachChild(instruction);
                appearEntrityWithDelay(instruction, 3);

                /*myActivity.mainScene.register(new TimerHandler(3.65f, true, new ITimerCallback() {
                    @Override
                    public void onTimePassed(final TimerHandler pTimerHandler) {
                        stopAllRound();
/*                        instruction = myActivity.createText("Tap on blue round \n to increase the number");
                        instruction.setPosition(MyActivity.CAMERA_WIDTH/2-instruction.getWidth()/2, MyActivity.CAMERA_HEIGHT-instruction.getHeight()*2);
                        instruction.setHorizontalAlign(HorizontalAlign.CENTER);
                        myActivity.setAppearence(instruction, 1, 0);
                        myActivity.mainScene.attachChild(instruction);
                    }
                }));
                */
                break;
            }

            case 1: {
                int type[] = {ORANGE, ORANGE, BLUE, ORANGE, ORANGE};
                int value[] = {1, 2, 3, 2, 1};
                createRoundObjects(type, value, (int) (MyActivity.CAMERA_WIDTH / 2 - blue.getWidth() / 2), (int) (-blue.getHeight() * 1.5));
                attachToScene(type, scene);
                break;
            }

            case 2: {
                int type[] = {ORANGE, ORANGE, ORANGE, BLUE, ORANGE};
                int value[] = {2, 4, 6, 8, 10};
                createRoundObjects(type, value, (int) (MyActivity.CAMERA_WIDTH / 2 - blue.getWidth() / 2), (int) (-blue.getHeight() * 1.5));
                attachToScene(type, scene);
                break;
            }

            case 3: {
                int type[] = {ORANGE, ORANGE, ORANGE, ORANGE, BLUE, ORANGE};
                int value[] = {2, 3, 5, 7, 11, 13};
                createRoundObjects(type, value, (int) (MyActivity.CAMERA_WIDTH / 2 - blue.getWidth() / 2), (int) (-blue.getHeight() * 1.5));
                attachToScene(type, scene);
                break;
            }

            case 4:
            case 5:
        }
    }

    /*
    Создает игровые объекты
     */
    private void createRoundObjects(int type[], int value[], int startX, int startY) {
        mValue = value;
        roundObjects = new RoundObject[type.length];
        roundObjects[0] = new RoundObject(value[0], startX, startY, getTexture(type[0]),font, vertexBufferObjectManager);
        for (int i=1; i < type.length; i++) {
            if (type[i] == ORANGE) roundObjects[i] = new RoundObject(value[i], startX, startY - i * (roundObjects[0].getHeight()+10), getTexture(type[i]), font, vertexBufferObjectManager);
            if (type[i] == BLUE) roundObjects[i] = new RoundObject(startX, startY - i * (roundObjects[0].getHeight()+10), getTexture(type[i]), font, vertexBufferObjectManager);
        }
        // Последнему присваиваем лисенер-activity, чтобы следить, когда закончился уровень
        roundObjects[roundObjects.length-1].setPositionListener(positionChangedListener);
    }

    /*

     */
    private void createRoundObjectsForDemoLevel(int type[], int value[], int startX, int startY) {
        mValue = value;
        roundObjects = new RoundObject[type.length];
        roundObjects[0] = new RoundObject(value[0], startX, startY, getTexture(type[0]),font, vertexBufferObjectManager);
        for (int i=1; i < type.length; i++) {
            if (type[i] == ORANGE) roundObjects[i] = new RoundObject(value[i], startX, startY - i * (roundObjects[0].getHeight()+10), getTexture(type[i]), font, vertexBufferObjectManager);
            if (type[i] == BLUE)  {
                final LevelsFactory levelsFactory = this;
                roundObjects[i] = new RoundObject(startX, startY - i * (roundObjects[0].getHeight()+10), getTexture(type[i]), font, vertexBufferObjectManager) {
                    @Override
                    public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
                        if (flagToDemoLevelFirstTap) {
                            flagToDemoLevelFirstTap = false;
                            //myActivity.setAppearence(instruction, 0, 1);
                            //myActivity.mainScene.detachChild(instruction);

                            //Text enjoyText =

                            instruction.setText("Solve the sequence\nand enjoy!");
                            instruction.setPosition(MyActivity.CAMERA_WIDTH/2-instruction.getWidth()/2, MyActivity.CAMERA_HEIGHT-instruction.getHeight()*2);
                            instruction.setHorizontalAlign(HorizontalAlign.CENTER);
                            myActivity.setAppearence(instruction, 0f, 1f);
                            levelsFactory.setVelovity(0, 200);

                        /*myActivity.mainScene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
                            @Override
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                instruction.setText("Solve the sequence\nand enjoy!");
                                instruction.setPosition(MyActivity.CAMERA_WIDTH/2-instruction.getWidth()/2, MyActivity.CAMERA_HEIGHT-instruction.getHeight()*2);
                                instruction.setHorizontalAlign(HorizontalAlign.CENTER);
                                myActivity.setAppearence(instruction, 1, 1);
                            }
                        }));
                        */
                        /*
                        myActivity.mainScene.registerUpdateHandler(new TimerHandler(2.5f, true, new ITimerCallback() {
                            @Override
                            public void onTimePassed(final TimerHandler pTimerHandler) {
                                setVelovity(0, roundObjects[0].getVelocityY());
                            }
                        }));
                        */
                        }

                        return super.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
                    }
                };
            }
        }
        // Последнему присваиваем лисенер-activity, чтобы следить, когда закончился уровень
        roundObjects[roundObjects.length-1].setPositionListener(positionChangedListener);
    }

    /*
    Возвращает текстуру по типу
     */
    private TiledTextureRegion getTexture(int type) {
        if (type == BLUE) return blue;
        if (type == ORANGE) return orange;
        return null;
    }

    /*
    Прикрепляет все объекты к сцене
    */
    private void attachToScene(int type[], Scene scene) {
        for (int i=0; i<roundObjects.length; i++) {
            scene.attachChild(roundObjects[i]);
            if (type[i] == BLUE) scene.registerTouchArea(roundObjects[i]);
        }
    }

    /*
    Анализирует результат игры
     */
    public boolean isCorrectAnswer() {
        for (int i=0; i<mValue.length; i++) {
            Debug.d(roundObjects[i].getValue() + "!=" + mValue[i]);
            if (roundObjects[i].getValue() != mValue[i]) return false;
        }
        return true;
    }

    public void stopAllRound() {
        for (int i=0; i<mValue.length; i++)
            roundObjects[i].setVelocity(0,0);
    }

    public void setVelovity(float Vx, float Vy) {
        for (int i=0; i<mValue.length; i++)
            roundObjects[i].setVelocity(Vx,Vy);
    }
}
