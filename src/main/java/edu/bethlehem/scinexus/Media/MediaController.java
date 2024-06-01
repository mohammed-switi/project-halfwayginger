package edu.bethlehem.scinexus.Media;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.File.FileStorageService;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
public class MediaController {

  private final MediaRepository repository;
  private final UserRepository userRepository;
  private final MediaModelAssembler assembler;
  private final JwtService jwtService;
  @Autowired
  FileStorageService fileStorageManager;

  @GetMapping("/medias/{mediaId}")
  EntityModel<Media> one(@PathVariable Long mediaId, Authentication auth) {
    Long userId = jwtService.extractId(auth);
    User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("User Not Found"));

    Media media = repository.findByIdAndOwner(mediaId, user);
    if (media == null)
      throw new MediaNotFoundException(mediaId, HttpStatus.NOT_FOUND);
    return assembler.toModel(media);
  }

  @GetMapping("/medias")
  CollectionModel<EntityModel<Media>> all() {
    List<EntityModel<Media>> medias = repository.findAll().stream().map(media -> assembler.toModel(media))
        .collect(Collectors.toList());

    return CollectionModel.of(medias, linkTo(methodOn(MediaController.class).all()).withSelfRel());
  }

  @PostMapping("/medias")
  public CollectionModel<EntityModel<Media>> handleConcurrentFilesUpload(
      @RequestParam("files") MultipartFile[] files, Authentication auth) {
    return fileStorageManager.saveAll(files, auth);
  }

  @RequestMapping(value = "medias/{mediaId}/files", method = RequestMethod.GET)
  public ResponseEntity<?> getFile(@PathVariable("mediaId") Long mediaId) {
    Media media = repository.findById(mediaId)
        .orElseThrow(() -> new MediaNotFoundException(mediaId, HttpStatus.NOT_FOUND));
    String filename = media.getFileName();

    // Checking whether the file requested for download exists or not
    String fileUploadPath = System.getProperty("user.dir") + "/Uploads";
    Path filePath = Paths.get(fileUploadPath, filename);
    if (!Files.exists(filePath)) {
      return ResponseEntity.notFound().build();
    }

    // Creating a new InputStreamResource object
    InputStreamResource resource;
    try {
      resource = new InputStreamResource(new FileInputStream(filePath.toFile()));
    } catch (FileNotFoundException e) {
      throw new MediaNotFoundException(mediaId, HttpStatus.NOT_FOUND);
    }

    // Setting up values for contentType and headerValue
    String contentType;
    try {
      contentType = Files.probeContentType(filePath);
    } catch (IOException e) {
      contentType = "application/octet-stream";
    }

    String headerValue = "inline; filename=\"" + filename + "\"";

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(contentType))
        .header(HttpHeaders.CONTENT_DISPOSITION, headerValue)
        .header("X-FRAME-OPTIONS",
            "ALLOW-FROM http://localhost:5173/")
        .body(resource);
  }

  // Assuming this is a method that returns the list of filenames in the upload
  // directory

  private String[] getFiles() {
    File folder = new File(System.getProperty("user.dir") + "/Uploads");
    return folder.list((dir, name) -> new File(dir, name).isFile());
  }

  // Getting list of filenames that have been uploaded
  // @RequestMapping(value = "/getFiles", method = RequestMethod.GET)
  // public String[] getFiles() {
  // String folderPath = System.getProperty("user.dir") + "/Uploads";

  // // Creating a new File instance
  // File directory = new File(folderPath);

  // // list() method returns an array of strings
  // // naming the files and directories
  // // in the directory denoted by this abstract pathname
  // String[] filenames = directory.list();

  // // returning the list of filenames
  // return filenames;

  // }

  @DeleteMapping("/medias/{id}")
  ResponseEntity<?> deleteMedia(@PathVariable Long id) {

    Media media = repository.findById(id).orElseThrow(() -> new MediaNotFoundException(id, HttpStatus.NOT_FOUND));

    repository.delete(media);

    return ResponseEntity.noContent().build();

  }
}
