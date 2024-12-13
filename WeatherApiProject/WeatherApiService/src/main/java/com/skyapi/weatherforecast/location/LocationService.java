package com.skyapi.weatherforecast.location;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skyapi.weatherforecast.AbstractLocationService;
import com.skyapi.weatherforecast.common.Location;

@Service
@Transactional
public class LocationService extends AbstractLocationService {

	private LocationRepository repo;

	public LocationService(LocationRepository repo) {
		super();
		this.repo = repo;
	}

	public Location add(Location location) {
		return repo.save(location);
	}

	@Deprecated
	List<Location> list() {
		return repo.findUntrashed();
	}

	public Page<Location> listByPage(int pageNum, int pageSize, String sortField) {
		Sort sort = Sort.by(sortField).ascending();
		Pageable pageable = PageRequest.of(pageNum, pageSize, sort);

		return repo.findUntrashed(pageable);
	}

	public Location get(String code) {
		Location location = repo.findByCode(code);

		if (location == null) {
			throw new LocationNotFoundException(code);

		}
		return location;
	}

	public Location update(Location locationInRequest) {
		String code = locationInRequest.getCode();

		Location locationInDB = repo.findByCode(code);

		if (locationInDB == null) {
			throw new LocationNotFoundException(code);
		}

		locationInDB.copyFieldsFrom(locationInRequest);

		return repo.save(locationInDB);
	}

	public void delete(String code) {
		Location location = repo.findByCode(code);

		if (location == null) {
			throw new LocationNotFoundException(code);
		}

		repo.trashByCode(code);
	}
	
	
}
