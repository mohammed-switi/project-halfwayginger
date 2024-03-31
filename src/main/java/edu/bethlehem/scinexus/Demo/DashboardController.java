package edu.bethlehem.scinexus.Demo;


import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class DashboardController {


    @PostMapping("/dashboard")
    public ModelAndView showDashboard(Model model) {

        // Add any necessary data to the model
        ModelAndView modelAndView=new ModelAndView("dashboard");

        return modelAndView; // Assuming "dashboard" is the name of your Thymeleaf template
    }


}
