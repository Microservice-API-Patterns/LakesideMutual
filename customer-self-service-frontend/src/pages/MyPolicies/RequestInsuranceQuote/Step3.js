// @flow

import React, { Fragment } from "react"
import { Button, Header, Table } from "semantic-ui-react"
import moment from "moment"

export type Props = {
  isSubmittingInsuranceQuoteRequest: boolean,
  onPrev: () => void,
  onSubmit: () => void,
  data: {
    step1: {
      contactAddressStreetAddress: string,
      contactAddressPostalCode: string,
      contactAddressCity: string,
      billingAddressStreetAddress: ?string,
      billingAddressPostalCode: ?string,
      billingAddressCity: ?string,
    },
    step2: {
      startDate: ?string,
      insuranceType: ?string,
      deductible: ?string,
    },
  },
}

export default class Step3 extends React.Component<Props> {
  render() {
    const isSubmittingInsuranceQuoteRequest =
      this.props.isSubmittingInsuranceQuoteRequest
    const step1Data = this.props.data.step1
    const contactAddressStreetAddress = step1Data.contactAddressStreetAddress
    const contactAddressPostalCode = step1Data.contactAddressPostalCode
    const contactAddressCity = step1Data.contactAddressCity
    const billingAddressStreetAddress =
      step1Data.billingAddressStreetAddress == null
        ? contactAddressStreetAddress
        : step1Data.billingAddressStreetAddress
    const billingAddressPostalCode =
      step1Data.billingAddressPostalCode == null
        ? contactAddressPostalCode
        : step1Data.billingAddressPostalCode
    const billingAddressCity =
      step1Data.billingAddressCity == null
        ? contactAddressCity
        : step1Data.billingAddressCity

    const step2Data = this.props.data.step2
    const startDate =
      step2Data.startDate == null
        ? "n/a"
        : moment(step2Data.startDate).format("DD.MM.YYYY")
    const insuranceType =
      step2Data.insuranceType == null ? "n/a" : step2Data.insuranceType
    const deductible =
      step2Data.deductible == null ? "n/a" : step2Data.deductible

    return (
      <Fragment>
        <Header as="h3">Personal Data</Header>
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
                {contactAddressStreetAddress}
                <br />
                {contactAddressPostalCode} {contactAddressCity}
              </Table.Cell>
              <Table.Cell>
                {billingAddressStreetAddress}
                <br />
                {billingAddressPostalCode} {billingAddressCity}
              </Table.Cell>
            </Table.Row>
          </Table.Body>
        </Table>

        <Header as="h3">Insurance</Header>
        <Table basic collapsing>
          <Table.Body>
            <Table.Row>
              <Table.Cell>
                <b>Start Date</b>
              </Table.Cell>
              <Table.Cell>{startDate}</Table.Cell>
            </Table.Row>
            <Table.Row>
              <Table.Cell>
                <b>Insurance Type</b>
              </Table.Cell>
              <Table.Cell>{insuranceType}</Table.Cell>
            </Table.Row>
            <Table.Row>
              <Table.Cell>
                <b>Deductible</b>
              </Table.Cell>
              <Table.Cell>{deductible}</Table.Cell>
            </Table.Row>
          </Table.Body>
        </Table>

        <Button
          floated="left"
          labelPosition="left"
          icon="left chevron"
          content="Previous"
          onClick={this.props.onPrev}
        />
        <Button
          primary
          floated="right"
          content="Request a Quote"
          loading={isSubmittingInsuranceQuoteRequest}
          onClick={this.props.onSubmit}
        />
      </Fragment>
    )
  }
}
