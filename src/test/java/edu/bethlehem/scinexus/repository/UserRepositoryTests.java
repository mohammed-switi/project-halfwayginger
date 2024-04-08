package edu.bethlehem.scinexus.repository;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.Status;
import edu.bethlehem.scinexus.User.User;

@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@DataJpaTest
public class UserRepositoryTests {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void UserRepository_SaveAll_ReturnUser() {
        User user = User.builder()
                .firstName("Mohammed")
                .lastName("Sowaity")
                .username("mohammed")
                .email("mohammed@example.com")
                .password("MustEncrypt")
                .role(Role.ACADEMIC)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593333333")
                .bio("NOTHING")
                .fieldOfWork("Software")
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser).isNotNull();
        Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
    }

    @Test
    public void userRepository_GetAll_ReturnMoreThanOneUser() {
        User user = User.builder()
                .firstName("Mohammed")
                .lastName("Sowaity")
                .username("mohammed1")
                .email("mohammed1@example.com")
                .password("MustEncrypt")
                .role(Role.ACADEMIC)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0592022022")
                .bio("NOTHING")
                .fieldOfWork("Software")
                .build();

        User user2 = User.builder()
                .firstName("Obadah")
                .lastName("Tahboub")
                .username("obaada")
                .email("obadah@example.com")
                .password("Must'ntEncrypt")
                .role(Role.ACADEMIC)
                .position(Position.UNDERGRADUATE_STUDENT)
                .education("Bethlehem University")
                .phoneNumber("0593333334")
                .bio("SOMETHING")
                .fieldOfWork("IT")
                .build();

        userRepository.saveAll(List.of(user, user2));

        List<User> getUsers = userRepository.findAll();

        Assertions.assertThat(getUsers).isNotNull();
        Assertions.assertThat(getUsers.size()).isEqualTo(2);
    }

    @Test
    public void UserRepository_FindByID_GetById() {
        User user = User.builder()
                .firstName("Nasreen")
                .lastName("Tafish")
                .username("tafoosha")
                .email("tafosshaxd@example.com")
                .password("helloMadmozel")
                .role(Role.ORGANIZATION)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593032024")
                .bio("Come On")
                .type(OrganizationType.BUSINESS)
                .fieldOfWork("Zigner")
                .build();
        userRepository.save(user);

        User findByidUser = userRepository.findById(user.getId()).get();

        Assertions.assertThat(findByidUser).isNotNull();

    }

    @Test
    public void UserRepository_FindByStatus_ReturnUserNotNull() {
        User user = User.builder()
                .firstName("Salem")
                .lastName("Ismail")
                .username("som3a")
                .email("som3a@example.com")
                .password("hibyemeak")
                .role(Role.ORGANIZATION)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593052023")
                .bio("GO OUT!")
                .fieldOfWork("Mansef")
                .type(OrganizationType.BUSINESS)
                .status(Status.ONLINE)
                .build();

        userRepository.save(user);

        User statusUser = userRepository.findByStatus(Status.ONLINE).get();

        Assertions.assertThat(statusUser).isNotNull();
        Assertions.assertThat(statusUser.getStatus()).isEqualTo(Status.ONLINE);

    }

    @Test
    public void UserRepository_UpdatedUser_ReturnUserNotNull() {
        User user = User.builder()
                .firstName("Mousa")
                .lastName("Mehsed")
                .username("hased")
                .email("shaned@example.com")
                .password("borker")
                .role(Role.ORGANIZATION)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593052324")
                .bio("GO ON1")
                .fieldOfWork("MAHSHI")
                .type(OrganizationType.BUSINESS)
                .status(Status.ONLINE)
                .build();

        userRepository.save(user);

        User savedUser = userRepository.findById(user.getId()).get();

        savedUser.setStatus(Status.OFFLINE);
        savedUser.setFirstName("JSAM");

        User updatedUser = userRepository.save(savedUser);

        Assertions.assertThat(updatedUser.getFirstName()).isNotNull();
        Assertions.assertThat(updatedUser.getStatus()).isNotNull();

    }

    @Test
    public void UserRepository_DeleteUser_ReturnUserNotNull() {
        User user = User.builder()
                .firstName("MASHI")
                .lastName("BAKI")
                .username("LAKI")
                .email("sHAki@example.com")
                .password("bFAI")
                .role(Role.ORGANIZATION)
                .position(Position.ASSISTANT_PROFESSOR)
                .education("Bethlehem University")
                .phoneNumber("0593255324")
                .bio("GO ON SPORT")
                .fieldOfWork("SHESHKABAB")
                .type(OrganizationType.BUSINESS)
                .status(Status.ONLINE)
                .build();

        userRepository.save(user);

        userRepository.deleteById(user.getId());

        Optional<User> deletedUser = userRepository.findById(user.getId());

        Assertions.assertThat(deletedUser).isEmpty();

    }

}
