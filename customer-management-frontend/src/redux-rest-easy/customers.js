// @flow

import { createResource } from "@brigad/redux-rest-easy"
import { normalize, schema } from "normalizr"
import {
  customerManagementBackend,
  customerSelfServiceBackend,
} from "../config"

const customerSchema = new schema.Entity("customers", undefined, {
  idAttribute: "customerId",
})

const customers = createResource("customers")({
  retrieveCustomers: {
    method: "GET",
    url: `${customerManagementBackend}/customers`,
    normalizer: data => {
      const normalizedData = normalize(data, {
        customers: [customerSchema],
      })
      return normalizedData
    },
    metadataNormalizer: data => {
      return { offset: data.offset, limit: data.limit, size: data.size }
    },
  },
  retrieveCustomer: {
    method: "GET",
    url: `${customerManagementBackend}/customers/:customerId`,
    normalizer: data => normalize(data, customerSchema),
  },
  updateCustomer: {
    method: "PUT",
    url: `${customerManagementBackend}/customers/:customerId`,
    normalizer: data => {
      return normalize(data, customerSchema)
    },
    networkHelpers: {
      handleError: async (err, dispatch) => {
        // Overrides the default error handler so that we don't consume
        // the response body more than once.
      },
    },
  },
  createCustomer: {
    method: "POST",
    url: `${customerSelfServiceBackend}/customers`,
    normalizer: data => {
      return normalize(data, customerSchema)
    },
    networkHelpers: {
      handleError: async (err, dispatch) => {
        // Overrides the default error handler so that we don't consume
        // the response body more than once.
      },
      requestPOST(body) {
        return {
          method: "POST",
          headers: {
            Accept: "application/json",
            "X-Auth-Token": `${localStorage.getItem("token") || ""}`,
            "Content-Type": "application/json",
          },
          body: JSON.stringify(body),
        }
      },
    },
  },
})

const {
  actions: {
    resource: { reset: resetCustomers },
    retrieveCustomers: { perform: retrieveCustomers },
    retrieveCustomer: { perform: retrieveCustomer },
    updateCustomer: { perform: updateCustomer },
    createCustomer: { perform: createCustomer },
  },
  selectors: {
    resource: { getResource: getAllCustomers, getResourceById: getCustomer },
    retrieveCustomers: {
      request: {
        isPerforming: isRetrievingCustomers,
        getMetadata: getCustomersMetadata,
      },
    },
    retrieveCustomer: {
      request: { isPerforming: isRetrievingCustomer },
    },
    updateCustomer: {
      request: { isPerforming: isUpdatingCustomer },
    },
  },
} = customers

export {
  retrieveCustomer,
  isRetrievingCustomer,
  retrieveCustomers,
  isRetrievingCustomers,
  getCustomersMetadata,
  getAllCustomers,
  getCustomer,
  resetCustomers,
  updateCustomer,
  isUpdatingCustomer,
  createCustomer,
}
