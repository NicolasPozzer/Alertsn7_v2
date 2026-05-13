package com.demo.alertsn7_v2_backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@Entity
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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
}
