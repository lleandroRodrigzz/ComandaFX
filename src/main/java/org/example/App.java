package org.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.Optional;

/**
 * JavaFX App
 */


public class App extends Application {

    private void fecharAplicacao(Stage stage){
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setHeaderText("Você realmente deseja sair?");
        alerta.setContentText("Clique em OK para confirmar ou Cancelar para permanecer na aplicação.");

        Optional<ButtonType> result = alerta.showAndWait();
        if (result.get() == ButtonType.OK){
            stage.close();
        }
    }

    @Override
    public void start(Stage stage) {
        Caixa caixa=new Caixa();
        Comanda nova;
        nova=caixa.novaComanda(100);
        nova.addItem(3, caixa.getProdutoId(2));
        nova.addItem(2, caixa.getProdutoId(1));
        nova.addItem(5, caixa.getProdutoId(3));
        nova=caixa.novaComanda(15);
        nova=caixa.novaComanda(33);
        nova=caixa.novaComanda(87);
        nova=caixa.novaComanda(98);
        nova=caixa.novaComanda(11);

        //Scene scene = new Scene(new ComandaForm(nova, caixa), 545, 510);
        //ScrollPane scrollPane = new ScrollPane(new ScrollPane(new ComandaPainel(caixa)));

        Scene scene = new Scene(new ComandaPainel(caixa),720,640);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            event.consume();
            fecharAplicacao(stage);
        });
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}