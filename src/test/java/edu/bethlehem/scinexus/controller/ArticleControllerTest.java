package edu.bethlehem.scinexus.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
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
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Article.ArticleController;
import edu.bethlehem.scinexus.Article.ArticleRequestDTO;
import edu.bethlehem.scinexus.Article.ArticleRequestPatchDTO;
import edu.bethlehem.scinexus.Article.ArticleService;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;

@WebMvcTest(controllers = ArticleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class ArticleControllerTest {

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

        private Article article;
        private Article anotherArticle;

        @BeforeEach
        public void init() {

                article = Article.builder()
                                .title("Doesn't matter")
                                .subject("SCIENCE")
                                .content("HELLO")
                                .createDateTime(LocalDateTime.now())
                                .publisher(user)
                                .visibility(Visibility.PUBLIC)
                                .createDateTime(LocalDateTime.now())
                                .build();

                anotherArticle = Article.builder()
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
        public void ArticleController_GET_AllArticles_ReturnAllArticles() throws Exception {

                when(articleService.findAllArticles()).thenReturn(
                                CollectionModel.of(List.of(EntityModel.of(article), EntityModel.of(anotherArticle))));

                ResultActions response = mockMvc.perform(get("/articles")
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void ArticleController_GET_OneArticle_ReturnOneArticle() throws Exception {

                when(articleService.findArticleById(1L)).thenReturn(EntityModel.of(article));

                ResultActions response = mockMvc.perform(get("/articles/{id}", 1)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void ArticleController_POST_PostArticle_ReturnArticle() throws Exception {

                ArticleRequestDTO newArticleRequestDTO = ArticleRequestDTO.builder()
                                .title("DoesntMatter")
                                .visibility(Visibility.PRIVATE)
                                .subject("NOT YOUR BUSINESS")
                                .content("Hello")
                                .visibility(Visibility.PRIVATE)
                                .build();

                EntityModel<Article> entityModel = EntityModel.of(new Article());
                given(articleService.createArticle(eq(newArticleRequestDTO), any(Authentication.class)))
                                .willReturn(entityModel);

                // When & Then
                mockMvc.perform(post("/articles")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newArticleRequestDTO))
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andExpect(status().isCreated());

        }

        // //This is Ideal Template for Patch Testing
        @Test
        public void ArticleController_Patch_UpdateArticlePartially_ReturnArticle() throws Exception {

                ArticleRequestPatchDTO articleRequestPatchDTO = ArticleRequestPatchDTO.builder()
                                .title("DoesntMatter")
                                .visibility(Visibility.PRIVATE)
                                .subject("NOT YOUR BUSINESS")
                                .content("Hello")
                                .visibility(Visibility.PRIVATE)
                                .build();

                when(articleService.updateArticlePartially(1L, articleRequestPatchDTO))
                                .thenReturn(EntityModel.of(article));

                ResultActions response = mockMvc.perform(patch("/articles/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept("application/hal+json")
                                .content(objectMapper.writeValueAsString(articleRequestPatchDTO))
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void ArticleController_DELETE_articlePost_ReturnNoContnent() throws Exception {

                doNothing().when(articleService).deleteArticle(1L);

                ResultActions response = mockMvc.perform(delete("/articles/{id}", 1)
                                .accept("application/json"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andDo(MockMvcResultHandlers.print());

        }

}
