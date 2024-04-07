package edu.bethlehem.scinexus.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.Article.Article;
import edu.bethlehem.scinexus.Auth.AuthenticationService;
import edu.bethlehem.scinexus.Journal.Visibility;
import edu.bethlehem.scinexus.Notification.Notification;
import edu.bethlehem.scinexus.Notification.NotificationController;
import edu.bethlehem.scinexus.Notification.NotificationService;
import edu.bethlehem.scinexus.Notification.Status;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = NotificationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)

public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private AuthenticationService authService;



    @MockBean
    private NotificationService notificationService;


    private User user;

    @Autowired
    private ObjectMapper objectMapper;

    private Article article;
    private Article anotherArticle;

    private Notification notification;


    private Notification anotherNotification;





    @BeforeEach
    public void init() {

        article=Article.builder()
                .title("Doesn't matter")
                .subject("SCIENCE")
                .content("HELLO")
                .createDateTime(LocalDateTime.now())
                .publisher(user)
                .visibility(Visibility.PUBLIC)
                .createDateTime(LocalDateTime.now())
                .build();

        anotherArticle=Article.builder()
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

        notification=Notification.builder()
                .user(user)
                .status(Status.SEEN)
                .createDateTime(LocalDateTime.now())
                .content("YOU HAVE ZERO MESSAGES")
                .build();

        anotherNotification=Notification.builder()
                .user(user)
                .status(Status.RECEIVED)
                .createDateTime(LocalDateTime.now())
                .content("YOU HAVE ZERO MESSAGES")
                .build();


    }



    @Test
    public void NotificationController_GET_AllNotification_ReturnAllNotification() throws Exception {

        when(notificationService.findAllNotifications()).
                thenReturn(CollectionModel.of(List.of(EntityModel.of(notification),EntityModel.of(anotherNotification))));

        ResultActions response = mockMvc.perform(get("/notifications")
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    public void NotificationController_GET_GetOneNotification_returnNotification() throws Exception {

        when(notificationService.findNotificationById(1L)).thenReturn(EntityModel.of(notification));

        ResultActions response = mockMvc.perform(get("/notifications/{notificationId}", 1)
                        .accept("application/hal+json")
                        .characterEncoding("utf-8"))
                .andDo(print());

        response.andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }


}
