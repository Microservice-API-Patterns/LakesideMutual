// @flow

import React, { Fragment } from "react"
import { Route, Redirect } from "react-router-dom"
import { Loader } from "semantic-ui-react"
import Overview from "./Overview"
import UpdateAddressForm from "./UpdateAddressForm"

export type Props = {
  actions: {
    clearErrors: () => void,
    changeAddress: (address: Address) => Promise<void>,
  },
  user: ?User,
  customer: ?Customer,
  isUpdatingAddress: boolean,
  addressUpdateError: ?FormSubmissionError,
}

export default class MyProfile extends React.Component<Props> {
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
        <Fragment>
          <Route
            exact
            path="/profile"
            render={(props) => <Overview customer={customer} />}
          />
          <Route
            exact
            path="/profile/change-address"
            render={(props) => (
              <UpdateAddressForm
                customer={customer}
                isUpdatingAddress={this.props.isUpdatingAddress}
                addressUpdateError={this.props.addressUpdateError}
                actions={this.props.actions}
              />
            )}
          />
        </Fragment>
      )
    }
  }
}
