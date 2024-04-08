package edu.bethlehem.scinexus.Demo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()

public class demoController {

    @GetMapping("/api/v1/demo-controller")
    public ResponseEntity<String> sayHello() {

        return ResponseEntity.ok("Hello from Sercured Endpoint");
    }

}
