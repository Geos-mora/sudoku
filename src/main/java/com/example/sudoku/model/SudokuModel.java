package com.example.sudoku.model;

public class SudokuModel {
    private static final int N = 6;

    // Matriz que se muestra en pantalla (con ceros en las celdas editables)
    private int[][] puzzle;

    /* array de la tabla  solucionada  (sin ceros)*/
    private int[][] solution;

    /* Parámetros de bloques 2x3*/
    private final int rowGrid = 3;
    private final int colGrid = 2;
    private final int rowBlock = 2;
    private final int colBlock = 3;

    /*tabla de refenrecia para iniciar en la ui,*/
    private static final int[][] PATRON_PISTAS = {
            {0, 0, 2, 0, 0, 4},
            {0, 3, 0, 0, 5, 0},
            {0, 0, 0, 1, 0, 6},
            {4, 0, 0, 0, 0, 0},
            {0, 5, 0, 0, 6, 0},
            {0, 0, 3, 0, 0, 2}
    };


    /* Getters */
    public int[][] getSudokuTabla() { return puzzle; } // mantiene tu nombre, pero ahora devuelve el puzzle visible
    public int getRowGrid() { return rowGrid; }
    public int getColGrid() { return colGrid; }
    public int getRowBlock() { return rowBlock; }
    public int getColBlock() { return colBlock; }

    /* Constructor: genera solución y aplica patrón de pistas */
    public SudokuModel() {
        regenerar();
    }

    /** Regenera una nueva solución y crea el puzzle según el patrón de pistas */
    public final void regenerar() {
        solution = SudokuGenerator.generarSudoku6x6Resuelto(); // nunca null
        puzzle = new int[N][N];
        /*aqui se genera la tabla donde se recorre la tabla de las pistas y la tabla generada*/
        for (int r = 0; r < N; r++) {
            for (int c = 0; c < N; c++) {
                puzzle[r][c] = (PATRON_PISTAS[r][c] == 0) ? 0 : solution[r][c];
            }
        }
    }

    /* Valor que se muestra en la grilla (puzzle) */
    public int getValorEn(int fila, int col) {
        validarIndices(fila, col);
        return puzzle[fila][col];
    }

    /* Valor correcto de la solución en esa celda (útil para validar) */
    public int getValorSolucion(int fila, int col) {
        validarIndices(fila, col);
        return solution[fila][col];
    }

    /* ¿La celda es editable (pista = 0 en puzzle)? */
    public boolean esCeldaEditable(int fila, int col) {
        validarIndices(fila, col);
        return puzzle[fila][col] == 0;
    }

    /* ¿La celda es “dada” (no editable)? */
    public boolean esCeldaGiven(int fila, int col) {
        validarIndices(fila, col);
        return puzzle[fila][col] != 0;
    }

    /* Valida si el valor ingresado por el usuario coincide con la SOLUCIÓN */
    public boolean esValorCorrecto(int fila, int col, String valorIngresado) {
        validarIndices(fila, col);
        if (valorIngresado == null || valorIngresado.isEmpty()) return true;
        try {
            int valor = Integer.parseInt(valorIngresado);
            return valor == solution[fila][col];
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /* Verifica conflicto en la fila/columna respecto al valor ingresado (contra la solución) */
    public boolean hayConflictoEnFilaOColumna(int fila, int col, String valorIngresado,
                                              int filaSeleccionada, int colSeleccionada) {
        validarIndices(fila, col);
        if (valorIngresado == null || valorIngresado.isEmpty()) return false;

        if (fila == filaSeleccionada || col == colSeleccionada) {
            return !esValorCorrecto(fila, col, valorIngresado);
        }
        return false;
    }

    /* --- Utilidades --- */
    private void validarIndices(int fila, int col) {
        if (puzzle == null || solution == null)
            throw new IllegalStateException("El modelo no ha sido inicializado. Llama a regenerar().");
        if (fila < 0 || fila >= N || col < 0 || col >= N)
            throw new IndexOutOfBoundsException("Índices fuera de rango 0..5: (" + fila + "," + col + ")");
    }
}
