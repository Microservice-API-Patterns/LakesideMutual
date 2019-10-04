<template>
  <div>
    <sui-modal v-model="confirmationModalOpen" size="tiny" :closable="true">
      <sui-modal-header>Delete Policy</sui-modal-header>
      <sui-modal-content>
        <p>Do you really want to delete this insurance policy? This action cannot be undone.</p>
      </sui-modal-content>
      <sui-modal-actions>
        <sui-button v-on:click="onCancelDeletion">Cancel</sui-button>
        <sui-button v-on:click="onConfirmDeletion" color="red">Delete</sui-button>
      </sui-modal-actions>
    </sui-modal>
    <sui-modal v-model="infoModalOpen">
      <sui-modal-header v-if="infoType === 'deductible'">Deductible</sui-modal-header>
      <sui-modal-header v-if="infoType === 'insurancePremium'">Insurance Premium</sui-modal-header>
      <sui-modal-header v-if="infoType === 'policyLimit'">Policy Limit</sui-modal-header>
      <sui-modal-content>
        <sui-modal-description>
          <p
            v-if="infoType === 'deductible'"
          >The deductible is the amount paid out of pocket by the policy holder (i.e., the customer) before an insurance provider (i.e., Lakeside Mutual) will pay any expenses.</p>
          <p
            v-if="infoType === 'insurancePremium'"
          >An insurance premium is the amount of money that the policy holder (i.e., the customer) must pay every month for an insurance policy.</p>
          <p
            v-if="infoType === 'policyLimit'"
          >A policy limit refers to the maximum monetary amount that an insurance provider (i.e., Lakeside Mutual) will pay out in relation to an insurance policy claim.</p>
        </sui-modal-description>
      </sui-modal-content>
      <sui-modal-actions>
        <sui-button positive v-on:click="hideInfoModal" class="modalButton">OK</sui-button>
      </sui-modal-actions>
    </sui-modal>
    <sui-grid padded>
      <sui-grid-row>
        <sui-grid-column :width="16">
          <sui-grid :columns="2" style="border: 2px solid #aaaaaa">
            <sui-grid-row>
              <sui-grid-column :width="8">
                <h3>{{policy.policyType}}</h3>
              </sui-grid-column>
              <sui-grid-column :width="8" textAlign="right">
                <sui-button-group size="tiny">
                  <router-link
                    is="sui-button"
                    :to="`/customers/${customer.customerId}/policies/${policy.policyId}/edit`"
                    icon="edit"
                    compact
                  >Edit</router-link>
                  <sui-button compact icon="trash" v-on:click="showConfirmationModal">Delete</sui-button>
                </sui-button-group>
              </sui-grid-column>
            </sui-grid-row>
            <sui-grid-row>
              <sui-grid-column :width="4">
                <b>Customer</b>
              </sui-grid-column>
              <sui-grid-column :width="12" verticalAlign="middle">
                <router-link
                  :to="`/customers/${customer.customerId}`"
                >{{customer.firstname}} {{customer.lastname}}</router-link>
              </sui-grid-column>
            </sui-grid-row>
            <sui-grid-row>
              <sui-grid-column :width="4">
                <b>Creation Date</b>
              </sui-grid-column>
              <sui-grid-column :width="12" verticalAlign="middle">
                <formatted-date :date="policy.creationDate" full/>
              </sui-grid-column>
            </sui-grid-row>
            <sui-grid-row>
              <sui-grid-column :width="4">
                <b>Period</b>
              </sui-grid-column>
              <sui-grid-column :width="12" verticalAlign="middle">
                <formatted-date :date="policy.policyPeriod.startDate"/>&nbsp;–
                <formatted-date :date="policy.policyPeriod.endDate"/>
              </sui-grid-column>
            </sui-grid-row>
            <sui-grid-row>
              <sui-grid-column :width="4">
                <b>Deductible&nbsp;</b>
                <sui-button
                  class="info-button"
                  circular
                  icon="info"
                  v-on:click="showDeductibleInfoModal"
                />
              </sui-grid-column>
              <sui-grid-column
                :width="12"
                verticalAlign="middle"
              >{{policy.deductible.amount}} {{policy.deductible.currency}}</sui-grid-column>
            </sui-grid-row>
            <sui-grid-row>
              <sui-grid-column :width="4">
                <b>Insurance Premium&nbsp;</b>
                <sui-button
                  class="info-button"
                  circular
                  icon="info"
                  v-on:click="showInsurancePremiumInfoModal"
                />
              </sui-grid-column>
              <sui-grid-column
                :width="12"
                verticalAlign="middle"
              >{{policy.insurancePremium.amount}} {{policy.insurancePremium.currency}}</sui-grid-column>
            </sui-grid-row>
            <sui-grid-row>
              <sui-grid-column :width="4">
                <b>Policy Limit&nbsp;</b>
                <sui-button
                  class="info-button"
                  circular
                  icon="info"
                  v-on:click="showPolicyLimitInfoModal"
                />
              </sui-grid-column>
              <sui-grid-column
                :width="12"
                verticalAlign="middle"
              >{{policy.policyLimit.amount}} {{policy.policyLimit.currency}}</sui-grid-column>
            </sui-grid-row>
            <sui-grid-row>
              <sui-grid-column :width="4">
                <b>Insuring Agreement</b>
              </sui-grid-column>
              <sui-grid-column :width="12" verticalAlign="middle">
                <sui-list divided relaxed>
                  <span v-if="policy.insuringAgreement.agreementItems.length === 0">N/A</span>
                  <sui-list-item
                    v-for="(agreementItem, index) in policy.insuringAgreement.agreementItems"
                    :key="index"
                  >
                    <sui-list-content>
                      <span is="sui-list-header">{{agreementItem.title}}</span>
                      <span is="sui-list-description">{{agreementItem.description}}</span>
                    </sui-list-content>
                  </sui-list-item>
                </sui-list>
              </sui-grid-column>
            </sui-grid-row>
          </sui-grid>
        </sui-grid-column>
      </sui-grid-row>
    </sui-grid>
  </div>
</template>

<script>
import { deletePolicy } from '../api'
import FormattedDate from '@/components/FormattedDate'

export default {
  name: 'Policy',
  components: { FormattedDate },
  props: ['policy', 'customer', 'onDelete'],
  data() {
    return {
      confirmationModalOpen: false,
      infoModalOpen: false,
      infoType: ''
    }
  },
  methods: {
    async removePolicy() {
      try {
        await deletePolicy(this.policy.policyId)
        this.onDelete()
      } catch (error) {
        console.log(error)
      }
    },
    showDeductibleInfoModal(evt) {
      evt.preventDefault()
      this.infoModalOpen = true
      this.infoType = 'deductible'
    },
    showInsurancePremiumInfoModal(evt) {
      evt.preventDefault()
      this.infoModalOpen = true
      this.infoType = 'insurancePremium'
    },
    showPolicyLimitInfoModal(evt) {
      evt.preventDefault()
      this.infoModalOpen = true
      this.infoType = 'policyLimit'
    },
    hideInfoModal(evt) {
      evt.preventDefault()
      this.infoModalOpen = false
    },
    showConfirmationModal(evt) {
      evt.preventDefault()
      this.confirmationModalOpen = true
    },
    onCancelDeletion(evt) {
      evt.preventDefault()
      this.confirmationModalOpen = false
    },
    async onConfirmDeletion(evt) {
      evt.preventDefault()
      await this.removePolicy()
      this.confirmationModalOpen = false
    }
  }
}
</script>

<style scoped>
.info-button {
  font-size: 11px !important;
  padding: 5px !important;
}
</style>
