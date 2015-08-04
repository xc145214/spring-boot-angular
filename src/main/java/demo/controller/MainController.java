package demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Paths;

/**
 * Created by Administrator on 2015/8/4.
 */
@Controller
public class MainController {

    // Define the log object for this class
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Environment env;

    /**
     * show index page
     *
     * @return
     */
    @RequestMapping("/")
    public String index() {

        // Log a simple message
        log.debug("debug level log");
        log.info("info level log");
        log.error("error level log");

        // Log a formatted string with parameters
        log.info("another info log with {}, {} and {} arguments", 1, "2", 3.0);

        return "hello.html";
    }

    /**
     * POST /uploadFile -> receive and locally save a file.
     *
     * @param uploadfile The uploaded file as Multipart file parameter in the
     *                   HTTP request. The RequestParam name must be the same of the attribute
     *                   "name" in the input tag with type file.
     * @return An http OK status in case of success, an http 4xx status in case
     * of errors.
     */
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(
            @RequestParam("uploadfile") MultipartFile uploadfile) {

        try {
            // Get the filename and build the local file path
            String filename = uploadfile.getOriginalFilename();
            String directory = env.getProperty("netgloo.paths.uploadedFiles");
            String filepath = Paths.get(directory, filename).toString();

            // Save the file locally
            BufferedOutputStream stream =
                    new BufferedOutputStream(new FileOutputStream(new File(filepath)));
            stream.write(uploadfile.getBytes());
            stream.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    } // method uploadFile

}
