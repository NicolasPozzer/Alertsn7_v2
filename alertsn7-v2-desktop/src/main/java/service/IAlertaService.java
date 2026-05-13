package service;

import java.util.List;
import model.Ticket;


public interface IAlertaService {
    
    public Boolean checkStatusServer();
    
    public List<Ticket> listaAlertas();
    
    public void saveTicket(Ticket tic);
    
    public void deleteTicket();
    
}
