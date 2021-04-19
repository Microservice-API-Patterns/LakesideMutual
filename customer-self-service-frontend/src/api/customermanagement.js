/**
 * This module exports functions to call the Customer Management backend API
 *
 * @module api/customermanagement
 * @flow
 */
import { checkStatus } from "./helpers"
import { customerManagementBackend as backend } from "../config"

export async function getInteractionLog(
  customerId: CustomerId
): Promise<InteractionLog> {
  return getJson(`/interaction-logs/${customerId}`)
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
