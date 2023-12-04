import java.awt.image.BufferedImage;

public class Sprite {
    private int x;
    private int y;
    private int width;
    private int height;
    private int texture;
    public Sprite(int x, int y, int width, int height, BufferedImage img){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = GraphicsObject.toTexture(img);
    }
    public void deleteTexture(){
        GraphicsObject.deleteTexture(texture);
        texture = -1;
    }
    public int getTexture(){
        return texture;
    }
    public void changeTexture(BufferedImage img){
        deleteTexture();
        texture = GraphicsObject.toTexture(img);
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
