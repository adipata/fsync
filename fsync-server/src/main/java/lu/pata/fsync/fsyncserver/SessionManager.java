package lu.pata.fsync.fsyncserver;

import lu.pata.fsync.api.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

@Component
public class SessionManager {
    Logger log=LoggerFactory.getLogger(SessionManager.class);

    private Map<String, Session> sessions=new HashMap<>();
    private Random rnd=new Random();

    public Session createSession(String ip){
        String sid=getSidByIp(ip);

        Session ses=new Session(ip);
        rnd.nextBytes(ses.getChallenge());
        ses.setSid(sid);
        sessions.put(sid,ses);

        return ses;
    }

    public Session getSession(String sid){
        return sessions.get(sid);
    }

    private String getSidByIp(String ip){
        String ret=null;

        for(String k:sessions.keySet()){
            if(sessions.get(k).getIp().equals(ip)){
                ret=k;
                break;
            }
        }

        if(ret==null) ret=UUID.randomUUID().toString();

        return ret;
    }

    public void newChallenge(String sid){
        rnd.nextBytes(sessions.get(sid).getChallenge());
    }
}
