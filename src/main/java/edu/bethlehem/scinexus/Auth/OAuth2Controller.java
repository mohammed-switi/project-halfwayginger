package edu.bethlehem.scinexus.Auth;

import edu.bethlehem.scinexus.SecurityConfig.OAuth2LoginSuccessHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

//@Controller
//public class OAuth2Controller {
//    @GetMapping("/login/oauth2/code/github")
//    public String githubCallback() {
//        // Handle GitHub callback
//        return "redirect:/"; // Redirect to home page after authentication
//    }
//
//    @GetMapping("/login/oauth2/code/google")
//    public String googleCallback() {
//        // Handle Google callback
//        return "redirect:http://localhost:5173/login"; // Redirect to the specified URL after authentication
//    }
//}
@RestController
public class OAuth2Controller {
    private final OAuth2AuthorizedClientService clientService;

    private final Logger logger = LoggerFactory.getLogger(OAuth2Controller.class);

    @Autowired
    public OAuth2Controller(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/login/oauth2/code/{provider}")
    public RedirectView loginSuccess(@PathVariable String provider, OAuth2AuthenticationToken authenticationToken) {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                authenticationToken.getAuthorizedClientRegistrationId(),
                authenticationToken.getName()
        );

        logger.info("I reached the login Success controller");
        // Handle storing the user information and token, then redirect to a successful login page
        // For example, store user details in your database and generate a JWT token for further authentication.

        return new RedirectView("/login-success");
    }
}