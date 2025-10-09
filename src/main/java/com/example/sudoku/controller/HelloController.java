package com.example.sudoku.controller;

import com.example.sudoku.model.SudokuModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.ArrayList;
import java.util.ArrayDeque;


public class HelloController {

    @FXML
    private GridPane gridContainer;

    private SudokuModel model;

    @FXML
    public void initialize(){
        model = new SudokuModel();
        System.out.println("initialize is ok");
        crearSudoku();
    }
    private GridPane makeBloque(int row, int col){
        GridPane gp=new GridPane();
        gp.getStyleClass().add("grid-board");
        return gp;
    }

    private TextField makeCell(int row, int col){
        TextField tf=new TextField();
        tf.getStyleClass().add("cell");

        return tf;
    }


    private void crearSudoku(){
        for (int r=0; r< model.getRowGrid(); r++){
            for (int c=0; c<model.getColGrid(); c++){
                GridPane grid=makeBloque(r,c);
                gridContainer.add(grid,c,r);

                for (int row=0; row< model.getRowBlock(); row++){
                    for (int col=0; col<model.getColBlock(); col++){
                        TextField cell=makeCell(row,col);

                        int rGlobal = r * model.getRowBlock() + row;
                        int cGlobal = c * model.getColBlock() + col;

                        // se guardan  las coordenadas globales como propiedades
                        cell.getProperties().put("fila", rGlobal);
                        cell.getProperties().put("col", cGlobal);
                        cell.setId("celda_" + rGlobal + "_" + cGlobal);
                        grid.add(cell,col,row);
                        int fila=(int)cell.getProperties().get("fila");
                        int columna=(int)cell.getProperties().get("col");
                     //   validarEntrada(gridContainer, columna,fila);


                        /**/
                        cell.setOnMouseClicked(event->{


                            /*funcion para resaltar celdas*/
                            sombrearFilaColumna(gridContainer,columna,fila);


                            System.out.println("Click en fila=" + fila + " col=" + columna);
                            GridPane padre=(GridPane) cell.getParent();

                            for (Node hijo: padre.getChildren()){
                                if (hijo instanceof TextField){
                                    TextField tf=(TextField) hijo;
                                    tf.setStyle("-fx-background-color: #C2CCFF;");


                                }
                            }
                            cell.setStyle("-fx-background-color: #FFBF63;");
                            System.out.println("id: "+cell.getId());
                        });

                    }
                }
                iniciarTabla(gridContainer);
                eventoMouse(gridContainer );
                validarEntrada(gridContainer);

            }
        }
    }


    private void sombrearFilaColumna(Parent padre, int columnaSeleccionada, int filaSeleccionada) {
        /* clear previous styles */
        for (Node node : padre.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                TextField tf = (TextField) node;
                tf.setStyle("");
            }else if (node instanceof Parent){
                sombrearFilaColumna((Parent) node,columnaSeleccionada,filaSeleccionada);
            }
        }

        /*highlight the row and column of the selected cell*/
        for (Node node : padre.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                TextField tf = (TextField) node;
                Integer col = (Integer) tf.getProperties().get("col");
                Integer fila=(Integer)  tf.getProperties().get("fila");


                if (col != null && col == columnaSeleccionada) {
                    tf.setStyle("-fx-background-color: #C2CCFF;");




                }
                if (fila!=null && fila==filaSeleccionada){
                    tf.setStyle("-fx-background-color: #C2CCFF;");
                }

            }else if (node instanceof Parent){
                sombrearFilaColumna((Parent) node, columnaSeleccionada,filaSeleccionada);
            }
        }
    }



    private void iniciarTabla(Parent padre){
        for (Node nodo: padre.getChildrenUnmodifiable()){
            if (nodo instanceof TextField){
                TextField tf=(TextField)nodo;
                Integer col=(Integer) tf.getProperties().get("col");
                Integer fila=(Integer) tf.getProperties().get("fila");

                if (fila == null || col == null) continue;

                // Obtener valor del modelo
                int v = model.getValorEn(fila, col);

                // Actualizar vista
                tf.setText(v==0? "" : Integer.toString(v));
                tf.setEditable(model.esCeldaEditable(fila, col));

                tf.getStyleClass().remove("given");
                if (model.esCeldaGiven(fila, col) && !tf.getStyleClass().contains("given")){
                    tf.getStyleClass().add("given");
                }

            }else if (nodo instanceof Parent){
                iniciarTabla((Parent) nodo);
            }
        }
    }


    /* funtion to change the color textfields when the mouse is over them */
    private void eventoMouse(Parent padre){
        for (Node nodo: padre.getChildrenUnmodifiable()){
            if (nodo instanceof TextField){
                TextField tf=(TextField) nodo;

                tf.setOnMouseEntered(event->{
                    tf.getStyleClass().add("hover");
                });
                tf.setOnMouseExited(event->{
                    tf.getStyleClass().remove("hover");
                });

            }else if (nodo instanceof Parent){
                eventoMouse((Parent) nodo);
            }
        }
    }


    private void validarEntrada(Parent padre) {
        for (TextField tf : todasLasCeldas(padre)) {
            /* Evitar adjuntar dos veces el handler*/
            if (Boolean.TRUE.equals(tf.getProperties().get("validador"))) continue;
            tf.getProperties().put("validador", true);

            tf.setOnKeyReleased(ev -> {
                String v = tf.getText();
                if (v == null) v = "";
                v = v.trim();

                Integer r0 = (Integer) tf.getProperties().get("fila");
                Integer c0 = (Integer) tf.getProperties().get("col");
                if (r0 == null || c0 == null) return;

                /* Limpiar colores anteriores en fila/col/bloque del entorno de esta celda*/
                limpiarEntorno(padre, r0, c0);

                if (v.isEmpty()) {
                    tf.setStyle(""); // si borran el número, quitamos rojo
                    return;
                }

                boolean conflicto = false;

                for (TextField otro : todasLasCeldas(padre)) {
                    if (otro == tf) continue;

                    Integer r = (Integer) otro.getProperties().get("fila");
                    Integer c = (Integer) otro.getProperties().get("col");
                    if (r == null || c == null) continue;

                    boolean mismaFilaColBloque = (r == r0) || (c == c0) || mismoBloque(r, c, r0, c0);

                    String valOtro = otro.getText();
                    if (valOtro == null) valOtro = "";
                    valOtro = valOtro.trim();

                    if (mismaFilaColBloque && !valOtro.isEmpty() && v.equals(valOtro)) {
                        /* Conflicto: marcar ambos en rojo*/
                        otro.setStyle("-fx-background-color: #FFC0B8;");
                        tf.setStyle("-fx-background-color: #FFC0B8;");
                        conflicto = true;
                    }
                }

                if (!conflicto) {
                    /* Si no hubo conflictos, quitamos el rojo de la celda actual*/
                    tf.setStyle("");
                }
            });
        }
    }

    private List<TextField> todasLasCeldas(Parent padre){
        ArrayList<TextField> listaCeldas=new ArrayList<>();

        for (Node node: padre.getChildrenUnmodifiable()){
            if (node instanceof TextField){
                TextField celda=(TextField) node;
                listaCeldas.add(celda);

            }else {
                if (node instanceof Parent){
                    listaCeldas.addAll(todasLasCeldas((Parent) node));
                }
            }
        }
        return listaCeldas;
    }

    private boolean mismoBloque(int r1, int c1, int r2, int c2) {
        int rb = model.getRowBlock(); // 2
        int cb = model.getColBlock(); // 3
        return (r1 / rb == r2 / rb) && (c1 / cb == c2 / cb);
    }

    private void limpiarEntorno(Parent root, int filaBase, int colBase) {
        for (TextField tf : todasLasCeldas(root)) {
            Integer r = (Integer) tf.getProperties().get("fila");
            Integer c = (Integer) tf.getProperties().get("col");
            if (r == null || c == null) continue;

            /* Limpiamos solo estilos */
            if (r == filaBase || c == colBase || mismoBloque(r, c, filaBase, colBase)) {
                tf.setStyle("");
            }
        }
    }



}