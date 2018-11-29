<template>
  <div>
    <sui-breadcrumb>
      <sui-breadcrumb-section><router-link to="/">Policies</router-link></sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle" />
      <sui-breadcrumb-section v-if="customer != null && !isLoadingCustomer">
        <router-link :to="`/customers/${customer.customerId}`">{{customer.firstname}} {{customer.lastname}}</router-link>
      </sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="isLoadingCustomer">Loading...</sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle" />
      <sui-breadcrumb-section v-if="isLoadingPolicy || policy == null">Loading...</sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="!isLoadingPolicy && policy != null">{{policy.policyType}} Policy</sui-breadcrumb-section>
    </sui-breadcrumb>
    <sui-segment basic v-if="isLoadingPolicy" class="loaderSegment">
      <sui-loader active>Loading...</sui-loader>
    </sui-segment>
    <div v-if="!isLoadingPolicy && policy != null">
      <br/>
      <policy :policy="policy"/>
    </div>
  </div>
</template>

<script>
import { getCustomer, getPolicy } from '../api'
import Policy from '@/components/Policy'

export default {
  name: 'CustomerPolicyDetail',
  components: { Policy },
  data() {
    return {
      customer: null,
      isLoadingCustomer: false,
      policy: null,
      isLoadingPolicy: false
    }
  },
  async created() {
    this.isLoadingCustomer = true
    const customerId = this.$route.params.customerid
    const customer = await getCustomer(customerId)
    this.customer = customer
    this.isLoadingCustomer = false

    this.isLoadingPolicy = true
    const policyId = this.$route.params.policyid
    const policy = await getPolicy(policyId)
    this.policy = policy
    this.isLoadingPolicy = false
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
