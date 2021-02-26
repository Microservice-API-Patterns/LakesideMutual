// @flow

import { Fragment } from "react"
import * as React from "react"
import { connect } from "@brigad/redux-rest-easy"
import {
  Header,
  Segment,
  Breadcrumb,
  Message,
  Button,
  Icon,
} from "semantic-ui-react"
import { getCustomer, retrieveCustomer } from "../../redux-rest-easy/customers"
import {
  getInteractionLog,
  retrieveInteractionLog,
  resetInteractionLogs,
  acknowledgeInteractions,
  isRetrievingInteractionLog,
} from "../../redux-rest-easy/interactionlogs"
import { type Match, Link } from "react-router-dom"
import CustomerProfileView from "./CustomerProfileView"
import ChatView from "./ChatView"
import errorMessages from "../../errorMessages"

export type Props = {
  customer: ?Customer,
  interactionLog: ?InteractionLog,
  retrieveCustomer: (
    customerId: string,
    onSuccess: () => void,
    onError: () => void
  ) => void,
  retrieveInteractionLog: (
    customerId: string,
    onSuccess: () => void,
    onError: () => void
  ) => void,
  isRetrievingInteractionLog: boolean,
  resetInteractionLogs: () => void,
  acknowledgeInteractions: (
    customerId: string,
    lastAcknowledgedInteractionId: string
  ) => void,
  match: Match,
}

type State = {
  fetchCustomerError: boolean,
  fetchInteractionLogError: boolean,
  isConnected: boolean,
}

class CustomerDetail extends React.Component<Props, State> {
  state = {
    fetchCustomerError: false,
    fetchInteractionLogError: false,
    isConnected: false,
  }

  componentDidMount() {
    const customerId = this.props.match.params.customerId
    if (customerId != null) {
      this.props.retrieveCustomer(
        customerId,
        () => {
          this.setState({ fetchCustomerError: false })
        },
        () => {
          this.setState({ fetchCustomerError: true })
        }
      )

      this.props.resetInteractionLogs()
      this.props.retrieveInteractionLog(
        customerId,
        () => {
          this.setState({ fetchInteractionLogError: false })
        },
        () => {
          this.setState({ fetchInteractionLogError: true })
        }
      )
    }
  }

  componentDidUpdate(prevProps: Props) {
    if (
      !this.props.isRetrievingInteractionLog &&
      prevProps.isRetrievingInteractionLog &&
      this.props.interactionLog
    ) {
      const interactionLog = this.props.interactionLog
      const interactions = interactionLog.interactions.filter(
        interaction => !interaction.sentByOperator
      )
      if (
        interactions.length > 0 &&
        interactions[interactions.length - 1].id !==
          interactionLog.lastAcknowledgedInteractionId
      ) {
        this.acknowledgeInteractions(interactions[interactions.length - 1].id)
      }
    }
  }

  acknowledgeInteractions(interactionId: string) {
    const customerId = this.props.match.params.customerId
    if (customerId != null) {
      this.props.acknowledgeInteractions(customerId, interactionId)
    }
  }

  render() {
    const { customer, interactionLog } = this.props
    const { fetchCustomerError, fetchInteractionLogError } = this.state

    return (
      <Segment>
        <Breadcrumb
          style={{
            fontSize: "1.28571429rem",
            marginTop: 0,
            paddingTop: 0,
          }}
        >
          <Breadcrumb.Section link as={Link} to="/">
            Customers
          </Breadcrumb.Section>
          <Breadcrumb.Divider icon="right angle" />
          <Breadcrumb.Section>
            {customer &&
              !fetchCustomerError &&
              `${customer.firstname} ${customer.lastname}`}
            {!customer && !fetchCustomerError && "Loading..."}
            {!customer && fetchCustomerError && "Error!"}
          </Breadcrumb.Section>
        </Breadcrumb>
        <Button
          as="a"
          compact
          size="mini"
          floated="right"
          href={`http://localhost:3010/customers/${this.props.match.params
            .customerId || ""}`}
        >
          <Icon name="external" />
          Open in Policy Management
        </Button>

        {fetchCustomerError && (
          <Message
            error
            header="Error"
            content={errorMessages.customerManagementBackendNotAvailable}
          />
        )}
        {!fetchCustomerError && customer && (
          <Fragment>
            <Header as="h4">
              Customer Profile
              <Button
                as={Link}
                to={`/customers/${customer.customerId}/edit_profile`}
                floated="right"
                size="mini"
                basic
                compact
              >
                <Icon name="edit" />
                Edit Profile
              </Button>
            </Header>
            <CustomerProfileView customer={customer} />
          </Fragment>
        )}

        {fetchInteractionLogError && (
          <Message
            error
            header="Error"
            content={errorMessages.customerManagementBackendNotAvailable}
          />
        )}
        {!fetchCustomerError &&
          !fetchInteractionLogError &&
          customer &&
          interactionLog && (
            <Fragment>
              <Header as="h4">Chat</Header>
              <ChatView
                customer={customer}
                interactionLog={interactionLog}
                didReceiveMessage={message => {
                  if (!message.sentByOperator && message.id != null) {
                    this.acknowledgeInteractions(message.id)
                  }
                }}
              />
            </Fragment>
          )}
      </Segment>
    )
  }
}

const mapStateToProps = (state, props) => {
  const customerId = props.match.params.customerId
  return {
    customer: getCustomer(state, customerId),
    interactionLog: getInteractionLog(state, customerId),
    isRetrievingInteractionLog: isRetrievingInteractionLog(state, props),
  }
}

const mapDispatchToProps = dispatch => ({
  retrieveCustomer: (customerId, onSuccess, onError) =>
    dispatch(
      retrieveCustomer({ onSuccess, onError, urlParams: { customerId } })
    ),
  retrieveInteractionLog: (customerId, onSuccess, onError) =>
    dispatch(
      retrieveInteractionLog({ onSuccess, onError, urlParams: { customerId } })
    ),
  resetInteractionLogs: () => dispatch(resetInteractionLogs()),
  acknowledgeInteractions: (customerId, lastAcknowledgedInteractionId) =>
    dispatch(
      acknowledgeInteractions({
        onSuccess: () => {},
        onError: () => {},
        urlParams: { customerId },
        body: { lastAcknowledgedInteractionId },
      })
    ),
})

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(CustomerDetail)
