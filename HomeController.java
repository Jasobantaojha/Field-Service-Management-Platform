package com.KeyStone.FieldServiceProject;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	    @GetMapping("/")
	    public String home() {
	        return "Field Service Project is running successfully!";
	    }
	}

