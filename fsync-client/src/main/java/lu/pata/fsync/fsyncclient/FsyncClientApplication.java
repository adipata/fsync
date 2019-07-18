package lu.pata.fsync.fsyncclient;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.io.FileInputStream;
import java.security.Security;

@SpringBootApplication
public class FsyncClientApplication implements CommandLineRunner {
    static Logger log=LoggerFactory.getLogger(FsyncClientApplication.class);

    @Autowired
    FileSend fileSend;

    @Autowired
    FileDownload fileDownload;

    public static void main(String[] args) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        SpringApplication.run(FsyncClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CommandLine arguments=parseArguments(args);

        if(arguments.hasOption("file")
                && arguments.hasOption("key")
                && arguments.hasOption("keys")){

            if(arguments.hasOption("send"))
                fileSend.send(new FileInputStream(new File(arguments.getOptionValue("send"))),
                        arguments.getOptionValue("file"),
                        arguments.getOptionValue("key"),
                        arguments.getOptionValue("keys"));

            if(arguments.hasOption("receive"))
                fileDownload.download(arguments.getOptionValue("file"),
                        arguments.getOptionValue("key"),
                        arguments.getOptionValue("keys"));
        } else {
            printHelp();
        }
    }

    private CommandLine parseArguments(String[] args){
        Options options=getOptions();
        CommandLine line=null;
        CommandLineParser parser=new DefaultParser();

        try {
            line=parser.parse(options,args);
        } catch (ParseException e) {
            log.error(e.getMessage());
        }

        return line;
    }

    private Options getOptions(){
        Options options=new Options();
        options.addOption("s","send",true,"Send a file");
        options.addOption("r","receive",true,"Receive a file");
        options.addOption("f","file",true,"The file name on the server");
        options.addOption("k","key",true,"Encryption key");
        options.addOption("ks","keys",true,"Key seed");
        return options;
    }

    private void printHelp(){
        Options options=getOptions();
        HelpFormatter formatter=new HelpFormatter();
        formatter.printHelp("client.jar",options,true);
    }
}
