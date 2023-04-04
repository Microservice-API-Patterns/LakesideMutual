package com.lakesidemutual.customerselfservice.interfaces;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.lakesidemutual.customerselfservice.interfaces.dtos.swagger.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/redirect")
public class RedirectController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RestTemplate restTemplate;

    @Operation(summary = "Get all customers.")
    @GetMapping(value = "/customer-management/customers")
    public Object getCustomers(
            @Parameter(description = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            @Parameter(description = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @Parameter(description = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {

        logger.info("getCustomersgetCustomersgetCustomersgetCustomers");
        System.out.println("getCustomersgetCustomersgetCustomersgetCustomers");

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8100/customers?filter=" + filter + "&limit=" + limit + "&offset=" + offset,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get customer with a given customer id.")
    @GetMapping(value = "/customer-management/customers/{customerId}")
    @CrossOrigin
    public Object getCustomerCustomerMgmt(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerId) {

        logger.info("getCustomergetCustomergetCustomergetCustomergetCustomer");
        System.out.println("getCustomergetCustomergetCustomergetCustomergetCustomer");

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8100/customers/" + customerId,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Update the profile of the customer with the given customer id")
    @PutMapping(value = "/customer-management/customers/{customerId}")
    public Object updateCustomer(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerId,
            @Parameter(description = "the customer's updated profile", required = true) @Valid @RequestBody CustomerProfileDto customerProfile) {

        try {
            restTemplate.put(
                    "http://192.168.1.2:8100/customers/" + customerId,
                    customerProfile);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get the interaction log for a customer with a given customer id.")
    @GetMapping(value = "/customer-management/interaction-logs/{customerId}")
    public Object getInteractionLog(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerId) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8100/interaction-logs/" + customerId,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Acknowledge all of a given customer's interactions up to the given interaction id.")
    @PatchMapping(value = "/customer-management/interaction-logs/{customerId}")
    public Object acknowledgeInteractions(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerId,
            @Parameter(description = "the id of the newest acknowledged interaction", required = true) @Valid @RequestBody InteractionAcknowledgementDto interactionAcknowledgementDto) {

        try {
            return restTemplate.patchForObject(
                    "http://192.168.1.2:8100/interaction-logs/" + customerId,
                    interactionAcknowledgementDto,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get a list of all unacknowledged notifications.")
    @GetMapping(value = "/customer-management/notifications")
    public Object getNotifications() {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8100/notifications/",
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get all customers.")
    @GetMapping(value = "/policy-management/customers")
    public Object getCustomersPolicyManagement(
            @Parameter(description = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            @Parameter(description = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @Parameter(description = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8090/customers?filter=" + filter + "&limit=" + limit + "&offset=" + offset,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get customer with a given customer id.")
    @GetMapping(value = "/policy-management/customers/{customerIdDto}")
    public Object getCustomer(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerIdDto) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8090/customers/" + customerIdDto,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get a customer's policies.")
    @GetMapping(value = "/policy-management/customers/{customerIdDto}/policies")
    public Object getPolicies(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerIdDto,
            @Parameter(description = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8090/customers/" + customerIdDto + "/policies",
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get all Insurance Quote Requests.")
    @GetMapping(value = "/policy-management/insurance-quote-requests")
    public Object getInsuranceQuoteRequests() {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8090/insurance-quote-requests",
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get a specific Insurance Quote Request.")
    @GetMapping(value = "/policy-management/insurance-quote-requests/{id}")
    public Object getInsuranceQuoteRequest(
            @Parameter(description = "the insurance quote request's unique id", required = true) @PathVariable String id) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8090/insurance-quote-requests/" + id,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Updates the status of an existing Insurance Quote Request")
    @PatchMapping(value = "/policy-management/insurance-quote-requests/{id}")
    public Object respondToInsuranceQuoteRequest(
            @Parameter(description = "the insurance quote request's unique id", required = true) @PathVariable String id,
            @Parameter(description = "the response that contains a new insurance quote if the request has been accepted", required = true) @Valid @RequestBody InsuranceQuoteResponseDto insuranceQuoteResponseDto) {

        try {
            return restTemplate.patchForObject(
                    "http://192.168.1.2:8090/insurance-quote-requests/" + id,
                    insuranceQuoteResponseDto,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }

    }

    @Operation(summary = "Create a new policy.")
    @PostMapping(value = "/policy-management/policies")
    public Object createPolicy(
            @Parameter(description = "the policy that is to be added", required = true) @Valid @RequestBody CreatePolicyRequestDto createPolicyDto,
            HttpServletRequest request) {

        try {
            return restTemplate.postForEntity(
                    "http://192.168.1.2:8090/policies",
                    createPolicyDto,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }

    }

    @Operation(summary = "Update an existing policy.")
    @PutMapping(value = "/policy-management/policies/{policyId}")
    public Object updatePolicy(
            @Parameter(description = "the policy's unique id", required = true) @PathVariable String policyId,
            @Parameter(description = "the updated policy", required = true) @Valid @RequestBody CreatePolicyRequestDto createPolicyDto,
            HttpServletRequest request) {

        try {
            restTemplate.put(
                    "http://192.168.1.2:8090/policies/" + policyId,
                    createPolicyDto);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }

    }

    @Operation(summary = "Delete an existing policy.")
    @DeleteMapping(value = "/policy-management/policies/{policyId}")
    public Object deletePolicy(
            @Parameter(description = "the policy's unique id", required = true) @PathVariable String policyId,
            HttpServletRequest request) {

        try {
            restTemplate.delete(
                    "http://192.168.1.2:8090/policies/" + policyId);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }

    }

    @Operation(summary = "Get all policies, newest first.")
    @GetMapping(value = "/policy-management/policies")
    public Object getPoliciesPolicyMgmt(
            @Parameter(description = "the maximum number of policies per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @Parameter(description = "the offset of the page's first policy", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @Parameter(description = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8090/policies/",
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }

    }

    @Operation(summary = "Get a single policy.")
    @GetMapping(value = "/policy-management/policies/{policyId}")
    public Object getPolicy(
            @Parameter(description = "the policy's unique id", required = true) @PathVariable String policyId,
            @Parameter(description = "a comma-separated list of the fields that should be expanded in the response", required = false) @RequestParam(value = "expand", required = false, defaultValue = "") String expand) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8090/policies/" + policyId,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Computes the risk factor for a given customer.")
    @PostMapping(value = "/policy-management/riskfactor/compute")
    public Object computeRiskFactor(
            @Parameter(description = "the request containing relevant customer attributes (e.g., postal code, birthday)", required = true) @Valid @RequestBody RiskFactorRequestDto riskFactorRequest) {

        try {
            return restTemplate.postForEntity(
                    "http://192.168.1.2:8090/riskfactor/compute",
                    riskFactorRequest,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get the cities for a particular postal code.")
    @GetMapping(value = "/customer-core/cities/{postalCode}")
    public Object getCitiesForPostalCode(
            @Parameter(description = "the postal code", required = true) @PathVariable String postalCode) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8110/cities/" + postalCode,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get all customers in pages of 10 entries per page.")
    @GetMapping(value = "/customer-core/customers")
    public Object getCustomers(
            @Parameter(description = "search terms to filter the customers by name", required = false) @RequestParam(value = "filter", required = false, defaultValue = "") String filter,
            @Parameter(description = "the maximum number of customers per page", required = false) @RequestParam(value = "limit", required = false, defaultValue = "10") Integer limit,
            @Parameter(description = "the offset of the page's first customer", required = false) @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
            @Parameter(description = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8110/customers/",
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Get a specific set of customers.")
    @GetMapping(value = "/customer-core/customers/{ids}") // MAP operation responsibility: Retrieval Operation
    public Object getCustomer(
            @Parameter(description = "a comma-separated list of customer ids", required = true) @PathVariable String ids,
            @Parameter(description = "a comma-separated list of the fields that should be included in the response", required = false) @RequestParam(value = "fields", required = false, defaultValue = "") String fields) {

        try {
            return restTemplate.getForObject(
                    "http://192.168.1.2:8110/customers/" + ids,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Update the profile of the customer with the given customer id")
    @PutMapping(value = "/customer-core/customers/{customerId}")
    public Object updateCustomer(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerId,
            @Parameter(description = "the customer's updated profile", required = true) @Valid @RequestBody CustomerProfileUpdateRequestDto requestDto) {

        try {
            restTemplate.put(
                    "http://192.168.1.2:8110/customers/" + customerId,
                    requestDto);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Change a customer's address.")
    @PutMapping(value = "/customer-core/customers/{customerId}/address")
    public Object changeAddress(
            @Parameter(description = "the customer's unique id", required = true) @PathVariable String customerId,
            @Parameter(description = "the customer's new address", required = true) @Valid @RequestBody AddressDto requestDto) {

        try {
            restTemplate.put(
                    "http://192.168.1.2:8110/customers/" + customerId + "/address",
                    requestDto);
            return new ResponseEntity<>("Success", HttpStatus.OK);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

    @Operation(summary = "Create a new customer.")
    @PostMapping(value = "/customer-core/customers")
    public Object createCustomer(
            @Parameter(description = "the customer's profile information", required = true) @Valid @RequestBody CustomerProfileUpdateRequestDto requestDto) {

        try {
            return restTemplate.postForEntity(
                    "http://192.168.1.2:8110/customers",
                    requestDto,
                    Object.class);
        } catch (HttpClientErrorException e) {
            return new ResponseEntity<>(e.getResponseBodyAsString(), e.getStatusCode());
        }
    }

}
