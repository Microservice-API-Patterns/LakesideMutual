const policyManagementBackend =
  window.__ENV ? window.__ENV.VUE_APP_POLICY_MANAGEMENT_BACKEND :
    process.env.VUE_APP_POLICY_MANAGEMENT_BACKEND

export async function getCustomers(link, filter) {
  if (link) {
    return getJson(link)
  } else {
    return getJson(
      `${policyManagementBackend}/customers?filter=${filter || ''}`
    )
  }
}

export async function getCustomer(customerId) {
  return getJson(`${policyManagementBackend}/customers/${customerId}`)
}

export async function getInsuranceQuoteRequests() {
  return getJson(`${policyManagementBackend}/insurance-quote-requests`)
}

export async function getInsuranceQuoteRequest(insuranceQuoteRequestId) {
  return getJson(
    `${policyManagementBackend}/insurance-quote-requests/${insuranceQuoteRequestId}`
  )
}

export async function respondToInsuranceQuoteRequest(
  insuranceQuoteRequestId,
  accept,
  expirationDate,
  insurancePremium,
  policyLimit
) {
  let body
  if (accept) {
    body = {
      status: 'QUOTE_RECEIVED',
      expirationDate,
      insurancePremium: {
        amount: insurancePremium,
        currency: 'CHF'
      },
      policyLimit: {
        amount: policyLimit,
        currency: 'CHF'
      }
    }
  } else {
    body = { status: 'REQUEST_REJECTED' }
  }
  return patchJson(
    `${policyManagementBackend}/insurance-quote-requests/${insuranceQuoteRequestId}`,
    body
  )
}

export async function getPolicies(link) {
  if (link) {
    return getJson(link)
  } else {
    return getJson(`${policyManagementBackend}/policies?expand=customer`)
  }
}

export async function getPolicy(policyId, expandCustomer = false) {
  const policy = await getJson(
    `${policyManagementBackend}/policies/${policyId}${
      expandCustomer ? '?expand=customer' : ''
    }`
  )
  return policy
}

export function getCustomerPolicies(customerId) {
  return getJson(`${policyManagementBackend}/customers/${customerId}/policies`)
}

export async function createPolicy(
  customerId,
  startDate,
  endDate,
  policyType,
  deductible,
  insurancePremium,
  policyLimit,
  agreementItems
) {
  const policy = {
    customerId,
    policyPeriod: {
      startDate,
      endDate
    },
    policyType,
    policyLimit: {
      amount: policyLimit,
      currency: 'CHF'
    },
    deductible: {
      amount: deductible,
      currency: 'CHF'
    },
    insurancePremium: {
      amount: insurancePremium,
      currency: 'CHF'
    },
    insuringAgreement: {
      agreementItems
    }
  }

  return postJson(`${policyManagementBackend}/policies`, policy)
}

export async function updatePolicy(
  policyId,
  customerId,
  startDate,
  endDate,
  policyType,
  deductible,
  insurancePremium,
  policyLimit,
  agreementItems
) {
  const policy = {
    customerId,
    policyPeriod: {
      startDate,
      endDate
    },
    policyType,
    deductible: {
      amount: deductible,
      currency: 'CHF'
    },
    policyLimit: {
      amount: policyLimit,
      currency: 'CHF'
    },
    insurancePremium: {
      amount: insurancePremium,
      currency: 'CHF'
    },
    insuringAgreement: {
      agreementItems
    }
  }

  return putJson(`${policyManagementBackend}/policies/${policyId}`, policy)
}

export async function deletePolicy(policyId) {
  const url = `${policyManagementBackend}/policies/${policyId}`
  await fetch(url, {
    method: 'DELETE'
  })
}

export async function computeRiskFactor(customer) {
  const request = {
    birthday: customer.birthday,
    postalCode: customer.postalCode
  }
  return postJson(`${policyManagementBackend}/riskfactor/compute`, request)
}

async function getJson(url) {
  const response = await fetch(url, {
    method: 'GET',
    headers: {
      Accept: 'application/json'
    }
  })
  return checkStatus(response)
}

async function postJson(url, body) {
  return submitJson(url, 'POST', body)
}

async function putJson(url, body) {
  return submitJson(url, 'PUT', body)
}

async function patchJson(url, body) {
  return submitJson(url, 'PATCH', body)
}

async function submitJson(url, method, body) {
  const response = await fetch(url, {
    method,
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json'
    },
    body: JSON.stringify(body)
  })
  return checkStatus(response)
}

export class ApiError {
  constructor(code, statusText, body) {
    this.code = code
    this.statusText = statusText
    this.body = body
  }
}

export async function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response.json()
  } else {
    let json
    try {
      json = await response.json()
    } catch (_) {
      throw new ApiError(response.status, response.statusText, '')
    }
    throw new ApiError(response.status, response.statusText, json)
  }
}
