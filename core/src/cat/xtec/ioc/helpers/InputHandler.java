package cat.xtec.ioc.helpers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import cat.xtec.ioc.objects.Spacecraft;
import cat.xtec.ioc.screens.GameScreen;
import cat.xtec.ioc.utils.Settings;

public class InputHandler implements InputProcessor {

    // Enter per a la gesitó del moviment d'arrastrar
    int previousY = 0;
    // Objectes necessaris
    private Spacecraft spacecraft;
    private GameScreen screen;
    private Vector2 stageCoord;
    private Vector2 stageTouch;

    private Stage stage;

    public InputHandler(GameScreen screen) {

        // Obtenim tots els elements necessaris
        this.screen = screen;
        spacecraft = screen.getSpacecraft();
        stage = screen.getStage();

    }

    @Override
    public boolean keyDown(int keycode) {

        if (keycode == Input.Keys.SPACE)
        {
            spacecraft.shoot();
            return true;
        }
        return false;

    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        switch (screen.getCurrentState()) {

            case READY:
                // Si fem clic comencem el joc
                screen.setCurrentState(GameScreen.GameState.RUNNING);
                break;
            case RUNNING:

                if(screenX <= Settings.GAME_WIDTH*0.4){
                    previousY = screenY;

                    stageCoord = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                    Actor actorHit = stage.hit(stageCoord.x, stageCoord.y, true);
                    if (actorHit != null)
                        Gdx.app.log("HIT", actorHit.getName());
                }else {

                    stageTouch = stage.screenToStageCoordinates(new Vector2(screenX, screenY));
                    spacecraft.shoot(stageTouch);

                }
                break;
            // Si l'estat és GameOver tornem a iniciar el joc
            case GAMEOVER:
                screen.reset();
                break;
        }

        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        // Quan deixem anar el dit acabem un moviment
        // i posem la nau en l'estat normal
        spacecraft.goStraight();
        return true;
    }


    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // Posem un umbral per evitar gestionar events quan el dit està quiet
        if (Math.abs(previousY - screenY) > 2 )

            if(screenX < Settings.GAME_WIDTH*0.4) {
                // Si la Y és major que la que tenim
                // guardada és que va cap avall
                if (previousY < screenY) {
                    spacecraft.goDown();
                } else {
                    // En cas contrari cap a dalt
                    spacecraft.goUp();
                }
            }
        // Guardem la posició de la Y
        previousY = screenY;
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
