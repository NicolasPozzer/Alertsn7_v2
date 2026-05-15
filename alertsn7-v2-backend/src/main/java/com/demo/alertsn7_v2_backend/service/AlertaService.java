package com.demo.alertsn7_v2_backend.service;

import com.demo.alertsn7_v2_backend.model.ApiCoin;
import com.demo.alertsn7_v2_backend.model.Ticket;
import com.demo.alertsn7_v2_backend.repository.IRepositoryTicket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        //System.out.println("\n\t |Comprobacion Automatica de Alertas...| \n");

        try {
            // Llamar una sola vez a la lista de la api
            List<ApiCoin> listaCotizacionesTiempoReal = apiServ.listadoApiCoin();
            List<Ticket> listaTickets = ticketServ.getTickets();

            if (!listaTickets.isEmpty() && !listaCotizacionesTiempoReal.isEmpty()) {

                Map<String, ApiCoin> coinsMap = listaCotizacionesTiempoReal.stream()
                        .collect(Collectors.toMap(ApiCoin::getSymbol, coin -> coin));

                for (Ticket ticket : listaTickets) {

                    ApiCoin coin = coinsMap.get(ticket.getNombre());

                    if (coin == null) continue;

                    // ===== ALERTA ENCIMA =====
                    if (ticket.getEncendido() &&
                            "Encima".equals(ticket.getDireccion()) &&
                            coin.getCurrent_price() > ticket.getPrecioEstablecido()) {

                        String message = "🔔 Alerta para |" + ticket.getNombre() +
                                "| ¡El Precio Supero los: ⬆ $" + ticket.getPrecioEstablecido();

                        botServ.sendMessage(telegramClientID, message);

                        apagarTicketTemporalmente(ticket);
                    }

                    // ===== ALERTA DEBAJO =====
                    else if (ticket.getEncendido() &&
                            "Debajo".equals(ticket.getDireccion()) &&
                            coin.getCurrent_price() < ticket.getPrecioEstablecido()) {

                        String message = "🔔 Alerta para |" + ticket.getNombre() +
                                "| ¡Precio cayó ⬇ por debajo de $" + ticket.getPrecioEstablecido();

                        botServ.sendMessage(telegramClientID, message);

                        apagarTicketTemporalmente(ticket);
                    }
                }
            }else {
                System.out.println("Lista vacia nada para Comprobar!");
            }
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error al intentar comprobar alertas!");
        }
    }

    private void apagarTicketTemporalmente(Ticket ticket) {
        ticket.setEncendido(false);
        ticket.setColor("table-secondary");
        ticket.setEmitirAlerta(1);
        ticket.setEmitirAlertaHasta(LocalDateTime.now().plusSeconds(50));

        repoTicket.save(ticket);
    }

    @Scheduled(fixedRate = 21000) // cada 21 segundos
    @Transactional
    public void limpiarAlertasVencidas() {
        try {
            List<Ticket> tickets = repoTicket.findTicketsConAlertaActiva();

            for (Ticket ticket : tickets) {
                if (ticket.getEmitirAlerta() == 1 &&
                        ticket.getEmitirAlertaHasta() != null &&
                        LocalDateTime.now().isAfter(ticket.getEmitirAlertaHasta())) {

                    ticket.setEmitirAlerta(0);
                    repoTicket.save(ticket);
                }
            }
        } catch (Exception e) {
            System.out.println(e + "\nError en scheduler de la funcion limpiarAlertasVencidas()");
        }
    }

}
