package fia.ues.saluddigital.Mapa;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedHashMap;
import java.util.Map;

public class Puntos {

    public Puntos() {
    }

    private String id;
    private String longitud;
    private String latitud;
    private String nombreLugar;
    private String descripcionLugar;
    private String imagenLugar;
    private String direccion;
    private Map<String, Object> additionalProperties = new LinkedHashMap<String, Object>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getNombreLugar() {
        return nombreLugar;
    }

    public void setNombreLugar(String nombreLugar) {
        this.nombreLugar = nombreLugar;
    }

    public String getDescripcionLugar() {
        return descripcionLugar;
    }

    public void setDescripcionLugar(String descripcionLugar) {
        this.descripcionLugar = descripcionLugar;
    }

    public String getImagenLugar() {
        return imagenLugar;
    }

    public void setImagenLugar(String imagenLugar) {
        this.imagenLugar = imagenLugar;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Puntos(JSONObject jsonObject){
        if(jsonObject ==null){
            return;}
        id = jsonObject.optString("id");
        longitud = jsonObject.optString("longitud");
        latitud = jsonObject.optString("latitud");
        nombreLugar = jsonObject.optString("nombre_lugar");
        descripcionLugar = jsonObject.optString("descripcion_lugar");
        imagenLugar = jsonObject.optString("imagen_lugar");

    }

    public JSONObject toJsonObject(){
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("descripcion_lugar",descripcionLugar);
            jsonObject.put("id",id);
            jsonObject.put("imagen_lugar",imagenLugar);
            jsonObject.put("latitud",latitud);
            jsonObject.put("longitud",longitud);
            jsonObject.put("nombre_lugar",nombreLugar);
        }catch (JSONException e){e.printStackTrace();;}
        return jsonObject;
    }
}

