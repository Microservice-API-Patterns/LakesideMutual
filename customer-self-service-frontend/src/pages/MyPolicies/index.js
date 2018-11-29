// @flow

import React, { Fragment } from "react"
import { Route, Redirect } from "react-router-dom"
import { Loader, Segment } from "semantic-ui-react"
import ActivePolicyOverview from "./ActivePolicyOverview"

export type Props = {
  user: ?User,
  customer: ?Customer,
  policy: ?Policy,
  isFetchingActivePolicy: boolean,
  policyFetchError: ?Error,
  actions: {
    fetchActivePolicy: (customerId: CustomerId) => Promise<void>,
  },
}

export default class extends React.Component<Props> {
  render() {
    const {
      user,
      customer,
      policy,
      isFetchingActivePolicy,
      policyFetchError,
    } = this.props

    const isLoadingUser = user == null
    const isLoadingCustomer = !!(
      user &&
      user.customerId != null &&
      customer == null
    )

    if (isLoadingUser || isLoadingCustomer || isFetchingActivePolicy) {
      return <Loader active />
    } else if (user != null && user.customerId == null && customer == null) {
      return <Redirect to={"/complete-registration"} />
    } else if (customer != null) {
      return (
        <Fragment>
          <Route
            exact
            path="/policies"
            render={props => (
              <Segment>
                <ActivePolicyOverview
                  customer={customer}
                  policy={policy}
                  isFetchingActivePolicy={isFetchingActivePolicy}
                  policyFetchError={policyFetchError}
                  actions={this.props.actions}
                />
              </Segment>
            )}
          />
        </Fragment>
      )
    }
  }
}
