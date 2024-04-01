package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.Auth.Email.EmailSender;
import edu.bethlehem.scinexus.Auth.EmailToken.ConfirmationToken;
import edu.bethlehem.scinexus.Auth.EmailToken.ConfirmationTokenService;
import edu.bethlehem.scinexus.Auth.EmailToken.EmailConfirmationException;
import edu.bethlehem.scinexus.SecurityConfig.JwtService;
import edu.bethlehem.scinexus.User.*;

import edu.bethlehem.scinexus.JPARepository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

        private final UserRepository userRepository;
        private final UserService userService;

        private final PasswordEncoder passwordEncoder;
        private final JwtService service;

        private final AuthenticationManager authenticationManager;

        private final ConfirmationTokenService confirmationTokenService;

        private final EmailSender emailSender;

        public AuthenticationResponse register(RegisterRequest request) {
                User user;

                if (request.getRole() == Role.ACADEMIC) {
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
                                        .education(request.getEducation())
                                        .badge(request.getBadge())
                                        .position(request.getPosition())
                                        .enabled(false)
                                        .locked(false)
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
                                        .enabled(false)
                                        .locked(false)
                                        .build();
                }

                userRepository.save(user);

//                String token = UUID.randomUUID().toString();
//
//                ConfirmationToken confirmationToken=ConfirmationToken.builder()
//                        .token(token)
//                        .createdAt(LocalDateTime.now())
//                        .expiresAt(LocalDateTime.now().plusMinutes(15))
//                        .user(user)
//                        .build();
//
//                confirmationTokenService.saveConfirmationToken(confirmationToken);
//                String link = "http://localhost:8080/api/v1/auth/confirm?token=" + token;
//
//                emailSender.send(request.getEmail(),
//                        buildEmail(request.getFirstName(),link));

                // Remember to Delete the following Lines, because the JWT Token Must Be Returned When Authenticating
                var jwtToken = service.generateToken(user);
                return AuthenticationResponse.builder()
                                .jwtToken(jwtToken)
                                .confirmationToken("token")
                                .build();

        }

        public AuthenticationResponse authenticate(AuthenticationRequest request) {

                Authentication authentication= authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                request.getEmail(),
                                                request.getPassword()));
                var user = userRepository.findByEmail(request.getEmail())
                                .orElseThrow(() -> new UserNotFoundException("User Not Found Exception", HttpStatus.NOT_FOUND));

                SecurityContextHolder.getContext().setAuthentication(authentication);
                var jwtToken = service.generateToken(user);
                return AuthenticationResponse.builder()
                                .jwtToken(jwtToken)
                                .build();
        }


        @Transactional
        public String confirmToken(String token) {
                ConfirmationToken confirmationToken = confirmationTokenService.
                        getToken(token).orElseThrow(() -> new IllegalStateException("Token Not Found"));

                if (confirmationToken.getConfirmedAt()!= null)
                        throw new EmailConfirmationException("Email already Confirmed");


                LocalDateTime expiredAt=confirmationToken.getExpiresAt();

                if (expiredAt.isBefore(LocalDateTime.now()))
                        throw new EmailConfirmationException("Token Expired");

                confirmationTokenService.setConfirmedAt(token);

                userService.enableUser(
                        confirmationToken.getUser().getEmail()
                );

            return "confirmed";
        }


        private String buildEmail(String name, String link) {
                return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                        "\n" +
                        "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                        "\n" +
                        "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                        "        \n" +
                        "        <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                        "          <tbody><tr>\n" +
                        "            <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                        "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td style=\"padding-left:10px\">\n" +
                        "                  \n" +
                        "                    </td>\n" +
                        "                    <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                        "                      <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                        "                    </td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "              </a>\n" +
                        "            </td>\n" +
                        "          </tr>\n" +
                        "        </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                        "      <td>\n" +
                        "        \n" +
                        "                <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                        "                  <tbody><tr>\n" +
                        "                    <td bgcolor=\"#1D70B8\" width=\"100%\" height=\"10\"></td>\n" +
                        "                  </tr>\n" +
                        "                </tbody></table>\n" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table>\n" +
                        "\n" +
                        "\n" +
                        "\n" +
                        "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                        "    <tbody><tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "      <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                        "        \n" +
                        "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> Thank you for registering. Please click on the below link to activate your account: </p><blockquote style=\"Margin:0 0 20px 0;border-left:10px solid #b1b4b6;padding:15px 0 0.1px 15px;font-size:19px;line-height:25px\"><p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\"> <a href=\"" + link + "\">Activate Now</a> </p></blockquote>\n Link will expire in 15 minutes. <p>See you soon</p>" +
                        "        \n" +
                        "      </td>\n" +
                        "      <td width=\"10\" valign=\"middle\"><br></td>\n" +
                        "    </tr>\n" +
                        "    <tr>\n" +
                        "      <td height=\"30\"><br></td>\n" +
                        "    </tr>\n" +
                        "  </tbody></table><div class=\"yj6qo\"></div><div class=\"adL\">\n" +
                        "\n" +
                        "</div></div>";
        }
}
