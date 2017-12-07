package com.prajjwalgupta.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {
	  private SpriteBatch batch;
  private	Texture background;
    private  Texture gameOver;
    private Animation animation;
 //   ShapeRenderer shapeRenderer ;
 private	Texture[] birds;
    private Texture bottomTube,topTube;
    private  int flapState=0;
    private   float birdY=0;
    private float velocity=0;
    private    int gamestate=0;
    private BitmapFont font;
    private  int scoringTube=0;
    private float gravity=2;
    private  int score=0;
    private float gap=400;
    private Random randomgenerator;
    private float maxOffset;
    private    float tubeVelocity=4;
    private int numberOfTubes=4;
    private float[] tubeX=new float[numberOfTubes];
    private  float distanceBetweenTubes;
    private float[] tubeOffset=new float[numberOfTubes];
    private Circle birdCircle;
    private Rectangle[] toptuberectangles;
    private Rectangle[] bottomtuberectangles;


    @Override
	public void create () {
		batch = new SpriteBatch();
		background=new Texture("bg.png");
        birds=new Texture[2];
        birds[0]=new Texture("bird.png");
        birds[1]=new Texture("bird2.png");
        birdCircle=new Circle();
        font=new BitmapFont();
        toptuberectangles=new Rectangle[numberOfTubes];
        bottomtuberectangles=new Rectangle[numberOfTubes];
        bottomTube=new Texture("bottomtube.png");
        topTube=new Texture("toptube.png");
      //  shapeRenderer =new ShapeRenderer();
        gameOver=new Texture("gameover.png");
        font.setColor(Color.WHITE);
        font.getData().setScale(10);
      //  maxOffset=Gdx.graphics.getHeight()/2 -gap/2-100;
        randomgenerator=new Random();
        distanceBetweenTubes=Gdx.graphics.getWidth() * 3/4;

             startGame();

		}



		public void  startGame()
        {

            birdY=Gdx.graphics.getHeight() /2 +
                    birds[0].getHeight() /2;

            for(int i=0;i<numberOfTubes;i++)
            {

                tubeOffset[i]=(randomgenerator.nextFloat() -0.5f)*(Gdx.graphics.getHeight() -gap-500);
                tubeX[i]=Gdx.graphics.getWidth()/2-topTube.getWidth()/2 + Gdx.graphics.getWidth()+i*distanceBetweenTubes;
                toptuberectangles[i]=new Rectangle();
                bottomtuberectangles[i]=new Rectangle();
            }


        }

	@Override
	public void render () {
        batch.begin();
        batch.draw(background,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        if(gamestate==1) {

            if(tubeX[scoringTube] <  Gdx.graphics.getWidth()/2 )
            {
                score++;
                Gdx.app.log("Score",String.valueOf(score));

                if(scoringTube<numberOfTubes-1)
                {
                    scoringTube++;
                }
                else {
                    scoringTube=0;
                }
            }

            if (Gdx.input.justTouched()) {
                velocity=-30;


            }
            for(int i=0;i<numberOfTubes;i++) {

                if(tubeX[i]< - topTube.getWidth())
                {
                    tubeX[i]+=numberOfTubes*distanceBetweenTubes;
                    tubeOffset[i]=(randomgenerator.nextFloat() -0.5f)*(Gdx.graphics.getHeight() -gap-500);

                }
                else {
                    tubeX[i] -= tubeVelocity;
                }

                batch.draw(topTube, tubeX[i], Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i]);
                batch.draw(bottomTube, tubeX[i],
                        Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]);

                 toptuberectangles[i]=new Rectangle(tubeX[i],
                         Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());

                bottomtuberectangles[i]=new Rectangle(tubeX[i],
                        Gdx.graphics.getHeight() / 2 - gap / 2 - bottomTube.getHeight() + tubeOffset[i]
                ,bottomTube.getWidth(),bottomTube.getHeight());

            }

             if(birdY>0  ) {
                velocity += gravity;
                birdY -= velocity;
            }
            else
             {
                 gamestate=2;

             }
        }
         else if (gamestate==0) {
            if (Gdx.input.justTouched()) {

                    gamestate=1;

            }
        }
        else if(gamestate==2)
        {

            batch.draw(gameOver,Gdx.graphics.getWidth()/2 -gameOver.getWidth()/2,
                    Gdx.graphics.getHeight()/2+gameOver.getHeight()/2);
              if(Gdx.input.justTouched()) {
                  gamestate=1;
                  startGame();
                  score = 0;
                  scoringTube = 0;
                  velocity = 0;
              }

        }
        if(flapState==0)
        {
            flapState=1;
        }
        else  {
            flapState=0;
        }


        batch.draw(birds[flapState],Gdx.graphics.getWidth() /2 -birds[flapState].getWidth() /2,birdY);
        font.draw(batch,String.valueOf(score),100,200);
        batch.end();
     //   shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
     //   shapeRenderer.setColor(Color.RED);
         birdCircle.set(Gdx.graphics.getWidth()/2,birdY+birds[flapState].getHeight()/2,birds[flapState].getWidth()/2);

       //  shapeRenderer.circle(birdCircle.x,birdCircle.y,birdCircle.radius);

        for(int i=0;i<numberOfTubes;i++) {

       //     shapeRenderer.rect(tubeX[i],
         //           Gdx.graphics.getHeight() / 2 + gap / 2 + tubeOffset[i],topTube.getWidth(),topTube.getHeight());

          //  shapeRenderer.rect(tubeX[i], Gdx.graphics.getHeight() / 2 - gap / 2 -bottomTube.getHeight() + tubeOffset[i]
          //        ,bottomTube.getWidth(),bottomTube.getHeight()
             //       );


            if((Intersector.overlaps(birdCircle,toptuberectangles[i])) || (Intersector.overlaps(birdCircle,
                    bottomtuberectangles[i])))
            {
              //  Gdx.app.log("Collison","Yes");

                gamestate=2;


            }

        }
          //  shapeRenderer.end();
	}

	@Override
	public void dispose () {
		batch.dispose();
	}
}
