package edu.bethlehem.scinexus.File;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaModelAssembler;
import edu.bethlehem.scinexus.Media.MediaRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Service
@AllArgsConstructor
class FileStorageService {

    private final MediaRepository mediaRepository;
    private final MediaModelAssembler mediaAssembler;

    public EntityModel<Media> save(MultipartFile file) {
        Long time = System.currentTimeMillis();
        String filePath = System.getProperty("user.dir") + "/Uploads" + File.separator + time + " "
                + file.getOriginalFilename();
        try {

            // Creating an object of FileOutputStream class
            FileOutputStream fout = new FileOutputStream(filePath);
            fout.write(file.getBytes());

            // Closing the connection
            fout.close();

        }

        // Catch block to handle exceptions
        catch (Exception e) {
            e.printStackTrace();
        }

        Media media = new Media();
        media.setPath(filePath);
        media.setFileName(time + " " + file.getOriginalFilename());
        media.setType(file.getContentType());
        return mediaAssembler.toModel(mediaRepository.save(media));
    }

    public CollectionModel<EntityModel<Media>> saveAll(MultipartFile[] files) {

        List<Media> mediaList = new ArrayList<Media>();
        for (MultipartFile file : files) {
            Long time = System.currentTimeMillis();

            String filePath = System.getProperty("user.dir") + "/Uploads" + File.separator + time + " "
                    + file.getOriginalFilename();
            try {

                // Creating an object of FileOutputStream class
                FileOutputStream fout = new FileOutputStream(filePath);
                fout.write(file.getBytes());

                // Closing the connection
                fout.close();

            }

            // Catch block to handle exceptions
            catch (Exception e) {
                e.printStackTrace();
            }

            Media media = new Media();
            media.setPath(filePath);
            media.setFileName(time + " " + file.getOriginalFilename());
            media.setType(file.getContentType());
            mediaList.add(media);
        }

        return CollectionModel.of(mediaRepository.saveAll(mediaList)
                .stream()
                .map(mediaAssembler::toModel)
                .collect(Collectors.toList()));

    }
}