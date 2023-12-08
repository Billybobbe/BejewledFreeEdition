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
    enum Gem{
        B, B_F, B_L,
        R, R_F, R_L,
        O, O_F, O_L,
        G, G_F, G_L,
        Y, Y_F, Y_L,
        S
    }


    private Gem[][] board;
    public Board(){
        board = new Gem[8][8];
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
                return Gem.R;
            case 1:
                return Gem.G;
            case 2:
                return Gem.B;
            case 3:
                return Gem.Y;
            default:
                return Gem.O;
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
        return switch(g1){
            case B:
            case B_F:
            case B_L:
                yield (g2 == Gem.B || g2 == Gem.B_F || g2 == Gem.B_L);
            case G:
            case G_F:
            case G_L:
                yield (g2 == Gem.G || g2 == Gem.G_F || g2 == Gem.G_L);
            case R:
            case R_F:
            case R_L:
                yield(g2 == Gem.R || g2 == Gem.R_F || g2 == Gem.R_L);
            case O:
            case O_F:
            case O_L:
                yield(g2 == Gem.O || g2 == Gem.O_F || g2 == Gem.O_L);
            case Y:
            case Y_F:
            case Y_L:
                yield(g2 == Gem.Y || g2 == Gem.Y_F || g2 == Gem.Y_L);
            case S:
                yield false;
        };
    }
    public void print(){
        for(int x = 0; x<board.length; x++){
            for(int y = 0; y<board[0].length; y++){
                System.out.print(board[x][y] + " ");
            }
            System.out.println();
        }
    }
}
