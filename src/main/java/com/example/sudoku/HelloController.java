package com.example.sudoku;

import javafx.fxml.FXML;
import javafx.scene.Node;
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
    private  int contador=1;

    @FXML
    private GridPane gridContainer;



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

                        cell.setOnMouseClicked(event->{
                            int fila=(int)cell.getProperties().get("fila");
                            int columna=(int)cell.getProperties().get("col");

                            /*funcion para resaltar celdas*/
                            highlightColumn(columna,fila);

                            System.out.println("Click en fila=" + fila + " col=" + columna);
                            GridPane padre=(GridPane) cell.getParent();

                            for (Node hijo: padre.getChildren()){
                                 if (hijo instanceof TextField){
                                     TextField tf=(TextField) hijo;
                                     tf.setStyle("-fx-background-color: lightblue;");



                                 }
                            }
                            cell.setStyle("-fx-background-color: #FFBF63;");
                            System.out.println("id: "+cell.getId());
                        });

                    }
                }




            }
        }


    }


    private void highlightColumn(int columnaSeleccionada, int filaSeleccionada) {
        /* limpiar estilos previos*/
        for (Node bloque : gridContainer.getChildren()) {
            if (bloque instanceof GridPane) {
                GridPane gridHijo = (GridPane) bloque;

                for (Node subHijo : gridHijo.getChildren()) {
                    if (subHijo instanceof TextField) {
                        subHijo.setStyle(""); // limpiar colores
                    }
                }
            }
        }


        for (Node bloque : gridContainer.getChildren()) {
            if (bloque instanceof GridPane) {
                GridPane gridHijo = (GridPane) bloque;

                for (Node subHijo : gridHijo.getChildren()) {
                    if (subHijo instanceof TextField) {
                        TextField tf = (TextField) subHijo;
                        Integer col = (Integer) tf.getProperties().get("col");
                        Integer fila=(Integer)  tf.getProperties().get("fila");
                        if (col != null && col == columnaSeleccionada) {
                            tf.setStyle("-fx-background-color: lightblue;");
                        }
                        if (fila!=null && fila==filaSeleccionada){
                            tf.setStyle("-fx-background-color: lightblue;");
                        }
                    }
                }
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


}
