package com.demo.alertsn7_v2_backend.repository;

import com.demo.alertsn7_v2_backend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositoryTicket extends JpaRepository<Ticket, Long> {

    // SOLO tickets con alerta activa
    @Query("""
           SELECT t 
           FROM Ticket t 
           WHERE t.emitirAlerta = 1 
           AND t.emitirAlertaHasta IS NOT NULL
           """)
    List<Ticket> findTicketsConAlertaActiva();

    // SOLO tickets encendidos (para el scheduler principal)
    List<Ticket> findByEncendidoTrue();
}
