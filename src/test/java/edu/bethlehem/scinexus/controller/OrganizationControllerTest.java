package edu.bethlehem.scinexus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Academic.AcademicRequestPatchDTO;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.Organization.OrganizationController;
import edu.bethlehem.scinexus.Organization.OrganizationRequestPatchDTO;
import edu.bethlehem.scinexus.Organization.OrganizationService;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.User.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = OrganizationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class OrganizationControllerTest {




    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationService authService;

    @MockBean
    private UserService userService;

    @MockBean
    private OrganizationService organizationService;

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
                .role(Role.ORGANIZATION)
                .education("Bethlehem University")
                .badge("I Don't know")
                .position(Position.ASSISTANT_PROFESSOR)
                .build();

    }



    @Test
    public void OrganizationController_GET_AllOrganizations_ReturnAllOrganizations() throws Exception {

        when(organizationService.findAllOrganizations()).
                thenReturn(CollectionModel.of(List.of(EntityModel.of(user),EntityModel.of(anotherUser))));

        ResultActions response = mockMvc.perform(get("/organizations")
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void OrganizationController_GET_getOneOrganizations_ReturnOneOrganizations() throws Exception {

        when(organizationService.findOrganizationById(1L)).thenReturn(EntityModel.of(user));

        ResultActions response = mockMvc.perform(get("/organizations/{organizationId}", 1L)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }




    //    //This is Ideal Template for Patch Testing
    @Test
    public void OrganizationController_PATCH_UpdateOrganizations_ReturnUpdatedOrganizations() throws Exception {

        OrganizationRequestPatchDTO organizationRequestPatchDTO=OrganizationRequestPatchDTO.builder()
                .type(OrganizationType.BUSINESS)
                .build();

        when(organizationService.updateOrganizationPartially(1L,organizationRequestPatchDTO))
                .thenReturn(EntityModel.of(user));

        ResultActions response= mockMvc.perform(patch("/organizations/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/hal+json")
                        .content(objectMapper.writeValueAsString(organizationRequestPatchDTO))
                        .characterEncoding("utf-8"))
                .andDo(print());


        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());


    }

    @Test
    public void OrganizationController_PATCH_AddAcademicToOrganization_ReturnAddedToOrganizations() throws Exception {



        when(organizationService.addAcademic( SecurityContextHolder.getContext().getAuthentication(),1L))
                .thenReturn(EntityModel.of(user));

        ResultActions response= mockMvc.perform(patch("/organizations/{academicId}/organization",1L)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }



    @Test
    public void OrganizationController_DELETE_DeleteOrganization_ReturnNoContent() throws Exception {

        doNothing().when(organizationService).deleteOrganization(1L);

        ResultActions response = mockMvc.perform(delete("/organizations/{id}", 1)
                        .accept("application/json"))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }
    @Test
    public void OrganizationController_DELETE_DeleteAcademicFromOrganization_ReturnNoContent() throws Exception {

        doNothing().when(organizationService).removeAcademic(1L,SecurityContextHolder.getContext().getAuthentication());

        ResultActions response = mockMvc.perform(delete("/organizations/{academicId}/organization", 1)
                        .accept("application/json"))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }

}
