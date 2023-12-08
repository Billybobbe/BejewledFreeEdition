import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
public class Practice {
    public static BufferedImage ken;
    private static long window;

    private static double mouseX;
    private static double mouseY;
    private static Board b;

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

        AtomicBoolean userClosed = new AtomicBoolean(false);

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                userClosed.set(true);
            }
            if(key == GLFW_KEY_1 && action == GLFW_RELEASE){
                b.randomDelete();
            }
        });



        b  = new Board();
        b.print();
        GameHandler g = new GameHandler(b);

        while(!userClosed.get()){

            glClear(GL_COLOR_BUFFER_BIT);
            GraphicsObject.draw();

            glfwSwapBuffers(window);
            glfwPollEvents();
            g.update();
            b.update();
            b.print();
            getMousey(window);
        }
    }
    public static void getMousey(long w){
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(w, x, y);
        mouseX = x[0];
        mouseY = 720-y[0];
    }
}
