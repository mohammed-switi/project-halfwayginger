package edu.bethlehem.scinexus.File;

import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalModelAssembler;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaModelAssembler;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Service
@AllArgsConstructor
public class FileStorageService {

    private final MediaRepository mediaRepository;
    private final JournalRepository journalRepository;
    private final UserRepository userRepository;
    private final MediaModelAssembler mediaAssembler;
    private final JournalModelAssembler journalAssembler;
    private final JwtService jwtService;

    public EntityModel<Media> save(MultipartFile file, Authentication auth) {
        Long userId = jwtService.extractId(auth);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());

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
        media.setOwner(user);
        return mediaAssembler.toModel(mediaRepository.save(media));
    }

    public CollectionModel<EntityModel<Media>> saveAll(MultipartFile[] files, Authentication auth) {
        Long userId = jwtService.extractId(auth);
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException());
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
            media.setOwner(user);
            mediaList.add(media);
        }

        return CollectionModel.of(mediaRepository.saveAll(mediaList)
                .stream()
                .map(mediaAssembler::toModel)
                .collect(Collectors.toList()));

    }

    public CollectionModel<EntityModel<Media>> saveAlltoJournal(MultipartFile[] files, Long journalId) {

        List<Media> mediaList = new ArrayList<Media>();
        Journal journal = journalRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
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
            journal.getMedias().add(media);
        }
        journalRepository.save(journal);
        return CollectionModel.of(mediaRepository.saveAll(mediaList)
                .stream()
                .map(mediaAssembler::toModel)
                .collect(Collectors.toList()));

    }
}