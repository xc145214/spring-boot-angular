package demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;
import java.util.List;

/**
 * Created by Administrator on 2015/8/4.
 */
@Controller
public class FileUploadController {

    @Autowired
    private Environment env;

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public
    @ResponseBody
    String provideUploadFile() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public
    @ResponseBody
    String handleFileUpload(@RequestParam("name") String name,
                            @RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {

                // Get the filename and build the local file path
                String filename = file.getOriginalFilename();
                String directory = env.getProperty("netgloo.paths.uploadedFiles");
                String filepath = Paths.get(directory, filename).toString();

                byte[] bytes = file.getBytes();
                BufferedOutputStream stream =
                        new BufferedOutputStream(new FileOutputStream(new File(filepath)));
                stream.write(bytes);
                stream.close();
                return "You successfully uploaded " + name + " into " + name + "-uploaded !";
            } catch (Exception e) {
                return "You failed to upload " + name + " => " + e.getMessage();
            }
        } else {
            return "You failed to upload " + name + " because the file was empty.";
        }
    }

    /**
     * 文件批量上传
     * @param request
     * @return
     */
    @RequestMapping(value="/batch/upload", method= RequestMethod.POST)
    public @ResponseBody
    String handleFileUpload(HttpServletRequest request){
        List<MultipartFile> files = ((MultipartHttpServletRequest)request).getFiles("file");
        for (int i =0; i< files.size(); ++i) {
            MultipartFile file = files.get(i);
            String name = file.getName();
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    BufferedOutputStream stream =
                            new BufferedOutputStream(new FileOutputStream(new File(name + i)));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    return "You failed to upload " + name + " => " + e.getMessage();
                }
            } else {
                return "You failed to upload " + name + " because the file was empty.";
            }
        }
        return "upload successful";
    }
}
