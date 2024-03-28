package edu.bethlehem.scinexus.ThymeleafManagment;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ThymeleafController {


    @GetMapping("/access-denied")
    public String hello(Model model) {
        model.addAttribute("message", "Hello, Thymeleaf!");
        return "access-denied"; // Return the name of the Thymeleaf template (hello.html)
    }
}
