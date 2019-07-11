package lu.pata.fsync.fsyncclient;

import lu.pata.fsync.api.FsyncData;
import lu.pata.fsync.api.FsyncDataResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Component
public class FileSend {
    public void send(InputStream data){
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            FsyncData reqData=new FsyncData();
            reqData.setFilename("papucea");
            reqData.setData(Base64.getEncoder().encodeToString(data.readAllBytes()));

            HttpEntity<FsyncData> request = new HttpEntity<>(reqData,headers);
            FsyncDataResponse resp = restTemplate.postForObject("http://localhost:8080/encode", request, FsyncDataResponse.class);
            System.out.println(resp.isSuccess());
        } catch (org.springframework.web.client.HttpClientErrorException ex){
            System.out.println(ex.getResponseBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
