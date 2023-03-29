package com.lakesidemutual.customerselfservice.interfaces.dtos.swagger;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Objects;

import javax.persistence.Embeddable;

import org.microserviceapipatterns.domaindrivendesign.ValueObject;

/**
 * An instance of MoneyAmount is a value object that represents an amount of money in a specific currency.
 * For example, this is used to represent the insurance premium of a policy.
 */
@Embeddable
public class MoneyAmount implements ValueObject {
	private BigDecimal amount;
	private Currency currency;

	public MoneyAmount() {}

	public MoneyAmount(BigDecimal amount, Currency currency) {
		Objects.requireNonNull(amount);
		Objects.requireNonNull(currency);
		this.amount = amount;
		this.currency = currency;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public Currency getCurrency() {
		return currency;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}

		MoneyAmount other = (MoneyAmount) obj;
		if(amount.compareTo(other.amount) != 0) {
			return false;
		}

		if(!currency.equals(other.currency)) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return Objects.hash(amount, currency);
	}
}
