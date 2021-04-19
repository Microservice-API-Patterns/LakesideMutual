package com.lakesidemutual.policymanagement.tests.infrastructure;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.math.BigDecimal;
import java.util.Calendar;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.broker.BrokerService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.lakesidemutual.policymanagement.domain.policy.UpdatePolicyEvent;
import com.lakesidemutual.policymanagement.infrastructure.RiskManagementMessageProducer;
import com.lakesidemutual.policymanagement.interfaces.dtos.customer.CustomerDto;
import com.lakesidemutual.policymanagement.interfaces.dtos.policy.PolicyDto;
import com.lakesidemutual.policymanagement.tests.TestUtils;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@TestPropertySource(properties = {
		"riskmanagement.queueName=newpolicies",
})
public class RiskManagementMessageProducerTests {
	private static String brokerBindAddress = "tcp://localhost:61616";

	@Value("${riskmanagement.queueName}")
	private String queueName;

	@TestConfiguration
	static class TestBeanConfiguration {
		@Bean
		public BrokerService broker() throws Exception {
			final BrokerService broker = new BrokerService();
			broker.addConnector(brokerBindAddress);
			broker.setPersistent(false);
			return broker;
		}

		@Bean
		public RiskManagementMessageProducer riskManagementMessageProducer() {
			return new RiskManagementMessageProducer();
		}

		@Bean
		public JmsTemplate jmsTemplate() {
			ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory();
			activeMQConnectionFactory.setBrokerURL(brokerBindAddress);

			MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
			converter.setTargetType(MessageType.TEXT);
			converter.setTypeIdPropertyName("_type");

			JmsTemplate jmsTemplate = new JmsTemplate(activeMQConnectionFactory);
			jmsTemplate.setMessageConverter(converter);
			jmsTemplate.setReceiveTimeout(1000);
			return jmsTemplate;
		}
	}

	@Autowired
	private RiskManagementMessageProducer riskManagementMessageProducer;

	@Autowired
	private JmsTemplate jmsTemplate;

	private PolicyDto policyA;
	private PolicyDto policyB;
	private PolicyDto policyC;
	private CustomerDto customerA;

	@Before
	public void setUp() {
		policyA = TestUtils.createTestPolicyDto("h3riovf4xq", "rgpp0wkpec", TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), BigDecimal.valueOf(1500), BigDecimal.valueOf(1000000), BigDecimal.valueOf(250));
		policyB = TestUtils.createTestPolicyDto("h3riovf5xq", "rgpp1wkpec", TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), BigDecimal.valueOf(1500), BigDecimal.valueOf(100000), BigDecimal.valueOf(190));
		policyC = TestUtils.createTestPolicyDto("h3riovf6xq", "rgpp2wkpec", TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), BigDecimal.valueOf(1500), BigDecimal.valueOf(10000), BigDecimal.valueOf(120));
		customerA = TestUtils.createTestCustomer("rgpp0wkpec", "Max", "Mustermann", TestUtils.createDate(1, Calendar.JANUARY, 1990), "Oberseestrasse 10", "8640", "Rapperswil", "max@example.com", "055 222 41 11");
	}

	@Test
	public void testUpdatePolicyEventNotification() throws Exception {
		final UpdatePolicyEvent event = new UpdatePolicyEvent("localhost", TestUtils.createDate(1, Calendar.JANUARY, 2018), customerA, policyA);
		riskManagementMessageProducer.emitEvent(event);

		try {
			UpdatePolicyEvent policyEvent1 = (UpdatePolicyEvent)jmsTemplate.receiveAndConvert(queueName);
			assertEqualUpdatePolicyEvent(policyEvent1, "localhost", "rgpp0wkpec", "Max Mustermann", "h3riovf4xq");

			UpdatePolicyEvent policyEvent2 = (UpdatePolicyEvent)jmsTemplate.receiveAndConvert(queueName);
			assertNull(policyEvent2);
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	@Test
	public void testMultipleUpdatePolicyEventNotifications() throws Exception {
		final UpdatePolicyEvent event1 = new UpdatePolicyEvent("localhost", TestUtils.createDate(1, Calendar.JANUARY, 2018), customerA, policyA);
		riskManagementMessageProducer.emitEvent(event1);
		final UpdatePolicyEvent event2 = new UpdatePolicyEvent("localhost", TestUtils.createDate(1, Calendar.JANUARY, 2018), customerA, policyB);
		riskManagementMessageProducer.emitEvent(event2);
		final UpdatePolicyEvent event3 = new UpdatePolicyEvent("localhost", TestUtils.createDate(1, Calendar.JANUARY, 2018), customerA, policyC);
		riskManagementMessageProducer.emitEvent(event3);

		try {
			UpdatePolicyEvent policyEvent1 = (UpdatePolicyEvent)jmsTemplate.receiveAndConvert(queueName);
			assertEqualUpdatePolicyEvent(policyEvent1, "localhost", "rgpp0wkpec", "Max Mustermann", "h3riovf4xq");

			UpdatePolicyEvent policyEvent2 = (UpdatePolicyEvent)jmsTemplate.receiveAndConvert(queueName);
			assertEqualUpdatePolicyEvent(policyEvent2, "localhost", "rgpp0wkpec", "Max Mustermann", "h3riovf5xq");

			UpdatePolicyEvent policyEvent3 = (UpdatePolicyEvent)jmsTemplate.receiveAndConvert(queueName);
			assertEqualUpdatePolicyEvent(policyEvent3, "localhost", "rgpp0wkpec", "Max Mustermann", "h3riovf6xq");

			UpdatePolicyEvent policyEvent4 = (UpdatePolicyEvent)jmsTemplate.receiveAndConvert(queueName);
			assertNull(policyEvent4);
		} catch(Exception e) {
			Assert.fail(e.getMessage());
		}
	}

	private void assertEqualUpdatePolicyEvent(UpdatePolicyEvent policyEvent, String originator, String customerId, String name, String policyId) {
		assertNotNull(policyEvent);
		assertEquals(customerId, policyEvent.getCustomer().getCustomerId());
		assertEquals(name, policyEvent.getCustomer().getCustomerProfile().getFirstname() + " " + policyEvent.getCustomer().getCustomerProfile().getLastname());
		assertEquals(policyId, policyEvent.getPolicy().getPolicyId());
		assertEquals(originator, policyEvent.getOriginator());
	}
}