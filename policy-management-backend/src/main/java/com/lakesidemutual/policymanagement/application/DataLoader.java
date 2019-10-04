package com.lakesidemutual.policymanagement.application;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Collections;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.policy.InsuringAgreementEntity;
import com.lakesidemutual.policymanagement.domain.policy.MoneyAmount;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.domain.policy.PolicyId;
import com.lakesidemutual.policymanagement.domain.policy.PolicyPeriod;
import com.lakesidemutual.policymanagement.domain.policy.PolicyType;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;

/**
 * The run() method of the DataLoader class is automatically executed when the application launches.
 * It populates the database with sample policies that can be used to test the application.
 * */
@Component
public class DataLoader implements ApplicationRunner {
	@Autowired
	private PolicyRepository policyRepository;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	@Override
	public void run(ApplicationArguments args) throws ParseException {
		if(policyRepository.count() > 0) {
			logger.info("Skipping import of application dummy data, because the database already contains existing entities.");
			return;
		}

		PolicyId policyId = new PolicyId("fvo5pkqerr");
		CustomerId customerId = new CustomerId("rgpp0wkpec");
		Date startDate = new GregorianCalendar(2018, Calendar.FEBRUARY, 5).getTime();
		Date endDate = new GregorianCalendar(2018, Calendar.FEBRUARY, 10).getTime();
		PolicyPeriod policyPeriod = new PolicyPeriod(startDate, endDate);
		PolicyType policyType = new PolicyType("Health Insurance");
		MoneyAmount deductible = new MoneyAmount(BigDecimal.valueOf(1500), Currency.getInstance("CHF"));
		MoneyAmount policyLimit = new MoneyAmount(BigDecimal.valueOf(1000000), Currency.getInstance("CHF"));
		MoneyAmount insurancePremium = new MoneyAmount(BigDecimal.valueOf(250), Currency.getInstance("CHF"));
		InsuringAgreementEntity insuringAgreement = new InsuringAgreementEntity(Collections.emptyList());
		PolicyAggregateRoot policy = new PolicyAggregateRoot(policyId, customerId, new Date(), policyPeriod, policyType,
				deductible, policyLimit, insurancePremium, insuringAgreement);
		policyRepository.save(policy);
		logger.info("DataLoader has successfully imported all application dummy data, the application is now ready.");
	}
}