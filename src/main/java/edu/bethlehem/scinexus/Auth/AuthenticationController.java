package edu.bethlehem.scinexus.Auth;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(

           @Valid @RequestBody RegisterRequest request
    ){
        AuthenticationResponse response = null;
        try {
         response= service.register(request);
        }  catch (DataIntegrityViolationException exception){
            throw  new RegisterRequestException(exception.getLocalizedMessage(), HttpStatus.CONFLICT);

        }
        return ResponseEntity.ok(response);


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
        System.out.println("IS NOW IS AUTHNTICCATED");

        // Validate request object (optional)


        return ResponseEntity.ok(service.authenticate(request));
    }

}
