package edu.bethlehem.scinexus.Auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @PostMapping("/oauth2/register")
    public ResponseEntity<OAuth2RegisterResponse> registerOAuth2(
            @Valid @RequestBody OAuthRegisterRequest request) {

        // Handle OAuth2 registration request
        try {
            // Your registration logic for OAuth2 requests
            return new ResponseEntity<>(service.registerOAuth(request), HttpStatus.CREATED);
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
        logger.trace("Now I am in the login end point " + request.toString());

        return ResponseEntity.ok(service.authenticate(request));
    }

    @GetMapping("/verify-token")
    public ResponseEntity verifiyToken(
            Authentication authentication
    ){
        HashMap<String,Boolean> response = new HashMap<>();
        response.put("isVerified",service.verifyToken(authentication));
        return ResponseEntity.ok(response);
    }

//    @PostMapping("/loginnow")
//    public ResponseEntity<AuthenticationResponse> processLoginnow(
//            @ModelAttribute("loginForm") AuthenticationRequest request) {
//
//        // Validate request object (optional)
//
//        logger.trace("Now I am in the loginnow end point " + request.toString());
//
//        return ResponseEntity.ok(service.authenticate(request));
//    }

//    @GetMapping("/info")
//    @ResponseBody
//    public Map<String, Object> user(HttpServletRequest request) {
//        logger.trace("this is reqeust " + request.toString());
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        // Access authentication information
//
//        return Collections.singletonMap("name", authentication.getName());
//    }

}
