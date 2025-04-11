// @flow

import * as React from "react"
import {useState} from "react"
import {Accordion, Grid, Icon, List} from "semantic-ui-react"
import moment from "moment"

export type Props = {
    customer: Customer,
}

function CustomerProfileView({customer}: Props): React$Element {
    const [showOldAddresses, setShowOldAddresses] = useState<boolean>(false)

    function handleClick() {
        setShowOldAddresses(!showOldAddresses);
    }

    const {
        firstname,
        lastname,
        streetAddress,
        birthday,
        postalCode,
        city,
        email,
        phoneNumber,
        moveHistory,
    } = customer

    return (
        <Grid columns="two">
            <Grid.Row>
                <Grid.Column>
                    <List>
                        <List.Item>
                            <List.Icon name="user"/>
                            <List.Content>
                                <List.Header>Name</List.Header>
                                <List.Description>
                                    {firstname} {lastname}
                                </List.Description>
                            </List.Content>
                        </List.Item>

                        <List.Item>
                            <List.Icon name="marker"/>
                            <List.Content>
                                <List.Header>Address</List.Header>
                                <List.Description>
                                    {streetAddress}, {postalCode} {city}
                                </List.Description>
                            </List.Content>
                        </List.Item>

                        <List.Item>
                            {moveHistory.length > 0 && (
                                <Accordion>
                                    <Accordion.Title
                                        active={showOldAddresses}
                                        index={0}
                                        onClick={handleClick}>
                                        <Icon name="dropdown"/>
                                        Old Addresses
                                    </Accordion.Title>
                                    <Accordion.Content active={showOldAddresses}>
                                        <List bulleted style={{marginLeft: 40}}>
                                            {moveHistory.map((address, index) => (
                                                <List.Item key={`${index}`}>
                                                    {address.streetAddress}, {address.postalCode}{" "}
                                                    {address.city}
                                                </List.Item>
                                            ))}
                                        </List>
                                    </Accordion.Content>
                                </Accordion>
                            )}
                        </List.Item>
                    </List>
                </Grid.Column>
                <Grid.Column>
                    <List>
                        <List.Item>
                            <List.Icon name="calendar"/>
                            <List.Content>
                                <List.Header>Date of Birth</List.Header>
                                <List.Description>
                                    {moment(birthday).format("MMMM Do YYYY")}
                                </List.Description>
                            </List.Content>
                        </List.Item>

                        <List.Item>
                            <List.Icon name="mail"/>
                            <List.Content>
                                <List.Header>Email Address</List.Header>
                                <List.Description>
                                    <a href={`mailto:${email}`}>{email}</a>
                                </List.Description>
                            </List.Content>
                        </List.Item>
                        <List.Item>
                            <List.Icon name="phone"/>
                            <List.Content>
                                <List.Header>Phone Number</List.Header>
                                <List.Description>{phoneNumber}</List.Description>
                            </List.Content>
                        </List.Item>
                    </List>
                </Grid.Column>
            </Grid.Row>
        </Grid>
    )
}

export default CustomerProfileView
