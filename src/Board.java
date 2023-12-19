import java.util.ArrayList;

public class Board {
    /*KEY:
    "a" = blue gem
    "b" = flame blue
    "c" = lighting blue

    "d" = red gem
    "e" = flame red
    "f" = lighting red

    "g" = orange gem
    "h" = flaming orange
    "i" = lightning orange

    "j" = green gem
    "k" = flame green
    "l" = lightning green

    "m" = yellow gem
    "n" = flame yellow
    "o" = lightning yellow

    "p" = supernova gem
     */



    private Gem[][] board;
    private GameHandler g;
    public Board(){
        board = new Gem[8][16];
        fillBoard();
    }
    public void setGameHandler(GameHandler g){
        this.g = g;
    }

    //fill it with gems
    private void fillBoard(){
        while(true){
            for(int x = 0; x<board.length; x++){
                for(int y = 0; y<board[0].length; y++){
                   board[x][y] = genGem();
                }
            }
            if(!checkBoardForMatches()){
                return;
            }
        }
    }


    //random gem generation
    private Gem genGem(){
        int random = (int)(Math.random()*6);
        switch(random){
            case 0:
                return new Gem(Gem.Type.R);
            case 1:
                return new Gem(Gem.Type.G);
            case 2:
                return new Gem(Gem.Type.B);
            case 3:
                return new Gem(Gem.Type.O);
            case 4:
                return new Gem(Gem.Type.Y);
            default:
                return new Gem(Gem.Type.P);
        }
    }
    //checks the entire board to see if there are any gems that could match
    private boolean checkBoardForMatches(){
        for(int x = 0; x < board.length; x++){
            for(int y = 8; y < board[0].length; y++){
                if(checkGemForMatches(x,y)){
                    return true;
                }
            }
        }
        return false;
    }
    //check an individual gem for a match
    public boolean checkGemForMatches(int x, int y){
        Gem g = board[x][y];
        int checkSizeX = 1;
        for(int i = x-1; i>=0; i--){
            if(!compareGem(g, board[i][y])||board[i][y]==null||board[i][y].behavior != Gem.Behavior.NOTHING){
                break;
            }
            checkSizeX++;
        }
        for(int i = x+1; i<board.length; i++){
            if(!compareGem(g, board[i][y])||board[i][y]==null||board[i][y].behavior != Gem.Behavior.NOTHING){
                break;
            }
            checkSizeX++;
        }
        int checkSizeY = 1;
        for(int i = y-1; i>=0; i--){
            if(!compareGem(g, board[x][i])||board[x][i]==null||board[x][i].behavior != Gem.Behavior.NOTHING){
                break;
            }
            checkSizeY++;
        }
        for(int i = y+1; i<board[0].length; i++){
            if(!compareGem(g, board[x][i])||board[x][i]==null||board[x][i].behavior != Gem.Behavior.NOTHING){
                break;
            }
            checkSizeY++;
        }
        return checkSizeX>=3||checkSizeY>=3;

    }
    //returns the matches in different directions of a gem
    private int[] getGemMatches(int x, int y){
        Gem g = board[x][y];
        int matchHorizontalLeft = 0;
        int matchHorizontalRight = 0;
        int matchVerticalUp = 0;
        int matchVerticalDown = 0;

        for(int i = x-1; i>=0; i--){
            if(!compareGem(g, board[i][y]) || Math.abs(board[i][y].shiftX)>0.01 || Math.abs(board[i][y].shiftY)>0.01 || board[i][y].behavior!=Gem.Behavior.NOTHING){
                break;
            }
            matchHorizontalLeft++;
        }
        for(int i = x+1; i<board.length; i++){
            if(!compareGem(g, board[i][y]) || Math.abs(board[i][y].shiftX)>0.01 || Math.abs(board[i][y].shiftY)>0.01 || board[i][y].behavior!=Gem.Behavior.NOTHING){
                break;
            }
            matchHorizontalRight++;
        }
        for(int i = y-1; i>7; i--){
            if(!compareGem(g, board[x][i]) || Math.abs(board[x][i].shiftX)>0.01 || Math.abs(board[x][i].shiftY)>0.01 || board[x][i].behavior!=Gem.Behavior.NOTHING){
                break;
            }
            matchVerticalDown++;
        }
        for(int i = y+1; i<board[0].length; i++){
            if(!compareGem(g, board[x][i]) || Math.abs(board[x][i].shiftX)>0.01 || Math.abs(board[x][i].shiftY)>0.01 || board[x][i].behavior!=Gem.Behavior.NOTHING){
                break;
            }
            matchVerticalUp++;
        }
        return new int[]{matchHorizontalLeft, matchHorizontalRight, matchVerticalUp, matchVerticalDown};

    }
    private void changeType(int x, int y, Gem.Type t){
        board[x][y].type = t;
    }
    private void changeCharge(int x, int y, Gem.Charge c){
        board[x][y].charge = c;
    }
    public void removeGem(int x, int y){
        if(board[x][y]!= null){
            switch(board[x][y].charge){
                case F:
                    int left = x-1;
                    int right = x+1;
                    int top = y-1;
                    int bottom = y+1;
                    if(left<0){
                        left = 0;
                    }
                    if(right>=board.length){
                        right = board.length-1;
                    }
                    if(top<8){
                        top = 8;
                    }
                    if(bottom>=board[0].length){
                        bottom = board[0].length-1;
                    }
                    board[x][y] = null;
                    for(int i = left; i<=right; i++){
                        for(int r = top; r<=bottom; r++){
                            if(board[i][r]!=null && board[i][r].behavior== Gem.Behavior.NOTHING){
                                if(board[i][r].charge==Gem.Charge.N){
                                    board[i][r] = null;
                                }
                                else{
                                    removeGem(i,r);
                                }
                            }
                        }
                    }
                    g.addExplosion(x,y);
                    break;

                case L:
                    board[x][y] = null;
                    g.addZapPoint(x, y, GameHandler.ZapPoint.Place.C);
                    for(int i = 0; i<board.length; i++){
                        g.addZapPoint(i, y, GameHandler.ZapPoint.Place.H);
                    }
                    for(int r = 8; r<board[0].length; r++){
                        g.addZapPoint(x, r, GameHandler.ZapPoint.Place.V);
                    }
                    break;
                case N:
                    board[x][y].behavior = Gem.Behavior.SHRINKING;
            }
        }
    }
    //deletes all the gems in a row from start to end
    private void deleteGemsHorizontal(int x, int y, int left, int right) {
        for (int i = x - left; i <= x + right; i++) {
            if(i!=x){
                removeGem(i, y);
            }
        }
    }
    //deletes all the gems in a column from start to end
    private void deleteGemsVertical(int x, int y, int up, int down) {
        for (int i = y - down; i <= y + up; i++) {
            if(i!=y){
                removeGem(x, i);
            }
        }

    }
    //makes the gems look like they are sliding
    private void combineGemsHorizontal(int x, int y, int left, int right) {
        for (int i = x - left; i <= x + right; i++) {
            if(i!=x && board[i][x]!=null){
                g.addSlideGem(i, y, x, y, board[i][y].type);
                board[i][y].behavior = Gem.Behavior.SLIDING;
            }
        }
        for (int i = x - left; i <= x + right; i++) {
            if(i!=x && board[i][y]!=null && board[i][y].charge!= Gem.Charge.N){
                removeGem(i,y);
            }
        }

    }
    private void combineGemsVertical(int x, int y, int up, int down) {
        for (int i = y - down; i <= y + up; i++) {
            if (i != y && board[x][i]!=null) {
                g.addSlideGem(x, i, x, y, board[x][i].type);
                board[x][i].behavior = Gem.Behavior.SLIDING;
            }
        }
        for (int i = y - down; i <= y + up; i++) {
            if (i != y && board[x][i]!=null && board[x][i].charge!= Gem.Charge.N){
                removeGem(x,i);
            }
        }
    }
    //actual matching logic here
    //depending on the number of matches it can turn into a different gem
    public void upgradeGems(){
        for(int type = 0; type<4; type++){
            for(int x = 0; x<board.length; x++) {
                for (int y = 8; y < board[0].length; y++) {
                    if (board[x][y] != null && Math.abs(board[x][y].shiftX) <= 0.01 && Math.abs(board[x][y].shiftY) <= 0.01) {

                        int[] totalMatches = getGemMatches(x, y);
                        int horizontalLeftMatches = totalMatches[0];
                        int horizontalRightMatches = totalMatches[1];
                        int verticalUpMatches = totalMatches[2];
                        int verticalDownMatches = totalMatches[3];

                        switch (type) {
                            /*
                            case 0:
                                if (horizontalLeftMatches + horizontalRightMatches >= 4 || verticalUpMatches + verticalDownMatches >= 4) {
                                    changeType(x, y, Gem.Type.S);
                                    if (horizontalLeftMatches + horizontalRightMatches >= 4) {
                                        combineGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                                    }
                                    else {
                                        for (int i = y - verticalDownMatches; i <= y + verticalUpMatches; i++) {
                                            combineGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                                        }
                                    }
                                }
                                break;
                             */
                            case 1:
                                if (horizontalLeftMatches + horizontalRightMatches >= 2 && verticalUpMatches + verticalDownMatches >= 2){
                                    changeCharge(x, y, Gem.Charge.L);
                                    combineGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                                    combineGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                                }
                                break;
                            case 2:
                                if (horizontalLeftMatches + horizontalRightMatches >= 3) {
                                    changeCharge(x, y, Gem.Charge.F);
                                    combineGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                                }
                                if (verticalUpMatches + verticalDownMatches >= 3) {
                                    changeCharge(x, y, Gem.Charge.F);
                                    combineGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                                }
                                break;
                            case 3:
                                if (verticalUpMatches + verticalDownMatches >= 2) {
                                    removeGem(x, y);
                                    deleteGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                                }
                                if (horizontalLeftMatches + horizontalRightMatches >= 2) {
                                    removeGem(x, y);
                                    deleteGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                                }
                        }
                    }
                }
            }
        }
    }
    //like upgradegems but for a single gem
    //excludes the 3 match as it can ovverride things
    public void upgradeGem(int x, int y){
        for(int type = 0; type<3; type++){
            if (board[x][y] != null && Math.abs(board[x][y].shiftX) <= 0.01 && Math.abs(board[x][y].shiftY) <= 0.01 && y>7) {

                int[] totalMatches = getGemMatches(x, y);
                int horizontalLeftMatches = totalMatches[0];
                int horizontalRightMatches = totalMatches[1];
                int verticalUpMatches = totalMatches[2];
                int verticalDownMatches = totalMatches[3];

                switch (type) {
                    /* I didn't have the time or images to make supernova gem, so this is just left in as an easter egg ;)
                    case 0:
                        if (horizontalLeftMatches + horizontalRightMatches >= 4 || verticalUpMatches + verticalDownMatches >= 4) {

                            if (horizontalLeftMatches + horizontalRightMatches >= 4) {
                                combineGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                            }
                            else {
                                for (int i = y - verticalDownMatches; i <= y + verticalUpMatches; i++) {
                                    combineGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                                }
                            }
                        }
                        break;
                             */
                    case 1:
                        if (horizontalLeftMatches + horizontalRightMatches >= 2 && verticalUpMatches + verticalDownMatches >= 2){
                            changeCharge(x, y, Gem.Charge.L);
                            combineGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                            combineGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                        }
                        break;
                    case 2:
                        if (horizontalLeftMatches + horizontalRightMatches >= 3) {
                            changeCharge(x, y, Gem.Charge.F);
                            combineGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                        }
                        if (verticalUpMatches + verticalDownMatches >= 3) {
                            changeCharge(x, y, Gem.Charge.F);
                            combineGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                        }
                        break;
                }
            }
        }
    }
    private boolean compareGem(Gem g1, Gem g2){
        if(g1 == null || g2 == null){
            return false;
        }
        return g1.type == g2.type;
    }
    //makes all the gems on the board fall down if they have an empty space beneath
    private void fall(){
        for(int x = 0; x<board.length; x++){
            for(int y = board[0].length-2; y>=0; y--){
                if(board[x][y+1] == null && board[x][y]!= null && board[x][y].behavior == Gem.Behavior.NOTHING){
                    Gem temp = board[x][y];
                    board[x][y] = board[x][y+1];
                    board[x][y+1] = temp;
                    temp.shiftY += 1;
                }
            }
        }
    }
    public void randomDelete(){
        int randomx = (int)(Math.random()*8);
        int randomy = (int)(Math.random()*8+8);
        if(board[randomx][randomy] != null && Math.abs(board[randomx][randomy].shiftY)<=0.1){
            removeGem(randomx, randomy);
        }
    }
    //print the board
    public void print(){
        System.out.println();
        for(int y = 0; y<board[0].length; y++){
            for(int x = 0; x<board.length; x++){
                if(board[x][y]!=null){
                    System.out.print(board[x][y].type + "" + board[x][y].charge + " ");
                }
                else{
                    System.out.print("**" + " ");
                }
            }
            System.out.println();
        }
    }
    public Gem[][] getBoard(){
        return board;
    }
    private void fillTopRow(){
        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length-8; y++){
                if(board[x][y] == null){
                    board[x][y] = genGem();
                }
            }
        }
    }

    public void update(){
        fall();
        fillTopRow();
        //upgradeGems();
    }
}
