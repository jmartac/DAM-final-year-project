package application.month_selector;

import application.Extras;
import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class FXML_MonthSelectorController implements Initializable {

    @FXML
    private JFXRadioButton checkMesesConcretos;

    @FXML
    private ToggleGroup tgMonthSelector;

    @FXML
    private JFXCheckBox checkMonth1;

    @FXML
    private JFXCheckBox checkMonth2;

    @FXML
    private JFXCheckBox checkMonth3;

    @FXML
    private JFXCheckBox checkMonth4;

    @FXML
    private JFXCheckBox checkMonth5;

    @FXML
    private JFXCheckBox checkMonth6;

    @FXML
    private JFXCheckBox checkMonth7;

    @FXML
    private JFXCheckBox checkMonth8;

    @FXML
    private JFXCheckBox checkMonth9;

    @FXML
    private JFXCheckBox checkMonth10;

    @FXML
    private JFXCheckBox checkMonth11;

    @FXML
    private JFXCheckBox checkMonth12;

    @FXML
    private JFXRadioButton checkIntervalos;

    @FXML
    private JFXTextField tfNumeroIntervalo;

    @FXML
    private JFXComboBox<String> cbListaMeses;

    @FXML
    private Label lbMaximo;

    private Stage windowStage;
    private final ArrayList<CheckBox> listaCheckBoxes = new ArrayList<>();
    private ObservableList<String> listaMesesVisibles;
    private ObservableList<String> todosLosMeses;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tgMonthSelector.selectedToggleProperty().addListener(observable -> {
            boolean first = checkMesesConcretos.isSelected();
            boolean second = checkIntervalos.isSelected();

            tfNumeroIntervalo.setDisable(first);
            cbListaMeses.setDisable(first);

            checkMonth1.setDisable(second);
            checkMonth2.setDisable(second);
            checkMonth3.setDisable(second);
            checkMonth4.setDisable(second);
            checkMonth5.setDisable(second);
            checkMonth6.setDisable(second);
            checkMonth7.setDisable(second);
            checkMonth8.setDisable(second);
            checkMonth9.setDisable(second);
            checkMonth10.setDisable(second);
            checkMonth11.setDisable(second);
            checkMonth12.setDisable(second);
        });

        listaCheckBoxes.add(checkMonth1);
        listaCheckBoxes.add(checkMonth2);
        listaCheckBoxes.add(checkMonth3);
        listaCheckBoxes.add(checkMonth4);
        listaCheckBoxes.add(checkMonth5);
        listaCheckBoxes.add(checkMonth6);
        listaCheckBoxes.add(checkMonth7);
        listaCheckBoxes.add(checkMonth8);
        listaCheckBoxes.add(checkMonth9);
        listaCheckBoxes.add(checkMonth10);
        listaCheckBoxes.add(checkMonth11);
        listaCheckBoxes.add(checkMonth12);
    }


    public void initData(Stage windowStage, ObservableList<String> listaMesesVisibles, ObservableList<String> todosLosMeses) {
        this.listaMesesVisibles = listaMesesVisibles;
        this.todosLosMeses = todosLosMeses;
        this.windowStage = windowStage;

        putMonths();
        if (todosLosMeses.size() > 1) {
            lbMaximo.setText("* Máximo: " + todosLosMeses.size() / 2);
            tfNumeroIntervalo.setTextFormatter(Extras.getIntegerFormatter(todosLosMeses.size()));
        } else {
            checkIntervalos.setDisable(true);
        }
    }

    private void putMonths() {
        for (int i = 0; i < 12; i++) {
            if (listaMesesVisibles.size() > i) {
                listaCheckBoxes.get(i).setText(listaMesesVisibles.get(i));
            } else {
                listaCheckBoxes.get(i).setVisible(false);
            }
        }
        cbListaMeses.setItems(todosLosMeses);
    }

    @FXML
    public void handleOnTerminarSeleccion(ActionEvent event) {
        if (getSelectedMonths() == null && getIntervalPerMonth() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Cuidado");
            alert.setHeaderText("¡No hay ningún mes seleccionado o al intervalo le falta algún dato!");
            alert.getDialogPane().setMaxWidth(400);

            alert.showAndWait();
            windowStage.close();
        } else {
            if (getSelectedMonths() != null) {
                if (getSelectedMonths().size() <= 0) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Cuidado");
                    alert.setHeaderText("¡No hay ningún mes seleccionado o al intervalo le falta algún dato!");
                    alert.getDialogPane().setMaxWidth(400);

                    alert.showAndWait();
                    windowStage.close();
                }
            }
            Extras.setMonthSelectorData(getSelectedMonths(), getIntervalPerMonth());
            windowStage.close();
        }
    }

    //Metodo que devuelve el intervalo de inserciones que el usuario haya seleccionado
    private HashMap<Integer, String> getIntervalPerMonth() {
        JFXRadioButton rbSelected = (JFXRadioButton) tgMonthSelector.getSelectedToggle();
        if (rbSelected.getId().equals("checkIntervalos")) {
            HashMap map = new HashMap<Integer, String>();
            if (!tfNumeroIntervalo.getText().isEmpty() && cbListaMeses.getValue() != null) {
                map.put(Integer.valueOf(tfNumeroIntervalo.getText()), cbListaMeses.getValue());
                return map;
            }
        }
        return null;
    }

    //Metodo que devuelve los meses que el usuario haya seleccionado
    private ObservableList<String> getSelectedMonths() {
        JFXRadioButton rbSelected = (JFXRadioButton) tgMonthSelector.getSelectedToggle();
        if (rbSelected.getId().equals("checkMesesConcretos")) {
            ObservableList<String> selectedMonths = FXCollections.observableArrayList();

            listaCheckBoxes.forEach(checkBox -> {
                if (checkBox.isSelected()) {
                    selectedMonths.add(checkBox.getText());
                }
            });

            return selectedMonths;
        }
        return null;
    }
}
