package com.skyapi.weatherforecast.location;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.PagedModel.PageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.BadRequestException;
import com.skyapi.weatherforecast.common.Location;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@RestController
@RequestMapping("/v1/locations")
@Validated

public class LocationApiController {

	private LocationService service;
	private ModelMapper modelMapper;

	private Map<String, String> propertyMap = Map.of("code", "code", "city_name", "cityName", "region_name",
			"regionName", "country_code", "countryCode", "country_Name", "countryName", "enabled", "enabled");

	public LocationApiController(LocationService service, ModelMapper modelMapper) {
		super();
		this.service = service;
		this.modelMapper = modelMapper;
	}

	@PostMapping
	public ResponseEntity<LocationDTO> addLocation(@RequestBody @Valid LocationDTO dto) {
		Location addedLocation = service.add(dto2Entity(dto));
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

		return ResponseEntity.created(uri).body(entity2DTO(addedLocation));
	}

	@Deprecated
	public ResponseEntity<?> listLocations() {
		List<Location> locations = service.list();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(listEntity2ListDTO(locations));

	}

	@GetMapping
	public ResponseEntity<?> listLocations(
			@RequestParam(value = "page", required = false, defaultValue = "1") @Min(value = 1) Integer pageNum,
			@RequestParam(value = "size", required = false, defaultValue = "5") @Min(value = 5) @Max(value = 20) Integer pageSize,
			@RequestParam(value = "sort", required = false, defaultValue = "code") String sortField)
			throws BadRequestException {

		if (!propertyMap.containsKey(sortField)) {
			throw new BadRequestException("invalid sort field: " + sortField);
		}

		Page<Location> page = service.listByPage(pageNum - 1, pageSize, propertyMap.get(sortField));

		List<Location> locations = page.getContent();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(addPageMetadataAndLinks2Collection(listEntity2ListDTO(locations), page, sortField));		
	}

	private CollectionModel<LocationDTO> addPageMetadataAndLinks2Collection(
			List<LocationDTO> listDTO, Page<Location> pageInfo, String sortField) throws BadRequestException {
		
		// add self link to each individual item
		for (LocationDTO dto : listDTO) {
			dto.add(linkTo(methodOn(LocationApiController.class).getLocation(dto.getCode())).withSelfRel());
		}
		
		int pageSize = pageInfo.getSize();
		int pageNum = pageInfo.getNumber() + 1;
		long totalElements = pageInfo.getTotalElements();
		int totalPages = pageInfo.getTotalPages();
		
		PageMetadata pageMetadata = new PageMetadata(pageSize, pageNum, totalElements);
		
		CollectionModel<LocationDTO> collectionModel = PagedModel.of(listDTO, pageMetadata);
		
		// add self link to collection
		collectionModel.add(linkTo(methodOn(LocationApiController.class).listLocations(pageNum, pageSize, sortField)).withSelfRel());
		
		if (pageNum > 1) {
			// adding link to first page if the current page is not the first one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class).listLocations(1, pageSize, sortField))
						.withRel(IanaLinkRelations.FIRST));
			
			// adding link to the previous page if the current page is not the first one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class).listLocations(pageNum - 1, pageSize, sortField))
						.withRel(IanaLinkRelations.PREV));			
		}
		
		if (pageNum < totalPages) {
			// adding link to next page if the current page is not the last one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class).listLocations(pageNum + 1, pageSize, sortField))
						.withRel(IanaLinkRelations.NEXT));			
			
			// add link to last page if the current page is not the last one
			collectionModel.add(
					linkTo(methodOn(LocationApiController.class).listLocations(totalPages, pageSize, sortField))
						.withRel(IanaLinkRelations.LAST));					
		}
		
		
		return collectionModel;
		
	}

	@GetMapping("/{code}")
	public ResponseEntity<?> getLocation(@PathVariable("code") String code) {
		Location location = service.get(code);

		return ResponseEntity.ok(entity2DTO(location));
	}

	@PutMapping
	public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto) {

		Location updatedLocation = service.update(dto2Entity(dto));

		return ResponseEntity.ok(entity2DTO(updatedLocation));

	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {

		service.delete(code);

		return ResponseEntity.noContent().build();

	}

	private List<LocationDTO> listEntity2ListDTO(List<Location> listEntity) {

		return listEntity.stream().map(entity -> entity2DTO(entity)).collect(Collectors.toList());

	}

	private LocationDTO entity2DTO(Location entity) {
		return modelMapper.map(entity, LocationDTO.class);
	}

	private Location dto2Entity(LocationDTO dto) {
		return modelMapper.map(dto, Location.class);
	}

}
