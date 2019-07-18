package lu.pata.fsync.fsyncserver;

import lu.pata.fsync.api.FsyncData;
import lu.pata.fsync.api.FsyncDataResponse;
import lu.pata.fsync.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.FileOutputStream;
import java.io.IOException;

@RestController
public class Upload {
    Logger log= LoggerFactory.getLogger(Upload.class);

    @Autowired
    SessionManager sessionManager;

    @Autowired
    SigningService signingService;

    @RequestMapping(value="/upload",method = RequestMethod.POST)
    public FsyncDataResponse upload(@RequestBody FsyncData req,HttpServletRequest request){
        FsyncDataResponse r=new FsyncDataResponse();
        Session ss=sessionManager.getSession(req.getSid());
        if(ss!=null || !ss.getIp().equals(request.getRemoteAddr())){
            if(signingService.verifySignature(sessionManager.getSession(req.getSid()).getChallenge(),req.getSignature(),req.getAlias())){
                try(FileOutputStream fos=new FileOutputStream("data/"+req.getFilename(),true)) {
                    fos.write(req.getData());
                    sessionManager.newChallenge(req.getSid());
                    r.setSuccess(true);
                } catch (IOException e) {
                    log.error("Error while writing file: "+e.getMessage());
                    r.setSuccess(false);
                }
            } else {
                log.error("Client provided an invalid signature.");
                r.setSuccess(false);
            }
        } else {
            log.error("Provided client session is not valid.");
            r.setSuccess(false);
        }

        return r;
    }

    @RequestMapping(value = "/session", method = RequestMethod.GET)
    public Session session(HttpServletRequest request){
        Session sd=sessionManager.createSession(request.getRemoteAddr());

        return sd;
    }
}
