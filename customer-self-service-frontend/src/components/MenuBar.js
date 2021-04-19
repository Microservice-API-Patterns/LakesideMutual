// @flow

import React, { Fragment } from "react"
import { Menu, Segment } from "semantic-ui-react"
import {
  Link,
  withRouter,
  type Location,
  type RouterHistory,
} from "react-router-dom"

export type Props = {
  actions: {
    logout: () => Promise<void>,
  },
  isAuthenticated: boolean,
  history: RouterHistory,
  location: Location,
  customer: ?Customer,
  user: ?User,
}

const MenuBar = (props: Props) => {
  if (!props.isAuthenticated) {
    return null
  }
  return (
    <Segment.Group style={{ marginTop: "1em" }}>
      <Segment>
        <Menu pointing secondary color="blue">
          {props.user != null &&
            props.user.customerId != null &&
            props.customer != null && (
              <Fragment>
                <Menu.Item
                  name="My Policies"
                  icon="dashboard"
                  active={props.location.pathname === "/policies"}
                  as={Link}
                  to="/policies"
                />
                <Menu.Item
                  name="My Profile"
                  icon="setting"
                  active={props.location.pathname.startsWith("/profile")}
                  as={Link}
                  to="/profile"
                />
                <Menu.Item
                  name="Contact us"
                  icon="help circle"
                  active={props.location.pathname.startsWith("/contact")}
                  as={Link}
                  to="/contact"
                />
              </Fragment>
            )}
          <Menu.Menu position="right">
            <Menu.Item
              name="Logout"
              icon="sign out"
              onClick={async () => {
                await props.actions.logout()
                props.history.push("/")
              }}
            />
          </Menu.Menu>
        </Menu>
      </Segment>
    </Segment.Group>
  )
}

export default withRouter(MenuBar)
