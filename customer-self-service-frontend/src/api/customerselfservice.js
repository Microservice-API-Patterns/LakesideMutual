/**
 * This module exports functions to call the Customer Self-Service backend API
 *
 * @module api/customerselfservice
 * @flow
 */
import { customerSelfServiceBackend as backend } from "../config"
import {
  postJson,
  getAuthenticatedJson,
  postAuthenticatedJson,
  putAuthenticatedJson,
} from "./helpers"

/** Builds the URL to the backend API */
function urlForEndpoint(endpoint: string): string {
  return `${backend}${endpoint}`
}

/**
 * Logs in a user with the provided credentials.
 * @param {string} email
 * @param {string} password
 */
export function login(
  email: string,
  password: string
): Promise<{ token: string }> {
  const url = urlForEndpoint("/auth")
  return postJson(url, { email, password })
}
/**
 * Signs up a new user with the provided credentials.
 * @param {*string} email
 * @param {*string} password
 */
export function signup(email: string, password: string): Promise<User> {
  const url = urlForEndpoint("/auth/signup")
  return postJson(url, { email, password })
}

/**
 * Returns the currently logged in user.
 * @param {string} token
 */
export function getUserDetails(token: string): Promise<User> {
  const url = urlForEndpoint("/user")
  return getAuthenticatedJson(url, token)
}

/**
 * Returns the customer with the given Id.
 * @param {string} token
 * @param {CustomerId} customerId
 */
export function getCustomer(
  token: string,
  customerId: CustomerId
): Promise<Customer> {
  const url = urlForEndpoint(`/customers/${customerId}`)
  return getAuthenticatedJson(url, token)
}

/**
 * After signing up, customers have to provide additional information about
 * themselves. A signup is only complete when this method has been called.
 * @param {string} token
 * @param {*} data
 */
export function completeRegistration<T>(
  token: string,
  data: T
): Promise<Customer> {
  const url = urlForEndpoint("/customers")
  return postAuthenticatedJson(url, token, data)
}

/**
 * Changes the current address of the given customer.
 * @param {string} token
 * @param {Customer} customer
 * @param {Adress} address
 */
export function changeAddress(
  token: string,
  customer: Customer,
  address: Address
): Promise<Address> {
  // Instead of assembling the URL to change the address ourselves,
  // we use the one provided in the customer response.
  const url = customer._links["address.change"].href
  return putAuthenticatedJson(url, token, address)
}

/**
 * Fetches a list of city suggestions for the given postal code.
 * @param {string} token
 * @param {string} postalCode
 */
export function lookupCitiySuggestions(
  token: string,
  postalCode: string
): Promise<CitySuggestions> {
  const url = urlForEndpoint(`/cities/${postalCode}`)
  return getAuthenticatedJson(url, token)
}
