// @flow

import * as React from "react"
import {Fragment, useState} from "react"
import {Button, Form, Grid, Icon, Input, Menu, Message, Table} from "semantic-ui-react"
import {Link} from "react-router-dom"
import errorMessages from "../../errorMessages"
import {useGetCustomersQuery} from "../../store/customerManagementApi";

function Customers(): React$Element {
    const [filter, setFilter] = useState<string>("")
    const [offset, setOffset] = useState<number>(0)

    // Note: This query is automatically refreshed if the parameters change 
    const {data: customersResponse, isError, isLoading, isFetching} = useGetCustomersQuery({filter, offset})

    function onFilterChange(event, data) {
        setFilter(data.value)
    }

    function onFilterSubmit() {
        setOffset(0)
    }

    function loadPreviousPage() {
        const offset = customersResponse.offset || 0
        const limit = customersResponse.limit || 0
        const newOffset = offset - limit
        if (newOffset >= 0) {
            setOffset(newOffset)
        }
    }

    function loadNextPage() {
        const offset = customersResponse.offset || 0
        const limit = customersResponse.limit || 0
        const size = customersResponse.size || 0
        const newOffset = offset + limit
        if (newOffset < size) {
            setOffset(newOffset)
        }
    }

    function renderCustomerName(customer: Customer) {
        const customerName = `${customer.firstname} ${customer.lastname}`
        if (filter === "") {
            return customerName
        }

        const startIndex = customerName
            .toLowerCase()
            .indexOf(filter.toLowerCase())
        if (startIndex === -1) {
            return customerName
        }

        const endIndex = startIndex + filter.length
        return (
            <span>
                {customerName.substring(0, startIndex)}
                <b>{customerName.substring(startIndex, endIndex)}</b>
                {customerName.substring(endIndex)}
            </span>
        )
    }

    function renderCustomers() {
        const offset = customersResponse.offset || 0
        const limit = customersResponse.limit || 0
        const size = customersResponse.size || 0
        const isFirstPage = offset === 0
        const isLastPage = offset + limit >= size

        return (
            <>
                <Grid columns={2}>
                    <Grid.Row>
                        <Grid.Column>
                            <Form
                                onSubmit={onFilterSubmit}
                                style={{flexDirection: "row", flex: 1}}
                            >
                                <Input
                                    placeholder="Search..."
                                    fluid
                                    onChange={onFilterChange}
                                    action
                                    autoFocus
                                >
                                    <input/>
                                    <Button
                                        type="submit"
                                        loading={isFetching}
                                        primary
                                    >
                                        <Icon name="search"/> Search
                                    </Button>
                                </Input>
                            </Form>
                        </Grid.Column>
                        <Grid.Column>
                            <Button
                                color="green"
                                floated="right"
                                as={Link}
                                to="/customers/new"
                            >
                                <Icon name="add"/>
                                New Customer
                            </Button>
                        </Grid.Column>
                    </Grid.Row>
                </Grid>

                <Table celled>
                    <Table.Header>
                        <Table.Row>
                            <Table.HeaderCell>Name</Table.HeaderCell>
                            <Table.HeaderCell>Email</Table.HeaderCell>
                            <Table.HeaderCell>Address</Table.HeaderCell>
                            <Table.HeaderCell/>
                        </Table.Row>
                    </Table.Header>

                    <Table.Body>
                        {customersResponse.customers.map(customer => {
                            return (
                                <Table.Row key={customer.customerId}>
                                    <Table.Cell>{renderCustomerName(customer)}</Table.Cell>
                                    <Table.Cell>{customer.email}</Table.Cell>
                                    <Table.Cell>{`${customer.streetAddress}, ${
                                        customer.postalCode
                                    } ${customer.city}`}</Table.Cell>
                                    <Table.Cell>
                                        <Button as={Link} to={`/customers/${customer.customerId}`}>
                                            Show
                                        </Button>
                                    </Table.Cell>
                                </Table.Row>
                            )
                        })}
                    </Table.Body>

                    <Table.Footer>
                        <Table.Row>
                            <Table.HeaderCell>
                                {offset + 1} - {Math.min(offset + limit, size)} of {size}
                            </Table.HeaderCell>
                            <Table.HeaderCell colSpan="3">
                                <Menu floated="right" pagination>
                                    <Menu.Item
                                        as="a"
                                        icon
                                        onClick={loadPreviousPage}
                                        disabled={isFirstPage}
                                    >
                                        <Icon name="chevron left"/>
                                    </Menu.Item>
                                    <Menu.Item
                                        as="a"
                                        icon
                                        onClick={loadNextPage}
                                        disabled={isLastPage}
                                    >
                                        <Icon name="chevron right"/>
                                    </Menu.Item>
                                </Menu>
                            </Table.HeaderCell>
                        </Table.Row>
                    </Table.Footer>
                </Table>
            </>
        )
    }

    return (
        <>
            {isError && (
                <Message
                    error
                    header="Error"
                    content={errorMessages.customerManagementBackendNotAvailable}
                />
            )}
            {!isError && !isLoading && renderCustomers()}
        </>
    )
}

export default Customers
