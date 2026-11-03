
package com.cleiton;

import java.awt.print.PrinterJob;
import java.io.File;
import java.util.Arrays;
import java.util.List;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.printing.PDFPageable;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

public class Main extends Application {

    private final ObservableList<String> arquivosPDF =
            FXCollections.observableArrayList();

    private final ListView<String> listaArquivos =
            new ListView<>(arquivosPDF);

    private final Label mensagem =
            new Label("Selecione uma pasta para carregar os PDFs.");

    private final ComboBox<PrintService> comboImpressoras =
            new ComboBox<>();

    private final Button btnIniciarImpressao =
            new Button("Iniciar impressão");

    private final Button btnSelecionarPasta =
            new Button("Selecionar pasta");

    private final Button btnAtualizarImpressoras =
            new Button("Atualizar impressoras");

    @Override
    public void start(Stage stage) {

        stage.setTitle("ImpressaoLotePDF");

        Label titulo = new Label("Impressão em Lote de PDFs");
        titulo.setStyle(
                "-fx-font-size: 22px; -fx-font-weight: bold;"
        );

        btnIniciarImpressao.setDisable(true);

        btnSelecionarPasta.setOnAction(event -> {
            DirectoryChooser seletor = new DirectoryChooser();
            seletor.setTitle("Selecione a pasta dos PDFs");

            File pasta = seletor.showDialog(stage);

            if (pasta != null) {
                carregarPDFs(pasta);
            }
        });

        btnAtualizarImpressoras.setOnAction(event ->
                carregarImpressoras()
        );

        btnIniciarImpressao.setOnAction(event ->
                iniciarImpressao()
        );

        comboImpressoras.setPrefWidth(300);
        comboImpressoras.setPromptText("Selecione a impressora");

        comboImpressoras.setConverter(
                new javafx.util.StringConverter<PrintService>() {
                    @Override
                    public String toString(PrintService impressora) {
                        return impressora == null
                                ? ""
                                : impressora.getName();
                    }

                    @Override
                    public PrintService fromString(String nome) {
                        return null;
                    }
                }
        );

        comboImpressoras.valueProperty().addListener(
                (observable, anterior, atual) ->
                        atualizarBotaoImpressao()
        );

        HBox botoes = new HBox(
                10,
                btnSelecionarPasta,
                btnIniciarImpressao
        );

        HBox areaImpressora = new HBox(
                10,
                new Label("Impressora:"),
                comboImpressoras,
                btnAtualizarImpressoras
        );

        VBox topo = new VBox(
                15,
                titulo,
                botoes,
                areaImpressora,
                mensagem
        );

        topo.setPadding(new Insets(20));

        Label rodape = new Label();

        rodape.textProperty().bind(
                Bindings.concat(
                        "Documentos encontrados: ",
                        Bindings.size(arquivosPDF)
                )
        );

        BorderPane layout = new BorderPane();
        layout.setTop(topo);
        layout.setCenter(listaArquivos);
        layout.setBottom(rodape);

        BorderPane.setMargin(
                listaArquivos,
                new Insets(0, 20, 10, 20)
        );

        BorderPane.setMargin(
                rodape,
                new Insets(10, 20, 20, 20)
        );

        stage.setScene(new Scene(layout, 850, 500));
        stage.show();

        carregarImpressoras();
    }

    private void carregarPDFs(File pasta) {

        arquivosPDF.clear();

        File[] arquivos = pasta.listFiles(
                arquivo -> arquivo.isFile()
                        && arquivo.getName()
                                .toLowerCase()
                                .endsWith(".pdf")
        );

        if (arquivos == null) {
            mensagem.setText(
                    "Não foi possível acessar a pasta selecionada."
            );
            atualizarBotaoImpressao();
            return;
        }

        List<File> lista = Arrays.asList(arquivos);

        lista.sort(
                (a, b) -> a.getName()
                        .compareToIgnoreCase(b.getName())
        );

        for (File arquivo : lista) {
            arquivosPDF.add(arquivo.getAbsolutePath());
        }

        mensagem.setText(
                arquivosPDF.isEmpty()
                        ? "Nenhum PDF encontrado nesta pasta."
                        : "Pasta carregada com sucesso."
        );

        atualizarBotaoImpressao();
    }

    private void carregarImpressoras() {

        PrintService anterior = comboImpressoras.getValue();

        PrintService[] impressoras =
                PrintServiceLookup.lookupPrintServices(null, null);

        comboImpressoras.getItems().setAll(impressoras);

        if (anterior != null) {
            for (PrintService impressora : impressoras) {
                if (impressora.getName().equals(anterior.getName())) {
                    comboImpressoras.getSelectionModel()
                            .select(impressora);
                    atualizarBotaoImpressao();
                    return;
                }
            }
        }

        for (PrintService impressora : impressoras) {
            if (impressora.getName().equalsIgnoreCase(
                    "IMPRESSORA_MIQ")) {

                comboImpressoras.getSelectionModel()
                        .select(impressora);
                break;
            }
        }

        if (impressoras.length == 0) {
            mensagem.setText(
                    "Nenhuma impressora foi encontrada."
            );
        } else if (comboImpressoras.getValue() == null) {
            mensagem.setText(
                    "Selecione uma impressora para continuar."
            );
        }

        atualizarBotaoImpressao();
    }

    private void atualizarBotaoImpressao() {

        boolean podeImprimir =
                !arquivosPDF.isEmpty()
                && comboImpressoras.getValue() != null;

        btnIniciarImpressao.setDisable(!podeImprimir);
    }

    private void iniciarImpressao() {

        PrintService impressora = comboImpressoras.getValue();

        if (impressora == null || arquivosPDF.isEmpty()) {
            mensagem.setText(
                    "Selecione uma impressora e carregue os PDFs."
            );
            return;
        }

        // Faz uma cópia da lista para o processamento em segundo plano.
        List<String> arquivos = List.copyOf(arquivosPDF);
        String nomeImpressora = impressora.getName();

        btnIniciarImpressao.setDisable(true);
        btnSelecionarPasta.setDisable(true);
        btnAtualizarImpressoras.setDisable(true);
        comboImpressoras.setDisable(true);

        Task<Void> tarefa = new Task<>() {

            @Override
            protected Void call() throws Exception {

                int enviados = 0;
                int erros = 0;
                int total = arquivos.size();

                for (int i = 0; i < total; i++) {

                    if (isCancelled()) {
                        break;
                    }

                    String caminho = arquivos.get(i);
                    File arquivo = new File(caminho);

                    updateMessage(
                            "Processando " + (i + 1) + " de "
                            + total + ": " + arquivo.getName()
                    );

                    updateProgress(i, total);

                    try (PDDocument documento =
                                 Loader.loadPDF(arquivo)) {

                        PrintService impressoraAtual = null;

                        for (PrintService servico :
                                PrintServiceLookup.lookupPrintServices(
                                        null, null)) {

                            if (servico.getName().equals(
                                    nomeImpressora)) {
                                impressoraAtual = servico;
                                break;
                            }
                        }

                        if (impressoraAtual == null) {
                            throw new IllegalStateException(
                                    "Impressora não encontrada: "
                                    + nomeImpressora
                            );
                        }

                        PrinterJob trabalho = PrinterJob.getPrinterJob();
                        trabalho.setPrintService(impressoraAtual);
                        trabalho.setPageable(new PDFPageable(documento));

                        // Envia o documento à fila de impressão.
                        trabalho.print();

                        enviados++;

                    } catch (Exception e) {

                        erros++;

                        System.err.println(
                                "Falha no arquivo " + caminho
                                + ": " + e.getMessage()
                        );
                    }

                    updateProgress(i + 1, total);
                }

                final int totalEnviados = enviados;
                final int totalErros = erros;
                final boolean cancelado = isCancelled();

                Platform.runLater(() -> {
                    if (cancelado) {
                        mensagem.setText(
                                "Processo interrompido. Enviados: "
                                + totalEnviados + "; erros: "
                                + totalErros
                        );
                    } else {
                        mensagem.setText(
                                "Processamento finalizado. Enviados: "
                                + totalEnviados + "; erros: "
                                + totalErros
                        );
                    }
                });

                return null;
            }
        };

        mensagem.textProperty().unbind();
        mensagem.textProperty().bind(tarefa.messageProperty());

        tarefa.setOnSucceeded(event -> finalizarImpressao());
        tarefa.setOnFailed(event -> {
            Throwable erro = tarefa.getException();

            mensagem.textProperty().unbind();
            mensagem.setText(
                    "Erro no processo: "
                    + (erro == null ? "desconhecido" : erro.getMessage())
            );

            finalizarImpressao();
        });

        Thread thread = new Thread(tarefa, "impressao-pdf");
        thread.setDaemon(true);
        thread.start();
    }

    private void finalizarImpressao() {

        mensagem.textProperty().unbind();

        btnSelecionarPasta.setDisable(false);
        btnAtualizarImpressoras.setDisable(false);
        comboImpressoras.setDisable(false);

        atualizarBotaoImpressao();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
