package com.lakesidemutual.customerselfservice.interfaces;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLoginEntity;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.UserResponseDto;

/**
 * This REST controller gives clients access to the current user. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/InformationHolderResource">Information Holder Resource</a>
 * @see <a href="https://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpointTypes/MasterDataHolder">Master Data Holder</a>
 */
@RestController
@RequestMapping("/user")
public class UserInformationHolder {

	@Autowired
	private UserLoginRepository userLoginRepository;

	@Operation(summary = "Get the user details for the currently logged-in user.")
	@PreAuthorize("isAuthenticated()")
	@GetMapping
	public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {
		String username = authentication.getName();
		UserLoginEntity user = userLoginRepository.findByEmail(username);
		String email = user.getEmail();
		String customerId = user.getCustomerId() != null ? user.getCustomerId().getId() : null;
		UserResponseDto response = new UserResponseDto(email, customerId);
		return ResponseEntity.ok(response);
	}
}
