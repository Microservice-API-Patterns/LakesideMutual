<template>
  <div>
    <sui-breadcrumb>
      <sui-breadcrumb-section><router-link to="/customers">Customers</router-link></sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle" />
      <sui-breadcrumb-section v-if="customer != null && !isLoadingCustomer && error == null">{{customer.firstname}} {{customer.lastname}}</sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="isLoadingCustomer">Loading...</sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="!isLoadingCustomer && error != null">Error</sui-breadcrumb-section>
    </sui-breadcrumb>
    <sui-button compact size="mini" floated="right" v-on:click="openCustomerManagement" ><sui-icon name="external"/> Open in Customer Management</sui-button>
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
          <sui-button floated="right" positive v-on:click="hideRiskFactorInfo" class="modalButton">
            OK
          </sui-button>
          <br/>
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
          <br/>
          <sui-button circular icon="info" size="tiny" compact v-on:click="showRiskFactorInfo"/>
        </sui-grid-column>
      </sui-grid>

      <sui-divider/>
      <h4 is="sui-header">
        Insurance Policy
        <router-link is="sui-button" :to="`/customers/${customer.customerId}/edit-policy`" floated="right" compact size="tiny" color="blue" v-if="!isLoadingPolicy && policy != null">Edit Policy</router-link>
      </h4>
      <sui-segment basic v-if="isLoadingPolicy" class="loaderSegment">
        <sui-loader active>Loading...</sui-loader>
      </sui-segment>
      <div v-if="!isLoadingPolicy && policy != null">
        <br/>
        <policy :policy="policy"/>
      </div>
      <div v-if="!isLoadingPolicy && policy == null">
        <router-link is="sui-button" :to="`/customers/${customer.customerId}/edit-policy`" compact size="tiny" color="green">Create Policy</router-link>
      </div>

      <sui-divider/>
      <h4 is="sui-header">
        Policy History
      </h4>
      <sui-segment basic v-if="isLoadingPolicyHistory" class="loaderSegment">
        <sui-loader active>Loading...</sui-loader>
      </sui-segment>
      <div v-if="!isLoadingPolicyHistory && policyHistory.length > 0">
        <policies-table :policies="policyHistory" :create-detail-link="(policyId) => `/customers/${this.$route.params.customerid}/${policyId}`"/>
      </div>
      <div v-if="!isLoadingPolicyHistory && policyHistory.length == 0">
        No older policies available.
      </div>
    </div>
  </div>
</template>

<script>
import {
  getCustomer,
  computeRiskFactor,
  getActivePolicy,
  getPolicyHistory
} from '../api'
import CustomerProfile from '@/components/CustomerProfile'
import Policy from '@/components/Policy'
import PoliciesTable from '@/components/PoliciesTable'
import ErrorMessage from '@/components/ErrorMessage'

export default {
  name: 'CustomerDetail',
  components: { CustomerProfile, Policy, PoliciesTable, ErrorMessage },
  data() {
    return {
      customer: null,
      riskFactor: null,
      isLoadingCustomer: false,
      error: null,
      policy: null,
      isLoadingPolicy: false,
      policyHistory: [],
      isLoadingPolicyHistory: false,
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
      } else if (this.riskFactor <= 100) {
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
    }
  },
  async created() {
    this.error = null
    this.isLoadingCustomer = true
    const customerId = this.$route.params.customerid
    try {
      const customer = await getCustomer(customerId)
      this.customer = customer
      const response = await computeRiskFactor(customer)
      this.riskFactor = response.riskFactor
    } catch (error) {
      this.error = error
    }
    this.isLoadingCustomer = false

    if (this.error != null) {
      return
    }

    try {
      this.isLoadingPolicy = true
      this.isLoadingPolicyHistory = true

      const [policy, policyHistory] = await Promise.all([
        getActivePolicy(customerId),
        getPolicyHistory(customerId)
      ])

      this.policy = policy
      this.policyHistory = policyHistory

      this.isLoadingPolicy = false
      this.isLoadingPolicyHistory = false
    } catch (error) {
      this.isLoadingPolicy = false
      this.isLoadingPolicyHistory = false
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

.modalButton {
  margin-bottom: 10px;
}
</style>
