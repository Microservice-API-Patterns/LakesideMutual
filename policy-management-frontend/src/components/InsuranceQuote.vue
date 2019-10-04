<template>
  <sui-grid celled>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Expiration Date</b>
      </sui-grid-column>
      <sui-grid-column :width="12">
        <formatted-date :date="insuranceQuote.expirationDate" full/>
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Insurance Premium</b>
      </sui-grid-column>
      <sui-grid-column
        :width="12"
      >{{insuranceQuote.insurancePremium.amount}} {{insuranceQuote.insurancePremium.currency}}</sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <b>Policy Limit</b>
      </sui-grid-column>
      <sui-grid-column
        :width="12"
      >{{insuranceQuote.policyLimit.amount}} {{insuranceQuote.policyLimit.currency}}</sui-grid-column>
    </sui-grid-row>
  </sui-grid>
</template>

<script>
import FormattedDate from '@/components/FormattedDate'

export default {
  name: 'InsuranceQuote',
  props: ['insuranceQuote'],
  components: { FormattedDate }
}
</script>

<style scoped>
</style>
