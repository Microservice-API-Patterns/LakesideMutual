<template>
  <div>
    <sui-breadcrumb>
      <sui-breadcrumb-section>
        <router-link to="/">Policies</router-link>
      </sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle"/>
      <sui-breadcrumb-section v-if="isLoadingPolicy && error == null">Loading...</sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="!isLoadingPolicy && error != null">Error!</sui-breadcrumb-section>
      <sui-breadcrumb-section
        v-if="!isLoadingPolicy && policy != null"
      >{{policy.policyType}} Policy ({{policy.customer.firstname}} {{policy.customer.lastname}})</sui-breadcrumb-section>
    </sui-breadcrumb>
    <sui-segment basic v-if="isLoadingPolicy" class="loaderSegment">
      <sui-loader active>Loading...</sui-loader>
    </sui-segment>
    <div v-if="!isLoadingPolicy && error == null && policy != null">
      <br>
      <policy :policy="policy" :customer="policy.customer" :onDelete="onDeletePolicy"/>
    </div>
    <error-message :error="error" resource="Policy" v-if="error != null"/>
  </div>
</template>

<script>
import { getPolicy } from '../api'
import Policy from '@/components/Policy'
import ErrorMessage from '@/components/ErrorMessage'

export default {
  name: 'PolicyDetail',
  components: { Policy, ErrorMessage },
  data() {
    return {
      policy: null,
      isLoadingPolicy: false,
      error: null
    }
  },
  methods: {
    onDeletePolicy() {
      this.$router.push('/')
    }
  },
  async created() {
    this.isLoadingPolicy = true
    const policyId = this.$route.params.policyid
    try {
      const policy = await getPolicy(policyId, true)
      this.policy = policy
    } catch (error) {
      this.error = error
    }
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
