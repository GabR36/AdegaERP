package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.entities.Produto;
import org.example.entities.Produto_Vendido;
import org.example.entities.Venda;
import org.example.services.VendaService;
import org.example.util.AbridorJanela;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;


@Controller
public class VendasController implements Initializable {

    @Autowired
    private AbridorJanela abridorJanela;

    @FXML
    private TableView<Venda> tView_Vendas;
    @FXML
    private TableView<Produto_Vendido> tView_ProdutosVendidos;
    @FXML
    private TableColumn<Venda, Integer> vendasIdCol;
    @FXML
    private TableColumn<Venda, Float> vendasValorFinalCol;
    @FXML
    private TableColumn<Venda, String> vendasFormaPagCol;
    @FXML
    private TableColumn<Venda, Date> vendasDataCol;
    @FXML
    private TableColumn<Venda, Float> vendasTotalCol;

    @Autowired
    private VendaService vendaService;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configuração das colunas
        vendasIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        vendasValorFinalCol.setCellValueFactory(new PropertyValueFactory<>("valor_final"));
        vendasFormaPagCol.setCellValueFactory(new PropertyValueFactory<>("forma_pagamento"));
        vendasDataCol.setCellValueFactory(new PropertyValueFactory<>("data"));
        vendasTotalCol.setCellValueFactory(new PropertyValueFactory<>("total"));


        tView_Vendas.widthProperty().addListener((obs, oldWidth, newWidth) -> {
            double totalWidth = newWidth.doubleValue();
            vendasValorFinalCol.setPrefWidth(totalWidth * 0.3); //
        });
        // Recupera os dados do banco de dados usando o serviço
        List<Venda> vendas = vendaService.buscarTodos();

        // Converte a lista para ObservableList
        ObservableList<Venda> vendasObservable = FXCollections.observableArrayList(vendas);

        // Associa os dados à TableView
        tView_Vendas.setItems(vendasObservable);
    }
    private void atualizarTabelaVenda() {
        // Busca novamente os dados do banco
        List<Venda> vendaAtualizadas = vendaService.buscarTodos();

        // Atualiza os dados na TableView
        tView_Vendas.setItems(FXCollections.observableArrayList(vendaAtualizadas));
    }

    public void onVenderButtonClick(ActionEvent actionEvent) {
        Stage stage = abridorJanela.abrirNovaJanela("/views/pdv-view.fxml",
                "Ponto de Venda",800,600);
        if (stage != null) {
            // Aguarda o fechamento da janela e atualiza a tabela
            stage.setOnHidden(event -> atualizarTabelaVenda());
        }
    }

}
