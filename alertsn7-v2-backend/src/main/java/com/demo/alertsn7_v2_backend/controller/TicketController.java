package com.demo.alertsn7_v2_backend.controller;

import com.demo.alertsn7_v2_backend.model.Ticket;
import com.demo.alertsn7_v2_backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/alertas")
public class TicketController {

    @Autowired //Inyectamos Dependencia
    private TicketService ticServ;

    /*End Points*/

    /*Lista de Tickets*/
    @GetMapping("/live")
    public Boolean getServerState(){
        return true;
    }

    /*Lista de Tickets*/
    @GetMapping("/list")
    public List<Ticket> getTickets(){
        return ticServ.getTickets();
    }

    /*Crear Ticket*/
    @PostMapping("/save")
    public ResponseEntity<String> saveTicket(@RequestBody Ticket tic){
        ticServ.saveTicket(tic);
        return ResponseEntity.ok("API y gráfica actualizadas correctamente.");
    }

    /*Borrar Ticket*/
    @DeleteMapping("/delete")
    public String deleteTicket(){
        ticServ.deleteTicket();
        return "El Ticket fue |Eliminado| Correctamente!";
    }

    /*Buscar un Ticket*/
    @GetMapping("/unaalerta/{id}")
    public Ticket findTicket(@PathVariable Long id){
        return ticServ.findTicket(id);
    }

    /*Editar un ticket*/
    @PutMapping("/edit")
    public String editTicket(@RequestBody Ticket tic){
        ticServ.editTicket(tic);
        return "Ticket |Editado| Correctamente!";
    }

}
