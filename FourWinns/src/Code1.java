/*
    Vier Gewinnt
*/

import codedraw.*;

import codedraw.Palette;

public class Code1 {

    private static int[][] genGameBoard(int row, int col) {
        int[][] gameBoard = new int[row][col];
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                gameBoard[i][j] = 0;
            }
        }
        return gameBoard;
    }

    private static void drawGameBoard(CodeDraw myDrawObj, int[][] currentGameBoard, int oneSquareSize) {
        int row = currentGameBoard.length;
        int col = currentGameBoard[0].length;
        int x = 0;
        int y = 0;
        myDrawObj.setColor(Palette.BLUE);
        myDrawObj.fillRectangle(0, 0, col * oneSquareSize, row * oneSquareSize);
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                if (currentGameBoard[i][j] == 0) {
                    myDrawObj.setColor(Palette.GRAY);
                    myDrawObj.fillCircle(x + (oneSquareSize / 2), y + (oneSquareSize / 2), (oneSquareSize / 3));
                } else if (currentGameBoard[i][j] == 1) {
                    myDrawObj.setColor(Palette.RED);
                    myDrawObj.fillCircle(x + (oneSquareSize / 2), y + (oneSquareSize / 2), (oneSquareSize / 3));
                } else if (currentGameBoard[i][j] == 2) {
                    myDrawObj.setColor(Palette.YELLOW);
                    myDrawObj.fillCircle(x + (oneSquareSize / 2), y + (oneSquareSize / 2), (oneSquareSize / 3));
                }
                x += oneSquareSize;
            }
            x = 0;
            y += oneSquareSize;
            }
        myDrawObj.show();
    }


    private static boolean isMovePossible(int[][] currentGameBoard, int col) {
        boolean movePossible = false;
        for (int i = currentGameBoard.length - 1; i >= 0; i--) {
            if (currentGameBoard[i][col] == 0) {
                movePossible = true;
                break;
            }
        }
        return movePossible;
    }

    private static void makeMove(int[][] currentGameBoard, int player, int col) {
        for (int i = currentGameBoard.length - 1; i >= 0; i--) {
            if (currentGameBoard[i][col] == 0) {
                currentGameBoard[i][col] = player;
                break;
            }
        }
    }

    private static boolean existsWinner(int[][] currentGameBoard, int player) {
        // check horizontal
        for (int row = 0; row < currentGameBoard.length; row++) {
            for (int col = 0; col < currentGameBoard[row].length - 3; col++) {
                if (currentGameBoard[row][col] == player && currentGameBoard[row][col + 1] == player
                        && currentGameBoard[row][col + 2] == player && currentGameBoard[row][col + 3] == player) {
                    return true;
                }
            }
        }
        // check vertical
        for (int row = 0; row < currentGameBoard.length - 3; row++) {
            for (int col = 0; col < currentGameBoard[row].length; col++) {
                if (currentGameBoard[row][col] == player && currentGameBoard[row + 1][col] == player
                        && currentGameBoard[row + 2][col] == player && currentGameBoard[row + 3][col] == player) {
                    return true;
                }
            }
        }
        // check diagonal (top-left to bottom-right)
        for (int row = 0; row < currentGameBoard.length - 3; row++) {
            for (int col = 0; col < currentGameBoard[row].length - 3; col++) {
                if (currentGameBoard[row][col] == player && currentGameBoard[row + 1][col + 1] == player
                        && currentGameBoard[row + 2][col + 2] == player && currentGameBoard[row + 3][col + 3] == player) {
                    return true;
                }
            }
        }
        // check diagonal (bottom-left to top-right)
        for (int row = 3; row < currentGameBoard.length; row++) {
            for (int col = 0; col < currentGameBoard[row].length - 3; col++) {
                if (currentGameBoard[row][col] == player && currentGameBoard[row - 1][col + 1] == player
                        && currentGameBoard[row - 2][col + 2] == player && currentGameBoard[row - 3][col + 3] == player) {
                    return true;
                }
            }
        }
        return false;
    }


    public static void main(String[] args) {

        // canvas settings
        int rowsGameBoard = 6;
        int colsGameBoard = 7;
        int oneSquareSize = 50;
        int width = oneSquareSize * colsGameBoard;
        int height = oneSquareSize * rowsGameBoard;

        CodeDraw myDrawObj = new CodeDraw(width, height);
        EventScanner myEventSC = myDrawObj.getEventScanner();

        // game variables
        int[][] myGameBoard = genGameBoard(rowsGameBoard, colsGameBoard);
        int player = 1;
        int fieldsUsed = 0;
        boolean gameActive = true;

        // set font for text
        TextFormat font = new TextFormat();
        font.setFontSize(28);
        font.setFontName("Arial");
        font.setTextOrigin(TextOrigin.CENTER);
        font.setBold(true);
        myDrawObj.setTextFormat(font);

        // initial draw of the game board
        drawGameBoard(myDrawObj, myGameBoard, oneSquareSize);

        // game play starts
        System.out.println("Player " + player + (player == 1 ? " (RED)" : " (YELLOW)") + " has to make a move!");
        while (!myDrawObj.isClosed() && gameActive) {
            if(myEventSC.hasKeyPressEvent()){
                if(myEventSC.nextKeyPressEvent().getChar() == 'q'){
                    gameActive = false;
                }
            }
            else if (myEventSC.hasMouseClickEvent()) {
                MouseClickEvent currentClick = myEventSC.nextMouseClickEvent();
                int mouseX = currentClick.getX();
                int mouseY = currentClick.getY();
                int col = mouseX / oneSquareSize;
                if (isMovePossible(myGameBoard, col)) {
                    makeMove(myGameBoard, player, col);
                    drawGameBoard(myDrawObj, myGameBoard, oneSquareSize);
                    if (existsWinner(myGameBoard, player)) {
                        myDrawObj.setColor(Palette.CYAN);
                        myDrawObj.drawText(width / 2, height / 2, "Player " + player + (player == 1 ? " (RED) " : " (YELLOW) ") + "wins!");
                        myDrawObj.show(3000);
                        myDrawObj.setColor(Palette.ORANGE);
                        myDrawObj.drawText( width / 2, height / 2, "Next round!");
                        myDrawObj.show(1000);
                        myGameBoard = genGameBoard(rowsGameBoard, colsGameBoard);
                        drawGameBoard(myDrawObj, myGameBoard, oneSquareSize);
                        fieldsUsed = 0;
                    } else if (fieldsUsed == rowsGameBoard * colsGameBoard) {
                        myDrawObj.drawText(width / 2, height / 2, "Board full!");
                        myDrawObj.show(3000);
                        myGameBoard = genGameBoard(rowsGameBoard, colsGameBoard);
                        drawGameBoard(myDrawObj, myGameBoard, oneSquareSize);
                        fieldsUsed = 0;
                    } else {
                        player = player == 1 ? 2 : 1;
                        fieldsUsed++;
                        System.out.println("Player " + player + (player == 1 ? " (RED)" : " (YELLOW)") + " has to make a move!");
                    }
                } else {
                    myDrawObj.setColor(Palette.ORANGE);
                    myDrawObj.drawText( width / 2, height / 2, "Column already full!");
                    myDrawObj.show(1000);
                }
            } else {
                myEventSC.nextEvent();
            }
        }
        myDrawObj.close();
    }
}



