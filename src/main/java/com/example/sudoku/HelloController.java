package com.example.sudoku;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SubScene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

public class HelloController {

    //variables
    private int rowGrid=3;
    private int colGrid=2;
    private int rowBlock=2;
    private int colBlock=3;

    @FXML
    private GridPane gridContainer;

    int[][] sudokuTabla = {
            {0, 2, 0, 4, 0, 6},
            {4, 0, 6, 0, 2, 0},
            {0, 3, 0, 5, 0, 1},
            {5, 0, 1, 0, 3, 0},
            {0, 4, 0, 6, 0, 2},
            {6, 0, 2, 0, 4, 0}
    };


    @FXML
    public void initialize(){
        System.out.println("initialize is ok");


        for (int r=0; r<rowGrid; r++){
            for (int c=0; c<colGrid; c++){
                GridPane grid=makeBloque(r,c);
                gridContainer.add(grid,c,r);

                for (int row=0; row<rowBlock; row++){
                    for (int col=0; col<colBlock; col++){
                        TextField cell=makeCell(row,col);

                        int rGlobal = r * rowBlock + row;
                        int cGlobal = c * colBlock + col;

                        // se guardan  las coordenadas globales como propiedades
                        cell.getProperties().put("fila", rGlobal);
                        cell.getProperties().put("col", cGlobal);
                        cell.setId("celda_" + rGlobal + "_" + cGlobal);
                        grid.add(cell,col,row);
                        int fila=(int)cell.getProperties().get("fila");
                        int columna=(int)cell.getProperties().get("col");
                        validarEntrada(gridContainer,sudokuTabla,columna,fila);


                        /**/
                        cell.setOnMouseClicked(event->{


                            /*funcion para resaltar celdas*/
                            sombrearFilaColumna(gridContainer,columna,fila);


                            System.out.println("Click en fila=" + fila + " col=" + columna);
                            GridPane padre=(GridPane) cell.getParent();

                            for (Node hijo: padre.getChildren()){
                                 if (hijo instanceof TextField){
                                     TextField tf=(TextField) hijo;
                                     tf.setStyle("-fx-background-color: #41E0C3;");


                                 }
                            }
                            cell.setStyle("-fx-background-color: #FFBF63;");
                            System.out.println("id: "+cell.getId());
                        });

                    }
                }
                iniciarTabla(gridContainer,sudokuTabla);

                eventoMouse(gridContainer );



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
                    tf.setStyle("-fx-background-color: #41E0C3;");

                }
                if (fila!=null && fila==filaSeleccionada){
                    tf.setStyle("-fx-background-color: #41E0C3;");
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

    private void iniciarTabla(Parent padre, int [][] tabla ){
        for (Node nodo: padre.getChildrenUnmodifiable()){
            if (nodo instanceof TextField){
                TextField tf=(TextField)nodo;
                Integer col=(Integer) tf.getProperties().get("col");
                Integer fila=(Integer) tf.getProperties().get("fila");

                if (fila == null || col == null) continue;

                int v=tabla[fila][col];
                tf.setText(v==0? "" : Integer.toString(v));
                tf.setEditable(v == 0);

                tf.getStyleClass().remove("given");
                if (v!=0 && !tf.getStyleClass().contains("given")){
                    tf.getStyleClass().add("given");
                }

            }else if (nodo instanceof Parent){
                iniciarTabla((Parent) nodo, tabla );
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


    private void validarEntrada(Parent padre, int [][] tabla, int columnaSeleccionada, int filaSeleccionada){
        for (Node nodo : padre.getChildrenUnmodifiable()){
            if (nodo instanceof TextField){
                TextField tf=(TextField) nodo;
                Integer col = (Integer) tf.getProperties().get("col");
                Integer fila=(Integer)  tf.getProperties().get("fila");
             /*   tf.setOnKeyReleased(event->{
                    String r=tf.getText();
                    for (Node cell:
                    )




                });*/
            }else {
                if (nodo instanceof Parent){
                    validarEntrada((Parent) nodo,tabla,columnaSeleccionada,filaSeleccionada);
                }
            }
        }

    }
}



