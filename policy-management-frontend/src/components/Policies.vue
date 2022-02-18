<template>
  <div>
    <h3 is="sui-header">Policies</h3>

    <error-message :error="error" resource="Policy" v-if="error != null"/>

    <div is="sui-container" fluid v-if="isLoading">
      <sui-loader active>Loading...</sui-loader>
    </div>

    <sui-table :celled="true" :padded="true" v-if="!isLoading && error == null">
      <sui-table-header>
        <sui-table-row>
          <sui-table-header-cell single-line>Creation Date</sui-table-header-cell>
          <sui-table-header-cell>Customer</sui-table-header-cell>
          <sui-table-header-cell>Insurance Premium</sui-table-header-cell>
          <sui-table-header-cell>Policy Limit</sui-table-header-cell>
          <sui-table-header-cell></sui-table-header-cell>
        </sui-table-row>
      </sui-table-header>

      <sui-table-body>
        <sui-table-row v-for="policy in policies" :key="policy.policyId">
          <sui-table-cell>
            <formatted-date :date="policy.creationDate" full/>
          </sui-table-cell>
          <sui-table-cell>{{policy.customer.firstname}} {{policy.customer.lastname}}</sui-table-cell>
          <sui-table-cell>{{policy.insurancePremium.amount}} {{policy.insurancePremium.currency}}</sui-table-cell>
          <sui-table-cell>{{policy.policyLimit.amount}} {{policy.policyLimit.currency}}</sui-table-cell>
          <sui-table-cell>
            <router-link
              is="sui-button"
              :to="`/policies/${policy.policyId}`"
              compact
              size="small"
            >Show Details</router-link>
          </sui-table-cell>
        </sui-table-row>
      </sui-table-body>

      <sui-table-footer>
        <sui-table-header-cell colspan="3">
          <span
            v-if="offset != null && size != null"
          >{{offset+1}}-{{offset+policies.length}} of {{size}}</span>
        </sui-table-header-cell>
        <sui-table-header-cell colspan="3">
          <sui-menu pagination>
            <a
              is="sui-menu-item"
              icon="left chevron"
              v-on:click="previousPage"
              :disabled="prev == null"
            ></a>
            <a
              is="sui-menu-item"
              icon="right chevron"
              v-on:click="nextPage"
              :disabled="next == null"
            ></a>
          </sui-menu>
        </sui-table-header-cell>
      </sui-table-footer>
    </sui-table>
  </div>
</template>

<script>
import { getPolicies } from '../api'
import ErrorMessage from '@/components/ErrorMessage'
import FormattedDate from '@/components/FormattedDate'

export default {
  name: 'Policies',
  components: { ErrorMessage, FormattedDate },
  data() {
    return {
      policies: [],
      isLoading: false,
      prev: null,
      next: null,
      offset: null,
      size: null,
      error: null
    }
  },
  async created() {
    await this.loadPolicies()
  },
  methods: {
    async loadPolicies(link) {
      this.isLoading = true
      this.error = null

      try {
        const paginatedResponse = await getPolicies(link)
        this.policies = paginatedResponse.policies
        this.prev = paginatedResponse._links.prev
        this.next = paginatedResponse._links.next
        this.offset = paginatedResponse.offset
        this.size = paginatedResponse.size
      } catch (error) {
        this.error = error
      }
      this.isLoading = false
    },
    previousPage(evt) {
      if (this.prev) {
        this.loadPolicies(this.prev.href)
      }
    },
    nextPage(evt) {
      if (this.next) {
        this.loadPolicies(this.next.href)
      }
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
