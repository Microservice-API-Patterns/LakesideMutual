// @flow

import React from "react"
import { Header } from "semantic-ui-react"
import { Button, Form, Message } from "semantic-ui-react"
import { extractFormError } from "../../utils"
import { Redirect } from "react-router-dom"
import errorMessages from "../../errorMessages"
import { ApiError } from "../../api/helpers"

export type Props = {
  actions: {
    clearErrors: () => void,
    changeAddress: (address: Address) => Promise<void>,
  },
  customer: Customer,
  isUpdatingAddress: boolean,
  addressUpdateError: FormSubmissionError,
}

const streetAddressKey = "streetAddress"
const postalCodeKey = "postalCode"
const cityKey = "city"

const context = new Map([
  [streetAddressKey, "Street Address"],
  [postalCodeKey, "Postal Code"],
  [cityKey, "City"],
])

type State = {
  streetAddress: string,
  postalCode: string,
  city: string,
  redirectToOverview: boolean,
}

class UpdateAddressForm extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props)

    const { streetAddress, postalCode, city } = props.customer
    this.state = {
      streetAddress,
      postalCode,
      city,
      redirectToOverview: false,
    }
    props.actions.clearErrors()
  }

  handleChange = (e: Event, { name, value }: { name: string, value: string }) =>
    this.setState({ [name]: value })

  handleSubmit = async () => {
    const address = {
      streetAddress: this.state.streetAddress,
      postalCode: this.state.postalCode,
      city: this.state.city,
    }
    await this.props.actions.changeAddress(address)
    if (!this.props.addressUpdateError) {
      this.setState({ redirectToOverview: true })
    }
  }

  renderTextField(key: string, value: string, errorFields: Array<string>) {
    return (
      <Form.Input
        label={context.get(key)}
        placeholder={context.get(key)}
        type="text"
        name={key}
        value={value}
        onChange={this.handleChange}
        error={errorFields.includes(key)}
      />
    )
  }

  getFormError(): ?FormError {
    const addressUpdateError = this.props.addressUpdateError

    if (
      addressUpdateError != null &&
      extractFormError(addressUpdateError, context) != null
    ) {
      return extractFormError(addressUpdateError, context)
    } else if (!(addressUpdateError instanceof ApiError)) {
      return {
        errorFields: [],
        errorMessages: [errorMessages.customerSelfServiceBackendNotAvailable],
      }
    } else {
      return null
    }
  }

  render() {
    if (this.state.redirectToOverview) {
      return <Redirect to={"/profile"} />
    } else {
      const { isUpdatingAddress, addressUpdateError } = this.props
      const { streetAddress, city, postalCode } = this.state

      const hasError = !!addressUpdateError
      const formError = this.getFormError()
      const errorFields = formError ? formError.errorFields : []
      const errorMessages = formError ? formError.errorMessages : []

      return (
        <Form
          className="attached fluid segment"
          onSubmit={this.handleSubmit}
          error={hasError}
        >
          <Header as="h3">Change Address</Header>

          {this.renderTextField(streetAddressKey, streetAddress, errorFields)}

          <Form.Group widths="equal">
            {this.renderTextField(postalCodeKey, postalCode, errorFields)}
            {this.renderTextField(cityKey, city, errorFields)}
          </Form.Group>

          <Button
            fluid
            color="blue"
            disabled={isUpdatingAddress}
            loading={isUpdatingAddress}
          >
            Save Changes
          </Button>

          <Message error>
            <Message.Header>Address Change failed</Message.Header>
            <Message.List items={errorMessages} />
          </Message>
        </Form>
      )
    }
  }
}

export default UpdateAddressForm
