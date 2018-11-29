<template>
  <sui-table :celled="true" :padded="true">
    <sui-table-header>
      <sui-table-row>
        <sui-table-header-cell single-line>Creation Date</sui-table-header-cell>
        <sui-table-header-cell>Insurance Premium</sui-table-header-cell>
        <sui-table-header-cell>Policy Limit</sui-table-header-cell>
        <sui-table-header-cell></sui-table-header-cell>
      </sui-table-row>
    </sui-table-header>

    <sui-table-body>
      <sui-table-row v-for="policy in policies" :key="policy.policyId">
        <sui-table-cell>
          {{policy.creationDate | formatDateTime}}
        </sui-table-cell>
        <sui-table-cell>
          {{policy.insurancePremium.amount}} {{policy.insurancePremium.currency}}
        </sui-table-cell>
        <sui-table-cell>
          {{policy.policyLimit.amount}} {{policy.policyLimit.currency}}
        </sui-table-cell>
        <sui-table-cell>
          <router-link is="sui-button" :to="createDetailLink(policy.policyId)" compact size="small">Show Details</router-link>
        </sui-table-cell>
      </sui-table-row>
    </sui-table-body>
  </sui-table>
</template>

<script>
import moment from 'moment'

export default {
  name: 'PoliciesTable',
  props: ['policies', 'createDetailLink'],
  filters: {
    formatDateTime(date) {
      return moment(date).format('DD. MMM YYYY HH:mm')
    }
  }
}
</script>

<style scoped>
</style>
