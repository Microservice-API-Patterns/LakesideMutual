package com.lakesidemutual.customercore.interfaces.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import com.lm.ccore.GetCustomerRequest;
import com.lm.ccore.GetCustomerResponse;

@Component
public class CustomerCoreRemoteSOAPProxy extends WebServiceGatewaySupport {

	private static final Logger log = LoggerFactory.getLogger(CustomerCoreRemoteSOAPProxy.class);

	public GetCustomerResponse getCustomerbyId(String id) {

		GetCustomerRequest request = new GetCustomerRequest();
		request.setId(id);

		log.info("Requesting customer name for " + id);

		GetCustomerResponse response = (GetCustomerResponse) getWebServiceTemplate()
				.marshalSendAndReceive("http://localhost:8110/ws/customers", request,
						new SoapActionCallback(
								"http://lm.com/ccore/GetCustomerRequest"));

		return response;
	}
}