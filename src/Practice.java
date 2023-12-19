import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;
import org.lwjgl.system.CallbackI;

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
    private static GameHandler g;

    public static final boolean moreOptimizedTextures = true;


    public static void main(String[] args) {
        //Opengl initialization
        //stuff like passing in window params, establishing capabilities, setting some parameters, etc.
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 5);
        window = glfwCreateWindow(1280, 720, "Test", NULL, NULL);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glEnable(GL_TEXTURE_2D);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glViewport(0, 0, 1280,720);
        glfwShowWindow(window);

        AtomicBoolean userClosed = new AtomicBoolean(false);

        //key listeners for gem movement
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                userClosed.set(true);
            }
            if(key == GLFW_KEY_1 && action == GLFW_RELEASE){
                g.setChargelightning();
            }
            if(key == GLFW_KEY_A && action == GLFW_RELEASE){
                g.move(0);
            }
            if(key == GLFW_KEY_D && action == GLFW_RELEASE){
                g.move(1);
            }
            if(key == GLFW_KEY_W && action == GLFW_RELEASE){
                g.move(2);
            }
            if(key == GLFW_KEY_S && action == GLFW_RELEASE){
                g.move(3);
            }
        });


        //creating the board and gamehandler for the game
        b  = new Board();
        b.print();
        g = new GameHandler(b);

        //adding background, frame, and checkerboard
        GraphicsObject.addSprite(new Sprite(0, 0, 1280, 720, ResourceManager.BACKGROUND_IMAGE, 0)); //background image
        GraphicsObject.addSprite(new Sprite(640, 85, 600, 600, ResourceManager.BOARD_IMAGE, 1));
        GraphicsObject.addSprite(new Sprite(609, 57, 659, 657, ResourceManager.BOARD_FRAME,1));

        double oldSysTime = System.nanoTime()*0.000000001;

        while(!userClosed.get()){
            //glClear(GL_COLOR_BUFFER_BIT);
            GraphicsObject.draw();

            //update inputs, mouse, and framebuffer.
            glfwSwapBuffers(window);
            glfwPollEvents();
            updateMousey(window);

            //every 10th or 100th of a second (can't remember which) it does the board physics
            if(System.nanoTime()*0.000000001-oldSysTime>=0.01){
                g.update();
                b.update();
                oldSysTime = System.nanoTime()*0.000000001;
            }
        }
    }

    //just for updating the position of the mouse
    private static void updateMousey(long w){
        double[] x = new double[1];
        double[] y = new double[1];
        glfwGetCursorPos(w, x, y);
        mouseX = x[0];
        mouseY = 720-y[0];
    }
    public static int getMouseX(){
        return (int)mouseX;
    }
    public static int getMouseY(){
        return (int)mouseY;
    }
}
