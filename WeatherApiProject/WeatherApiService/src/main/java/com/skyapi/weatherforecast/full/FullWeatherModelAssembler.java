package com.skyapi.weatherforecast.full;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.skyapi.weatherforecast.GeoLocationException;

@Component
public class FullWeatherModelAssembler 
	implements RepresentationModelAssembler<FullWeatherDTO, EntityModel<FullWeatherDTO>> {

	@Override
	public EntityModel<FullWeatherDTO> toModel(FullWeatherDTO dto) {
		EntityModel<FullWeatherDTO> entityModel = EntityModel.of(dto);
		
		try {
			entityModel.add(linkTo(
					methodOn(FullWeatherApiController.class).getFullWeatherByIPAddress(null))
						.withSelfRel());
		} catch (GeoLocationException e) {
			
			e.printStackTrace();
		}
		
		return entityModel;
	}

}
