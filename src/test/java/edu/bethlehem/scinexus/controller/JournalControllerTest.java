package edu.bethlehem.scinexus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.Interaction.Interaction;
import edu.bethlehem.scinexus.Interaction.InteractionType;
import edu.bethlehem.scinexus.Journal.*;
import edu.bethlehem.scinexus.Opinion.Opinion;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = JournalController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class JournalControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        private AuthenticationService authService;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private JournalService journalService;

        private User user;

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

                journal = Journal.builder()
                                .publisher(user)
                                .content("HELLo")
                                .visibility(Visibility.PRIVATE)
                                .updateDateTime(LocalDateTime.now())
                                .build();

                anotherJournal = Journal.builder()
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

                anotherOpinion = Opinion.builder()
                                .opinionOwner(user)
                                .journal(journal)
                                .content("IDON")
                                .createDateTime(LocalDateTime.now())
                                .build();

                interaction = Interaction.builder()
                                .journal(journal)
                                .type(InteractionType.INTRESTED)
                                .createDateTime(LocalDateTime.now())
                                .interactorUser(user)
                                .opinion(opinion)
                                .build();

                anotherInteraction = Interaction.builder()
                                .journal(journal)
                                .type(InteractionType.LIKE)
                                .createDateTime(LocalDateTime.now())
                                .interactorUser(user)
                                .opinion(opinion)
                                .build();

        }

        @Test
        public void JournalController_GET_AllJournals_ReturnAllJournals() throws Exception {

                when(journalService.findAllJournals()).thenReturn(
                                CollectionModel.of(List.of(EntityModel.of(journal), EntityModel.of(anotherJournal))));

                ResultActions response = mockMvc.perform(get("/journals")
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void JournalController_GET_OneJournal_ReturnOneJournal() throws Exception {

                when(journalService.findJournalById(1L)).thenReturn(EntityModel.of(journal));

                ResultActions response = mockMvc.perform(get("/journals/{journalId}", 1)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void JournalController_GET_GetJournalInteraction_ReturnOneJournal() throws Exception {

                when(journalService.getJournalInteractions(1L)).thenReturn(CollectionModel
                                .of(List.of(EntityModel.of(interaction), EntityModel.of(anotherInteraction))));

                ResultActions response = mockMvc.perform(get("/journals/{journalId}/interactions", 1)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void JournalController_GET_GetJournalOpinions_ReturnJournalOpinions() throws Exception {

                when(journalService.getJournalOpinions(1L)).thenReturn(
                                CollectionModel.of(List.of(EntityModel.of(opinion), EntityModel.of(anotherOpinion))));

                ResultActions response = mockMvc.perform(get("/journals/{journalId}/opinions", 1)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void JournalController_Post_AttachMediaToJournal_ReturnCreatedJournal() throws Exception {

                Long[] mediaIds = { 1L, 2L, 3L };
                MediaIdDTO mediaIdDTO = new MediaIdDTO();
                mediaIdDTO.setMediaIds(mediaIds);

                EntityModel<Journal> entityModel = EntityModel.of(new Journal());
                given(journalService.attachMedia(
                                1L, mediaIdDTO))
                                .willReturn(entityModel);

                // When & Then
                mockMvc.perform(post("/journals/{journalId}/media", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(mediaIdDTO))
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andExpect(status().isOk());

        }

        // // //This is Ideal Template for Patch Testing
        @Test
        public void JournalController_Patch_AddContributorToJournal_ReturnJournal() throws Exception {

                when(journalService.addContributor(1L, 1L))
                                .thenReturn(EntityModel.of(journal));

                ResultActions response = mockMvc
                                .perform(patch("/journals/{journalId}/contributors/{contributorId}", 1L, 1L)
                                                .accept("application/hal+json")
                                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void JournalController_DELETE_RemoveContributorFromJournal_ReturnNoContent() throws Exception {

                doNothing().when(journalService).removeContributor(1L, 1L);

                ResultActions response = mockMvc
                                .perform(delete("/journals/{journalId}/contributors/{contributorId}", 1L, 1L)
                                                .accept("application/json"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void JournalController_DELETE_DettachMedia_ReturnNoContnent() throws Exception {

                Long[] mediaIds = { 1L, 2L, 3L };
                MediaIdDTO mediaIdDTO = new MediaIdDTO();
                mediaIdDTO.setMediaIds(mediaIds);

                EntityModel<Journal> entityModel = EntityModel.of(new Journal());
                given(journalService.attachMedia(
                                1L, mediaIdDTO))
                                .willReturn(entityModel);

                // When & Then
                mockMvc.perform(delete("/journals/{journalId}/media", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(mediaIdDTO))
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andExpect(status().isOk());

        }
}
