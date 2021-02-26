package com.lakesidemutual.customercore.application;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
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
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerAggregateRoot;
import com.lakesidemutual.customercore.domain.customer.CustomerId;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;
import com.lakesidemutual.customercore.infrastructure.CustomerRepository;

/**
 * The run() method of the DataLoader class is automatically executed when the application launches.
 * It populates the database with sample customers that can be used to test the application.
 * */
@Component
@Profile("!test")
public class DataLoader implements ApplicationRunner {
	@Autowired
	private CustomerRepository customerRepository;

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private List<CustomerAggregateRoot> createCustomersFromDummyData(List<Map<String, String>> customerData) {
		SimpleDateFormat birthdayFormat = new SimpleDateFormat("MM/dd/yyyy");
		List<CustomerAggregateRoot> customers = customerData.stream().map(data -> {
			final CustomerId customerId = new CustomerId(data.get("id"));
			final String firstName = data.get("first_name");
			final String lastName = data.get("last_name");
			Date birthday = null;
			try {
				birthday = birthdayFormat.parse(data.get("birthday"));
			} catch (ParseException e) {
			}
			final Address currentAddress = new Address(data.get("street_address"), data.get("postal_code"), data.get("city"));
			final String email = data.get("email");
			final String phoneNumber = data.get("phone_number");
			final CustomerProfileEntity customerProfile = new CustomerProfileEntity(firstName, lastName, birthday, currentAddress, email, phoneNumber);
			return new CustomerAggregateRoot(customerId, customerProfile);
		}).collect(Collectors.toList());
		return customers;
	}


	/**
	 * Note: The files mock_customers_small.csv and mock_customers_large.csv are the same as the files
	 * mock_users_small.csv and mock_users_large.csv in the Customer Self-Service backend. The Customer
	 * Core DataLoader creates a customer for each row in the corresponding CSV file and the
	 * Customer Self-Service DataLoader creates a user login for each of these customers. Therefore,
	 * these files should be kept in sync.
	 *
	 * This dummy data was generated using https://mockaroo.com/
	 */
	private List<Map<String, String>> loadCustomers() {
		try(InputStream file = new ClassPathResource("mock_customers_small.csv").getInputStream()) {
			CsvMapper mapper = new CsvMapper();
			CsvSchema schema = CsvSchema.emptySchema().withHeader();
			MappingIterator<Map<String, String>> readValues = mapper.readerFor(Map.class).with(schema).readValues(file);
			return readValues.readAll();
		} catch (Exception e) {
			return Collections.emptyList();
		}
	}

	@Override
	public void run(ApplicationArguments args) throws ParseException {
		if(customerRepository.count() > 0) {
			logger.info("Skipping import of application dummy data, because the database already contains existing entities.");
			return;
		}

		List<Map<String, String>> customerData = loadCustomers();

		logger.info("Loaded " + customerData.size() + " customers.");

		List<CustomerAggregateRoot> customers = createCustomersFromDummyData(customerData);

		logger.info("Created " + customerData.size() + " customers.");

		customerRepository.saveAll(customers);

		logger.info("Inserted " + customerData.size() + " customers into database.");

		logger.info("DataLoader has successfully imported all application dummy data, the application is now ready.");
	}
}