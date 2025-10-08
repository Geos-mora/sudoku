package com.example.sudoku.controller;

import com.example.sudoku.model.SudokuModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class HelloController {

    @FXML
    private GridPane gridContainer;

    private SudokuModel model;

    private TextField celdaSeleccionada = null;

    @FXML
    public void initialize(){
        model = new SudokuModel();
        System.out.println("initialize is ok");


        for (int r=0; r< model.getRowGrid(); r++){
            for (int c=0; c<model.getColGrid(); c++){
                GridPane grid=makeBloque(r,c);
                gridContainer.add(grid,c,r);

                for (int row=0; row< model.getRowBlock(); row++){
                    for (int col=0; col<model.getColBlock(); col++){
                        TextField cell=makeCell(row,col);

                        int rGlobal = r * model.getRowBlock() + row;
                        int cGlobal = c * model.getColBlock() + col;

                        /* Global coordinates are saved as properties*/
                        cell.getProperties().put("fila", rGlobal);
                        cell.getProperties().put("col", cGlobal);
                        cell.setId("celda_" + rGlobal + "_" + cGlobal);
                        grid.add(cell,col,row);
                        int fila=(int)cell.getProperties().get("fila");
                        int columna=(int)cell.getProperties().get("col");


                        /*To manage the click
                        * Changes colors of selected cells
                        * Returns to original colors once clicked on again*/
                        cell.setOnMouseClicked(event->{

                            // Toggle: si es la misma celda, deseleccionar
                            if (celdaSeleccionada == cell) {
                                limpiarSeleccion(gridContainer);
                                celdaSeleccionada = null;
                                System.out.println("Celda deseleccionada");
                                return; // Salir del evento
                            }

                            // Seleccionar nueva celda
                            sombrearFilaColumna(gridContainer, columna, fila);

                            GridPane padre = (GridPane) cell.getParent();
                            for (Node hijo : padre.getChildren()) {
                                if (hijo instanceof TextField) {
                                    ((TextField) hijo).setStyle("-fx-background-color: #C2CCFF;");
                                }
                            }

                            cell.setStyle("-fx-background-color: #FFBF63;");
                            celdaSeleccionada = cell;

                            System.out.println("Click en fila=" + fila + " col=" + columna);
                        });

                    }
                }
                iniciarTabla(gridContainer);
                eventoMouse(gridContainer );
            }
        }

    }

    /*Method to clean selection*/
    private void limpiarSeleccion(Parent padre) {
        for (Node node : padre.getChildrenUnmodifiable()) {
            if (node instanceof TextField) {
                TextField tf = (TextField) node;
                tf.setStyle(""); // Volver al estilo original del CSS
            } else if (node instanceof Parent) {
                limpiarSeleccion((Parent) node);
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


    private void validarEntrada(Parent padre, int columnaSeleccionada, int filaSeleccionada){
        for (Node node: padre.getChildrenUnmodifiable()){
            if (node instanceof TextField){
                TextField tf=(TextField) node;
                Integer fila=(Integer) tf.getProperties().get("fila");
                Integer col=(Integer) tf.getProperties().get("col");

                if (fila != null && col != null) {
                    // Usar el modelo para validar
                    if (model.hayConflictoEnFilaOColumna(fila, col, tf.getText(),
                            filaSeleccionada, columnaSeleccionada)){
                        tf.setStyle("-fx-background-color:red");
                    }
                }

            } else if (node instanceof Parent) {
                validarEntrada((Parent) node, columnaSeleccionada, filaSeleccionada);
            }
        }
    }
}


