package com.lakesidemutual.customercore.interfaces.dtos.customer;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.lakesidemutual.customercore.domain.customer.Address;
import com.lakesidemutual.customercore.domain.customer.CustomerProfileEntity;
import com.lakesidemutual.customercore.interfaces.validation.PhoneNumber;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;
import java.util.Objects;

/**
 * CustomerProfileUpdateRequestDto is a data transfer object (DTO) that represents the personal data (customer profile) of a customer.
 * It is sent to the CustomerInformationHolder when a new customer is created or the profile of an existing customer is updated.
 */
public class CustomerProfileUpdateRequestDto {
    @NotEmpty
    private String firstname;

    @NotEmpty
    private String lastname;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @NotEmpty
    private String streetAddress;

    @NotEmpty
    private String postalCode;

    @NotEmpty
    private String city;

    @Email
    @NotEmpty
    private String email;

    @PhoneNumber
    private String phoneNumber;

    public CustomerProfileUpdateRequestDto() {
    }

    public CustomerProfileUpdateRequestDto(String firstname, String lastname, Date birthday, String streetAddress, String postalCode, String city, String email, String phoneNumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthday = birthday;
        this.streetAddress = streetAddress;
        this.postalCode = postalCode;
        this.city = city;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public CustomerProfileEntity toDomainObject() {
        Address address = new Address(getStreetAddress(), getPostalCode(), getCity());
        return new CustomerProfileEntity(getFirstname(), getLastname(), getBirthday(), address, getEmail(), getPhoneNumber());
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerProfileUpdateRequestDto that = (CustomerProfileUpdateRequestDto) o;
        return Objects.equals(firstname, that.firstname) && Objects.equals(lastname, that.lastname) && Objects.equals(birthday, that.birthday) && Objects.equals(streetAddress, that.streetAddress) && Objects.equals(postalCode, that.postalCode) && Objects.equals(city, that.city) && Objects.equals(email, that.email) && Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstname, lastname, birthday, streetAddress, postalCode, city, email, phoneNumber);
    }
}
