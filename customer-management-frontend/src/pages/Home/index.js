// @flow

import * as React from "react"
import {Header, Segment} from "semantic-ui-react"
import Customers from "./Customers"
import Notifications from "./Notifications";

function Home(): React$Element {
    return (
        <Segment>
            <Header as="h3" content="Notifications"/>
            <Notifications/>
            <Header as="h3" content="Customers"/>
            <Customers/>
        </Segment>
    )
}

export default Home