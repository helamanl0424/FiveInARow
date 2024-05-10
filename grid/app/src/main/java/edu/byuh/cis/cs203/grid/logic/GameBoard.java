package edu.byuh.cis.cs203.grid.logic;

public class GameBoard {

    private Player[][] grid;
    private final int DIM = 5;
    private Player whoseTurnIsIt;
//    public int row;
//    public int column = 0;
//    public boolean checkForHorizontalWinSoon = false;

    //private char[] rowLetters = {'A','B','C','D','E' };

    /**
     * constructor
     */
    public GameBoard() {
        grid = new Player[DIM][DIM];
        resetGameBoard();
        whoseTurnIsIt = Player.X;
    }

    /**
     * reset the GameBoard
     */
    public void resetGameBoard() {
        for (int i=0; i<DIM; ++i) {
            for (int j=0; j<DIM; ++j) {
                grid[i][j] = Player.BLANK;
            }
        }
        whoseTurnIsIt = Player.X;
        //checkForHorizontalWinSoon = false;
    }

    /**
     * store the moves in array
     * @param move
     */
    public void submitMove(char move) {
        if (move >= '1' && move <= '5') {
            //vertical move, move stuff down
            int col = Integer.parseInt(""+move)-1;
            Player newVal = whoseTurnIsIt;
            for (int i=0; i<DIM; ++i) {
                if (grid[i][col] == Player.BLANK) {
                    grid[i][col] = newVal;
                    break;
                } else {
                    Player tmp = grid[i][col];
                    grid[i][col] = newVal;
                    newVal = tmp;
                }
            }

        } else { //A-E
            //horizontal move, move stuff right
            int row = (int)(move - 'A');
            Player newVal = whoseTurnIsIt;
            for (int i=0; i<DIM; ++i) {
                if (grid[row][i] == Player.BLANK) {
                    grid[row][i] = newVal;
                    break;
                } else {
                    Player tmp = grid[row][i];
                    grid[row][i] = newVal;
                    newVal = tmp;
                }
            }
        }
        if (whoseTurnIsIt == Player.X) {
            whoseTurnIsIt = Player.O;
        } else {
            whoseTurnIsIt = Player.X;
        }
    }

//    public void checkForHorizontalWinSoon() {
//        for (int i=0; i<DIM; ++i) {
//            int count = 0;
//            for (int j=0; j<DIM; ++j) {
//                if (grid[i][j] == Player.X) {
//                    count++;
//                    if (count == 4) {
//                        checkForHorizontalWinSoon = true;
//                        row = i;
//                    }
//                }
//            }
//        }
//    }
//
//    public int getRow() {
//        return row;
//    }
//
//    public Player checkForVerticalWinSoon() {
//
//        return null;
//    }
//
//    public Player checkForDiagnalWinSoon() {
//
//        return null;
//    }

    /**
     * check the winner
     * @return
     */
    public Player checkForWin() {
        Player winner = Player.BLANK;
        Player winners = Player.BLANK;

        //check all rows
        for (int i=0; i<DIM; ++i) {
            if (grid[i][0] != Player.BLANK) {
                winner = grid[i][0];
                for (int j=0; j<DIM; ++j) {
                    if (grid[i][j] != winner) {
                        winner = Player.BLANK;
                        break;
                    }
                }
                if (winner != Player.BLANK) {
                    if (winners == Player.BLANK) {
                        winners = winner; //5 in a row!
                    }else if (winners != winner) {
                        return Player.TIE; //There are two winners!
                    }
                }
            }
        }

        //check all columns
        for (int i=0; i<DIM; ++i) {
            if (grid[0][i] != Player.BLANK) {
                winner = grid[0][i];
                for (int j=0; j<DIM; ++j) {
                    if (grid[j][i] != winner) {
                        winner = Player.BLANK;
                        break;
                    }
                }
                if (winner != Player.BLANK) {
                    if (winners == Player.BLANK) {
                        winners = winner; //5 in a colunms!
                    }else if (winners != winner) {
                        return Player.TIE; //There are two winners!
                    }
                }
            }
        }

        //check top-left -> bottom-right diagonal
        if (grid[0][0] != Player.BLANK) {
            winner = grid[0][0];
            for (int i=0; i<DIM; ++i) {
                if (grid[i][i] != winner) {
                    winner = Player.BLANK;
                    break;
                }
            }
            if (winner != Player.BLANK) {
                if (winners == Player.BLANK) {
                    winners = winner; //5 in a diagonal!
                }else if (winners != winner) {
                    return Player.TIE; //There are two winners!

                }
            }
        }

        //check bottom-left -> top-right diagonal
        if (grid[DIM-1][0] != Player.BLANK) {
            winner = grid[DIM-1][0];
            for (int i=0; i<DIM; ++i) {
                if (grid[DIM-1-i][i] != winner) {
                    winner = Player.BLANK;
                    break;
                }
            }
            if (winner != Player.BLANK) {
                if (winners == Player.BLANK) {
                    winners = winner; //5 in a diagonal!
                }else if (winners != winner) {
                    return Player.TIE; //There are two winners!
                }
            }
        }
        return winners;
    }

    /**
     * getter for currentPlayer
     * @return
     */
    public Player getCurrentPlayer() {

        return whoseTurnIsIt;
    }
}