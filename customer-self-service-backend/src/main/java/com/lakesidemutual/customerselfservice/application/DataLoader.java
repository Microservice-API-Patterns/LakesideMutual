package com.lakesidemutual.customerselfservice.application;

import java.io.InputStream;
import java.text.ParseException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.lakesidemutual.customerselfservice.domain.customer.CustomerId;
import com.lakesidemutual.customerselfservice.domain.identityaccess.UserLogin;
import com.lakesidemutual.customerselfservice.infrastructure.UserLoginRepository;

/**
 * The run() method of the DataLoader class is automatically executed when the application launches.
 * It populates the database with sample user logins that can be used to test the application.
 * */
@Component
@Profile("!test")
public class DataLoader implements ApplicationRunner {
	@Autowired
	private UserLoginRepository userRepository;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<UserLogin> createUserLoginsFromDummyData(List<Map<String, String>> userData) {
		PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return userData.stream().map(data -> {
			final String email = data.get("email");
			final String hashedPassword = passwordEncoder.encode(data.get("password"));
			final CustomerId customerId = new CustomerId(data.get("id"));
			return new UserLogin(email, hashedPassword, "USER", customerId);
		}).collect(Collectors.toList());
	}

	/**
	 * Note: The files mock_users_small.csv and mock_users_large.csv are the same as the files
	 * mock_customers_small.csv and mock_customers_large.csv in the Customer Core. The Customer
	 * Core DataLoader creates a customer for each row in the corresponding CSV file and the
	 * Customer Self-Service DataLoader creates a user login for each of these customers. Therefore,
	 * these files should be kept in sync.
	 *
	 * This dummy data was generated using https://mockaroo.com/
	 */
	private List<Map<String, String>> loadUsers() {
		try {
			InputStream file = new ClassPathResource("mock_users_small.csv").getInputStream();
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = CsvSchema.emptySchema().withHeader();
			MappingIterator<Map<String, String>> readValues = mapper.readerFor(Map.class).with(schema).readValues(file);
			return readValues.readAll();
		} catch (Exception e) {
			logger.error("Could not load mock data", e);
			return Collections.emptyList();
		}
	}

	@Override
	public void run(ApplicationArguments args) throws ParseException {

		List<Map<String, String>> userData = loadUsers();

		logger.info("Loaded " + userData.size() + " users.");

		List<UserLogin> userLogins = createUserLoginsFromDummyData(userData);

		logger.info("Created " + userLogins.size() + " user logins.");

		userRepository.saveAll(userLogins);

		logger.info("Inserted " + userLogins.size() + " user logins into database.");

		logger.info("DataLoader has successfully imported all application dummy data, the application is now ready.");
	}
}