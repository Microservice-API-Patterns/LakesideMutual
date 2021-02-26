// @flow

import React from "react"
import { Loader, Breadcrumb, Segment } from "semantic-ui-react"
import { Button, Form, Message } from "semantic-ui-react"
import { Redirect } from "react-router-dom"
import { type Match, Link } from "react-router-dom"
import {
  getCustomer,
  retrieveCustomer,
  updateCustomer,
  isUpdatingCustomer,
} from "../../redux-rest-easy/customers"
import { connect } from "@brigad/redux-rest-easy"
import moment from "moment"
import errorMessages from "../../errorMessages"

function extractFormError(error, context: Map<string, string>): ?FormError {
  if (error.errors != null) {
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
  customer: ?Customer,
  retrieveCustomer: (
    customerId: string,
    onSuccess: () => void,
    onError: () => void
  ) => void,
  updateCustomer: (
    customerId: string,
    updatedCustomer: {
      firstname: string,
      lastname: string,
      city: string,
      streetAddress: string,
      postalCode: string,
      email: string,
      phoneNumber: string,
      birthday: string,
    },
    onSuccess: () => void,
    onError: () => void
  ) => void,
  isUpdatingCustomer: boolean,
  match: Match,
}

const firstnameKey = "firstname"
const lastnameKey = "lastname"
const birthdayKey = "birthday"
const streetAddressKey = "streetAddress"
const postalCodeKey = "postalCode"
const cityKey = "city"
const emailKey = "email"
const phoneNumberKey = "phoneNumber"

const context = new Map([
  [firstnameKey, "First Name"],
  [lastnameKey, "Last Name"],
  [birthdayKey, "Date of Birth"],
  [streetAddressKey, "Street Address"],
  [postalCodeKey, "Postal Code"],
  [cityKey, "City"],
  [emailKey, "Email Address"],
  [phoneNumberKey, "Phone Number"],
])

type State = {
  fetchCustomerError: boolean,
  redirectToOverview: boolean,
  firstname: string,
  lastname: string,
  birthday: string,
  streetAddress: string,
  postalCode: string,
  city: string,
  phoneNumber: string,
  email: string,
  formError: ?FormError,
}

class EditCustomer extends React.Component<Props, State> {
  state = {
    fetchCustomerError: false,
    redirectToOverview: false,
    firstname: "",
    lastname: "",
    birthday: "",
    streetAddress: "",
    postalCode: "",
    city: "",
    phoneNumber: "",
    email: "",
    formError: null,
  }

  componentDidMount() {
    if (this.props.customer) {
      this.updateFormState(this.props.customer)
    } else {
      const customerId = this.props.match.params.customerId
      if (customerId != null) {
        this.props.retrieveCustomer(
          customerId,
          () => {
            this.setState({ fetchCustomerError: false })
          },
          () => {
            this.setState({ fetchCustomerError: true })
          }
        )
      }
    }
  }

  updateFormState(customer: Customer) {
    const {
      firstname,
      lastname,
      birthday,
      streetAddress,
      postalCode,
      city,
      email,
      phoneNumber,
    } = customer

    const formattedBirthday = moment(birthday).format("YYYY-MM-DD")
    this.setState({
      firstname,
      lastname,
      birthday: formattedBirthday,
      streetAddress,
      postalCode,
      city,
      email,
      phoneNumber,
    })
  }

  componentDidUpdate(prevProps: Props) {
    if (this.props.customer && !prevProps.customer) {
      this.updateFormState(this.props.customer)
    }
  }

  handleChange = (
    e: Event,
    { name, value }: { name: string, value: string }
  ) => {
    this.setState({ [name]: value })
  }

  handleSubmit = async () => {
    const customerId = this.props.match.params.customerId
    if (customerId != null) {
      const {
        firstname,
        lastname,
        birthday,
        streetAddress,
        postalCode,
        city,
        email,
        phoneNumber,
      } = this.state
      const updatedCustomer = {
        firstname,
        lastname,
        birthday,
        streetAddress,
        postalCode,
        city,
        email,
        phoneNumber,
      }
      this.props.updateCustomer(
        customerId,
        updatedCustomer,
        () => {
          this.setState({ formError: null, redirectToOverview: true })
        },
        error => {
          if (!error) {
            return
          }

          error.response.json().then(response => {
            const formError = extractFormError(response, context)
            this.setState({ formError })
          })
        }
      )
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
    const { customer, isUpdatingCustomer } = this.props
    const {
      fetchCustomerError,
      firstname,
      lastname,
      birthday,
      streetAddress,
      postalCode,
      city,
      email,
      phoneNumber,
      formError,
    } = this.state

    const hasError = !!formError
    const errorFields = formError ? formError.errorFields : []
    const errorMessages = formError ? formError.errorMessages : []

    if (!customer) {
      return <Loader active />
    } else if (this.state.redirectToOverview) {
      return <Redirect to={`/customers/${customer.customerId}`} push />
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
            <Breadcrumb.Section
              link
              as={Link}
              to={`/customers/${customer ? customer.customerId : ""}`}
            >
              {customer &&
                !fetchCustomerError &&
                `${customer.firstname} ${customer.lastname}`}
              {!customer && !fetchCustomerError && "Loading..."}
              {!customer && fetchCustomerError && "Error!"}
            </Breadcrumb.Section>
            <Breadcrumb.Divider icon="right angle" />
            <Breadcrumb.Section>Edit Profile</Breadcrumb.Section>
          </Breadcrumb>
          <br />
          <br />

          <Form onSubmit={this.handleSubmit} error={hasError}>
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

            {this.renderTextField(emailKey, email, errorFields)}
            {this.renderTextField(phoneNumberKey, phoneNumber, errorFields)}

            <Button
              fluid
              color="blue"
              disabled={isUpdatingCustomer}
              loading={isUpdatingCustomer}
            >
              Save Changes
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
  const customerId = props.match.params.customerId
  return {
    customer: getCustomer(state, customerId),
    isUpdatingCustomer: isUpdatingCustomer(state, props),
  }
}

const mapDispatchToProps = dispatch => ({
  retrieveCustomer: (customerId, onSuccess, onError) =>
    dispatch(
      retrieveCustomer({ onSuccess, onError, urlParams: { customerId } })
    ),
  updateCustomer: (customerId, updatedCustomer, onSuccess, onError) =>
    dispatch(
      updateCustomer({
        onSuccess,
        onError,
        urlParams: { customerId },
        body: updatedCustomer,
      })
    ),
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(EditCustomer)
