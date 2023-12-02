import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
public class Practice {
    private static long window;

    public static void main(String[] args) {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        window = glfwCreateWindow(1280, 720, "Test", NULL, NULL);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glViewport(0, 0, 1280,720);
        glfwShowWindow(window);

        AtomicBoolean userclosed = new AtomicBoolean(false);

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                userclosed.set(true);
            }
        });

        BufferedImage ken;
        try {
            ken = ImageIO.read(new File("C:/Users/David/Downloads/01-12-23_1329.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        int[] pixels = new int[ken.getWidth()*ken.getHeight()*4];
        ken.getRGB(0, 0, ken.getWidth(), ken.getHeight(), pixels, 0, ken.getWidth());
        ByteBuffer buffer = BufferUtils.createByteBuffer(ken.getWidth()*ken.getHeight()*4);

        for(int y = 0; y<ken.getHeight(); y++){
            for(int x = 0; x<ken.getWidth(); x++){
                int pixel = ken.getRGB(x, y);
                buffer.put((byte) ((pixel >> 16) & 0xFF));
                buffer.put((byte) ((pixel >> 8) & 0xFF)); //Credit to user Krythic for these values
                buffer.put((byte) (pixel & 0xFF));
                buffer.put((byte) ((pixel >> 24) & 0xFF));
            }
        }
        buffer.flip();
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB4, ken.getWidth(), ken.getHeight(), 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);


        while(!userclosed.get()){

            glClear(GL_COLOR_BUFFER_BIT);

            glBindTexture(texture, GL_TEXTURE_2D);
            glBegin(GL_QUADS);

            glTexCoord2f(0,0);
            glVertex2d(-0.5,-0.5);

            glTexCoord2f(1,0);
            glVertex2d(0.5,-0.5);

            glTexCoord2f(1,1);
            glVertex2d(0.5, 0.5);

            glTexCoord2f(0,1);
            glVertex2d(-0.5, 0.5);

            glEnd();
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
