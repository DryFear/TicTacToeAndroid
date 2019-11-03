package ru.unfortunately.school.tictactoe;

import androidx.annotation.NonNull;

public class WinnerLogic {


    public static int checkWinner(@NonNull GridItemView[][] mas) throws Exception {
        boolean isWin = true;

        //Matrix correct check
        int len = mas[0].length;
        for (int i = 1; i < mas.length; i++){
                if(mas[i].length != len){
                    throw new Exception("Incorrect matrix size");
                }
        }
        //Horizontal check
        for(int i = 0; i < mas.length; i++){
            int symbol = mas[i][0].getSymbol();
            for (int j = 1; j < mas[i].length; j++) {
                if(symbol != mas[i][j].getSymbol()){
                    isWin = false;
                }
            }
            if(isWin && symbol != -1){
                return symbol;
            }
        }
        //Vertical check
        isWin = true;
        for(int i = 0; i < mas[0].length; i++){
            int symbol = mas[0][i].getSymbol();
            for (int j = 1; j < mas.length; j++) {
                if(symbol != mas[j][i].getSymbol()){
                    isWin = false;
                }
            }
            if(isWin && symbol != -1){
                return symbol;
            }
        }
        //Diagonal check
        isWin = true;
        if(mas.length == mas[0].length){
            int symbol = mas[0][0].getSymbol();
            for (int i = 1; i < mas.length; i++) {
                if(symbol != mas[i][i].getSymbol()){
                    isWin = false;
                }
            }
            if(isWin && symbol != -1){
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
            if(isWin && symbol != -1){
                return symbol;
            }
        }
        return -1;
    }

}
