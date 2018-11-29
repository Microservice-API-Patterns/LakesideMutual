<template>
  <sui-grid celled="internally" :columns="2">
    <sui-grid-row v-if="customer">
      <sui-grid-column :width="4">
        <h5 is="sui-header">Customer</h5>
      </sui-grid-column>
      <sui-grid-column :width="12" verticalAlign="middle">
        <router-link :to="`/customers/${customer.customerId}`">{{customer.firstname}} {{customer.lastname}}</router-link>
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <h5 is="sui-header">Creation Date</h5>
      </sui-grid-column>
      <sui-grid-column :width="12" verticalAlign="middle">
        {{policy.creationDate | formatDateTime}}
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <h5 is="sui-header">Period</h5>
      </sui-grid-column>
      <sui-grid-column :width="12" verticalAlign="middle">
        {{policy.policyPeriod.startDate | formatDate}} – {{policy.policyPeriod.endDate | formatDate}}
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <h5 is="sui-header">Policy Type</h5>
      </sui-grid-column>
      <sui-grid-column :width="12" verticalAlign="middle">
        {{policy.policyType}}
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <h5 is="sui-header">Insurance Premium</h5>
      </sui-grid-column>
      <sui-grid-column :width="12" verticalAlign="middle">
        {{policy.insurancePremium.amount}} {{policy.insurancePremium.currency}}
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <h5 is="sui-header">Policy Limit</h5>
      </sui-grid-column>
      <sui-grid-column :width="12" verticalAlign="middle">
        {{policy.policyLimit.amount}} {{policy.policyLimit.currency}}
      </sui-grid-column>
    </sui-grid-row>
    <sui-grid-row>
      <sui-grid-column :width="4">
        <h5 is="sui-header">Insuring Agreement</h5>
      </sui-grid-column>
      <sui-grid-column :width="12" verticalAlign="middle">
        <sui-list divided relaxed>
          <span v-if="policy.insuringAgreement.agreementItems.length === 0">N/A</span>
          <sui-list-item v-for="(agreementItem, index) in policy.insuringAgreement.agreementItems" :key="index">
            <sui-list-content>
              <span is="sui-list-header">{{agreementItem.title}}</span>
              <span is="sui-list-description">{{agreementItem.description}}</span>
            </sui-list-content>
          </sui-list-item>
        </sui-list>
      </sui-grid-column>
    </sui-grid-row>
  </sui-grid>
</template>

<script>
import moment from 'moment'

export default {
  name: 'Policy',
  props: ['policy', 'customer'],
  filters: {
    formatDate(date) {
      return moment(date).format('DD. MMM YYYY')
    },
    formatDateTime(date) {
      return moment(date).format('DD. MMM YYYY HH:mm')
    }
  }
}
</script>

<style scoped>
</style>
