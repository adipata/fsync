package lu.pata.fsync.fsyncserver;

import lu.pata.fsync.api.FsyncData;
import lu.pata.fsync.api.FsyncDataResponse;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class Encode {

    @RequestMapping(value="/encode",method = RequestMethod.POST)
    public FsyncDataResponse encode(@RequestBody FsyncData req){

        try(FileOutputStream fos=new FileOutputStream("data/"+req.getFilename(),true)) {
            fos.write(Base64.getDecoder().decode(req.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FsyncDataResponse r=new FsyncDataResponse();
        r.setSuccess(true);
        return r;
    }
}
