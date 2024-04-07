package edu.bethlehem.scinexus.Auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<RegisterRequest> register(

           @Valid @RequestBody RegisterRequest request
    ){
        try {
            return new ResponseEntity<>(service.register(request), HttpStatus.CREATED);
        }  catch (DataIntegrityViolationException exception){
            throw  new RegisterRequestException(exception.getLocalizedMessage(), HttpStatus.CONFLICT);

        }



    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody AuthenticationRequest request
    ){

       return ResponseEntity.ok(service.authenticate(request));

    }

    @GetMapping("/confirm")
    public String confirm(@RequestParam("token") String token){
        return service.confirmToken(token);
    }

    @GetMapping("/login")
    public ModelAndView showLoginForm(Model model){
        ModelAndView modelAndView=new ModelAndView("login");

        model.addAttribute("loginForm", new AuthenticationRequest());

        return modelAndView;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> processLogin(@ModelAttribute("loginForm")  AuthenticationRequest request) {

        // Validate request object (optional)

        return ResponseEntity.ok(service.authenticate(request));
    }

}
