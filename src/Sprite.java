import java.awt.image.BufferedImage;

public class Sprite {
    private int x;
    private int y;
    private int width;
    private int height;
    private int texture;

    public Sprite(int x, int y, int width, int height, int texture){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    public int getTexture(){
        return texture;
    }
    public void changeTexture(int texture){
        this.texture = texture;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }
    public int getHeight(){
        return height;
    }

}
