import {
  customerSelfServiceBackend,
  policyManagementBackend,
  customerManagementBackend,
} from "./config"

const errorMessages = {
  invalidLoginCredentials: "Invalid email address or password",
  customerSelfServiceBackendNotAvailable: `The Customer Self-Service backend ${customerSelfServiceBackend} is currently not available. Please follow the instructions in the corresponding README to ensure that it is running and listening on the right port.`,
  policyManagementBackendNotAvailable: `The Policy Management backend ${policyManagementBackend} is currently not available.  Please follow the instructions in the corresponding README to ensure that it is running and listening on the right port.`,
  customerManagementBackendNotAvailable: `The Customer Management backend ${customerManagementBackend} is currently not available. Please follow the instructions in the corresponding README to ensure that it is running and listening on the right port.`,
  emailAddressAlreadyRegistered: "This email address is already registered.",
  noPolicyAvailable: "No policy available.",
}

export default errorMessages
