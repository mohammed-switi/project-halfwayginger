package edu.bethlehem.scinexus.Organization;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.Method;
import java.util.List;
import java.util.stream.Collectors;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import edu.bethlehem.scinexus.User.UserService;

@Service
@RequiredArgsConstructor
@Data
public class OrganizationService {

    private final UserRepository userRepository;
    private final OrganizationModelAssembler assembler;
    private final JwtService jwtService;
    private final UserService userService;

    public User getUserById(long id) {

        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public EntityModel<User> findOrganizationById(Long academicId) {

        User academic = userRepository.findById(academicId)
                .orElseThrow(() -> new OrganizationNotFoundException(academicId));

        return assembler.toModel(academic);
    }

    // We Should Specify An Admin Authority To get All Academics
    public CollectionModel<EntityModel<User>> findAllOrganizations() {

        List<EntityModel<User>> academics = userRepository.findAll().stream()
                .map(academic -> assembler.toModel(academic))
                .collect(Collectors.toList());

        return CollectionModel.of(academics, linkTo(methodOn(OrganizationController.class).all()).withSelfRel());

    }

    public EntityModel<User> updateOrganization(Long academicId,
            OrganizationRequestDTO newOrganizationRequestDTO) {

        User academic = userRepository.findById(academicId)
                .orElseThrow(() -> new OrganizationNotFoundException(academicId));

        try {
            for (Method method : OrganizationRequestDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newOrganizationRequestDTO);

                    String propertyName = method.getName().substring(3); // remove "get"
                    Method setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                    setter.invoke(academic, value);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return assembler.toModel(userRepository.save(academic));
    }

    public EntityModel<User> updateOrganizationPartially(Long academicId,
            OrganizationRequestPatchDTO newOrganizationRequestDTO) {

        User academic = userRepository.findById(academicId)
                .orElseThrow(() -> new OrganizationNotFoundException(academicId, HttpStatus.UNPROCESSABLE_ENTITY));

        try {
            for (Method method : OrganizationRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newOrganizationRequestDTO);
                    if (value != null) {
                        String propertyName = method.getName().substring(3); // remove "get"
                        Method setter = User.class.getMethod("set" + propertyName, method.getReturnType());
                        setter.invoke(academic, value);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return assembler.toModel(userRepository.save(academic));

    }

    public void deleteOrganization(Long academicId) {
        User academic = userRepository.findById(academicId)
                .orElseThrow(() -> new OrganizationNotFoundException(academicId, HttpStatus.NOT_FOUND));
        userRepository.delete(academic);
    }

}
