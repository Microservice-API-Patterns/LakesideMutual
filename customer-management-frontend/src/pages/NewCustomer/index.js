// @flow

import React from "react"
import {
  Breadcrumb,
  Segment,
  Button,
  Form,
  Message,
  Header,
} from "semantic-ui-react"
import { Redirect } from "react-router-dom"
import { Link } from "react-router-dom"
import { signupUser, loginUser } from "../../redux-rest-easy/users"
import { createCustomer } from "../../redux-rest-easy/customers"
import { connect } from "@brigad/redux-rest-easy"
import errorMessages from "../../errorMessages"

function extractFormError(error, context: Map<string, string>): ?FormError {
  if (error.status === 409) {
    return {
      errorFields: ["email"],
      errorMessages: ["This email address has already been taken."],
    }
  } else if (error.errors != null) {
    const errorFields = error.errors.map(error => error.field)
    const errorMessages = error.errors.map(
      error =>
        `${context.get(error.field) || error.field} ${error.defaultMessage}`
    )

    return {
      errorFields,
      errorMessages,
    }
  } else {
    return {
      errorFields: [],
      errorMessages: [errorMessages.customerManagementBackendNotAvailable],
    }
  }
}

export type Props = {
  signupUser: (email: string, password: string) => Promise<void>,
  loginUser: (email: string, password: string) => Promise<string>,
  createCustomer: (
    firstname: string,
    lastname: string,
    birthday: string,
    streetAddress: string,
    postalCode: string,
    city: string,
    phoneNumber: string
  ) => Promise<void>,
}

const emailKey = "email"
const passwordKey = "password"
const firstnameKey = "firstname"
const lastnameKey = "lastname"
const birthdayKey = "birthday"
const streetAddressKey = "streetAddress"
const postalCodeKey = "postalCode"
const cityKey = "city"
const phoneNumberKey = "phoneNumber"

const context = new Map([
  [emailKey, "Email Address"],
  [passwordKey, "Password"],
  [firstnameKey, "First Name"],
  [lastnameKey, "Last Name"],
  [birthdayKey, "Date of Birth"],
  [streetAddressKey, "Street Address"],
  [postalCodeKey, "Postal Code"],
  [cityKey, "City"],
  [phoneNumberKey, "Phone Number"],
])

type State = {
  isCreatingCustomer: boolean,
  redirectToOverview: boolean,
  email: string,
  password: string,
  firstname: string,
  lastname: string,
  birthday: string,
  streetAddress: string,
  postalCode: string,
  city: string,
  phoneNumber: string,
  formError: ?FormError,
}

class NewCustomer extends React.Component<Props, State> {
  state = {
    isCreatingCustomer: false,
    redirectToOverview: false,
    email: "",
    password: "",
    firstname: "",
    lastname: "",
    birthday: "",
    streetAddress: "",
    postalCode: "",
    city: "",
    phoneNumber: "",
    formError: null,
  }

  handleChange = (
    e: Event,
    { name, value }: { name: string, value: string }
  ) => {
    this.setState({ [name]: value })
  }

  handleSubmit = async () => {
    const {
      email,
      password,
      firstname,
      lastname,
      birthday,
      streetAddress,
      postalCode,
      city,
      phoneNumber,
    } = this.state

    try {
      this.setState({ isCreatingCustomer: true })
      await this.props.signupUser(email, password)
      const token = await this.props.loginUser(email, password)
      localStorage.setItem("token", token)
      await this.props.createCustomer(
        firstname,
        lastname,
        birthday,
        streetAddress,
        postalCode,
        city,
        phoneNumber
      )
      this.setState({ isCreatingCustomer: false, redirectToOverview: true })
    } catch (error) {
      this.setState({ isCreatingCustomer: false })

      error.response.json().then(response => {
        const formError = extractFormError(response, context)
        this.setState({ formError })
      })
    }
  }

  renderTextField(
    key: string,
    value: string,
    errorFields: Array<string>,
    type: string = "text"
  ) {
    return (
      <Form.Input
        label={context.get(key)}
        placeholder={context.get(key)}
        type={type}
        name={key}
        value={value}
        onChange={this.handleChange}
        error={errorFields.includes(key)}
      />
    )
  }

  render() {
    const {
      isCreatingCustomer,
      email,
      password,
      firstname,
      lastname,
      birthday,
      streetAddress,
      postalCode,
      city,
      phoneNumber,
      formError,
    } = this.state

    const hasEmptyFields =
      !email ||
      !password ||
      !firstname ||
      !lastname ||
      !birthday ||
      !streetAddress ||
      !postalCode ||
      !city ||
      !phoneNumber
    const hasError = !!formError
    const errorFields = formError ? formError.errorFields : []
    const errorMessages = formError ? formError.errorMessages : []

    if (this.state.redirectToOverview) {
      return <Redirect to="/" push />
    } else {
      return (
        <Segment>
          <Breadcrumb
            style={{
              fontSize: "1.28571429rem",
              marginTop: 0,
              paddingTop: 0,
            }}
          >
            <Breadcrumb.Section link as={Link} to="/">
              Customers
            </Breadcrumb.Section>
            <Breadcrumb.Divider icon="right angle" />
            <Breadcrumb.Section>New Customer</Breadcrumb.Section>
          </Breadcrumb>
          <br />
          <br />

          <Form onSubmit={this.handleSubmit} error={hasError}>
            <Header>Login Credentials</Header>
            <Form.Group widths="equal">
              {this.renderTextField(emailKey, email, errorFields)}
              {this.renderTextField(
                passwordKey,
                password,
                errorFields,
                "password"
              )}
            </Form.Group>

            <Header>Customer Profile</Header>
            <Form.Group widths="equal">
              {this.renderTextField(firstnameKey, firstname, errorFields)}
              {this.renderTextField(lastnameKey, lastname, errorFields)}
            </Form.Group>

            {this.renderTextField(birthdayKey, birthday, errorFields, "date")}
            {this.renderTextField(streetAddressKey, streetAddress, errorFields)}

            <Form.Group widths="equal">
              {this.renderTextField(postalCodeKey, postalCode, errorFields)}
              {this.renderTextField(cityKey, city, errorFields)}
            </Form.Group>

            {this.renderTextField(phoneNumberKey, phoneNumber, errorFields)}

            <Button
              fluid
              color="blue"
              disabled={hasEmptyFields || isCreatingCustomer}
              loading={isCreatingCustomer}
            >
              Create Customer
            </Button>

            <Message error>
              <Message.Header>Update failed</Message.Header>
              <Message.List items={errorMessages} />
            </Message>
          </Form>
        </Segment>
      )
    }
  }
}

const mapStateToProps = (state, props) => {
  return {}
}

const mapDispatchToProps = dispatch => ({
  signupUser: (email, password) =>
    new Promise((resolve, reject) => {
      dispatch(
        signupUser({
          onSuccess: resolve,
          onError: reject,
          body: { email, password },
        })
      )
    }),
  loginUser: (email, password) =>
    new Promise((resolve, reject) => {
      dispatch(
        loginUser({
          onSuccess: data => resolve(data.users[email].token),
          onError: reject,
          body: { email, password },
        })
      )
    }),
  createCustomer: (
    firstname,
    lastname,
    birthday,
    streetAddress,
    postalCode,
    city,
    phoneNumber
  ) =>
    new Promise((resolve, reject) => {
      dispatch(
        createCustomer({
          onSuccess: resolve,
          onError: reject,
          body: {
            firstname,
            lastname,
            birthday,
            streetAddress,
            postalCode,
            city,
            phoneNumber,
          },
        })
      )
    }),
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(NewCustomer)
