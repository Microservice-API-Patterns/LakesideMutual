<template>
  <div>
    <sui-breadcrumb>
      <sui-breadcrumb-section>
        <router-link to="/customers">Customers</router-link>
      </sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle"/>
      <sui-breadcrumb-section v-if="customer != null && !isLoadingCustomer">
        <router-link
          :to="`/customers/${this.$route.params.customerid}`"
        >{{customer.firstname}} {{customer.lastname}}</router-link>
      </sui-breadcrumb-section>
      <sui-breadcrumb-section v-if="isLoadingCustomer">Loading...</sui-breadcrumb-section>
      <sui-breadcrumb-divider icon="right angle"/>
      <sui-breadcrumb-section>New Policy</sui-breadcrumb-section>
    </sui-breadcrumb>
    <sui-segment basic v-if="isLoadingCustomer" class="loaderSegment">
      <sui-loader active>Loading...</sui-loader>
    </sui-segment>

    <div v-if="!isLoadingCustomer" style="margin-top: 20px">
      <sui-form :loading="isSubmittingPolicy" :error="hasError" v-on:submit="submitPolicy">
        <sui-grid celled="internally" :columns="2">
          <sui-grid-row>
            <sui-grid-column :width="4" verticalAlign="middle">
              <h5 is="sui-header">Period</h5>
            </sui-grid-column>
            <sui-grid-column :width="12">
              <sui-grid :columns="4">
                <sui-grid-row>
                  <sui-grid-column :width="2" verticalAlign="middle">
                    <center>
                      <span>From</span>
                    </center>
                  </sui-grid-column>
                  <sui-grid-column :width="6">
                    <sui-form-field :error="errorFields.includes('policyPeriod.startDate')">
                      <input v-model="startDate" type="date">
                    </sui-form-field>
                  </sui-grid-column>
                  <sui-grid-column :width="2" verticalAlign="middle">
                    <center>
                      <span>to</span>
                    </center>
                  </sui-grid-column>
                  <sui-grid-column :width="6">
                    <sui-form-field :error="errorFields.includes('policyPeriod.endDate')">
                      <input v-model="endDate" type="date">
                    </sui-form-field>
                  </sui-grid-column>
                </sui-grid-row>
              </sui-grid>
            </sui-grid-column>
          </sui-grid-row>
          <sui-grid-row>
            <sui-grid-column :width="4" verticalAlign="middle">
              <h5 is="sui-header">Policy Type</h5>
            </sui-grid-column>
            <sui-grid-column :width="12">
              <sui-form-field :error="errorFields.includes('policyType')">
                <sui-dropdown
                  :options="policyTypes"
                  placeholder="Policy Type"
                  v-model="policyType"
                />
              </sui-form-field>
            </sui-grid-column>
          </sui-grid-row>
          <sui-grid-row>
            <sui-grid-column :width="4" verticalAlign="middle">
              <h5 is="sui-header">Deductible</h5>
            </sui-grid-column>
            <sui-grid-column :width="12">
              <sui-form-field :error="errorFields.includes('deductible.amount')">
                <input placeholder="Amount in CHF" v-model="deductible" type="number">
              </sui-form-field>
            </sui-grid-column>
          </sui-grid-row>
          <sui-grid-row>
            <sui-grid-column :width="4" verticalAlign="middle">
              <h5 is="sui-header">Insurance Premium</h5>
            </sui-grid-column>
            <sui-grid-column :width="12">
              <sui-form-field :error="errorFields.includes('insurancePremium.amount')">
                <input placeholder="Amount in CHF" v-model="insurancePremium" type="number">
              </sui-form-field>
            </sui-grid-column>
          </sui-grid-row>
          <sui-grid-row>
            <sui-grid-column :width="4" verticalAlign="middle">
              <h5 is="sui-header">Policy Limit</h5>
            </sui-grid-column>
            <sui-grid-column :width="12">
              <sui-form-field :error="errorFields.includes('policyLimit.amount')">
                <input placeholder="Amount in CHF" v-model="policyLimit" type="number">
              </sui-form-field>
            </sui-grid-column>
          </sui-grid-row>
          <sui-grid-row>
            <sui-grid-column :width="4">
              <h5 is="sui-header">Insuring Agreement</h5>
            </sui-grid-column>
            <sui-grid-column :width="12">
              <sui-grid divided="vertically">
                <sui-grid-row
                  :columns="2"
                  v-for="(insuringAgreementItem, index) in insuringAgreementItems"
                  :key="index"
                >
                  <sui-grid-column :width="14">
                    <sui-form-field
                      :error="errorFields.includes(`insuringAgreement.agreementItems[${index}].title`)"
                    >
                      <input placeholder="Title" v-model="insuringAgreementItem.title">
                    </sui-form-field>
                    <sui-form-field
                      :error="errorFields.includes(`insuringAgreement.agreementItems[${index}].description`)"
                    >
                      <textarea
                        placeholder="Description"
                        v-model="insuringAgreementItem.description"
                        rows="4"
                      ></textarea>
                    </sui-form-field>
                  </sui-grid-column>
                  <sui-grid-column :width="2" verticalAlign="middle">
                    <sui-button
                      type="button"
                      icon="trash"
                      color="red"
                      size="tiny"
                      v-on:click="(evt) => removeInsuringItem(evt, index)"
                    />
                  </sui-grid-column>
                </sui-grid-row>
              </sui-grid>
              <br>
              <sui-button
                type="button"
                content="Add Item"
                icon="add"
                color="green"
                size="tiny"
                v-on:click="addInsuringItem"
              />
            </sui-grid-column>
          </sui-grid-row>
        </sui-grid>

        <br>
        <sui-button type="submit">Create Policy</sui-button>

        <sui-message error>
          <sui-message-header>Failed to create policy</sui-message-header>
          <sui-message-list>
            <sui-message-item v-for="(msg, index) in errorMessages" :key="index">{{msg}}</sui-message-item>
          </sui-message-list>
        </sui-message>
      </sui-form>
    </div>
  </div>
</template>

<script>
import { getCustomer, createPolicy } from '../api'
import { extractFormError } from '../utils'
import moment from 'moment'

const context = new Map([
  ['policyPeriod.startDate', 'Start Date'],
  ['policyPeriod.endDate', 'End Date'],
  ['policyType', 'Policy Type'],
  ['deductible.amount', 'Deductible'],
  ['insurancePremium.amount', 'Insurance Premium'],
  ['policyLimit.amount', 'Policy Limit']
])

export default {
  name: 'CreatePolicy',
  data() {
    return {
      customer: null,
      isLoadingCustomer: false,
      startDate: '',
      endDate: '',
      policyType: null,
      deductible: '',
      insurancePremium: '',
      policyLimit: '',
      insuringAgreementItems: [],
      isSubmittingPolicy: false,
      hasError: false,
      errorFields: [],
      errorMessages: [],
      policyTypes: [
        { key: 'life', text: 'Life Insurance', value: 'Life Insurance' },
        { key: 'home', text: 'Home Insurance', value: 'Home Insurance' },
        { key: 'health', text: 'Health Insurance', value: 'Health Insurance' },
        { key: 'car', text: 'Car Insurance', value: 'Car Insurance' }
      ]
    }
  },
  methods: {
    inputToDate(input) {
      return moment(input).toDate()
    },
    dateToInput(date) {
      return moment(date).format('YYYY-MM-DD')
    },
    async submitPolicy(evt) {
      evt.preventDefault()

      try {
        this.isSubmittingPolicy = true
        const customerId = this.$route.params.customerid
        await createPolicy(
          customerId,
          this.inputToDate(this.startDate),
          this.inputToDate(this.endDate),
          this.policyType,
          this.deductible,
          this.insurancePremium,
          this.policyLimit,
          this.insuringAgreementItems
        )
        this.isSubmittingPolicy = false
        this.$router.push(`/customers/${this.$route.params.customerid}`)
      } catch (error) {
        this.isSubmittingPolicy = false
        const formError = error ? extractFormError(error, context) : null
        this.hasError = !!error
        this.errorFields = formError ? formError.errorFields : []
        this.errorMessages = formError ? formError.errorMessages : []
      }
    },
    addInsuringItem(evt) {
      evt.preventDefault()
      this.insuringAgreementItems.push({ title: '', description: '' })
    },
    removeInsuringItem(evt, index) {
      evt.preventDefault()
      this.insuringAgreementItems.splice(index, 1)
    }
  },
  async created() {
    this.isLoadingCustomer = true
    const customerId = this.$route.params.customerid
    const customer = await getCustomer(customerId)
    this.customer = customer
    this.isLoadingCustomer = false
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
