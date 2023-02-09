// @flow

import React, { Fragment } from "react"
import { Loader, Message, Header } from "semantic-ui-react"
import PolicyOverview from "./PolicyOverview"
import { ApiError } from "../../api/helpers"
import errorMessages from "../../errorMessages"

export type Props = {
  customer: Customer,
  policies: ?[Policy],
  isFetchingPolicies: boolean,
  policiesFetchError: ?Error,
  actions: {
    fetchPolicies: (customerId: CustomerId) => Promise<void>,
  },
}

export default class PoliciesOverview extends React.Component<Props> {
  componentDidMount() {
    if (
      !this.props.isFetchingPolicies &&
      this.props.policiesFetchError == null
    ) {
      this.props.actions.fetchPolicies(this.props.customer.customerId)
    }
  }

  renderPolicies() {
    const { policies, isFetchingPolicies, policiesFetchError } = this.props

    if (isFetchingPolicies) {
      return <Loader active />
    } else if (policiesFetchError) {
      if (policiesFetchError instanceof ApiError) {
        return (
          <Fragment>
            <p>{errorMessages.noPolicyAvailable}</p>
            <Message
              icon="info circle"
              info
              content="Use the Policy Management frontend to create a new insurance policy for this customer."
              size="small"
            />
          </Fragment>
        )
      } else {
        return (
          <Message
            error
            header="Error"
            content={errorMessages.policyManagementBackendNotAvailable}
          />
        )
      }
    } else if (policies && policies.length > 0) {
      return (
        <Fragment>
          {policies.map((policy) => (
            <Fragment key={policy.policyId}>
              <PolicyOverview policy={policy} />
              <br />
            </Fragment>
          ))}
        </Fragment>
      )
    } else if (policies && policies.length === 0) {
      return <p>You don't have any policies yet.</p>
    } else {
      return null
    }
  }

  render() {
    return (
      <Fragment>
        <Header as="h3">Policies</Header>
        {this.renderPolicies()}
      </Fragment>
    )
  }
}
