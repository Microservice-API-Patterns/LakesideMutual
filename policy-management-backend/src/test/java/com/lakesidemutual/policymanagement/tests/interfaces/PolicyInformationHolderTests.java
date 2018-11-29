package com.lakesidemutual.policymanagement.tests.interfaces;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Optional;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultMatcher;

import com.lakesidemutual.policymanagement.domain.policy.PolicyAggregateRoot;
import com.lakesidemutual.policymanagement.infrastructure.CustomerCoreService;
import com.lakesidemutual.policymanagement.infrastructure.PolicyRepository;
import com.lakesidemutual.policymanagement.infrastructure.RiskManagementService;
import com.lakesidemutual.policymanagement.interfaces.PolicyInformationHolder;
import com.lakesidemutual.policymanagement.tests.TestUtils;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(value = PolicyInformationHolder.class, secure = false)
public class PolicyInformationHolderTests {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private PolicyRepository policyRepository;

	@MockBean
	private RiskManagementService riskManagementService;

	@MockBean
	private CustomerCoreService customerCoreService;

	private PolicyAggregateRoot policyA;
	private PolicyAggregateRoot policyB;
	private PolicyAggregateRoot policyC;

	@Before
	public void setUp() {
		policyA = TestUtils.createTestPolicy("h3riovf4xq", "rgpp0wkpec", TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), BigDecimal.valueOf(1000000), BigDecimal.valueOf(250));
		policyB = TestUtils.createTestPolicy("h3riovf5xq", "rgpp1wkpec", TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), BigDecimal.valueOf(100000), BigDecimal.valueOf(190));
		policyC = TestUtils.createTestPolicy("h3riovf6xq", "rgpp2wkpec", TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), TestUtils.createDate(1, Calendar.JANUARY, 1990), BigDecimal.valueOf(10000), BigDecimal.valueOf(120));
	}

	@Test
	public void whenPoliciesExist_thenGetPoliciesShouldReturnPaginatedResponses() throws Exception {
		Mockito.when(policyRepository.findAll(Sort.by(Sort.Direction.DESC, PolicyAggregateRoot.FIELD_CREATION_DATE))).thenReturn(Arrays.asList(policyA, policyB, policyC));

		mvc.perform(get("/policies?limit=2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.limit", is(2)))
		.andExpect(jsonPath("$.size", is(3)))
		.andExpect(jsonPath("$.offset", is(0)))
		.andExpect(jsonPath("$.policies", hasSize(2)))
		.andExpect(new PolicyResultMatcher("$.policies[0]", policyA))
		.andExpect(new PolicyResultMatcher("$.policies[1]", policyB));

		mvc.perform(get("/policies?limit=2&offset=2"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.limit", is(2)))
		.andExpect(jsonPath("$.size", is(3)))
		.andExpect(jsonPath("$.offset", is(2)))
		.andExpect(jsonPath("$.policies", hasSize(1)))
		.andExpect(new PolicyResultMatcher("$.policies[0]", policyC));
	}

	@Test
	public void whenNoPoliciesExist_thenGetAllPoliciesShouldReturnEmptyArray() throws Exception {
		mvc.perform(get("/policies"))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.limit", is(10)))
		.andExpect(jsonPath("$.size", is(0)))
		.andExpect(jsonPath("$.offset", is(0)))
		.andExpect(jsonPath("$.policies", hasSize(0)));
	}

	@Test
	public void whenNonexistingPolicyIdIsUsed_thenNotFoundShouldBeReturned() throws Exception {
		mvc.perform(get("/policies/" + policyA.getId().getId().toString())).andExpect(status().isNotFound());
		mvc.perform(get("/policies/" + policyB.getId().getId().toString())).andExpect(status().isNotFound());
		mvc.perform(get("/policies/" + policyC.getId().getId().toString())).andExpect(status().isNotFound());
	}

	@Test
	public void whenExistingPolicyIdIsUsed_thenCustomerShouldBeReturned() throws Exception {
		Mockito.when(policyRepository.findById(policyA.getId())).thenReturn(Optional.of(policyA));
		Mockito.when(policyRepository.findById(policyB.getId())).thenReturn(Optional.of(policyB));
		Mockito.when(policyRepository.findById(policyC.getId())).thenReturn(Optional.of(policyC));

		mvc.perform(get("/policies/" + policyA.getId().getId().toString()))
		.andExpect(status().isOk())
		.andExpect(new PolicyResultMatcher("$", policyA));
		mvc.perform(get("/policies/" + policyB.getId().getId().toString()))
		.andExpect(status().isOk())
		.andExpect(new PolicyResultMatcher("$", policyB));
		mvc.perform(get("/policies/" + policyC.getId().getId().toString()))
		.andExpect(status().isOk())
		.andExpect(new PolicyResultMatcher("$", policyC));
	}

	class PolicyResultMatcher implements ResultMatcher {
		private String jsonPathPrefix;
		private PolicyAggregateRoot policy;

		PolicyResultMatcher(String jsonPathPrefix, PolicyAggregateRoot policy) {
			this.jsonPathPrefix = jsonPathPrefix;
			this.policy = policy;
		}

		private void matchJson(MvcResult result, String jsonPath, String expected) throws Exception {
			jsonPath(jsonPathPrefix + jsonPath, is(expected)).match(result);
		}

		private void matchJson(MvcResult result, String jsonPath, int expected) throws Exception {
			jsonPath(jsonPathPrefix + jsonPath, is(expected)).match(result);
		}

		private <T> void matchJson(MvcResult result, String jsonPath, Matcher<T> matcher) throws Exception {
			jsonPath(jsonPathPrefix + jsonPath, matcher).match(result);
		}

		@Override
		public void match(MvcResult result) throws Exception {
			matchJson(result, ".policyId", policy.getId().getId().toString());
			matchJson(result, ".customer", policy.getCustomerId().getId().toString());
			matchJson(result, ".creationDate", TestUtils.createISO8601Timestamp(policy.getCreationDate()));
			matchJson(result, ".policyPeriod.startDate", TestUtils.createISO8601Timestamp(policy.getPolicyPeriod().getStartDate()));
			matchJson(result, ".policyPeriod.endDate",   TestUtils.createISO8601Timestamp(policy.getPolicyPeriod().getEndDate()));
			matchJson(result, ".policyLimit.amount", policy.getPolicyLimit().getAmount().intValue());
			matchJson(result, ".policyLimit.currency", policy.getPolicyLimit().getCurrency().toString());
			matchJson(result, ".insurancePremium.amount", policy.getInsurancePremium().getAmount().intValue());
			matchJson(result, ".insurancePremium.currency", policy.getInsurancePremium().getCurrency().toString());
			matchJson(result, ".insuringAgreement.agreementItems", hasSize(0));
		}
	}
}
