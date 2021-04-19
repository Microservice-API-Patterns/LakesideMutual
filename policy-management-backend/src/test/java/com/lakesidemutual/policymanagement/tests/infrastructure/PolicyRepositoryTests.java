package com.lakesidemutual.policymanagement.tests.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.lakesidemutual.policymanagement.domain.customer.CustomerId;
import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;
import com.lakesidemutual.policymanagement.tests.TestUtils;

@RunWith(SpringRunner.class)
@DataJpaTest
public class PolicyRepositoryTests {
	@Autowired
	private PolicyRepository policyRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Before
	public void setUp() {
		entityManager.persist(TestUtils.createTestPolicy("h3riovf4xq", "rgpp0wkpec", TestUtils.createDate(2, Calendar.JANUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2020), BigDecimal.valueOf(1500), BigDecimal.valueOf(1000000), BigDecimal.valueOf(250)));
		entityManager.persist(TestUtils.createTestPolicy("h3riovf5xq", "rgpp0wkpec", TestUtils.createDate(7, Calendar.JANUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2020), BigDecimal.valueOf(1500), BigDecimal.valueOf(100000), BigDecimal.valueOf(190)));
		entityManager.persist(TestUtils.createTestPolicy("h3riovf6xq", "rgpp0wkpec", TestUtils.createDate(3, Calendar.JANUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2020), BigDecimal.valueOf(1500), BigDecimal.valueOf(10000), BigDecimal.valueOf(120)));
		entityManager.persist(TestUtils.createTestPolicy("h3riovf7xq", "rgpp1wkpec", TestUtils.createDate(5, Calendar.JANUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2020), BigDecimal.valueOf(1500), BigDecimal.valueOf(1000000), BigDecimal.valueOf(180)));
		entityManager.persist(TestUtils.createTestPolicy("h3riovf8xq", "rgpp2wkpec", TestUtils.createDate(4, Calendar.JANUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2019), TestUtils.createDate(1, Calendar.FEBRUARY, 2020), BigDecimal.valueOf(1500), BigDecimal.valueOf(1000000), BigDecimal.valueOf(200)));
	}

	@Test
	public void testFindAllByCustomerIdOrderByCreationDateDesc1() throws Exception {
		List<PolicyAggregateRoot> policies = policyRepository.findAllByCustomerIdOrderByCreationDateDesc(new CustomerId("rgpp0wkpec"));
		assertThat(policies).size().isEqualTo(3);
		PolicyAggregateRoot policy1 = policies.get(0);
		PolicyAggregateRoot policy2 = policies.get(1);
		PolicyAggregateRoot policy3 = policies.get(2);
		assertThat(policy1.getCreationDate()).isEqualTo(TestUtils.createDate(7, Calendar.JANUARY, 2019));
		assertThat(policy2.getCreationDate()).isEqualTo(TestUtils.createDate(3, Calendar.JANUARY, 2019));
		assertThat(policy3.getCreationDate()).isEqualTo(TestUtils.createDate(2, Calendar.JANUARY, 2019));
	}

	@Test
	public void testFindAllByCustomerIdOrderByCreationDateDesc2() throws Exception {
		List<PolicyAggregateRoot> policies = policyRepository.findAllByCustomerIdOrderByCreationDateDesc(new CustomerId("rgpp1wkpec"));
		assertThat(policies).size().isEqualTo(1);
		PolicyAggregateRoot policy1 = policies.get(0);
		assertThat(policy1.getCreationDate()).isEqualTo(TestUtils.createDate(5, Calendar.JANUARY, 2019));
	}

	@Test
	public void testFindAllByCustomerIdOrderByCreationDateDesc3() throws Exception {
		List<PolicyAggregateRoot> policies = policyRepository.findAllByCustomerIdOrderByCreationDateDesc(new CustomerId("abcdef"));
		assertThat(policies).size().isEqualTo(0);
	}
}
