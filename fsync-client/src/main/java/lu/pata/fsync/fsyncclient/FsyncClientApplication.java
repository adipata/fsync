package lu.pata.fsync.fsyncclient;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FsyncClientApplication implements CommandLineRunner {
    static Logger log=LoggerFactory.getLogger(FsyncClientApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(FsyncClientApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        CommandLine arguments=parseArguments(args);

        if(arguments.hasOption("send")){

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
        return options;
    }


    private void printHelp(){
        Options options=getOptions();
        HelpFormatter formatter=new HelpFormatter();
        formatter.printHelp("client.jar",options,true);
    }
}
