import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;

public class BasicGameApp implements MouseListener, MouseMotionListener, KeyListener, Runnable {

    //screen size
    final int WIDTH = 700;
    final int HEIGHT = 400;

    //variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public BufferStrategy bufferStrategy;

    //game variables
    public long elapsedTime;
    public long score;
    public boolean gameStart = false;
    public boolean gameOver;
    //highScore = 5939

    //characters
    public Car car;
    public Obstacle[] herd;

    //images
    public Image carPic;
    public Image moosePic;
    public Image backgroundPic;

    //sound effects
    public SoundFile crashSound;
    public SoundFile engineSlow;
    public SoundFile engineFast;


    //main method definition
    public static void main(String[] args) {
        BasicGameApp myApp = new BasicGameApp();   //creates a new instance of the app
        new Thread(myApp).start();  //start up the thread
    }

    //basic information about game
    public BasicGameApp() {
        setUpGraphics();
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);

        carPic = Toolkit.getDefaultToolkit().getImage("Back of Ferrari.png"); //load the picture
        car = new Car("car",(int)((Math.random()*100)+140), 4, 55, 35);

        moosePic = Toolkit.getDefaultToolkit().getImage("Moose-PNG-Image-File.png"); //load the picture

        herd = new Obstacle[4];
        for(int x=0;x<herd.length;x=x+1){
            herd[x] = new Obstacle("herd "+x, (int)((Math.random()*150)+200), (int)((Math.random()*150)+100), (double)((Math.random()*2)), (double)((Math.random()*2)), 60,40);
        }

        backgroundPic = Toolkit.getDefaultToolkit().getImage("Road Background.gif");

        crashSound = new SoundFile("Metal Crash 01.wav");
        engineSlow = new SoundFile("Car Engine Run Loop 02.wav");
        engineFast = new SoundFile("Car Engine Run Loop 01.wav");
    }

    //sets up graphics
    private void setUpGraphics() {
        frame = new JFrame("Application Template");   //Create the program window or frame.  Names it.

        JPanel panel = (JPanel) frame.getContentPane();  //sets up a JPanel which is what goes in the frame
        panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));  //sizes the JPanel
        panel.setLayout(null);   //set the layout

        // creates a canvas which is a blank rectangular area of the screen onto which the application can draw
        // and trap input events (Mouse and Keyboard events)
        canvas = new Canvas();
        canvas.setBounds(0, 0, WIDTH, HEIGHT);
        canvas.setIgnoreRepaint(true);

        panel.add(canvas);  // adds the canvas to the panel.


        // frame operations
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  //makes the frame close and exit nicely
        frame.pack();  //adjusts the frame and its contents so the sizes are at their default or larger
        frame.setResizable(false);   //makes it so the frame cannot be resized
        frame.setVisible(true);      //IMPORTANT!!!  if the frame is not set to visible it will not appear on the screen!

        // sets up things so the screen displays images nicely.
        canvas.createBufferStrategy(2);
        bufferStrategy = canvas.getBufferStrategy();
        canvas.requestFocus();
    }

    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        //arrow keys move car side to side
        if (keyCode == 39) {
            car.right = true;
        }
        if (keyCode == 37) {
            car.left = true;
        }

        //space bar moves car up
        if (keyCode == 32) {
            car.up = true;
            car.down = false;
            engineFast.loop();
            engineSlow.stop();
        }

        //"enter" key starts and restarts game
        if (keyCode == 10) {
            gameStart = true;
            engineSlow.loop();
            if(gameOver == true){
                restart();
            }
        }
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        //stops moving car is key is lifted
        if (keyCode == 39) {
            car.right = false;
        }
        if (keyCode == 37) {
            car.left = false;
        }

        //car moves down by default unless specifically told to move up
        if (keyCode == 32) {
            car.up = false;
            car.down = true;
            engineSlow.loop();
            engineFast.stop();
        }
    }

    //unused necessary methods
    public void keyTyped(KeyEvent e) {
    }
    public void mouseClicked(MouseEvent e) {
        int x, y;
        x = e.getX();
        y = e.getY();
        System.out.println(x+", "+y);
    }
    public void mousePressed(MouseEvent e) {

    }
    public void mouseReleased(MouseEvent e) {

    }
    public void mouseEntered(MouseEvent e) {

    }
    public void mouseExited(MouseEvent e) {

    }
    public void mouseDragged(MouseEvent e) {

    }
    public void mouseMoved(MouseEvent e) {

    }

    //renders images
    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        //start screen
        if(gameStart == false){
            g.setColor(Color.RED);
            g.fillRect(0,0,WIDTH,HEIGHT);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Comic Sans", Font.BOLD, 25));
            g.drawString("Press Enter",300,200);
        }

        //restart screen
        if((gameOver == true)&&(gameStart == true)){
            g.setColor(Color.RED);
            g.fillRect(0,0,WIDTH,HEIGHT);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Comic Sans", Font.BOLD, 25));
            g.drawString("Press Enter",300,200);
            g.drawString("Your Score Was: "+score,250,300);
        }

        //actual game running
        else if(gameStart == true) {

            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.BOLD, 25));
            g.drawString(elapsedTime + "", 300, 100);

            g.drawImage(carPic, car.xpos, car.ypos, car.width, car.height, null);

            for (int x = 0; x < herd.length; x = x + 1) {
                g.drawImage(moosePic, (int) herd[x].xpos, (int) herd[x].ypos, herd[x].width, herd[x].height, null);
            }
        }

        g.dispose();
        bufferStrategy.show();
    }

    //runs game
    public void run() {
        while (true) {

            if(gameStart == true) {
                crash();
                moveThings();

                //calculate the elapsed time/score
                if (gameOver == false) {
                    elapsedTime = elapsedTime + 1;
                }
            }
            render();
            pause(10);
        }
    }

    //lets objects move
    public void moveThings() {
        car.move();
        if (gameOver == false) {
            for (int x = 0; x < herd.length; x = x + 1) {
                herd[x].move();
            }
        }
    }

    //handles object interactions
    public void crash(){
        for(int i=0;i<herd.length;i++){
            if(herd[i].rec.intersects(car.rec)){
                herd[i].xpos = (i+1)*125;
                herd[i].ypos = 200;
                herd[i].move();
                crashSound.play();

                if(elapsedTime>1) {
                    score = elapsedTime;
                }
                gameOver = true;
            }
        }
    }

    //resets game to original state after "enter" key is pressed
    public void restart(){
        elapsedTime = 0;
        gameOver = false;
        gameStart = true;

        for(int x=0;x<herd.length;x=x+1){
            herd[x].xpos = (int)((Math.random()*150)+200);
            herd[x].ypos = (int)((Math.random()*150)+100);
            herd[x].dx = (double)((Math.random()*2));
            herd[x].dy = (double)((Math.random()*2));
        }
        car.xpos = (int)((Math.random()*100)+140);

    }

    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }
}