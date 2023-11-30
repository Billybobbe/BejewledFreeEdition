import org.lwjgl.opengl.*;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.*;
public class Practice {
    private static long window;

    public static void main(String[] args) {
        glfwInit();
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
        window = glfwCreateWindow(1280, 720, "Test", NULL, NULL);
        glfwMakeContextCurrent(window);
        GL.createCapabilities();
        glViewport(0, 0, 1280,720);
        glfwShowWindow(window);
        glClearColor(0, 0, 0, 0);
        glDrawPixels(10, 10, 0, 0, 1);

        AtomicBoolean userclosed = new AtomicBoolean(false);

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE){
                userclosed.set(true);
            }
        });
        while(!userclosed.get()){
            glClear(GL_COLOR_BUFFER_BIT);
            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }
}
