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
           ken = ImageIO.read(new File("./res/01-12-23_1329.jpg"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for(int i = 0; i<10; i++){
            for(int r =0; r<10; r++){
                GraphicsObject.addSprite(new Sprite(i*128,r*72,128,72, ken));
            }
        }

        while(!userclosed.get()){

            glClear(GL_COLOR_BUFFER_BIT);
            GraphicsObject.draw();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
