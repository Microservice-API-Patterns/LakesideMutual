<template>
  <div>
    <br />
    <b>
      <u>
        <formatted-date :date="date" full />
      </u>
    </b>
    <div v-if="status === 'REQUEST_SUBMITTED'">
      <p>
        <router-link
          :to="`/customers/${insuranceQuoteRequest.customerInfo.customerId}`"
        >{{insuranceQuoteRequest.customerInfo.firstname}} {{insuranceQuoteRequest.customerInfo.lastname}}</router-link>
        requested a quote for a new
        {{insuranceQuoteRequest.insuranceOptions.insuranceType}} policy:
      </p>

      <insurance-quote-request :insuranceQuoteRequest="insuranceQuoteRequest" />

      <div v-if="isCurrentStatus">
        <p>
          Lakeside Mutual has not yet responded to this request.
          <sui-button size="mini" compact color="blue" v-on:click="openResponseModal">Respond</sui-button>
        </p>

        <sui-modal v-model="responseModalOpen" size="small" :closable="true">
          <sui-modal-header>Respond to Insurance Quote Request</sui-modal-header>
          <sui-modal-content>
            <sui-form :error="hasError">
              <div class="radioButtons">
                <input type="radio" id="accept" :checked="accept" v-on:click="onAccept" />
                <label for="accept">Accept</label>
                <input type="radio" id="reject" :checked="!accept" v-on:click="onReject" />
                <label for="reject">Reject</label>
              </div>
              <sui-form-field v-if="accept">
                <label>Expiration Date</label>
                <input v-model="expirationDate" type="datetime-local" />
              </sui-form-field>
              <sui-form-field
                v-if="accept"
                :error="errorFields.includes('insurancePremium.amount')"
              >
                <label>Insurance Premium</label>
                <input placeholder="Amount in CHF" v-model="insurancePremium" type="number" />
              </sui-form-field>
              <sui-form-field v-if="accept" :error="errorFields.includes('policyLimit.amount')">
                <label>Policy Limit</label>
                <input placeholder="Amount in CHF" v-model="policyLimit" type="number" />
              </sui-form-field>
              <sui-message error>
                <sui-message-header>Failed to respond to Insurance Quote Request</sui-message-header>
                <sui-message-list>
                  <sui-message-item v-for="(msg, index) in errorMessages" :key="index">{{msg}}</sui-message-item>
                </sui-message-list>
              </sui-message>
            </sui-form>
          </sui-modal-content>
          <sui-modal-actions>
            <sui-button v-on:click="cancelResponseModal">Cancel</sui-button>
            <sui-button
              positive
              v-on:click="confirmResponseModal"
              :disabled="!isValid()"
            >Send Response</sui-button>
          </sui-modal-actions>
        </sui-modal>
      </div>
    </div>
    <div v-if="status === 'REQUEST_REJECTED'">
      Lakeside Mutual has
      <span style="color: red;">rejected</span> this Insurance Quote Request.
    </div>
    <div v-if="status === 'QUOTE_RECEIVED'">
      <p>Lakeside Mutual has submitted the following Insurance Quote:</p>
      <insurance-quote :insuranceQuote="insuranceQuoteRequest.insuranceQuote" />
      <p v-if="isCurrentStatus">
        <router-link
          :to="`/customers/${insuranceQuoteRequest.customerInfo.customerId}`"
        >{{insuranceQuoteRequest.customerInfo.firstname}} {{insuranceQuoteRequest.customerInfo.lastname}}</router-link>&nbsp;has not yet accepted or rejected this Insurance Quote.
      </p>
    </div>
    <p v-if="status === 'QUOTE_ACCEPTED'">
      <router-link
        :to="`/customers/${insuranceQuoteRequest.customerInfo.customerId}`"
      >{{insuranceQuoteRequest.customerInfo.firstname}} {{insuranceQuoteRequest.customerInfo.lastname}}</router-link>&nbsp;has
      <span style="color: green;">accepted</span> this Insurance Quote.
    </p>
    <p v-if="status === 'POLICY_CREATED'">
      <router-link :to="`/policies/${insuranceQuoteRequest.policyId}`">A new policy</router-link>&nbsp;has been created.
    </p>
    <p v-if="status === 'QUOTE_REJECTED'">
      <router-link
        :to="`/customers/${insuranceQuoteRequest.customerInfo.customerId}`"
      >{{insuranceQuoteRequest.customerInfo.firstname}} {{insuranceQuoteRequest.customerInfo.lastname}}</router-link>&nbsp;has
      <span style="color: red;">rejected</span> this Insurance Quote.
    </p>
    <p v-if="status === 'QUOTE_EXPIRED'">
      This Insurance Quote has
      <span style="color: red;">expired</span>.
    </p>
  </div>
</template>

<script>
import { extractFormError } from '../utils'
import { respondToInsuranceQuoteRequest } from '../api'
import InsuranceQuote from '@/components/InsuranceQuote'
import InsuranceQuoteRequest from '@/components/InsuranceQuoteRequest'
import FormattedDate from '@/components/FormattedDate'
import moment from 'moment'

const context = new Map([
  ['insurancePremium.amount', 'Insurance Premium'],
  ['policyLimit.amount', 'Policy Limit'],
  ['expirationDate', 'Expiration Date']
])

export default {
  name: 'StatusDescription',
  components: { InsuranceQuote, InsuranceQuoteRequest, FormattedDate },
  props: {
    date: String,
    status: String,
    insuranceQuoteRequest: Object,
    isCurrentStatus: Boolean,
    onUpdate: Function
  },
  data() {
    return {
      responseModalOpen: false,
      accept: true,
      expirationDate: '',
      insurancePremium: null,
      policyLimit: null,
      hasError: false,
      errorFields: [],
      errorMessages: []
    }
  },
  methods: {
    computeDefaultExpirationDate() {
      return moment()
        .add(1, 'months')
        .hours(23)
        .minutes(59)
        .seconds(0)
        .format('YYYY-MM-DDTHH:mm')
    },
    openResponseModal() {
      this.responseModalOpen = true
      this.accept = true
      this.expirationDate = this.computeDefaultExpirationDate()
      this.insurancePremium = null
      this.policyLimit = null
      this.hasError = false
      this.errorFields = []
      this.errorMessages = []
    },
    cancelResponseModal() {
      this.responseModalOpen = false
    },
    onAccept() {
      this.accept = true
    },
    onReject() {
      this.accept = false
      this.insurancePremium = null
      this.policyLimit = null
      this.hasError = false
      this.errorFields = []
      this.errorMessages = []
    },
    isValid() {
      if (this.accept) {
        const insurancePremium = parseFloat(this.insurancePremium)
        const policyLimit = parseFloat(this.policyLimit)
        return (
          this.expirationDate !== '' &&
          !isNaN(insurancePremium) &&
          !isNaN(policyLimit)
        )
      } else {
        return true
      }
    },
    async confirmResponseModal() {
      try {
        const expirationDate = this.accept
          ? moment(this.expirationDate).toDate()
          : null
        const insurancePremium = this.accept
          ? parseFloat(this.insurancePremium)
          : null
        const policyLimit = this.accept ? parseFloat(this.policyLimit) : null
        await respondToInsuranceQuoteRequest(
          this.insuranceQuoteRequest.id,
          this.accept,
          expirationDate,
          insurancePremium,
          policyLimit
        )
        this.responseModalOpen = false
        this.onUpdate()
      } catch (error) {
        const formError = error ? extractFormError(error, context) : null
        this.hasError = !!error
        this.errorFields = formError ? formError.errorFields : []
        this.errorMessages = formError ? formError.errorMessages : []
      }
    }
  }
}
</script>

<style scoped>
.radioButtons {
  margin-bottom: 20px;
}

.radioButtons input {
  margin-right: 5px;
}

.radioButtons label {
  margin-right: 20px;
}
</style>
