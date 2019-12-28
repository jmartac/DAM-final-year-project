package application;


import application.comunero.Comunero;
import application.comunero.FXML_addComuneroController;
import application.month_selector.FXML_MonthSelectorController;
import com.jfoenix.controls.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.print.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Scale;
import javafx.scene.web.WebView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.converter.DoubleStringConverter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class FXML_mainPanelController implements Initializable {
    @FXML
    private JFXComboBox<String> cbEscogerYearEnt;

    @FXML
    private MenuBar menuBar;

    @FXML
    private AnchorPane mainPane;

    @FXML
    private JFXTabPane tpMain;

    @FXML
    private Tab entradasTab;

    @FXML
    private AnchorPane entradasAnchorPane;

    @FXML
    private JFXComboBox<String> cbComuneroEntrada;

    @FXML
    private JFXTextField tfCantidadEntrada;

    @FXML
    private JFXButton btIntroducirEntrada;

    @FXML
    private Label lbInfoEntrada;

    @FXML
    private JFXRadioButton checkTodosMesesEntradas;

    @FXML
    private ToggleGroup tgMesesEntradas;

    @FXML
    private JFXButton btImprimirEntradas;

    @FXML
    private JFXButton btExportarEntradas;

    @FXML
    private JFXButton btLimpiarTablaEntradas;

    @FXML
    private JFXComboBox<String> cbLimpiarComunero;

    @FXML
    private JFXComboBox<String> cbLimpiarMeses;

    @FXML
    private JFXButton btAddNewMes;

    @FXML
    private TableView<ObservableList> tvEntradas;

    @FXML
    private TableColumn<Comunero, String> tcComuneroEnt;

    @FXML
    private TableColumn<ObservableList, String> tcYearEnt;

    @FXML
    private TableColumn<ObservableList, String> tcMes1Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes2Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes3Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes4Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes5Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes6Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes7Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes8Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes9Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes10Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes11Ent;

    @FXML
    private TableColumn<ObservableList, String> tcMes12Ent;

    @FXML
    private TableColumn<Comunero, String> tcSumaTotalEnt;

    @FXML
    private JFXRadioButton checkMesesConcretosEntradas;

    @FXML
    private JFXButton btMesesConcretosEntradas;

    @FXML
    private Tab salidasTab;

    @FXML
    private AnchorPane SalidasAnchorPane;

    @FXML
    private JFXTextField tfDescripcionSalida;

    @FXML
    private JFXTextField tfCantidadSalida;

    @FXML
    private JFXButton btIntroducirSalida;

    @FXML
    private JFXRadioButton checkTodosMesesSalida;

    @FXML
    private ToggleGroup tgMesesSalidas;

    @FXML
    private JFXRadioButton checkMesesConcretosSalidas;

    @FXML
    private JFXButton btMesesConcretosSalidas;

    @FXML
    private JFXButton btImprimirSalidas;

    @FXML
    private JFXButton btExportarSalidas;

    @FXML
    private JFXButton btLimpiarTablaSalidas;

    @FXML
    private JFXComboBox<String> cbLimpiarSalida;

    @FXML
    private JFXComboBox<String> cbLimpiarMesesSalidas;

    @FXML
    public JFXComboBox<String> cbEscogerYearSal;

    public JFXButton btLimpiarComuneroEntradas;
    public JFXButton btLimpiarMesEntradas;

    public JFXButton btLimpiarMesSalidas;
    public JFXButton btLimpiarSalida;
    @FXML
    public JFXButton btAddNewMesSal;

    @FXML
    private TableView<ObservableList> tvSalidas;

    @FXML
    private Tab balanceEntTab;

    @FXML
    private TableView<ObservableList> tvBalEntradas;

    @FXML
    private Tab registroSalTab;

    @FXML
    private TableView<ObservableList> tvRegistroSalidas;

    @FXML
    private Tab graficosTab;

    @FXML
    private JFXComboBox<String> cbEscogerTipoGrafico;

    @FXML
    private JFXComboBox<String> cbTablaGrafico;

    @FXML
    private JFXComboBox<String> cbContenidoGrafico;

    @FXML
    private AnchorPane apGraficoContainer;

    @FXML
    private Tab comunidadTab;

    @FXML
    private JFXTabPane tpComunidad;

    @FXML
    private Tab tabComuneros;

    @FXML
    private TableColumn<?, ?> tcComunero;

    @FXML
    private TableColumn<?, ?> tcDni;

    @FXML
    private TableColumn<?, ?> tcFinca;

    @FXML
    private TableColumn<?, ?> tcParticipacion;

    @FXML
    private Label lbNumComuneros;

    @FXML
    private Label lbNumFincas;

    @FXML
    private Tab tabPrevision;

    @FXML
    private Tab tabComentarios;

    @FXML
    private JFXButton btAgregarComment;

    @FXML
    private JFXButton btAgregarCambioJunta;

    @FXML
    private JFXButton btAgregarSugerencia;

    @FXML
    private JFXButton btAgregarIncidencia;

    @FXML
    private JFXButton btLimpiarListaComentarios;

    @FXML
    private JFXListView<String> lvComentarios;

    @FXML
    public JFXButton btImprimirBalEntradas;

    @FXML
    public JFXButton btExportarBalEntradas;

    @FXML
    public JFXButton btImprimirRegistroSal;

    @FXML
    public JFXButton btExportarRegistroSal;

    @FXML
    public BarChart<?, ?> graficos_barChart;

    @FXML
    public JFXButton btExportarGrafico;

    @FXML
    public PieChart graficos_pieChart;

    @FXML
    public JFXButton btReiniciarCamposGraficos;

    @FXML
    public JFXButton btAddComunero;

    @FXML
    public JFXButton btEliminarComunero;

    @FXML
    public TableView tvComuneros;

    @FXML
    public Label lbPorcentajeRepartido;

    @FXML
    public JFXListView<String> lvFincas;

    @FXML
    public TextArea taNuevoComentario;

    @FXML
    public Label lbLimiteCaracteres;

    private DataBaseManager dataBaseManager;
    private static ObservableList<String> listaNombresComuneros = FXCollections.observableArrayList();
    private final File desktopDirectory = new File(System.getProperty("user.home") + File.separator + "Desktop");
    private Boolean seHanHechoCambiosEntradas = false;
    private Boolean seHanHechoCambiosSalidas = false;
    private Map<Integer, String> setComentarios;
    private DoubleStringConverter doubleStringConverter = new DoubleStringConverter() {
        @Override
        public Double fromString(String string) {
            try {
                string = string.replaceAll(",", ".");
                return Double.valueOf(string);
            } catch (NumberFormatException ex) {
                return 0.0;
            }
        }
    };

    final ObservableList<String> opcionesChart_Entradas = FXCollections.observableArrayList(
            "Entradas por meses",
            "Total de entradas por comunero");
    final ObservableList<String> opcionesChart_Salidas = FXCollections.observableArrayList(
            "Salidas por meses");
    final ObservableList<String> opcionesChart_EntSal = FXCollections.observableArrayList(
            "Comparación de entradas y salidas");
    final ObservableList<String> opcionesChart_BalEntradas = FXCollections.observableArrayList(
            "Balance de Entradas total");
    final ObservableList<String> opcionesChart_RegSalidas = FXCollections.observableArrayList(
            "Registro de Salidas por descripción");
    final ObservableList<String> tablasDisponibles = FXCollections.observableArrayList(
            "Entradas", "Salidas", "Entradas y Salidas", "Balance de Entradas", "Registro de Salidas");
    final ObservableList<String> tiposGrafico = FXCollections.observableArrayList(
            "Diagrama de Barras", "Diagrama de Sectores");

    /**
     * Initializing method
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tpComunidad.setDisableAnimation(true);
        dataBaseManager = new DataBaseManager();

        //formato para los textfield con datos DOUBLE
        tfCantidadEntrada.setTextFormatter(Extras.getDoubleFormatter());
        tfCantidadSalida.setTextFormatter(Extras.getDoubleFormatter());

        initializeEntradas();
        initializeSalidas();

        btImprimirEntradas.setOnAction(event -> {
            imprimirTabla(tvEntradas);
        });
        btImprimirSalidas.setOnAction(event -> {
            imprimirTabla(tvSalidas);
        });
        btImprimirBalEntradas.setOnAction(event -> {
            imprimirTabla(tvBalEntradas);
        });
        btImprimirRegistroSal.setOnAction(event -> {
            imprimirTabla(tvRegistroSalidas);
        });

        btExportarEntradas.setOnAction(event -> {
            String msg = "Se exportará la tabla con las columnas visibles en el escritorio del sistema.";
            if (mostrarDialogoConfirmacion(msg)) {
                try {
                    exportarTabla(tvEntradas, "ENTRADAS");
                } catch (IOException e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            }
        });
        btExportarSalidas.setOnAction(event -> {
            String msg = "Se exportará la tabla con las columnas visibles en el escritorio del sistema.";
            if (mostrarDialogoConfirmacion(msg)) {
                try {
                    exportarTabla(tvSalidas, "SALIDAS");
                } catch (IOException e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            }
        });
        btExportarBalEntradas.setOnAction(event -> {
            String msg = "Se exportará la tabla del Balance de Entradas mostrada actualmente. El archivo será guardado en el Escritorio.";
            if (mostrarDialogoConfirmacion(msg)) {
                try {
                    exportarTabla(tvBalEntradas, "BALANCE DE ENTRADAS");
                } catch (IOException e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            }
        });
        btExportarRegistroSal.setOnAction(event -> {
            String msg = "Se exportará la tabla del Registro de Salidas mostrada actualmente. El archivo será guardado en el Escritorio.";
            if (mostrarDialogoConfirmacion(msg)) {
                try {
                    exportarTabla(tvRegistroSalidas, "REGISTRO DE SALIDAS");
                } catch (IOException e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            }
        });
        btExportarGrafico.setOnAction(event -> {
            String msg = "Se exportará en el gráfico mostrado actualmente en formato PNG. El archivo será guardado en el Escritorio.";

            if (checkEmptyFieldsGraficos()) {
                if (mostrarDialogoConfirmacion(msg)) {
                    try {
                        if (graficos_barChart.isVisible()) {
                            guardarChartPNG(graficos_barChart);
                        } else {
                            guardarChartPNG(graficos_pieChart);
                        }
                    } catch (Exception e) {
                        mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                    }
                }
            }
        });

        btLimpiarTablaEntradas.setOnAction(event -> {
            String msg = "Se va a reiniciar TODAS las entradas de toda la tabla a 0.0 (en todos los MESES, " +
                    "incluso los que no están visibles " +
                    "en este momento) para TODOS los comuneros. Lo único que se mantendrá serán los comuneros actuales.";

            if (mostrarDialogoConfirmacion(msg)) {
                cbLimpiarMesesSalidas.getSelectionModel().clearSelection();
                ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("ENTRADAS");
                listaColumnas.remove("COMUNERO");
                listaColumnas.remove("TOTAL");

                StringBuilder setDatos = new StringBuilder();
                listaColumnas.forEach(mes -> setDatos.append(mes).append("=").append(0.0).append(","));

                setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                dataBaseManager.limpiarDatosTablaEntera("ENTRADAS", setDatos.toString());
                loadEntradas(false);
            }

        });
        btLimpiarTablaSalidas.setOnAction(event -> {
            String msg = "Se va a reiniciar TODAS las salidas de toda la tabla a 0.0 (en todos los MESES, " +
                    "incluso los que no están visibles " +
                    "en este momento) para TODAS las descripciones. Lo único que se mantendrá serán las descripciones actuales.";
            if (mostrarDialogoConfirmacion(msg)) {
                ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("SALIDAS");
                listaColumnas.remove("DESCRIPCION");
                listaColumnas.remove("TOTAL");

                StringBuilder setDatos = new StringBuilder();
                listaColumnas.forEach(mes -> setDatos.append(mes).append("=").append(0.0).append(","));

                setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                dataBaseManager.limpiarDatosTablaEntera("SALIDAS", setDatos.toString());
                loadSalidas(false);
            }
        });

        btReiniciarCamposGraficos.setOnAction(event -> {
            cbTablaGrafico.setItems(FXCollections.observableArrayList());
            cbTablaGrafico.setItems(tablasDisponibles);
            cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList());
            cbEscogerTipoGrafico.setItems(tiposGrafico);
            cbContenidoGrafico.setItems(FXCollections.observableArrayList());
            cbContenidoGrafico.setItems(opcionesChart_Entradas);
        });

        cbTablaGrafico.setItems(tablasDisponibles);
        cbTablaGrafico.setOnAction(event -> {
            if (cbTablaGrafico.getSelectionModel().getSelectedItem() != null) {
                cbTablaGrafico.setFocusColor(Paint.valueOf("#4059a9"));
                String tablaSeleccionada = cbTablaGrafico.getSelectionModel().getSelectedItem();
                switch (tablaSeleccionada) {
                    case "Entradas":
                        cbContenidoGrafico.setItems(opcionesChart_Entradas);
                        cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList());
                        cbEscogerTipoGrafico.setItems(tiposGrafico);
                        break;
                    case "Salidas":
                        cbContenidoGrafico.setItems(opcionesChart_Salidas);
                        cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList());
                        cbEscogerTipoGrafico.setItems(tiposGrafico);
                        break;
                    case "Entradas y Salidas":
                        cbContenidoGrafico.setItems(opcionesChart_EntSal);
                        cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList());
                        cbEscogerTipoGrafico.setItems(tiposGrafico);
                        break;
                    case "Balance de Entradas":
                        cbContenidoGrafico.setItems(opcionesChart_BalEntradas);
                        cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList());
                        cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList("Diagrama de Barras"));
                        break;
                    case "Registro de Salidas":
                        cbContenidoGrafico.setItems(opcionesChart_RegSalidas);
                        cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList());
                        cbEscogerTipoGrafico.setItems(tiposGrafico);
                        break;
                    default:
                        break;
                }
            }
        });

        cbEscogerTipoGrafico.setItems(tiposGrafico);
        cbEscogerTipoGrafico.setOnAction(event -> {
            if (cbEscogerTipoGrafico.getSelectionModel().getSelectedItem() != null) {
                cbEscogerTipoGrafico.setFocusColor(Paint.valueOf("#4059a9"));
                if (cbTablaGrafico.getSelectionModel().getSelectedItem().equalsIgnoreCase("Balance de Entradas")) {
                    cbEscogerTipoGrafico.setItems(FXCollections.observableArrayList("Diagrama de Barras"));
                } else {
                    cbEscogerTipoGrafico.setItems(tiposGrafico);
                }
                ObservableList<String> list = cbContenidoGrafico.getItems();
                cbContenidoGrafico.setItems(FXCollections.observableArrayList());
                cbContenidoGrafico.setItems(list);
            }
        });

        cbContenidoGrafico.setOnAction(event -> {
            if (cbContenidoGrafico.getSelectionModel().getSelectedItem() != null) {
                cbContenidoGrafico.setFocusColor(Paint.valueOf("#4059a9"));
                final String tablaSeleccionada = cbTablaGrafico.getSelectionModel().getSelectedItem();
                final String tipoGraficoSeleccionado = cbEscogerTipoGrafico.getSelectionModel().getSelectedItem();
                final String contenidoGraficoSeleccionado = cbContenidoGrafico.getSelectionModel().getSelectedItem();
                String yearSeleccionado;

                if (tablaSeleccionada != null && tipoGraficoSeleccionado != null && contenidoGraficoSeleccionado != null) {
                    if (tipoGraficoSeleccionado.equals("Diagrama de Barras")) {
                        graficos_pieChart.setVisible(false);
                        graficos_barChart.setVisible(true);
                        switch (contenidoGraficoSeleccionado) {
                            case "Entradas por meses":
                                yearSeleccionado = cbEscogerYearEnt.getSelectionModel().getSelectedItem();
                                graficos_barChart = generarBarChartSumaPorMes(graficos_barChart, obtenerTablaSeleccionada(tablaSeleccionada), null,
                                        tablaSeleccionada, yearSeleccionado);
                                break;
                            case "Total de entradas por comunero":
                                yearSeleccionado = "todos los meses (desde " + tvEntradas.getColumns().get(1).getText() + ")";
                                graficos_barChart = generarBarChartPorComunero(graficos_barChart, yearSeleccionado);
                                break;
                            case "Salidas por meses":
                                yearSeleccionado = cbEscogerYearSal.getSelectionModel().getSelectedItem();
                                graficos_barChart = generarBarChartSumaPorMes(graficos_barChart, obtenerTablaSeleccionada(tablaSeleccionada), null,
                                        tablaSeleccionada, yearSeleccionado);
                                break;
                            case "Balance de Entradas total":
                                yearSeleccionado = cbEscogerYearEnt.getSelectionModel().getSelectedItem();
                                graficos_barChart = generarBarChartBalance(graficos_barChart, tvBalEntradas, tablaSeleccionada, yearSeleccionado);
                                break;
                            case "Registro de Salidas por descripción":
                                yearSeleccionado = cbEscogerYearSal.getSelectionModel().getSelectedItem();
                                graficos_barChart = generarBarChartBalance(graficos_barChart, tvRegistroSalidas, tablaSeleccionada, yearSeleccionado);
                                break;
                            case "Comparación de entradas y salidas":
                                yearSeleccionado = cbEscogerYearEnt.getSelectionModel().getSelectedItem();
                                graficos_barChart = generarBarChartSumaPorMes(graficos_barChart, tvEntradas, tvSalidas,
                                        tablaSeleccionada, yearSeleccionado);
                                break;
                        }
                    } else { //Diagrama de Sectores
                        graficos_barChart.setVisible(false);
                        graficos_pieChart.setVisible(true);
                        switch (contenidoGraficoSeleccionado) {
                            case "Entradas por meses":
                                yearSeleccionado = cbEscogerYearEnt.getSelectionModel().getSelectedItem();
                                graficos_pieChart = generarPieChartSumaPorMes(graficos_pieChart, tvEntradas, null, tablaSeleccionada, yearSeleccionado);
                                break;
                            case "Total de entradas por comunero":
                                yearSeleccionado = "todos los meses (desde " + tvEntradas.getColumns().get(1).getText() + ")";
                                graficos_pieChart = generarPieChartSumaPorComunero(graficos_pieChart, yearSeleccionado);
                                break;
                            case "Salidas por meses":
                                yearSeleccionado = cbEscogerYearSal.getSelectionModel().getSelectedItem();
                                graficos_pieChart = generarPieChartSumaPorMes(graficos_pieChart, tvSalidas, null, tablaSeleccionada, yearSeleccionado);
                                break;
                            case "Registro de Salidas por descripción":
                                yearSeleccionado = cbEscogerYearSal.getSelectionModel().getSelectedItem();
                                graficos_pieChart = generarPieChartRegistroSal(graficos_pieChart, yearSeleccionado);
                                break;
                            case "Comparación de entradas y salidas":
                                yearSeleccionado = cbEscogerYearEnt.getSelectionModel().getSelectedItem();
                                graficos_pieChart = generarPieChartSumaPorMes(graficos_pieChart, tvEntradas, tvSalidas, tablaSeleccionada, yearSeleccionado);
                                break;
                        }
                    }

                }
            }
        });

        loadComuneros(true);
        loadFincas();
        loadComentarios();
        loadBalanceEntradas(true);
        loadRegistroSalidas(true);

        tpMain.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue == entradasTab && newValue == balanceEntTab && seHanHechoCambiosEntradas) {
                loadBalanceEntradas(false);
                seHanHechoCambiosEntradas = false;
            }

            if (oldValue == salidasTab && newValue == registroSalTab && seHanHechoCambiosSalidas) {
                loadRegistroSalidas(false);
                seHanHechoCambiosSalidas = false;
            }
        });
    }


    private ObservableList<String> getListaDeYears(String tabla) {
        ObservableList<String> list = dataBaseManager.getTablePRAGMA(tabla);
        ObservableList<String> yearsLimpios = FXCollections.observableArrayList();

        list.forEach(o -> {
            if (o.matches(".*\\d{4}")) {
                yearsLimpios.add(o.substring(3));
            }
        });
        list.clear();
        list.add(yearsLimpios.get(0));

        String patron = yearsLimpios.get(0);
        for (int i = 0; i < yearsLimpios.size(); i++) {
            String s = yearsLimpios.get(i);
            if (!s.matches(patron)) {
                patron = patron.concat("|" + s);
                list.add(s);
            }
        }
        FXCollections.reverse(list);
        list.add(0, "Últimos 12 meses");
        return list;
    }

    private void setResizableColumns(TableView<ObservableList> table, boolean resizable) {
        table.getColumns().forEach(col -> {
            col.setResizable(resizable);
        });
        if (table.equals(tvEntradas))
            table.getColumns().get(0).setMinWidth(100);
        else
            table.getColumns().get(0).setMinWidth(150);
    }

    //ENTRADAS
    private void loadEntradas(boolean firstLoad) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        tvEntradas.getColumns().clear();


        Connection connection = dataBaseManager.getConnection();
        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT * FROM Entradas");

            for (int i = 0; (i < resultSet.getMetaData().getColumnCount()); i++) {
                final int j = i;
                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
                if (col.getText().equals("COMUNERO")) {
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, SimpleStringProperty>() {
                        @Override
                        public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            //cada  'param.getValue().get(j)' es un valor en la tabla
                            if (param.getValue().get(j) != null) {
                                try {
                                    String dato = param.getValue().get(j).toString();
                                    return new SimpleStringProperty(dato);
                                } catch (NumberFormatException e) {
        mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                }
                                return null;
                            } else return null;
                        }
                    });
                } else { //COLUMNAS CON VALORES NUMÉRICOS
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Double>, SimpleDoubleProperty>() {
                        @Override
                        public SimpleDoubleProperty call(TableColumn.CellDataFeatures<ObservableList, Double> param) {
                            //cada  'param.getValue().get(j)' es un valor en la tabla
                            if (param.getValue().get(j) != null) {
                                try {
                                    Double dato = Double.valueOf(param.getValue().get(j).toString());
                                    return new SimpleDoubleProperty(dato);
                                } catch (NumberFormatException e) {
        mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                }
                                return null;
                            } else return null;
                        }
                    });
                    if (!col.getText().equalsIgnoreCase("TOTAL")) {
                        col.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverter));
                    }

                    col.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) event -> {
                        Double value = (Double) (event.getNewValue() != null ? event.getNewValue() : event.getOldValue());
                        if (value != null) {
                            String newValue = event.getNewValue().toString();
                            String columnName = event.getTableColumn().getText();
                            String rowKey = event.getRowValue().toString();
                            rowKey = rowKey.substring(1, rowKey.indexOf(","));

                            StringBuilder setDatos = new StringBuilder().append(columnName).append("=").append(newValue);

                            dataBaseManager.actualizarComuneroEntradas(rowKey, setDatos.toString());
                            seHanHechoCambiosEntradas = true;
                        }
                        loadEntradas(false);
                        tvEntradas.requestFocus();
                    });
                }

                col.setResizable(false);
                tvEntradas.getColumns().addAll(col);

            }

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (i == 1) {
                        row.add(resultSet.getString(i));
                    } else {
                        row.add(String.valueOf(resultSet.getDouble(i)));
                    }
                }
                data.add(row);
            }

            tvEntradas.setItems(data);
            resultSet.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }

        try {
            connection.close();
            dataBaseManager.reiniciarConnection();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }

        //Mostrar la tabla en un periodo concreto
        if (firstLoad)
            mostrarUltimoYearEntradas();
        else {
            String yearSeleccionado = cbEscogerYearEnt.getSelectionModel().getSelectedItem();
            if (yearSeleccionado.equalsIgnoreCase("Últimos 12 meses")) {
                mostrarUltimoYearEntradas();
            } else {
                mostrarMesesPerYearEntradas(yearSeleccionado);
            }
        }

    }

    //ENTRADAS
    private void mostrarMesesPerYearEntradas(String year) {
        setResizableColumns(tvEntradas, true);
        tvEntradas.getColumns().forEach(observableListTableColumn -> {
            String name = observableListTableColumn.getText();
            if (name.contains(year))
                observableListTableColumn.setVisible(true);
            else observableListTableColumn.setVisible(false);
        });
        //columnas Comunero y TOTAL
        tvEntradas.getColumns().get(0).setVisible(true);
        tvEntradas.getColumns().get(tvEntradas.getColumns().size() - 1).setVisible(true);
        valueToColumnTOTALEntradas();
        setResizableColumns(tvEntradas, false);
    }

    //ENTRADAS
    private void mostrarUltimoYearEntradas() {
        setResizableColumns(tvEntradas, true);
        ObservableList<TableColumn> soloMColMeses = FXCollections.observableArrayList();

        tvEntradas.getColumns().forEach(observableListTableColumn -> {
            String name = observableListTableColumn.getText();
            if (name.matches(".*\\d{4}"))
                soloMColMeses.add(observableListTableColumn);
        });

        FXCollections.reverse(soloMColMeses);
        for (int i = 0; i < soloMColMeses.size(); i++) {
            if (i >= 12) soloMColMeses.get(i).setVisible(false);
            else soloMColMeses.get(i).setVisible(true);
        }
        valueToColumnTOTALEntradas();
        setResizableColumns(tvEntradas, false);
    }

    //ENTRADAS
    private void valueToColumnTOTALEntradas() {
        tvEntradas.getItems().forEach(rowOL -> {
            boolean visible;
            Double suma = 0.0;
            int size = rowOL.size() - 1;
            DoubleStringConverter sc = new DoubleStringConverter();

            for (int colIndex = 1; colIndex < size; colIndex++) {
                visible = tvEntradas.getColumns().get(colIndex).isVisible();
                if (rowOL.get(colIndex) != null && visible) {
                    String num = rowOL.get(colIndex).toString();


                    suma += sc.fromString(num);
                }
            }

            rowOL.set(rowOL.size() - 1, suma);

        });
    }

    //ENTRADAS
    private boolean checkEmptyFieldsEntradas() {
        if (cbComuneroEntrada.getSelectionModel().isEmpty()) {
            cbComuneroEntrada.requestFocus();
            return false;
        } else if (tfCantidadEntrada.getText().trim().isEmpty()) {
            tfCantidadEntrada.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    //ENTRADAS
    @FXML
    public void handleOnIntroducirEntradas(ActionEvent event) {
        String comuneroIntroducir = cbComuneroEntrada.getSelectionModel().getSelectedItem();
        String cantidadIntroducir = tfCantidadEntrada.getText();
        ObservableList<String> mesesSeleccionados = FXCollections.observableArrayList();
        StringBuilder setDatos = new StringBuilder();

        JFXRadioButton rbSeleccionado = (JFXRadioButton) tgMesesEntradas.getSelectedToggle();
        if (checkEmptyFieldsEntradas()) {
            boolean operacionCorrecta = false;
            try {
                if (rbSeleccionado.getId().equals("checkTodosMesesEntradas")) {
                    ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("ENTRADAS");
                    listaColumnas.remove("COMUNERO");
                    listaColumnas.remove("TOTAL");

                    listaColumnas.forEach(mes -> {
                        setDatos.append(mes).append("=").append(cantidadIntroducir).append(",");
                    });

                    setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                    dataBaseManager.actualizarComuneroEntradas(comuneroIntroducir, setDatos.toString());
                    operacionCorrecta = true;
                } else {
                    if (Extras.getMonthSelectorData() instanceof ObservableList) {
                        //noinspection unchecked
                        mesesSeleccionados = (ObservableList<String>)
                                Extras.getMonthSelectorData();

                        mesesSeleccionados.forEach(mes -> {
                            setDatos.append(mes).append("=").append(cantidadIntroducir).append(",");
                        });

                        setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                        dataBaseManager.actualizarComuneroEntradas(comuneroIntroducir, setDatos.toString());
                        operacionCorrecta = true;
                    } else if (Extras.getMonthSelectorData() != null) {
                        //noinspection unchecked
                        HashMap<Integer, String> intervaloSeleccionado = (HashMap<Integer, String>)
                                Extras.getMonthSelectorData();

                        int intervalo = 0;
                        for (int key : intervaloSeleccionado.keySet()) {
                            intervalo = key;
                        }

                        String nombrePrimeraColumna = intervaloSeleccionado.get(intervalo);

                        ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("ENTRADAS");
                        int indexPrimeraColumna = listaColumnas.indexOf(nombrePrimeraColumna);

                        for (int index = indexPrimeraColumna; index < listaColumnas.size(); index += intervalo) {
                            if (!listaColumnas.get(index).equalsIgnoreCase("TOTAL")) {
                                mesesSeleccionados.add(listaColumnas.get(index));
                            }
                        }

                        mesesSeleccionados.forEach(columna ->
                                setDatos.append(columna).append("=").append(cantidadIntroducir).append(","));

                        setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                        dataBaseManager.actualizarComuneroEntradas(comuneroIntroducir, setDatos.toString());
                        operacionCorrecta = true;
                    } else {
                        mostrarDialogoError("No ha seleccionado ninguna opción en el apartado de" +
                                " \"Seleccionar meses concretos...\"");
                        rbSeleccionado.requestFocus();
                        operacionCorrecta = false;
                    }
                }
            } catch (Exception e) {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
            if (operacionCorrecta) {
                tgMesesEntradas.selectToggle(checkTodosMesesEntradas);
                tfCantidadEntrada.setText("");
                Extras.cleanSelectedMonths();
                loadEntradas(false);
                loadBalanceEntradas(false);
                seHanHechoCambiosEntradas = true;
            }
        }
    }

    //ENTRADAS
    @FXML
    public void handleOnAddNewMesEntradas(ActionEvent event) {
        LocalDate ultimaFechaRegistrada = LocalDate.parse(dataBaseManager.getUltimaFecha("ENTRADAS"));
        String colName = Extras.getMonthNameForDB(ultimaFechaRegistrada);
        try {
            dataBaseManager.addNewColumnIntoTable("ENTRADAS", colName, "DOUBLE");
            loadEntradas(false);
            loadBalanceEntradas(false);
            seHanHechoCambiosEntradas = true;
            dataBaseManager.actualizarFechaUltimoMes("ENTRADAS");
            cbEscogerYearEnt.setItems(getListaDeYears("ENTRADAS"));
            cbLimpiarMeses.setItems(dataBaseManager.getListaMesesEntradas());
            mostrarUltimoYearEntradas();
            tvEntradas.refresh();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    //ENTRADAS
    private boolean comprobarCamposEntradas() {
        if (cbComuneroEntrada.getSelectionModel().getSelectedItem() == null) {
            cbComuneroEntrada.requestFocus();
            return false;
        }
        if (tfCantidadEntrada.getText().isEmpty()) {
            tfCantidadEntrada.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * INICIALIZAR la tabla ENTRADAS
     */
    private void initializeEntradas() {
        listaNombresComuneros = dataBaseManager.getListaComuneros();

        //preparar tabla
        loadEntradas(true);


        cbComuneroEntrada.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && comprobarCamposEntradas()) {
                btIntroducirEntrada.requestFocus();
                btIntroducirEntrada.fire();
            }
        });
        tfCantidadEntrada.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && comprobarCamposEntradas()) {
                btIntroducirEntrada.requestFocus();
                btIntroducirEntrada.fire();
            }
        });
        //preparar combo boxes
        cbComuneroEntrada.setItems(listaNombresComuneros);
        cbEscogerYearEnt.setItems(getListaDeYears("ENTRADAS"));
        cbEscogerYearEnt.setVisibleRowCount(5);
        cbEscogerYearEnt.setOnAction(event -> {
            SelectionModel selectedOption = cbEscogerYearEnt.getSelectionModel();
            if (selectedOption.getSelectedIndex() == 0 || selectedOption.getSelectedItem() == null) {
                mostrarUltimoYearEntradas();
                btAddNewMes.setDisable(false);
            } else {
                btAddNewMes.setDisable(true);
                mostrarMesesPerYearEntradas(cbEscogerYearEnt.getSelectionModel().getSelectedItem());
            }
            seHanHechoCambiosEntradas = true;
        });
        cbEscogerYearEnt.setValue("Últimos 12 meses");

        //Llevar meses visibles actuales al selector
        checkTodosMesesEntradas.setText("Todos los meses desde " + tvEntradas.getColumns().get(1).getText());
        ObservableList<String> listaMesesAPasar = FXCollections.observableArrayList();
        ObservableList<String> todosLosMeses = FXCollections.observableArrayList();
        tgMesesEntradas.selectedToggleProperty().addListener(observable -> {
            btMesesConcretosEntradas.setDisable(checkTodosMesesEntradas.isSelected());
        });

        //TIENE QUE RECOGER LOS MESES QUE SE ESTÁN MOSTRANDO ACTUALMENTE EN LA TABLA
        btMesesConcretosEntradas.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                listaMesesAPasar.clear();
                todosLosMeses.clear();

                final String actualYear = cbEscogerYearEnt.getSelectionModel().getSelectedItem();
                if (!actualYear.equals("Últimos 12 meses")) {
                    ObservableList<TableColumn<ObservableList, ?>> columnasVisibles = tvEntradas.getVisibleLeafColumns();
                    columnasVisibles.forEach(obsTablaCol -> {
                        if (obsTablaCol.getText().contains(actualYear)) {
                            listaMesesAPasar.add(obsTablaCol.getText());
                        }
                    });

                } else {
                    ObservableList<TableColumn<ObservableList, ?>> columnasVisibles = tvEntradas.getVisibleLeafColumns();
                    columnasVisibles.forEach(obsTablaCol -> {
                        String text = obsTablaCol.getText();
                        if (!text.equalsIgnoreCase("Comunero") && !text.equalsIgnoreCase("total")) {
                            listaMesesAPasar.add(text);
                        }
                    });
                }


                ObservableList<TableColumn<ObservableList, ?>> columnas = tvEntradas.getColumns();
                columnas.forEach(observableListTableColumn -> {
                    String text = observableListTableColumn.getText();
                    if (!text.equalsIgnoreCase("Comunero") && !text.equalsIgnoreCase("total")) {
                        todosLosMeses.add(text);
                    }
                });

                Parent root;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(
                            "application/month_selector/FXML_monthSelector.fxml"));
                    root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Seleccionar meses");
                    stage.setScene(new Scene(root, 488, 604));
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                    stage.getIcons().add(new Image("application/resources/icon_32px.png"));
                    stage.setResizable(false);

                    FXML_MonthSelectorController monthController = loader.getController();
                    monthController.initData(stage, listaMesesAPasar, todosLosMeses);

                    stage.show();
                    //((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (Exception e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            }
        });


        cbLimpiarComunero.setItems(listaNombresComuneros);
        cbLimpiarComunero.setOnAction(event -> {
            if (cbLimpiarComunero.getSelectionModel().getSelectedItem() != null) {
                btLimpiarComuneroEntradas.setDisable(false);
            } else {
                btLimpiarComuneroEntradas.setDisable(true);
            }
        });
        btLimpiarComuneroEntradas.setOnAction(event -> {
            String comunero = cbLimpiarComunero.getSelectionModel().getSelectedItem();
            String msg = "Se va a reiniciar todas las entradas del comunero " +
                    cbLimpiarComunero.getSelectionModel().getSelectedItem() + " a 0.0.";
            if (mostrarDialogoConfirmacion(msg)) {
                ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("ENTRADAS");
                listaColumnas.remove("COMUNERO");
                listaColumnas.remove("TOTAL");

                StringBuilder setDatos = new StringBuilder();
                listaColumnas.forEach(mes -> setDatos.append(mes).append("=").append(0.0).append(","));

                setDatos.deleteCharAt(setDatos.length() - 1);
                dataBaseManager.limpiarDatosComunero(comunero, setDatos.toString());
                loadEntradas(false);
            }
            cbLimpiarComunero.getSelectionModel().clearSelection();
        });

        cbLimpiarMeses.setItems(dataBaseManager.getListaMesesEntradas());
        cbLimpiarMeses.setOnAction(event -> {
            if (cbLimpiarMeses.getSelectionModel().getSelectedItem() != null) {
                btLimpiarMesEntradas.setDisable(false);
            } else {
                btLimpiarMesEntradas.setDisable(true);
            }
        });
        btLimpiarMesEntradas.setOnAction(event -> {
            String mesABorrar = cbLimpiarMeses.getSelectionModel().getSelectedItem();
            String msg = "Se va a reiniciar todas las entradas del mes " +
                    cbLimpiarMeses.getSelectionModel().getSelectedItem() + " a 0.0 para todos los comuneros.";
            if (mostrarDialogoConfirmacion(msg)) {
                dataBaseManager.limpiarDatosMesEntradas(mesABorrar);
                loadEntradas(false);
            }
            cbLimpiarMeses.getSelectionModel().clearSelection();
        });
    }

    //SALIDAS
    private void loadSalidas(boolean firstLoad) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        tvSalidas.getColumns().clear();


        Connection connection = dataBaseManager.getConnection();
        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT * FROM SALIDAS");

            for (int i = 0; (i < resultSet.getMetaData().getColumnCount()); i++) {
                final int j = i;
                TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));


                if (col.getText().equals("DESCRIPCION")) {
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, SimpleStringProperty>() {
                        @Override
                        public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            //cada  'param.getValue().get(j)' es un valor en la tabla
                            if (param.getValue().get(j) != null) {
                                try {
                                    String dato = param.getValue().get(j).toString();
                                    return new SimpleStringProperty(dato);
                                } catch (NumberFormatException e) {
        mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                }
                                return null;
                            } else return null;
                        }
                    });

                    col.setCellFactory(TextFieldTableCell.forTableColumn());

                    col.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) event -> {
                        String value = (String) (event.getNewValue() != null ? event.getNewValue() : event.getOldValue());
                        if (value != null && !value.trim().isEmpty()) {
                            String newValue = event.getNewValue().toString();
                            String columnName = event.getTableColumn().getText();
                            String rowKey = event.getRowValue().toString();
                            rowKey = rowKey.substring(1, rowKey.indexOf(","));

                            StringBuilder setDatos = new StringBuilder().append(columnName).append(" = '").append(newValue).append("'");
                            dataBaseManager.updateSalidas(false, rowKey, setDatos.toString());
                        }
                        loadSalidas(false);
                        loadRegistroSalidas(false);
                        tvSalidas.requestFocus();
                    });
                } else { //COLUMNAS CON VALORES NUMÉRICOS
                    col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Double>, SimpleDoubleProperty>() {
                        @Override
                        public SimpleDoubleProperty call(TableColumn.CellDataFeatures<ObservableList, Double> param) {
                            //cada  'param.getValue().get(j)' es un valor en la tabla
                            if (param.getValue().get(j) != null) {
                                try {
                                    Double dato = Double.valueOf(param.getValue().get(j).toString());
                                    return new SimpleDoubleProperty(dato);
                                } catch (NumberFormatException e) {
        mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                }
                                return null;
                            } else return null;
                        }
                    });

                    if (!col.getText().equalsIgnoreCase("TOTAL")) {
                        col.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverter));
                    }

                    col.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) event -> {
                        Double value = (Double) (event.getNewValue() != null ? event.getNewValue() : event.getOldValue());
                        if (value != null) {
                            String newValue = event.getNewValue().toString();
                            String columnName = event.getTableColumn().getText();
                            String rowKey = event.getRowValue().toString();
                            rowKey = rowKey.substring(1, rowKey.indexOf(","));

                            StringBuilder setDatos = new StringBuilder().append(columnName).append("=").append(newValue);

                            dataBaseManager.updateSalidas(true, rowKey, setDatos.toString());
                            seHanHechoCambiosSalidas = true;
                        }
                        loadSalidas(false);
                        loadRegistroSalidas(false);
                        tvSalidas.requestFocus();
                    });

                }
                col.setResizable(false);
                tvSalidas.getColumns().addAll(col);

            }

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (i == 1) {
                        row.add(resultSet.getString(i));
                    } else {
                        row.add(String.valueOf(resultSet.getDouble(i)));
                    }
                }
                data.add(row);
            }

            tvSalidas.setItems(data);
            resultSet.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }

        try {
            connection.close();
            dataBaseManager.reiniciarConnection();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }

        //Mostrar la tabla en un periodo concreto
        if (firstLoad)
            mostrarUltimoYearSalidas();
        else {
            String yearSeleccionado = cbEscogerYearSal.getSelectionModel().getSelectedItem();
            if (yearSeleccionado.equalsIgnoreCase("Últimos 12 meses")) {
                mostrarUltimoYearSalidas();
            } else {
                mostrarMesesPerYearSalidas(yearSeleccionado);
            }
        }

    }

    //SALIDAS
    private void mostrarMesesPerYearSalidas(String year) {
        setResizableColumns(tvSalidas, true);
        tvSalidas.getColumns().forEach(observableListTableColumn -> {
            String name = observableListTableColumn.getText();
            if (name.contains(year))
                observableListTableColumn.setVisible(true);
            else observableListTableColumn.setVisible(false);
        });
        //columnas Comunero y TOTAL
        tvSalidas.getColumns().get(0).setVisible(true);
        tvSalidas.getColumns().get(tvSalidas.getColumns().size() - 1).setVisible(true);
        valueToColumnTOTALSalidas();
        setResizableColumns(tvSalidas, false);
    }

    //SALIDAS
    private void mostrarUltimoYearSalidas() {
        setResizableColumns(tvSalidas, true);
        ObservableList<TableColumn> soloMColMeses = FXCollections.observableArrayList();

        tvSalidas.getColumns().forEach(observableListTableColumn -> {
            String name = observableListTableColumn.getText();
            if (name.matches(".*\\d{4}"))
                soloMColMeses.add(observableListTableColumn);
        });

        FXCollections.reverse(soloMColMeses);
        for (int i = 0; i < soloMColMeses.size(); i++) {
            if (i >= 12) soloMColMeses.get(i).setVisible(false);
            else soloMColMeses.get(i).setVisible(true);
        }
        valueToColumnTOTALSalidas();
        setResizableColumns(tvSalidas, false);
    }

    //SALIDAS
    private void valueToColumnTOTALSalidas() {
        tvSalidas.getItems().forEach(rowOL -> {
            boolean visible;
            Double suma = 0.0;
            int size = rowOL.size() - 1;
            DoubleStringConverter sc = new DoubleStringConverter();

            for (int colIndex = 1; colIndex < size; colIndex++) {
                visible = tvSalidas.getColumns().get(colIndex).isVisible();
                if (rowOL.get(colIndex) != null && visible) {
                    String num = rowOL.get(colIndex).toString();


                    suma += sc.fromString(num);
                }
            }

            rowOL.set(rowOL.size() - 1, suma);

        });
    }

    //SALIDAS
    private boolean checkEmptyFieldsSalidas() {
        if (tfDescripcionSalida.getText().trim().isEmpty()) {
            tfDescripcionSalida.requestFocus();
            return false;
        } else if (tfCantidadSalida.getText().trim().isEmpty()) {
            tfCantidadSalida.requestFocus();
            return false;
        } else {
            return true;
        }
    }

    //SALIDAS
    @FXML
    public void handleOnIntroducirSalidas(ActionEvent event) {
        String descripcionIntroducir = tfDescripcionSalida.getText();
        String cantidadIntroducir = tfCantidadSalida.getText();
        ObservableList<String> mesesSeleccionados = FXCollections.observableArrayList();
        StringBuilder setDatos = new StringBuilder();

        JFXRadioButton rbSeleccionado = (JFXRadioButton) tgMesesSalidas.getSelectedToggle();
        if (checkEmptyFieldsSalidas()) {
            boolean operacionCorrecta = false;
            try {
                if (rbSeleccionado.getId().equals("checkTodosMesesSalida")) {
                    ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("SALIDAS");
                    listaColumnas.remove("DESCRIPCION");
                    listaColumnas.remove("TOTAL");

                    listaColumnas.forEach(mes -> {
                        setDatos.append(mes).append("=").append(cantidadIntroducir).append(",");
                    });

                    setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                    dataBaseManager.insertNuevosDatosSalidas(descripcionIntroducir, setDatos.toString());
                    operacionCorrecta = true;
                } else {
                    if (Extras.getMonthSelectorData() instanceof ObservableList) {
                        //noinspection unchecked
                        mesesSeleccionados = (ObservableList<String>)
                                Extras.getMonthSelectorData();

                        mesesSeleccionados.forEach(mes -> {
                            setDatos.append(mes).append("=").append(cantidadIntroducir).append(",");
                        });

                        setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                        dataBaseManager.insertNuevosDatosSalidas(descripcionIntroducir, setDatos.toString());
                        operacionCorrecta = true;
                    } else if (Extras.getMonthSelectorData() != null) {
                        //noinspection unchecked
                        HashMap<Integer, String> intervaloSeleccionado = (HashMap<Integer, String>)
                                Extras.getMonthSelectorData();

                        int intervalo = 0;
                        for (int key : intervaloSeleccionado.keySet()) {
                            intervalo = key;
                        }

                        String nombrePrimeraColumna = intervaloSeleccionado.get(intervalo);

                        ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("SALIDAS");
                        int indexPrimeraColumna = listaColumnas.indexOf(nombrePrimeraColumna);

                        for (int index = indexPrimeraColumna; index < listaColumnas.size(); index += intervalo) {
                            if (!listaColumnas.get(index).equalsIgnoreCase("TOTAL")) {
                                mesesSeleccionados.add(listaColumnas.get(index));
                            }
                        }

                        mesesSeleccionados.forEach(columna ->
                                setDatos.append(columna).append("=").append(cantidadIntroducir).append(","));

                        setDatos.deleteCharAt(setDatos.length() - 1); //eliminar ultima coma

                        dataBaseManager.insertNuevosDatosSalidas(descripcionIntroducir, setDatos.toString());
                        operacionCorrecta = true;
                    } else {
                        mostrarDialogoError("No ha seleccionado ninguna opción en el apartado de" +
                                " \"Seleccionar meses concretos...\"");
                        rbSeleccionado.requestFocus();
                        operacionCorrecta = false;
                    }
                }
            } catch (Exception e) {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
            if (operacionCorrecta) {
                tgMesesSalidas.selectToggle(checkTodosMesesSalida);
                tfDescripcionSalida.setText("");
                tfCantidadSalida.setText("");
                Extras.cleanSelectedMonths();
                loadSalidas(false);
                cbLimpiarSalida.setItems(dataBaseManager.getListaDescripciones());
                loadRegistroSalidas(false);
                seHanHechoCambiosSalidas = true;
            }
        }
    }

    //SALIDAS
    @FXML
    public void handleOnAddNewMesSalidas(ActionEvent event) {
        LocalDate ultimaFechaRegistrada = LocalDate.parse(dataBaseManager.getUltimaFecha("SALIDAS"));
        String colName = Extras.getMonthNameForDB(ultimaFechaRegistrada);
        try {
            dataBaseManager.addNewColumnIntoTable("SALIDAS", colName, "DOUBLE");
            loadSalidas(false);
            loadRegistroSalidas(false);
            seHanHechoCambiosSalidas = true;
            dataBaseManager.actualizarFechaUltimoMes("SALIDAS");
            cbEscogerYearSal.setItems(getListaDeYears("SALIDAS"));
            cbLimpiarMesesSalidas.setItems(dataBaseManager.getListaMesesSalidas());

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    //SALIDAS
    @FXML
    public void handleEliminarSalida(ActionEvent event) {
        String msg = "Se va a eliminar la salida seleccionada de todos los meses de forma irreversible. " +
                "Esto también la eliminará de la tabla de Registro de Salida." +
                "\n\n¿Desea continuar?";
        if (tvSalidas.getSelectionModel().getSelectedItem() != null) {
            if (mostrarDialogoConfirmacion(msg)) {
                String salidaSeleccionada = tvSalidas.getSelectionModel().getSelectedItem().get(0).toString();
                dataBaseManager.eliminarSalida(salidaSeleccionada);
                loadSalidas(false);
                cbLimpiarSalida.setItems(dataBaseManager.getListaDescripciones());
                loadRegistroSalidas(false);
            } else {
                tvSalidas.getSelectionModel().clearSelection();
            }
        } else {
            tvSalidas.getSelectionModel().clearSelection();
        }
    }

    //SALIDAS
    private boolean comprobarCamposSalidas() {
        if (tfDescripcionSalida.getText().isEmpty()) {
            tfDescripcionSalida.requestFocus();
            return false;
        }
        if (tfCantidadSalida.getText().isEmpty()) {
            tfCantidadSalida.requestFocus();
            return false;
        }
        return true;
    }

    /**
     * INICIALIZAR la tabla SALIDAS
     */
    private void initializeSalidas() {
        //preparar tabla
        loadSalidas(true);

        tfDescripcionSalida.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && comprobarCamposSalidas()) {
                btIntroducirSalida.requestFocus();
                btIntroducirSalida.fire();
            }
        });
        tfCantidadSalida.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.ENTER) && comprobarCamposSalidas()) {
                btIntroducirSalida.requestFocus();
                btIntroducirSalida.fire();
            }
        });

        cbEscogerYearSal.setItems(getListaDeYears("SALIDAS"));

        cbEscogerYearSal.setVisibleRowCount(5);
        cbEscogerYearSal.setOnAction(event -> {
            SelectionModel selectedOption = cbEscogerYearSal.getSelectionModel();
            if (selectedOption.getSelectedIndex() == 0 || selectedOption.getSelectedItem() == null) {
                mostrarUltimoYearSalidas();
                btAddNewMesSal.setDisable(false);
            } else {
                btAddNewMesSal.setDisable(true);
                mostrarMesesPerYearSalidas(cbEscogerYearSal.getSelectionModel().getSelectedItem());
            }
            seHanHechoCambiosSalidas = true;
        });
        cbEscogerYearSal.setValue("Últimos 12 meses");

        //Llevar meses visibles actuales al selector
        checkTodosMesesSalida.setText("Todos los meses desde " + tvSalidas.getColumns().get(1).getText());
        ObservableList<String> listaMesesAPasar = FXCollections.observableArrayList();
        ObservableList<String> todosLosMeses = FXCollections.observableArrayList();
        tgMesesSalidas.selectedToggleProperty().addListener(observable -> {
            btMesesConcretosSalidas.setDisable(checkTodosMesesSalida.isSelected());
        });


        //TIENE QUE RECOGER LOS MESES QUE SE ESTÁN MOSTRANDO ACTUALMENTE EN LA TABLA
        btMesesConcretosSalidas.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                listaMesesAPasar.clear();
                todosLosMeses.clear();

                final String actualYear = cbEscogerYearSal.getSelectionModel().getSelectedItem();
                if (!actualYear.equals("Últimos 12 meses")) {
                    ObservableList<TableColumn<ObservableList, ?>> columnasVisibles = tvSalidas.getVisibleLeafColumns();
                    columnasVisibles.forEach(obsTablaCol -> {
                        if (obsTablaCol.getText().contains(actualYear)) {
                            listaMesesAPasar.add(obsTablaCol.getText());
                        }
                    });

                } else {
                    ObservableList<TableColumn<ObservableList, ?>> columnasVisibles = tvSalidas.getVisibleLeafColumns();
                    columnasVisibles.forEach(obsTablaCol -> {
                        String text = obsTablaCol.getText();
                        if (!text.equalsIgnoreCase("DESCRIPCION") && !text.equalsIgnoreCase("total")) {
                            listaMesesAPasar.add(text);
                        }
                    });
                }


                ObservableList<TableColumn<ObservableList, ?>> columnas = tvSalidas.getColumns();
                columnas.forEach(observableListTableColumn -> {
                    String text = observableListTableColumn.getText();
                    if (!text.equalsIgnoreCase("DESCRIPCION") && !text.equalsIgnoreCase("total")) {
                        todosLosMeses.add(text);
                    }
                });

                Parent root;
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(
                            "application/month_selector/FXML_monthSelector.fxml"));
                    root = loader.load();
                    Stage stage = new Stage();
                    stage.setTitle("Seleccionar meses");
                    stage.setScene(new Scene(root, 488, 604));
                    stage.initModality(Modality.WINDOW_MODAL);
                    stage.initOwner(((Node) event.getSource()).getScene().getWindow());
                    stage.getIcons().add(new Image("application/resources/icon_32px.png"));
                    stage.setResizable(false);

                    FXML_MonthSelectorController monthController = loader.getController();
                    monthController.initData(stage, listaMesesAPasar, todosLosMeses);

                    stage.show();
                    //((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                } catch (NullPointerException e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            }
        });

        cbLimpiarSalida.setItems(dataBaseManager.getListaDescripciones());
        cbLimpiarSalida.setOnAction(event -> {
            if (cbLimpiarSalida.getSelectionModel().getSelectedItem() != null) {
                btLimpiarSalida.setDisable(false);
            } else {
                btLimpiarSalida.setDisable(true);
            }
        });
        btLimpiarSalida.setOnAction(event -> {
            String descripcion = cbLimpiarSalida.getSelectionModel().getSelectedItem();
            String msg = "Se va a reiniciar todas las salidas de " +
                    cbLimpiarSalida.getSelectionModel().getSelectedItem() + " a 0.0.";

            if (mostrarDialogoConfirmacion(msg)) {
                ObservableList<String> listaColumnas = dataBaseManager.getTablePRAGMA("SALIDAS");
                listaColumnas.remove("DESCRIPCION");
                listaColumnas.remove("TOTAL");

                StringBuilder setDatos = new StringBuilder();
                listaColumnas.forEach(mes -> setDatos.append(mes).append("=").append(0.0).append(","));

                setDatos.deleteCharAt(setDatos.length() - 1);
                dataBaseManager.limpiarDatosDescripcion(descripcion, setDatos.toString());
                loadSalidas(false);
            }
            cbLimpiarSalida.getSelectionModel().clearSelection();
        });

        cbLimpiarMesesSalidas.setItems(dataBaseManager.getListaMesesSalidas());
        cbLimpiarMesesSalidas.setOnAction(event -> {
            if (cbLimpiarMesesSalidas.getSelectionModel().getSelectedItem() != null) {
                btLimpiarMesSalidas.setDisable(false);
            } else {
                btLimpiarMesSalidas.setDisable(true);
            }
        });
        btLimpiarMesSalidas.setOnAction(event -> {
            String mesABorrar = cbLimpiarMesesSalidas.getSelectionModel().getSelectedItem();
            String msg = "Se va a reiniciar todas las salidas del mes " +
                    mesABorrar + " a 0.0 para TODAS las descripciones.";
            if (mostrarDialogoConfirmacion(msg)) {
                dataBaseManager.limpiarDatosMesSalidas(mesABorrar);
                loadSalidas(false);
            }
            cbLimpiarMesesSalidas.getSelectionModel().clearSelection();
        });
    }

    /**
     * Cargar la tabla BALANCE ENTRADAS
     */
    private void loadBalanceEntradas(boolean firstBoot) {
        if (firstBoot) {
            TableColumn colComunero = new TableColumn("COMUNERO");
            TableColumn colCuota = new TableColumn("Cuota Mensual");
            TableColumn colCuota12Meses = new TableColumn("Cuota Anual");
            TableColumn colResultadoYearAnterior = new TableColumn("Resultado balance año anterior");
            TableColumn colEstimacionBalanceActual = new TableColumn("Debe ingresar este año");
            TableColumn colYearAnteriorMasActual = new TableColumn("Lo ingresado realmente este año");
            TableColumn colAFavor = new TableColumn("A FAVOR");
            TableColumn colEnContra = new TableColumn("EN CONTRA");


            colComunero.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            String dato = param.getValue().get(0).toString();
                            return new SimpleStringProperty(dato);
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colCuota.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            String dato = param.getValue().get(1).toString();
                            return new SimpleStringProperty(dato);
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colCuota12Meses.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            return new SimpleStringProperty(param.getValue().get(2).toString());
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colResultadoYearAnterior.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            Double result = Double.valueOf(param.getValue().get(3).toString());
                            if (result > 0) {
                                return new SimpleStringProperty("se pagó " + result.toString() + " de más");
                            } else if (result < 0) {
                                return new SimpleStringProperty("faltó por pagar " + result.toString());
                            } else {
                                return new SimpleStringProperty(result.toString());
                            }
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colEstimacionBalanceActual.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            return new SimpleStringProperty(param.getValue().get(4).toString());
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colYearAnteriorMasActual.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            return new SimpleStringProperty(param.getValue().get(5).toString());
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colAFavor.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            Double result = Double.valueOf(param.getValue().get(6).toString());
                            if (result >= 0) {
                                return new SimpleStringProperty("+" + result.toString());
                            }
                            return new SimpleStringProperty("");
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colEnContra.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, javafx.beans.value.ObservableValue<String>>() {
                @Override
                public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                    if (param.getValue() != null) {
                        try {
                            Double result = Double.valueOf(param.getValue().get(7).toString());
                            if (result < 0) {
                                return new SimpleStringProperty("DEBE " + result.toString());
                            }
                            return new SimpleStringProperty("");
                        } catch (NumberFormatException e) {
mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                        }
                        return null;
                    } else return null;
                }
            });

            colComunero.setMinWidth(150);
            colComunero.setResizable(false);
            colCuota.setMinWidth(100);
            colCuota.setResizable(false);
            colCuota12Meses.setMinWidth(50);
            colCuota12Meses.setResizable(false);
            colResultadoYearAnterior.setMinWidth(225);
            colResultadoYearAnterior.setResizable(false);
            colEstimacionBalanceActual.setMinWidth(160);
            colEstimacionBalanceActual.setResizable(false);
            colYearAnteriorMasActual.setMinWidth(225);
            colYearAnteriorMasActual.setResizable(false);
            colAFavor.setMinWidth(45);
            colAFavor.setResizable(false);
            colEnContra.setMinWidth(45);
            colEnContra.setResizable(false);

            tvBalEntradas.getColumns().add(colComunero);
            tvBalEntradas.getColumns().add(colCuota);
            tvBalEntradas.getColumns().add(colCuota12Meses);
            tvBalEntradas.getColumns().add(colResultadoYearAnterior);
            tvBalEntradas.getColumns().add(colEstimacionBalanceActual);
            tvBalEntradas.getColumns().add(colYearAnteriorMasActual);
            tvBalEntradas.getColumns().add(colAFavor);
            tvBalEntradas.getColumns().add(colEnContra);
        }

        ObservableList<ObservableList> data = FXCollections.observableArrayList();
        Connection connection = dataBaseManager.getConnection();
        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT COMUNERO FROM ENTRADAS");

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();

                //ROW index 0
                row.add(resultSet.getString("COMUNERO"));

                Statement sttmt = connection.createStatement();
                ResultSet rs = sttmt.executeQuery("SELECT CUOTA FROM COMUNEROS WHERE NOMBRE='" + row.get(0) + "'");

                //si rs está cerrado es porque no encuentra el resultado (result query vacía)

                //ROW index 1
                row.add(rs.getString("CUOTA"));

                rs.close();
                sttmt.close();


                //cuota anual
                //ROW index 2
                row.add(String.valueOf((Double.valueOf(row.get(1)) * 12)));


                //balance año anterior
                if (calcularBalancePastYear(row) != null) {
                    //ROW index 3
                    row.add(calcularBalancePastYear(row));
                } else {
                    //ROW index 3
                    row.add("0.0");
                }


                //estimacion balance actual
                //ROW index 4
                row.add(String.valueOf((-(Double.valueOf(row.get(3))) + (Double.valueOf(row.get(2))))));

                //ROW index 5
                row.add(calcularBalanceActualYear(row));

                //ROW index 6
                row.add(String.valueOf((Double.valueOf(row.get(5)) - (Double.valueOf(row.get(4))))));
                //ROW index 7
                row.add(String.valueOf((Double.valueOf(row.get(5)) - (Double.valueOf(row.get(4))))));

                data.add(row);
            }

            tvBalEntradas.setItems(data);
            resultSet.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }

        try {
            connection.close();
            dataBaseManager.reiniciarConnection();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    //BALANCE ENTRADAS
    private String calcularBalancePastYear(ObservableList row) {
        try {
            String comuneroNombre = row.get(0).toString();
            Double loQuePagoPastYear = 0.0;
            final Double cuotaAnual = Double.valueOf(row.get(2).toString());
            double loQueDeberiaHaberPagadoPastYear;

            if (tvEntradas.getColumns().size() > 25) {
                TableColumn primeraColAnoActual = tvEntradas.getVisibleLeafColumn(1);
                int indexPrimera = tvEntradas.getColumns().indexOf(primeraColAnoActual);

                List<TableColumn<ObservableList, ?>> pastYear = tvEntradas.getColumns().subList(indexPrimera - 12, indexPrimera);
                for (ObservableList item : tvEntradas.getItems()) {
                    String comuneroItem = item.get(0).toString();
                    if (comuneroItem.equals(comuneroNombre)) {
                        for (TableColumn<ObservableList, ?> column : pastYear) {
                            loQuePagoPastYear += (Double) column.getCellObservableValue(item).getValue();
                        }
                        break;
                    }
                }

                loQueDeberiaHaberPagadoPastYear = loQuePagoPastYear - cuotaAnual;
                return Double.toString(loQueDeberiaHaberPagadoPastYear);
            }
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    //BALANCE ENTRADAS
    private String calcularBalanceActualYear(ObservableList row) {
        try {
            String comuneroNombre = row.get(0).toString();
            int indexTotal = tvEntradas.getVisibleLeafColumns().size() - 1;

            for (ObservableList item : tvEntradas.getItems()) {
                String comuneroItem = item.get(0).toString();
                if (comuneroItem.equals(comuneroNombre)) {
                    return tvEntradas.getVisibleLeafColumn(indexTotal).getCellObservableValue(item).getValue().toString();

                }
            }

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    /**
     * Cargar la tabla REGISTRO SALIDAS
     */
    private void loadRegistroSalidas(boolean firstLoad) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        Connection connection = dataBaseManager.getConnection();
        ResultSet resultSet;
        try {
            resultSet = connection.createStatement().executeQuery("SELECT * FROM REGISTRO_SALIDAS");

            if (firstLoad) {
                for (int i = 0; (i < resultSet.getMetaData().getColumnCount()); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));

                    //COLUMNA TITULAR SALIDA y OBSERVACIONES
                    if (col.getText().equals("OBSERVACIONES") || col.getText().equals("TITULAR_SALIDA")) {
                        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, SimpleStringProperty>() {
                            @Override
                            public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                                //cada  'param.getValue().get(j)' es un valor en la tabla
                                if (param.getValue().get(j) != null) {
                                    try {
                                        String dato = param.getValue().get(j).toString();
                                        return new SimpleStringProperty(dato);
                                    } catch (NumberFormatException e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                    }
                                    return null;
                                } else return null;
                            }
                        });


                        if (col.getText().equals("OBSERVACIONES")) {
                            col.setCellFactory(TextFieldTableCell.forTableColumn());
                            col.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) event -> {
                                String value = (String) (event.getNewValue() != null ? event.getNewValue() : event.getOldValue());
                                if (value != null && !value.trim().isEmpty()) {
                                    String newValue = event.getNewValue().toString();
                                    String columnName = event.getTableColumn().getText();
                                    String rowKey = event.getRowValue().toString();
                                    rowKey = rowKey.substring(1, rowKey.indexOf(","));

                                    StringBuilder setDatos = new StringBuilder().append(columnName).append("='").append(newValue).append("'");
                                    dataBaseManager.actualizarObservacionRegistroSalidas(rowKey, setDatos.toString());
                                }
                                loadRegistroSalidas(false);
                                tvRegistroSalidas.requestFocus();
                            });
                            col.setMinWidth(750);
                            col.setResizable(false);
                        } else {
                            col.setMinWidth(200);
                            col.setResizable(false);
                        }

                    } else { //COLUMNA TOTAL_12MESES
                        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Double>, SimpleDoubleProperty>() {
                            @Override
                            public SimpleDoubleProperty call(TableColumn.CellDataFeatures<ObservableList, Double> param) {
                                //cada  'param.getValue().get(j)' es un valor en la tabla
                                if (param.getValue().get(j) != null) {
                                    try {
                                        Double dato = Double.valueOf(param.getValue().get(j).toString());
                                        return new SimpleDoubleProperty(dato);
                                    } catch (NumberFormatException e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                    }
                                    return null;
                                } else return null;
                            }
                        });
                        col.setMinWidth(150);
                        col.setResizable(false);

                    }
                    col.setResizable(false);
                    tvRegistroSalidas.getColumns().addAll(col);
                }
            }


            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    if (i == 1 || i == 3) {
                        row.add(resultSet.getString(i));
                    } else {
                        String descripcion = resultSet.getString(1);
                        final ObservableList[] item = {FXCollections.observableArrayList()};
                        tvSalidas.getItems().forEach(observableList -> {
                            if (observableList.get(0).toString()
                                    .equalsIgnoreCase(descripcion)) {
                                item[0] = observableList;
                            }
                        });
                        String total = tvSalidas.getColumns().get(tvSalidas.getColumns().size() - 1)
                                .getCellData(item[0]).toString();

                        row.add(total);
                    }
                }
                data.add(row);
            }

            tvRegistroSalidas.setItems(data);
            resultSet.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        try {
            connection.close();
            dataBaseManager.reiniciarConnection();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    /**
     * Cargar la tabla FINCAS
     */
    private void loadFincas() {
        ObservableList<String> fincas = dataBaseManager.getFincas();
        lvFincas.setItems(fincas);
    }

    /**
     * Cargar la tabla COMUNEROS
     */
    private void loadComuneros(boolean firstLoad) {
        ObservableList<ObservableList> data = FXCollections.observableArrayList();

        ResultSet resultSet;
        try {
            resultSet = dataBaseManager.getContentTablaComuneros();
            if (firstLoad) {
                for (int i = 0; (i < resultSet.getMetaData().getColumnCount()); i++) {
                    final int j = i;
                    TableColumn col = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));

                    //COLUMNA TITULAR SALIDA y OBSERVACIONES
                    if (col.getText().equals("NOMBRE") || col.getText().equals("DNI") || col.getText().equals("FINCA")) {
                        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, SimpleStringProperty>() {
                            @Override
                            public SimpleStringProperty call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                                //cada  'param.getValue().get(j)' es un valor en la tabla
                                if (param.getValue().get(j) != null) {
                                    try {
                                        String dato = param.getValue().get(j).toString();
                                        return new SimpleStringProperty(dato);
                                    } catch (NumberFormatException e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                    }
                                    return null;
                                } else return null;
                            }
                        });

                        col.setCellFactory(TextFieldTableCell.forTableColumn());

                        if (col.getText().equals("FINCA")) {
                            col.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) event -> {
                                String value = (String) (event.getNewValue() != null ? event.getNewValue() : event.getOldValue());
                                if (value != null && !value.trim().isEmpty()) {
                                    String newValue = event.getNewValue().toString();
                                    String oldValue = event.getOldValue().toString();

                                    if (!dataBaseManager.fincaExists(newValue)) {
                                        try {
                                            dataBaseManager.insertFinca(newValue);
                                        } catch (Exception e) {
                                            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                                        }
                                    }

                                    String rowKey = event.getRowValue().toString();
                                    rowKey = rowKey.substring(1, rowKey.indexOf(","));

                                    StringBuilder setDatos = new StringBuilder().append("FINCA = '").append(newValue).append("'");

                                    dataBaseManager.updateComunero(rowKey, setDatos.toString());
                                    dataBaseManager.insertComuneroIntoTable("ENTRADAS", rowKey, null);
                                }

                                loadFincas();
                                loadComuneros(false);
                                tvComuneros.requestFocus();
                            });
                        } else {
                            col.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) event -> {
                                String value = (String) (event.getNewValue() != null ? event.getNewValue() : event.getOldValue());
                                if (value != null && !value.trim().isEmpty()) {
                                    String newValue = event.getNewValue().toString();

                                    String columnName = event.getTableColumn().getText();
                                    String rowKey = event.getRowValue().toString();
                                    rowKey = rowKey.substring(1, rowKey.indexOf(","));

                                    StringBuilder setDatos = new StringBuilder().append(columnName).append("='").append(newValue).append("'");

                                    dataBaseManager.updateComunero(rowKey, setDatos.toString());
                                }

                                loadComuneros(false);
                                loadEntradas(false);
                                loadBalanceEntradas(false);
                                tvComuneros.requestFocus();
                            });
                        }

                    } else { //COLUMNA CUOTA o PARTICIPACIÓN
                        col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, Double>, SimpleDoubleProperty>() {
                            @Override
                            public SimpleDoubleProperty call(TableColumn.CellDataFeatures<ObservableList, Double> param) {
                                //cada  'param.getValue().get(j)' es un valor en la tabla
                                if (param.getValue().get(j) != null) {
                                    try {
                                        Double dato = Double.valueOf(param.getValue().get(j).toString());
                                        return new SimpleDoubleProperty(dato);
                                    } catch (NumberFormatException e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());                                    }
                                    return null;
                                } else return null;
                            }
                        });
                        col.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverter));

                        col.setOnEditCommit((EventHandler<TableColumn.CellEditEvent>) event -> {
                            Double value = (Double) (event.getNewValue() != null ? event.getNewValue() : event.getOldValue());
                            if (value != null) {
                                String newValue = event.getNewValue().toString();
                                String columnName = event.getTableColumn().getText();
                                String rowKey = event.getRowValue().toString();
                                rowKey = rowKey.substring(1, rowKey.indexOf(","));

                                StringBuilder setDatos = new StringBuilder().append(columnName).append("=").append(newValue);

                                dataBaseManager.updateComunero(rowKey, setDatos.toString());
                            }
                            loadComuneros(false);
                            loadBalanceEntradas(false);
                            tvComuneros.requestFocus();
                        });
                    }
                    col.setPrefWidth(129.2);
                    col.setResizable(false);
                    tvComuneros.getColumns().addAll(col);
                }
            }


            double sumaPorcentaje = 0;

            while (resultSet.next()) {
                ObservableList<String> row = FXCollections.observableArrayList();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    row.add(resultSet.getString(i));
                }
                data.add(row);
                sumaPorcentaje += Double.valueOf(row.get(4));
            }

            lbNumComuneros.setText(String.valueOf(data.size()));
            lbPorcentajeRepartido.setText(String.valueOf(sumaPorcentaje));

            if (sumaPorcentaje == 100) {
                lbPorcentajeRepartido.setStyle("-fx-text-fill:green;");
            } else {
                lbPorcentajeRepartido.setStyle("-fx-text-fill:orangered;");
            }

            tvComuneros.setItems(data);

            tvComuneros.setOnMouseClicked(event -> {
                if (tvComuneros.getSelectionModel().getSelectedItem() != null && tvComuneros.isFocused()) {
                    btEliminarComunero.setDisable(false);
                } else {
                    btEliminarComunero.setDisable(true);
                }
            });

            resultSet.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    //COMUNEROS
    @FXML
    public void handleEliminarComunero(ActionEvent event) {
        String msg = "Se va a eliminar al comunero seleccionado. Esto ELIMINARÁ las entradas que pueda tener este comunero," +
                " así como cualquier dato relativo a el mismo." +
                "\n\n¿Desea continuar?";
        if (tvComuneros.getSelectionModel().getSelectedItem() != null) {
            if (mostrarDialogoConfirmacion(msg)) {
                String comuneroName = ((ObservableList) tvComuneros.getSelectionModel().getSelectedItem()).get(0).toString();
                dataBaseManager.eliminarComunero(comuneroName);
                loadComuneros(false);
                loadEntradas(false);
                listaNombresComuneros = dataBaseManager.getListaComuneros();
                cbComuneroEntrada.setItems(listaNombresComuneros);
                cbLimpiarComunero.setItems(listaNombresComuneros);
                loadBalanceEntradas(false);
                btEliminarComunero.setDisable(true);
            } else {
                tvComuneros.getSelectionModel().clearSelection();
                btEliminarComunero.setDisable(true);
            }
        } else {
            btEliminarComunero.setDisable(true);
        }
    }

    //COMUNEROS
    @FXML
    public void handleAddComunero(ActionEvent event) {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(
                    "application/comunero/FXML_addComunero.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Añadir nuevos comuneros");
            stage.setScene(new Scene(root, 804, 588));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(((Node) event.getSource()).getScene().getWindow());
            stage.getIcons().add(new Image("application/resources/icon_32px.png"));
            stage.setResizable(false);

            FXML_addComuneroController addComuneroController = loader.getController();
            addComuneroController.initData(stage);

            stage.showAndWait();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }

        loadFincas();
        loadComuneros(false);
        listaNombresComuneros = dataBaseManager.getListaComuneros();
        cbComuneroEntrada.setItems(listaNombresComuneros);
        cbLimpiarComunero.setItems(listaNombresComuneros);
        loadEntradas(false);
        loadBalanceEntradas(false);
    }

    //COMUNEROS
    @FXML
    public void handleEliminarFinca(ActionEvent event) {
        String msg = "Se va a eliminar la finca seleccionada. Esto ELIMINARÁ de forma irreversible a todos los comuneros " +
                "que tengan asignada esta finca," +
                " así como cualquier dato relativo a el mismo (como, por ejemplo, todas sus ENTRADAS)." +
                "\n\n¿Desea continuar?";
        if (lvFincas.getSelectionModel().getSelectedItem() != null) {
            if (mostrarDialogoConfirmacion(msg)) {
                String fincaSeleccionada = lvFincas.getSelectionModel().getSelectedItem();
                dataBaseManager.eliminarFinca(fincaSeleccionada);
                loadFincas();
                loadComuneros(false);
                loadEntradas(false);
                loadBalanceEntradas(false);
            } else {
                lvFincas.getSelectionModel().clearSelection();
            }
        } else {
            lvFincas.getSelectionModel().clearSelection();
        }
    }

    /**
     * Cargar la tabla COMENTARIOS
     */
    private void loadComentarios() {
        ObservableList<String> comentarios = FXCollections.observableArrayList();

        taNuevoComentario.setTextFormatter(new TextFormatter<>(change -> {
            String textWithNewChanges = change.getControlNewText();
            if (textWithNewChanges.length() <= 300) {
                lbLimiteCaracteres.setVisible(false);
                return change;
            } else {
                lbLimiteCaracteres.setVisible(true);
                return null;
            }
        }));

        setComentarios = dataBaseManager.getComentarios();
        setComentarios.forEach((id, comentario) -> comentarios.add(comentario));

        lvComentarios.setItems(comentarios);

        lvComentarios.setEditable(true);
        lvComentarios.setCellFactory(TextFieldListCell.forListView());
        lvComentarios.setOnEditCommit((ListView.EditEvent<String> event) -> {
            String value = (event.getNewValue());
            String oldValue = lvComentarios.getSelectionModel().getSelectedItem();
            if (value != null) {
                AtomicBoolean seguir = new AtomicBoolean(true);
                setComentarios.forEach((id, comentario) -> {
                    if (comentario.equals(oldValue) && seguir.get()) {
                        dataBaseManager.updateComentario(id, value);
                        seguir.set(false);
                    }
                });

                loadComentarios();
            }
        });
    }

    //COMENTARIOS
    @FXML
    public void handleAddComentario(ActionEvent event) {
        String newComentario = taNuevoComentario.getText();
        if (!newComentario.trim().isEmpty()) {
            dataBaseManager.insertComentario(newComentario);
            loadComentarios();
            taNuevoComentario.setText("");
        } else {
            taNuevoComentario.requestFocus();
        }
    }

    //COMENTARIOS
    @FXML
    public void handleEliminarComentario(ActionEvent event) {
        String comentarioSeleccionado = lvComentarios.getSelectionModel().getSelectedItem();
        if (comentarioSeleccionado != null) {
            AtomicBoolean seguir = new AtomicBoolean(true);
            setComentarios.forEach((id, comentario) -> {
                if (comentario.equals(comentarioSeleccionado) && seguir.get()) {
                    dataBaseManager.eliminarComentario(id);
                }
            });

            loadComentarios();
        }
    }

    //COMENTARIOS
    @FXML
    public void handleEliminarListado(ActionEvent event) {
        String msg = "Se van a ELIMINAR todos los comentarios. Esta acción es irreversible. ¿Está seguro de querer continuar?";
        if (mostrarDialogoConfirmacion(msg)) {
            dataBaseManager.limpiarComentarios();
            loadComentarios();
        }
    }

    //COMENTARIOS
    public void handleCopiarComentario(ActionEvent event) {
        String clipboardString = lvComentarios.getSelectionModel().getSelectedItem();
        final ClipboardContent content = new ClipboardContent();
        content.putString(clipboardString);
        Clipboard.getSystemClipboard().setContent(content);
    }

    //GRÁFICOS e IMP/EXP
    private void guardarChartPNG(Chart chart) {
        WritableImage image = chart.snapshot(new SnapshotParameters(), null);

        File file = new File(desktopDirectory + File.separator + Extras.formatFileName("gráfico") + ".png");

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
        } catch (IOException e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    private void imprimirTabla(TableView<ObservableList> tabla) {
        try {
            PrinterJob printerJob = PrinterJob.createPrinterJob();
            Stage primaryStage = Extras.getPrimaryStage();

            PageLayout pageLayout = Printer.getDefaultPrinter().createPageLayout(Paper.A4, PageOrientation.LANDSCAPE, Printer.MarginType.HARDWARE_MINIMUM);
            double scaleX = pageLayout.getPrintableWidth() / tabla.getBoundsInParent().getWidth() / 1.2;
            double scaleY = pageLayout.getPrintableHeight() / tabla.getBoundsInParent().getHeight() / 1.2;
            Scale scale = new Scale(scaleX, scaleY);

            tabla.getTransforms().add(scale);

            if (printerJob.showPrintDialog(primaryStage.getOwner()) && printerJob.printPage(pageLayout, tabla))
                printerJob.endJob();

            tabla.getTransforms().remove(scale);
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    private void exportarTabla(TableView<ObservableList> tabla, String nombreTabla) throws IOException {
        Workbook workbook = new HSSFWorkbook();
        Sheet spreadSheet = workbook.createSheet(nombreTabla);

        Row row = spreadSheet.createRow(0);

        //cabecera
        for (int j = 0; j < tabla.getVisibleLeafColumns().size(); j++) {
            row.createCell(j).setCellValue(tabla.getVisibleLeafColumns().get(j).getText());
        }

        for (int i = 0; i < tabla.getItems().size(); i++) {
            row = spreadSheet.createRow(i + 1);
            for (int j = 0; j < tabla.getVisibleLeafColumns().size(); j++) {
                if (tabla.getVisibleLeafColumns().get(j).getCellData(i) != null) {
                    row.createCell(j).setCellValue(tabla.getVisibleLeafColumns().get(j).getCellData(i).toString());
                } else {
                    row.createCell(j).setCellValue("");
                }
            }
        }

        String fileName = Extras.formatFileName(nombreTabla);
        FileOutputStream fileOut = new FileOutputStream(desktopDirectory + File.separator + fileName + ".xls");
        workbook.write(fileOut);
        fileOut.close();
    }

    private boolean mostrarDialogoConfirmacion(String msg) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmar acción");
        alert.setHeaderText(msg);
        alert.getDialogPane().setMaxWidth(400);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private boolean mostrarDialogoError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hubo un error");
        alert.setHeaderText(msg);
        alert.getDialogPane().setMaxWidth(400);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private boolean mostrarDialogoInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(msg);
        alert.getDialogPane().setMaxWidth(400);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            return true;
        }
        return false;
    }

    private boolean checkEmptyFieldsGraficos() {
        if (cbTablaGrafico.getSelectionModel().getSelectedItem() == null || cbTablaGrafico.getSelectionModel().getSelectedItem().isEmpty()) {
            cbTablaGrafico.setFocusColor(Paint.valueOf("#ff7c66"));
            cbTablaGrafico.requestFocus();
            return false;
        } else if (cbEscogerTipoGrafico.getSelectionModel().getSelectedItem() == null || cbEscogerTipoGrafico.getSelectionModel().getSelectedItem().isEmpty()) {
            cbEscogerTipoGrafico.setFocusColor(Paint.valueOf("#ff7c66"));
            cbEscogerTipoGrafico.requestFocus();
            return false;
        } else if (cbContenidoGrafico.getSelectionModel().getSelectedItem() == null || cbContenidoGrafico.getSelectionModel().getSelectedItem().isEmpty()) {
            cbContenidoGrafico.setFocusColor(Paint.valueOf("#ff7c66"));
            cbContenidoGrafico.requestFocus();
            return false;
        }
        return true;
    }

    private PieChart generarPieChartSumaPorComunero(PieChart pieChart, String year) {
        try {
            pieChart.setTitle("Entradas por Comunero en: " + year);

            ObservableList<PieChart.Data> chartDataList = FXCollections.observableArrayList();
            ObservableList<String> listaComuneros = dataBaseManager.getListaComuneros();

            listaComuneros.forEach(comunero -> {
                ResultSet entradas = dataBaseManager.getEntradasComuneroDB(comunero);
                double suma = 0.0;
                try {
                    int lastColIndex = entradas.getMetaData().getColumnCount();
                    String colName = "";
                    for (int i = 2; i < lastColIndex; i++) {
                        colName = entradas.getMetaData().getColumnName(i);
                        suma += entradas.getDouble(colName);
                    }
                    chartDataList.add(new PieChart.Data(comunero, suma));
                } catch (Exception e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
                try {
                    entradas.close();
                } catch (Exception e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            });

            pieChart.getData().clear();
            pieChart.setData(chartDataList);

            pieChart.getData().forEach(data -> {
                data.nameProperty().setValue(data.getName() + " " + data.pieValueProperty().getValue());
            });

            return pieChart;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    private PieChart generarPieChartSumaPorMes(PieChart pieChart, TableView table, TableView table2, String tableName, String year) {
        try {
            pieChart.setTitle(tableName + " por mes en: " + year);

            if (table2 != null) {
                tableName = "Entradas";
            }

            ObservableList<PieChart.Data> chartDataList = FXCollections.observableArrayList();
            ObservableList<TableColumn<ObservableList, ?>> columnasVisibles = table.getVisibleLeafColumns();
            double suma = 0.0;
            for (int i = 1; i < columnasVisibles.size() - 1; i++) {
                String colName = columnasVisibles.get(i).getText();

                suma = dataBaseManager.obtenerSumaDatosMes(colName, tableName);
                chartDataList.add(new PieChart.Data(colName, suma));
            }
            pieChart.getData().clear();
            pieChart.setData(chartDataList);

            if (table2 != null) {
                String yearSalidas = cbEscogerYearSal.getSelectionModel().getSelectedItem();
                if (yearSalidas.equalsIgnoreCase(year)) {
                    pieChart.setTitle("Comparación del total de las Entradas y Salidas en: " + year);
                } else {
                    pieChart.setTitle("Comparación del total de las Entradas en " + year + " y Salidas en " + yearSalidas);
                }
                suma = 0.0;
                for (int i = 0; i < chartDataList.size(); i++) {
                    suma += chartDataList.get(i).getPieValue();
                }
                chartDataList.clear();

                columnasVisibles = table2.getVisibleLeafColumns();
                double sumaTable2 = 0.0;
                for (int i = 1; i < columnasVisibles.size() - 1; i++) {
                    String colName = columnasVisibles.get(i).getText();

                    sumaTable2 = dataBaseManager.obtenerSumaDatosMes(colName, "Salidas");
                    chartDataList.add(new PieChart.Data(colName, sumaTable2));
                }
                for (int i = 0; i < chartDataList.size(); i++) {
                    sumaTable2 += chartDataList.get(i).getPieValue();
                }
                chartDataList.clear();
                chartDataList.addAll(new PieChart.Data("Entradas", suma), new PieChart.Data("Salidas", sumaTable2));
                pieChart.setData(chartDataList);
            }
            pieChart.getData().forEach(data -> {
                data.nameProperty().setValue(data.getName() + " " + data.pieValueProperty().getValue());
            });

            return pieChart;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    private PieChart generarPieChartRegistroSal(PieChart pieChart, String year) {
        try {
            pieChart.setTitle("Registro de Salidas por descripción en: " + year);

            ObservableList<PieChart.Data> chartDataList = FXCollections.observableArrayList();
            ObservableList<TableColumn<ObservableList, ?>> columnas = tvRegistroSalidas.getColumns();

            double suma;
            tvRegistroSalidas.getItems().forEach(itemList -> {
                TableColumn col;
                String descripcion = "";
                double cantidadTotal = 0.0;
                for (int i = 0; i < 2; i++) {
                    col = columnas.get(i);
                    if (i == 0) {
                        descripcion = col.getCellObservableValue(itemList).getValue().toString();
                    } else {
                        cantidadTotal = Double.parseDouble(col.getCellObservableValue(itemList).getValue().toString());
                    }
                }
                chartDataList.add(new PieChart.Data(descripcion, cantidadTotal));
            });


            pieChart.getData().clear();
            pieChart.setData(chartDataList);

            pieChart.getData().forEach(data -> {
                data.nameProperty().setValue(data.getName() + " " + data.pieValueProperty().getValue());
            });

            return pieChart;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            return null;
        }
    }

    private BarChart<?, ?> generarBarChartPorComunero(BarChart<?, ?> barChart, String mesInicial) {
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        barChart.setTitle("Entradas por Comunero en: " + mesInicial);
        xAxis.setLabel("Comunero");
        yAxis.setLabel("Cantidad");
        XYChart.Series serie1 = new XYChart.Series();
        serie1.setName("Total Comunero");

        ObservableList<String> listaComuneros = dataBaseManager.getListaComuneros();

        listaComuneros.forEach(comunero -> {
            ResultSet entradas = dataBaseManager.getEntradasComuneroDB(comunero);
            double suma = 0.0;
            try {
                int lastColIndex = entradas.getMetaData().getColumnCount();
                String colName = "";
                for (int i = 2; i < lastColIndex; i++) {
                    colName = entradas.getMetaData().getColumnName(i);
                    suma += entradas.getDouble(colName);
                }
                serie1.getData().add(new XYChart.Data(comunero, suma));
            } catch (Exception e) {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
            try {
                entradas.close();
            } catch (Exception e) {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
        });


        barChart.getData().clear();
        barChart.getData().add(serie1);
        barChart.setAnimated(false);
        return barChart;

    }

    private BarChart<?, ?> generarBarChartSumaPorMes(BarChart<?, ?> barChart, TableView table, TableView table2, String tableName, String year) {
        try {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            barChart.setTitle(tableName + " por mes en: " + year);
            xAxis.setLabel("Mes");
            yAxis.setLabel("Cantidad");

            if (table2 != null) {
                tableName = "Entradas";
            }

            XYChart.Series serie1 = new XYChart.Series();
            serie1.setName(year);

            ObservableList<TableColumn<ObservableList, ?>> columnasVisibles = table.getVisibleLeafColumns();
            double suma = 0.0;
            for (int i = 1; i < columnasVisibles.size() - 1; i++) {
                String colName = columnasVisibles.get(i).getText();
                suma = dataBaseManager.obtenerSumaDatosMes(colName, tableName);
                serie1.getData().add(new XYChart.Data(colName, suma));
            }
            barChart.getData().clear();
            barChart.getData().add(serie1);

            if (table2 != null) {
                String yearSalidas = cbEscogerYearSal.getSelectionModel().getSelectedItem();
                if (yearSalidas.equalsIgnoreCase(year)) {
                    barChart.setTitle("Comparación del total de las Entradas y Salidas en: " + year);
                } else {
                    barChart.setTitle("Comparación del total de las Entradas en " + year + " y Salidas en " + yearSalidas);
                }
                tableName = "Salidas";
                XYChart.Series serie2 = new XYChart.Series();
                serie1.setName("Entradas");
                serie2.setName("Salidas");
                columnasVisibles = table2.getVisibleLeafColumns();
                suma = 0.0;
                for (int i = 1; i < columnasVisibles.size() - 1; i++) {
                    String colName = columnasVisibles.get(i).getText();
                    suma = dataBaseManager.obtenerSumaDatosMes(colName, tableName);
                    serie2.getData().add(new XYChart.Data(colName, suma));
                }
                barChart.getData().add(serie2);
            }
            barChart.setAnimated(false);

            return barChart;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    private BarChart<?, ?> generarBarChartBalance(BarChart<?, ?> barChart, TableView table, String tableName, String year) {
        try {
            final CategoryAxis xAxis = new CategoryAxis();
            final NumberAxis yAxis = new NumberAxis();
            barChart.setTitle(tableName + " en: " + year);
            xAxis.setLabel("Resultados totales");
            yAxis.setLabel("Cantidad");

            XYChart.Series serie1 = new XYChart.Series();
            serie1.setName(year);

            ObservableList<TableColumn> columnas = table.getColumns();
            double suma;

            if (table.equals(tvBalEntradas)) {
                TableColumn col = null;
                for (int i = 1; i < 6; i++) {
                    if (i == 3) i = 4;
                    col = columnas.get(i);

                    suma = Extras.getSumaColumna(col, table);
                    serie1.getData().add(new XYChart.Data(col.getText(), suma));
                }
            } else if (table.equals(tvRegistroSalidas)) {
                tvRegistroSalidas.getItems().forEach(itemList -> {
                    TableColumn col;
                    String descripcion = "";
                    double cantidadTotal = 0.0;
                    for (int i = 0; i < 2; i++) {
                        col = columnas.get(i);
                        if (i == 0) {
                            descripcion = col.getCellObservableValue(itemList).getValue().toString();
                        } else {
                            cantidadTotal = Double.parseDouble(col.getCellObservableValue(itemList).getValue().toString());
                        }
                    }
                    serie1.getData().add(new XYChart.Data(descripcion, cantidadTotal));
                });
            }

            barChart.getData().clear();
            barChart.getData().add(serie1);
            barChart.setAnimated(false);

            return barChart;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            return null;
        }
    }

    private TableView obtenerTablaSeleccionada(String tableName) {
        switch (tableName) {
            case "Entradas":
                return tvEntradas;
            case "Salidas":
                return tvSalidas;
            case "Balance de Entradas":
                return tvBalEntradas;
            case "Registro de Salidas":
                return tvRegistroSalidas;
        }
        return null;
    }

    @FXML
    public void handleAyuda(ActionEvent event) {
        String msg = "Panel de ayuda";

        WebView webView = new WebView();
        webView.getEngine().load(getClass().getResource("resources/ayuda.html").toExternalForm());
        webView.setPrefSize(750, 650);


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Panel de ayuda");
        alert.setHeaderText(msg);
        alert.getDialogPane().setMaxWidth(750);
        alert.getDialogPane().setContent(webView);

        alert.showAndWait();
    }

    @FXML
    public void handleImportarBD(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Importar Base de Datos");


            File bdImportar = fileChooser.showOpenDialog(Extras.getPrimaryStage());


            if (bdImportar != null) {
                Path bdFrom = bdImportar.toPath();
                Path bdTo = Paths.get(System.getProperty("user.home") + File.separator +
                        "GestionComunidad" + File.separator + bdImportar.getName());

                if (!bdImportar.getName().matches(".*.db"))
                    throw new Exception("Archivo inválido");

                mostrarDialogoInfo("Se conservará una copia de seguridad de la BD actual en la carpeta GestionComunidad (localizada en su carpeta de usuario).");

                Path bdBackup = Paths.get(System.getProperty("user.home") + File.separator + "GestionComunidad" + File.separator + "ComunidadBaseDatos.db");
                File bdActual = bdBackup.toFile();

                dataBaseManager.getConnection().close();

                if (bdActual.renameTo(new File(System.getProperty("user.home") + File.separator +
                        "GestionComunidad" + File.separator + Extras.formatFileName("ComunidadBaseDatos") + ".db"))) {


                    Files.copy(bdFrom, bdTo, StandardCopyOption.REPLACE_EXISTING);
                    File newDB = new File(System.getProperty("user.home") + File.separator +
                            "GestionComunidad" + File.separator + bdImportar.getName());
                    newDB.renameTo(new File(System.getProperty("user.home") + File.separator +
                            "GestionComunidad" + File.separator + "ComunidadBaseDatos.db"));

                    Thread.sleep(500);
                    initialize(null, null);

                    Thread.sleep(500);
                    mostrarDialogoInfo("La base de datos se ha importado correctamente.");
                }
            }
        } catch (Exception ex) {
            mostrarDialogoError("Hubo un error al importar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleExportarBD(ActionEvent event) {
        try {
            DirectoryChooser chooser = new DirectoryChooser();
            chooser.setTitle("Escoger directorio");

            File fileExportar = chooser.showDialog(Extras.getPrimaryStage());

            if (fileExportar != null) {
                Path bdAExportar = Paths.get(System.getProperty("user.home") + File.separator +
                        "GestionComunidad" + File.separator + "ComunidadBaseDatos.db");
                Path pathExportar = Paths.get(fileExportar.toString() + File.separator + "ComunidadBaseDatos.db");

                mostrarDialogoInfo("Se exportará una copia de seguridad de la BD actual en la ruta indicada.");

                dataBaseManager.getConnection().close();

                Path copy = Files.copy(bdAExportar, pathExportar, StandardCopyOption.REPLACE_EXISTING);
                if (copy.toFile().exists()) {
                    mostrarDialogoInfo("Base de Datos exportada correctamente en la ruta: \n\n" +
                            pathExportar);
                    initialize(null, null);
                } else {
                    throw new Exception("no se ha generado bien la copia...");
                }
                initialize(null, null);
            }
        } catch (Exception ex) {
            mostrarDialogoError("Hubo un error al exportar: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleSalir(ActionEvent event) {
        if (mostrarDialogoConfirmacion("Se va a cerrar la aplicación.\n¿Desea continuar?")) {
            System.exit(0);
        }
    }
}
