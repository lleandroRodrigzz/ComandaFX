package org.example;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Comparator;

public class ComandaPainel extends FlowPane {
    private Caixa caixa;
    private boolean mostrarPagas = true;
    private boolean tamanhoDiminuido = false;

    public ComandaPainel(Caixa caixa) {
        super();
        this.caixa = caixa;
        inicializaComponentes();
        inicializaMenuContexto();
    }

    private void inicializaComponentes() {
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color:#757575;");
        this.setVgap(15);
        this.setHgap(15);
        atualizarPainel();
    }

    private void inicializaMenuContexto(){
        ContextMenu contextMenu = new ContextMenu();
        MenuItem ordenarItem = new MenuItem("Ordenar Comandas");
        ordenarItem.setOnAction(e -> ordenarComandas());
        MenuItem diminuirItem = new MenuItem("Diminuir/Aumentar Tamanho");
        diminuirItem.setOnAction(e -> diminuirTamComandas());
        MenuItem mostrarOcultarItem = new MenuItem("Mostrar/Ocultar Comandas Pagas");
        mostrarOcultarItem.setOnAction(e -> mostrarOcultarPagas());

        contextMenu.getItems().addAll(ordenarItem,diminuirItem,mostrarOcultarItem);

        this.setOnContextMenuRequested(e -> contextMenu.show(this, e.getScreenX(), e.getScreenY()));
    }

    private void ordenarComandas(){
        caixa.getComandas().sort(Comparator.comparingInt(Comanda::getNumero));
        atualizarPainel();
    }

    private void diminuirTamComandas(){
        tamanhoDiminuido = !tamanhoDiminuido;
        for (var node : this.getChildren()) {
            if (node instanceof Button button && !button.getText().equals("+")) {
                if (tamanhoDiminuido) {
                    button.setPrefSize(button.getPrefWidth() / 2, button.getPrefHeight() / 2); //diminui
                } else {
                    button.setPrefSize(button.getPrefWidth() * 2, button.getPrefHeight() * 2); //aumenta
                }
            }
        }
        atualizarPainel();
    }
    private void mostrarOcultarPagas() {
        mostrarPagas = !mostrarPagas;
        atualizarPainel();
    }

    private void atualizarPainel() {
        this.getChildren().clear();

        Button buttonAdd = new Button("+");
        buttonAdd.setPrefSize(tamanhoDiminuido ? 80 : 160, tamanhoDiminuido ? 70 : 140);
        buttonAdd.setStyle("-fx-background-color:#283593;-fx-text-fill:yellow;");
        buttonAdd.setFont(new Font(tamanhoDiminuido ? 30 : 60));
        buttonAdd.setOnAction(e -> novaComanda());
        this.getChildren().add(buttonAdd);

        for (Comanda comanda : caixa.getComandas()) {
            if (!mostrarPagas && "Sim".equals(comanda.getPago())) {
                continue;
            }

            Button button = new Button("" + comanda.getNumero());
            button.setPrefSize(tamanhoDiminuido ? 80 : 160, tamanhoDiminuido ? 70 : 140);
            if (comanda.getItens().isEmpty()) {
                button.setStyle("-fx-background-color:#FDD835;-fx-text-fill:black;");
            } else if ("Sim".equals(comanda.getPago())) {
                button.setStyle("-fx-background-color:#00C853;-fx-text-fill:black;");
            } else {
                button.setStyle("-fx-background-color:#E53935;-fx-text-fill:white;");
            }
            button.setFont(new Font(tamanhoDiminuido ? 24 : 48));
            button.setOnAction(e -> abrirComanda(comanda));
            this.getChildren().add(button);
        }
    }

    private void novaComanda() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setContentText("Informe o numero da comanda");
        String aux = dialog.showAndWait().get();
        Comanda nova = caixa.novaComanda(Integer.parseInt(aux));
        Scene scene = new Scene(new ComandaForm(nova, caixa), 545, 510);
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        this.getChildren().clear();
        atualizarPainel();
    }

    private void abrirComanda(Comanda comanda) {
        Scene scene = new Scene(new ComandaForm(comanda, caixa), 545, 510);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        this.getChildren().clear();
        atualizarPainel();
    }
}
