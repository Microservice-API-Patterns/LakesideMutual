package com.lakesidemutual.policymanagement.interfaces.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.apache.activemq.security.SimpleAuthenticationPlugin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

/**
 * The MessagingConfiguration class configures the ActiveMQ message broker. This broker is used
 * to send events to the Risk Management Server when a policy changes.
 * */
@Configuration
public class MessagingConfiguration {
	@Value("${riskmanagement.stompBrokerBindAddress}")
	private String stompBrokerBindAddress;

	@Value("${riskmanagement.tcpBrokerBindAddress}")
	private String tcpBrokerBindAddress;

	@Bean
	public BrokerService broker(@Value("${spring.activemq.user}") String user, @Value("${spring.activemq.password}") String password) throws Exception {
		final BrokerService broker = new BrokerService();
		broker.addConnector(stompBrokerBindAddress);
		broker.addConnector(tcpBrokerBindAddress);
		broker.setPersistent(true);
		broker.setUseJmx(true);

		final Map<String, String> userPasswords = new HashMap<>();
		userPasswords.put(user, password);
		SimpleAuthenticationPlugin authenticationPlugin = new SimpleAuthenticationPlugin();
		authenticationPlugin.setUserPasswords(userPasswords);
		broker.setPlugins(new BrokerPlugin[] { authenticationPlugin });
		return broker;
	}

	/**
	 * The @Bean attribute turns the returned object (in this case a MessageConverter) into a Spring bean.
	 * A bean is an object that is instantiated, assembled, and otherwise managed by a Spring container.
	 * Other classes can use dependency injection (e.g., using the @Autowired annotation) to obtain a reference
	 * to a specific bean.
	 *
	 * @see <a href=
	 *      "https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html">https://docs.spring.io/spring-javaconfig/docs/1.0.0.M4/reference/html/ch02s02.html</a>
	 */
	@Bean
	public MessageConverter jacksonJmsMessageConverter() {
		MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
		converter.setTargetType(MessageType.TEXT);
		converter.setTypeIdPropertyName("_type");
		return converter;
	}
}