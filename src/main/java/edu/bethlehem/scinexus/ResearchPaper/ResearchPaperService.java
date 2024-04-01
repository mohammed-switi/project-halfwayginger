package edu.bethlehem.scinexus.ResearchPaper;

import edu.bethlehem.scinexus.JPARepository.ResearchPaperRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.JPARepository.InteractionRepository;
import edu.bethlehem.scinexus.JPARepository.OpinionRepository;
import edu.bethlehem.scinexus.Organization.OrganizationNotFoundException;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.JPARepository.UserRepository;

@Service
@RequiredArgsConstructor
public class ResearchPaperService {

    @PersistenceContext
    private EntityManager entityManager;
    private final JwtService jwtService;
    private final ResearchPaperRepository researchPaperRepository;
    private final UserRepository userRepository;
    private final InteractionRepository interactionRepository;
    private final OpinionRepository opinionRepository;
    private final ResearchPaperModelAssembler assembler;
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
    public List<EntityModel<ResearchPaper>> findAllResearchPapers() {
        logger.trace("Fetching all research papers");
        return researchPaperRepository
                .findAll()
                .stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());
    }

    public ResearchPaper saveResearchPaper(ResearchPaper researchPaper) {
        return researchPaperRepository.save(researchPaper);
    }

    public EntityModel<ResearchPaper> createResearchPaper(ResearchPaperRequestDTO newResearchPaperRequestDTO,
            Authentication authentication) {
        logger.trace("Creating a new research paper");
        ResearchPaper researchPaper = convertResearchPaperDtoToResearchPaperEntity(authentication,
                newResearchPaperRequestDTO);
        logger.debug("Saving the research paper");
        return assembler.toModel(saveResearchPaper(researchPaper));
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

    public EntityModel<ResearchPaper> validate(Long researchPaperId, Authentication authentication) {
        logger.trace("Validating the research paper");
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(
                        () -> new ResearchPaperNotFoundException(researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));
        User organization = userRepository.findById(jwtService.extractId(authentication))
                .orElseThrow(() -> new OrganizationNotFoundException("Organization Not Found", HttpStatus.NOT_FOUND));
        logger.trace("Adding the organization to the research paper's validatedBy list");
        researchPaper.getValidatedBy().add(organization);
        organization.getValidated().add(researchPaper);

        userRepository.save(organization);
        logger.debug("Saving the organization");
        logger.debug("Saving the research paper");
        return assembler.toModel(researchPaperRepository.save(researchPaper));
    }

}
