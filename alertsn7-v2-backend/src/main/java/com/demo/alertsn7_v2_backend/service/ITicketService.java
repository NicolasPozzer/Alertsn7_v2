package com.demo.alertsn7_v2_backend.service;

import com.demo.alertsn7_v2_backend.model.Ticket;

import java.util.List;

public interface ITicketService {

    public List<Ticket> getTickets();

    public List<Ticket> getTicketsEncendidos();

    public List<Ticket> getTicketsConAlertaActiva();

    public void saveTicket(Ticket tic);

    public void deleteTicket();

    public Ticket findTicket(Long id);

    public void editTicket(Ticket tic);

}
