import Vue from 'vue'
import Router from 'vue-router'
import Policies from '@/components/Policies'
import PolicyDetail from '@/components/PolicyDetail'
import InsuranceQuoteRequests from '@/components/InsuranceQuoteRequests'
import InsuranceQuoteRequestDetail from '@/components/InsuranceQuoteRequestDetail'
import Customers from '@/components/Customers'
import CustomerDetail from '@/components/CustomerDetail'
import EditPolicy from '@/components/EditPolicy'
import CreatePolicy from '@/components/CreatePolicy'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'Policies',
      component: Policies
    },
    {
      path: '/policies/:policyid',
      name: 'PolicyDetail',
      component: PolicyDetail
    },
    {
      path: '/insurance-quote-requests',
      name: 'InsuranceQuoteRequests',
      component: InsuranceQuoteRequests
    },
    {
      path: '/insurance-quote-requests/:requestid',
      name: 'InsuranceQuoteRequestDetail',
      component: InsuranceQuoteRequestDetail
    },
    {
      path: '/customers',
      name: 'Customers',
      component: Customers
    },
    {
      path: '/customers/:customerid',
      name: 'CustomerDetail',
      component: CustomerDetail
    },
    {
      path: '/customers/:customerid/policies/:policyid/edit',
      name: 'EditPolicy',
      component: EditPolicy
    },
    {
      path: '/customers/:customerid/policies/new',
      name: 'CreatePolicy',
      component: CreatePolicy
    }
  ]
})
