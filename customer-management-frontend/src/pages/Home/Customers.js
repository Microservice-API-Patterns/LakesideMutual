// @flow

import * as React from "react"
import { Fragment } from "react"
import { connect } from "@brigad/redux-rest-easy"
import { Input, Form, Button, Message, Grid } from "semantic-ui-react"
import {
  retrieveCustomers,
  isRetrievingCustomers,
  getCustomersMetadata,
  getAllCustomers,
  resetCustomers,
} from "../../redux-rest-easy/customers"
import { Icon, Menu, Table } from "semantic-ui-react"
import { Link } from "react-router-dom"
import errorMessages from "../../errorMessages"

export type Props = {
  retrieveCustomers: (
    filter: string,
    offset: number,
    onSuccess: () => void,
    onError: () => void
  ) => void,
  isRetrievingCustomers: boolean,
  customers: Array<Customer>,
  metadata: { offset: ?number, limit: ?number, size: ?number },
  resetCustomers: () => void,
}

type State = {
  error: boolean,
  filter: string,
  activeFilter: string,
}

class Customers extends React.Component<Props, State> {
  state = {
    error: false,
    filter: "",
    activeFilter: "",
  }

  componentDidMount() {
    this.loadCustomers(0)
  }

  onSuccess = () => {
    this.setState({ error: false })
  }

  onError = () => {
    this.setState({ error: true })
  }

  loadCustomers = offset => {
    this.props.resetCustomers()
    this.props.retrieveCustomers(
      this.state.filter,
      offset,
      this.onSuccess,
      this.onError
    )
    this.setState({ activeFilter: this.state.filter })
  }

  onChange = (event, data) => {
    this.setState({ filter: data.value })
  }

  onSubmit = () => {
    this.loadCustomers(0)
  }

  loadPreviousPage = () => {
    const metadata = this.props.metadata
    const offset = metadata.offset || 0
    const limit = metadata.limit || 0
    const newOffset = offset - limit
    if (newOffset >= 0) {
      this.loadCustomers(newOffset)
    }
  }

  loadNextPage = () => {
    const metadata = this.props.metadata
    const offset = metadata.offset || 0
    const limit = metadata.limit || 0
    const size = metadata.size || 0
    const newOffset = offset + limit
    if (newOffset < size) {
      this.loadCustomers(newOffset)
    }
  }

  renderCustomerName(customer: Customer) {
    const customerName = `${customer.firstname} ${customer.lastname}`
    if (this.state.activeFilter === "") {
      return customerName
    }

    const startIndex = customerName
      .toLowerCase()
      .indexOf(this.state.activeFilter.toLowerCase())
    if (startIndex === -1) {
      return customerName
    }

    const endIndex = startIndex + this.state.activeFilter.length
    return (
      <span>
        {customerName.substring(0, startIndex)}
        <b>{customerName.substring(startIndex, endIndex)}</b>
        {customerName.substring(endIndex)}
      </span>
    )
  }

  renderCustomers() {
    const metadata = this.props.metadata
    const offset = metadata.offset || 0
    const limit = metadata.limit || 0
    const size = metadata.size || 0
    const isFirstPage = offset === 0
    const isLastPage = offset + limit >= size

    return (
      <Fragment>
        <Grid columns={2}>
          <Grid.Row>
            <Grid.Column>
              <Form
                onSubmit={this.onSubmit}
                style={{ flexDirection: "row", flex: 1 }}
              >
                <Input
                  placeholder="Search..."
                  fluid
                  onChange={this.onChange}
                  action
                  autoFocus
                >
                  <input />
                  <Button
                    type="submit"
                    loading={this.props.isRetrievingCustomers}
                    primary
                  >
                    <Icon name="search" /> Search
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
                <Icon name="add" />
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
              <Table.HeaderCell />
            </Table.Row>
          </Table.Header>

          <Table.Body>
            {this.props.customers.map(customer => {
              return (
                <Table.Row key={customer.customerId}>
                  <Table.Cell>{this.renderCustomerName(customer)}</Table.Cell>
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
                    onClick={this.loadPreviousPage}
                    disabled={isFirstPage}
                  >
                    <Icon name="chevron left" />
                  </Menu.Item>
                  <Menu.Item
                    as="a"
                    icon
                    onClick={this.loadNextPage}
                    disabled={isLastPage}
                  >
                    <Icon name="chevron right" />
                  </Menu.Item>
                </Menu>
              </Table.HeaderCell>
            </Table.Row>
          </Table.Footer>
        </Table>
      </Fragment>
    )
  }

  render() {
    const { error } = this.state

    return (
      <Fragment>
        {error && (
          <Message
            error
            header="Error"
            content={errorMessages.customerManagementBackendNotAvailable}
          />
        )}
        {!error && this.renderCustomers()}
      </Fragment>
    )
  }
}

const mapStateToProps = (state, props) => ({
  customers: getAllCustomers(state),
  metadata: getCustomersMetadata(state, props),
  isRetrievingCustomers: isRetrievingCustomers(state, props),
})

const mapDispatchToProps = dispatch => {
  return {
    retrieveCustomers: (filter, offset, onSuccess, onError) =>
      dispatch(
        retrieveCustomers({ onSuccess, onError, query: { filter, offset } })
      ),
    resetCustomers: () => dispatch(resetCustomers()),
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Customers)
