// @flow

import React, { Fragment } from "react"
import { Link, withRouter, type Match } from "react-router-dom"
import {
  Breadcrumb,
  Segment,
  Header,
  Table,
  Grid,
  Button,
  Message,
} from "semantic-ui-react"
import moment from "moment"

export type Props = {
  insuranceQuoteRequest: ?InsuranceQuoteRequest,
  isFetchingInsuranceQuoteRequest: boolean,
  insuranceQuoteRequestFetchError: ?Error,
  isRespondingToInsuranceQuote: boolean,
  insuranceQuoteResponseError: ?Error,
  actions: {
    fetchInsuranceQuoteRequest: (id: string) => Promise<void>,
    respondToInsuranceQuote: (
      insuranceQuoteRequestId: string,
      accepted: boolean
    ) => Promise<void>,
  },
  match: Match,
}

class InsuranceQuoteRequestDetail extends React.Component<Props> {
  constructor(props: Props) {
    super(props)

    const id = props.match.params.id
    if (!props.isFetchingInsuranceQuoteRequest && id != null) {
      props.actions.fetchInsuranceQuoteRequest(id)
    }
  }

  formatDate(date: ?string): string {
    return moment(date).format("DD. MMM YYYY")
  }

  formatDatetime(date: ?string): string {
    return moment(date).format("DD. MMM YYYY HH:mm")
  }

  async respondToInsuranceQuote(
    insuranceQuoteRequest: InsuranceQuoteRequest,
    accepted: boolean
  ) {
    const { actions } = this.props
    await actions.respondToInsuranceQuote(
      insuranceQuoteRequest.id || "",
      accepted
    )
    await actions.fetchInsuranceQuoteRequest(insuranceQuoteRequest.id || "")
  }

  renderInsuranceQuoteRequestDetails(
    insuranceQuoteRequest: InsuranceQuoteRequest
  ) {
    const { customerInfo, insuranceOptions } = insuranceQuoteRequest
    return (
      <Grid padded>
        <Grid.Row>
          <Grid.Column>
            <Grid columns={2} style={{ border: "1px solid #aaaaaa" }}>
              <Grid.Row>
                <Grid.Column>
                  <Header as="h4">Personal Data</Header>
                  <Table basic collapsing celled>
                    <Table.Header>
                      <Table.Row>
                        <Table.HeaderCell>Contact Address</Table.HeaderCell>
                        <Table.HeaderCell>Billing Address</Table.HeaderCell>
                      </Table.Row>
                    </Table.Header>
                    <Table.Body>
                      <Table.Row>
                        <Table.Cell>
                          {customerInfo.contactAddress.streetAddress}
                          <br />
                          {customerInfo.contactAddress.postalCode}{" "}
                          {customerInfo.contactAddress.city}
                        </Table.Cell>
                        <Table.Cell>
                          {customerInfo.billingAddress.streetAddress}
                          <br />
                          {customerInfo.billingAddress.postalCode}{" "}
                          {customerInfo.billingAddress.city}
                        </Table.Cell>
                      </Table.Row>
                    </Table.Body>
                  </Table>
                </Grid.Column>
                <Grid.Column>
                  <Header as="h4">Insurance Options</Header>
                  <Table basic collapsing>
                    <Table.Body>
                      <Table.Row>
                        <Table.Cell>
                          <b>Start Date</b>
                        </Table.Cell>
                        <Table.Cell>
                          {this.formatDate(insuranceOptions.startDate)}
                        </Table.Cell>
                      </Table.Row>
                      <Table.Row>
                        <Table.Cell>
                          <b>Insurance Type</b>
                        </Table.Cell>
                        <Table.Cell>
                          {insuranceOptions.insuranceType}
                        </Table.Cell>
                      </Table.Row>
                      <Table.Row>
                        <Table.Cell>
                          <b>Deductible</b>
                        </Table.Cell>
                        <Table.Cell>
                          {insuranceOptions.deductible.amount}{" "}
                          {insuranceOptions.deductible.currency}
                        </Table.Cell>
                      </Table.Row>
                    </Table.Body>
                  </Table>
                </Grid.Column>
              </Grid.Row>
            </Grid>
          </Grid.Column>
        </Grid.Row>
      </Grid>
    )
  }

  renderInsuranceQuoteDetails(insuranceQuote: InsuranceQuote) {
    const { expirationDate, insurancePremium, policyLimit } = insuranceQuote
    return (
      <Grid padded>
        <Grid.Row>
          <Grid.Column>
            <Grid columns={2} style={{ border: "1px solid #aaaaaa" }}>
              <Grid.Row>
                <Grid.Column>
                  <Table basic collapsing celled>
                    <Table.Body>
                      <Table.Row>
                        <Table.Cell>
                          <b>Expiration Date</b>
                        </Table.Cell>
                        <Table.Cell>
                          {this.formatDatetime(expirationDate)}
                        </Table.Cell>
                      </Table.Row>
                      <Table.Row>
                        <Table.Cell>
                          <b>Insurance Premium</b>
                        </Table.Cell>
                        <Table.Cell>
                          {insurancePremium.amount} {insurancePremium.currency}
                        </Table.Cell>
                      </Table.Row>
                      <Table.Row>
                        <Table.Cell>
                          <b>Policy Limit</b>
                        </Table.Cell>
                        <Table.Cell>
                          {policyLimit.amount} {policyLimit.currency}
                        </Table.Cell>
                      </Table.Row>
                    </Table.Body>
                  </Table>
                </Grid.Column>
              </Grid.Row>
            </Grid>
          </Grid.Column>
        </Grid.Row>
      </Grid>
    )
  }

  renderInsuranceQuoteRequest(insuranceQuoteRequest: InsuranceQuoteRequest) {
    return (
      <Fragment>
        {insuranceQuoteRequest.statusHistory.map((statusChange, index) => {
          const isCurrentStatus =
            index === insuranceQuoteRequest.statusHistory.length - 1
          return (
            <Fragment key={`status-history-entry-${index}`}>
              {this.renderInsuranceQuoteRequestStatusChange(
                insuranceQuoteRequest,
                statusChange,
                isCurrentStatus
              )}
            </Fragment>
          )
        })}
      </Fragment>
    )
  }

  renderInsuranceQuoteRequestStatusChange(
    insuranceQuoteRequest: InsuranceQuoteRequest,
    statusChange: RequestStatusChange,
    isCurrentStatus: boolean
  ) {
    switch (statusChange.status) {
      case "REQUEST_SUBMITTED":
        return (
          <Fragment>
            <br />
            <br />
            <p>
              <span style={{ fontWeight: "bold", textDecoration: "underline" }}>
                {this.formatDatetime(statusChange.date)}:
              </span>
              <br /> You submitted the following Insurance Quote Request:
            </p>
            {this.renderInsuranceQuoteRequestDetails(insuranceQuoteRequest)}
            <br />
            {isCurrentStatus && (
              <p>
                Lakeside Mutual has not yet responded to this Insurance Quote
                Request.
              </p>
            )}
          </Fragment>
        )
      case "REQUEST_REJECTED":
        return (
          <p>
            <span style={{ fontWeight: "bold", textDecoration: "underline" }}>
              {this.formatDatetime(statusChange.date)}:
            </span>
            <br />
            Lakeside Mutual has <span style={{ color: "red" }}>
              rejected
            </span>{" "}
            this Insurance Quote Request.
          </p>
        )
      case "QUOTE_RECEIVED":
        return (
          <Fragment>
            <p>
              <span style={{ fontWeight: "bold", textDecoration: "underline" }}>
                {this.formatDatetime(statusChange.date)}:
              </span>
              <br />
              Lakeside Mutual has{" "}
              <span style={{ color: "green" }}>accepted</span> this Insurance
              Quote Request and has responded with the following Insurance
              Quote:
            </p>

            {insuranceQuoteRequest.insuranceQuote &&
              this.renderInsuranceQuoteDetails(
                insuranceQuoteRequest.insuranceQuote
              )}
            <br />
            {isCurrentStatus && (
              <p>
                Would you like to accept this Insurance Quote?&nbsp;&nbsp;
                <Button
                  size="mini"
                  color="green"
                  compact
                  onClick={() =>
                    this.respondToInsuranceQuote(insuranceQuoteRequest, true)
                  }
                >
                  Accept
                </Button>
                <Button
                  size="mini"
                  color="red"
                  compact
                  onClick={() =>
                    this.respondToInsuranceQuote(insuranceQuoteRequest, false)
                  }
                >
                  Reject
                </Button>
              </p>
            )}
          </Fragment>
        )
      case "QUOTE_ACCEPTED":
        return (
          <p>
            <span style={{ fontWeight: "bold", textDecoration: "underline" }}>
              {this.formatDatetime(statusChange.date)}:
            </span>
            <br />
            You have <span style={{ color: "green" }}>accepted</span> this
            Insurance Quote.
          </p>
        )
      case "POLICY_CREATED":
        return (
          <p>
            <span style={{ fontWeight: "bold", textDecoration: "underline" }}>
              {this.formatDatetime(statusChange.date)}:
            </span>
            <br />A new policy has been created.
          </p>
        )
      case "QUOTE_REJECTED":
        return (
          <p>
            <span style={{ fontWeight: "bold", textDecoration: "underline" }}>
              {this.formatDatetime(statusChange.date)}:
            </span>
            <br />
            You have <span style={{ color: "red" }}>rejected</span> this
            Insurance Quote. No new insurance policy will be created.
          </p>
        )
      case "QUOTE_EXPIRED":
        return (
          <p>
            <span style={{ fontWeight: "bold", textDecoration: "underline" }}>
              {this.formatDatetime(statusChange.date)}:
            </span>
            <br />
            This Insurance Quote has{" "}
            <span style={{ color: "red" }}>expired</span>.
          </p>
        )
      default:
        return null
    }
  }

  render() {
    return (
      <Segment>
        <Breadcrumb size="big">
          <Breadcrumb.Section>
            <Link to="/">Insurance Quote Requests</Link>
          </Breadcrumb.Section>
          <Breadcrumb.Divider icon="right angle" />
          <Breadcrumb.Section active>
            Insurance Quote Request Details
          </Breadcrumb.Section>
        </Breadcrumb>

        {!this.props.insuranceQuoteRequest &&
          this.props.insuranceQuoteRequestFetchError && (
            <Message
              error
              header="Error"
              content="Insurance Quote Request not found."
            />
          )}

        {this.props.insuranceQuoteRequest &&
          !this.props.insuranceQuoteRequestFetchError &&
          this.renderInsuranceQuoteRequest(this.props.insuranceQuoteRequest)}
      </Segment>
    )
  }
}

export default withRouter(InsuranceQuoteRequestDetail)
