// @flow

import React from "react"
import { Redirect, Link } from "react-router-dom"
import {
  Button,
  Grid,
  Header,
  Form,
  Segment,
  Message,
  Popup,
} from "semantic-ui-react"
import { extractFormError } from "../utils"
import { ApiError } from "../api/helpers"
import errorMessages from "../errorMessages"

export type Props = {
  actions: {
    clearErrors: () => void,
    signup: (email: string, password: string) => Promise<boolean>,
  },
  isSigningUp: boolean,
  signupError: ?Error,
}

const emailKey = "email"
const passwordKey = "password"

const context = new Map([
  [emailKey, "Email Address"],
  [passwordKey, "Password"],
])

type State = {
  email: string,
  password: string,
  agb: boolean,
  shouldRedirect: boolean,
}

class Signup extends React.Component<Props, State> {
  state = {
    email: "",
    password: "",
    agb: false,
    shouldRedirect: false,
  }

  constructor(props: Props) {
    super(props)
    props.actions.clearErrors()
  }

  handleChange = (e: Event, { name, value }: { name: string, value: string }) =>
    this.setState({ [name]: value })

  onSubmit = async (event: Event) => {
    event.preventDefault()
    const { email, password } = this.state
    const success = await this.props.actions.signup(email, password)
    if (success) {
      this.setState({ shouldRedirect: true })
    }
  }

  getFormError(): ?FormError {
    const signupError = this.props.signupError

    if (signupError != null && extractFormError(signupError, context) != null) {
      return extractFormError(signupError, context)
    } else if (signupError instanceof ApiError && signupError.code === 409) {
      return {
        errorFields: ["email"],
        errorMessages: [errorMessages.emailAddressAlreadyRegistered],
      }
    } else if (!(signupError instanceof ApiError)) {
      return {
        errorFields: [],
        errorMessages: [errorMessages.customerSelfServiceBackendNotAvailable],
      }
    } else {
      return null
    }
  }

  render() {
    const { signupError, isSigningUp } = this.props
    const { email, password, agb, shouldRedirect } = this.state

    const hasError = !!signupError
    const formError = this.getFormError()
    const errorFields = formError ? formError.errorFields : []
    const errorMessages = formError ? formError.errorMessages : []

    if (shouldRedirect) {
      return <Redirect to="/login" />
    }

    return (
      <Grid className="SignupScreen" verticalAlign="middle" centered={true}>
        <Grid.Column>
          <Header as="h2" content="Lakeside Mutual - Customer Self-Service" />
          <Form size="large" error={hasError}>
            <Segment stacked={true}>
              <Header as="h3" content="Sign up to your LM account" />
              <Form.Input
                name={emailKey}
                onChange={this.handleChange}
                icon="mail"
                iconPosition="left"
                placeholder={context.get(emailKey)}
                value={email}
                error={errorFields.includes(emailKey)}
              />
              <Form.Input
                name={passwordKey}
                onChange={this.handleChange}
                icon="lock"
                iconPosition="left"
                placeholder={context.get(passwordKey)}
                type="password"
                value={password}
                error={errorFields.includes(passwordKey)}
              />
              <Form.Checkbox
                onChange={(event, { checked }) =>
                  this.setState({ agb: checked })
                }
                inline
                label={
                  <label>
                    I agree to the{" "}
                    <Popup trigger={<a href="#toc">terms and conditions</a>}>
                      <Popup.Header>Terms and Conditions</Popup.Header>
                      <Popup.Content>
                        <p>
                          Thank you for your interest in our example
                          application.
                        </p>
                        <p>
                          The software is provided "as is" without warranty of
                          any kind. The authors specifically disclaim all other
                          warranties, expressed or implied, including without
                          limitation the implied warranties of merchantability
                          or fitness for a particular use.
                        </p>
                      </Popup.Content>
                    </Popup>
                    .
                  </label>
                }
              />
              <Button
                primary
                disabled={!agb || isSigningUp}
                loading={isSigningUp}
                onClick={this.onSubmit}
                size="large"
                fluid={true}
                content="Sign up"
              />
            </Segment>
            <Message error>
              <Message.Header>Sign up failed</Message.Header>
              <Message.List items={errorMessages} />
            </Message>
          </Form>
          <Message>
            <Link to="/">Back to the homepage</Link>
          </Message>
        </Grid.Column>
      </Grid>
    )
  }
}

export default Signup
