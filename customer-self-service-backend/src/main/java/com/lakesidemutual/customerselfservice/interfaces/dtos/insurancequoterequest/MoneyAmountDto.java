package com.lakesidemutual.customerselfservice.interfaces.dtos.insurancequoterequest;

import java.math.BigDecimal;
import java.util.Currency;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import com.lakesidemutual.customerselfservice.domain.insurancequoterequest.MoneyAmount;

/**
 * MoneyAmountDto is a data transfer object (DTO) that represents an amount of money in a specific currency.
 */
public class MoneyAmountDto {
	@NotNull
	@DecimalMax(value = "1000000000000", inclusive = false)
	@DecimalMin("0")
	private BigDecimal amount;

	@NotEmpty
	private String currency;

	public MoneyAmountDto() {
	}

	private MoneyAmountDto(BigDecimal amount, String currency) {
		this.amount = amount;
		this.currency = currency;
	}

	public static MoneyAmountDto fromDomainObject(MoneyAmount moneyAmount) {
		return new MoneyAmountDto(moneyAmount.getAmount(), moneyAmount.getCurrency().toString());
	}

	public MoneyAmount toDomainObject() {
		return new MoneyAmount(amount, Currency.getInstance(currency));
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}
}
