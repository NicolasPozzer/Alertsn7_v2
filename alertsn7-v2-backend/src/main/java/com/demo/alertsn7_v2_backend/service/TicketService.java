package com.demo.alertsn7_v2_backend.service;

import com.demo.alertsn7_v2_backend.model.ApiCoin;
import com.demo.alertsn7_v2_backend.model.Ticket;
import com.demo.alertsn7_v2_backend.repository.IRepositoryTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TicketService implements ITicketService{

    @Autowired
    private IRepositoryTicket repoTicket;

    @Override
    public List<Ticket> getTickets() {
        try {
            List<Ticket> listaTickets = repoTicket.findAll();
            return listaTickets;
        } catch (Exception e) {
            // Manejar errores, lanzar excepciones o devolver una lista vacía según tus necesidades.
            System.out.println("Error al consultar Tickets!");
            return Collections.emptyList();
        }
    }

    @Override
    // NUEVO → tickets encendidos
    public List<Ticket> getTicketsEncendidos() {
        return repoTicket.findByEncendidoTrue();
    }

    @Override
    // NUEVO → tickets con alerta activa
    public List<Ticket> getTicketsConAlertaActiva() {
        return repoTicket.findTicketsConAlertaActiva();
    }

    @Override
    public void saveTicket(Ticket tic) {
        repoTicket.save(tic);
    }

    @Override
    public void deleteTicket() {
        Boolean encendido = false;
        List<Ticket> listaTickets = getTickets();

        try {
            if (!listaTickets.isEmpty()){
                for(Ticket tic : listaTickets){

                    if (encendido.equals(tic.getEncendido())){
                        repoTicket.delete(tic);
                    }
                }
            }
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @Override
    public Ticket findTicket(Long id) {
        Ticket tic = repoTicket.findById(id).orElse(null);
        return tic;
    }

    @Override
    public void editTicket(Ticket tic) {
        this.saveTicket(tic);
    }
}
