package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.UserRepository;

import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.Role;
import edu.bethlehem.scinexus.User.User;
import edu.bethlehem.scinexus.User.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;

        private final PasswordEncoder passwordEncoder;
        private final JwtService service;

        private final AuthenticationManager authenticationManager;

        public AuthenticationResponse register(RegisterRequest request) {
                User user;

                if (request.getRole() == Role.ACADEMIC) {
                        user = User.builder()
                                        .firstName(request.getFirstName())
                                        .lastName(request.getLastName())
                                        .username(request.getUsername())
                                        .email(request.getEmail())
                                        .phoneNumber(request.getPhoneNumber())
                                        .password(request.getPassword())
                                        .role(request.getRole())
                                        .bio(request.getBio())
                                        .fieldOfWork(request.getFieldOfWork())
                                        .education(request.getEducation())
                                        .badge(request.getBadge())
                                        .position(request.getPosition())
                                        .build();

                } else {
                        user = User.builder()
                                        .firstName(request.getFirstName())
                                        .lastName(request.getLastName())
                                        .username(request.getUsername())
                                        .email(request.getEmail())
                                        .phoneNumber(request.getPhoneNumber())
                                        .password(passwordEncoder.encode(request.getPassword()))
                                        .role(request.getRole())
                                        .bio(request.getBio())
                                        .fieldOfWork(request.getFieldOfWork())
                                         .type(request.getType())
                                        .build();
                }

                userRepository.save(user);
                // Remember to Delete the following Lines, because the JWT Token Must Be Returned When Authenticating
                var jwtToken = service.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();

        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {

                authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new UserNotFoundException("User Not Found Exception", HttpStatus.NOT_FOUND));
                var jwtToken = service.generateToken(user);
                return AuthenticationResponse.builder()
                                .token(jwtToken)
                                .build();
        }
}
