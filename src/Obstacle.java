import java.awt.*;
public class Obstacle {

    public String name;
    public int xpos;
    public int ypos;
    public int dx;
    public int dy;
    public int width;
    public int height;
    public Rectangle rec;

    public Obstacle(String pName, int pXpos, int pYpos, int pDx, int pDy, int pWidth, int pHeight) {
        name = pName;
        xpos = pXpos;
        ypos = pYpos;
        dx = pDx;
        dy = pDy;
        width = pWidth;
        height = pHeight;
        rec = new Rectangle(xpos,ypos,width,height);
    }

    public void move(){
        xpos = xpos + dx;
        ypos = ypos + dy;

        if (xpos >= 550-width) {
            dx = -dx;
        }
        if (xpos <= 120) {
            dx = -dx;
        }
        if(ypos > 400 && dy > 0) {
            ypos = 160;
        }

        rec = new Rectangle(xpos,ypos,width,height);
    }

    public void moveOver(){
        xpos = xpos + dx;
        ypos = ypos + dy;

        rec = new Rectangle(xpos,ypos,width,height);
    }

}
