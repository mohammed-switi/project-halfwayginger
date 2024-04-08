package edu.bethlehem.scinexus.Auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class IndexController {

    @GetMapping("/index")
    public ModelAndView index(Model model) {
        ModelAndView modelAndView= new ModelAndView("index");

        return modelAndView; // Return the name of the HTML file without the extension
    }
}
