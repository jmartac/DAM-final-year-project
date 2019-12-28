package application;

import application.comunero.Comunero;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.File;
import java.sql.*;
import java.time.LocalDate;
import java.time.Month;
import java.time.Period;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DataBaseManager {
    private Connection conn;
    private final String dbURL = "jdbc:sqlite:" + System.getProperty("user.home") +
            File.separator + "GestionComunidad" + File.separator + "ComunidadBaseDatos.db";
    private Statement sttmt;

    public DataBaseManager() {
        createDBConnection();
    }

    Connection getConnection() {
        return conn;
    }

    public void reiniciarConnection() {
        try {
            conn.close();

            conn = null;

            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(dbURL);
            sttmt = conn.createStatement();
            sttmt.executeUpdate("PRAGMA foreign_keys = ON;");
            sttmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    private void createDBConnection() {
        try {
            Class.forName("org.sqlite.JDBC");

            conn = DriverManager.getConnection(dbURL);
            sttmt = conn.createStatement();
            sttmt.executeUpdate("PRAGMA foreign_keys = ON;");
            sttmt.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    public void createColumnsFirstBoot(LocalDate date) {
        try {
            LocalDate actualDate = LocalDate.now();

            Period period = Period.between(date, actualDate);
            if (period.getDays() >= 30 || date.getMonthValue() != actualDate.getMonthValue())
                period = period.plusMonths(1);

            final int diferenciaMeses = ((int) period.toTotalMonths()) + 1;


            int year = date.getYear();
            int newYear = 0;

            for (int i = 0; i < diferenciaMeses; i++) {
                int mesNum = date.getMonthValue() + i;
                mesNum = mesNum - newYear;
                String newColumnName;

                if (mesNum == 12) {
                    newColumnName = Month.of(mesNum).toString().substring(0, 3) + year;

                    addNewColumnIntoTable("ENTRADAS", newColumnName, "DOUBLE");
                    addNewColumnIntoTable("SALIDAS", newColumnName, "DOUBLE");
                    newYear += 12;
                    year++;
                } else {
                    newColumnName = Month.of(mesNum).toString().substring(0, 3) + year;
                    addNewColumnIntoTable("ENTRADAS", newColumnName, "DOUBLE");
                    addNewColumnIntoTable("SALIDAS", newColumnName, "DOUBLE");
                }
            }

            ObservableList<String> comunerosLista = getListaComuneros();
            comunerosLista.forEach(com -> {
                //comunero null porque ya se hizo antes de ejecutar este método
                insertComuneroIntoTable("ENTRADAS", com, null);
            });

            String query = "INSERT INTO DATOS_INICIO (FECHA_INICIO, FECHA_ULTIMO_MES_ENTRADAS, FECHA_ULTIMO_MES_SALIDAS) VALUES (?,?,?)";
            try {
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setDate(1, Date.valueOf(date));
                pstmt.setDate(2, Date.valueOf(date.plusMonths(diferenciaMeses - 1)));
                pstmt.setDate(3, Date.valueOf(date.plusMonths(diferenciaMeses - 1)));

                pstmt.executeUpdate();
                pstmt.close();
            } catch (Exception e) {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void actualizarFechaUltimoMes(String tableName) {
        LocalDate nuevoMes = LocalDate.parse(getUltimaFecha(tableName)).plusMonths(1);

        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE DATOS_INICIO");

        if (tableName.equalsIgnoreCase("entradas")) {
            sql.append(" SET FECHA_ULTIMO_MES_ENTRADAS = ?");
        } else {
            sql.append(" SET FECHA_ULTIMO_MES_SALIDAS = ?");
        }

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.setDate(1, Date.valueOf(nuevoMes));
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    String getUltimaFecha(String tableName) {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet rs = sttmt.executeQuery("SELECT * FROM DATOS_INICIO");

            ObservableList<String> lista = FXCollections.observableArrayList();
            while (rs.next()) {
                if (tableName.equalsIgnoreCase("ENTRADAS")) {
                    lista.add(rs.getDate("FECHA_ULTIMO_MES_ENTRADAS").toString());
                } else {
                    lista.add(rs.getDate("FECHA_ULTIMO_MES_SALIDAS").toString());
                }
            }
            rs.close();
            sttmt.close();
            return lista.get(0);
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    void addNewColumnIntoTable(String tableName, String newColumnName, String columnType) throws Exception {
        try {
            if (tableName.equalsIgnoreCase("Entradas")) {
                dropTotalColumnEntradas();
            } else if (tableName.equalsIgnoreCase("Salidas")) {
                dropTotalColumnSalidas();
            } else {
                throw new Exception("LA TABLA NO ES NI SALIDAS NI ENTRADAS");
            }
            sttmt = conn.createStatement();
            sttmt.execute("ALTER TABLE " + tableName + " ADD COLUMN " + newColumnName + " " + columnType + "DEFAULT (0.0)");
            sttmt.execute("ALTER TABLE " + tableName + " ADD COLUMN TOTAL REAL");
            sttmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    ObservableList<String> getTablePRAGMA(String tableName) {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet rs = sttmt.executeQuery("PRAGMA table_info(" + tableName + ")");

            ObservableList<String> lista = FXCollections.observableArrayList();
            while (rs.next()) {
                lista.add(rs.getString("NAME"));
            }
            return lista;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    private void dropTotalColumnEntradas() throws Exception {
        try {
            reiniciarConnection();
            sttmt = conn.createStatement();

            conn.setAutoCommit(false);

            String nuevaEntradas = sttmt.executeQuery("SELECT name, sql FROM sqlite_master WHERE type='table' AND name='ENTRADAS'").getString("sql");
            nuevaEntradas = nuevaEntradas.replaceFirst(", TOTAL REAL", "");

            String colNames = getTablePRAGMA("ENTRADAS").toString();
            colNames = colNames.replaceFirst("\\[", "(");
            colNames = colNames.replaceFirst("]", ")");
            colNames = colNames.replaceFirst(", TOTAL", "");

            String datosNuevaEntradas = "INSERT INTO ENTRADAS " + colNames + " SELECT " +
                    colNames.replaceFirst("\\(", "").replaceFirst("\\)", "") +
                    " FROM sqlitestudio_temp_table";

            sttmt.executeUpdate("PRAGMA foreign_keys = 0");
            sttmt.executeUpdate("CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM ENTRADAS");
            sttmt.executeUpdate("DROP TABLE ENTRADAS");
            sttmt.executeUpdate(nuevaEntradas); //create table Entradas
            sttmt.executeUpdate(datosNuevaEntradas); //insert data Entradas from temp table
            sttmt.executeUpdate("DROP TABLE sqlitestudio_temp_table");
            sttmt.executeUpdate("PRAGMA foreign_keys = 1");

            sttmt.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        conn.setAutoCommit(true);
    }

    private void dropTotalColumnSalidas() throws Exception {
        try {
            reiniciarConnection();
            sttmt = conn.createStatement();

            conn.setAutoCommit(false);

            String nuevaSalidas = sttmt.executeQuery("SELECT name, sql FROM sqlite_master WHERE type='table' AND name='SALIDAS'").getString("sql");
            nuevaSalidas = nuevaSalidas.replaceFirst(", TOTAL REAL", "");

            String colNames = getTablePRAGMA("SALIDAS").toString();
            colNames = colNames.replaceFirst("\\[", "(");
            colNames = colNames.replaceFirst("]", ")");
            colNames = colNames.replaceFirst(", TOTAL", "");

            String datosNuevaEntradas = "INSERT INTO SALIDAS " + colNames + " SELECT " +
                    colNames.replaceFirst("\\(", "").replaceFirst("\\)", "") +
                    " FROM sqlitestudio_temp_table";

            sttmt.executeUpdate("PRAGMA foreign_keys = 0");
            sttmt.executeUpdate("CREATE TABLE sqlitestudio_temp_table AS SELECT * FROM SALIDAS");
            sttmt.executeUpdate("DROP TABLE SALIDAS");
            sttmt.executeUpdate(nuevaSalidas); //create table Entradas
            sttmt.executeUpdate(datosNuevaEntradas); //insert data Entradas from temp table
            sttmt.executeUpdate("DROP TABLE sqlitestudio_temp_table");
            sttmt.executeUpdate("PRAGMA foreign_keys = 1");

            sttmt.close();
            conn.commit();
        } catch (Exception e) {
            conn.rollback();
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        conn.setAutoCommit(true);
    }

    public void insertComunero(Comunero comunero) {
        String query = "INSERT INTO COMUNEROS (PARTICIPACION, CUOTA, FINCA, DNI, NOMBRE) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setDouble(1, comunero.getParticipacion());
            pstmt.setDouble(2, comunero.getCuotaMensual());
            pstmt.setString(3, comunero.getFinca());
            pstmt.setString(4, comunero.getDni());
            pstmt.setString(5, comunero.getNombre());

            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            if (e.getMessage().contains("[SQLITE_CONSTRAINT_UNIQUE]  A UNIQUE constraint failed (UNIQUE constraint failed: COMUNEROS.NOMBRE)")) {
                mostrarDialogoWarning("Hay comuneros que tienen el mismo nombre que otros ya existenes, por lo que no se insertarán.");
            } else {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
        }
    }

    //Este método requiere que se cree previamente el objeto Comunero correspondiente al nuevo comunero
    public void insertComuneroIntoTable(String tableName, String nombreComunero, Comunero comunero) {
        //si comunero es null, el comunero ya debería existir previamente
        try {
            if (comunero != null) {
                insertFinca(comunero.getFinca());
                insertComunero(comunero);
            }

            String query = "INSERT INTO " + tableName + " (COMUNERO) VALUES (?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, nombreComunero);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void limpiarDatosTablaEntera(String nombreTablaDB, String setDatosEliminar) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ").append(nombreTablaDB);
            sql.append(" SET ").append(setDatosEliminar);
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    public void insertFinca(String finca) throws Exception {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("INSERT INTO FINCAS (FINCA) VALUES ('").append(finca).append("')");

            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void updateComunero(String comunero, String setDatosNuevos) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE COMUNEROS");
        sql.append(" SET ").append(setDatosNuevos);
        sql.append(" WHERE NOMBRE='").append(comunero).append("'");

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    ObservableList<String> getListaComuneros() {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet rs = sttmt.executeQuery("SELECT * FROM COMUNEROS");

            ObservableList<String> lista = FXCollections.observableArrayList();
            while (rs.next()) {
                lista.add(rs.getString("NOMBRE"));
            }
            rs.close();
            sttmt.close();
            return lista;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    ObservableList<String> getListaMesesEntradas() {
        ObservableList<String> lista = getTablePRAGMA("ENTRADAS");
        lista.remove("COMUNERO");
        lista.remove("TOTAL");

        return lista;
    }

    ObservableList<String> getListaDescripciones() {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet rs = sttmt.executeQuery("SELECT * FROM TITULAR_SALIDA");

            ObservableList<String> lista = FXCollections.observableArrayList();
            while (rs.next()) {
                lista.add(rs.getString("DESCRIPCION"));
            }
            rs.close();
            sttmt.close();
            return lista;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    ObservableList<String> getListaMesesSalidas() {
        ObservableList<String> lista = getTablePRAGMA("SALIDAS");
        lista.remove("DESCRIPCION");
        lista.remove("TOTAL");

        return lista;
    }

    ResultSet getEntradasComuneroDB(String comunero) {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet resultSet = sttmt.executeQuery("SELECT * FROM ENTRADAS WHERE COMUNERO='" + comunero + "'");
            return resultSet;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    void actualizarComuneroEntradas(String comuneroSelected, String setDatosNuevos) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ENTRADAS");
        sql.append(" SET ").append(setDatosNuevos);
        sql.append(" WHERE COMUNERO='").append(comuneroSelected).append("'");

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void insertNuevosDatosSalidas(String descripcion, String setDatosNuevos) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO TITULAR_SALIDA (DESCRIPCION) VALUES ('").append(descripcion).append("')");

        PreparedStatement pstmt = null;
        try {
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();

            sql = new StringBuilder();
            sql.append("INSERT INTO SALIDAS (DESCRIPCION) VALUES ('").append(descripcion).append("')");
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();

            sql = new StringBuilder();
            sql.append("UPDATE SALIDAS");
            sql.append(" SET ").append(setDatosNuevos);
            sql.append(" WHERE DESCRIPCION='").append(descripcion).append("'");
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();

            pstmt.close();

            insertDescripcionRegistroSalidas(descripcion);
        } catch (Exception e) {
            if (e.getMessage().contains("[SQLITE_CONSTRAINT_PRIMARYKEY]  A PRIMARY KEY constraint failed " +
                    "(UNIQUE constraint failed: TITULAR_SALIDA.DESCRIPCION)")) {
                mostrarDialogoError("No se puede introducir dos salidas con exactamente la misma descripción.");
            } else {
                mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
            }
            if (pstmt != null)
                pstmt.close();
        }
    }

    void updateSalidas(boolean esNumerico, String descripcion, String setDatosNuevos) {
        try {
            StringBuilder sql = new StringBuilder();

            if (esNumerico)
                sql.append("UPDATE SALIDAS SET ").append(setDatosNuevos);
            else
                sql.append("UPDATE TITULAR_SALIDA SET ").append(setDatosNuevos);

            sql.append(" WHERE DESCRIPCION ='").append(descripcion).append("'");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    private void insertDescripcionRegistroSalidas(String descripcionNueva) {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO REGISTRO_SALIDAS (TITULAR_SALIDA) VALUES ('").append(descripcionNueva).append("')");

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void actualizarObservacionRegistroSalidas(String observacion, String setDatosNuevos) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE REGISTRO_SALIDAS");
            sql.append(" SET ").append(setDatosNuevos);
            sql.append(" WHERE TITULAR_SALIDA='").append(observacion).append("'");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }

    }

    double obtenerSumaDatosMes(String nombreMes, String table) {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet resultSet = sttmt.executeQuery("SELECT " + nombreMes + " FROM " + table);
            double suma = 0.0;
            while (resultSet.next()) {
                suma += resultSet.getDouble(nombreMes);
            }

            resultSet.close();
            sttmt.close();
            return suma;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return 0.0;
    }

    ResultSet getContentTablaComuneros() {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet resultSet = sttmt.executeQuery("SELECT NOMBRE, DNI, FINCA, CUOTA, PARTICIPACION FROM COMUNEROS");

            return resultSet;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    void eliminarComunero(String comunero) {
        StringBuilder sql;
        try {
            sql = new StringBuilder();
            sql.append("DELETE FROM COMUNEROS WHERE NOMBRE = '").append(comunero).append("'");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    ObservableList<String> getFincas() {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet rs = sttmt.executeQuery("SELECT FINCA FROM FINCAS");

            ObservableList<String> listado = FXCollections.observableArrayList();
            while (rs.next()) {
                listado.add(rs.getString("FINCA"));
            }
            rs.close();
            sttmt.close();
            return listado;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    public boolean fincaExists(String finca) {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet rs = sttmt.executeQuery("SELECT FINCA FROM FINCAS");

            ObservableList<String> listado = FXCollections.observableArrayList();
            while (rs.next()) {
                if (rs.getString("FINCA").equalsIgnoreCase(finca)) {
                    return true;
                }
            }
            rs.close();
            sttmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return false;
    }

    public Map<Integer, String> getComentarios() {
        Statement sttmt;
        try {
            sttmt = conn.createStatement();

            ResultSet rs = sttmt.executeQuery("SELECT * FROM COMENTARIOS");

            Map<Integer, String> comentarios = new HashMap<>();
            while (rs.next()) {
                comentarios.put(rs.getInt("id"), rs.getString("COMENTARIO"));
            }
            rs.close();
            sttmt.close();
            return comentarios;
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
        return null;
    }

    void eliminarComentario(int idComentario) {
        StringBuilder sql;
        try {
            sql = new StringBuilder();
            sql.append("DELETE FROM COMENTARIOS WHERE id = ").append(idComentario);
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void limpiarComentarios() {
        StringBuilder sql;
        try {
            sql = new StringBuilder();
            sql.append("DELETE FROM COMENTARIOS");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void insertComentario(String newComentario) {
        try {

            PreparedStatement pstmt = conn.prepareStatement(
                    "INSERT INTO COMENTARIOS (COMENTARIO) VALUES ('" + newComentario + "')");
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void updateComentario(Integer id, String value) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE COMENTARIOS SET COMENTARIO = '").append(value).append("'");
        sql.append(" WHERE id=").append(id);

        try {
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();

        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void eliminarFinca(String fincaSeleccionada) {
        StringBuilder sql;
        try {
            sql = new StringBuilder();
            sql.append("DELETE FROM FINCAS WHERE FINCA = '").append(fincaSeleccionada).append("'");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void eliminarSalida(String salidaDescripcion) {
        StringBuilder sql;
        try {
            sql = new StringBuilder();
            sql.append("DELETE FROM TITULAR_SALIDA WHERE DESCRIPCION = '").append(salidaDescripcion).append("'");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    private boolean mostrarDialogoError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Hubo un error");
        alert.setHeaderText(msg);
        alert.getDialogPane().setMaxWidth(400);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    private boolean mostrarDialogoWarning(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Cuidado");
        alert.setHeaderText(msg);
        alert.getDialogPane().setMaxWidth(400);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == ButtonType.OK;
    }

    void limpiarDatosMesSalidas(String mesABorrar) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE SALIDAS SET ").append(mesABorrar).append(" = 0.0");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void limpiarDatosMesEntradas(String mesABorrar) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ENTRADAS SET ").append(mesABorrar).append(" = 0.0");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());

            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void limpiarDatosDescripcion(String descripcion, String setDatosEliminar) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE SALIDAS SET ").append(setDatosEliminar)
                    .append(" WHERE DESCRIPCION = '").append(descripcion).append("'");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }

    void limpiarDatosComunero(String comunero, String setDatosEliminar) {
        try {
            StringBuilder sql = new StringBuilder();
            sql.append("UPDATE ENTRADAS SET ").append(setDatosEliminar)
                    .append(" WHERE COMUNERO = '").append(comunero).append("'");
            PreparedStatement pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();
            pstmt = conn.prepareStatement(sql.toString());
            pstmt.executeUpdate();

            pstmt.close();
        } catch (Exception e) {
            mostrarDialogoError("Hubo un error:\n\n"+e.getMessage());
        }
    }
}
