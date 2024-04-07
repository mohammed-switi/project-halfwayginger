package edu.bethlehem.scinexus.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Auth.*;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.OrganizationType;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import jakarta.mail.internet.MimeMessage;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
// import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(controllers = AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class AuthenticationControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        private AuthenticationService authService;

        @Autowired
        private ObjectMapper objectMapper;

        private RegisterRequest registerOrganizationRequest;
        private RegisterRequest registerAcademicRequest;

        private AuthenticationResponse authenticationResponse;

        private AuthenticationRequest authenticationRequest;

        @BeforeEach
        public void init() {
                registerOrganizationRequest = RegisterRequest.builder()
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
                                .position(null) // Set default value for Position or remove this line if it's not
                                                // nullable
                                .build();

                registerAcademicRequest = RegisterRequest.builder()
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

                authenticationRequest = AuthenticationRequest.builder()
                                .email("Obadah@example.com")
                                .password("ObadahI@2003!")
                                .build();

        }

        @Test
        public void AuthenticationController_CreateOrginizationalUser_ReturnAuthenticationResponse() throws Exception {

                given(authService.register(ArgumentMatchers.any()))
                                .willAnswer(invocation -> invocation.getArgument(0));

                ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerOrganizationRequest))
                                .characterEncoding("utf-8"));

                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                                                CoreMatchers.is(registerOrganizationRequest.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                                                CoreMatchers.is(registerOrganizationRequest.getEmail())))

                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void AuthenticationController_CreateAcademicUser_ReturnAuthenticationResponse() throws Exception {

                given(authService.register(ArgumentMatchers.any()))
                                .willAnswer(invocation -> invocation.getArgument(0));

                ResultActions response = mockMvc.perform(post("/api/v1/auth/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(registerAcademicRequest))
                                .characterEncoding("utf-8"));

                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName",
                                                CoreMatchers.is(registerAcademicRequest.getFirstName())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.email",
                                                CoreMatchers.is(registerAcademicRequest.getEmail())))

                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void AuthenticationController_LoginRequest_ReturnAuthenticationResponse() throws Exception {

                AuthenticationResponse authenticationResponse = new AuthenticationResponse("Dummy Jwt Token",
                                "Dummy ConfirmationToken");
                System.out.println(authenticationResponse);
                given(authService.authenticate(ArgumentMatchers.any()))
                                .willReturn(authenticationResponse);

                ResultActions response = mockMvc.perform(post("/api/v1/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(authenticationRequest))
                                .characterEncoding("utf-8"));

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andExpect(MockMvcResultMatchers.jsonPath("$.jwtToken",
                                                CoreMatchers.is(authenticationResponse.getJwtToken())))
                                .andExpect(MockMvcResultMatchers.jsonPath("$.confirmationToken",
                                                CoreMatchers.is(authenticationResponse.getConfirmationToken())))

                                .andDo(MockMvcResultHandlers.print());

        }

}
