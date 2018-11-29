import Vue from 'vue'
import Router from 'vue-router'
import Policies from '@/components/Policies'
import PolicyDetail from '@/components/PolicyDetail'
import Customers from '@/components/Customers'
import CustomerDetail from '@/components/CustomerDetail'
import CustomerPolicyDetail from '@/components/CustomerPolicyDetail'
import EditPolicy from '@/components/EditPolicy'

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
      path: '/customers/:customerid/edit-policy',
      name: 'EditPolicy',
      component: EditPolicy
    },
    {
      path: '/customers/:customerid/:policyid',
      name: 'CustomerPolicyDetail',
      component: CustomerPolicyDetail
    }
  ]
})
