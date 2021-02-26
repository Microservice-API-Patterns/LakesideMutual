package com.lakesidemutual.customercore.tests.interfaces.dtos.customer;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.junit4.SpringRunner;

import com.lakesidemutual.customercore.interfaces.dtos.customer.AddressDto;

@RunWith(SpringRunner.class)
@JsonTest
public class AddressDtoTests {
	@Autowired
	private JacksonTester<AddressDto> json;

	@Test
	public void deserializeJson() throws Exception {
		String content = "{\"streetAddress\":\"Oberseestrasse 10\",\"postalCode\":\"8640\",\"city\":\"Rapperswil\"}";
		AddressDto address = json.parseObject(content);
		assertThat(address.getStreetAddress()).isEqualTo("Oberseestrasse 10");
		assertThat(address.getPostalCode()).isEqualTo("8640");
		assertThat(address.getCity()).isEqualTo("Rapperswil");
	}

	@Test
	public void serializeJson() throws Exception {
		AddressDto address = new AddressDto("Oberseestrasse 10", "8640", "Rapperswil");
		assertJsonPropertyEquals(address, "@.streetAddress", "Oberseestrasse 10");
		assertJsonPropertyEquals(address, "@.postalCode", "8640");
		assertJsonPropertyEquals(address, "@.city", "Rapperswil");
	}

	private void assertJsonPropertyEquals(AddressDto address, String key, String value) throws Exception {
		assertThat(json.write(address)).extractingJsonPathStringValue(key).isEqualTo(value);
	}
}
