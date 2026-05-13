package service;



public class EnvironmentService implements IEnvironmentService{

    
    @Override
    public String urlServerBackend() {
        //Local
        String url = "http://localhost:8077";
        
        //Produccion
        //String url = "http://ec2-15-228-127-221.sa-east-1.compute.amazonaws.com:8080";
        return url;
    }
    
    
}
