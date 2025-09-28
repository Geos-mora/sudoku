package com.example.sudoku;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class HelloController {
    @FXML
    private GridPane gridBoard;
    @FXML
    public void initialize(){
        System.out.println("initialize is ok");
        for (int r=0; r<6; r++){
            for (int c=0; c<6; c++){
                TextField cell=makeCell(r,c);
                gridBoard.add(cell,c,r);
            }
        }
    }

    private TextField makeCell(int row, int col){
        TextField tf=new TextField();
        tf.getStyleClass().add("cell");
        return tf;
    }


}
