package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.util.List;

/**
 * The CitiesResponseDto represents a list of city names, transferred as simple
 * (atomic) strings. This is an example of the <a href=
 * "https://www.microservice-api-patterns.org/patterns/structure/representationElements/AtomicParameter">Atomic
 * Parameter</a> pattern.
 */
public class CitiesResponseDto {
	private final List<String> cities;

	public CitiesResponseDto(List<String> cities) {
		this.cities = cities;
	}

	public List<String> getCities() {
		return cities;
	}
}
