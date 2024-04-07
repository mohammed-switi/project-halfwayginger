package edu.bethlehem.scinexus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.ResearchPaper.ResearchLanguage;
import edu.bethlehem.scinexus.ResearchPaper.ResearchPaper;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.User.*;
import edu.bethlehem.scinexus.UserLinks.UserLinks;
import edu.bethlehem.scinexus.UserLinks.UserLinksService;
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

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

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

        @MockBean
        private UserLinksService userLinksService;
        private User user;
        private User anotherUser;
        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private UserRepository repository;

        private UserLinks userLinks;

        private Article article;

        private ResearchPaper researchPaper;
        private UserRequestPatchDTO  userRequestPatchDTO;

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
                                // .links(Arrays.asList(anotherUser))
                                .build();

                userLinks=UserLinks.builder()
                        .id(1L)
                        .linksTo(user)
                        .linksFrom(anotherUser)
                        .accepted(true)
                        .build();
                article=Article.builder()
                        .title("I Don't BELONG")
                        .subject("SOMETHIGN I DONT WANNA TALK ABOUT")
                        .content("NO CONTENT")
                        .build();

                researchPaper=ResearchPaper.builder()
                        .publisher(user)
                        .title("NOT INTERESTED")
                        .content("NOT INCLUDED")
                        .description("DOESNT MATTER")
                        .language(ResearchLanguage.CHINESE)
                        .noOfPages(200).build();

                userRequestPatchDTO= UserRequestPatchDTO.builder()
                        .firstName("SADDAM")
                        .lastName("HESSIEN")
                        .bio("LEADER")
                        .email("SADDAMLOL@Iraq.gov")
                        .username("SESO")
                        .password("Seso!2@Usababe")
                        .phoneNumber("0503290022")
                        .fieldOfWork("Biscuits")
                        .build();
        }

        @Test
        public void UserController_GET_AllUsers_ReturnAllUsers() throws Exception {

                when(userService.all()).
                        thenReturn(CollectionModel.of(Arrays.asList(EntityModel.of(user),EntityModel.of(anotherUser))));

                ResultActions response = mockMvc.perform(get("/users")
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void UserController_GET_OneUsers_ReturnOneUsers() throws Exception {

                when(userService.one(1L)).thenReturn(EntityModel.of(user));

                ResultActions response = mockMvc.perform(get("/users/{id}", 1)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void UserController_GET_UserLinks_returnUsers() throws Exception {

                when(userLinksService.getUserLinks(SecurityContextHolder.getContext().getAuthentication()))
                        .thenReturn(CollectionModel.of(Arrays.asList(EntityModel.of(userLinks))));

                ResultActions response= mockMvc.perform(get("/users/links")
                        .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                        .andDo(print());


                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());


        }

        @Test
        public void UserController_GET_UserArticles_returnArticlesList() throws Exception {


                when(userService.getUserArticles(SecurityContextHolder.getContext().getAuthentication()))
                        .thenReturn(CollectionModel.of(List.of(EntityModel.of(article))));

                ResultActions response= mockMvc.perform(get("/users/articles")
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                        .andDo(print());


                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());


        }

        @Test
        public void UserController_GET_UserArticle_ReturnOneArticle() throws Exception {
                when(userService.getUserArticle(1L,SecurityContextHolder.getContext().getAuthentication()))
                        .thenReturn(EntityModel.of(article));

                ResultActions response= mockMvc.perform(get("/users/articles/{articleId}",1L)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                        .andDo(print());


                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());

        }




        @Test
        public void UserController_GET_UserResearchPapers_returnResearchPapersList() throws Exception {


                when(userService.getUserResearchPapers(SecurityContextHolder.getContext().getAuthentication()))
                        .thenReturn(CollectionModel.of(List.of(EntityModel.of(researchPaper))));

                ResultActions response= mockMvc.perform(get("/users/researchpapers")
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                        .andDo(print());


                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());


        }







        @Test
        public void UserController_GET_UserResearchPaper_ReturnOneResearchPaper() throws Exception {
                when(userService.getUserResearchPaper(1L,SecurityContextHolder.getContext().getAuthentication()))
                        .thenReturn(EntityModel.of(researchPaper));

                ResultActions response= mockMvc.perform(get("/users/researchpapers/{researchPaperId}",1L)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                        .andDo(print());


                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());

        }


        @Test
        public void UserController_PUT_RespondToLinkage_returnUserLink() throws Exception {
                Boolean answer= true;

                when(userLinksService.acceptLink(SecurityContextHolder.getContext().getAuthentication(),1L,Boolean.TRUE))
                        .thenReturn(EntityModel.of(userLinks));

                ResultActions response= mockMvc.perform(put("/users/links/{userLinkTo}/response",1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(answer))
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                        .andDo(print());


                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());


        }
//TODO Obada
//        @Test
//        public void UserController_PATCH_updateUserPartially_ReturnUpdatedUser(){
//
//                when(userService.updateUserPartially(userRequestPatchDTO,1L))
//                        .thenReturn(EntityModel.of(user));
//
//                ResultActions response= mockMvc.perform(put("/users/links/{userLinkTo}/response",1L)
//
//                                .accept("application/hal+json")
//                                .characterEncoding("utf-8"))
//                        .andDo(print());
//
//
//                response.andExpect(MockMvcResultMatchers.status().isOk())
//                        .andDo(MockMvcResultHandlers.print());
//
//
//        }
        @Test
        public void UserController_PUT_LinkUser_ReturnUserLLinked() throws Exception {


                when(userLinksService.linkUser(SecurityContextHolder.getContext().getAuthentication(),1L))
                        .thenReturn(EntityModel.of(userLinks));

                ResultActions response= mockMvc.perform(put("/users/links/{userLinkTo}",1L)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                        .andDo(print());


                response.andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());


        }




        @Test
        public void UserController_Delete_UserLink_NOContent() throws Exception {

                doNothing().when(userLinksService).unLink(SecurityContextHolder.getContext().getAuthentication(),1L);

                ResultActions response = mockMvc.perform(delete("/users/links/{userLinkTO}", 1)
                                .accept("application/json"))
                        .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isNoContent())
                        .andDo(MockMvcResultHandlers.print());
        }
        @Test
        public void UserController_Delete_User_NOContent() throws Exception {

                doNothing().when(userService).deleteUser(1L);

                ResultActions response = mockMvc.perform(delete("/users/{id}", 1)
                                .accept("application/json"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andDo(MockMvcResultHandlers.print());

        }

}
