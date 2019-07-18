package lu.pata.fsync.fsyncserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.security.Security;

@SpringBootApplication
public class FsyncServerApplication {

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SpringApplication.run(FsyncServerApplication.class, args);
    }

}
