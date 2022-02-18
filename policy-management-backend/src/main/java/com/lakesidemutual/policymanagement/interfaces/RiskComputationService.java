package com.lakesidemutual.policymanagement.interfaces;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

import javax.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.policymanagement.interfaces.dtos.risk.RiskFactorRequestDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.risk.RiskFactorResponseDto;

/**
 * This REST controller allows clients to compute the risk factor for a given customer. It is an application of
 * the <a href="https://www.microservice-api-patterns.org/patterns/responsibility/operationResponsibilities/ComputationFunction">Computation Function</a> pattern.
 * A Computation Service performs a function f that only depends on its input parameters and does not alter the state of the server.
 *
 * @see <a href=
 *      "https://www.microservice-api-patterns.org/patterns/responsibility/operationResponsibilities/ComputationFunction">https://www.microservice-api-patterns.org/patterns/responsibility/operationResponsibilities/ComputationFunction</a>
 */
@RestController
@RequestMapping("/riskfactor")
public class RiskComputationService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Operation(summary = "Computes the risk factor for a given customer.")
	@PostMapping(value = "/compute")
	public ResponseEntity<RiskFactorResponseDto> computeRiskFactor(
			@Parameter(description = "the request containing relevant customer attributes (e.g., postal code, birthday)", required = true)
			@Valid
			@RequestBody
			RiskFactorRequestDto riskFactorRequest) {
		int age = getAge(riskFactorRequest.getBirthday());
		String postalCode = riskFactorRequest.getPostalCode();
		logger.debug("Computing risk factor (age={}, postal-code={})", age, postalCode);
		int riskFactor = computeRiskFactor(age, postalCode);
		return ResponseEntity.ok(new RiskFactorResponseDto(riskFactor));
	}

	private int getAge(Date birthday) {
		LocalDate birthdayLocalDate = birthday.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate now = LocalDate.now();
		return Period.between(birthdayLocalDate, now).getYears();
	}

	private int computeRiskFactor(int age, String postalCode) {
		int ageGroupRiskFactor = computeAgeGroupRiskFactor(age);
		int localityRiskFactor = computeLocalityRiskFactor(postalCode);
		return (ageGroupRiskFactor + localityRiskFactor) / 2;
	}

	private int computeAgeGroupRiskFactor(int age) {
		if(age > 90) {
			return 100;
		} else if(age > 70) {
			return 90;
		} else if(age > 60) {
			return 70;
		} else if(age > 50) {
			return 60;
		} else if(age > 40) {
			return 50;
		} else if(age > 25) {
			return 20;
		} else {
			return 40;
		}
	}

	private int computeLocalityRiskFactor(String postalCodeStr) {
		try {
			int postalCode = Integer.parseInt(postalCodeStr);
			if((postalCode >= 8000 && postalCode < 9000) || (postalCode >= 1000 && postalCode < 2000)) {
				return 80;
			} else if(postalCode >= 5000 && postalCode < 6000) {
				return 10;
			} else {
				return 30;
			}
		} catch(NumberFormatException e) {
			return 0;
		}
	}
}