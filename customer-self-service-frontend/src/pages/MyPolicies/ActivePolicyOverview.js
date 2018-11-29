// @flow

import React, { Fragment } from "react"
import { Loader, Message } from "semantic-ui-react"
import PolicyOverview from "./PolicyOverview"
import { ApiError } from "../../api/helpers"
import errorMessages from "../../errorMessages"

export type Props = {
  customer: Customer,
  policy: ?Policy,
  isFetchingActivePolicy: boolean,
  policyFetchError: ?Error,
  actions: {
    fetchActivePolicy: (customerId: CustomerId) => Promise<void>,
  },
}

export default class extends React.Component<Props> {
  constructor(props: Props) {
    super(props)

    if (
      props.policy == null &&
      !props.isFetchingActivePolicy &&
      props.policyFetchError == null
    ) {
      props.actions.fetchActivePolicy(props.customer.customerId)
    }
  }

  render() {
    const { policy, isFetchingActivePolicy, policyFetchError } = this.props

    if (isFetchingActivePolicy) {
      return <Loader active />
    } else if (policyFetchError) {
      if (policyFetchError instanceof ApiError) {
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
    } else if (policy) {
      return <PolicyOverview policy={policy} />
    } else {
      return null
    }
  }
}
