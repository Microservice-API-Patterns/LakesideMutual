package com.lakesidemutual.customercore.interfaces;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customercore.domain.city.CityLookupService;
import com.lakesidemutual.customercore.interfaces.dtos.city.CitiesResponseDto;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

/**
 * This REST controller allows clients to retrieve a list of cities that match a given postal code. It is an application of
 * the <a href="http://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpoints/WADE-StaticDataHolder.html">Static Data Holder</a> pattern.
 * A Static Data Holder is a dedicated endpoint that serves as single point of reference for static data (i.e., data that almost never changes).
 *
 * @see <a href=
 *      "http://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpoints/WADE-StaticDataHolder.html">www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpoints/WADE-StaticDataHolder.html</a>
 */
@RestController
@RequestMapping("/cities")
public class CityStaticDataHolder {
	@Autowired
	private CityLookupService cityLookupService;

	@ApiOperation(value = "Get the cities for a particular postal code.")
	@GetMapping(value = "/{postalCode}")
	public ResponseEntity<CitiesResponseDto> getCitiesForPostalCode(
			@ApiParam(value = "the postal code", required = true) @PathVariable String postalCode) {

		List<String> cities = cityLookupService.getCitiesForPostalCode(postalCode);
		CitiesResponseDto response = new CitiesResponseDto(cities);
		return ResponseEntity.ok(response);
	}
}
