package lu.pata.fsync.fsyncserver;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Base64;

@RestController
public class Encode {

    //TODO: implement @RequestBody
    @RequestMapping(value="/encode",method = RequestMethod.POST)
    public String encode(@RequestParam("fileName") String fileName,@RequestParam("data") String data){

        try(FileOutputStream fos=new FileOutputStream("data/"+fileName,true)) {
            fos.write(Base64.getDecoder().decode(data));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "Updated: "+fileName;
    }
}
