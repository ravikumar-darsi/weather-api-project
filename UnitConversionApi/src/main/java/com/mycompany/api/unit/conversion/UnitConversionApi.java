package com.mycompany.api.unit.conversion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/unit-conversion")
public class UnitConversionApi {

	@PostMapping
	public ResponseEntity<ConversionDetails> convert(@RequestBody ConversionDetails details) {
		try {
			
			UnitConverter.convert(details);
			
		} catch (UnitConversionException e) {
			return ResponseEntity.badRequest().build();
		}
		
		return new ResponseEntity<>(details, HttpStatus.OK);
	}
}
