// @flow

import React from "react"
import { Redirect } from "react-router-dom"
import { Loader } from "semantic-ui-react"
import CompleteProfileForm from "./CompleteProfileForm"

export type Props = {
  user: ?User,
  customer: ?Customer,
  isCompletingRegistration: boolean,
  registrationError: ?Error,
  actions: {
    clearErrors: () => void,
    completeRegistration: (data: CompleteProfileFormData) => Promise<void>,
    lookupCitiySuggestions: (postalCode: string) => Promise<CitySuggestions>,
  },
}

export default class CompleteRegistration extends React.Component<Props> {
  render() {
    const { user, customer } = this.props

    const isLoadingUser = user == null
    const isLoadingCustomer = !!(
      user &&
      user.customerId != null &&
      customer == null
    )

    if (isLoadingUser || isLoadingCustomer) {
      return <Loader active />
    } else if (user != null && user.customerId == null && customer == null) {
      return <CompleteProfileForm {...this.props} />
    } else {
      return <Redirect to={"/policies"} />
    }
  }
}
