<template>
  <div>
    <h3 is="sui-header">Insurance Quote Requests</h3>

    <error-message :error="error" resource="Insurance Quote Request" v-if="error != null"/>

    <div is="sui-container" fluid v-if="isLoading">
      <sui-loader active>Loading...</sui-loader>
    </div>

    <b>Open Insurance Quote Requests</b>
    <p
      v-if="!isLoading && error == null && openInsuranceQuoteRequests.length == 0"
    >No open Insurance Quote Requests available.</p>

    <insurance-quote-requests-table
      :insuranceQuoteRequests="openInsuranceQuoteRequests"
      v-if="!isLoading && error == null && openInsuranceQuoteRequests.length > 0"
    />

    <b>Past Insurance Quote Requests</b>
    <p
      v-if="!isLoading && error == null && pastInsuranceQuoteRequests.length == 0"
    >No past Insurance Quote Requests available.</p>

    <insurance-quote-requests-table
      :insuranceQuoteRequests="pastInsuranceQuoteRequests"
      v-if="!isLoading && error == null && pastInsuranceQuoteRequests.length > 0"
    />
  </div>
</template>

<script>
import { getInsuranceQuoteRequests } from '../api'
import ErrorMessage from '@/components/ErrorMessage'
import InsuranceQuoteRequestsTable from '@/components/InsuranceQuoteRequestsTable'

export default {
  name: 'InsuranceQuoteRequests',
  components: { ErrorMessage, InsuranceQuoteRequestsTable },
  data() {
    return {
      openInsuranceQuoteRequests: [],
      pastInsuranceQuoteRequests: [],
      isLoading: false,
      error: null
    }
  },
  async created() {
    await this.loadInsuranceQuoteRequests()
  },
  methods: {
    async loadInsuranceQuoteRequests() {
      this.isLoading = true
      this.error = null

      try {
        const allInsuranceQuoteRequests = await getInsuranceQuoteRequests()
        this.openInsuranceQuoteRequests = allInsuranceQuoteRequests.filter(
          request => {
            const status = this.getStatus(request)
            return status === 'REQUEST_SUBMITTED' || status === 'QUOTE_RECEIVED'
          }
        )
        this.pastInsuranceQuoteRequests = allInsuranceQuoteRequests.filter(
          request => {
            const status = this.getStatus(request)
            return (
              status === 'REQUEST_REJECTED' ||
              status === 'POLICY_CREATED' ||
              status === 'QUOTE_REJECTED' ||
              status === 'QUOTE_EXPIRED'
            )
          }
        )
      } catch (error) {
        this.error = error
      }
      this.isLoading = false
    },
    getStatus(insuranceQuoteRequest) {
      const statusHistory = insuranceQuoteRequest.statusHistory
      return statusHistory[statusHistory.length - 1].status
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.ui.fluid.container {
  padding-top: 140px;
}
</style>
