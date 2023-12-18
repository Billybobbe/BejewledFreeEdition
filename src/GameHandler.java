import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

public class GameHandler {
    private ArrayList<Sprite>gemsDrawn;
    private ArrayList<SlideGem>slideGemList;
    private Board board;
    private Gem[][] boardArr;

    private static final int shiftX = 640;
    private static final int shiftY = 10;
    private static final int space = 5;
    private static final int sizeX = 70;
    private static final int sizeY = 70;
    private int boxXSelected = -1;
    private int boxYSelected = -1;
    private static float slideSpeed = 0.02f;
    private static float shrinkSpeed = 0.02f;

    public class SlideGem{
        protected int initialX;
        protected int initialY;
        protected float currentX;
        protected float currentY;
        protected float speed;
        protected int destX;
        protected int destY;
        protected Gem.Type type;
        public SlideGem(int x, int y, int destX, int destY, Gem.Type t){
            this.type = t;
            this.initialX = x;
            this.initialY = y;
            this.currentX = x;
            this.currentY = y;
            this.destX = destX;
            this.destY = destY;
            this.speed = Math.abs(currentX-destX)+Math.abs(currentY-destY);
        }
    }



    public GameHandler(Board b){
        board = b;
        b.setGameHandler(this);
        boardArr = board.getBoard();
        gemsDrawn = new ArrayList<>();
        slideGemList = new ArrayList<>();

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
                if(g!=null && g.behavior != Gem.Behavior.SLIDING){
                    if(g.shiftX>0.01){
                        g.shiftX -= 0.05f;
                        if(Math.abs(g.shiftX)<=0.01){
                            board.upgradeGem(x, y);
                            board.upgradeGems();
                        }
                    }
                    if(g.shiftX<-0.01){
                        g.shiftX += 0.05f;
                        if(Math.abs(g.shiftX)<=0.01){
                            board.upgradeGem(x, y);
                            board.upgradeGems();
                        }
                    }
                    if(g.shiftY>0.01){
                        g.shiftY -= 0.05f;
                        if(Math.abs(g.shiftY)<=0.01){
                            board.upgradeGem(x, y);
                            board.upgradeGems();
                        }
                    }
                    if(g.shiftY<-0.01){
                        g.shiftY += 0.05f;
                        if(Math.abs(g.shiftY)<=0.01){
                            board.upgradeGem(x, y);
                            board.upgradeGems();
                        }
                    }
                    if(g.behavior== Gem.Behavior.SHRINKING){
                        g.size -= shrinkSpeed;
                        if(g.size<=0){
                            boardArr[x][y] = null;
                        }
                    }

                    if(y>7){
                        Sprite sp = genSprite(g, x, y);
                        gemsDrawn.add(sp);
                        GraphicsObject.addSprite(sp);

                        int[] coords = transformToCoords(x, y, g.shiftX, g.shiftY, g.size);
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
        };
        for(int i = 0; i<slideGemList.size(); i++){
            SlideGem g = slideGemList.get(i);
            if(g.currentX>g.destX){
                g.currentX -= slideSpeed*g.speed;
            }
            if(g.currentX<g.destX){
                g.currentX += slideSpeed*g.speed;
            }
            if(g.currentY>g.destY){
                g.currentY -= slideSpeed*g.speed;
            }
            if(g.currentY<g.destY){
                g.currentY += slideSpeed*g.speed;
            }

            if(Math.abs(g.currentX-g.destX)<0.05 && Math.abs(g.currentY-g.destY)<0.05){
                slideGemList.remove(g);
                boardArr[g.initialX][g.initialY] = null;
                i--;
            }
            int[] coords = transformToCoords(g.currentX, g.currentY, 0, 0, 1);
            Sprite sp = new Sprite(coords[0], coords[1], coords[2], coords[3], getTypeTexture(g.type));
            gemsDrawn.add(sp);
            GraphicsObject.addSprite(sp);
        }
    }

    private static int[] transformToCoords(float x, float y, float xOffset, float yOffset, float sizeMultiplier){


        int transformedX = (int)(shiftX + (space+sizeX)*(x+xOffset-sizeMultiplier/2f+1/2f));
        int transformedY = (int)(shiftY + (space+sizeY)*(8-y+8+yOffset-sizeMultiplier/2f+1/2f));
        return new int[]{transformedX, transformedY, (int)(sizeX*sizeMultiplier), (int)(sizeY*sizeMultiplier)};
    }
    private Sprite genSprite(Gem g, int x, int y){
        int[] coords = transformToCoords(x, y, g.shiftX, g.shiftY, g.size);
        return new Sprite(coords[0], coords[1], coords[2], coords[3], getTypeTexture(g.type));
    }
    private int getTypeTexture(Gem.Type t){
        switch(t){
            case R:
                return ResourceManager.RED_GEM;
            case G:
                return ResourceManager.GREEN_GEM;
            case B:
                return ResourceManager.BLUE_GEM;
            case O:
                return ResourceManager.ORANGE_GEM;
            case Y:
                return ResourceManager.YELLOW_GEM;
            default:
                return ResourceManager.PURPLE_GEM;
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
        if(boxXSelected >= 0 && boxXSelected < boardArr.length && boxYSelected >= 8 && boxYSelected < boardArr[0].length && Math.abs(boardArr[boxXSelected][boxYSelected].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected][boxYSelected].shiftY)<=0.1 && boardArr[boxXSelected][boxYSelected].behavior == Gem.Behavior.NOTHING){
            switch(direction){
                case 0:
                    if(boxXSelected>0 && Math.abs(boardArr[boxXSelected-1][boxYSelected].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected-1][boxYSelected].shiftY)<=0.1 && boardArr[boxXSelected-1][boxYSelected].behavior == Gem.Behavior.NOTHING){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected-1][boxYSelected];
                        boardArr[boxXSelected-1][boxYSelected] = temp;

                        boardArr[boxXSelected-1][boxYSelected].shiftX += 1;
                        boardArr[boxXSelected][boxYSelected].shiftX -= 1;
                    }
                    break;
                case 1:
                    if(boxXSelected< boardArr.length-1 && Math.abs(boardArr[boxXSelected+1][boxYSelected].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected+1][boxYSelected].shiftY)<=0.1 && boardArr[boxXSelected+1][boxYSelected].behavior == Gem.Behavior.NOTHING) {
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected + 1][boxYSelected];
                        boardArr[boxXSelected + 1][boxYSelected] = temp;

                        boardArr[boxXSelected + 1][boxYSelected].shiftX -= 1;
                        boardArr[boxXSelected][boxYSelected].shiftX += 1;
                    }
                        break;
                case 2:
                    if(boxYSelected>8 && Math.abs(boardArr[boxXSelected][boxYSelected-1].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected][boxYSelected-1].shiftY)<=0.1 && boardArr[boxXSelected][boxYSelected-1].behavior == Gem.Behavior.NOTHING){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected][boxYSelected-1];
                        boardArr[boxXSelected][boxYSelected-1] = temp;

                        boardArr[boxXSelected][boxYSelected-1].shiftY -= 1;
                        boardArr[boxXSelected][boxYSelected].shiftY += 1;
                    }
                        break;
                default:
                    if(boxYSelected< boardArr[0].length-1 && Math.abs(boardArr[boxXSelected][boxYSelected+1].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected][boxYSelected+1].shiftY)<=0.1 && boardArr[boxXSelected][boxYSelected+1].behavior == Gem.Behavior.NOTHING){
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
    public void addSlideGem(int x, int y, int destX, int destY, Gem.Type t){
        slideGemList.add(new SlideGem(x, y, destX, destY, t));
    }
}
