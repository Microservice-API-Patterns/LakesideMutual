/**
 * This module exports functions to call the Policy Management backend API
 *
 * @module api/policymanagement
 * @flow
 */
import { checkStatus } from "./helpers"
import { policyManagementBackend as backend } from "../config"

export async function getPolicies(customerId: CustomerId): Promise<[Policy]> {
  return getJson(`/customers/${customerId}/policies`)
}

async function getJson<U>(endpoint: string): Promise<U> {
  const response = await fetch(`${backend}${endpoint}`, {
    method: "GET",
    headers: {
      Accept: "application/json",
    },
  })
  return checkStatus(response)
}
