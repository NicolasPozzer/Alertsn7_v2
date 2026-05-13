package com.demo.alertsn7_v2_backend.service;

import com.demo.alertsn7_v2_backend.model.ApiCoin;
import com.demo.alertsn7_v2_backend.model.Ticket;
import com.demo.alertsn7_v2_backend.repository.IRepositoryTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class AlertaService implements IAlertaService{
    @Autowired
    private TicketService ticketServ;
    @Autowired
    private IRepositoryTicket repoTicket;
    @Autowired
    private ApiCoinService apiServ;
    @Autowired
    private BotService botServ;

    @Value("${telegram_chat_client_id}")
    private String telegramClientID;

    @Scheduled(cron = "0 */4 * * * *") //Ejecucion cada 4 minutos
    @Override
    public void emitirAlerta() {
        System.out.println("\n\t |Comprobacion Automatica de Alertas...| \n");

        try {
            /*LLamar una sola vez a la lista de la api*/
            List<ApiCoin> listaCotizacionesTiempoReal = apiServ.listadoApiCoin();
            List<Ticket> listaTickets = ticketServ.getTickets();
            if (!listaTickets.isEmpty() || !listaCotizacionesTiempoReal.isEmpty()){
                for (Ticket ticket : listaTickets) {
                    for (ApiCoin coin : listaCotizacionesTiempoReal) {

                        if (ticket.getNombre().equals(coin.getSymbol())) {
                            /*Saber si es mayor*/
                            if (ticket.getEncendido() && "Encima".equals(ticket.getDireccion())
                                    && coin.getCurrent_price() > ticket.getPrecioEstablecido()) {

                                /*Enviar mensaje*/
                                String chatId = telegramClientID;
                                String message = "🔔 Alerta para |"+ticket.getNombre()+
                                        "| ¡El Precio Supero los: ⬆ $"+ticket.getPrecioEstablecido();
                                botServ.sendMessage(chatId, message);

                                /*Cambiar el estado a -> Apagado*/
                                ticket.setEncendido(false);
                                ticket.setColor("table-secondary");
                                ticket.setEmitirAlerta(1);
                                this.repoTicket.save(ticket); // Guardar el cambio en la base de datos

                                // Lógica para cambiar el estado a 0 después de 50 segundos
                                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                                executor.schedule(() -> {
                                    ticket.setEmitirAlerta(0);
                                    this.repoTicket.save(ticket);
                                }, 50, TimeUnit.SECONDS);
                            }

                            /*Saber si es menor*/
                            else if (ticket.getEncendido() && "Debajo".equals(ticket.getDireccion())
                                    && coin.getCurrent_price() < ticket.getPrecioEstablecido()) {

                                /*Enviar mensaje*/
                                String chatId = telegramClientID;
                                String message = "🔔 Alerta para |"+ticket.getNombre()+
                                        "| ¡Precio cayó ⬇ por debajo de $"+ticket.getPrecioEstablecido();
                                botServ.sendMessage(chatId, message);

                                /*Cambiar el estado a -> Apagado*/
                                ticket.setEncendido(false);
                                ticket.setColor("table-secondary");
                                ticket.setEmitirAlerta(1);
                                this.repoTicket.save(ticket); // Guardar el cambio en la base de datos

                                // Lógica para cambiar el estado a 0 después de 50 segundos
                                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                                executor.schedule(() -> {
                                    ticket.setEmitirAlerta(0);
                                    this.repoTicket.save(ticket);
                                }, 50, TimeUnit.SECONDS);
                            }
                        }
                    }
                }
            }else {
                System.out.println("Lista vacia nada para Comprobar!");
            }
        }catch (Exception e){
            System.out.println("Error al intentar emitir alerta!");
        }
    }




    /*//////////////////////////////////////////////////////////////////////
   ///////////////////////Esto deberia Hacer el Front///////////////////////
    ////////////////////////////////////////////////////////////////////////
    @Scheduled(cron = "0/30 * * * * *")
    @Override
    public void emitirAlertaFrontend() {

        Grafica grafica1 = graficaServ.findGrafica(1L);

        List<Ticket> listaTickets = grafica1.getLista_tickets();

        if (!listaTickets.isEmpty()){
            for (Ticket tic : listaTickets){
                if (tic.getEmitirAlerta() == 1){
                    System.out.println("\n\t--ALERTA SONANDO!!!! EN EL FRONT.--\n");
                }else {
                    System.out.println("\n\t--NADA PARA EMITIR EN FRONT--\n");
                }
            }
        }else {
            System.out.println("Lista Vacia!");
        }
    }*/

}
