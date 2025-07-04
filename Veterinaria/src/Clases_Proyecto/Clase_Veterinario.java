package Clases_Proyecto;

public class Clase_Veterinario {
	private String nombre,apellido;
	private int edad;
	private String especializacion;
	public Clase_Veterinario(String nombre, String apellido, int edad, String especializacion) {
		super();
		this.nombre = nombre;
		this.apellido = apellido;
		this.edad = edad;
		this.especializacion = especializacion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getApellido() {
		return apellido;
	}
	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	public int getEdad() {
		return edad;
	}
	public void setEdad(int edad) {
		this.edad = edad;
	}
	public String getEspecializacion() {
		return especializacion;
	}
	public void setEspecializacion(String especializacion) {
		this.especializacion = especializacion;
	}
	
}
