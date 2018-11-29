// @flow

import { createResource } from "@brigad/redux-rest-easy"
import { normalize, schema } from "normalizr"
import { customerManagementBackend } from "../config"

const notificationSchema = new schema.Entity("notifications", undefined, {
  idAttribute: "customerId",
})

const notifications = createResource("notifications")({
  retrieveNotifications: {
    method: "GET",
    url: `${customerManagementBackend}/notifications`,
    normalizer: data => {
      return normalize(data, new schema.Array(notificationSchema))
    },
  },
})

const {
  actions: {
    resource: { reset: resetNotifications },
    retrieveNotifications: { perform: retrieveNotifications },
  },
  selectors: {
    resource: { getResource: getAllNotifications },
  },
} = notifications

export { retrieveNotifications, getAllNotifications, resetNotifications }
