package application.first_boot;

import application.DataBaseManager;
import application.Extras;
import application.comunero.Comunero;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;
import java.util.ResourceBundle;

public class FXML_firstBootController implements Initializable {

    public Label lbConsejo;
    @FXML
    private AnchorPane panePrincipal;

    @FXML
    private ImageView imgCheck1;

    @FXML
    private TableView<Comunero> tvComunerosBase;

    @FXML
    private TableColumn<Comunero, String> tcNombreCom;

    @FXML
    private TableColumn<Comunero, String> tcDniCom;

    @FXML
    private TableColumn<Comunero, String> tcFincaCom;

    @FXML
    private TableColumn<Comunero, Double> tcCuotaCom;

    @FXML
    private JFXTextField tfNombre;

    @FXML
    private JFXTextField tfApellidos;

    @FXML
    private JFXTextField tfDni;

    @FXML
    private JFXTextField tfFinca;

    @FXML
    private JFXTextField tfCuota;

    @FXML
    private JFXComboBox cbEliminarComunero;

    @FXML
    private JFXButton btAddComunero;

    @FXML
    private ImageView imgCheck2;

    @FXML
    private Label lbStep2;

    @FXML
    private Label lbPorcentaje;

    @FXML
    private TableView<Comunero> tvParticipacion;

    @FXML
    private TableColumn<Comunero, String> tcNombrePar;

    @FXML
    private TableColumn<Comunero, String> tcFincaPar;

    @FXML
    private TableColumn<Comunero, Double> tcPorcentajePar;

    @FXML
    private ImageView imgCheck3;

    @FXML
    private Label lbStep3;

    @FXML
    private Label lbCampoObligatorio;

    @FXML
    private JFXDatePicker dpFechaGestion;

    @FXML
    private JFXButton btTerminar;

    private Stage windowStsage;
    //Lista de comuneros a las que apuntan las tablas para mostrar su contenido, respectivamente
    private static ObservableList<Comunero> comuneros = FXCollections.observableArrayList();
    private static ObservableList<Comunero> comunerosParticipantes = FXCollections.observableArrayList();
    //Lista de TextFields que contiene los textfields del paso 1
    private ArrayList<JFXTextField> comunerosTableTextFields = new ArrayList<>();
    //Listener para comprobar que el primer paso ha sido completado y desbloquear los siguientes
    private ListChangeListener<Comunero> stepOneComplete_Listener = c -> {
        if (comuneros.size() >= 2) {
            setFormDisable(false);
            imgCheck1.setVisible(true);
        } else {
            setFormDisable(true);
            imgCheck1.setVisible(false);
        }
        checkFormStatus();
    };
    //Listener para añadir los comuneros del paso 1 a la tabla del paso 2
    private ListChangeListener<Comunero> fillInSecondTable_Listener = c -> {
        if (comunerosParticipantes.isEmpty() && !comuneros.isEmpty()) {
            comunerosParticipantes.clear();
            comunerosParticipantes.addAll(comuneros);

        } else {
            c.next();
            if (c.wasRemoved()) {
                comunerosParticipantes.removeAll(c.getRemoved());
            }
            if (c.wasAdded()) {
                comunerosParticipantes.addAll(c.getAddedSubList());
            }
        }
    };
    //Convertidor de String a Double que devuelve 100 si es mayor a esa cifra o 0 si no es un número convertible
    private DoubleStringConverter doubleStringConverterParticipacion = new DoubleStringConverter() {
        @Override
        public Double fromString(String string) {
            try {
                double value = Double.valueOf(string);
                if (value <= 100.0) {
                    return value;
                } else return 100.0;
            } catch (NumberFormatException ex) {
                return 0.0;
            }
        }
    };

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

    /**
     * Initializing method
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupComunerosTable();

        tfCuota.setTextFormatter(Extras.getDoubleFormatter());

        comunerosTableTextFields.add(tfNombre);
        comunerosTableTextFields.add(tfDni);
        comunerosTableTextFields.add(tfFinca);
        comunerosTableTextFields.add(tfCuota);

        comunerosTableTextFields.forEach(jfxTextField -> {
            jfxTextField.setOnKeyPressed(event -> {
                if (event.getCode().equals(KeyCode.ENTER) && checkEmptyTextFields()) {
                    btAddComunero.requestFocus();
                    btAddComunero.fire();
                }
            });
        });

        comuneros.addListener(stepOneComplete_Listener);
        comuneros.addListener(fillInSecondTable_Listener);

        setupParticipacionTable();

        setUpDataPicker();
    }



    public void initData(Stage windowStsage) {
        this.windowStsage = windowStsage;
        this.windowStsage.setOnCloseRequest(event -> {
            if (mostrarDialogoConfirmacion("Se va a cerrar la aplicación.\n\n¿Desea continuar?")) {
                System.exit(0);
            }
        });
    }

    @FXML   //Botón Añadir Comunero
    public void handleAddComunero(ActionEvent event) {
        if (checkEmptyTextFields()) {
            Comunero newComunero = new Comunero(
                    tfNombre.getText().trim().concat(" " + tfApellidos.getText().trim()), tfDni.getText().trim().toUpperCase(),
                    tfFinca.getText().trim(), Double.parseDouble(tfCuota.getText()));

            boolean duplicado = false;
            for (Comunero comunero : comuneros) {
                if (comunero.getNombre().trim().equals(newComunero.getNombre().trim())){
                    duplicado = true;
                }
            }

            if (duplicado){
                mostrarDialogoError("No puede haber 2 o más comuneros con el mismo nombre exacto.");
                tfNombre.clear();
                tfApellidos.clear();
            } else {
                comuneros.add(newComunero);
                clearTextFields();
            }
        }
        tvComunerosBase.refresh();
        tvParticipacion.refresh();
    }

    @FXML   //clic derecho y Eliminar
    public void handleEliminarComunero(ActionEvent event) {
        Comunero comuneroToRemove = tvComunerosBase.getSelectionModel().getSelectedItem();
        comuneros.remove(comuneroToRemove);
    }

    @FXML
    public void handleTerminar(ActionEvent event) {
        //CONECTAR A BASE DE DATOS E INTRODUCIR EN TABLAS: COMUNEROS Y DATOSINICIALES
        DataBaseManager dataBaseManager = new DataBaseManager();
        LocalDate fechaInicioGestion = dpFechaGestion.getValue();

        for (Comunero comunero : comuneros) {
            try {
                dataBaseManager.insertFinca(comunero.getFinca());
            } catch (Exception e) {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
            dataBaseManager.insertComunero(comunero);
        }

        dataBaseManager.createColumnsFirstBoot(fechaInicioGestion);
        windowStsage.close();
    }


    //Comprueba que no haya ningún textfield vacío
    private boolean checkEmptyTextFields() {
        for (JFXTextField tf : comunerosTableTextFields) {
            if (tf.getText().trim().isEmpty()) {
                tf.requestFocus();
                return false;
            }
        }
        return true;
    }

    //Vacía los datos que hay en los textfields para la tabla 1
    private void clearTextFields() {
        tfNombre.clear();
        tfApellidos.clear();
        tfDni.clear();
        tfFinca.clear();
        tfCuota.clear();
    }

    //Prepara la tabla 1
    private void setupComunerosTable() {
        tcNombreCom.setCellValueFactory(new PropertyValueFactory<Comunero, String>("nombre"));
        tcNombreCom.setCellFactory(TextFieldTableCell.forTableColumn());

        tcDniCom.setCellValueFactory(new PropertyValueFactory<Comunero, String>("dni"));
        tcDniCom.setCellFactory(TextFieldTableCell.forTableColumn());

        tcFincaCom.setCellValueFactory(new PropertyValueFactory<Comunero, String>("finca"));
        tcFincaCom.setCellFactory(TextFieldTableCell.forTableColumn());

        tcCuotaCom.setCellValueFactory(new PropertyValueFactory<Comunero, Double>("cuotaMensual"));
        tcCuotaCom.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverter));

        tvComunerosBase.setItems(comuneros);
        tvComunerosBase.setEditable(true);

        setupOnEditEventsComunerosTable(tvComunerosBase.getColumns());
    }

    //Prepara los eventos que saltarán con la edición de celdas de la tabla del paso 1
    private void setupOnEditEventsComunerosTable(ObservableList<TableColumn<Comunero, ?>> tableColumns) {
        tcNombreCom.setOnEditCommit(event -> {
            String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            Comunero c = (event.getTableView().getItems().get(
                    event.getTablePosition().getRow()));

            c.setNombre(value.trim().replaceAll(" +", " "));
            tvComunerosBase.refresh();
            tvComunerosBase.requestFocus();
        });

        tcDniCom.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            (event.getTableView().getItems().get(
                    event.getTablePosition().getRow())).setDni(value.trim());
            tvComunerosBase.refresh();
            tvComunerosBase.requestFocus();
        });

        tcFincaCom.setOnEditCommit(event -> {
            final String value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            (event.getTableView().getItems().get(
                    event.getTablePosition().getRow())).setFinca(value.trim());
            tvComunerosBase.refresh();
            tvComunerosBase.requestFocus();
        });

        tcCuotaCom.setOnEditCommit(event -> {
            final Double value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            (event.getTableView().getItems().get(
                    event.getTablePosition().getRow())).setCuotaMensual(value);
            tvComunerosBase.refresh();
            tvComunerosBase.requestFocus();
        });
    }

    //Prepara la tabla 2 y el evento que saltará con la edición de porcentajes de la misma tabla
    private void setupParticipacionTable() {
        tcNombrePar.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcFincaPar.setCellValueFactory(new PropertyValueFactory<>("finca"));
        tcPorcentajePar.setCellValueFactory(new PropertyValueFactory<>("participacion"));

        tcPorcentajePar.setCellFactory(TextFieldTableCell.forTableColumn(doubleStringConverterParticipacion));

        tcPorcentajePar.setOnEditCommit(event -> {
            final Double value = event.getNewValue() != null ?
                    event.getNewValue() : event.getOldValue();
            (event.getTableView().getItems().get(
                    event.getTablePosition().getRow())).setParticipacion(value);
            tvParticipacion.refresh();
            tvParticipacion.requestFocus();

            printPercentage();
        });

        tvParticipacion.setItems(comunerosParticipantes);
        tvParticipacion.setEditable(true);
    }

    //Prepara el paso de añadir una fecha de la que empezar a gestionar la comu
    private void setUpDataPicker() {
        dpFechaGestion.setOnAction(event -> {
            if (dpFechaGestion.getValue() != null) {
                imgCheck3.setVisible(true);
            } else imgCheck3.setVisible(false);
            checkFormStatus();
        });
    }

    //Actualiza el indicador del total de porcentaje repartido
    private void printPercentage() {
        double percentage = 0;
        for (Comunero comunerosParticipante : comunerosParticipantes) {
            percentage = percentage + comunerosParticipante.getParticipacion();
        }
        lbPorcentaje.setText("Porcentaje repartido: " + percentage + "%");

        if (percentage == 100) {
            imgCheck2.setVisible(true);
            lbPorcentaje.setStyle("-fx-text-fill:green;");
        } else {
            imgCheck2.setVisible(false);
            lbPorcentaje.setStyle("-fx-text-fill:orangered;");
        }
        checkFormStatus();
    }

    //Habilitador de los siguientes PASOS
    private void setFormDisable(boolean enabled) {
        lbStep2.setDisable(enabled);
        lbPorcentaje.setDisable(enabled);
        tvParticipacion.setDisable(enabled);
        lbConsejo.setDisable(enabled);
        lbStep3.setDisable(enabled);
        lbCampoObligatorio.setDisable(enabled);
        dpFechaGestion.setDisable(enabled);
    }

    //Comprueba que los 3 pasos del formulario han sido completados correctamente para habilitar el botón de Terminar
    private void checkFormStatus() {
        if (imgCheck1.isVisible() && imgCheck3.isVisible()) {
            btTerminar.setDisable(false);
        } else btTerminar.setDisable(true);
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
}

