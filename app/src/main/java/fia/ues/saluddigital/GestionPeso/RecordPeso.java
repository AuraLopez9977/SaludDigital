package fia.ues.saluddigital.GestionPeso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordPeso {

    private Date fecha;
    private double peso;
    private String unidad;

    public RecordPeso() {
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = parseFecha(fecha);
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    private Date parseFecha(String fecha) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            return dateFormat.parse(fecha);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
