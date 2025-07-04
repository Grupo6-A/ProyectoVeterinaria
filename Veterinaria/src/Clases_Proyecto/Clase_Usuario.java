package Clases_Proyecto;

public class Clase_Usuario {
    private int id;
    private String nombre;
    private String usuario;
    private String clave;

 
    public Clase_Usuario() {
    }

    
    public Clase_Usuario(String nombre, String usuario, String clave) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
    }

   
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}