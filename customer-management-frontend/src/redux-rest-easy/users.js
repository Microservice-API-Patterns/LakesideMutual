// @flow

import { createResource } from "@brigad/redux-rest-easy"
import { normalize, schema } from "normalizr"
import { customerSelfServiceBackend } from "../config"

const usersSchema = new schema.Entity("users", undefined, {
  idAttribute: "email",
})

const users = createResource("users")({
  signupUser: {
    method: "POST",
    url: `${customerSelfServiceBackend}/auth/signup`,
    normalizer: data => {
      return normalize(data, usersSchema)
    },
    networkHelpers: {
      handleError: async (err, dispatch) => {
        // Overrides the default error handler so that we don't consume
        // the response body more than once.
      },
    },
  },
  loginUser: {
    method: "POST",
    url: `${customerSelfServiceBackend}/auth`,
    normalizer: data => {
      return normalize(data, usersSchema)
    },
    networkHelpers: {
      handleError: async (err, dispatch) => {
        // Overrides the default error handler so that we don't consume
        // the response body more than once.
      },
    },
  },
})

const {
  actions: {
    signupUser: { perform: signupUser },
    loginUser: { perform: loginUser },
  },
} = users

export { signupUser, loginUser }
