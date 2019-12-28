package application;

import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.text.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class Extras {
    private static DecimalFormat decimalFormat = new DecimalFormat("#.0");
    private static NumberFormat numberFormat = NumberFormat.getInstance();
    private static ObservableList<String> listaMesesElegidos = null;
    private static HashMap<Integer, String> intervalos = null;
    private static Stage primaryStage;
    private static final DateFormat dateFormat = new SimpleDateFormat("hhmmssSSS");


    public static TextFormatter getDoubleFormatter() {
        return new TextFormatter<>(change -> {
            String textWithNewChanges = change.getControlNewText();
            if (textWithNewChanges.isEmpty()) {
                return change;
            }

            String introducedCharacter = change.getText();
            ParsePosition pp = new ParsePosition(0);
            Object object = decimalFormat.parse(textWithNewChanges, pp);

            if (object == null || pp.getIndex() < textWithNewChanges.length()) {
                if (introducedCharacter.equals(",") && !textWithNewChanges.contains(".")) {
                    change.setText(".");
                    return change;
                }

                return null; //nuevo cambio no permitido
            } else {
                return change; //nuevo cambio permitido
            }
        });
    }

    public static TextFormatter getIntegerFormatter(int numMesesTotal) {
        return new TextFormatter<>(change -> {
            String textWithNewChanges = change.getControlNewText();
            if (textWithNewChanges.isEmpty()) {
                return change;
            }

            String introducedCharacter = change.getText();
            ParsePosition pp = new ParsePosition(0);
            Object object = numberFormat.parse(textWithNewChanges, pp);

            if (object == null || pp.getIndex() < textWithNewChanges.length()
                    || introducedCharacter.equals(".")) {
                return null;
            } else {
                int intervalo = Integer.valueOf(textWithNewChanges);
                if (intervalo > numMesesTotal / 2) {
                    return null;
                }
                return change;
            }
        });
    }

    public static void setMonthSelectorData(ObservableList<String> listaMesesElegidos, HashMap<Integer, String> intervalos) {
        Extras.listaMesesElegidos = listaMesesElegidos;
        Extras.intervalos = intervalos;
    }

    static Object getMonthSelectorData() {
        if (listaMesesElegidos != null) {
            return listaMesesElegidos;
        } else if (intervalos != null) {
            return intervalos;
        }
        return null;
    }

    static void cleanSelectedMonths() {
        Extras.listaMesesElegidos = null;
        Extras.intervalos = null;
    }

    static String getMonthNameForDB(LocalDate localDate) {
        return localDate.getMonth().plus(1).toString().substring(0, 3) + localDate.plusMonths(1).getYear();
    }

    static void setPrimaryStage(Stage primaryStage) {
        Extras.primaryStage = primaryStage;
    }

    static Stage getPrimaryStage() {
        return Extras.primaryStage;
    }

    static String formatFileName(String fileName) {
        Date date = Calendar.getInstance().getTime();
        return fileName + "_" + dateFormat.format(date);
    }

    static Double getSumaColumna(TableColumn columna, TableView table) {
        double suma = 0.0;
        for (int i = 0; i < table.getItems().size(); i++) {
            suma += Double.valueOf(columna.getCellObservableValue(i).getValue().toString());
        }
        return suma;
    }
}
