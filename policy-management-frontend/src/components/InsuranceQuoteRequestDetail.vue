<template>
  <div>
    <sui-breadcrumb>
      <sui-breadcrumb-section>
        <router-link to="/insurance-quote-requests">Insurance Quote Requests</router-link>
      </sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle"/>
      <sui-breadcrumb-section v-if="isLoadingInsuranceQuoteRequest && error == null">Loading...</sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="!isLoadingInsuranceQuoteRequest && error != null">Error!</sui-breadcrumb-section>
      <sui-breadcrumb-section
        v-if="!isLoadingInsuranceQuoteRequest && insuranceQuoteRequest != null"
      >{{insuranceQuoteRequest.insuranceOptions.insuranceType}}</sui-breadcrumb-section>
    </sui-breadcrumb>

    <sui-segment basic v-if="isLoadingInsuranceQuoteRequest" class="loaderSegment">
      <sui-loader active>Loading...</sui-loader>
    </sui-segment>
    <div v-if="!isLoadingInsuranceQuoteRequest && insuranceQuoteRequest != null">
      <status-description
        v-for="(statusChange, index) in insuranceQuoteRequest.statusHistory"
        :key="index"
        :insuranceQuoteRequest="insuranceQuoteRequest"
        :date="statusChange.date"
        :status="statusChange.status"
        :isCurrentStatus="index === insuranceQuoteRequest.statusHistory.length-1"
        :onUpdate="loadInsuranceQuoteRequest"
      />
    </div>
    <error-message :error="error" resource="Insurance Quote Request" v-if="error != null"/>
  </div>
</template>

<script>
import { getInsuranceQuoteRequest } from '../api'
import ErrorMessage from '@/components/ErrorMessage'
import StatusDescription from '@/components/StatusDescription'

export default {
  name: 'InsuranceQuoteRequestDetail',
  components: {
    ErrorMessage,
    StatusDescription
  },
  data() {
    return {
      insuranceQuoteRequest: null,
      isLoadingInsuranceQuoteRequest: false,
      error: null
    }
  },
  async created() {
    this.loadInsuranceQuoteRequest()
  },
  methods: {
    async loadInsuranceQuoteRequest() {
      this.isLoadingInsuranceQuoteRequest = true
      const requestId = this.$route.params.requestid
      try {
        const insuranceQuoteRequest = await getInsuranceQuoteRequest(requestId)
        this.insuranceQuoteRequest = insuranceQuoteRequest
      } catch (error) {
        this.error = error
      }
      this.isLoadingInsuranceQuoteRequest = false
    }
  }
}
</script>

<style scoped>
.ui.breadcrumb {
  font-size: 1.28571429rem;
  margin-top: 0px;
}

.loaderSegment {
  padding-top: 60px;
  padding-bottom: 60px;
}
</style>
