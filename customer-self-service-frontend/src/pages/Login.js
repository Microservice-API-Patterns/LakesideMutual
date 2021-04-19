// @flow

import React from "react"
import { Redirect, Link, type Location } from "react-router-dom"
import { Button, Grid, Header, Form, Segment, Message } from "semantic-ui-react"
import { ApiError } from "../api/helpers"
import errorMessages from "../errorMessages"

export type Props = {
  actions: {
    clearErrors: () => void,
    login: (email: string, password: string) => Promise<void>,
  },
  isLoggingIn: boolean,
  loginError: ?Error,
  location: Location,
}

type State = {
  email: string,
  password: string,
  shouldRedirect: boolean,
}

class Login extends React.Component<Props, State> {
  state = {
    email: "",
    password: "",
    shouldRedirect: false,
  }

  testUserEmail = "admin@example.com"
  testUserPassword = "1password"

  constructor(props: Props) {
    super(props)
    props.actions.clearErrors()
  }

  componentWillReceiveProps(newProps: Props) {
    if (
      this.props.isLoggingIn &&
      !newProps.isLoggingIn &&
      !newProps.loginError
    ) {
      this.setState({ shouldRedirect: true })
    }
  }

  handleChange = (e: Event, { name, value }: { name: string, value: string }) =>
    this.setState({ [name]: value })

  onSubmit = async (event: Event) => {
    event.preventDefault()
    const { email, password } = this.state
    await this.props.actions.login(email, password)
  }

  fillInTestCredentials = () => {
    this.setState({
      email: this.testUserEmail,
      password: this.testUserPassword,
    })
  }

  getErrorMessage(): string {
    const error = this.props.loginError
    if (error instanceof ApiError) {
      return errorMessages.invalidLoginCredentials
    } else {
      return errorMessages.customerSelfServiceBackendNotAvailable
    }
  }

  render() {
    const { loginError, isLoggingIn, location } = this.props
    const { from } = location.state || {
      from: { pathname: "/policies" },
    }

    if (this.state.shouldRedirect) {
      return <Redirect to={from} />
    }

    return (
      <Grid className="LoginScreen" verticalAlign="middle" centered={true}>
        <Grid.Column>
          <Header as="h2" content="Lakeside Mutual - Customer Self-Service" />
          <Form size="large" error={!!loginError}>
            <Segment stacked={true}>
              <Header as="h3" content="Log in to your LM account" />
              <Form.Input
                name="email"
                onChange={this.handleChange}
                icon="mail"
                iconPosition="left"
                placeholder="Email Address"
                value={this.state.email}
              />
              <Form.Input
                name="password"
                onChange={this.handleChange}
                icon="lock"
                iconPosition="left"
                placeholder="Password"
                type="password"
                value={this.state.password}
              />
              <Button
                primary
                onClick={this.onSubmit}
                size="large"
                fluid={true}
                disabled={isLoggingIn}
                loading={isLoggingIn}
                content="Log in"
              />
              <Message style={{ textAlign: "left" }} info>
                <p>
                  Use the following credentials to test the application without
                  creating a new account:
                </p>
                <br />
                <Grid columns={3} padded={false}>
                  <Grid.Row style={{ padding: 0 }}>
                    <Grid.Column>
                      <b>Email Address:</b>
                      <br />
                      <b>Password:</b>
                    </Grid.Column>
                    <Grid.Column>
                      {this.testUserEmail}
                      <br />
                      {this.testUserPassword}
                    </Grid.Column>
                    <Grid.Column textAlign="right" verticalAlign="middle">
                      <Button
                        compact
                        size="tiny"
                        color="grey"
                        onClick={this.fillInTestCredentials}
                      >
                        Fill in
                      </Button>
                    </Grid.Column>
                  </Grid.Row>
                </Grid>
              </Message>
            </Segment>
            <Message
              error
              header="Log in failed"
              content={this.getErrorMessage()}
            />
          </Form>
          <Message>
            <Link to="/signup">Sign up for a new account here.</Link>
          </Message>
        </Grid.Column>
      </Grid>
    )
  }
}

export default Login
