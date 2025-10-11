module com.example.sudoku {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens com.example.sudoku to javafx.fxml;
    exports com.example.sudoku;
    exports com.example.sudoku.view;
    opens com.example.sudoku.view to javafx.fxml;
    exports com.example.sudoku.controller;
    opens com.example.sudoku.controller to javafx.fxml;
}