// @flow

import * as React from "react"
import { Redirect } from "react-router-dom"
import { Loader } from "semantic-ui-react"
import Chat from "./Chat"

export type Props = {
  user: ?User,
  customer: ?Customer,
  interactionLog: ?InteractionLog,
  isFetchingInteractionLog: boolean,
  interactionLogFetchError: ?Error,
  actions: {
    fetchInteractionLog: (customerId: CustomerId) => Promise<void>,
  },
}

export default class Contact extends React.Component<Props> {
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
      return <Redirect to={"/complete-registration"} />
    } else if (customer != null) {
      return (
        <Chat
          customer={customer}
          interactionLog={this.props.interactionLog}
          isFetchingInteractionLog={this.props.isFetchingInteractionLog}
          interactionLogFetchError={this.props.interactionLogFetchError}
          actions={this.props.actions}
        />
      )
    }
  }
}
