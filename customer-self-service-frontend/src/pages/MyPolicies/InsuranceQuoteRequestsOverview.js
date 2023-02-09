// @flow

import React, { Fragment } from "react"
import { Link, Redirect } from "react-router-dom"
import {
  Loader,
  Message,
  Button,
  Table,
  Icon,
  Header,
  Form,
} from "semantic-ui-react"
import moment from "moment"
import errorMessages from "../../errorMessages"

export type Props = {
  customer: Customer,
  insuranceQuoteRequests: ?[InsuranceQuoteRequest],
  isFetchingInsuranceQuoteRequests: boolean,
  insuranceQuoteRequestFetchError: ?Error,
  actions: {
    fetchInsuranceQuoteRequests: (customerId: CustomerId) => Promise<void>,
  },
}

type State = {
  showArchivedInsuranceQuoteRequests: boolean,
  redirectLink: ?string,
}

export default class InsuranceQuoteRequestsOverview extends React.Component<
  Props,
  State
> {
  state = {
    showArchivedInsuranceQuoteRequests: false,
    redirectLink: null,
  }

  constructor(props: Props) {
    super(props)

    if (!props.isFetchingInsuranceQuoteRequests) {
      props.actions.fetchInsuranceQuoteRequests(props.customer.customerId)
    }
  }

  renderStatus(insuranceQuoteRequest: InsuranceQuoteRequest) {
    const status =
      insuranceQuoteRequest.statusHistory[
        insuranceQuoteRequest.statusHistory.length - 1
      ].status
    if (status === "REQUEST_SUBMITTED") {
      return <i>Waiting for Quote</i>
    } else if (status === "REQUEST_REJECTED") {
      return <i style={{ color: "red" }}>Request was rejected</i>
    } else if (status === "QUOTE_RECEIVED") {
      return <i>Insurance Quote available</i>
    } else if (status === "QUOTE_ACCEPTED") {
      return <i style={{ color: "green" }}>Quote accepted</i>
    } else if (status === "POLICY_CREATED") {
      return <i style={{ color: "green" }}>Policy created</i>
    } else if (status === "QUOTE_REJECTED") {
      return <i style={{ color: "red" }}>Quote rejected</i>
    } else if (status === "QUOTE_EXPIRED") {
      return <i style={{ color: "red" }}>Quote expired</i>
    }
  }

  renderInsuranceQuoteRequests() {
    const {
      insuranceQuoteRequests,
      isFetchingInsuranceQuoteRequests,
      insuranceQuoteRequestFetchError,
    } = this.props

    const { showArchivedInsuranceQuoteRequests } = this.state

    if (isFetchingInsuranceQuoteRequests) {
      return <Loader active />
    } else if (insuranceQuoteRequestFetchError) {
      return (
        <Message
          error
          header="Error"
          content={errorMessages.customerSelfServiceBackendNotAvailable}
        />
      )
    } else if (insuranceQuoteRequests && insuranceQuoteRequests.length === 0) {
      return <p>You haven't submitted any insurance quote requests yet.</p>
    } else if (insuranceQuoteRequests && insuranceQuoteRequests.length > 0) {
      const filteredInsuranceQuoteRequests = showArchivedInsuranceQuoteRequests
        ? insuranceQuoteRequests
        : insuranceQuoteRequests.filter((request) => {
            const status =
              request.statusHistory[request.statusHistory.length - 1].status
            return (
              status !== "REQUEST_REJECTED" &&
              status !== "POLICY_CREATED" &&
              status !== "QUOTE_REJECTED" &&
              status !== "QUOTE_EXPIRED"
            )
          })

      return (
        <Fragment>
          <Form.Checkbox
            label="Show archived Insurance Quote Requests"
            checked={showArchivedInsuranceQuoteRequests}
            onChange={(event, data) =>
              this.setState({
                showArchivedInsuranceQuoteRequests: data.checked,
              })
            }
          />
          {filteredInsuranceQuoteRequests.length === 0 && (
            <Fragment>
              <br />
              <p>There are currently no open insurance quote requests.</p>
            </Fragment>
          )}
          {filteredInsuranceQuoteRequests.length > 0 && (
            <Table selectable>
              <Table.Body>
                {filteredInsuranceQuoteRequests.map(
                  (insuranceQuoteRequest, index) => (
                    <Table.Row
                      key={index}
                      style={{ cursor: "pointer" }}
                      onClick={() =>
                        this.setState({
                          redirectLink: `/policies/insurance-quote-requests/${
                            insuranceQuoteRequest.id || ""
                          }`,
                        })
                      }
                    >
                      <Table.Cell collapsing>
                        {moment(insuranceQuoteRequest.date).format(
                          "DD. MMM YYYY"
                        )}
                      </Table.Cell>
                      <Table.Cell>
                        <b>
                          {insuranceQuoteRequest.insuranceOptions.insuranceType}
                        </b>
                      </Table.Cell>
                      <Table.Cell collapsing textAlign="right">
                        {this.renderStatus(insuranceQuoteRequest)}
                      </Table.Cell>
                    </Table.Row>
                  )
                )}
              </Table.Body>
            </Table>
          )}
        </Fragment>
      )
    } else {
      return null
    }
  }

  render() {
    if (this.state.redirectLink != null) {
      return <Redirect to={this.state.redirectLink} />
    }
    return (
      <Fragment>
        <Header as="h3">
          Insurance Quote Requests
          <Button
            as={Link}
            to="/policies/new-insurance-quote-request"
            size="tiny"
            color="green"
            compact
            floated="right"
          >
            <Icon name="plus" />
            New Insurance Quote Request
          </Button>
        </Header>
        {this.renderInsuranceQuoteRequests()}
      </Fragment>
    )
  }
}
