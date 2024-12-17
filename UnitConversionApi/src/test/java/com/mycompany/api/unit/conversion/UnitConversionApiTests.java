package com.mycompany.api.unit.conversion;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.api.unit.conversion.repo.User;
import com.mycompany.api.unit.conversion.repo.UserRepository;
import com.mycompany.api.unit.conversion.security.SecurityConfig;

@WebMvcTest(UnitConversionApi.class)
@Import(SecurityConfig.class)
public class UnitConversionApiTests {

	private static final String REQUEST_URI = "/api/unit-conversion";

	@Autowired
	MockMvc mvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean UserRepository userRepo;

	@Test
	public void shouldReturn401BecauseNoCredentials() throws Exception {

		ConversionDetails details = new ConversionDetails();
		details.setFromUnit("km");
		details.setToUnit("mile");
		details.setFromValue(100);

		String bodyContent = mapper.writeValueAsString(details);

		mvc.perform(post(REQUEST_URI).contentType("application/json").content(bodyContent)).andDo(print())
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void shouldReturn200BecauseValidCredentials() throws Exception {
		
		String username= "admin";
		String rawPassword = "admin";
		String encodedPassword = "$2a$10$9U.Qw8EdjaZIlsDEgrZrWugXLyEysIYvCNVbxR.q8c.IvZvGvA/12";
		
		ConversionDetails details = new ConversionDetails();
		details.setFromUnit("km");
		details.setToUnit("mile");
		details.setFromValue(100);

		Mockito.when(userRepo.findByUsername(username)).thenReturn(new User(username, encodedPassword));
		
		String bodyContent = mapper.writeValueAsString(details);

		mvc.perform(post(REQUEST_URI).contentType("application/json")
				.content(bodyContent).with(httpBasic(username, rawPassword)))
				.andDo(print())
				.andExpect(status().isOk());
	}
	
	@Test
	public void shouldReturn400BecauseInvalidCredentials() throws Exception {
		
		String username= "admin";
		String rawPassword = "awsws";
		String encodedPassword = "$2a$10$9U.Qw8EdjaZIlsDEgrZrWugXLyEysIYvCNVbxR.q8c.IvZvGvA/12";
		
		ConversionDetails details = new ConversionDetails();
		details.setFromUnit("km");
		details.setToUnit("mile");
		details.setFromValue(100);

		Mockito.when(userRepo.findByUsername(username)).thenReturn(new User(username, encodedPassword));
		
		String bodyContent = mapper.writeValueAsString(details);

		mvc.perform(post(REQUEST_URI).contentType("application/json")
				.content(bodyContent).with(httpBasic(username, rawPassword)))
				.andDo(print())
				.andExpect(status().isUnauthorized());
	}

}
