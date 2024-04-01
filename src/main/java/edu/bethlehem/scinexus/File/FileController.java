
package edu.bethlehem.scinexus.File;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

import edu.bethlehem.scinexus.Media.Media;

@RestController
public class FileController {
    @Autowired
    FileStorageService fileStorageManager;

    @PostMapping("/single-file-upload")
    public ResponseEntity<?> handleFileUploadUsingCurl(
            @RequestParam("file") MultipartFile file, Authentication auth) throws IOException {
        return ResponseEntity.ok(fileStorageManager.save(file, auth));
    }

    @PostMapping("/multiple-files-upload")
    public CollectionModel<EntityModel<Media>> handleConcurrentFilesUpload(
            @RequestParam("files") MultipartFile[] files, Authentication auth) throws IOException {
        return fileStorageManager.saveAll(files, auth);
    }

    // Getting list of filenames that have been uploaded
    // @RequestMapping(value = "/getFiles", method = RequestMethod.GET)
    public String[] getFiles() {
        String folderPath = System.getProperty("user.dir") + "/Uploads";

        // Creating a new File instance
        File directory = new File(folderPath);

        // list() method returns an array of strings
        // naming the files and directories
        // in the directory denoted by this abstract pathname
        String[] filenames = directory.list();

        // returning the list of filenames
        return filenames;

    }

    // Downloading a file
    @RequestMapping(value = "/files/{path:.+}", method = RequestMethod.GET)
    public ResponseEntity downloadFile(@PathVariable("path") String filename)
            throws FileNotFoundException {

        // Checking whether the file requested for download exists or not
        String fileUploadpath = System.getProperty("user.dir") + "/Uploads";
        String[] filenames = this.getFiles();
        boolean contains = Arrays.asList(filenames).contains(filename);
        if (!contains) {
            return new ResponseEntity("File Not Found", HttpStatus.NOT_FOUND);
        }

        // Setting up the filepath
        String filePath = fileUploadpath + File.separator + filename;

        // Creating new file instance
        File file = new File(filePath);

        // Creating a new InputStreamResource object
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));

        // Creating a new instance of HttpHeaders Object
        HttpHeaders headers = new HttpHeaders();

        // Setting up values for contentType and headerValue
        String contentType = "application/octet-stream";
        String headerValue = "attachment; filename=\"" + resource.getFilename() +
                "\"";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
                .body(resource);

    }
}
