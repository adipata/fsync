package lu.pata.fsync.api;

public class Session {
    private byte[] challenge=new byte[20];
    private String ip;
    private String sid;

    public Session(String ip){
        this.ip=ip;
    }

    public Session(){}

    public byte[] getChallenge() {
        return challenge;
    }

    public void setChallenge(byte[] challenge) {
        this.challenge = challenge;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }
}
