package org.example;

import javafx.scene.control.Alert;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Comanda {
    private String pago;
    private int id;
    private int numero;
    private LocalDate data;
    private double valor;
    private static int id_seq=1;
    private List<ItemComanda> itens = new ArrayList<>();

    public Comanda() {
        this(id_seq, 0,LocalDate.now(),0,"Não");
        id_seq++;
        itens=new ArrayList<>();
    }

    public List<ItemComanda> getItens() {
        return itens;
    }

    public void addItem(int quant, Produto produto)
    {
        if (pago.equals("Não")){
            ItemComanda item = new ItemComanda(quant, produto);
            itens.add(item);
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ALERTA!");
            alert.setHeaderText("A COMANDA JÁ FOI PAGA");
            alert.showAndWait();
        }
    }
    public Comanda(int id, int numero, LocalDate data, double valor, String pago) {
        this.id=id;
        this.numero=numero;
        this.data = data;
        this.valor = valor;
        this.pago = pago;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getPago() {
        return pago;
    }

    public void setPago(String pago) {
        this.pago = pago;
    }

    public double getValorTotal()
    {
        double valor=0;
        for(ItemComanda item : itens )
            valor+= item.quantidade*item.produto.getValor();
        return valor;
    }

    @Override
    public String toString() {
        String str="***********************";
        str+="\nComanda id:"+id;
        str+="\nnúmero: "+numero;
        str+="\ndata: "+data;
        str+="\n***********************";
        for(ItemComanda item : itens )
            str+="\n"+item.quantidade+" "+item.produto.getNome()+" "+ item.produto.getValor();
        str+="\n***********************";
        str+="\nvalor total R$: "+this.getValorTotal();

        return str;
    }
    //inner class
    static record ItemComanda (int quantidade, Produto produto) {
        @Override
        public String toString() {
            return quantidade+" x "+produto.getNome() + " - " + produto.getValor() + " R$";
        }
    }
}
