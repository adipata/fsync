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

import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileDownload {
    static Logger log= LoggerFactory.getLogger(FileDownload.class);

    @Value("${app.chunksize}")
    String chunkSizeStr;
    @Value("${app.finfo}")
    String pathFInfo;
    @Value("${app.download}")
    String pathDownload;

    CryptTool cryptTool;

    public void download(String filename,String encKey,String encKeySeed){
        cryptTool=new CryptTool(encKey,encKeySeed);
        int chunkSize=Integer.parseInt(chunkSizeStr)+16; // +16 to add the padding

        FileInfo fi=getFInfo(filename);
        if(fi.getSize()>0) {
            log.info("File size:" + fi.getSize());
            long bytesDownloaded=0;
            ProgressBar pb = new ProgressBar("Download file", fi.getSize());
            pb.start();

            while(bytesDownloaded<fi.getSize()){
                byte[] chunk=getChunk(filename,bytesDownloaded,chunkSize);
                if(chunk!=null) {
                    bytesDownloaded += chunk.length;

                    try (FileOutputStream fos = new FileOutputStream(filename, true)) {
                        fos.write(cryptTool.decrypt(chunk));
                    } catch (IOException e) {
                        log.error("Error while writing file: " + e.getMessage());
                        break;
                    }

                    pb.stepTo(bytesDownloaded);
                } else {
                    log.error("Received data chunk is null.");
                    break;
                }
            }

            pb.stop();
            log.info("Download complete");
        } else {
            log.error("File size is 0");
        }
    }

    private FileInfo getFInfo(String filename) throws org.springframework.web.client.HttpClientErrorException{
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(pathFInfo+filename, FileInfo.class);
    }

    private byte[] getChunk(String filename,long offset,long length){
        DataRequest dr=new DataRequest();
        dr.setFilename(filename);
        dr.setOffset(offset);
        dr.setLength(length);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers=new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<DataRequest> request = new HttpEntity<>(dr,headers);
        DataChunk dataChunk = restTemplate.postForObject(pathDownload, request, DataChunk.class);

        return dataChunk.getData();
    }
}
