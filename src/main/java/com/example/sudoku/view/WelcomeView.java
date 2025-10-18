package com.example.sudoku.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeView extends Stage {
    public WelcomeView()throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/sudoku/WelcomeGame.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        this.setTitle("Sudoku");
        this.setScene(scene);
        this.show();
    }


}
