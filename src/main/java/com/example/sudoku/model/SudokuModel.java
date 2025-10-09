package com.example.sudoku.model;

public class SudokuModel {
    private int [][] sudokuTabla;
    private int rowGrid=3;
    private int colGrid=2;
    private int rowBlock=2;
    private int colBlock=3;

    /* Getters */
    public int[][] getSudokuTabla() { return sudokuTabla; }
    public int getRowGrid() { return rowGrid; }
    public int getColGrid() { return colGrid; }
    public int getRowBlock() { return rowBlock; }
    public int getColBlock() { return colBlock; }

    /* Constructor */
    public SudokuModel() {
        sudokuTabla = new int[][] {
            {0, 2, 0, 4, 0, 6},
            {4, 0, 6, 0, 2, 0},
            {0, 3, 0, 5, 0, 1},
            {5, 0, 1, 0, 3, 0},
            {0, 4, 0, 6, 0, 2},
            {6, 0, 2, 0, 4, 0}
    };
    }

    /* Method to obtain the value in position */
    public int getValorEn(int fila, int col) {
        return sudokuTabla[fila][col];
    }

    /* Method to know if a cell is editable (initial value 0)*/
    public boolean esCeldaEditable(int fila, int col) {
        return sudokuTabla[fila][col] == 0;
    }

    /* Method to know if a cell is "given" (values different from 0)"*/
    public boolean esCeldaGiven(int fila, int col) {
        return sudokuTabla[fila][col] != 0;
    }

    /* Validate if the input value is correct*/
    public boolean esValorCorrecto(int fila, int col, String valorIngresado) {
        if (valorIngresado == null || valorIngresado.isEmpty()) {
            return true; // If the cell is empty there's no conflict
        }

        try {
            int valor = Integer.parseInt(valorIngresado);
            return valor == sudokuTabla[fila][col];
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*Verify if there's conflict in the row or column*/
    public boolean hayConflictoEnFilaOColumna(int fila, int col, String valorIngresado,
                                              int filaSeleccionada, int colSeleccionada) {
        if (valorIngresado == null || valorIngresado.isEmpty()) {
            return false;
        }

        // Verify is it's in the same column or row as the selected cell only
        if (fila == filaSeleccionada || col == colSeleccionada) {
            return !esValorCorrecto(fila, col, valorIngresado);
        }

        return false;
    }
}