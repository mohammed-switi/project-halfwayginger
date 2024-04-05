package edu.bethlehem.scinexus.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.User.*;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.hateoas.MediaTypes;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static reactor.core.publisher.Mono.when;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class UserControllerTest {


    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationService authService;


    @MockBean
   private UserService userService;


    private User user;
    private User anotherUser;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository repository;




@BeforeEach
public void init(){
   anotherUser= User.builder()
            .firstName("Moahmmed")
            .lastName("Sowait")
            .username("mohama")
            .email("mohamemd@example.com")
            .password("switI@2003!")
            .bio("HARD WORKING")
            .phoneNumber("0594242542")
            .fieldOfWork("IN METH")
            .role(Role.ORGANIZATION)
            .type(OrganizationType.BUSINESS)
            .education("")
            .badge("")
            .position(null)
            .build();

     user = User.builder()
            .firstName("Obadah")
            .lastName("Tahboub")
            .username("Obadahhhhg")
            .email("Obadah@example.com")
            .password("ObadahI@2003!")
            .bio("HARD WORKING, Lazy")
            .phoneNumber("0594242532")
            .fieldOfWork("IN METH")
            .role(Role.ACADEMIC)
            .education("Bethlehem University")
            .badge("I Don't know")
            .position(Position.ASSISTANT_PROFESSOR)
            .links(Arrays.asList(anotherUser))
            .build();
    repository.save(user);
}
    @Test
    public void UserController_getAllUsers_ReturnAllUsers() throws Exception {


        given(repository.findAll()).willReturn(Arrays.asList(
                User.builder()
                        .firstName("Obadah")
                        .lastName("Tahboub")
                        .username("Obadahhhhg")
                        .email("Obadah@example.com")
                        .password("ObadahI@2003!")
                        .bio("HARD WORKING, Lazy")
                        .phoneNumber("0594242532")
                        .fieldOfWork("IN METH")
                        .role(Role.ACADEMIC)
                        .education("Bethlehem University")
                        .badge("I Don't know")
                        .position(Position.ASSISTANT_PROFESSOR)
                        .build()
        ));

        ResultActions response= mockMvc.perform(get("/users")
                .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }



    @Test
    public void UserController_getOneUsers_ReturnOneUsers() throws Exception {

        given(userService.one(1L)).willReturn(EntityModel.of( User.builder()
                .firstName("Obadah")
                .lastName("Tahboub")
                .username("Obadahhhhg")
                .email("Obadah@example.com")
                .password("ObadahI@2003!")
                .bio("HARD WORKING, Lazy")
                .phoneNumber("0594242532")
                .fieldOfWork("IN METH")
                .role(Role.ACADEMIC)
                .education("Bethlehem University")
                .badge("I Don't know")
                .position(Position.ASSISTANT_PROFESSOR)
                .build())

        );

        ResultActions response= mockMvc.perform(get("/users/{id}",1)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void UserController_getUserLinks_ReturnUsersLinks() throws Exception {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        given(userService.getLinks(authentication))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response= mockMvc.perform(get("/users/links")
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                   .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    public void UserController_LinkUser_ReturnUser() throws Exception {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        given(userService.linkUser(authentication,1L))
                .willAnswer(invocation -> invocation.getArgument(0));

        ResultActions response= mockMvc.perform(put("/users/links/{userLinkTo}",1)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    public void UserController_DeleteUser_ReturnUser() throws Exception {

    doNothing().when(userService).deleteUser(1L);


        ResultActions response= mockMvc.perform(delete("/users/{id}",1)
                        .accept("application/json"))
                       .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }






}
