package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;

@SuppressWarnings({ "removal", "unused" })
@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTests {

	private static final String END_POINT_PATH = "/v1/locations";

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	LocationService service;

	@Test
	public void testAddShouldReturn400BadRequest() throws Exception {
		LocationDTO location = new LocationDTO();

		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testAddShouldReturn201Created() throws Exception {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());

		Mockito.when(service.add(location)).thenReturn(location);

		String bodyContent = mapper.writeValueAsString(dto);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isCreated()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.code", is("NYC_USA"))).andExpect(jsonPath("$.city_name", is("New York City")))
				.andExpect(header().string("Location", "/v1/locations/NYC_USA")).andDo(print());
	}

	@Test
	public void testValidateRequestBodyLocationCodeNotNull() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.errors[0]", is("Location code cannot be null"))).andDo(print());
	}

	@Test
	public void testValidateRequestBodyLocationCodeLength() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setCode("");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.errors[0]", is("Location code must have 3-12 characters"))).andDo(print());
	}

	@Test
	public void testValidateRequestBodyAllFieldsInvalid() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setRegionName("");

		String bodyContent = mapper.writeValueAsString(location);

		MvcResult mvcResult = mockMvc.perform(post(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andExpect(content().contentType("application/json")).andDo(print())
				.andReturn();

		String responseBody = mvcResult.getResponse().getContentAsString();

		assertThat(responseBody).contains("Location code cannot be null");
		assertThat(responseBody).contains("City name cannot be null");
		assertThat(responseBody).contains("Region name must have 3-128 characters");
		assertThat(responseBody).contains("Country name cannot be null");
		assertThat(responseBody).contains("Country code cannot be null");
	}

	@SuppressWarnings("deprecation")
	@Test
	@Disabled
	public void testListShouldReturn204NoContent() throws Exception {

		Mockito.when(service.list()).thenReturn(Collections.emptyList());

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNoContent()).andDo(print());
	}

	@SuppressWarnings("deprecation")
	@Test
	@Disabled
	public void testListShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("NYC_USA");
		location1.setCityName("New York City");
		location1.setRegionName("New York");
		location1.setCountryCode("US");
		location1.setCountryName("United States of America");
		location1.setEnabled(true);

		Location location2 = new Location();
		location2.setCode("LACA_USA");
		location2.setCityName("Los Angeles");
		location2.setRegionName("California");
		location2.setCountryCode("US");
		location2.setCountryName("United States of America");
		location2.setEnabled(true);

		Mockito.when(service.list()).thenReturn(List.of(location1, location2));

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isOk())
				.andExpect(content().contentType("application/json")).andExpect(jsonPath("$[0].code", is("NYC_USA")))
				.andExpect(jsonPath("$[0].city_name", is("New York City")))
				.andExpect(jsonPath("$[1].code", is("LACA_USA")))
				.andExpect(jsonPath("$[1].city_name", is("Los Angeles"))).andDo(print());
	}

	@Test
	public void testListByPageShouldReturn204NoContent() throws Exception {
		
		Mockito.when(service.listByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
						.thenReturn(Page.empty());
		
		mockMvc.perform(get(END_POINT_PATH))
				.andExpect(status().isNoContent())
				.andDo(print());
	}
	
	@Test
	public void testListByPageShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("NYC_USA");
		location1.setCityName("New York City");
		location1.setRegionName("New York");
		location1.setCountryCode("US");
		location1.setCountryName("United States of America");
		location1.setEnabled(true);		
		
		Location location2 = new Location();
		location2.setCode("LACA_USA");
		location2.setCityName("Los Angeles");
		location2.setRegionName("California");
		location2.setCountryCode("US");
		location2.setCountryName("United States of America");
		location2.setEnabled(true);	
		
		Page<Location> page = new PageImpl<>(List.of(location1, location2));
		
		Mockito.when(service.listByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
					.thenReturn(page);
		
		mockMvc.perform(get(END_POINT_PATH))
			.andExpect(status().isOk())
			.andExpect(content().contentType("application/json"))
			.andExpect(jsonPath("$[0].code", is("NYC_USA")))
			.andExpect(jsonPath("$[0].city_name", is("New York City")))
			.andExpect(jsonPath("$[1].code", is("LACA_USA")))
			.andExpect(jsonPath("$[1].city_name", is("Los Angeles")))			
			.andDo(print());			
	}
	
	@Test
	public void testListByPageShouldReturn400BadRequest() throws Exception {
		int pageNum = 0;
		int pageSize = 5;
		String sortField = "code";
		
		Mockito.when(service.listByPage(pageNum, pageSize, sortField))
						.thenReturn(Page.empty());
		
		String requestURI= END_POINT_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;
		
		mockMvc.perform(get(requestURI))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("must be greater than or equal to 1")))
				.andDo(print());
		
	}
	
	@Test
	public void testListByPageShouldReturn400BadRequestInvalidPageSize() throws Exception {
		int pageNum = 1;
		int pageSize = 3;
		String sortField = "code";
		
		Mockito.when(service.listByPage(pageNum, pageSize, sortField))
						.thenReturn(Page.empty());
		
		String requestURI= END_POINT_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;
		
		mockMvc.perform(get(requestURI))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("must be greater than or equal to 5")))
				.andDo(print());
		
	}
	
	@Test
	public void testListByPageShouldReturn400BadRequestInvalidSortField() throws Exception {
		int pageNum = 1;
		int pageSize = 5;
		String sortField = "code_abc";
		
		Mockito.when(service.listByPage(pageNum, pageSize, sortField))
						.thenReturn(Page.empty());
		
		String requestURI= END_POINT_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;
		
		mockMvc.perform(get(requestURI))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("invalid sort field")))
				.andDo(print());
		
	}
	
	
	@Test
	public void testGetShouldReturn405MethodNotAllowed() throws Exception {
		String requestURI = END_POINT_PATH + "/ABCDEF";

		mockMvc.perform(post(requestURI)).andExpect(status().isMethodNotAllowed()).andDo(print());
	}

//	@Test
//	public void testGetShouldReturn404NotFound() throws Exception {
//		String requestURI = END_POINT_PATH + "/ABCDEF";
//
//		// Mock the service to throw LocationNotFoundException
//		when(service.get("ABCDEF")).thenThrow(new LocationNotFoundException("Location with code ABCDEF not found"));
//
//		mockMvc.perform(get(requestURI)).andExpect(status().isNotFound()).andDo(print());
//	}
	@Test
	public void testGetShouldReturn404NotFound() throws Exception {

		String locationCode = "ABCDEF";

		String requestURI = END_POINT_PATH + "/" + locationCode;

		Mockito.when(service.get(locationCode)).thenThrow(LocationNotFoundException.class);
		mockMvc.perform(get(requestURI)).andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testGetShouldReturn200OK() throws Exception {
		String code = "LACA_USA";
		String requestURI = END_POINT_PATH + "/" + code;

		Location location = new Location();
		location.setCode("LACA_USA");
		location.setCityName("Los Angeles");
		location.setRegionName("California");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		Mockito.when(service.get(code)).thenReturn(location);

		mockMvc.perform(get(requestURI)).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.code", is(code))).andExpect(jsonPath("$.city_name", is("Los Angeles")))
				.andDo(print());
	}

	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setCode("ABCDEF");
		location.setCityName("Los Angeles");
		location.setRegionName("California");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		LocationNotFoundException ex = new LocationNotFoundException(location.getCityName());
		
		Mockito.when(service.update(Mockito.any())).thenThrow(ex);

		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());
	}

	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setCityName("Los Angeles");
		location.setRegionName("California");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		String bodyContent = mapper.writeValueAsString(location);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		location.setEnabled(true);

		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());

		Mockito.when(service.update(location)).thenReturn(location);

		String bodyContent = mapper.writeValueAsString(dto);

		mockMvc.perform(put(END_POINT_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.code", is("NYC_USA"))).andExpect(jsonPath("$.city_name", is("New York City")))
				.andDo(print());
	}

	@Test
	public void testDeleteShouldReturn404NotFound() throws Exception {
		String code = "LACA_USA";
		String requestURI = END_POINT_PATH + "/" + code;

		LocationNotFoundException ex = new LocationNotFoundException(code);

		Mockito.doThrow(ex).when(service).delete(code);

		mockMvc.perform(delete(requestURI))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());
	}

	@Test
	public void testDeleteShouldReturn204NoContent() throws Exception {
		String code = "LACA_USA";
		String requestURI = END_POINT_PATH + "/" + code;

		Mockito.doNothing().when(service).delete(code);

		mockMvc.perform(delete(requestURI)).andExpect(status().isNoContent()).andDo(print());
	}
}
