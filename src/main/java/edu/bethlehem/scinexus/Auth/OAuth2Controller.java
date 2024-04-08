package edu.bethlehem.scinexus.Auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OAuth2Controller {
    @GetMapping("/login/oauth2/code/github")
    public String githubCallback() {
        // Handle GitHub callback
        return "redirect:/"; // Redirect to home page after authentication
    }

    @GetMapping("/login/oauth2/code/google")
    public String googleCallback() {
        // Handle Google callback
        return "redirect:/"; // Redirect to home page after authentication
    }
}
