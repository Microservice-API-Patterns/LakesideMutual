<template>
  <div>
    <sui-breadcrumb>
      <sui-breadcrumb-section>
        <router-link to="/customers">Customers</router-link>
      </sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle"/>
      <sui-breadcrumb-section
        v-if="customer != null && !isLoadingCustomer && error == null"
      >{{customer.firstname}} {{customer.lastname}}</sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="isLoadingCustomer">Loading...</sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="!isLoadingCustomer && error != null">Error</sui-breadcrumb-section>
    </sui-breadcrumb>
    <sui-button compact size="mini" floated="right" v-on:click="openCustomerManagement">
      <sui-icon name="external"/>Open in Customer Management
    </sui-button>
    <sui-segment basic v-if="isLoadingCustomer" class="loaderSegment">
      <sui-loader active>Loading...</sui-loader>
    </sui-segment>

    <error-message :error="error" resource="Customer" v-if="error != null"/>

    <div v-if="!isLoadingCustomer && error == null" style="margin-top: 20px">
      <sui-modal v-model="open">
        <sui-modal-header>Risk Factor</sui-modal-header>
        <sui-modal-content>
          <sui-modal-description>
            <p>
              The risk factor can be used as a measure to assess whether a specific customer will likely issue a lot of insurance claims.
              It is a number between 0 and 100 where 0 means "low risk" and 100 means "high risk".
            </p>
          </sui-modal-description>
        </sui-modal-content>
        <sui-modal-actions>
          <sui-button
            floated="right"
            positive
            v-on:click="hideRiskFactorInfo"
            class="modalButton"
          >OK</sui-button>
          <br>
        </sui-modal-actions>
      </sui-modal>

      <sui-grid :columns="2">
        <sui-grid-column verticalAlign="middle">
          <h4 is="sui-header">Customer Profile</h4>
          <customer-profile :customer="customer"/>
        </sui-grid-column>
        <sui-grid-column textAlign="center" verticalAlign="middle">
          <sui-statistic :color="riskFactorColor">
            <sui-statistic-label>Risk Factor</sui-statistic-label>
            <sui-statistic-value>{{riskFactor}}</sui-statistic-value>
          </sui-statistic>
          <br>
          <sui-button circular icon="info" size="tiny" compact v-on:click="showRiskFactorInfo"/>
        </sui-grid-column>
      </sui-grid>

      <sui-divider/>
      <h4 is="sui-header">Insurance Policies
        <router-link
          is="sui-button"
          :to="`/customers/${customer.customerId}/policies/new`"
          floated="right"
          compact
          size="tiny"
          color="green"
          icon="plus"
        >New Policy</router-link>
      </h4>
      <sui-segment basic v-if="isLoadingPolicies || isLoadingCustomer" class="loaderSegment">
        <sui-loader active>Loading...</sui-loader>
      </sui-segment>
      <div v-if="!isLoadingPolicies && !isLoadingCustomer && policies.length > 0">
        <br>
        <div v-for="policy in policies" :key="policy.policyId">
          <policy :policy="policy" :customer="customer" :onDelete="onDeletePolicy"/>
          <br>
        </div>
      </div>
      <p
        v-if="!isLoadingPolicies && !isLoadingCustomer && policies.length === 0"
      >This customer doesn't have any insurance policies yet.</p>
    </div>
  </div>
</template>

<script>
import { getCustomer, computeRiskFactor, getCustomerPolicies } from '../api'
import CustomerProfile from '@/components/CustomerProfile'
import Policy from '@/components/Policy'
import ErrorMessage from '@/components/ErrorMessage'

export default {
  name: 'CustomerDetail',
  components: { CustomerProfile, Policy, ErrorMessage },
  data() {
    return {
      customer: null,
      riskFactor: null,
      isLoadingCustomer: false,
      error: null,
      policies: [],
      isLoadingPolicies: false,
      open: false
    }
  },
  computed: {
    riskFactorColor() {
      if (this.riskFactor == null) {
        return 'grey'
      } else if (this.riskFactor <= 20) {
        return 'green'
      } else if (this.riskFactor <= 40) {
        return 'olive'
      } else if (this.riskFactor <= 60) {
        return 'yellow'
      } else if (this.riskFactor <= 80) {
        return 'orange'
      } else {
        return 'red'
      }
    }
  },
  methods: {
    showRiskFactorInfo() {
      this.open = true
    },
    hideRiskFactorInfo() {
      this.open = false
    },
    openCustomerManagement() {
      window.location = `http://localhost:3020/customers/${
        this.customer.customerId
      }`
    },
    async loadCustomer(customerId) {
      this.isLoadingCustomer = true
      try {
        const customer = await getCustomer(customerId)
        this.customer = customer
        const response = await computeRiskFactor(customer)
        this.riskFactor = response.riskFactor
      } catch (error) {
        this.error = error
      }
      this.isLoadingCustomer = false
    },
    async loadPolicies(customerId) {
      this.isLoadingPolicies = true
      try {
        const policies = await getCustomerPolicies(customerId)
        this.policies = policies
      } catch (error) {
        this.error = error
      }
      this.isLoadingPolicies = false
    },
    async onDeletePolicy() {
      const customerId = this.$route.params.customerid
      await this.loadPolicies(customerId)
    }
  },
  async created() {
    const customerId = this.$route.params.customerid
    await this.loadCustomer(customerId)
    if (this.error != null) {
      return
    }
    await this.loadPolicies(customerId)
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

.modalButton {
  margin-bottom: 10px;
}
</style>
