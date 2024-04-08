package edu.bethlehem.scinexus.Auth;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

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

        return ResponseEntity.ok(service.authenticate(request));
    }

}
