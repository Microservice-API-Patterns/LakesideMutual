// @flow

import React from "react"
import {Menu, Segment} from "semantic-ui-react"
import {Link} from "react-router-dom"
import {useLocation} from "react-router";

function MenuBar(): React$Element {
    const location = useLocation();
    return (
        <Segment.Group style={{marginTop: "1em"}}>
            <Segment>
                <Menu pointing secondary color="blue">
                    <Menu.Item
                        name="Home"
                        icon="dashboard"
                        active={location.pathname === "/"}
                        as={Link}
                        to="/"
                    />
                </Menu>
            </Segment>
        </Segment.Group>
    )
}

export default MenuBar
