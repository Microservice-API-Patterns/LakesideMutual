// @flow

import React from "react"
import { Menu, Segment } from "semantic-ui-react"
import { Link, withRouter, type Location } from "react-router-dom"

export type Props = {
  location: Location,
}

const MenuBar = (props: Props) => {
  return (
    <Segment.Group style={{ marginTop: "1em" }}>
      <Segment>
        <Menu pointing secondary color="blue">
          <Menu.Item
            name="Home"
            icon="dashboard"
            active={props.location.pathname === "/"}
            as={Link}
            to="/"
          />
        </Menu>
      </Segment>
    </Segment.Group>
  )
}

export default withRouter(MenuBar)
