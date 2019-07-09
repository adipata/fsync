package lu.pata.fsync.fsyncclient;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class FileSend {
    public void send(){
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.valueOf(MediaType.APPLICATION_FORM_URLENCODED_VALUE));

            HttpEntity<String> request = new HttpEntity<>("fileName=cotoi&data=Q290b2kgdmFzaWxl",headers);
            String quote = restTemplate.postForObject("http://localhost:8080/encode", request, String.class);
            System.out.println(quote);
        } catch (org.springframework.web.client.HttpClientErrorException ex){
            System.out.println(ex.getResponseBodyAsString());
        }
    }
}
