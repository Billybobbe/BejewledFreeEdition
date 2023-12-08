import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ResourceManager {

    public static int RED_GEM;

    static {
        try {
            RED_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/red.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int GREEN_GEM;

    static {
        try {
            GREEN_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/green.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int BLUE_GEM;

    static {
        try {
            BLUE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/blue.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int ORANGE_GEM;

    static {
        try {
            ORANGE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/orange.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int YELLOW_GEM;

    static {
        try {
            YELLOW_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/yellow.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
}
