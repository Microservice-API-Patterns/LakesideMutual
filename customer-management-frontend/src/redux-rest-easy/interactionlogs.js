// @flow

import { createResource } from "@brigad/redux-rest-easy"
import { normalize, schema } from "normalizr"
import { customerManagementBackend } from "../config"

const interactionLogSchema = new schema.Entity("interactionlogs", undefined, {
  idAttribute: "customerId",
})

const interactionlogs = createResource("interactionlogs")({
  retrieveInteractionLog: {
    method: "GET",
    url: `${customerManagementBackend}/interaction-logs/:customerId`,
    normalizer: data => normalize(data, interactionLogSchema),
  },
  acknowledgeInteractions: {
    method: "PATCH",
    url: `${customerManagementBackend}/interaction-logs/:customerId`,
    normalizer: data => normalize(data, interactionLogSchema),
  },
})

const {
  actions: {
    resource: { reset: resetInteractionLogs },
    retrieveInteractionLog: { perform: retrieveInteractionLog },
    acknowledgeInteractions: { perform: acknowledgeInteractions },
  },
  selectors: {
    resource: { getResourceById: getInteractionLog },
    retrieveInteractionLog: {
      request: { isPerforming: isRetrievingInteractionLog },
    },
  },
} = interactionlogs

export {
  retrieveInteractionLog,
  isRetrievingInteractionLog,
  getInteractionLog,
  resetInteractionLogs,
  acknowledgeInteractions,
}
