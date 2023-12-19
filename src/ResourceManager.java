import org.lwjgl.system.CallbackI;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ResourceManager {
    //just the loading of all my textures
    public static int RED_GEM;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                RED_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/red_low.png")));
            }
            else{
                RED_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/red.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int GREEN_GEM;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                GREEN_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/green_low.png")));
            }
            else{
                GREEN_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/green.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int BLUE_GEM;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                BLUE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/blue_low.png")));
            }
            else{
                BLUE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/blue.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int ORANGE_GEM;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                ORANGE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/orange_low.png")));
            }
            else{
                ORANGE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/orange.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int YELLOW_GEM;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                YELLOW_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/yellow_low.png")));
            }
            else{
                YELLOW_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/yellow.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int PURPLE_GEM;
    static {
        try {
            if(Practice.moreOptimizedTextures){
                PURPLE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/purple_low.png")));
            }
            else{
                PURPLE_GEM = GraphicsObject.toTexture(ImageIO.read(new File("./res/purple.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int FLAME_EFFECT;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                FLAME_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/flame_low.png")));
            }
            else{
                FLAME_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/flame.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int LIGHTNING_EFFECT;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                LIGHTNING_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/lightning_low.png")));
            }
            else{
                LIGHTNING_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/lightning.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int EXPLOSION_EFFECT;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                EXPLOSION_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/explosion_low.png")));
            }
            else{
                EXPLOSION_EFFECT = GraphicsObject.toTexture(ImageIO.read(new File("./res/explosion.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int ZAP_CENTER;

    static {
        try {

            if(Practice.moreOptimizedTextures){
                ZAP_CENTER = GraphicsObject.toTexture(ImageIO.read(new File("./res/zapCenter_low.png")));
            }
            else{
                ZAP_CENTER = GraphicsObject.toTexture(ImageIO.read(new File("./res/zapCenter.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int ZAP_HORIZONTAL;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                ZAP_HORIZONTAL = GraphicsObject.toTexture(ImageIO.read(new File("./res/zapHorizontal_low.png")));
            }
            else{
                ZAP_HORIZONTAL = GraphicsObject.toTexture(ImageIO.read(new File("./res/zapHorizontal.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int ZAP_VERTICAL;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                ZAP_VERTICAL = GraphicsObject.toTexture(ImageIO.read(new File("./res/zapVertical_low.png")));
            }
            else{
                ZAP_VERTICAL = GraphicsObject.toTexture(ImageIO.read(new File("./res/zapVertical.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int BACKGROUND_IMAGE;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                BACKGROUND_IMAGE = GraphicsObject.toTexture(ImageIO.read(new File("./res/background_low.jpg")));
            }
            else{
                BACKGROUND_IMAGE = GraphicsObject.toTexture(ImageIO.read(new File("./res/background.jpg")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int BOARD_IMAGE;

    static {
        try {
            BOARD_IMAGE = GraphicsObject.toTexture(ImageIO.read(new File("./res/board.png")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
    public static int BOARD_FRAME;

    static {
        try {
            if(Practice.moreOptimizedTextures){
                BOARD_FRAME = GraphicsObject.toTexture(ImageIO.read(new File("./res/frame_low.png")));
            }
            else{
                BOARD_FRAME = GraphicsObject.toTexture(ImageIO.read(new File("./res/frame.png")));
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    };
}
