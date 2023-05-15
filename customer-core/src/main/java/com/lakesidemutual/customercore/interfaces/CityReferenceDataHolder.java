package com.lakesidemutual.customercore.interfaces;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customercore.domain.city.CityLookupService;
import com.lakesidemutual.customercore.interfaces.dtos.city.CitiesResponseDto;

/**
 * This REST controller allows clients to retrieve a list of cities that match a given postal code. It is an application of
 * the <a href="https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/ReferenceDataHolder">Reference Data Holder</a> pattern.
 * A Reference Data Holder is a dedicated endpoint that serves as single point of reference for static data (i.e., data that almost never changes).
 *
 * @see <a href=
 *      "https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/ReferenceDataHolder">https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/ReferenceDataHolder</a>
 */
@RestController
@RequestMapping("/cities")
public class CityReferenceDataHolder {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private CityLookupService cityLookupService;

	@Operation(summary = "Get the cities for a particular postal code.")
	@GetMapping(value = "/{postalCode}")
	public ResponseEntity<CitiesResponseDto> getCitiesForPostalCode(
			@Parameter(description = "the postal code", required = true) @PathVariable String postalCode,
			@RequestHeader(value = "rest-tester", required = false) String restTester) {
		
		if(restTester == null){
			logger.info("TRIGGERED EDGE");
		} else {
			logger.info("TRIGGERED NODE: " + restTester);
		} 

		List<String> cities = cityLookupService.getCitiesForPostalCode(postalCode);
		CitiesResponseDto response = new CitiesResponseDto(cities);
		return ResponseEntity.ok(response);
	}
}
