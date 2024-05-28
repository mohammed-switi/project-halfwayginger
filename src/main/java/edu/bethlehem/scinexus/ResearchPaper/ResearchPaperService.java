package edu.bethlehem.scinexus.ResearchPaper;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.JPARepository.InteractionRepository;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.JPARepository.ResearchPaperRepository;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.JPARepository.UserResearchPaperRequestRepository;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.JournalNotFoundException;
import edu.bethlehem.scinexus.Journal.MediaIdDTO;
import edu.bethlehem.scinexus.Media.Media;
import edu.bethlehem.scinexus.Media.MediaNotFoundException;
import edu.bethlehem.scinexus.Notification.NotificationService;
import edu.bethlehem.scinexus.Organization.OrganizationNotFoundException;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.UserResearchPaper.ResearchPaperRequestKey;
import edu.bethlehem.scinexus.UserResearchPaper.UserResearchPaperRequest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import edu.bethlehem.scinexus.JPARepository.JournalRepository;
import edu.bethlehem.scinexus.JPARepository.MediaRepository;

@Service
@RequiredArgsConstructor
public class ResearchPaperService {

    @PersistenceContext
    private EntityManager entityManager;
    private final JournalRepository journalRepository;
    private final MediaRepository mediaRepository;
    private final JwtService jwtService;
    private final ResearchPaperRepository researchPaperRepository;
    private final UserResearchPaperRequestRepository userResearchPaperRequestRepository;
    private final UserRepository userRepository;
    private final InteractionRepository interactionRepository;
    private final OpinionRepository opinionRepository;
    private final ResearchPaperModelAssembler assembler;
    private final NotificationService notificationService;
    Logger logger = LoggerFactory.getLogger(ResearchPaperService.class);

    public ResearchPaper convertResearchPaperDtoToResearchPaperEntity(Authentication authentication,
            ResearchPaperRequestDTO ResearchPaperRequestDTO) {

        return ResearchPaper.builder()
                .content(ResearchPaperRequestDTO.getContent())
                .visibility(ResearchPaperRequestDTO.getVisibility())
                .language(ResearchPaperRequestDTO.getLanguage())
                .title(ResearchPaperRequestDTO.getTitle())
                .subject(ResearchPaperRequestDTO.getSubject())
                .description(ResearchPaperRequestDTO.getDescription())
                .publisher(getUserId(jwtService.extractId(authentication)))
                .build();
    }

    public User getUserId(long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public EntityModel<ResearchPaper> findResearchPaperById(Long ResearchPaperId) {

        ResearchPaper researchPaper = researchPaperRepository.findById(
                ResearchPaperId)
                .orElseThrow(() -> new ResearchPaperNotFoundException(ResearchPaperId));

        return assembler.toModel(researchPaper);
    }

    // We Should Specify An Admin Authority To get All ResearchPapers
    public CollectionModel<EntityModel<ResearchPaper>> findAllResearchPapers() {
        logger.trace("Fetching all research papers");
        return CollectionModel.of(researchPaperRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList()));
    }

    public ResearchPaper saveResearchPaper(ResearchPaper researchPaper) {
        return researchPaperRepository.save(researchPaper);
    }

    public EntityModel<ResearchPaper> createResearchPaper(ResearchPaperRequestDTO newResearchPaperRequestDTO,
            Authentication authentication) {
        logger.trace("Creating a new research paper");
        ResearchPaper researchPaper = convertResearchPaperDtoToResearchPaperEntity(authentication,
                newResearchPaperRequestDTO);

        researchPaper = saveResearchPaper(researchPaper);
        notificationService.notifyLinks(
                jwtService.extractId(authentication),
                "A new Research Paper from your links",
                linkTo(methodOn(
                        ResearchPaperController.class).one(researchPaper.getId())));
        logger.debug("Saving the research paper");
        return assembler.toModel(researchPaper);
    }

    public EntityModel<ResearchPaper> updateResearchPaper(Long researchPaperId,
            ResearchPaperRequestDTO newResearchPaperRequestDTO) {
        logger.trace("Updating the research paper");
        return researchPaperRepository.findById(
                researchPaperId)
                .map(researchPaper -> {
                    researchPaper.setContent(newResearchPaperRequestDTO.getContent());
                    researchPaper.setVisibility(newResearchPaperRequestDTO.getVisibility());
                    researchPaper.setLanguage(newResearchPaperRequestDTO.getLanguage());
                    researchPaper.setTitle(newResearchPaperRequestDTO.getTitle());
                    researchPaper.setSubject(newResearchPaperRequestDTO.getSubject());
                    researchPaper.setDescription(newResearchPaperRequestDTO.getDescription());
                    logger.debug("Saving the research paper");
                    return assembler.toModel(researchPaperRepository.save(researchPaper));
                })
                .orElseThrow(() -> new ResearchPaperNotFoundException(
                        researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));
    }

    public EntityModel<ResearchPaper> updateResearchPaperPartially(Long researchPaperId,
            ResearchPaperRequestPatchDTO newResearchPaperRequestDTO) {
        logger.trace("Partially updating the research paper");
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(
                        () -> new ResearchPaperNotFoundException(researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));

        try {
            for (Method method : ResearchPaperRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newResearchPaperRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        logger.trace("Updating researchPaper property: " + propertyName);
                        if (propertyName.equals("Class")) // Class is a reserved keyword in Java
                            continue;
                        Method setter = ResearchPaper.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(researchPaper, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        logger.debug("Saving the research paper");
        return assembler.toModel(researchPaperRepository.save(researchPaper));

    }

    public void deleteResearchPaper(Long researchPaperId) {
        logger.trace("Deleting the research paper");
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(
                        () -> new ResearchPaperNotFoundException(researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));
        // I had to add these lines to delete the opinions and interactions
        // of the article cuz the cascading didn't work

        researchPaper.getInteractions().forEach(interaction -> interactionRepository.delete(interaction));
        researchPaper.getOpinions().forEach(opinion -> opinionRepository.delete(opinion));
        logger.trace("Deleting the research paper's interactions and opinions");
        researchPaperRepository.delete(researchPaper);
        logger.debug("Deleting the research paper");
    }

    EntityModel<ResearchPaper> requestAccessToResearchPaper(Long researchPaperId,
            Authentication authentication) {
        logger.debug("requesting access to ResearchPaper with id: " + researchPaperId);
        User user = jwtService.getUser(authentication);
        logger.trace("Got user with id: " + user.getId());
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(() -> new ResearchPaperNotFoundException(
                        "The ResearchPaper with id:" + researchPaperId + " is not found",
                        HttpStatus.NOT_FOUND));
        UserResearchPaperRequest urpr = userResearchPaperRequestRepository.findByUserAndResearchPaper(user,
                researchPaper);
        if (urpr != null)
            throw new UserNotFoundException("A request is already has been made", HttpStatus.CONFLICT);
        UserResearchPaperRequest userResearchPaperRequest = new UserResearchPaperRequest();
        userResearchPaperRequest.setId(new ResearchPaperRequestKey(user.getId(), researchPaperId));
        userResearchPaperRequest.setUser(user);
        userResearchPaperRequest.setResearchPaper(researchPaper);
        userResearchPaperRequest.setAccepted(false);
        userResearchPaperRequestRepository.save(userResearchPaperRequest);
        return assembler.toModel(researchPaper);

    }

    EntityModel<ResearchPaper> respondToRequest(Boolean answer, Long researchPaperId, Long userId) {
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(() -> new ResearchPaperNotFoundException(researchPaperId));
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        UserResearchPaperRequest urpr = userResearchPaperRequestRepository.findByUserAndResearchPaper(user,
                researchPaper);
        if (urpr == null)
            throw new ResearchPaperNotFoundException("Requset is not foung for user with id: " + userId
                    + " and researchpaper with id: " + researchPaperId, HttpStatus.NOT_FOUND);
        if (!answer)
            userResearchPaperRequestRepository.delete(urpr);

        else {
            urpr.setAccepted(true);
            userResearchPaperRequestRepository.save(urpr);

        }
        return assembler.toModel(researchPaper);
    }

    public EntityModel<ResearchPaper> validate(Long researchPaperId, Authentication authentication) {
        logger.trace("Validating the research paper");
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(
                        () -> new ResearchPaperNotFoundException(researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));
        User organization = userRepository.findByIdAndRole(jwtService.extractId(authentication), Role.ORGANIZATION)
                .orElseThrow(() -> new OrganizationNotFoundException("Organization Not Found", HttpStatus.NOT_FOUND));
        logger.trace("Adding the organization to the research paper's validatedBy list");
        researchPaper.getValidatedBy().add(organization);
        organization.getValidated().add(researchPaper);

        userRepository.save(organization);
        logger.debug("Saving the organization");
        logger.debug("Saving the research paper");
        return assembler.toModel(researchPaperRepository.save(researchPaper));
    }

    public EntityModel<ResearchPaper> attachMedia(Long journalId, MediaIdDTO mediaIds) {
        ResearchPaper journal = researchPaperRepository.findById(journalId)
                .orElseThrow(() -> new JournalNotFoundException(journalId, HttpStatus.NOT_FOUND));
        for (Long mediaId : mediaIds.getMediaIds()) {
            Media media = mediaRepository.findById(mediaId)
                    .orElseThrow(() -> new MediaNotFoundException(mediaId, HttpStatus.NOT_FOUND));
            if (media.getOwnerJournal() != null)
                throw new MediaNotFoundException("media is already attached to a journal", HttpStatus.CONFLICT);
            journal.setJouranlFile(media);
            // media.setOwnerJournal(journal);
        }
        // mediaRepository.saveAll(journal.getMedias());
        return assembler.toModel(researchPaperRepository.save(journal));
    }


    public Long getResearchPaperCount(Long userId) {


       Long count = researchPaperRepository.findByPublisherId(userId).stream().count();


       return  count;
    }
}
