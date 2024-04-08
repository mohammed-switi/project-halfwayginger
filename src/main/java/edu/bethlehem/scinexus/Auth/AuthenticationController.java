package edu.bethlehem.scinexus.Auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.SecurityConfig.UserDetailsImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;
    private final JwtService jwtService;
    @Autowired
    private ObjectMapper objectMapper;
    Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(

            @Valid @RequestBody RegisterRequest request) {
        try {
            return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
        } catch (DataIntegrityViolationException exception) {
            throw new RegisterRequestException(exception.getLocalizedMessage(), HttpStatus.CONFLICT);

        }

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request) {

        return ResponseEntity.ok(service.authenticate(request));

    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token) {
        return service.confirmToken(token);
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(Model model) {
        ModelAndView modelAndView = new ModelAndView("login");

        model.addAttribute("loginForm", new AuthenticationRequest());

        return modelAndView;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> processLogin(
            @ModelAttribute("loginForm") AuthenticationRequest request) {

        // Validate request object (optional)
        logger.trace("Now I am in the login end point "+ request.toString());

        return ResponseEntity.ok(service.authenticate(request));
    }
    @PostMapping("/loginnow")
    public ResponseEntity<AuthenticationResponse> processLoginnow(@ModelAttribute("loginForm")  AuthenticationRequest request) {

        // Validate request object (optional)

        logger.trace("Now I am in the loginnow end point "+ request.toString());

        return ResponseEntity.ok(service.authenticate(request));
    }



    @GetMapping("/info")
    @ResponseBody
    public Map<String, Object> user(HttpServletRequest request) {
        logger.trace("this is reqeust "+ request.toString());
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Access authentication information


        return Collections.singletonMap("name", authentication.getName());
    }


}
