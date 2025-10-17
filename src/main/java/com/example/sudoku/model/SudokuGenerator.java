package com.example.sudoku.model;

public class SudokuGenerator {

    // Constructor privado para que no se instancie la clase
    private SudokuGenerator() {}

    /* función que generadora el Sudoku 6x6 resuelto*/
    public static int[][] generarSudoku6x6Resuelto() {
        final int n = 6, boxRows = 2, boxCols = 3;
        int[][] base = new int[n][n];
        // Base patrón válido para bloques 2x3
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                base[r][c] = ((r * boxCols + (r / boxRows) + c) % n) + 1;
            }
        }

        // Bandas (filas) = 3; Pilas (columnas) = 2  ← aquí estaba el bug
        int[] bandOrder = {0, 1, 2};                 // 3 bandas de 2 filas
        int[] stackOrder = {0, 1};                   // 2 pilas de 3 columnas (CORREGIDO)
        int[][] rowsInBand = {{0, 1}, {2, 3}, {4, 5}};
        int[][] colsInStack = {{0, 1, 2}, {3, 4, 5}};

        java.util.concurrent.ThreadLocalRandom rnd = java.util.concurrent.ThreadLocalRandom.current();

        // Shuffle de bandas y orden interno de filas
        for (int i = bandOrder.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = bandOrder[i]; bandOrder[i] = bandOrder[j]; bandOrder[j] = t;
        }
        for (int b = 0; b < rowsInBand.length; b++) {
            for (int i = rowsInBand[b].length - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int t = rowsInBand[b][i]; rowsInBand[b][i] = rowsInBand[b][j]; rowsInBand[b][j] = t;
            }
        }

        // Shuffle de pilas y orden interno de columnas
        for (int i = stackOrder.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = stackOrder[i]; stackOrder[i] = stackOrder[j]; stackOrder[j] = t;
        }
        for (int s = 0; s < colsInStack.length; s++) {
            for (int i = colsInStack[s].length - 1; i > 0; i--) {
                int j = rnd.nextInt(i + 1);
                int t = colsInStack[s][i]; colsInStack[s][i] = colsInStack[s][j]; colsInStack[s][j] = t;
            }
        }

        // Permutación de números 1..6
        int[] numPerm = {1, 2, 3, 4, 5, 6};
        for (int i = numPerm.length - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = numPerm[i]; numPerm[i] = numPerm[j]; numPerm[j] = t;
        }

        // Permutación final de filas y columnas preservando bloques
        int[] rowPerm = new int[n];
        int idx = 0;
        for (int b = 0; b < bandOrder.length; b++) {
            int band = bandOrder[b];
            for (int k = 0; k < rowsInBand[band].length; k++) {
                rowPerm[idx++] = rowsInBand[band][k];
            }
        }

        int[] colPerm = new int[n];
        idx = 0;
        for (int s = 0; s < stackOrder.length; s++) {
            int stack = stackOrder[s];
            for (int k = 0; k < colsInStack[stack].length; k++) {
                colPerm[idx++] = colsInStack[stack][k];
            }
        }

        // Aplicar permutaciones y reetiquetar dígitos
        int[][] out = new int[n][n];
        for (int r = 0; r < n; r++) {
            for (int c = 0; c < n; c++) {
                int v = base[rowPerm[r]][colPerm[c]];
                out[r][c] = numPerm[v - 1];
            }
        }
        return out;
    }

}
