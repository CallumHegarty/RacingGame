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

    //Mouse position variables
    public int mouseX, mouseY;

    //timer variables
    public long startTime;
    public long currentTime;
    public long elapsedTime;

    public boolean startTimer;

    public Car car;
    public Obstacle moose;

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
        car = new Car("car",(int)((Math.random()*100)+140), 3, 70, 45);

        moosePic = Toolkit.getDefaultToolkit().getImage("Moose-PNG-Image-File.png"); //load the picture
        moose = new Obstacle("moose", (int)((Math.random()*10)+300), 200, 1, 1, 70,50);

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
    }

    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();

        if (keyCode == 39) {
            car.right = false;
        }
        if (keyCode == 37) {
            car.left = false;
        }
    }


    public void mouseClicked(MouseEvent e) {

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

        //g.drawString(elapsedTime+"",70,340);

        g.drawImage(backgroundPic, 0, 0, WIDTH, HEIGHT, null);

        g.drawImage(carPic, car.xpos, car.ypos, car.width, car.height, null);

        g.drawImage(moosePic, moose.xpos, moose.ypos, moose.width, moose.height, null);

        g.dispose();
        bufferStrategy.show();
    }

    public void run() {
        while (true) {
            //get the current time
            currentTime = System.currentTimeMillis();

            //calculate the elapsed time
            elapsedTime = currentTime-startTime;

            crash();
            moveThings();
            render();
            //System.out.println(car.xpos);
            pause(10); // sleep for 10 ms
        }
    }

    public void moveThings() {
        car.move();
        moose.move();
    }

    public void crash(){
        if(car.rec.intersects(moose.rec)){
            car.dx = 0;
            System.out.println("CRASH!!");
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
