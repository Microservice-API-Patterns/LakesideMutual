<template>
  <sui-table :celled="true" compact>
    <sui-table-header>
      <sui-table-row>
        <sui-table-header-cell>Date</sui-table-header-cell>
        <sui-table-header-cell>Customer</sui-table-header-cell>
        <sui-table-header-cell>Insurance Type</sui-table-header-cell>
        <sui-table-header-cell>Status</sui-table-header-cell>
        <sui-table-header-cell collapsing></sui-table-header-cell>
      </sui-table-row>
    </sui-table-header>

    <sui-table-body>
      <sui-table-row
        v-for="insuranceQuoteRequest in insuranceQuoteRequests"
        :key="insuranceQuoteRequest.id"
      >
        <sui-table-cell>
          <formatted-date :date="insuranceQuoteRequest.date" full/>
        </sui-table-cell>
        <sui-table-cell>
          <router-link
            :to="`customers/${insuranceQuoteRequest.customerInfo.customerId}`"
          >{{insuranceQuoteRequest.customerInfo.firstname}} {{insuranceQuoteRequest.customerInfo.lastname}}</router-link>
        </sui-table-cell>
        <sui-table-cell>{{insuranceQuoteRequest.insuranceOptions.insuranceType}}</sui-table-cell>
        <sui-table-cell>
          <request-status :status="getStatus(insuranceQuoteRequest)"/>
        </sui-table-cell>
        <sui-table-cell>
          <router-link
            is="sui-button"
            :to="`insurance-quote-requests/${insuranceQuoteRequest.id}`"
            compact
            size="small"
            style="white-space: nowrap;"
          >Show Details</router-link>
        </sui-table-cell>
      </sui-table-row>
    </sui-table-body>
  </sui-table>
</template>

<script>
import RequestStatus from '@/components/RequestStatus'
import FormattedDate from '@/components/FormattedDate'

export default {
  name: 'InsuranceQuoteRequestsTable',
  components: { RequestStatus, FormattedDate },
  props: ['insuranceQuoteRequests'],
  methods: {
    getStatus(insuranceQuoteRequest) {
      const statusHistory = insuranceQuoteRequest.statusHistory
      return statusHistory[statusHistory.length - 1].status
    }
  }
}
</script>

<style scoped>
</style>
