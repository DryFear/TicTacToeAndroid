package ru.unfortunately.school.tictactoe;

import android.util.Log;

import androidx.annotation.NonNull;

class WinnerLogic {

    private static final String TAG = "TEST";

    static int checkWinner(@NonNull GridItemView[][] mas){
        //Matrix correct check
        matrixCorrectCheck(mas);

        //Horizontal check
        Integer symbol1 = horizontalCheck(mas);
        if (symbol1 != null) return symbol1;

        //Vertical check
        Integer symbol2 = verticalCheck(mas);
        if (symbol2 != null) return symbol2;

        //Diagonal check
        Integer symbol = diagonalCheck(mas);
        if (symbol != null) return symbol;

        //Is draw checking
        boolean drawFlag = drawCheck(mas);
        if(drawFlag){
            return GridItemView.EMPTY_SYMBOL;
        }

        return -1;
    }

    private static boolean drawCheck(@NonNull GridItemView[][] mas) {
        boolean drawFlag = true;
        for (GridItemView[] ma : mas) {
            for (GridItemView gridItemView : ma) {
                if (gridItemView.getSymbol() == GridItemView.EMPTY_SYMBOL) {
                    drawFlag = false;
                }
            }
        }
        return drawFlag;
    }

    private static Integer diagonalCheck(@NonNull GridItemView[][] mas) {
        boolean isWin;
        isWin = true;
        if(mas.length == mas[0].length){
            int symbol = mas[0][0].getSymbol();
            for (int i = 1; i < mas.length; i++) {
                if(symbol != mas[i][i].getSymbol()){
                    isWin = false;
                }
            }
            if(isWin && symbol != GridItemView.EMPTY_SYMBOL){
                for (int i = 0; i < mas.length; i++) {
                    mas[i][i].makeItemWinner();
                }
                return symbol;
            }
        }

        isWin = true;
        if(mas.length == mas[0].length){
            int symbol = mas[0][mas[0].length-1].getSymbol();
            for (int i = 1; i < mas.length; i++) {
                if(symbol != mas[i][mas.length - 1 - i].getSymbol()){
                    isWin = false;
                }
            }
            if(isWin && symbol != GridItemView.EMPTY_SYMBOL){
                for (int i = 0; i < mas.length; i++) {
                    mas[i][mas.length - 1 - i].makeItemWinner();
                }
                return symbol;
            }
        }
        return null;
    }

    private static Integer verticalCheck(@NonNull GridItemView[][] mas) {
        boolean isWin;
        for(int i = 0; i < mas[0].length; i++){
            isWin = true;
            int symbol = mas[0][i].getSymbol();
            for (int j = 1; j < mas.length; j++) {
                if(symbol != mas[j][i].getSymbol()){
                    isWin = false;
                }
            }
            if(isWin && symbol != GridItemView.EMPTY_SYMBOL){
                for (GridItemView[] ma : mas) {
                    ma[i].makeItemWinner();
                }
                return symbol;
            }
        }
        return null;
    }

    private static void matrixCorrectCheck(@NonNull GridItemView[][] mas) {
        int len = mas[0].length;
        for (int i = 1; i < mas.length; i++){
                if(mas[i].length != len){
                    throw new RuntimeException("Incorrect matrix size");
                }
        }
    }

    private static Integer horizontalCheck(@NonNull GridItemView[][] mas) {
        boolean isWin;
        for (GridItemView[] ma : mas) {
            isWin = true;
            int symbol = ma[0].getSymbol();
            for (int j = 1; j < ma.length; j++) {
                if (symbol != ma[j].getSymbol()) {
                    isWin = false;
                }
            }
            if (symbol != GridItemView.EMPTY_SYMBOL && isWin) {
                for (GridItemView gridItemView : ma) {
                    gridItemView.makeItemWinner();
                    Log.i(TAG, "horizontalCheck:");
                }
                return symbol;
            }
        }
        return null;
    }

}
