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
import lombok.RequiredArgsConstructor;
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
            User user = null;

        System.out.println(request.getRole());
//
//            userRepository.save(user);
//            userRepository.save(user);
            if(request.getRole() == Role.ACADEMIC){
               user = new Academic(request.getName(), request.getUsername(),passwordEncoder.encode(request.getPassword()), request.getEmail());
                user.setRole(Role.ACADEMIC);
                academicRepository.save((Academic) user);
            }
            else {
            user = new Organization(request.getName(), request.getUsername(),passwordEncoder.encode(request.getPassword()), request.getEmail());
            user.setRole(Role.ORGANIZATION);
            organizationRepository.save((Organization) user);
             }

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
            .orElseThrow(() -> new UserNotFoundException("User Not Found Exception"));
        var jwtToken = service.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
