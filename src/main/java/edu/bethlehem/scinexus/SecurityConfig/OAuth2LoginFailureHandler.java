package edu.bethlehem.scinexus.SecurityConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component

public class OAuth2LoginFailureHandler extends  SimpleUrlAuthenticationFailureHandler{

    Logger logger = LoggerFactory.getLogger(OAuth2LoginFailureHandler.class);

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, org.springframework.security.core.AuthenticationException exception) throws IOException, IOException {
        logger.error("Authentication failed: " + exception.getMessage());

        // Redirect to an error page or login page
        getRedirectStrategy().sendRedirect(request, response, "http://localhost:5173/login");
    }
}
