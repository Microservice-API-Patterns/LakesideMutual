<template>
  <div>
    <h3 is="sui-header">Customers</h3>

    <error-message :error="error" resource="Customer" v-if="error != null"/>

    <div is="sui-container" fluid v-if="isLoading">
      <sui-loader active>Loading...</sui-loader>
    </div>

    <sui-form v-on:submit="search">
      <sui-grid :columns="2">
        <sui-grid-column verticalAlign="middle">
          <sui-form-field>
            <input v-model="filter" type="text" placeholder="Search...">
          </sui-form-field>
        </sui-grid-column>
        <sui-grid-column verticalAlign="middle">
          <sui-button type="submit" icon="search" primary>Search</sui-button>
        </sui-grid-column>
      </sui-grid>
    </sui-form>

    <sui-table :celled="true" :padded="true" v-if="!isLoading && error == null">
      <sui-table-header>
        <sui-table-row>
          <sui-table-header-cell single-line>Name</sui-table-header-cell>
          <sui-table-header-cell>Email</sui-table-header-cell>
          <sui-table-header-cell>Address</sui-table-header-cell>
          <sui-table-header-cell></sui-table-header-cell>
        </sui-table-row>
      </sui-table-header>

      <sui-table-body>
        <sui-table-row v-for="customer in customers" :key="customer.customerId">
          <sui-table-cell v-html="customer.customerName"></sui-table-cell>
          <sui-table-cell>
            <a :href="`mailto:${customer.email}`">{{customer.email}}</a>
          </sui-table-cell>
          <sui-table-cell>{{customer.streetAddress}}, {{customer.postalCode}} {{customer.city}}</sui-table-cell>
          <sui-table-cell>
            <router-link
              is="sui-button"
              :to="`/customers/${customer.customerId}`"
              compact
              size="small"
            >Manage Policy</router-link>
          </sui-table-cell>
        </sui-table-row>
      </sui-table-body>

      <sui-table-footer>
        <sui-table-header-cell colspan="2">
          <span
            v-if="offset != null && size != null"
          >{{offset+1}}-{{offset+customers.length}} of {{size}}</span>
        </sui-table-header-cell>
        <sui-table-header-cell colspan="2">
          <sui-menu v-sui-floated:right pagination>
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
import { getCustomers } from '../api'
import ErrorMessage from '@/components/ErrorMessage'

export default {
  name: 'Customers',
  components: { ErrorMessage },
  data() {
    return {
      customers: [],
      isLoading: false,
      error: null,
      prev: null,
      next: null,
      offset: null,
      size: null,
      filter: '',
      activeFilter: ''
    }
  },
  async created() {
    await this.loadCustomers(null, '')
  },
  methods: {
    async loadCustomers(link, filter) {
      this.isLoading = true
      this.error = null

      try {
        const paginatedResponse = await getCustomers(link, filter)
        this.customers = paginatedResponse.customers.map(this.addCustomerName)
        this.prev = paginatedResponse._links.prev
        this.next = paginatedResponse._links.next
        this.offset = paginatedResponse.offset
        this.size = paginatedResponse.size
      } catch (error) {
        this.error = error
      }
      this.isLoading = false
    },
    search(evt) {
      evt.preventDefault()
      this.activeFilter = this.filter
      this.loadCustomers(null, this.filter)
    },
    previousPage(evt) {
      if (this.prev) {
        this.loadCustomers(this.prev.href, null)
      }
    },
    nextPage(evt) {
      if (this.next) {
        this.loadCustomers(this.next.href, null)
      }
    },
    getCustomerName(customer) {
      const customerName = `${customer.firstname} ${customer.lastname}`
      if (this.activeFilter === '') {
        return customerName
      }

      const startIndex = customerName
        .toLowerCase()
        .indexOf(this.activeFilter.toLowerCase())
      if (startIndex === -1) {
        return customerName
      }

      const endIndex = startIndex + this.activeFilter.length
      const prefix = customerName.substring(0, startIndex)
      const highlighted = customerName.substring(startIndex, endIndex)
      const suffix = customerName.substring(endIndex)
      return `${prefix}<b>${highlighted}</b>${suffix}`
    },
    addCustomerName(customer) {
      const customerName = this.getCustomerName(customer)
      return Object.assign({}, customer, { customerName })
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
