package service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import model.Ticket;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AlertaService implements IAlertaService {

    private EnvironmentService envServ;
    
    @Override
    public Boolean checkStatusServer() {
        EnvironmentService envServ = new EnvironmentService();

        String url = "/alertas/live";

        try {
            RestTemplate restTemplate = new RestTemplate();
            Boolean respuesta = restTemplate.getForObject(envServ.urlServerBackend() + url, Boolean.class);
            return respuesta;
        } catch (Exception e) {
            // Si ocurre algún error, imprime la traza de la excepción y devuelve una lista vacía
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Ticket> listaAlertas() {
        EnvironmentService envServ = new EnvironmentService();

        String url = "/alertas/list";

        try {
            RestTemplate restTemplate = new RestTemplate();
            Ticket[] PositionsArray = restTemplate.getForObject(envServ.urlServerBackend() + url, Ticket[].class);
            return Arrays.asList(PositionsArray);
        } catch (Exception e) {
            // Si ocurre algún error, imprime la traza de la excepción y devuelve una lista vacía
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public void saveTicket(Ticket tic) {
        String url = "/alertas/save";

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.postForEntity(envServ.urlServerBackend() + url, tic, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                System.out.println("Respuesta del servidor: " + responseBody);
            } else {
                System.err.println("Error al guardar el ticket. Código de estado: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteTicket() {
        String url = "/alertas/delete";
        
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(envServ.urlServerBackend() + url, HttpMethod.DELETE, null, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                System.out.println("Respuesta del servidor: " + responseBody);
            } else {
                System.err.println("Error al eliminar el ticket. Código de estado: " + response.getStatusCodeValue());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EnvironmentService getEnvServ() {
        return envServ;
    }

    public void setEnvServ(EnvironmentService envServ) {
        this.envServ = envServ;
    }

    public AlertaService(EnvironmentService envServ) {
        this.envServ = envServ;
    }

}
