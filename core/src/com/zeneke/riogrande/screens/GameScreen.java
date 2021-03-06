package com.zeneke.riogrande.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zeneke.riogrande.Borde;
import com.zeneke.riogrande.utils.Constants;

import java.util.Iterator;

public class GameScreen implements Screen
{

    private boolean SOUND = false;

    private Texture piedraImage;
    private Texture barcaImage;
    private Texture fondoRioImage;
    private Texture game_bg;
    private Texture menu_bg;
    private Texture troncoImage;
    private Texture troncoImageFA;
    private Texture borde1Image;
    private Texture borde2Image;
    private Texture borde3Image;
    private Texture borde4Image;
    private Texture bordeLeftImage;
    private OrthographicCamera cameraGame;
    private OrthographicCamera cameraHUD;
    private SpriteBatch batch;
    private Rectangle barca;
    private Sound piedraSound;
    private Sound troncoSound;
    private Music rioMusic;

    private int anchoPiedra;
    private int altoPiedra;
    private Array<Rectangle> piedras;
    private long lastPiedraTime;

    private int anchoTronco;
    private int altoTronco;
    private Array<Rectangle> troncos;
    private long lastTroncoTime;


    private Array<Borde> bordes_derecha;
    private Rectangle lastBorde;

    private Array<Borde> bordes_izquierda;

    private int bob_max = 30;
    private int bob_min = -30;
    private int bob_pos = 0;
    private boolean bob_arriba = true;

    private Texture bordeParaPintar;

    private TextureAtlas rocaSpriteSheet;
    private Array<Sprite> skeleton;
    private float animationElapsed;
    private float frameLength = 0.5f;
    private int currentFrame = 0;

    private Viewport viewport;

    @Override
    public void show()
    {
        // load the images for the droplet and the bucket, 64x64 pixels each
        fondoRioImage = new Texture(Gdx.files.internal("rio_fondo_1.png"));
        piedraImage = new Texture(Gdx.files.internal("piedra_1_128.png"));
        barcaImage = new Texture(Gdx.files.internal("barca_1.png"));
        troncoImage = new Texture(Gdx.files.internal("tronco_1_128.png"));
        borde1Image = new Texture(Gdx.files.internal("sideborder_1.png"));
        borde2Image = new Texture(Gdx.files.internal("sideborder_2.png"));
        borde3Image = new Texture(Gdx.files.internal("sideborder_3.png"));
        borde4Image = new Texture(Gdx.files.internal("sideborder_4.png"));
        bordeLeftImage = new Texture(Gdx.files.internal("sideborder_left.png"));
        troncoImageFA = new Texture(Gdx.files.internal("tronco_FA.png"));

        anchoPiedra = piedraImage.getWidth();
        altoPiedra = piedraImage.getHeight();

        anchoTronco = troncoImage.getWidth();
        altoTronco = troncoImage.getHeight();

        cameraGame = new OrthographicCamera();
        cameraGame.position.x = Constants.VIEWPORT_WIDTH * 0.5f;
        cameraGame.position.y = Constants.VIEWPORT_HEIGHT * 0.5f;

        viewport = new FillViewport(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT, cameraGame);


       //cameraGame.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);

        batch = new SpriteBatch();

        barca = new Rectangle();
        barca.x = Constants.LEVEL_WIDTH / 2;
        barca.y = 800;
        //barca.width = 64;
        //barca.height = 64;
        barca.setSize(64,64);

        piedras = new Array<Rectangle>();
        spawnPiedra();
        troncos = new Array<Rectangle>();
        spawnTronco();
        bordes_derecha = new Array<Borde>();
        spawnBordeDerecho();
        bordes_izquierda = new Array<Borde>();
        spawnBordeIzquierdo();
        // load the drop sound effect and the rain background "music"
        piedraSound = Gdx.audio.newSound(Gdx.files.internal("sound/splash1.wav"));
        troncoSound = Gdx.audio.newSound(Gdx.files.internal("sound/splash2.wav"));
        rioMusic = Gdx.audio.newMusic(Gdx.files.internal("sound/stream1.mp3"));

        // start the playback of the background music immediately
        rioMusic.setLooping(true);
        if (SOUND)
            rioMusic.play();

        rocaSpriteSheet = new TextureAtlas("img/anim/roca.txt");


        skeleton = rocaSpriteSheet.createSprites("roca_2");

        //dont forget to set the size of your sprites!
        for(int i=0; i<skeleton.size; i++)
        {
            skeleton.get(i).setSize(1.0f, 1.0f);
        }

    }

    @Override
    public void render(float delta)
    {
        //update la logica del skeletor
        float dt = Gdx.graphics.getDeltaTime();
        animationElapsed += dt;
        while(animationElapsed > frameLength)
        {
            animationElapsed -= frameLength;
            currentFrame = (currentFrame == skeleton.size - 1) ? 0 : ++currentFrame;
        }

        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        cameraGame.update();
        Gdx.gl.glViewport(0,0 ,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        batch.setProjectionMatrix(cameraGame.combined);
        batch.begin();
        batch.draw(fondoRioImage, 0.0f, 0.0f);
        batch.draw(barcaImage, barca.x, barca.y, barca.getWidth(), barca.getHeight());
        for(Rectangle piedra: piedras)
        {
            batch.draw(piedraImage, piedra.x, piedra.y);
        }
        for(Rectangle tronco: troncos)
        {
            batch.draw(troncoImage, tronco.x, tronco.y);
        }
        for(Borde borde: bordes_derecha)
        {
            batch.draw(borde.getTexture(), borde.x, borde.y);
        }
        for(Borde borde: bordes_izquierda)
        {
            batch.draw(borde.getTexture(), borde.x, borde.y);
        }
        batch.draw(skeleton.get(currentFrame),0,0);
        batch.end();



        if(Gdx.input.isTouched())
        {
            Vector3 touchPos = new Vector3();
            touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            cameraGame.unproject(touchPos);
            barca.x = touchPos.x - 64 / 2;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
            barca.x -= 200 * Gdx.graphics.getDeltaTime();
        if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            barca.x += 200 * Gdx.graphics.getDeltaTime();

        if(barca.x < 0)
            barca.x = 0;
        if(barca.x > Constants.LEVEL_WIDTH - barca.getWidth())
        {
            barca.x = Constants.LEVEL_WIDTH - barca.getWidth();
        }

        if(TimeUtils.nanoTime()/1000000 - lastPiedraTime > 3000)
        {
            spawnPiedra();
        }
        if(TimeUtils.nanoTime()/1000000 - lastTroncoTime > 5000)
        {
            spawnTronco();
        }
        if (bordes_derecha.size != 0)
            if (bordes_derecha.peek().getY() > 0)
            {
                spawnBordeDerecho();
            }

        if (bordes_derecha.size != 0)
            if (bordes_izquierda.peek().getY() > 0)
            {
                spawnBordeIzquierdo();
            }



        Iterator<Rectangle> iter = piedras.iterator();
        while(iter.hasNext()) {
            Rectangle piedra = iter.next();
            piedra.y += 100 * Gdx.graphics.getDeltaTime();
            if(piedra.y + 64 < 0) iter.remove();
            if(piedra.overlaps(barca)) {
                if (SOUND)
                    piedraSound.play();
                iter.remove();
            }
        }

        Iterator<Rectangle> iterTroncos = troncos.iterator();
        while(iterTroncos.hasNext()) {
            Rectangle tronco = iterTroncos.next();
            tronco.y += 100 * Gdx.graphics.getDeltaTime();
            if(tronco.y + 64 < 0) iterTroncos.remove();
            if(tronco.overlaps(barca)) {
                if (SOUND)
                    troncoSound.play();
                iterTroncos.remove();
            }
        }

        Iterator<Borde> iterBordesDerecha = bordes_derecha.iterator();
        while(iterBordesDerecha.hasNext()) {
            Borde borde = iterBordesDerecha.next();
            if (borde.y < 0 )
                borde.y += Math.floor(100 * Gdx.graphics.getDeltaTime());
            else
                borde.y += 100 * Gdx.graphics.getDeltaTime();
            if(borde.y + 64 > 1024) iterBordesDerecha.remove();
        }

        Iterator<Borde> iterBordesIzquierda = bordes_izquierda.iterator();
        while(iterBordesIzquierda.hasNext()) {
            Borde borde = iterBordesIzquierda.next();
            if (borde.y < 0 )
                borde.y += Math.floor(100 * Gdx.graphics.getDeltaTime());
            else
                borde.y += 100 * Gdx.graphics.getDeltaTime();
            //if(borde.y + 64 > 1024) iterBordesIzquierda.remove();
        }

        if (bob_arriba)
        {
            barca.y += 40 * Gdx.graphics.getDeltaTime();
            bob_pos--;
        }
        if (bob_pos < bob_min)
            bob_arriba = false;
        if (!bob_arriba)
        {
            barca.y -= 40 * Gdx.graphics.getDeltaTime();
            bob_pos++;
        }
        if (bob_pos > bob_max)
            bob_arriba = true;

    }

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height);

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }

    private void spawnPiedra() {
        Rectangle piedra = new Rectangle();
        piedra.x = MathUtils.random(0, Constants.LEVEL_WIDTH - 128);
        piedra.y = 0;
        piedra.width = anchoPiedra;
        piedra.height = altoPiedra;
        piedras.add(piedra);
        lastPiedraTime = TimeUtils.nanoTime() / 1000000;
    }

    private void spawnTronco() {
        Rectangle tronco = new Rectangle();
        tronco.x = MathUtils.random(0, Constants.LEVEL_WIDTH - 128);
        tronco.y = 0;
        tronco.width = anchoTronco;
        tronco.height = altoTronco;
        troncos.add(tronco);
        lastTroncoTime = TimeUtils.nanoTime() / 1000000;
    }

    private void spawnBordeDerecho()
    {
        Borde borde = new Borde();
        int num = MathUtils.random(0,100);

        if (num < 15)
            borde.setTexture(borde2Image);
        else if (num < 30)
            borde.setTexture(borde3Image);
        else if ( num < 60)
            borde.setTexture(borde1Image);
        else
            borde.setTexture(borde4Image);

        bordes_derecha.add(borde);
    }

    private void spawnBordeIzquierdo()
    {
        Borde borde = new Borde(0,bordeLeftImage.getHeight());
        borde.setTexture(bordeLeftImage);
        bordes_izquierda.add(borde);
    }
}
