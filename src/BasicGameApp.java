import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

//Keyboard and Mouse
import java.awt.event.*;

public class BasicGameApp implements MouseListener, MouseMotionListener, KeyListener, Runnable {

    final int WIDTH = 700;
    final int HEIGHT = 400;

    //Declare the variables needed for the graphics
    public JFrame frame;
    public Canvas canvas;
    public BufferStrategy bufferStrategy;

    //timer variables
    public long startTime;
    public long currentTime;
    public long elapsedTime;
    public int highScore = 3012;
    public boolean gameStart = false;

    public boolean gameOver;

    public Car car;
    public Obstacle moose;
    public Obstacle moose2;
    public Obstacle[] herd;

    public Image carPic;
    public Image moosePic;
    public Image backgroundPic;


    // Main method definition
    // This is the code that runs first and automatically
    public static void main(String[] args) {
        BasicGameApp myApp = new BasicGameApp();   //creates a new instance of the app
        new Thread(myApp).start();  //start up the thread
    }

    public BasicGameApp() {
        setUpGraphics();
        canvas.addMouseListener(this);
        canvas.addMouseMotionListener(this);
        canvas.addKeyListener(this);

        carPic = Toolkit.getDefaultToolkit().getImage("Back of Ferrari.png"); //load the picture
        car = new Car("car",(int)((Math.random()*100)+140), 4, 55, 35);

        moosePic = Toolkit.getDefaultToolkit().getImage("Moose-PNG-Image-File.png"); //load the picture
        moose = new Obstacle("moose", 90, 200, 1, 1, 9,6);

        //moose2 = new Obstacle("moose2", (int)((Math.random()*50)+300), 300, 2, 1, 60,40);

        herd = new Obstacle[4];
        for(int x=0;x<herd.length;x=x+1){
            herd[x] = new Obstacle("herd "+x, (int)((Math.random()*150)+200), (int)((Math.random()*150)+100), (double)((Math.random()*2)), (double)((Math.random()*2)), 60,40);
        }
        backgroundPic = Toolkit.getDefaultToolkit().getImage("Road Background.gif");

    }

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


    public void keyTyped(KeyEvent e) {

    }


    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 39) {
            car.right = true;
        }
        if (keyCode == 37) {
            car.left = true;
        }
        if (keyCode == 32) {
            car.up = true;
            car.down = false;
        }

        if (keyCode == 10) {
            gameStart = true;
        }
    }


    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 39) {
            car.right = false;
        }
        if (keyCode == 37) {
            car.left = false;
        }
        if (keyCode == 32) {
            car.up = false;
            car.down = true;
        }
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

    private void render() {
        Graphics2D g = (Graphics2D) bufferStrategy.getDrawGraphics();
        g.clearRect(0, 0, WIDTH, HEIGHT);

        if((gameStart == false)||(gameOver == true)){
            g.setColor(Color.CYAN);
            g.fillRect(0,0,WIDTH,HEIGHT);
            g.setColor(Color.BLACK);
            g.setFont(new Font("Comic Sans", Font.BOLD, 25));
            g.drawString("Press Enter",300,200);
            //g.drawImage(startScreenPic, 0,0, WIDTH, HEIGHT, null);
        }
        else {
            g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

            g.setColor(Color.BLACK);
            g.setFont(new Font("TimesRoman", Font.BOLD, 25));
            g.drawString(elapsedTime + "", 300, 100);

            g.drawImage(carPic, car.xpos, car.ypos, car.width, car.height, null);

            //g.drawImage(moosePic, (int) moose.xpos, (int) moose.ypos, moose.width, moose.height, null);

            //g.drawImage(moosePic, moose2.xpos, moose2.ypos, moose2.width, moose2.height, null);

            for (int x = 0; x < herd.length; x = x + 1) {
                g.drawImage(moosePic, (int) herd[x].xpos, (int) herd[x].ypos, herd[x].width, herd[x].height, null);
            }
        }

        g.dispose();
        bufferStrategy.show();
    }

    public void run() {
        while (true) {
            //get the current time
            currentTime = System.currentTimeMillis();

            //calculate the elapsed time
            //elapsedTime = currentTime-startTime;
            if(gameStart == true) {
                if (gameOver == false) {
                    elapsedTime = elapsedTime + 1;
                }

                crash();
                moveThings();
            }
            render();
            pause(10);
        }
    }

    public void moveThings() {
            car.move();
            if (gameOver == true) {
                for (int x = 0; x < herd.length; x = x + 1) {
                    herd[x].moveOver();
                }
            } else {
                for (int x = 0; x < herd.length; x = x + 1) {
                    herd[x].move();
                }
            }

    }

    public void crash(){
        for(int i=0;i<herd.length;i++){
            if(herd[i].rec.intersects(car.rec)){
                herd[i].xpos = (i+1)*125;
                herd[i].ypos = 200;
                herd[i].dx = 0;
                herd[i].dy = 0;
                System.out.println("CRASH!!");
                System.out.println("Score: " + elapsedTime);
                if(elapsedTime > highScore){
                    System.out.println("New High Score!");
                }
                gameOver = true;
            }
        }
    }

    public void pause(int time) {
        //sleep
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {

        }
    }


}
