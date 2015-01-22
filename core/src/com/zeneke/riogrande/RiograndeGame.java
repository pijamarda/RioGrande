package com.zeneke.riogrande;

import com.badlogic.gdx.Game;
import com.zeneke.riogrande.screens.GameScreen;

public class RiograndeGame extends Game
{
    @Override
    public void create ()
    {
        setScreen(new GameScreen());
    }


}
