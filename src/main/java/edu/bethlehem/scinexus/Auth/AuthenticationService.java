package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.Academic.Academic;
import edu.bethlehem.scinexus.Academic.AcademicRepository;
import edu.bethlehem.scinexus.Config.JwtService;
import edu.bethlehem.scinexus.Organization.Organization;
import edu.bethlehem.scinexus.Organization.OrganizationRepository;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import edu.bethlehem.scinexus.User.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final AcademicRepository academicRepository;
    private final OrganizationRepository organizationRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService service;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
            User user;

            if(request.getRole() == Role.ACADEMIC){
               user = (Academic) Academic.builder()
                      .firstName(request.getFirstName())
                      .lastName(request.getLastName())
                      .email(request.getEmail())
                      .phoneNumber(passwordEncoder.encode(request.getPassword()))
                      .password(request.getPassword())
                      .role(request.getRole())
                      .bio(request.getBio())
                      .fieldOfWork(request.getFieldOfWork())
                      .build();

            }
            else {
                 user = Organization.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                         .username(request.getUsername())
                        .email(request.getEmail())
                        .phoneNumber(request.getPhoneNumber())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .bio(request.getBio())
                        .fieldOfWork(request.getFieldOfWork())
                        .build();
             }

            userRepository.save(user);

        var jwtToken = service.generateToken(user);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();

    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
            )
    );
    var user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UserNotFoundException("User Not Found Exception", HttpStatus.NOT_FOUND));
        var jwtToken = service.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
