package com.example.sudoku;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class HelloController {

    //variables


    @FXML
    private GridPane gridContainer;

    private GridPane makeBloque(int row, int col){
        GridPane gp=new GridPane();
        gp.getStyleClass().add("grid-board");
        return gp;
    }
    @FXML
    public void initialize(){
        System.out.println("initialize is ok");


        for (int r=0; r<3; r++){
            for (int c=0; c<2; c++){
                GridPane grid=makeBloque(r,c);
                gridContainer.add(grid,c,r);

                for (int row=0; row<2; row++){
                    for (int col=0; col<3; col++){
                        TextField cell=makeCell(r,c);
                        grid.add(cell,col,row);
                    }
                }




            }
        }



    }



    private TextField makeCell(int row, int col){
        TextField tf=new TextField();
        tf.getStyleClass().add("cell");
        return tf;
    }


}
