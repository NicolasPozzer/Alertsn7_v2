package com.demo.alertsn7_v2_backend.service;

import com.demo.alertsn7_v2_backend.model.Ticket;
import com.demo.alertsn7_v2_backend.repository.IRepositoryTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TicketService implements ITicketService{

    @Autowired
    private IRepositoryTicket repoTicket;

    @Override
    public List<Ticket> getTickets() {
        List<Ticket> listaTickets = repoTicket.findAll();
        return listaTickets;
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
