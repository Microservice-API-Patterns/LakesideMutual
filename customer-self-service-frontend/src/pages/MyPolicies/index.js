// @flow

import React, { Fragment } from "react"
import { Route, Redirect } from "react-router-dom"
import { Loader, Segment } from "semantic-ui-react"
import PoliciesOverview from "./PoliciesOverview"
import RequestInsuranceQuote from "./RequestInsuranceQuote"
import InsuranceQuoteRequestsOverview from "./InsuranceQuoteRequestsOverview"
import InsuranceQuoteRequestDetail from "./InsuranceQuoteRequestDetail"

export type Props = {
  user: ?User,
  customer: ?Customer,
  policies: ?[Policy],
  isFetchingPolicies: boolean,
  policiesFetchError: ?Error,
  isCreatingInsuranceQuoteRequest: boolean,
  insuranceQuoteRequestCreationError: ?Error,
  insuranceQuoteRequest: ?InsuranceQuoteRequest,
  isFetchingInsuranceQuoteRequest: boolean,
  insuranceQuoteRequests: ?[InsuranceQuoteRequest],
  isFetchingInsuranceQuoteRequests: boolean,
  insuranceQuoteRequestFetchError: ?Error,
  isRespondingToInsuranceQuote: boolean,
  insuranceQuoteResponseError: ?Error,
  actions: {
    fetchPolicies: (customerId: CustomerId) => Promise<void>,
    fetchInsuranceQuoteRequests: (customerId: CustomerId) => Promise<void>,
    createInsuranceQuoteRequest: (InsuranceQuoteRequest) => Promise<void>,
    resetInsuranceQuoteRequestCreationError: () => void,
    fetchInsuranceQuoteRequest: (id: string) => Promise<void>,
    respondToInsuranceQuote: (
      insuranceQuoteRequestId: string,
      accepted: boolean
    ) => Promise<void>,
  },
}

export default class MyPolicies extends React.Component<Props> {
  render() {
    const {
      user,
      customer,
      policies,
      isFetchingPolicies,
      policiesFetchError,
      isCreatingInsuranceQuoteRequest,
      insuranceQuoteRequestCreationError,
      insuranceQuoteRequest,
      isFetchingInsuranceQuoteRequest,
      insuranceQuoteRequests,
      isFetchingInsuranceQuoteRequests,
      insuranceQuoteRequestFetchError,
      isRespondingToInsuranceQuote,
      insuranceQuoteResponseError,
    } = this.props

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
            path="/policies"
            render={(props) => (
              <Segment>
                <InsuranceQuoteRequestsOverview
                  customer={customer}
                  insuranceQuoteRequests={insuranceQuoteRequests}
                  isFetchingInsuranceQuoteRequests={
                    isFetchingInsuranceQuoteRequests
                  }
                  insuranceQuoteRequestFetchError={
                    insuranceQuoteRequestFetchError
                  }
                  actions={this.props.actions}
                />
                <PoliciesOverview
                  customer={customer}
                  policies={policies}
                  isFetchingPolicies={isFetchingPolicies}
                  policiesFetchError={policiesFetchError}
                  actions={this.props.actions}
                />
              </Segment>
            )}
          />
          <Route
            exact
            path="/policies/new-insurance-quote-request"
            render={(props) => (
              <RequestInsuranceQuote
                customer={customer}
                isCreatingInsuranceQuoteRequest={
                  isCreatingInsuranceQuoteRequest
                }
                insuranceQuoteRequestCreationError={
                  insuranceQuoteRequestCreationError
                }
                actions={this.props.actions}
              />
            )}
          />
          <Route
            exact
            path="/policies/insurance-quote-requests/:id"
            render={(props) => (
              <InsuranceQuoteRequestDetail
                insuranceQuoteRequest={insuranceQuoteRequest}
                isFetchingInsuranceQuoteRequest={
                  isFetchingInsuranceQuoteRequest
                }
                insuranceQuoteRequestFetchError={
                  insuranceQuoteRequestFetchError
                }
                isRespondingToInsuranceQuote={isRespondingToInsuranceQuote}
                insuranceQuoteResponseError={insuranceQuoteResponseError}
                actions={this.props.actions}
              />
            )}
          />
        </Fragment>
      )
    }
  }
}
