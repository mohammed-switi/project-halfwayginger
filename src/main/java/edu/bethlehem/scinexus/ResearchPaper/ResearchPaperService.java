package edu.bethlehem.scinexus.ResearchPaper;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.Organization.OrganizationNotFoundException;
import edu.bethlehem.scinexus.Organization.OrganizationRepository;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperNotFoundException;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperRequestPatchDTO;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaperRequestDTO;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import edu.bethlehem.scinexus.User.UserResponsDTO;

@Service
@RequiredArgsConstructor
public class ResearchPaperService {

    @PersistenceContext
    private EntityManager entityManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ResearchPaperRepository researchPaperRepository;
    private final OrganizationRepository organizationRepository;
    private final ResearchPaperModelAssembler assembler;

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
        ResearchPaper researchPaper = convertResearchPaperDtoToResearchPaperEntity(authentication,
                newResearchPaperRequestDTO);
        return assembler.toModel(saveResearchPaper(researchPaper));
    }

    public EntityModel<ResearchPaper> updateResearchPaper(Long researchPaperId,
            ResearchPaperRequestDTO newResearchPaperRequestDTO) {

        return researchPaperRepository.findById(
                researchPaperId)
                .map(researchPaper -> {
                    researchPaper.setContent(newResearchPaperRequestDTO.getContent());
                    researchPaper.setVisibility(newResearchPaperRequestDTO.getVisibility());
                    researchPaper.setLanguage(newResearchPaperRequestDTO.getLanguage());
                    researchPaper.setTitle(newResearchPaperRequestDTO.getTitle());
                    researchPaper.setSubject(newResearchPaperRequestDTO.getSubject());
                    researchPaper.setDescription(newResearchPaperRequestDTO.getDescription());

                    return assembler.toModel(researchPaperRepository.save(researchPaper));
                })
                .orElseThrow(() -> new ResearchPaperNotFoundException(
                        researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));
    }

    public EntityModel<ResearchPaper> updateResearchPaperPartially(Long researchPaperId,
            ResearchPaperRequestPatchDTO newResearchPaperRequestDTO) {

        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(
                        () -> new ResearchPaperNotFoundException(researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));

        try {
            for (Method method : ResearchPaperRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newResearchPaperRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        Method setter = ResearchPaper.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(researchPaper, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assembler.toModel(researchPaperRepository.save(researchPaper));

    }

    public void deleteResearchPaper(Long researchPaperId) {
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(
                        () -> new ResearchPaperNotFoundException(researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));
        researchPaperRepository.delete(researchPaper);
    }

    public EntityModel<ResearchPaper> validate(Long researchPaperId, Authentication authentication) {
        ResearchPaper researchPaper = researchPaperRepository.findById(researchPaperId)
                .orElseThrow(
                        () -> new ResearchPaperNotFoundException(researchPaperId, HttpStatus.UNPROCESSABLE_ENTITY));
        Organization organization = organizationRepository.findById(jwtService.extractId(authentication))
                .orElseThrow(() -> new OrganizationNotFoundException("Organization Not Found", HttpStatus.NOT_FOUND));
        researchPaper.getValidatedBy().add(organization);
        organization.getValidated().add(researchPaper);

        organizationRepository.save(organization);
        return assembler.toModel(researchPaperRepository.save(researchPaper));
    }

}
