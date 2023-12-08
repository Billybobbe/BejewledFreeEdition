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
        board = new Gem[8][9];
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
    private boolean compareGem(Gem g1, Gem g2){
        return switch(g1.type){
            case B:
            case B_F:
            case B_L:
                yield (g2.type == Gem.Type.B || g2.type == Gem.Type.B_F || g2.type == Gem.Type.B_L);
            case G:
            case G_F:
            case G_L:
                yield (g2.type == Gem.Type.G || g2.type == Gem.Type.G_F || g2.type == Gem.Type.G_L);
            case R:
            case R_F:
            case R_L:
                yield(g2.type == Gem.Type.R || g2.type == Gem.Type.R_F || g2.type == Gem.Type.R_L);
            case O:
            case O_F:
            case O_L:
                yield(g2.type == Gem.Type.O || g2.type == Gem.Type.O_F || g2.type == Gem.Type.O_L);
            case Y:
            case Y_F:
            case Y_L:
                yield(g2.type == Gem.Type.Y || g2.type == Gem.Type.Y_F || g2.type == Gem.Type.Y_L);
            case S:
                yield false;
        };
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
        int randomy = (int)(Math.random()*8);
        board[randomx][randomy] = null;
    }
    public void print(){
        System.out.println();
        for(int y = 0; y<board[0].length; y++){
            for(int x = 0; x<board.length; x++){
                if(board[x][y]!=null){
                    System.out.print(board[x][y].type + " ");
                }
                else{
                    System.out.print("*" + " ");
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
            if(board[x][0] == null){
                board[x][0] = genGem();
            }
        }
    }

    public void update(){
        fillTopRow();
        fall();
    }
}
