package edu.bethlehem.scinexus.SecurityConfig;

import edu.bethlehem.scinexus.JPARepository.UserRepository;
import edu.bethlehem.scinexus.User.UserService;

import edu.bethlehem.scinexus.User.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserService userService;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(OAuth2LoginSuccessHandler.class);

    public OAuth2LoginSuccessHandler(JwtService jwtService, UserService userService, UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        // Extracting The Authenticaiton Object
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        logger.info("OAuth2 Authentication successful: " + token);

        // Extracnting the Email
        String email = token.getPrincipal().getAttribute("email");
        //Checking If User is Already Registered
        Boolean isRegistered = userRepository.findByEmail(email).isPresent();

        if (isRegistered) {
            logger.info("User is already registered");
            String registeredRedirectUrl = "http://localhost:5173/login";

            UserDetailsImpl userDetails = (UserDetailsImpl) userService.loadUserByUsername(email);
            String jwtToken = jwtService.generateToken(userDetails);
            Cookie cookie = new Cookie("JWT_TOKEN", jwtToken);

            cookie.setPath("/"); // set the cookie path
            cookie.setHttpOnly(false); // make the cookie accessible only via HTTP (not accessible via JavaScript)
            // Add other cookie properties as needed, such as domain and max-age

            response.addCookie(cookie);


            String loginRedirectUrl = "http://localhost:5173/";
            logger.info("Redirect URL after successful authentication: " + loginRedirectUrl + " with JWT token: " + jwtToken);
            response.sendRedirect(loginRedirectUrl);
        } else {
            logger.info("User is not registered");

            // Differentiating between Google and GitHub
            String registrationRedirectUrl;
            String clientRegistrationId = token.getAuthorizedClientRegistrationId();
            String provider = clientRegistrationId; // use the client registration ID as the provider

            if ("google".equals(clientRegistrationId)) {
                // Extracting first name and last name for Google
                String firstName = token.getPrincipal().getAttribute("given_name");
                String lastName = token.getPrincipal().getAttribute("family_name");

                // Redirect to registration page with Google info as query parameters
                registrationRedirectUrl = String.format(
                        "http://localhost:5173/register?email=%s&name=%s&provider=%s",
                        URLEncoder.encode(email, "UTF-8"),
                        URLEncoder.encode(firstName+" "+lastName, "UTF-8"),
                        URLEncoder.encode(provider, "UTF-8")
                );
            } else if ("github".equals(clientRegistrationId)) {
                // Extracting name for GitHub
                String firstName = token.getPrincipal().getAttribute("name");

                // Redirect to registration page with GitHub info as query parameters
                registrationRedirectUrl = String.format(
                        "http://localhost:5173/register?email=%s&name=%s&provider=%s",
                        URLEncoder.encode(email, "UTF-8"),
                        URLEncoder.encode(firstName, "UTF-8"),
                        URLEncoder.encode(provider, "UTF-8")
                );
            } else {
                // Handle other providers if needed
                registrationRedirectUrl = String.format(
                        "http://localhost:5173/register?provider=%s",
                        URLEncoder.encode(provider, "UTF-8")
                );
            }
            logger.info("this is redirect url "+registrationRedirectUrl);

            response.sendRedirect(registrationRedirectUrl);
            //Saving the User to The Database
//            User user = userService.registerOrUpdateOAuth2User(token.getPrincipal());
//
//            //Reteriving the Registerd Id for the Authenticated User
//            String userID = String.valueOf(userRepository.findByEmail(email).get().getId());
//
//            // Modifind the Authentication ojbect to Add the ID to it OAuth2DefualtUser
//            Map<String, Object> attributes = new HashMap<>(token.getPrincipal().getAttributes());
//            attributes.put("id", userID);
//            OAuth2User oAuth2User = new DefaultOAuth2User(
//                    token.getPrincipal().getAuthorities(),
//                    attributes,
//                    "name");
//
//            //Casting the OAuth2Defualt User to Authentication Object after Modificaiton
//            OAuth2AuthenticationToken oAuth = new OAuth2AuthenticationToken(oAuth2User, token.getAuthorities(), token.getAuthorizedClientRegistrationId());
//
//            // Casting the User to userDetails
//            UserDetailsImpl userDetails = (UserDetailsImpl) user;
//
//            //Genertaing the Jwt Token
//            String jwtToken = jwtService.generateToken(userDetails);
//            logger.info("Here is the jwt Token : " + jwtToken);
//
//            //Extracting the ID from the Authentication Object
//            logger.info("Here is the user : " + jwtService.extractId(oAuth));
//
//            // Send JWT token in response
//            response.setContentType("application/json");
//            response.getWriter().write("{\"token\":\"" + jwtToken + "\"}");
//            response.setStatus(HttpServletResponse.SC_OK);


        }
        clearAuthenticationAttributes(request);
    }
}
