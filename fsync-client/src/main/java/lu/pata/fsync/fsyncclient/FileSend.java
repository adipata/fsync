package lu.pata.fsync.fsyncclient;

import lu.pata.fsync.api.*;
import me.tongfei.progressbar.ProgressBar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.InputStream;

@Component
public class FileSend {
    static Logger log= LoggerFactory.getLogger(FileSend.class);

    @Value("${app.chunksize}")
    String chunkSizeStr;

    @Value("${key.file}")
    String keyFilename;
    @Value("${key.alias}")
    String keyAlias;
    @Value("${key.pass}")
    String keyPass;

    @Value("${app.upload}")
    String pathUpload;
    @Value("${app.session}")
    String pathSession;

    SigningTool signingTool;

    CryptTool cryptTool;

    @PostConstruct
    public void init(){
        signingTool=new SigningTool(keyFilename,keyPass.toCharArray(), keyAlias);
    }

    public void send(InputStream data,String fileName,String encKey,String encKeySeed){
        cryptTool=new CryptTool(encKey,encKeySeed);
        sendFile(data,fileName);
    }

    private void sendFile(InputStream data,String fileName){
        int chunkSize=Integer.parseInt(chunkSizeStr);

        byte[] chunk=new byte[chunkSize];

        try {
            ProgressBar pb = new ProgressBar("Send file", data.available());
            pb.start();
            int rb;
            while((rb=data.read(chunk))>0){
                pb.stepBy(rb);
                byte[] chunkData=new byte[rb];
                System.arraycopy(chunk,0,chunkData,0,rb);
                chunkData=cryptTool.encrypt(chunkData);
                sendChunk(chunkData,fileName);
            }
            pb.stop();
            log.info("Send complete");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void sendChunk(byte[] data,String fileName) throws Exception {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers=new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Session ses=getSession();

            FsyncData reqData=new FsyncData();
            reqData.setFilename(fileName);
            reqData.setData(data);
            reqData.setSid(ses.getSid());
            reqData.setSignature(signingTool.sign(ses.getChallenge()));
            reqData.setAlias(keyAlias);

            HttpEntity<FsyncData> request = new HttpEntity<>(reqData,headers);
            FsyncDataResponse resp = restTemplate.postForObject(pathUpload, request, FsyncDataResponse.class);

            if(!resp.isSuccess()) throw new Exception("Server did't accept the file.");
    }

    private Session getSession() throws org.springframework.web.client.HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(pathSession, Session.class);
    }
}
