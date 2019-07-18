package lu.pata.fsync.fsyncserver;

import lu.pata.fsync.api.DataChunk;
import lu.pata.fsync.api.DataRequest;
import lu.pata.fsync.api.FileInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@RestController
public class Download {
    Logger log= LoggerFactory.getLogger(Download.class);

    @RequestMapping(value = "/download", method = RequestMethod.POST)
    public DataChunk download(@RequestBody DataRequest req, HttpServletRequest request){
        DataChunk d=new DataChunk();

        File f = new File("data/"+req.getFilename());
        if(f.exists()){
            try(FileInputStream is=new FileInputStream(f)){
                is.skip(req.getOffset());
                ByteArrayOutputStream os=new ByteArrayOutputStream();
                int b;
                int readb=0;
                while((b=is.read())!=-1 && readb<req.getLength()){
                    os.write(b);
                    readb++;
                }
                d.setData(os.toByteArray());
            } catch(IOException ex){
                log.error("Error reading file "+req.getFilename()+":"+ex.getMessage());
            }
        }

        return d;
    }

    @RequestMapping(value = "/info", method = RequestMethod.GET)
    public FileInfo fileInfo(@RequestParam("fn") String fileName, HttpServletRequest request){
        File f = new File("data/"+fileName);

        FileInfo fi=new FileInfo();

        if(f.exists()) fi.setSize(f.length());

        return fi;
    }
}
