import java.awt.*;
public class Car {

    public String name;
    public int xpos;
    public int ypos;
    public int dx;
    public int dy = 2;
    public int width;
    public int height;
    public Rectangle rec;

    public boolean right;
    public boolean left;
    public boolean down;
    public boolean up;

    public Car(String pName, int pXpos, int pDx, int pWidth, int pHeight) {
        name = pName;
        xpos = pXpos;
        ypos = 400-(pHeight*2);
        dx = pDx;
        width = pWidth;
        height = pHeight;
        rec = new Rectangle(xpos,ypos,width,height);

    }

    public void move(){
        if (right) {
            xpos = xpos + dx;
            if (xpos > 560 - width) {
                xpos = 560 - width;
            }
        }

        if (left) {
            xpos = xpos - dx;
            if (xpos < 110) {
                xpos = 110;
            }
        }

        if (up) {
            ypos = ypos - dy;
            if (ypos < 300) {
                ypos = 300;
            }
        }

        if (down) {
            ypos = ypos + dy;
            if (ypos > 380 - height) {
                ypos = 380 - height;
            }
        }

        rec = new Rectangle(xpos,ypos,width,height);
    }

}
