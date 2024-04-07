package edu.bethlehem.scinexus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Article.*;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Post.*;
import edu.bethlehem.scinexus.ResearchPaper.*;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
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
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ResearchPaperController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class ResearchPaperControllerTest {




    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationService authService;



    @MockBean
    private ResearchPaperService researchPaperService;


    private User user;

    @Autowired
    private ObjectMapper objectMapper;

    private ResearchPaper researchPaper;
    private ResearchPaper anotherResearchPaper;





    @BeforeEach
    public void init() {

        researchPaper=ResearchPaper.builder()
                .title("Doesn't matter")
                .subject("SCIENCE")
                .content("HELLO")
                .createDateTime(LocalDateTime.now())
                .publisher(user)
                .visibility(Visibility.PUBLIC)
                .createDateTime(LocalDateTime.now())
                .build();

        anotherResearchPaper=researchPaper.builder()
                .title("Doesn't matter")
                .createDateTime(LocalDateTime.now())
                .subject("SCIENCE")
                .content("HI")
                .publisher(user)
                .visibility(Visibility.PRIVATE)
                .createDateTime(LocalDateTime.now())
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
    public void ResearchPaperController_GET_getAllResearchPapers_returnListOfResearchPaper() throws Exception {

        when(researchPaperService.findAllResearchPapers()).
                thenReturn(CollectionModel.of(List.of(EntityModel.of(researchPaper),EntityModel.of(anotherResearchPaper))));

        ResultActions response = mockMvc.perform(get("/researchpapers")
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void ResearchPaperController_GET_getOneResearchPaper_returnOneResearchPaper() throws Exception {

        when(researchPaperService.findResearchPaperById(1L)).thenReturn(EntityModel.of(researchPaper));

        ResultActions response = mockMvc.perform(get("/researchpapers/{id}", 1)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }
//
//
    @Test
    public void ResearchPaperController_POST_createNewResearchPaper_returnCreatedResearchPaper() throws Exception {

        ResearchPaperRequestDTO newResearchPaperRequestDTO = ResearchPaperRequestDTO.builder()
                .title("DoesntMatter")
                .visibility(Visibility.PRIVATE)
                .subject("NOT YOUR BUSINESS")
                .content("Hello")
                .visibility(Visibility.PRIVATE)
                .description("HEllo")
                .language(ResearchLanguage.CHINESE)
                .build();

        EntityModel<ResearchPaper> entityModel = EntityModel.of(new ResearchPaper());
        given(researchPaperService.createResearchPaper(eq(newResearchPaperRequestDTO),any(Authentication.class)))
                .willReturn(entityModel);

        // When & Then
        mockMvc.perform(post("/researchpapers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newResearchPaperRequestDTO))
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andExpect(status().isCreated());

    }


    @Test
    public void ResearchPaperController_POST_validateResearchPaper_returnOneValidatedResearchPaper() throws Exception {


        EntityModel<ResearchPaper> entityModel = EntityModel.of(new ResearchPaper());
        given(researchPaperService.validate(any(),any(Authentication.class)))
                .willReturn(entityModel);

        // When & Then
        mockMvc.perform(post("/researchpapers/{researchPaperId}/validate",1L)

                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }



    @Test
    public void ResearchPaperController_POST_RequestResearchPaperAccess_returnAccessedResearchPaper() throws Exception {


        EntityModel<ResearchPaper> entityModel = EntityModel.of(new ResearchPaper());
        given(researchPaperService.createResearchPaper(any(),any(Authentication.class)))
                .willReturn(entityModel);

        // When & Then
        mockMvc.perform(post("/researchpapers/{researchPaperId}/access",1L)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }
//
//
//    //    //This is Ideal Template for Patch Testing
    @Test
    public void ResearchPaperController_PATCH_UpdateResearchPaper_ReturnUpdatedResearchPaper() throws Exception {

        ResearchPaperRequestPatchDTO researchPaperRequestPatchDTO= ResearchPaperRequestPatchDTO.builder()
                .title("DoesntMatter")
                .visibility(Visibility.PRIVATE)
                .subject("NOT YOUR BUSINESS")
                .content("Hello")
                .visibility(Visibility.PRIVATE)
                .build();

        when(researchPaperService.updateResearchPaperPartially(1L,researchPaperRequestPatchDTO))
                .thenReturn(EntityModel.of(researchPaper));

        ResultActions response= mockMvc.perform(patch("/researchpapers/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/hal+json")
                        .content(objectMapper.writeValueAsString(researchPaperRequestPatchDTO))
                        .characterEncoding("utf-8"))
                .andDo(print());


        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print());


    }
//
    @Test
    public void ResearchPaperController_DELETE_DeleteResearchPaper_returnNoContent() throws Exception {

        doNothing().when(researchPaperService).deleteResearchPaper(1L);

        ResultActions response = mockMvc.perform(delete("/researchpapers/{id}", 1)
                        .accept("application/json"))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }

}
