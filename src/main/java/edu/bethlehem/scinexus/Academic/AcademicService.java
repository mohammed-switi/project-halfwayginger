package edu.bethlehem.scinexus.Academic;

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
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import edu.bethlehem.scinexus.Academic.AcademicNotFoundException;
import edu.bethlehem.scinexus.Academic.AcademicRequestDTO;
import edu.bethlehem.scinexus.Academic.AcademicRequestPatchDTO;
import edu.bethlehem.scinexus.DatabaseLoading.DataLoader;
import edu.bethlehem.scinexus.Organization.OrganizationNotFoundException;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import edu.bethlehem.scinexus.User.UserService;

@Service
@RequiredArgsConstructor
@Data
public class AcademicService {
    private final UserRepository userRepository;
    private final AcademicModelAssembler assembler;
    private final JwtService jwtService;
    private final UserService userService;
    Logger logger = LoggerFactory.getLogger(DataLoader.class);

    public User getUserById(long id) {
        logger.trace("Getting User by ID");
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User Not Found", HttpStatus.NOT_FOUND));

    }

    public EntityModel<User> findAcademicById(Long academicId) {
        logger.trace("Finding Academic by ID");
        User academic = userRepository.findByIdAndRole(academicId, Role.ACADEMIC)
                .orElseThrow(() -> new AcademicNotFoundException(academicId));

        return assembler.toModel(academic);
    }

    // We Should Specify An Admin Authority To get All Academics
    public CollectionModel<EntityModel<User>> findAllAcademics() {
        logger.trace("Finding All Academics");
        List<EntityModel<User>> academics = userRepository.findAllByRole(Role.ACADEMIC).stream()
                .map(academic -> assembler.toModel(academic))
                .collect(Collectors.toList());

        return CollectionModel.of(academics, linkTo(methodOn(AcademicController.class).all()).withSelfRel());

    }

    public User saveAcademic(User academic) {
        logger.trace("Saving Academic");
        return userRepository.save(academic);
    }

    // No need for any PUT method
    // public EntityModel<User> updateAcademic(Long academicId,
    // AcademicRequestDTO newAcademicRequestDTO) {
    // logger.trace("Updating Academic");

    // User academic = userRepository.findById(academicId)
    // .orElseThrow(() -> new AcademicNotFoundException(academicId));

    // try {
    // for (Method method : AcademicRequestDTO.class.getMethods()) {
    // if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
    // logger.trace("Property Name: " + method.getName());
    // Object value = method.invoke(newAcademicRequestDTO);

    // String propertyName = method.getName().substring(3); // remove "get"
    // if (propertyName.equals("Role")) {
    // continue;
    // }
    // Method setter = User.class.getMethod("set" + propertyName,
    // method.getReturnType());
    // setter.invoke(academic, value);

    // }
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }
    // return assembler.toModel(userRepository.save(academic));
    // }

    public EntityModel<User> updateAcademicPartially(Long academicId,
            AcademicRequestPatchDTO newAcademicRequestDTO) {
        logger.trace("Partially Updating Academic");
        User academic = userRepository.findById(academicId)
                .orElseThrow(() -> new AcademicNotFoundException(academicId, HttpStatus.UNPROCESSABLE_ENTITY));

        try {
            for (Method method : AcademicRequestPatchDTO.class.getMethods()) {
                if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
                    Object value = method.invoke(newAcademicRequestDTO);
                    if (value != null) {

                        String propertyName = method.getName().substring(3); // remove "get"
                        if (propertyName.equals("Class")) // Class is a reserved keyword in Java
                            continue;
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

    // We will be deleteing a user
    // public void deleteAcademic(Long academicId) {
    // logger.trace("Deleting Academic");
    // User academic = userRepository.findById(academicId)
    // .orElseThrow(() -> new AcademicNotFoundException(academicId,
    // HttpStatus.NOT_FOUND));
    // userRepository.delete(academic);
    // }

}
