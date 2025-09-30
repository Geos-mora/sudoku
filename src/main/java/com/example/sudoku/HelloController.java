package com.example.sudoku;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class HelloController {

    //variables
    private int rowGrid=3;
    private int colGrid=2;
    private int rowCell=2;
    private int colCell=3;


    @FXML
    private GridPane gridContainer;


    @FXML
    public void initialize(){
        System.out.println("initialize is ok");


        for (int r=0; r<rowGrid; r++){
            for (int c=0; c<colGrid; c++){
                GridPane grid=makeBloque(r,c);
                gridContainer.add(grid,c,r);


                for (int row=0; row<rowCell; row++){
                    for (int col=0; col<colCell; col++){
                        TextField cell=makeCell(r,c);
                        grid.add(cell,col,row);
                        cell.setOnMouseClicked(event->{
                            GridPane padre=(GridPane) cell.getParent();

                            for (Node hijo: padre.getChildren()){
                                 if (hijo instanceof TextField){
                                     TextField tf=(TextField) hijo;
                                     tf.setStyle("-fx-background-color: lightblue;");
                                 }
                            }
                            cell.setStyle("-fx-background-color: #FFBF63;");
                        });

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
