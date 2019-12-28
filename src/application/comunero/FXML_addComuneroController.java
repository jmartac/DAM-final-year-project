package application.comunero;

import application.DataBaseManager;
import application.Extras;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class FXML_addComuneroController implements Initializable {

    @FXML
    private AnchorPane panePrincipal;

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
    private Label lbCampoObligatorio;

    @FXML
    private JFXButton btTerminar;

    //Lista de comuneros a las que apuntan las tablas para mostrar su contenido, respectivamente
    private static ObservableList<Comunero> comuneros = FXCollections.observableArrayList();
    //Lista de TextFields que contiene los textfields del paso 1
    private ArrayList<JFXTextField> comunerosTableTextFields = new ArrayList<>();
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
    private Stage windowStage;

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

        tvComunerosBase.getItems().addListener((ListChangeListener<? super Comunero>) c -> {
            if (tvComunerosBase.getItems().size() >= 1) {
                btTerminar.setDisable(false);
            } else btTerminar.setDisable(true);
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
    }

    @FXML   //clic derecho y Eliminar
    public void handleEliminarComunero(ActionEvent event) {
        Comunero comuneroToRemove = tvComunerosBase.getSelectionModel().getSelectedItem();
        comuneros.remove(comuneroToRemove);
    }

    @FXML
    public void handleTerminar(ActionEvent event) {
        DataBaseManager dataBaseManager = new DataBaseManager();

        for (Comunero comunero : comuneros) {
            if (!dataBaseManager.fincaExists(comunero.getFinca())) {
                try {
                    dataBaseManager.insertFinca(comunero.getFinca());
                } catch (Exception e) {
                    mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                }
            }
            dataBaseManager.insertComunero(comunero);
            dataBaseManager.reiniciarConnection();
            dataBaseManager.insertComuneroIntoTable("ENTRADAS", comunero.getNombre(), null);
        }
        windowStage.close();
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

    public void initData(Stage windowStage) {
        this.windowStage = windowStage;
    }

    public void handleCancelar(ActionEvent event) {
        windowStage.close();
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
