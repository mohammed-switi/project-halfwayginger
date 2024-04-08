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

import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Post.Post;
import edu.bethlehem.scinexus.Post.PostController;
import edu.bethlehem.scinexus.Post.PostRequestDTO;
import edu.bethlehem.scinexus.Post.PostRequestPatchDTO;
import edu.bethlehem.scinexus.Post.PostService;
import edu.bethlehem.scinexus.SecurityConfig.JwtAuthenticationFilter;
import edu.bethlehem.scinexus.User.Position;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;

@WebMvcTest(controllers = PostController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class PostControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        private AuthenticationService authService;

        @MockBean
        private PostService postService;

        private User user;

        @Autowired
        private ObjectMapper objectMapper;

        private Post post;
        private Post anotherPost;

        @BeforeEach
        public void init() {

                post = Post.builder()
                                .content("HELLO")
                                .publisher(user)
                                .visibility(Visibility.PUBLIC)
                                .createDateTime(LocalDateTime.now())
                                .build();

                anotherPost = Post.builder()
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
        public void PostController_GET_AllPosts_ReturnAllPosts() throws Exception {

                when(postService.findAllPosts()).thenReturn(
                                CollectionModel.of(List.of(EntityModel.of(post), EntityModel.of(anotherPost))));

                ResultActions response = mockMvc.perform(get("/posts")
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void PostController_GET_OnePost_ReturnOnePosts() throws Exception {

                when(postService.findPostById(1L)).thenReturn(EntityModel.of(post));

                ResultActions response = mockMvc.perform(get("/posts/{id}", 1)
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void PostController_POST_CreatePost_ReturnPost() throws Exception {

                PostRequestDTO newPostRequestDTO = PostRequestDTO.builder().content("Hello")
                                .visibility(Visibility.PRIVATE)
                                .build();
                EntityModel<Post> entityModel = EntityModel.of(new Post());
                given(postService.createPost(any(Authentication.class), eq(newPostRequestDTO)))
                                .willReturn(entityModel);

                // When & Then
                mockMvc.perform(post("/posts")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newPostRequestDTO))
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andExpect(status().isCreated());

        }

        @Test
        public void PostController_POST_ResharePost_ReturnPost() throws Exception {
                // Given
                PostRequestDTO newPostRequestDTO = PostRequestDTO.builder().content("Hello")
                                .visibility(Visibility.PRIVATE)
                                .build(); // populate with test data
                // populate with expected result
                EntityModel<Post> entityModel = EntityModel.of(new Post());
                given(postService.createResharePost(any(Authentication.class), eq(newPostRequestDTO), any()))
                                .willReturn(entityModel);

                // When & Then
                mockMvc.perform(post("/posts/{journalId}/reshare", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(newPostRequestDTO))
                                .accept("application/hal+json")
                                .characterEncoding("utf-8"))
                                .andExpect(status().isCreated());

        }

        // This is Ideal Template for Patch Testing
        @Test
        public void PostController_Patch_UpdatePostPartially_ReturnPost() throws Exception {

                PostRequestPatchDTO postRequestPatchDTO = PostRequestPatchDTO.builder()
                                .content("Helo")
                                .visibility(Visibility.PRIVATE)
                                .build();
                when(postService.updatePostPartially(1L, postRequestPatchDTO))
                                .thenReturn(EntityModel.of(post));

                ResultActions response = mockMvc.perform(patch("/posts/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept("application/hal+json")
                                .content(objectMapper.writeValueAsString(postRequestPatchDTO))
                                .characterEncoding("utf-8"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isCreated())
                                .andDo(MockMvcResultHandlers.print());

        }

        @Test
        public void PostController_DELETE_deletePost_ReturnNoContnent() throws Exception {

                doNothing().when(postService).deletePost(1L);

                ResultActions response = mockMvc.perform(delete("/posts/{id}", 1)
                                .accept("application/json"))
                                .andDo(print());

                response.andExpect(MockMvcResultMatchers.status().isNoContent())
                                .andDo(MockMvcResultHandlers.print());

        }

}
