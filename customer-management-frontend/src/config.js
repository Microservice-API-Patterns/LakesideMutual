// @flow

function getEnvironmentVariable(name: string, defaultValue: string): string {
  return process.env[name] != null ? process.env[name] : defaultValue
}

export const customerSelfServiceBackend = getEnvironmentVariable(
  "REACT_APP_CUSTOMER_SELF_SERVICE_BACKEND",
  "http://localhost:8080"
)
export const customerManagementBackend = getEnvironmentVariable(
  "REACT_APP_CUSTOMER_MANAGEMENT_BACKEND",
  "http://localhost:8100"
)
