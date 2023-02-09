// @flow

import * as React from "react"
import { Header, Segment } from "semantic-ui-react"
import Notifications from "./Notifications"
import Customers from "./Customers"

export type Props = {}

type State = {}

export default class Home extends React.Component<Props, State> {
  state = {}

  render() {
    return (
      <Segment>
        <Header as="h3" content="Notifications" />
        <Notifications />
        <Header as="h3" content="Customers" />
        <Customers />
      </Segment>
    )
  }
}
