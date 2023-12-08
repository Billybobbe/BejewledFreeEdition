import java.util.ArrayList;
import java.util.HashMap;

public class GameHandler {
    private ArrayList<Sprite>gemsDrawn;
    private Gem[][] board;

    public GameHandler(Board b){
        board = b.getBoard();
        gemsDrawn = new ArrayList<>();

    }
    public void update(){
        for(Sprite sp : gemsDrawn){
            GraphicsObject.deleteSprite(sp);
        }
        for(int x = 0; x<board.length; x++){
            for(int y = 1; y<board.length; y++){
                Gem g = board[x][y];
                if(g!=null){
                    if(g.shiftX>0.01){
                        g.shiftX -= 0.05;
                    }
                    if(g.shiftX<0.01){
                        g.shiftX += 0.05;
                    }
                    if(g.shiftY>0.01){
                        g.shiftY -= 0.05;
                    }
                    if(g.shiftY<0.01){
                        g.shiftY += 0.05;
                    }

                    Sprite sp = genSprite(g, x, y);
                    gemsDrawn.add(sp);
                    GraphicsObject.addSprite(sp);
                }
            }
        }
    }

    private static int[] transformToCoords(int x, int y, float xOffset, float yOffset){
        int shiftX = 640;
        int shiftY = 10;
        int space = 5;
        int sizeX = 70;
        int sizeY = 70;

        int transformedX = (int)(shiftX + (space+sizeX)*(x+xOffset));
        int transformedY = (int)(shiftY + (space+sizeY)*(8-y+yOffset));
        return new int[]{transformedX, transformedY, sizeX, sizeY};
    }
    private Sprite genSprite(Gem g, int x, int y){
        int[] coords = transformToCoords(x, y, g.shiftX, g.shiftY);
        switch(g.type){
            case R:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.RED_GEM);
            case G:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.GREEN_GEM);
            case B:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.BLUE_GEM);
            case O:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.ORANGE_GEM);
            default:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.YELLOW_GEM);
        }
    }
}
