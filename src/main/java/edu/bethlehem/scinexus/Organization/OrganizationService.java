package edu.bethlehem.scinexus.Organization;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.DatabaseLoading.DataLoader;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.User.UserService;

@Service
@RequiredArgsConstructor
@Data
public class OrganizationService {

    private final UserRepository userRepository;
    private final OrganizationModelAssembler assembler;
    private final JwtService jwtService;
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public User getUserById(long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public EntityModel<User> findOrganizationById(Long organizationId) {
        logger.trace("Finding Organization by ID");
        User organization = userRepository.findByIdAndRole(organizationId, Role.ORGANIZATION)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));

        return assembler.toModel(organization);
    }

    // We Should Specify An Admin Authority To get All organizations
    public CollectionModel<EntityModel<User>> findAllOrganizations() {
        logger.trace("Finding All Organizations");
        List<EntityModel<User>> organizations = userRepository.findAllByRole(Role.ORGANIZATION).stream()
                .map(organization -> assembler.toModel(organization))
                .collect(Collectors.toList());

        return CollectionModel.of(organizations, linkTo(methodOn(OrganizationController.class).all()).withSelfRel());

    }

    public EntityModel<User> updateOrganization(Long organizationId,
            OrganizationRequestDTO newOrganizationRequestDTO) {
        logger.trace("Updating Organization");
        User organization = userRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId));

        try {
            for (Method method : OrganizationRequestDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newOrganizationRequestDTO);

                    String propertyName = method.getName().substring(3); // remove "get"
                    Method setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                    setter.invoke(organization, value);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assembler.toModel(userRepository.save(organization));
    }

    public EntityModel<User> updateOrganizationPartially(Long organizationId,
            OrganizationRequestPatchDTO newOrganizationRequestDTO) {
        logger.trace("Partially Updating Organization");
        User organization = userRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId, HttpStatus.UNPROCESSABLE_ENTITY));

        try {
            for (Method method : OrganizationRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newOrganizationRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        Method setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(organization, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assembler.toModel(userRepository.save(organization));

    }

    public void deleteOrganization(Long organizationId) {
        logger.trace("Deleting Organization");
        User organization = userRepository.findById(organizationId)
                .orElseThrow(() -> new OrganizationNotFoundException(organizationId, HttpStatus.NOT_FOUND));
        userRepository.delete(organization);
    }

}
