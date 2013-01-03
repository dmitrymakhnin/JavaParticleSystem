import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.util.*;

class CustomBall
{
    public double x;
    public double y;
    public double dx;
    public double dy;
    public Color ballColor;

    public CustomBall(int game_width, int game_height)
    {
        x = Math.random() * game_width;
        y = Math.random() * game_height;
        dx = (Math.random() * 1000) - 500;
        dy = (Math.random() * 1000) - 500;
        ballColor = new Color((int)(Math.random() * 0xFF), (int)(Math.random() * 0xFF), (int)(Math.random() * 0xFF));
    }
}

class GamePanel extends JPanel implements Runnable
{
    public static int GAME_WIDTH = 800;
    public static int GAME_HEIGHT = 500;
    public static final Color BALL_COLOR = Color.RED;

    private float posx;
    private float posy;
    private double dx = 25;   // x_pixels_per_second
    private double dy = 25;   // y_pixels_per_second
    private double ballx;
    private double bally;
    private int ball_radius = 10;
    private int game_fps = 30;
    private int nballs = 1000; 
    int shape = 0; // 0 = ovals 1 = rectangles

    ArrayList<CustomBall> balls = new ArrayList<CustomBall>();

    BufferedImage image = null;
  
    public GamePanel()
    {
        setPreferredSize(new Dimension(GAME_WIDTH, GAME_HEIGHT));
        System.out.println("CALLED" + GAME_WIDTH + ", " + GAME_HEIGHT);
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                directParticles(evt.getX(), evt.getY());
            }
        });
        setFocusable(true);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                switch (evt.getKeyCode()) {
                case (KeyEvent.VK_1):
                    reverseParticles();
                    break;
                case (KeyEvent.VK_ESCAPE):
                    System.exit(0);
                    break;
                case (KeyEvent.VK_2):
                    randomizeParticles();
                    break;
                case (KeyEvent.VK_3):
                    repositionParticles();
                    break;
                case (KeyEvent.VK_4):
                    toggleShape();
                    break;
                case (KeyEvent.VK_5):
                    offsetRadius(-1);
                    break;
                case (KeyEvent.VK_6):
                    offsetRadius(1);
                    break;
                };
            }
        });
    }

    public void offsetRadius(int offset)
    {
        System.out.println("offsetRadius called.");
        ball_radius += offset;
    }

    public void toggleShape()
    {
        System.out.println("toggleShape called.");
        if (shape == 0) {
            shape = 1;
        } else {
            shape = 0;
        }     
    }

    public void repositionParticles()
    {
        System.out.println("repositionParticles called.");
        for (CustomBall ball : balls) 
        {
            ball.x = Math.random() * getWidth();
            ball.y = Math.random() * getHeight();
        }        
    }

    public void randomizeParticles()
    {
        System.out.println("randomizeParticles called.");
        for (CustomBall ball : balls) 
        {
            ball.dx = Math.random() * getWidth() - (getWidth() / 2);
            ball.dy = Math.random() * getHeight() - (getHeight() / 2);
        }        
    }

    public void reverseParticles()
    {
        System.out.println("reverseParticles called.");
        for (CustomBall ball : balls) 
        {
            ball.dx = -ball.dx;
            ball.dy = -ball.dy;
        }
    }

    public void directParticles(int x, int y){
        System.out.println("directParticles function called");
        for (CustomBall ball : balls) 
        {
            double vmag = Math.sqrt(Math.pow(ball.x + ball.dx, 2) + Math.pow(ball.y + ball.dy, 2)); 
            double cmag = Math.sqrt(Math.pow(x - ball.x, 2) + Math.pow(y - ball.y, 2));
            double vhat_new_x = (x - ball.x);
            double vhat_new_y = (y - ball.y);
            ball.dx = vhat_new_x / cmag * vmag;
            ball.dy = vhat_new_y / cmag * vmag;
        }
    }

    public void run()
    {
        gameInit();

        boolean running = true;
        double t1 = System.nanoTime(); 
        double t2;
        double dt;
        
        System.out.println("A: " + getWidth() + ", " + "B " + getHeight());
        GAME_WIDTH = getWidth();
        GAME_HEIGHT = getHeight();

        while (running)
        {
             
            t2 = System.nanoTime();
            dt = (t2 - t1)/1000000000;
            double balldx;
            double balldy;
            for (CustomBall ball : balls) 
            {
                balldx = ball.dx * dt;
                balldy = ball.dy * dt;
                boolean hiteast = balldx + ball.x + ball_radius > GAME_WIDTH;
                boolean hitwest = balldx + ball.x < 0;
                boolean hitnorth = balldy + ball.y < 0;
                boolean hitsouth = balldy + ball.y + ball_radius > GAME_HEIGHT;
                boolean collided = hiteast || hitwest || hitnorth || hitsouth;

                if (hiteast) {
                    ball.x = GAME_WIDTH - ball_radius;
                    ball.dx *= -0.1;
                }
                if (hitwest) {
                    ball.x = 0;
                    ball.dx *= -0.25;
                }
                if (hitnorth) {
                    ball.y = 0;
                    ball.dy *= -0.25;
                }
                if (hitsouth) {
                    ball.y = GAME_HEIGHT - ball_radius;
                    ball.dy *= -0.1 ;
                }
                if (!collided) {
                    ball.x = ball.x + ball.dx * dt;
                    ball.y = ball.y + ball.dy * dt;
                    if (ball.dx < 150) {
                        ball.dx = ball.dx * (1 + Math.random()/20);
                    } else {
                        ball.dx = ball.dx;
                    }
                    if (ball.dy < 150) {
                        ball.dy = ball.dy * (1 + Math.random()/20);
                    } else {
                        ball.dy = ball.dy;
                    }

                }
            }           

            gameRender();
            repaint();
            try {
                t1 = System.nanoTime();
                Thread.sleep(1000 / game_fps);
            } catch (Exception e) {
                
            }
        }
    }
   
    public void gameStart()
    {
        new Thread(this).run();
    }

    private void gameInit() 
    {
        GAME_WIDTH = getWidth();
        GAME_HEIGHT = getHeight();
        ballx = 200;
        bally = 200;
        for (int i = 0; i < nballs; ++i)
        {
            balls.add(new CustomBall(getWidth(), getHeight()));
        }
    }

    public void gameRender()
    {    
        if (image == null) {
            GAME_WIDTH = getWidth();
            GAME_HEIGHT = getHeight();
            image = new BufferedImage(GAME_WIDTH, GAME_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
        Graphics g = image.getGraphics();      
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(BALL_COLOR);
        g.fillOval((int)ballx, (int)bally, ball_radius, ball_radius);
 
        for (CustomBall ball : balls)
        {
            g.setColor(ball.ballColor);
            if (shape == 0) {
                g.fillOval((int)ball.x, (int)ball.y, ball_radius, ball_radius);
            } else if (shape == 1) {
                g.fillRect((int)ball.x, (int)ball.y, ball_radius, ball_radius);
            }
        }
    }

    public void paint(Graphics g)
    {
        g.drawImage(image, 0, 0, null);
    }
}

public class Main extends JFrame
{
    public Main()
    {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();        
        GraphicsDevice dg = ge.getDefaultScreenDevice();        

        setTitle("Bouncing Balls.");
        setUndecorated(true);
        GamePanel p = new GamePanel();
        add(p);
        pack();
        setLocationRelativeTo(null);
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent evt) {
                if (evt.getKeyCode() == KeyEvent.VK_ESCAPE){
                    System.exit(0);
                }
            }
        });   
        setVisible(true);  
        dg.setFullScreenWindow(this);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        p.gameStart();
    }

    public static void main(String[] args)
    {
        new Main();
    }
}