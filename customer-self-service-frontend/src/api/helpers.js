/**
 * This module contains helper functions to get and post JSON data to the
 * backend.
 *
 * @module api/helpers
 * @flow
 */

/**
 * Sends an authentiacted JSON-GET request
 * @param {string} url of the API
 * @param {string} token
 */
export async function getAuthenticatedJson<T>(
  url: string,
  token: string
): Promise<T> {
  const response = await fetch(url, {
    method: "GET",
    headers: {
      "X-Auth-Token": token,
      Accept: "application/json",
    },
  })
  return checkStatus(response)
}

/**
 * Sends a JSON-POST request with the given payload to the url
 * @param {string} url of the API
 * @param {T} params to be sent in the request body.
 */
export async function postJson<T, U>(url: string, params: T): Promise<U> {
  const response = await fetch(url, {
    method: "POST",
    headers: {
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify(params),
  })
  return checkStatus(response)
}

/**
 * Sends an authentiacted JSON-POST request
 * @param {string} url of the API
 * @param {string} token
 * @param {T} params to be sent in the request body.
 */
export async function postAuthenticatedJson<T, U>(
  url: string,
  token: string,
  params: T
): Promise<U> {
  const response = await fetch(url, {
    method: "POST",
    headers: {
      "X-Auth-Token": token,
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify(params),
  })
  return checkStatus(response)
}

/**
 * Sends an authentiacted JSON-PATCH request
 * @param {string} url of the API
 * @param {string} token
 * @param {T} params to be sent in the request body.
 */
export async function patchAuthenticatedJson<T, U>(
  url: string,
  token: string,
  params: T
): Promise<U> {
  const response = await fetch(url, {
    method: "PATCH",
    headers: {
      "X-Auth-Token": token,
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify(params),
  })
  return checkStatus(response)
}

/**
 * Sends an authentiacted JSON-PUT request
 * @param {string} url of the API
 * @param {string} token
 * @param {T} params to be sent in the request body.
 */
export async function putAuthenticatedJson<T, U>(
  url: string,
  token: string,
  params: T
): Promise<U> {
  const response = await fetch(url, {
    method: "PUT",
    headers: {
      "X-Auth-Token": token,
      "Content-Type": "application/json",
      Accept: "application/json",
    },
    body: JSON.stringify(params),
  })
  return checkStatus(response)
}

/**
 * Represents an API error with a status code, textual status and a generic
 * body.
 */
export class ApiError<T> {
  code: number
  statusText: string
  body: T

  constructor(code: number, statusText: string, body: T) {
    this.code = code
    this.statusText = statusText
    this.body = body
  }
}

/**
 * Checks the status of a HTTP response and returns the body as json. If the
 * request was not successful, throws a new ApiError instance.
 * @param {Response} response
 */
export async function checkStatus<T>(response: Response): Promise<T> {
  if (response.status >= 200 && response.status < 300) {
    return response.json()
  } else {
    let json
    try {
      json = await response.json()
    } catch (_) {
      throw new ApiError(response.status, response.statusText, "")
    }
    throw new ApiError(response.status, response.statusText, json)
  }
}
