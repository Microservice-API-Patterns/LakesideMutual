<template>
  <sui-grid celled>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Customer</b>
      </sui-grid-column>
      <sui-grid-column :width="12">
        <router-link
          :to="`/customers/${insuranceQuoteRequest.customerInfo.customerId}`"
        >{{insuranceQuoteRequest.customerInfo.firstname}} {{insuranceQuoteRequest.customerInfo.lastname}}</router-link>
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Address</b>
      </sui-grid-column>
      <sui-grid-column :width="5">
        <b>Contact Address</b>
        <br>
        {{insuranceQuoteRequest.customerInfo.contactAddress.streetAddress}}
        <br>
        {{insuranceQuoteRequest.customerInfo.contactAddress.postalCode}} {{insuranceQuoteRequest.customerInfo.contactAddress.city}}
      </sui-grid-column>
      <sui-grid-column :width="7">
        <b>Billing Address</b>
        <br>
        {{insuranceQuoteRequest.customerInfo.billingAddress.streetAddress}}
        <br>
        {{insuranceQuoteRequest.customerInfo.billingAddress.postalCode}} {{insuranceQuoteRequest.customerInfo.billingAddress.city}}
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Insurance Type</b>
      </sui-grid-column>
      <sui-grid-column :width="12">{{insuranceQuoteRequest.insuranceOptions.insuranceType}}</sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Start Date</b>
      </sui-grid-column>
      <sui-grid-column :width="12">
        <formatted-date :date="insuranceQuoteRequest.insuranceOptions.startDate"/>
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Deductible</b>
      </sui-grid-column>
      <sui-grid-column
        :width="12"
      >{{insuranceQuoteRequest.insuranceOptions.deductible.amount}} {{insuranceQuoteRequest.insuranceOptions.deductible.currency}}</sui-grid-column>
    </sui-grid-row>
  </sui-grid>
</template>

<script>
import FormattedDate from '@/components/FormattedDate'

export default {
  name: 'InsuranceQuoteRequest',
  props: ['insuranceQuoteRequest'],
  components: { FormattedDate }
}
</script>

<style scoped>
</style>
