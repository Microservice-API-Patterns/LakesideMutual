// @flow

import * as React from "react"
import { Fragment } from "react"
import { connect } from "@brigad/redux-rest-easy"
import { Message, Label, Loader } from "semantic-ui-react"
import { Table } from "semantic-ui-react"
import { Redirect } from "react-router-dom"
import errorMessages from "../../errorMessages"
import SockJsClient from "react-stomp"
import { customerManagementBackend } from "../../config"
import {
  getAllNotifications,
  retrieveNotifications,
  resetNotifications,
} from "../../redux-rest-easy/notifications"

type ReactRef<ElementType: React.ElementType> = {
  current: null | React.ElementRef<ElementType>,
}

export type Props = {
  retrieveNotifications: (onSuccess: () => void, onError: () => void) => void,
  resetNotifications: () => void,
  notifications: Array<Notification>,
}

type State = {
  didTimeout: boolean,
  isConnected: boolean,
  notifications: Array<Notification>,
  redirectCustomerId: ?CustomerId,
}

class Notifications extends React.Component<Props, State> {
  state = {
    didTimeout: false,
    isConnected: false,
    notifications: [],
    redirectCustomerId: null,
  }

  timeout: ?TimeoutID
  clientRef: ReactRef<SockJsClient> = React.createRef()

  componentDidMount() {
    this.props.resetNotifications()
    this.props.retrieveNotifications(() => {}, () => {})

    this.timeout = setTimeout(() => {
      const client = this.clientRef.current
      if (client) {
        this.setState({ didTimeout: true })
        client.disconnect()
      }
    }, 5000)
  }

  componentDidUpdate(prevProps: Props) {
    if (
      this.props.notifications.length > 0 &&
      prevProps.notifications.length === 0
    ) {
      this.setState({ notifications: this.props.notifications })
    }
  }

  renderNotifications() {
    const { notifications, isConnected, didTimeout } = this.state
    return (
      <Fragment>
        {isConnected &&
          !didTimeout &&
          notifications.length > 0 && (
            <Table color="blue" selectable>
              <Table.Body>
                {notifications.map((notification, index) => (
                  <Table.Row
                    key={index}
                    style={{ cursor: "pointer" }}
                    onClick={() => {
                      this.setState({
                        redirectCustomerId: notification.customerId,
                      })
                    }}
                  >
                    <Table.Cell>
                      <b>{notification.username}</b>
                    </Table.Cell>
                    <Table.Cell collapsing>
                      <Label circular color="blue">
                        {notification.count === 1
                          ? "1 Message"
                          : `${notification.count} Messages`}
                      </Label>
                    </Table.Cell>
                  </Table.Row>
                ))}
              </Table.Body>
            </Table>
          )}
        {isConnected &&
          !didTimeout &&
          notifications.length === 0 && (
            <p>There are currently no new notifications.</p>
          )}
        {!isConnected && !didTimeout && <Loader active inline size="small" />}
        {!isConnected &&
          didTimeout && (
            <Message
              error
              header="Error"
              content={
                errorMessages.customerManagementBackendNotAvailable
              }
            />
          )}
        <SockJsClient
          url={`${customerManagementBackend}/ws`}
          topics={["/topic/notifications"]}
          onMessage={message => {
            this.setState({ notifications: message })
          }}
          onConnect={() => {
            if (this.timeout) {
              clearTimeout(this.timeout)
            }
            this.setState({ isConnected: true })
          }}
          ref={this.clientRef}
        />
      </Fragment>
    )
  }

  render() {
    const { redirectCustomerId } = this.state

    if (redirectCustomerId != null) {
      return <Redirect to={`/customers/${redirectCustomerId}`} push />
    }

    return this.renderNotifications()
  }
}

const mapStateToProps = (state, props) => ({
  notifications: getAllNotifications(state),
})

const mapDispatchToProps = dispatch => {
  return {
    retrieveNotifications: (onSuccess, onError) => {
      dispatch(retrieveNotifications({ onSuccess, onError }))
    },
    resetNotifications: () => dispatch(resetNotifications()),
  }
}

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Notifications)
