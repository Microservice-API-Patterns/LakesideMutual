package com.lakesidemutual.customerselfservice.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLogin;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.UserResponseDto;

import io.swagger.annotations.ApiOperation;

/**
 * This REST controller gives clients access to the current user. It is an example of the
 * <i>Information Holder Resource</i> pattern. This particular one is a special type of information holder called <i>Master Data Holder</i>.
 *
 * @see <a href="http://www.microservice-api-patterns.org/patterns/responsibility/endpointRoles/WADE-InformationHolderResource.html">Information Holder Resource</a>
 * @see <a href="http://www.microservice-api-patterns.org/patterns/responsibility/informationHolderEndpoints/WADE-MasterDataHolder.html">Master Data Holder</a>
 */
@RestController
@RequestMapping("/user")
public class UserInformationHolder {

	@Autowired
	private UserLoginRepository userLoginRepository;

	@ApiOperation(value = "Get the user details for the currently logged-in user.")
	@GetMapping
	public ResponseEntity<UserResponseDto> getCurrentUser() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication instanceof AnonymousAuthenticationToken) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}

		String username = authentication.getName();
		UserLogin user = userLoginRepository.findByEmail(username);
		String email = user.getEmail();
		String customerId = user.getCustomerId() != null ? user.getCustomerId().getId() : null;
		UserResponseDto response = new UserResponseDto(email, customerId);
		return ResponseEntity.ok(response);
	}
}
