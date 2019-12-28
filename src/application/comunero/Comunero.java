package application.comunero;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class Comunero {
    private final SimpleStringProperty nombre, dni, finca;
    private final SimpleDoubleProperty cuotaMensual, participacion;

    public Comunero(String nombre, String dni, String finca, double cuotaMensual, double participacion) {
        this.nombre = new SimpleStringProperty(nombre);
        this.dni = new SimpleStringProperty(dni);
        this.finca = new SimpleStringProperty(finca);
        this.cuotaMensual = new SimpleDoubleProperty(cuotaMensual);
        this.participacion = new SimpleDoubleProperty(participacion);
    }

    public Comunero(String nombre, String dni, String finca, double cuotaMensual) {
        this.nombre = new SimpleStringProperty(nombre);
        this.dni = new SimpleStringProperty(dni);
        this.finca = new SimpleStringProperty(finca);
        this.cuotaMensual = new SimpleDoubleProperty(cuotaMensual);
        this.participacion = new SimpleDoubleProperty(0);
    }

    public String getNombre() {
        return nombre.get();
    }

    public SimpleStringProperty nombreProperty() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre.set(nombre);
    }

    public String getFinca() {
        return finca.get();
    }

    public SimpleStringProperty fincaProperty() {
        return finca;
    }

    public void setFinca(String finca) {
        this.finca.set(finca);
    }

    public String getDni() {
        return dni.get();
    }

    public SimpleStringProperty dniProperty() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni.set(dni);
    }

    public double getCuotaMensual() {
        return cuotaMensual.get();
    }

    public SimpleDoubleProperty cuotaMensualProperty() {
        return cuotaMensual;
    }

    public void setCuotaMensual(double cuotaMensual) {
        this.cuotaMensual.set(cuotaMensual);
    }

    public double getParticipacion() {
        return participacion.get();
    }

    public SimpleDoubleProperty participacionProperty() {
        return participacion;
    }

    public void setParticipacion(double participacion) {
        this.participacion.set(participacion);
    }
}
