package edu.bethlehem.scinexus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Article.*;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.Interaction.*;
import edu.bethlehem.scinexus.Journal.Journal;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Opinion.Opinion;
import edu.bethlehem.scinexus.Post.*;
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
import org.springframework.security.core.context.SecurityContextHolder;
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

@WebMvcTest(controllers = InteractionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class InteractionControllerTest {



    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationService authService;



    @MockBean
    private ArticleService articleService;


    private User user;

    @Autowired
    private ObjectMapper objectMapper;


    @MockBean
    private InteractionService interactionService;
    private Interaction interaction;
    private Interaction anotherInteraction;

    private Opinion opinion;

    private Opinion anotherOpinion;


    private Journal journal;
    private Journal anotherJournal;



    @BeforeEach
    public void init() {



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

        journal= Journal.builder()
                .publisher(user)
                .content("HELLo")
                .visibility(Visibility.PRIVATE)
                .updateDateTime(LocalDateTime.now())
                .build();

        anotherJournal=Journal.builder()
                .publisher(user)
                .content("MEka")
                .visibility(Visibility.PUBLIC)
                .updateDateTime(LocalDateTime.now())
                .build();

        opinion = Opinion.builder()
                .opinionOwner(user)
                .journal(journal)
                .content("IDON")
                .createDateTime(LocalDateTime.now())
                .build();

        anotherOpinion= Opinion.builder()
                .opinionOwner(user)
                .journal(journal)
                .content("IDON")
                .createDateTime(LocalDateTime.now())
                .build();

        interaction= Interaction.builder()
                .journal(journal)
                .type(InteractionType.LOVE)
                .createDateTime(LocalDateTime.now())
                .interactorUser(user)
                .opinion(opinion)
                .build();

        anotherInteraction=Interaction.builder()
                .journal(journal)
                .type(InteractionType.LIKE)
                .createDateTime(LocalDateTime.now())
                .interactorUser(user)
                .opinion(opinion)
                .build();

    }



    @Test
    public void InteractionController_GET_AllInteractions_ReturnAllInteractions() throws Exception {

        when(interactionService.findAllInteractions()).
                thenReturn(CollectionModel.of(List.of(EntityModel.of(interaction),EntityModel.of(anotherInteraction))));

        ResultActions response = mockMvc.perform(get("/interactions")
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void ArticleController_GET_GetOneInteraction_ReturnOneInteraction() throws Exception {

        when(interactionService.findInteractionById(1L)).thenReturn(EntityModel.of(interaction));

        ResultActions response = mockMvc.perform(get("/interactions/{id}", 1)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }


    @Test
    public void InteractionController_POST_CreateOpinionInteraction_ReturnInteraction() throws Exception {

        InteractionRequestDTO interactionRequestDTO= InteractionRequestDTO.builder()
                .type(InteractionType.LOVE)
                .build();


        EntityModel<Interaction> entityModel = EntityModel.of(new Interaction());
        given(interactionService.addOpinionInteraction(any(),any(),any(Authentication.class)))
                .willReturn(entityModel);

        // When & Then
        mockMvc.perform(post("/interactions/opinion/{opinionId}",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(interactionRequestDTO))
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }

    @Test
    public void InteractionController_POST_CreateJournalInteraction_ReturnInteraction() throws Exception {

        InteractionRequestDTO interactionRequestDTO= InteractionRequestDTO.builder()
                .type(InteractionType.LOVE)
                .build();


        EntityModel<Interaction> entityModel = EntityModel.of(new Interaction());
        given(interactionService.addJournalInteraction(any(),any(),any(Authentication.class)))
                .willReturn(entityModel);

        // When & Then
        mockMvc.perform(post("/interactions/journal/{journalId}",1,1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(interactionRequestDTO))
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andExpect(status().isOk());

    }
//
//
//    //    //This is Ideal Template for Patch Testing
    @Test
    public void InteractionController_Patch_UpdateInteractionPartially_ReturnInteraction() throws Exception {

        InteractionRequestDTO interactionRequestDTO= InteractionRequestDTO.builder()
                .type(InteractionType.LOVE)
                .build();

        when(interactionService.updateInteraction(1L,interactionRequestDTO))
                .thenReturn(EntityModel.of(interaction));

        ResultActions response= mockMvc.perform(patch("/interactions/{id}",1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept("application/hal+json")
                        .content(objectMapper.writeValueAsString(interactionRequestDTO))
                        .characterEncoding("utf-8"))
                .andDo(print());


        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());


    }
//
    @Test
    public void InteractionController_DELETE_DeleteInteraction_ReturnNoContent() throws Exception {

        doNothing().when(interactionService).deleteInteraction(1L, SecurityContextHolder.getContext().getAuthentication());

        ResultActions response = mockMvc.perform(delete("/interactions/{id}", 1)
                        .accept("application/json"))
                .andDo(print());

        response.andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());

    }

}
