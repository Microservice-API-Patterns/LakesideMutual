import env from "@beam-australia/react-env"

export const customerSelfServiceBackend =
  env("CUSTOMER_SELF_SERVICE_BACKEND") || ""

export const customerManagementBackend =
  env("CUSTOMER_MANAGEMENT_BACKEND") || ""
