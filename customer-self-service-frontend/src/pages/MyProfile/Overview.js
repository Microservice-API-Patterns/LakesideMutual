// @flow

import React from "react"
import { Button, Segment, List, Header } from "semantic-ui-react"
import { Link } from "react-router-dom"

export type Props = {
  customer: Customer,
}

class Overview extends React.Component<Props> {
  render() {
    const {
      firstname,
      lastname,
      streetAddress,
      city,
      postalCode,
      email,
      phoneNumber,
    } = this.props.customer

    return (
      <Segment>
        <Header as="h3">
          Customer Profile
          <Button
            as={Link}
            to="/profile/change-address"
            floated="right"
            size="mini"
            basic
            compact
          >
            Change Address
          </Button>
        </Header>
        <List>
          <List.Item>
            <List.Icon name="user" />
            <List.Content>
              {firstname} {lastname}
            </List.Content>
          </List.Item>
          <List.Item>
            <List.Icon name="marker" />
            <List.Content>
              {streetAddress}, {postalCode} {city}
            </List.Content>
          </List.Item>
          <List.Item>
            <List.Icon name="mail" />
            <List.Content>
              <a href={`mailto:${email}`}>{email}</a>
            </List.Content>
          </List.Item>
          <List.Item>
            <List.Icon name="phone" />
            <List.Content>{phoneNumber}</List.Content>
          </List.Item>
        </List>
      </Segment>
    )
  }
}

export default Overview
