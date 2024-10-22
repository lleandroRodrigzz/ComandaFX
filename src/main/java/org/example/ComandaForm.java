package org.example;

import javafx.collections.FXCollections;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Optional;

public class ComandaForm extends AnchorPane {
    private Label lbNumComanda, lbDataComanda, lbValorComanda;
    private ComboBox <Produto> cbProdutos;
    private Spinner <Integer> spQuantidade;
    private Button btAdicionar, btConfirmar, btCancelar, btPagar;
    private ListView lvItens;
    private Comanda comanda = null;
    private Caixa caixa = null;
    private Comanda comandaBackup = null;

    public ComandaForm(Comanda comanda, Caixa caixa) {
        super();
        this.comanda = comanda;
        this.caixa = caixa;

        this.comandaBackup = new Comanda(comanda.getId(), comanda.getNumero(), comanda.getData(), comanda.getValor(), comanda.getPago());
        this.comandaBackup.getItens().addAll(comanda.getItens());

        inicializaComponentes();
    }

    private void inicializaComponentes() {
        //criando o painel superior
        AnchorPane painel =new AnchorPane();
        painel.setStyle("-fx-background-color:orange;");
        painel.setLayoutX(10); painel.setLayoutY(10);
        painel.setPrefSize(525,100);
        Label label=new Label("Comanda");
        label.setLayoutX(15);label.setLayoutY(5);label.setFont(new Font(14));label.setStyle("-fx-text-fill:black;");
        lbNumComanda=new Label("0");
        lbNumComanda.setLayoutX(15);lbNumComanda.setLayoutY(20);
        lbNumComanda.setFont(new Font(40));lbNumComanda.setStyle("-fx-text-fill:black;");
        lbDataComanda=new Label("99/99/99");
        lbDataComanda.setLayoutX(20);lbDataComanda.setLayoutY(75);
        lbDataComanda.setFont(new Font(10));lbDataComanda.setStyle("-fx-text-fill:black;");
        Label label2=new Label("R$");
        label2.setLayoutX(460);label2.setLayoutY(20);label2.setFont(new Font(14));label2.setStyle("-fx-text-fill:black;");
        lbValorComanda=new Label("0,00");
        lbValorComanda.setLayoutX(420);lbValorComanda.setLayoutY(30);
        lbValorComanda.setFont(new Font(40));lbValorComanda.setStyle("-fx-text-fill:black;");
        painel.getChildren().addAll(label,lbNumComanda,lbDataComanda,label2,lbValorComanda);
        this.getChildren().add(painel);
        //criando o combobox
        cbProdutos=new ComboBox();
        cbProdutos.setLayoutX(10); cbProdutos.setLayoutY(140); cbProdutos.setPrefWidth(300);
        spQuantidade=new Spinner(1,12,1);//new SpinnerNumberModel(0,0,10,1));
        spQuantidade.setLayoutX(330);spQuantidade.setLayoutY(140); spQuantidade.setPrefWidth(80);
        btAdicionar=new Button("+");
        btAdicionar.setLayoutX(450);btAdicionar.setLayoutY(140);btAdicionar.setPrefWidth(80);
        lvItens=new ListView();
        lvItens.setLayoutX(10);lvItens.setLayoutY(180);lvItens.setPrefSize(525,280);
        btPagar=new Button("Pagar");
        btPagar.setLayoutX(260);
        btPagar.setLayoutY(480);
        btConfirmar=new Button("Confirmar");
        btConfirmar.setLayoutX(330);
        btConfirmar.setLayoutY(480);
        btCancelar=new Button("Cancelar");
        btCancelar.setLayoutX(420);
        btCancelar.setLayoutY(480);
        this.getChildren().addAll(cbProdutos,spQuantidade,btAdicionar,lvItens,btConfirmar,btCancelar,btPagar);
        //gerar eventos
        btAdicionar.setOnAction(e->{adicionarItem();});
        btConfirmar.setOnAction(e->{fecharForm();});
        btPagar.setOnAction(e->{PagarComanda();});
        btCancelar.setOnAction(e->{cancelarAlteracao();});

        //atribuir os valores da comanda para os componentes
        lbNumComanda.setText(""+comanda.getNumero());
        lbDataComanda.setText(comanda.getData().toString());
        lbValorComanda.setText(""+comanda.getValorTotal());
        cbProdutos.setItems(FXCollections.observableList(caixa.getProdutos()));
        lvItens.setItems(FXCollections.observableList(comanda.getItens()));
    }

    private void PagarComanda() {
        if(comanda.getPago().equals("Não")){
            double valorTotalComanda = comanda.getValorTotal();
            TextInputDialog dialog = new TextInputDialog();

            dialog.setTitle("Dialogo Comanda");
            dialog.setHeaderText("Sua comanda deu " + valorTotalComanda + " R$");
            dialog.setContentText("Digite o valor que vai pagar");

            Optional<String> valorPagoOpitional = dialog.showAndWait();
            double valorPago = Double.parseDouble(valorPagoOpitional.get());
            if(valorPago >= valorTotalComanda){
                comanda.setPago("Sim");
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Alerta");
                alert.setHeaderText("Troco do Cliente: " + ((valorTotalComanda - valorPago) * -1) + " R$");
                alert.setContentText("Pagamento Confirmado");
                alert.showAndWait();
            }
            else{
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Alerta");
                alert.setHeaderText("Pagamento Não Realizado");
                alert.setContentText("O valor pago é insuficiente para cobrir o total da comanda.");
                alert.showAndWait();
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Alerta Comanda");
            alert.setHeaderText("Alerta!");
            alert.setContentText("Esta comanda ja foi paga!");
            alert.showAndWait();
        }
    }

    private void cancelarAlteracao(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancelar Edição");
        alert.setHeaderText("Cancelar edição da comanda");
        alert.setContentText("Tem certeza de que deseja cancelar a edição? Todas as alterações não salvas serão perdidas.");

        Optional<ButtonType> botao = alert.showAndWait();

        if(botao.get() == ButtonType.OK && comandaBackup != null){
            comanda.setId(comandaBackup.getId());
            comanda.setNumero(comandaBackup.getNumero());
            comanda.setData(comandaBackup.getData());
            comanda.setValor(comandaBackup.getValor());
            comanda.setPago(comandaBackup.getPago());
            comanda.getItens().clear();
            comanda.getItens().addAll(comandaBackup.getItens());

            Stage stage = (Stage) this.getScene().getWindow();
            stage.close();
        }
    }

    private void fecharForm() {
        this.getScene().getWindow().hide();
    }

    private void adicionarItem() {
        comanda.addItem(spQuantidade.getValue(),cbProdutos.getSelectionModel().getSelectedItem());
        lvItens.setItems(FXCollections.observableList(comanda.getItens()));
        lbValorComanda.setText(""+comanda.getValorTotal());
    }
}
