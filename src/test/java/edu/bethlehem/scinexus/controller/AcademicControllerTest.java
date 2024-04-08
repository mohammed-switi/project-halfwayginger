package edu.bethlehem.scinexus.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bethlehem.scinexus.Academic.AcademicController;
import edu.bethlehem.scinexus.Academic.AcademicRequestPatchDTO;
import edu.bethlehem.scinexus.Academic.AcademicService;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserService;

@WebMvcTest(controllers = AcademicController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class AcademicControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        private AuthenticationService authService;

        @MockBean
        private UserService userService;

        @MockBean
        private AcademicService academicService;

        @MockBean
        private UserRepository repository;

        private User user;
        private User anotherUser;

        @Autowired
        private ObjectMapper objectMapper;

        @BeforeEach
        public void init() {

                anotherUser = User.builder()
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
                                .build();

        }

        @Test
        public void AcademicController_GET_AllAcademics_ReturnAllAcademics() throws Exception {

                when(academicService.findAllAcademics()).thenReturn(
                                CollectionModel.of(List.of(EntityModel.of(user), EntityModel.of(anotherUser))));

                ResultActions response = mockMvc.perform(get("/academics")
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void AcademicController_GET_OneAcademics_ReturnOneAcademics() throws Exception {

                when(academicService.findAcademicById(1L)).thenReturn(EntityModel.of(user));

                ResultActions response = mockMvc.perform(get("/academics/{academicId}", 1L)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        // //This is Ideal Template for Patch Testing
        @Test
        public void AcademicController_Patch_UpdateAcademicPartially_ReturnAcademic() throws Exception {

                AcademicRequestPatchDTO academicRequestPatchDTO = AcademicRequestPatchDTO.builder()
                                .badge("Nothing")
                                .education("Bethlehem")
                                .position(Position.ASSISTANT_PROFESSOR)
                                .build();

                when(academicService.updateAcademicPartially(1L, academicRequestPatchDTO))
                                .thenReturn(EntityModel.of(user));

                ResultActions response = mockMvc.perform(patch("/academics/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept("application/hal+json")
                                .content(objectMapper.writeValueAsString(academicRequestPatchDTO))
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andDo(MockMvcResultHandlers.print());

        }

}
