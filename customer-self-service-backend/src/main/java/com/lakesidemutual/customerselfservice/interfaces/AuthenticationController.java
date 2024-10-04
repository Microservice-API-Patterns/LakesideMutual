package com.lakesidemutual.customerselfservice.interfaces;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLoginEntity;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;
import com.lakesidemutual.customerselfservice.interfaces.configuration.JwtUtils;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.AuthenticationRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.AuthenticationResponseDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.SignupRequestDto;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.UserAlreadyExistsException;
import com.lakesidemutual.customerselfservice.interfaces.dtos.identityaccess.UserResponseDto;


/**
 * This class is a REST Controller that is used to authenticate existing users and to sign up new users.
 * */
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Value("${token.header}")
	private String tokenHeader;

	@Autowired
	private JwtUtils tokenUtils;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserLoginRepository userRepository;

	@Operation(summary = "Authenticate a user based on a given email address and password.")
	@PostMapping
	public ResponseEntity<AuthenticationResponseDto> authenticationRequest(
			@Parameter(description = "the email and password used to authenticate the user", required = true) @RequestBody AuthenticationRequestDto authenticationRequest)
					throws AuthenticationException {

		// Perform the authentication
		try {
			Authentication authentication = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getEmail(), authenticationRequest.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch(AuthenticationException e) {
			logger.info("Authentication of user '" + authenticationRequest.getEmail() + "' failed.");
			throw e;
		}

		// Reload password post-authentication so we can generate token
		UserDetails userDetails = this.userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
		String token = this.tokenUtils.generateToken(userDetails);

		// Return the token
		return ResponseEntity.ok(new AuthenticationResponseDto(authenticationRequest.getEmail(), token));
	}

	@Operation(summary = "Create a new user.")
	@PostMapping(value = "/signup")
	public ResponseEntity<UserResponseDto> signupUser(
			@Parameter(description = "the email and password used to create a new user", required = true) @Valid @RequestBody SignupRequestDto registration) {

		if (userRepository.findByEmail(registration.getEmail()) != null) {
			final String errorMessage = "User with email '" + registration.getEmail() + "' does already exist.";
			logger.info(errorMessage);
			throw new UserAlreadyExistsException(errorMessage);
		}

		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String hashedPassword = passwordEncoder.encode(registration.getPassword());
		UserLoginEntity newUser = new UserLoginEntity(registration.getEmail(), hashedPassword, "ADMIN", null);
		userRepository.save(newUser);
		return ResponseEntity.ok(new UserResponseDto(registration.getEmail(), null));
	}
}
