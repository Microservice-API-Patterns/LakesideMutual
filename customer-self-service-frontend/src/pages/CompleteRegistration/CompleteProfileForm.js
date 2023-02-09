// @flow

import React, { Fragment } from "react"
import { Button, Form, Message } from "semantic-ui-react"
import { extractFormError } from "../../utils"
import errorMessages from "../../errorMessages"
import { ApiError } from "../../api/helpers"

export type Props = {
  actions: {
    clearErrors: () => void,
    completeRegistration: (data: CompleteProfileFormData) => Promise<void>,
    lookupCitiySuggestions: (postalCode: string) => Promise<CitySuggestions>,
  },
  isCompletingRegistration: boolean,
  registrationError: FormSubmissionError,
}

const firstnameKey = "firstname"
const lastnameKey = "lastname"
const streetAddressKey = "streetAddress"
const postalCodeKey = "postalCode"
const cityKey = "city"
const phoneNumberKey = "phoneNumber"
const birthdayKey = "birthday"

const context = new Map([
  [firstnameKey, "First Name"],
  [lastnameKey, "Last Name"],
  [streetAddressKey, "Street Address"],
  [postalCodeKey, "Postal Code"],
  [cityKey, "City"],
  [phoneNumberKey, "Phone Number"],
  [birthdayKey, "Birthday"],
])

type State = {
  formContent: {
    firstname: string,
    lastname: string,
    streetAddress: string,
    postalCode: string,
    city: string,
    phoneNumber: string,
    birthday: string,
  },
  citySearch: string,
  cityValue: string,
  cityOptions: Array<{ key: string, text: string, value: string }>,
}

export default class CompleteProfileForm extends React.Component<Props, State> {
  state = {
    formContent: {
      firstname: "",
      lastname: "",
      streetAddress: "",
      postalCode: "",
      city: "",
      phoneNumber: "",
      birthday: "",
    },
    citySearch: "",
    cityValue: "",
    cityOptions: [],
  }

  constructor(props: Props) {
    super(props)
    props.actions.clearErrors()
  }

  handleChange = (e: Event, { name, value }: { name: string, value: string }) =>
    this.setState((prevState) => {
      return { formContent: { ...prevState.formContent, [name]: value } }
    })

  handleSubmit = async () => {
    await this.props.actions.completeRegistration(this.state.formContent)
  }

  renderTextField(
    key: string,
    value: string,
    errorFields: Array<string>,
    onBlur: ?(e: Event) => Promise<void> = null
  ) {
    return (
      <Form.Input
        label={context.get(key)}
        placeholder={context.get(key)}
        type="text"
        name={key}
        value={value}
        onChange={this.handleChange}
        onBlur={onBlur}
        error={errorFields.includes(key)}
      />
    )
  }

  handleCitySearchChange = (
    e: Event,
    { searchQuery }: { searchQuery: string }
  ) => {
    this.setState((prevState) => {
      return {
        formContent: { ...prevState.formContent, [cityKey]: searchQuery },
        citySearch: searchQuery,
        cityValue: "",
      }
    })
  }

  handleCityItemSelect = (e: Event, { value }: { value: string }) => {
    this.setState((prevState) => {
      return {
        formContent: { ...prevState.formContent, [cityKey]: value },
        citySearch: "",
        cityValue: value,
      }
    })
  }

  fetchCitySuggestions = async (e: Event) => {
    const postalCode = (e.target: window.HTMLInputElement).value
    this.setState({ cityOptions: [] })

    try {
      const result = await this.props.actions.lookupCitiySuggestions(postalCode)
      const cityOptions = result.cities.map((city) => {
        return {
          key: city,
          text: city,
          value: city,
        }
      })
      this.setState({ cityOptions })
    } catch (error) {
      console.log(error)
    }
  }

  getFormError(): ?FormError {
    const registrationError = this.props.registrationError

    if (
      registrationError != null &&
      extractFormError(registrationError, context) != null
    ) {
      return extractFormError(registrationError, context)
    } else if (!(registrationError instanceof ApiError)) {
      return {
        errorFields: [],
        errorMessages: [errorMessages.customerSelfServiceBackendNotAvailable],
      }
    } else {
      return null
    }
  }

  render() {
    const { isCompletingRegistration, registrationError } = this.props

    const {
      firstname,
      lastname,
      streetAddress,
      postalCode,
      phoneNumber,
      birthday,
    } = this.state.formContent

    const hasError = !!registrationError
    const formError = this.getFormError()
    const errorFields = formError ? formError.errorFields : []
    const errorMessages = formError ? formError.errorMessages : []

    return (
      <Fragment>
        <Message
          attached="top"
          header="Welcome to Lakeside Mutual"
          content="Please fill out the following form to complete your registration"
        />
        <Form
          className="attached fluid segment"
          onSubmit={this.handleSubmit}
          error={hasError}
        >
          <Form.Group widths="equal">
            {this.renderTextField(firstnameKey, firstname, errorFields)}
            {this.renderTextField(lastnameKey, lastname, errorFields)}
          </Form.Group>

          {this.renderTextField(streetAddressKey, streetAddress, errorFields)}

          <Form.Group widths="equal">
            {this.renderTextField(
              postalCodeKey,
              postalCode,
              errorFields,
              this.fetchCitySuggestions
            )}

            <Form.Dropdown
              fluid
              label={context.get(cityKey)}
              placeholder={context.get(cityKey)}
              name={cityKey}
              options={this.state.cityOptions}
              search
              selection
              selectOnBlur={false}
              noResultsMessage={null}
              onChange={this.handleCityItemSelect}
              onSearchChange={this.handleCitySearchChange}
              value={this.state.cityValue}
              searchQuery={this.state.citySearch}
              error={errorFields.includes(cityKey)}
            />
          </Form.Group>

          {this.renderTextField(phoneNumberKey, phoneNumber, errorFields)}

          <Form.Input
            label={context.get(birthdayKey)}
            placeholder={context.get(birthdayKey)}
            type="date"
            name={birthdayKey}
            value={birthday}
            onChange={this.handleChange}
            error={errorFields.includes(birthdayKey)}
          />
          <Button
            fluid
            color="blue"
            disabled={isCompletingRegistration}
            loading={isCompletingRegistration}
          >
            Complete Registration
          </Button>

          <Message error>
            <Message.Header>Registration failed</Message.Header>
            <Message.List items={errorMessages} />
          </Message>
        </Form>
      </Fragment>
    )
  }
}
