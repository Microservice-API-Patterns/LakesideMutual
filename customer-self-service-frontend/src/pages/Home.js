// @flow

import React from "react"
import { Link, Redirect } from "react-router-dom"
import { Button, Grid, Header } from "semantic-ui-react"
import { Segment } from "semantic-ui-react"

export type Props = {
  isAuthenticated: boolean,
}

const Home = ({ isAuthenticated }: Props) =>
  isAuthenticated ? (
    <Redirect to={"/policies"} />
  ) : (
    <Grid className="HomeScreen" verticalAlign="middle" centered={true}>
      <Grid.Column>
        <Header as="h2" content="Lakeside Mutual - Customer Self-Service" />
        <Segment stacked={true}>
          <Header as="h3" content="Welcome to LM Customer Self-Service" />
          <Button
            style={{ marginBottom: "1em" }}
            fluid
            primary
            as={Link}
            to={"/login"}
            content="Log in to access your LM account"
          />
          <p>If you do not already have an account, sign up here:</p>
          <Button fluid as={Link} to={"/signup"} content="Sign up" />
        </Segment>
      </Grid.Column>
    </Grid>
  )

export default Home
