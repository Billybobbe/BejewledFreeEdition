import java.util.ArrayList;

public class GameHandler {
    private ArrayList<Sprite>gemsDrawn;
    private Board board;
    private Gem[][] boardArr;

    private static final int shiftX = 640;
    private static final int shiftY = 10;
    private static final int space = 5;
    private static final int sizeX = 70;
    private static final int sizeY = 70;
    private int boxXSelected = -1;
    private int boxYSelected = -1;

    public GameHandler(Board b){
        board = b;
        boardArr = board.getBoard();
        gemsDrawn = new ArrayList<>();

    }
    public void update(){
        while(gemsDrawn.size()>0){
            Sprite sp = gemsDrawn.get(0);
            gemsDrawn.remove(sp);
            GraphicsObject.deleteSprite(sp);
        }
        for(int x = 0; x< boardArr.length; x++){
            for(int y = 0; y< boardArr[0].length; y++){
                Gem g = boardArr[x][y];
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
                    if(g.size<1){
                        g.size -= 0.01;
                        if(g.size<=0){
                            boardArr[x][y] = null;
                        }
                    }

                    if(y>7){
                        Sprite sp = genSprite(g, x, y);
                        gemsDrawn.add(sp);
                        GraphicsObject.addSprite(sp);

                        int[] coords = transformToCoords(x, y, 0, 0, 1);
                        int xPos = coords[0];
                        int yPos = coords[1];
                        int sizeX = coords[2];
                        int sizeY = coords[3];
                        Sprite effect = new Sprite(xPos, yPos, sizeX, sizeY, 0, 2);
                        switch(g.charge){
                            case F:
                                effect.changeTexture(ResourceManager.FLAME_EFFECT);
                                gemsDrawn.add(effect);
                                GraphicsObject.addSprite(effect);
                                break;
                            case L:
                                effect.changeTexture(ResourceManager.LIGHTNING_EFFECT);
                                gemsDrawn.add(effect);
                                GraphicsObject.addSprite(effect);
                        }
                    }
                }
            }
        }
    }

    private static int[] transformToCoords(int x, int y, float xOffset, float yOffset, float sizeMultiplier){


        int transformedX = (int)(shiftX + (space+sizeX)*(x+xOffset-sizeMultiplier/2f+1/2f));
        int transformedY = (int)(shiftY + (space+sizeY)*(8-y+8+yOffset-sizeMultiplier/2f+1/2f));
        return new int[]{transformedX, transformedY, (int)(sizeX*sizeMultiplier), (int)(sizeY*sizeMultiplier)};
    }
    private Sprite genSprite(Gem g, int x, int y){
        int[] coords = transformToCoords(x, y, g.shiftX, g.shiftY, g.size);
        switch(g.type){
            case R:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.RED_GEM);
            case G:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.GREEN_GEM);
            case B:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.BLUE_GEM);
            case O:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.ORANGE_GEM);
            case Y:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.YELLOW_GEM);
            default:
                return new Sprite(coords[0], coords[1], coords[2], coords[3], ResourceManager.YELLOW_GEM);
        }
    }
    public void selectBox(){
        int mouseX = Practice.getMouseX();
        int mouseY = Practice.getMouseY();
        mouseX -= shiftX;
        mouseY -= shiftY;
        mouseX /= (space+sizeX);
        mouseY /= (space + sizeY);
        mouseY = 16-mouseY;
        boxXSelected = mouseX;
        boxYSelected = mouseY;
        System.out.println(boxXSelected + ", " + boxYSelected);
    }
    public void move(int direction){
        //left right up down
        selectBox();
        if(boxXSelected >= 0 && boxXSelected < boardArr.length && boxYSelected >= 8 && boxYSelected < boardArr[0].length && Math.abs(boardArr[boxXSelected][boxYSelected].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected][boxYSelected].shiftY)<=0.1){
            switch(direction){
                case 0:
                    if(boxXSelected>0){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected-1][boxYSelected];
                        boardArr[boxXSelected-1][boxYSelected] = temp;

                        boardArr[boxXSelected-1][boxYSelected].shiftX += 1;
                        boardArr[boxXSelected][boxYSelected].shiftX -= 1;
                    }
                    break;
                case 1:
                    if(boxXSelected< boardArr.length-1) {
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected + 1][boxYSelected];
                        boardArr[boxXSelected + 1][boxYSelected] = temp;

                        boardArr[boxXSelected + 1][boxYSelected].shiftX -= 1;
                        boardArr[boxXSelected][boxYSelected].shiftX += 1;
                    }
                        break;
                case 2:
                    if(boxYSelected>8){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected][boxYSelected-1];
                        boardArr[boxXSelected][boxYSelected-1] = temp;

                        boardArr[boxXSelected][boxYSelected-1].shiftY -= 1;
                        boardArr[boxXSelected][boxYSelected].shiftY += 1;
                    }
                        break;
                default:
                    if(boxYSelected< boardArr[0].length-1){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected][boxYSelected+1];
                        boardArr[boxXSelected][boxYSelected+1] = temp;

                        boardArr[boxXSelected][boxYSelected+1].shiftY += 1;
                        boardArr[boxXSelected][boxYSelected].shiftY -= 1;
                    }
                    break;
            }
        }
    }
}
