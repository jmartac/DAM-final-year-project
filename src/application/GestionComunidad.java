package application;

import application.first_boot.FXML_firstBootController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public class GestionComunidad extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        File pathCarpeta = new File(System.getProperty("user.home") +
                File.separator + "GestionComunidad");
        File db = new File(pathCarpeta.getPath() + File.separator + "ComunidadBaseDatos.db");

        //Comprobar que la BD existe y CREARLA si no
        if (!pathCarpeta.exists() || !db.exists()) {
            pathCarpeta.mkdirs();
            db.createNewFile();
            try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + db.getPath())) {
                if (conn != null) {
                    System.out.println("DB creada porque no se encontró.");
                    Statement sttmt;

                    try {
                        sttmt = conn.createStatement();
                        conn.setAutoCommit(false);

                        sttmt.executeUpdate("PRAGMA foreign_keys = 0");

                        sttmt.executeUpdate("DROP TABLE IF EXISTS COMENTARIOS");
                        sttmt.executeUpdate("CREATE TABLE COMENTARIOS (COMENTARIO VARCHAR (300) NOT NULL, id INTEGER PRIMARY KEY AUTOINCREMENT)");
                        sttmt.executeUpdate("DROP TABLE IF EXISTS COMUNEROS");
                        sttmt.executeUpdate("CREATE TABLE COMUNEROS (NOMBRE VARCHAR NOT NULL UNIQUE, DNI VARCHAR, FINCA VARCHAR REFERENCES FINCAS (FINCA) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL, CUOTA DOUBLE NOT NULL, PARTICIPACION DOUBLE NOT NULL, ID_COMUNERO INTEGER PRIMARY KEY AUTOINCREMENT)");
                        sttmt.executeUpdate("DROP TABLE IF EXISTS DATOS_INICIO");
                        sttmt.executeUpdate("CREATE TABLE DATOS_INICIO (FECHA_INICIO DATE NOT NULL, FECHA_ULTIMO_MES_ENTRADAS DATE, FECHA_ULTIMO_MES_SALIDAS DATE)");
                        sttmt.executeUpdate("DROP TABLE IF EXISTS ENTRADAS");
                        sttmt.executeUpdate("CREATE TABLE ENTRADAS (COMUNERO VARCHAR REFERENCES COMUNEROS (NOMBRE) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL UNIQUE, TOTAL REAL)");
                        sttmt.executeUpdate("DROP TABLE IF EXISTS FINCAS");
                        sttmt.executeUpdate("CREATE TABLE FINCAS (ID_FINCA INTEGER PRIMARY KEY AUTOINCREMENT, FINCA VARCHAR NOT NULL UNIQUE)");
                        sttmt.executeUpdate("DROP TABLE IF EXISTS REGISTRO_SALIDAS");
                        sttmt.executeUpdate("CREATE TABLE REGISTRO_SALIDAS (TITULAR_SALIDA VARCHAR REFERENCES TITULAR_SALIDA (DESCRIPCION) ON DELETE CASCADE ON UPDATE CASCADE NOT NULL PRIMARY KEY ON CONFLICT ROLLBACK, TOTAL_12MESES DOUBLE DEFAULT (0.0), OBSERVACIONES VARCHAR DEFAULT Ninguna)");
                        sttmt.executeUpdate("DROP TABLE IF EXISTS SALIDAS");
                        sttmt.executeUpdate("CREATE TABLE SALIDAS (DESCRIPCION VARCHAR REFERENCES TITULAR_SALIDA (DESCRIPCION) ON DELETE CASCADE ON UPDATE CASCADE UNIQUE NOT NULL, TOTAL REAL)");
                        sttmt.executeUpdate("DROP TABLE IF EXISTS TITULAR_SALIDA");
                        sttmt.executeUpdate("CREATE TABLE TITULAR_SALIDA (DESCRIPCION VARCHAR PRIMARY KEY NOT NULL)");

                        sttmt.executeUpdate("PRAGMA foreign_keys = 1");

                        sttmt.close();
                        conn.commit();
                    } catch (Exception e) {
                        conn.rollback();
                        mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
                    }

                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
        }

        //Comprobar si es el primer arranque o no
        if (esPrimerArranque(db.getPath())) {
            mostrarVentanaPrimerArranque();
        }

        //Panel principal de la app
        Parent root = FXMLLoader.load(getClass().getResource("FXML_mainPanel.fxml"));
        primaryStage.setTitle("Gestión Comunidad");
        primaryStage.setScene(new Scene(root, 1280, 800));
        primaryStage.getIcons().add(new Image("application/resources/icon_32px.png"));
        primaryStage.setMinHeight(800);
        primaryStage.setMinWidth(1280);

        primaryStage.setOnCloseRequest(event -> {
            if (mostrarDialogoConfirmacion("Se va a cerrar la aplicación.\n\n¿Desea continuar?")) {
                System.exit(0);
            }
        });

        Extras.setPrimaryStage(primaryStage);
        primaryStage.show();
    }

    private boolean esPrimerArranque(String dbURL) {
        try {
            Class.forName("org.sqlite.JDBC");

            Connection conn = DriverManager.getConnection("jdbc:sqlite:" + dbURL);
            if (conn != null) {
                Statement sttmt = conn.createStatement();
                ResultSet rs = sttmt.executeQuery("SELECT FECHA_INICIO FROM DATOS_INICIO");
                if (!rs.isClosed()) {
                    if (rs.getString("FECHA_INICIO") != null) {
                        rs.close();
                        conn.close();
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return true;
    }

    private void mostrarVentanaPrimerArranque() {
        Parent root;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource(
                    "application/first_boot/FXML_firstBoot.fxml"));
            root = loader.load();
            Stage stage = new Stage();
            stage.setTitle("Inicio");
            stage.setScene(new Scene(root, 804, 550));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.getIcons().add(new Image("application/resources/icon_32px.png"));
            stage.setResizable(false);

            FXML_firstBootController firstBootController = loader.getController();
            firstBootController.initData(stage);

            stage.showAndWait();
        } catch (IOException e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
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
