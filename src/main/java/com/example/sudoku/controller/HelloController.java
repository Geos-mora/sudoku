package com.example.sudoku.controller;

import com.example.sudoku.model.SudokuModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.*;


public class HelloController {
    Random random=new Random();

    @FXML
    private GridPane gridContainer;
    @FXML
    private Button nuevoJuego;

    private SudokuModel model;

    @FXML
    public void initialize() {
        // Tu código de inicialización actual
        iniciarSudoku();

        // Bloquear la interacción inicial
        gridContainer.setDisable(true);
    }

    @FXML
    public void iniciarSudoku(){
        model = new SudokuModel();
        crearSudoku();
        agregarDeseleccionGlobal();
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
                eventoMouse(gridContainer);

            }
        }
    }

    private int filaSeleccionadaActual = -1;
    private int colSeleccionadaActual = -1;

    private void sombrearFilaColumna(Parent padre, int columnaSeleccionada, int filaSeleccionada) {

        if (filaSeleccionadaActual == filaSeleccionada && colSeleccionadaActual == columnaSeleccionada) {

            for (TextField tf : todasLasCeldas(padre)) {
                tf.setStyle("");
            }
            filaSeleccionadaActual = -1;
            colSeleccionadaActual = -1;
            return;
        }


        filaSeleccionadaActual = filaSeleccionada;
        colSeleccionadaActual = columnaSeleccionada;


        for (TextField tf : todasLasCeldas(padre)) {
            tf.setStyle("");
        }


        for (TextField tf : todasLasCeldas(padre)) {
            Integer col = (Integer) tf.getProperties().get("col");
            Integer fila = (Integer) tf.getProperties().get("fila");

            if (col != null && col == columnaSeleccionada) {
                tf.setStyle("-fx-background-color: #C2CCFF;");
            }
            if (fila != null && fila == filaSeleccionada) {
                tf.setStyle("-fx-background-color: #C2CCFF;");
            }
        }


        for (TextField tf : todasLasCeldas(padre)) {
            Integer tfFila = (Integer) tf.getProperties().get("fila");
            Integer tfCol = (Integer) tf.getProperties().get("col");

            if (tfFila != null && tfCol != null &&
                    tfFila == filaSeleccionada && tfCol == columnaSeleccionada) {

                GridPane padree = (GridPane) tf.getParent();


                for (TextField tfBloque : todasLasCeldas(padree)) {
                    tfBloque.setStyle("-fx-background-color: #C2CCFF;");
                }

                tf.setStyle("-fx-background-color: #FFBF63;");
                break;
            }
        }
    }


    private void agregarDeseleccionGlobal() {
        gridContainer.setOnMouseClicked(event -> {

            Object target = event.getTarget();
            if (!(target instanceof TextField)) {
                for (TextField tf : todasLasCeldas(gridContainer)) {
                    tf.setStyle("");
                }
                filaSeleccionadaActual = -1;
                colSeleccionadaActual = -1;
            }
        });
    }

    @FXML
    private void onButtonPlay() {
        gridContainer.setDisable(false);

        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("New Game");
        alerta.setHeaderText(null);
        alerta.setContentText("Do you want to start a new game?");

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            model = new SudokuModel();
            limpiarTabla(gridContainer);
            iniciarTabla(gridContainer);
            nuevoJuego.setText("New Game");
            nuevoJuego.setStyle("-fx-pref-width:150;");
        } else {
            gridContainer.setDisable(true);
        }

        engancharHandlers(gridContainer);
    }

      private void limpiarTabla(Parent root) {
        for (TextField tf : todasLasCeldas(root)) {
            tf.setText("");
            tf.setEditable(true);
            tf.getStyleClass().remove("given");
            tf.getStyleClass().remove("pistaUsuario");
            tf.setStyle("");
        }
    }

    private void limpiarEstadosVisuales(Parent root) {
        for (TextField tf : todasLasCeldas(root)) {

            tf.getStyleClass().removeAll(
                    "hover", "highlight", "pistaUsuario", "conflict", "selected"
            );

            tf.setStyle("");

            tf.deselect();
        }

        root.requestFocus();
    }

    @FXML
    private void onButtonReset() {
        limpiarEstadosVisuales(gridContainer);

        for (TextField tf : todasLasCeldas(gridContainer)) {
            Integer fila = (Integer) tf.getProperties().get("fila");
            Integer col  = (Integer) tf.getProperties().get("col");
            if (fila == null || col == null) continue;

            if (model.esCeldaEditable(fila, col)) {
                tf.setText("");
            }
        }

        iniciarTabla(gridContainer);
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


        if (celdasVacias.size() <= 1) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Sin más pistas disponibles");
            alerta.setHeaderText(null);
            alerta.setContentText("Solo queda una celda vacía.\n¡Debes completarla tú mismo!");
            alerta.showAndWait();
            return;
        }


        Random rand = new Random();
        TextField celdaSeleccionada = celdasVacias.get(rand.nextInt(celdasVacias.size()));

        Integer fila = (Integer) celdaSeleccionada.getProperties().get("fila");
        Integer col  = (Integer) celdaSeleccionada.getProperties().get("col");

        int valorCorrecto = model.getValorSolucion(fila, col);


        celdaSeleccionada.setText(Integer.toString(valorCorrecto));
        if (!celdaSeleccionada.getStyleClass().contains("hint")) {
            celdaSeleccionada.getStyleClass().add("hint");
        }
    }



    private void winner(Parent padre) {
        boolean todasCeldasLlenas= true;
        boolean todasCeldasCorrectas= true;

        for (TextField tf : todasLasCeldas(padre)) {
            Integer fila= (Integer) tf.getProperties().get("fila");
            Integer col= (Integer) tf.getProperties().get("col");
            if (fila==null || col==null) continue;

            String valor= tf.getText()== null?"" :tf.getText().trim();


            if (valor.isEmpty()) {
                todasCeldasLlenas = false;
                break;
            }


            if (!model.esValorCorrecto(fila, col, valor)) {
                todasCeldasCorrectas=false;
            }
        }


        if (todasCeldasLlenas && todasCeldasCorrectas) {
            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("¡Felicidades!");
            alerta.setHeaderText("¡Has completado el Sudoku!");
            alerta.setContentText("Todas las celdas son correctas.\n¡Eres un verdadero maestro del Sudoku!");
            alerta.showAndWait();
        }
    }

    /*---------------------------------------------------------------------------------------*/
    /*funcion que se encarga de unir los  tf.setOnKeyReleased puesto que al crear los por separado generan conflicto*/
    private void engancharHandlers(Parent padre) {
        for (TextField tf : todasLasCeldas(padre)) {
            if (Boolean.TRUE.equals(tf.getProperties().get("validador"))) continue;
            tf.getProperties().put("validador", true);


            tf.setTextFormatter(new javafx.scene.control.TextFormatter<>(change -> {
                String nuevoTexto = change.getControlNewText();


                if (nuevoTexto.isEmpty()) {
                    return change;
                }


                if (nuevoTexto.length() > 1) {
                    mostrarErrorValidacion("Solo se permite UN dígito");
                    return null;
                }


                if (!nuevoTexto.matches("[0-9]")) {
                    mostrarErrorValidacion("Solo se permiten números");
                    return null;
                }


                int numero = Integer.parseInt(nuevoTexto);
                if (numero < 1 || numero > 6) {
                    mostrarErrorValidacion("Solo se permiten números del 1 al 6");
                    return null;
                }

                return change;
            }));

            tf.setOnKeyReleased(e -> {
                validarEntrada(tf, padre);
                winner(gridContainer);
            });
        }
    }

    private void mostrarErrorValidacion(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setTitle("Entrada inválida");
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }


    private void validarEntrada(TextField tf, Parent padre) {
        String v = (tf.getText() == null ? "" : tf.getText().trim());
        Integer r0 = (Integer) tf.getProperties().get("fila");
        Integer c0 = (Integer) tf.getProperties().get("col");
        if (r0 == null || c0 == null) return;

        limpiarEntorno(padre, r0, c0);
        if (v.isEmpty()) {
            tf.setStyle("");
            return;
        }

        boolean conflicto = false;


        for (TextField itemTf : todasLasCeldas(padre)) {
            if (itemTf == tf) continue;
            Integer r = (Integer) itemTf.getProperties().get("fila");
            Integer c = (Integer) itemTf.getProperties().get("col");
            if (r == null || c == null) continue;

            boolean mismaFilaColBloque = (r.equals(r0)) || (c.equals(c0)) || mismoBloque(r, c, r0, c0);
            String valOtro = (itemTf.getText() == null ? "" : itemTf.getText().trim());

            if (mismaFilaColBloque && !valOtro.isEmpty() && v.equals(valOtro)) {
                itemTf.setStyle("-fx-background-color: #FFC0B8;");
                tf.setStyle("-fx-background-color: #FFC0B8;");
                conflicto = true;
            }
        }


        if (!conflicto) {
            tf.setStyle("");
        }
    }
    /*------------------------------------------------------------------------------------------------------*/

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

    private void onMaouseClick(Parent padre, TextField cell, int columna, int fila) {
        cell.setOnMouseClicked(event -> {
            sombrearFilaColumna(padre, columna, fila);
        });
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