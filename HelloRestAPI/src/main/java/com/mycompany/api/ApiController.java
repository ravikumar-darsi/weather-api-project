package com.mycompany.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@GetMapping("/api/hello")
	public Response hello() {
		return new Response("Hello world REST API");
	}
}
