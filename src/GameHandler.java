import org.lwjgl.system.CallbackI;

import java.util.ArrayList;

public class GameHandler {
    private ArrayList<Sprite>spritesDrawn;
    private ArrayList<SlideGem>slideGemList;
    private ArrayList<ExplosionPoint>explosions;
    private ArrayList<ZapPoint>zaps;
    private Board board;
    private Gem[][] boardArr;

    //sizing and position info for the board and gems
    private static final int shiftX = 640;
    private static final int shiftY = 10;
    private static final int space = 5;
    private static final int sizeX = 70;
    private static final int sizeY = 70;

    //what box the mouse is hovering over
    private int boxXSelected = -1;
    private int boxYSelected = -1;
    private static float slideSpeed = 0.02f;
    private static float shrinkSpeed = 0.02f;

    //some classes for the specific object types needed in some of my arraylists

    public static class SlideGem{
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
    public static class ExplosionPoint{
        protected int x;
        protected int y;
        protected float size = 0;
        protected int maxSize = 5;
        private static final float speed = 0.1f;
        public ExplosionPoint(int x, int y){
            this.x = x;
            this.y = y;
        }
        public void update(){
            size += speed;
        }
    }
    public static class ZapPoint{
        protected enum Place{
            C, H, V
        }
        protected int x;
        protected int y;
        protected int timer = 100;
        protected Place place;


        public ZapPoint(int x, int y, Place place){
            this.x = x;
            this.y = y;
            this.place = place;
        }
        public void update() {
            timer--;
        }
    }


//pass in board upon creation, initialize all the arraylists
    public GameHandler(Board b){
        board = b;
        b.setGameHandler(this);
        boardArr = board.getBoard();
        spritesDrawn = new ArrayList<>();
        slideGemList = new ArrayList<>();
        explosions = new ArrayList<>();
        zaps = new ArrayList<>();

    }
    //go through each of the arraylists, add the sprites in there to the graphicsobject
    //since the GraphicsObject keeps sprites they get removed and re-added every frame. (probably not the most efficient, I know)
    public void update(){
        //remove the old ones
        while(spritesDrawn.size()>0){
            Sprite sp = spritesDrawn.get(0);
            spritesDrawn.remove(sp);
            GraphicsObject.deleteSprite(sp);
        }
        //for all the gems on the board
        for(int x = 0; x< boardArr.length; x++){
            for(int y = 0; y< boardArr[0].length; y++){
                Gem g = boardArr[x][y];
                if(g!=null && g.behavior != Gem.Behavior.SLIDING){
                    //adjust all the shifts on them, reduce the magnitude
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
                    //if shrinking make the size smaller
                    if(g.behavior== Gem.Behavior.SHRINKING){
                        g.size -= shrinkSpeed;
                        if(g.size<=0){
                            boardArr[x][y] = null;
                        }
                    }

                    if(y>7){
                        //add gems as sprites and any effects they have based on their charge
                        Sprite sp = genSprite(g, x, y);
                        spritesDrawn.add(sp);
                        GraphicsObject.addSprite(sp);

                        int[] coords = transformToCoords(x, y, g.shiftX, g.shiftY, g.size);
                        int xPos = coords[0];
                        int yPos = coords[1];
                        int sizeX = coords[2];
                        int sizeY = coords[3];
                        Sprite effect = new Sprite(xPos, yPos, sizeX, sizeY, 0, 2);
                        //if they have a charge we add it as an effect
                        switch(g.charge){
                            case F:
                                effect.changeTexture(ResourceManager.FLAME_EFFECT);
                                spritesDrawn.add(effect);
                                GraphicsObject.addSprite(effect);
                                break;
                            case L:
                                effect.changeTexture(ResourceManager.LIGHTNING_EFFECT);
                                spritesDrawn.add(effect);
                                GraphicsObject.addSprite(effect);
                        }
                    }
                }
            }
        };
        //update the sliding gems, these slide into the gem being combined
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
            //if it's close enough to the destination we remove it
            if(Math.abs(g.currentX-g.destX)<0.05 && Math.abs(g.currentY-g.destY)<0.05){
                slideGemList.remove(g);
                boardArr[g.initialX][g.initialY] = null;
                i--;
            }
            //add the slidegems as sprites
            int[] coords = transformToCoords(g.currentX, g.currentY, 0, 0, 1);
            Sprite sp = new Sprite(coords[0], coords[1], coords[2], coords[3], getTypeTexture(g.type));
            spritesDrawn.add(sp);
            GraphicsObject.addSprite(sp);
        }
        //explosions grow in size until they get big, then remove
        for(int i = 0; i<explosions.size(); i++){
            ExplosionPoint e = explosions.get(i);
            e.update();
            if(e.size>= e.maxSize){
                explosions.remove(e);
                i--;
            }
            int[] coords = transformToCoords(e.x, e.y, 0, 0, e.size);
            Sprite sp  = new Sprite(coords[0],coords[1],coords[2],coords[3],ResourceManager.EXPLOSION_EFFECT, 4);
            spritesDrawn.add(sp);
            GraphicsObject.addSprite(sp);
        }
        //the zaps from the lightning gems just stay on screen for a set period of time
        //this gets diminished in the form of a counter
        for(int i = 0; i<zaps.size(); i++){
            ZapPoint z = zaps.get(i);
            z.update();
            if(z.timer<=0){
                zaps.remove(z);
                if(boardArr[z.x][z.y]!=null && boardArr[z.x][z.y].charge!= Gem.Charge.N){
                    board.removeGem(z.x, z.y);
                }
                else{
                    boardArr[z.x][z.y]=null;
                }
                i--;
            }
            int[] coords = transformToCoords(z.x, z.y, 0, 0, 1);
            Sprite sp = new Sprite(coords[0],coords[1],coords[2],coords[3],0);
            switch (z.place){
                case C:
                    sp.changeTexture(ResourceManager.ZAP_CENTER);
                    break;
                case H:
                    sp.changeTexture(ResourceManager.ZAP_HORIZONTAL);
                    break;
                case V:
                    sp.changeTexture(ResourceManager.ZAP_VERTICAL);
            }
            spritesDrawn.add(sp);
            GraphicsObject.addSprite(sp);
        }
    }
    //turns position on board into sprite coordinates
    private static int[] transformToCoords(float x, float y, float xOffset, float yOffset, float sizeMultiplier){


        int transformedX = (int)(shiftX + (space+sizeX)*(x+xOffset-sizeMultiplier/2f+1/2f));
        int transformedY = (int)(shiftY + (space+sizeY)*(8-y+8+yOffset-sizeMultiplier/2f+1/2f));
        return new int[]{transformedX, transformedY, (int)(sizeX*sizeMultiplier+5), (int)(sizeY*sizeMultiplier+5)}; //this makes it line up properly since there is spacing
    }
    private Sprite genSprite(Gem g, int x, int y){
        int[] coords = transformToCoords(x, y, g.shiftX, g.shiftY, g.size);
        return new Sprite(coords[0], coords[1], coords[2], coords[3], getTypeTexture(g.type));
    }
    //return texture of gem
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
        //movement logic, if it won't match afterwards it can't move
        //cases for what key is pressed, corresponds to the direction of movement.
        selectBox();
        if(boxXSelected >= 0 && boxXSelected < boardArr.length && boxYSelected >= 8 && boxYSelected < boardArr[0].length && boardArr[boxXSelected][boxYSelected]!=null && Math.abs(boardArr[boxXSelected][boxYSelected].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected][boxYSelected].shiftY)<=0.1 && boardArr[boxXSelected][boxYSelected].behavior == Gem.Behavior.NOTHING){
            switch(direction){
                case 0:
                    if(boxXSelected>0 && Math.abs(boardArr[boxXSelected-1][boxYSelected].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected-1][boxYSelected].shiftY)<=0.1 && boardArr[boxXSelected-1][boxYSelected].behavior == Gem.Behavior.NOTHING){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected-1][boxYSelected];
                        boardArr[boxXSelected-1][boxYSelected] = temp;
                        if(board.checkGemForMatches(boxXSelected, boxYSelected)||board.checkGemForMatches(boxXSelected-1, boxYSelected)){
                            boardArr[boxXSelected-1][boxYSelected].shiftX += 1;
                            boardArr[boxXSelected][boxYSelected].shiftX -= 1;
                        }
                        else{
                            temp = boardArr[boxXSelected][boxYSelected];
                            boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected-1][boxYSelected];
                            boardArr[boxXSelected-1][boxYSelected] = temp;
                        }

                    }
                    break;
                case 1:
                    if(boxXSelected< boardArr.length-1 && Math.abs(boardArr[boxXSelected+1][boxYSelected].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected+1][boxYSelected].shiftY)<=0.1 && boardArr[boxXSelected+1][boxYSelected].behavior == Gem.Behavior.NOTHING) {
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected + 1][boxYSelected];
                        boardArr[boxXSelected + 1][boxYSelected] = temp;
                        if(board.checkGemForMatches(boxXSelected, boxYSelected)||board.checkGemForMatches(boxXSelected+1, boxYSelected)){
                            boardArr[boxXSelected + 1][boxYSelected].shiftX -= 1;
                            boardArr[boxXSelected][boxYSelected].shiftX += 1;
                        }
                        else{
                            temp = boardArr[boxXSelected][boxYSelected];
                            boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected + 1][boxYSelected];
                            boardArr[boxXSelected + 1][boxYSelected] = temp;
                        }


                    }
                        break;
                case 2:
                    if(boxYSelected>8 && Math.abs(boardArr[boxXSelected][boxYSelected-1].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected][boxYSelected-1].shiftY)<=0.1 && boardArr[boxXSelected][boxYSelected-1].behavior == Gem.Behavior.NOTHING){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected][boxYSelected-1];
                        boardArr[boxXSelected][boxYSelected-1] = temp;
                        if(board.checkGemForMatches(boxXSelected, boxYSelected)||board.checkGemForMatches(boxXSelected, boxYSelected-1)){
                            boardArr[boxXSelected][boxYSelected-1].shiftY -= 1;
                            boardArr[boxXSelected][boxYSelected].shiftY += 1;
                        }
                        else{
                            temp = boardArr[boxXSelected][boxYSelected];
                            boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected][boxYSelected-1];
                            boardArr[boxXSelected][boxYSelected-1] = temp;
                        }

                    }
                        break;
                default:
                    if(boxYSelected< boardArr[0].length-1 && Math.abs(boardArr[boxXSelected][boxYSelected+1].shiftX)<=0.1 && Math.abs(boardArr[boxXSelected][boxYSelected+1].shiftY)<=0.1 && boardArr[boxXSelected][boxYSelected+1].behavior == Gem.Behavior.NOTHING){
                        Gem temp = boardArr[boxXSelected][boxYSelected];
                        boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected][boxYSelected+1];
                        boardArr[boxXSelected][boxYSelected+1] = temp;
                        if(board.checkGemForMatches(boxXSelected, boxYSelected)||board.checkGemForMatches(boxXSelected, boxYSelected+1)){
                            boardArr[boxXSelected][boxYSelected+1].shiftY += 1;
                            boardArr[boxXSelected][boxYSelected].shiftY -= 1;
                        }
                        else{
                            temp = boardArr[boxXSelected][boxYSelected];
                            boardArr[boxXSelected][boxYSelected] = boardArr[boxXSelected][boxYSelected+1];
                            boardArr[boxXSelected][boxYSelected+1] = temp;
                        }

                    }
                    break;
            }
        }
    }
    //methods for adding to the arraylists
    public void addSlideGem(int x, int y, int destX, int destY, Gem.Type t){
        slideGemList.add(new SlideGem(x, y, destX, destY, t));
    }
    public void addExplosion(int x, int y){
        ExplosionPoint e = new ExplosionPoint(x,y);
        explosions.add(e);
    }
    public void addZapPoint(int x, int y, ZapPoint.Place p){
        ZapPoint z = new ZapPoint(x, y, p);
        zaps.add(z);
    }
    public void setChargelightning(){
        selectBox();
        boardArr[boxXSelected][boxYSelected].charge = Gem.Charge.L;
    }
}
