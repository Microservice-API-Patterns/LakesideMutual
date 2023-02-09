// @flow

import React, { Fragment } from "react"
import { Redirect, Link } from "react-router-dom"
import { Segment, Breadcrumb, Step, Icon, Message } from "semantic-ui-react"
import Step1 from "./Step1"
import Step2 from "./Step2"
import Step3 from "./Step3"
import moment from "moment"
import { extractFormError } from "../../../utils"
import errorMessages from "../../../errorMessages"
import { ApiError } from "../../../api/helpers"

export type Props = {
  customer: Customer,
  isCreatingInsuranceQuoteRequest: boolean,
  insuranceQuoteRequestCreationError: ?Error,
  actions: {
    createInsuranceQuoteRequest: (InsuranceQuoteRequest) => Promise<void>,
    resetInsuranceQuoteRequestCreationError: () => void,
  },
}

type State = {
  shouldRedirect: boolean,
  currentStep: number,
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
}

const contactStreetAddressKey = "customerInfo.contactAddress.streetAddress"
const contactPostalCodeKey = "customerInfo.contactAddress.postalCode"
const contactCityKey = "customerInfo.contactAddress.city"
const billingStreetAddressKey = "customerInfo.billingAddress.streetAddress"
const billingPostalCodeKey = "customerInfo.billingAddress.postalCode"
const billingCityKey = "customerInfo.billingAddress.city"
const startDateKey = "insuranceOptions.startDate"
const insuranceTypeKey = "insuranceOptions.insuranceType"
const deductibleKey = "insuranceOptions.deductible.amount"

const context = new Map([
  [startDateKey, "Start Date"],
  [insuranceTypeKey, "Insurance Type"],
  [deductibleKey, "Deductible"],
  [contactStreetAddressKey, "Street Address of Contact Address"],
  [contactPostalCodeKey, "Postal Code of Contact Address"],
  [contactCityKey, "City of Contact Address"],
  [billingStreetAddressKey, "Street Address of Billing Address"],
  [billingPostalCodeKey, "Postal Code of Billing Address"],
  [billingCityKey, "City of Billing Address"],
])

export default class RequestInsuranceCode extends React.Component<
  Props,
  State
> {
  constructor(props: Props) {
    super(props)
    const { customer } = this.props
    this.state = {
      shouldRedirect: false,
      currentStep: 0,
      step1: {
        contactAddressStreetAddress: customer.streetAddress,
        contactAddressPostalCode: customer.postalCode,
        contactAddressCity: customer.city,
        billingAddressStreetAddress: null,
        billingAddressPostalCode: null,
        billingAddressCity: null,
      },
      step2: {
        startDate: moment().format("YYYY-MM-DD"),
        insuranceType: null,
        deductible: null,
      },
    }
  }

  componentWillReceiveProps(newProps: Props) {
    if (
      this.props.isCreatingInsuranceQuoteRequest &&
      !newProps.isCreatingInsuranceQuoteRequest &&
      !newProps.insuranceQuoteRequestCreationError
    ) {
      this.setState({ shouldRedirect: true })
    }
  }

  goToStep(index: number) {
    if (index === 2) {
      this.props.actions.resetInsuranceQuoteRequestCreationError()
    }
    this.setState({ currentStep: index })
  }

  submitInsuranceQuoteRequest() {
    const {
      contactAddressStreetAddress,
      contactAddressPostalCode,
      contactAddressCity,
      billingAddressStreetAddress,
      billingAddressPostalCode,
      billingAddressCity,
    } = this.state.step1

    const { customer } = this.props

    const customerInfo: CustomerInfo = {
      customerId: customer.customerId,
      firstname: customer.firstname,
      lastname: customer.lastname,
      contactAddress: {
        streetAddress: contactAddressStreetAddress,
        postalCode: contactAddressPostalCode,
        city: contactAddressCity,
      },
      billingAddress: {
        streetAddress:
          billingAddressStreetAddress != null
            ? billingAddressStreetAddress
            : contactAddressStreetAddress,
        postalCode:
          billingAddressPostalCode != null
            ? billingAddressPostalCode
            : contactAddressPostalCode,
        city:
          billingAddressCity != null ? billingAddressCity : contactAddressCity,
      },
    }

    const step2Data = this.state.step2
    const startDate =
      step2Data.startDate != null && step2Data.startDate !== ""
        ? moment(step2Data.startDate).format("YYYY-MM-DD")
        : null
    const insuranceType =
      step2Data.insuranceType != null ? step2Data.insuranceType : ""
    const deductibleStr =
      step2Data.deductible != null ? step2Data.deductible : ""
    const deductibleParts = deductibleStr.split(" ")
    const deductible = {
      amount: parseFloat(deductibleParts[0]),
      currency: deductibleParts[1],
    }

    const insuranceOptions: InsuranceOptions = {
      startDate,
      insuranceType,
      deductible,
    }

    const insuranceQuoteRequest: InsuranceQuoteRequest = {
      statusHistory: [],
      customerInfo,
      insuranceOptions,
    }
    this.props.actions.createInsuranceQuoteRequest(insuranceQuoteRequest)
  }

  getFormError(): ?FormError {
    const insuranceQuoteRequestCreationError =
      this.props.insuranceQuoteRequestCreationError

    if (
      insuranceQuoteRequestCreationError != null &&
      extractFormError(insuranceQuoteRequestCreationError, context) != null
    ) {
      return extractFormError(insuranceQuoteRequestCreationError, context)
    } else if (
      insuranceQuoteRequestCreationError != null &&
      !(insuranceQuoteRequestCreationError instanceof ApiError)
    ) {
      return {
        errorFields: [],
        errorMessages: [errorMessages.customerSelfServiceBackendNotAvailable],
      }
    } else {
      return null
    }
  }

  render() {
    const { currentStep } = this.state
    if (this.state.shouldRedirect) {
      return <Redirect to="/policies" />
    }

    const formError = this.getFormError()
    const errorMessages = formError ? formError.errorMessages : []
    return (
      <Segment>
        <Breadcrumb size="big">
          <Breadcrumb.Section>
            <Link to="/">Insurance Quote Requests</Link>
          </Breadcrumb.Section>
          <Breadcrumb.Divider icon="right angle" />
          <Breadcrumb.Section active>
            New Insurance Quote Request
          </Breadcrumb.Section>
        </Breadcrumb>

        <Step.Group size="tiny">
          <Step
            link
            active={currentStep === 0}
            onClick={() => this.goToStep(0)}
          >
            <Icon name="user" />
            <Step.Content>
              <Step.Title>Personal Data</Step.Title>
              <Step.Description>Enter your personal details</Step.Description>
            </Step.Content>
          </Step>
          <Step
            link
            active={currentStep === 1}
            onClick={() => this.goToStep(1)}
          >
            <Icon name="file alternate outline" />
            <Step.Content>
              <Step.Title>Insurance</Step.Title>
              <Step.Description>Configure the insurance</Step.Description>
            </Step.Content>
          </Step>
          <Step
            link
            active={currentStep === 2}
            onClick={() => this.goToStep(2)}
          >
            <Icon name="info" />
            <Step.Content>
              <Step.Title>Confirm Request</Step.Title>
              <Step.Description>
                Send the insurance quote request
              </Step.Description>
            </Step.Content>
          </Step>
        </Step.Group>

        {currentStep === 0 && (
          <Step1
            onNext={() => this.goToStep(1)}
            onChange={(formData) => this.setState({ step1: formData })}
            formData={this.state.step1}
          />
        )}
        {currentStep === 1 && (
          <Step2
            onPrev={() => this.goToStep(0)}
            onNext={() => this.goToStep(2)}
            onChange={(formData) => this.setState({ step2: formData })}
            formData={this.state.step2}
          />
        )}
        {currentStep === 2 && (
          <Fragment>
            {errorMessages.length > 0 && (
              <Message error>
                <Message.Header>Error</Message.Header>
                <Message.List items={errorMessages} />
              </Message>
            )}
            <Step3
              isSubmittingInsuranceQuoteRequest={
                this.props.isCreatingInsuranceQuoteRequest
              }
              onPrev={() => this.goToStep(1)}
              onSubmit={this.submitInsuranceQuoteRequest.bind(this)}
              data={{ step1: this.state.step1, step2: this.state.step2 }}
            />
          </Fragment>
        )}

        <br />
        <br />
      </Segment>
    )
  }
}
