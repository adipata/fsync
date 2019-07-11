package lu.pata.fsync.fsyncserver;

import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class Encode {

    //TODO: implement @RequestBody
    //@RequestParam("fileName") String fileName,@RequestParam("data") String data
    @RequestMapping(value="/encode",method = RequestMethod.POST)
    public FsyncResponse encode(@RequestBody FsyncResponse req){

        try(FileOutputStream fos=new FileOutputStream("data/"+req.getFileName(),true)) {
            fos.write(Base64.getDecoder().decode(req.getData()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FsyncResponse r=new FsyncResponse();
        r.setData("date din fisier");
        r.setFileName("nume fisier");

        return r;
    }
}
