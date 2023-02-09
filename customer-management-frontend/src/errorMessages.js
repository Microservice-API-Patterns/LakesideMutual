import { customerManagementBackend } from "./config"

const errorMessages = {
  customerManagementBackendNotAvailable: `The Customer Management backend ${customerManagementBackend} is urrently not available. Please follow the instructions in the corresponding README to ensure that it is running and listening on the right port.`,
}

export default errorMessages
