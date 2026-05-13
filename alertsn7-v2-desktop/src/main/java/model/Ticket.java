package model;

public class Ticket {
    
    private Long id;
    private String nombre;
    private Double precioEstablecido;
    private String direccion;  //  False = Short | True = Long
    private Boolean encendido;  //  False = Apagado | True = Encendido
    private String color;
    private int emitirAlerta;

    public Ticket() {
    }

    public Ticket(Long id, String nombre, Double precioEstablecido, String direccion, Boolean encendido, String color, int emitirAlerta) {
        this.id = id;
        this.nombre = nombre;
        this.precioEstablecido = precioEstablecido;
        this.direccion = direccion;
        this.encendido = encendido;
        this.color = color;
        this.emitirAlerta = emitirAlerta;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioEstablecido() {
        return precioEstablecido;
    }

    public void setPrecioEstablecido(Double precioEstablecido) {
        this.precioEstablecido = precioEstablecido;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Boolean getEncendido() {
        return encendido;
    }

    public void setEncendido(Boolean encendido) {
        this.encendido = encendido;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getEmitirAlerta() {
        return emitirAlerta;
    }

    public void setEmitirAlerta(int emitirAlerta) {
        this.emitirAlerta = emitirAlerta;
    }

    @Override
    public String toString() {
        return "Ticket{" + "id=" + id + ", nombre=" + nombre + ", precioEstablecido=" + precioEstablecido + ", direccion=" + direccion + ", encendido=" + encendido + ", color=" + color + ", emitirAlerta=" + emitirAlerta + '}';
    }
    
    
}
