import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.HashSet;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;

public class GraphicsObject {

    private static final int maxLayers = 10;

    private static final int canvasX = 1280;
    private static final int canvasY = 720;
    private static final HashSet<Sprite> sprites = new HashSet<>();

    public static void draw() {
        for (int i = 0; i < maxLayers; i++) {
            for (Sprite sp : sprites) {
                if (sp.getLayer() == i) {
                    drawSprite(sp);
                }
            }
        }
    }
    private static void drawSprite(Sprite sp){
        float calculatedX = sp.getX()*2/(float)canvasX-1;
        float calculatedY = sp.getY()*2/(float)canvasY-1;
        float calculatedWidth = sp.getWidth()*2/(float)canvasX;
        float calculatedHeight = sp.getHeight()*2/(float)canvasY;


        glBindTexture(GL_TEXTURE_2D, sp.getTexture());
        glBegin(GL_QUADS);

        glTexCoord2f(0,1);
        glVertex2d(calculatedX,calculatedY);

        glTexCoord2f(1,1);
        glVertex2d(calculatedX+calculatedWidth,calculatedY);

        glTexCoord2f(1,0);
        glVertex2d(calculatedX+calculatedWidth, calculatedY+calculatedHeight);

        glTexCoord2f(0,0);
        glVertex2d(calculatedX, calculatedY+calculatedHeight);

        glEnd();

    }

    public static int toTexture(BufferedImage img) {
        int[] pixels = new int[img.getWidth() * img.getHeight()];
        img.getRGB(0, 0, img.getWidth(), img.getHeight(), pixels, 0, img.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(img.getWidth() * img.getHeight() * 4);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = pixels[y * img.getWidth() + x];
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF)); //Credit to user Krythic for these values
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, img.getWidth(), img.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
        return texture;
    }
    public static void deleteTexture(int texture){
        glDeleteTextures(texture);
    }
    public static void addSprite(Sprite sp){
        sprites.add(sp);
    }
    public static void deleteSprite(Sprite sp){
        sprites.remove(sp);
    }
}