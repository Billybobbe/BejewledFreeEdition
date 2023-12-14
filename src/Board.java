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
    public Board(){
        board = new Gem[8][16];
        fillBoard();
    }
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



    private Gem genGem(){
        int random = (int)(Math.random()*5);
        switch(random){
            case 0:
                return new Gem(Gem.Type.R);
            case 1:
                return new Gem(Gem.Type.G);
            case 2:
                return new Gem(Gem.Type.B);
            case 3:
                return new Gem(Gem.Type.O);
            default:
                return new Gem(Gem.Type.Y);
        }
    }
    private boolean checkBoardForMatches(){
        for(int x = 0; x < board.length; x++){
            for(int y = 0; y < board[0].length; y++){
                if(checkGemForMatches(x,y)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean checkGemForMatches(int x, int y){
        Gem g = board[x][y];
        int checkSizeX = 1;
        for(int i = x-1; i>=0; i--){
            if(!compareGem(g, board[i][y])){
                break;
            }
            checkSizeX++;
        }
        for(int i = x+1; i<board.length; i++){
            if(!compareGem(g, board[i][y])){
                break;
            }
            checkSizeX++;
        }
        int checkSizeY = 1;
        for(int i = y-1; i>=0; i--){
            if(!compareGem(g, board[x][i])){
                break;
            }
            checkSizeY++;
        }
        for(int i = y+1; i<board[0].length; i++){
            if(!compareGem(g, board[x][i])){
                break;
            }
            checkSizeY++;
        }
        return checkSizeX>=3||checkSizeY>=3;

    }
    private int[] getGemMatches(int x, int y){
        Gem g = board[x][y];
        int matchHorizontalLeft = 0;
        int matchHorizontalRight = 0;
        int matchVerticalUp = 0;
        int matchVerticalDown = 0;

        for(int i = x-1; i>=0; i--){
            if(!compareGem(g, board[i][y]) || Math.abs(board[i][y].shiftX)>0.1 || Math.abs(board[i][y].shiftY)>0.1 || board[i][y].size<1){
                break;
            }
            matchHorizontalLeft++;
        }
        for(int i = x+1; i<board.length; i++){
            if(!compareGem(g, board[i][y]) || Math.abs(board[i][y].shiftX)>0.1 || Math.abs(board[i][y].shiftY)>0.1 || board[i][y].size<1){
                break;
            }
            matchHorizontalRight++;
        }
        for(int i = y-1; i>7; i--){
            if(!compareGem(g, board[x][i]) || Math.abs(board[x][i].shiftX)>0.1 || Math.abs(board[x][i].shiftY)>0.1 || board[x][i].size<1){
                break;
            }
            matchVerticalDown++;
        }
        for(int i = y+1; i<board[0].length; i++){
            if(!compareGem(g, board[x][i]) || Math.abs(board[x][i].shiftX)>0.1 || Math.abs(board[x][i].shiftY)>0.1 || board[x][i].size<1){
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
    private void removeGem(int x, int y){
        if(board[x][y].size == 1){
            board[x][y].size = 0.9f;
        }
    }
    private void deleteGemsHorizontal(int x, int y, int left, int right) {
        for (int i = x - left; i <= x + right; i++) {
            if(i!=x){
                removeGem(i, y);
            }
        }
    }
    private void deleteGemsVertical(int x, int y, int up, int down) {
        for (int i = y - down; i <= y + up; i++) {
            if(i!=y){
                removeGem(x, i);
            }
        }
    }

    public void upgradeGems(){
        for(int type = 0; type<4; type++){
            for(int x = 0; x<board.length; x++) {
                for (int y = 8; y < board[0].length; y++) {
                    if (board[x][y] != null && Math.abs(board[x][y].shiftX) <= 0.1 && Math.abs(board[x][y].shiftY) <= 0.1) {

                        int[] totalMatches = getGemMatches(x, y);
                        int horizontalLeftMatches = totalMatches[0];
                        int horizontalRightMatches = totalMatches[1];
                        int verticalUpMatches = totalMatches[2];
                        int verticalDownMatches = totalMatches[3];

                        switch (type) {
                            case 0:
                                if (horizontalLeftMatches + horizontalRightMatches >= 4 || verticalUpMatches + verticalDownMatches >= 4) {
                                    changeType(x, y, Gem.Type.S);
                                    if (horizontalLeftMatches + horizontalRightMatches >= 4) {
                                        deleteGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                                    }
                                    else {
                                        for (int i = y - verticalDownMatches; i <= y + verticalUpMatches; i++) {
                                            deleteGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                                        }
                                    }
                                }
                                break;
                            case 1:
                                if (horizontalLeftMatches + horizontalRightMatches >= 2 && verticalUpMatches + verticalDownMatches >= 2){
                                    changeCharge(x, y, Gem.Charge.L);
                                    deleteGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                                    deleteGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
                                }
                                break;
                            case 2:
                                if (horizontalLeftMatches + horizontalRightMatches >= 3) {
                                    changeCharge(x, y, Gem.Charge.F);
                                    deleteGemsHorizontal(x, y, horizontalLeftMatches, horizontalRightMatches);
                                }
                                if (verticalUpMatches + verticalDownMatches >= 3) {
                                    changeCharge(x, y, Gem.Charge.F);
                                    deleteGemsVertical(x, y, verticalUpMatches, verticalDownMatches);
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
    private boolean compareGem(Gem g1, Gem g2){
        if(g1 == null || g2 == null){
            return false;
        }
        return g1.type == g2.type;
    }
    private void fall(){
        for(int x = 0; x<board.length; x++){
            for(int y = board[0].length-2; y>=0; y--){
                if(board[x][y+1] == null && board[x][y]!= null){
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
        upgradeGems();
    }
}
