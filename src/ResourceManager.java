import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ResourceManager {

    public static int RED_GEM;

    static {
        try {
            RED_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/red.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int GREEN_GEM;

    static {
        try {
            GREEN_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/green.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int BLUE_GEM;

    static {
        try {
            BLUE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/blue.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int ORANGE_GEM;

    static {
        try {
            ORANGE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/orange.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int YELLOW_GEM;

    static {
        try {
            YELLOW_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/yellow.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int FLAME_EFFECT;

    static {
        try {
            FLAME_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/flame.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int LIGHTNING_EFFECT;

    static {
        try {
            LIGHTNING_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/lightning.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int BACKGROUND_IMAGE;

    static {
        try {
            BACKGROUND_IMAGE = GraphicsObject.toTexture(ImageIO.read(new File("./res/background.jpg")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
}
