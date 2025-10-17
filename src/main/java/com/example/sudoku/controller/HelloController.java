package com.example.sudoku.controller;

import com.example.sudoku.model.SudokuModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;


public class HelloController {
    Random random=new Random();

    @FXML
    private GridPane gridContainer;
    @FXML
    private Button nuevoJuego;

    private SudokuModel model;

    @FXML
    public void initialize(){
        model = new SudokuModel();
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

                        /* se agregan todos los 36 textfield al sus respectivos bloques*/
                        grid.add(cell,col,row);
                        int fila=(int)cell.getProperties().get("fila");
                        int columna=(int)cell.getProperties().get("col");


                        /* evento que al hacer click pinta el bloque y la celda seleccionada */
                         onMaouseClick(gridContainer,cell, columna, fila);

                    }
                }
                eventoMouse(gridContainer );
                validarEntrada(gridContainer);

            }
        }
    }


    private void sombrearFilaColumna(Parent padre, int columnaSeleccionada, int filaSeleccionada) {
        /* clear previous styles */
        for (TextField tf : todasLasCeldas(padre)) {
                tf.setStyle("");
        }

        /*highlight the row and column of the selected cell*/
        for (TextField tf : todasLasCeldas(padre)) {
                Integer col = (Integer) tf.getProperties().get("col");
                Integer fila=(Integer)  tf.getProperties().get("fila");

                if (col != null && col == columnaSeleccionada) {
                    tf.setStyle("-fx-background-color: #C2CCFF;");

                }
                if (fila!=null && fila==filaSeleccionada){
                    tf.setStyle("-fx-background-color: #C2CCFF;");
                }

        }
    }

    @FXML
    private void onButtonPlay() {
        if (nuevoJuego.getText().equals("Nuevo Juego")) {
            model=new SudokuModel(); /*se genera una nueva tabla si el texto del boton cambia a nuevo juego*/
            limpiarTabla(gridContainer);
            iniciarTabla(gridContainer);

        } else {
            iniciarTabla(gridContainer);
            nuevoJuego.setText("Nuevo Juego");
            nuevoJuego.setStyle("-fx-pref-width:150;");
            obtenerValorReal(gridContainer);
        }
    }

      private void limpiarTabla(Parent root) {
        for (TextField tf : todasLasCeldas(root)) {
            tf.setText("");
            tf.setEditable(true);
            tf.getStyleClass().remove("given");
            tf.setStyle("");
        }
    }
    @FXML
    private void onButtonReset() {
        for (TextField tf : todasLasCeldas(gridContainer)) {
            Integer fila = (Integer) tf.getProperties().get("fila");
            Integer col  = (Integer) tf.getProperties().get("col");
            if (fila == null || col == null) continue;

            if (model.esCeldaEditable(fila, col)) {
                tf.setText("");  /* borra lo escrito por el usuario*/
                tf.setStyle("");  /* quita conflictos*/
            }
        }
    }

    @FXML
    private void onPistaParaUsuario( ){
        pistaParaUsuario(gridContainer);
    }

    private void pistaParaUsuario(Parent padre) {
        List<TextField> celdasVacias = new ArrayList<>();

          for (Node nodo : todasLasCeldas(padre)) {
            if (nodo instanceof TextField tf) {
                String texto = tf.getText();
                if (texto == null || texto.isEmpty()) {
                    celdasVacias.add(tf);
                }
            }
          }

        /* Si solo queda una celda vacía, no dar pista y el usuario la pueda llenar*/
        if (celdasVacias.size() <= 1) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sin más pistas disponibles");
            alerta.setHeaderText(null);
            alerta.setContentText("Solo queda una celda vacía.\n¡Debes completarla tú mismo!");
            alerta.showAndWait();
            return;
        }

        Random rand= new Random();
        TextField celdaSeleccionada =celdasVacias.get(rand.nextInt(celdasVacias.size()));

        Integer fila =(Integer)celdaSeleccionada.getProperties().get("fila");
        Integer col =(Integer)celdaSeleccionada.getProperties().get("col");

        int valorCorrecto= model.getValorSolucion(fila, col);
        celdaSeleccionada.setText(String.valueOf(valorCorrecto));

        celdaSeleccionada.setStyle(
                        "-fx-effect: dropshadow(gaussian, rgba(0,180,60,0.6), 12, 0.0, 0, 4);\n" +
                        "-fx-border-color: #001A69;\n" +
                        "-fx-border-width:1;"+
                        "-fx-background-color:#E0FFE7;"
        );
    }


    /*esta funcion me servira PARA obtener el valor real si el usuario escribe  mal o no la celda que acaba de editar*/
   private void obtenerValorReal(Parent padre) {
       for (TextField tf : todasLasCeldas(padre)) {

           tf.setOnKeyReleased(event -> {
               Integer fila= (Integer) tf.getProperties().get("fila");
               Integer col= (Integer) tf.getProperties().get("col");
               if (fila==null || col==null) return;

               String valorIngresado= tf.getText()== null ? "" : tf.getText().trim();
               if (valorIngresado.isEmpty()) {
                   tf.setStyle(""); /* limpiar si se borran*/
                   return;
               }



              if (model.esValorCorrecto(fila, col, valorIngresado)) {
                   tf.setStyle("-fx-background-color: #C6F6C6;");
               } else {
                   tf.setStyle("-fx-background-color: #FFC0B8;");
               }
           });
       }
   }



    private void iniciarTabla(Parent padre){
        for (TextField tf: todasLasCeldas(padre)){
                Integer col=(Integer) tf.getProperties().get("col");
                Integer fila=(Integer) tf.getProperties().get("fila");

                if (fila == null || col == null) continue;
                /* Obtener valor del modelo*/
                int v = model.getValorEn(fila, col);

                /* Actualizar vista*/
                tf.setText(v==0? "" : Integer.toString(v));
                tf.setEditable(model.esCeldaEditable(fila, col));

                tf.getStyleClass().remove("given");
                if (model.esCeldaGiven(fila, col) && !tf.getStyleClass().contains("given")){
                    tf.getStyleClass().add("given");
                }
        }
    }


    /* funtion to change the color textfields when the mouse is over them */
    private void eventoMouse(Parent padre){
        for (TextField tf: todasLasCeldas(padre)){
            tf.setOnMouseEntered(event->{
                tf.getStyleClass().add("hover");
            });
            tf.setOnMouseExited(event->{
                tf.getStyleClass().remove("hover");
            });

        }
    }

    private void onMaouseClick(Parent padre, TextField cell,int columna, int fila){
        cell.setOnMouseClicked(event->{
            /*funcion para resaltar celdas*/
            sombrearFilaColumna(padre,columna,fila);
            System.out.println("Click en fila=" + fila + " col=" + columna);
            GridPane padree=(GridPane) cell.getParent();

            for (TextField tf: todasLasCeldas(padree)){
                tf.setStyle("-fx-background-color: #C2CCFF;");
            }
            cell.setStyle("-fx-background-color: #FFBF63;");

        });
    }


    private void validarEntrada(Parent padre) {
        for (TextField tf : todasLasCeldas(padre)) {
            /* Evitar adjuntar dos veces el handler*/
            if (Boolean.TRUE.equals(tf.getProperties().get("validador"))) continue;
            tf.getProperties().put("validador", true);

            tf.setOnKeyReleased(event -> {
                String v = tf.getText();
                if (v == null) v = "";
                v = v.trim();

                Integer r0 = (Integer) tf.getProperties().get("fila");
                Integer c0 = (Integer) tf.getProperties().get("col");
                if (r0 == null || c0 == null) return;

                /* Limpiar colores anteriores en fila/col/bloque del entorno de esta celda*/
                limpiarEntorno(padre, r0, c0);

                if (v.isEmpty()) {
                    tf.setStyle(""); /* si borran el número, quitamos rojo*/
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