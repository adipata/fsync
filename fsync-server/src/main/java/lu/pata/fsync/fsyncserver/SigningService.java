package lu.pata.fsync.fsyncserver;

import lu.pata.fsync.api.SigningTool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class SigningService {
    Logger log= LoggerFactory.getLogger(SigningService.class);

    @Value("${key.file}")
    String keyFilename;
    @Value("${key.pass}")
    String keyPass;
    @Value("${key.alias}")
    String keyAlias;

    SigningTool signingTool;

    @PostConstruct
    public void init() {
        signingTool=new SigningTool(keyFilename,keyPass.toCharArray(),keyAlias);
    }

    public boolean verifySignature(byte[] data,byte[] signature,String crtAlias){
        try {
            return signingTool.verifySignature(data, signature, crtAlias);
        } catch (Exception ex){
            log.error("Error while calculating signature: "+ex.getMessage());
            return false;
        }
    }
}
