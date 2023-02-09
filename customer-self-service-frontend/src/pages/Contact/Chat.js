// @flow

import * as React from "react"
import { Segment, Header, Loader, Message } from "semantic-ui-react"
import SockJsClient from "react-stomp"
import ChatRoom from "../../components/ChatRoom"
import { customerManagementBackend } from "../../config"
import errorMessages from "../../errorMessages"

type ReactRef<ElementType: React.ElementType> = {
  current: null | React.ElementRef<ElementType>,
}

export type Props = {
  customer: Customer,
  interactionLog: ?InteractionLog,
  isFetchingInteractionLog: boolean,
  interactionLogFetchError: ?Error,
  actions: {
    fetchInteractionLog: (customerId: CustomerId) => Promise<void>,
  },
}

type State = {
  didTimeout: boolean,
  isConnected: boolean,
  messages: Array<Message>,
}

export default class Chat extends React.Component<Props, State> {
  state = {
    didTimeout: false,
    isConnected: false,
    messages: [],
  }

  timeout: ?TimeoutID
  clientRef: ReactRef<SockJsClient> = React.createRef()

  componentDidMount() {
    this.props.actions.fetchInteractionLog(this.props.customer.customerId)

    this.timeout = setTimeout(() => {
      const client = this.clientRef.current
      if (client) {
        this.setState({ didTimeout: true })
        client.disconnect()
      }
    }, 5000)
  }

  componentDidUpdate(prevProps: Props) {
    if (this.props.interactionLog != null && prevProps.interactionLog == null) {
      const customerId = this.props.customer.customerId
      const interactionLog = this.props.interactionLog
      const interactions = interactionLog.interactions

      const messages: Array<Message> = interactions.map((interaction) => ({
        username: interactionLog.username,
        content: interaction.content,
        customerId: customerId,
        date: interaction.date,
        sentByOperator: interaction.sentByOperator,
      }))

      this.setState({ messages })
    }
  }

  appendMessage = (message: Message) => {
    const messages = this.state.messages.concat([message])
    this.setState({ messages })
  }

  render() {
    const { customer } = this.props
    const { didTimeout, isConnected, messages } = this.state

    return (
      <Segment>
        <Header as="h3">Contact Us</Header>
        <p>
          Do you need help? We are here for you! Send a message in the chat
          below and our customer service will get back to you as soon as
          possible.
        </p>
        <SockJsClient
          url={`${customerManagementBackend}/ws`}
          topics={["/topic/messages"]}
          onMessage={(message) => {
            if (customer.customerId === message.customerId) {
              this.appendMessage(message)
            }
          }}
          onConnect={() => {
            if (this.timeout) {
              clearTimeout(this.timeout)
            }
            this.setState({ isConnected: true })
          }}
          ref={this.clientRef}
        />
        {isConnected && !didTimeout && (
          <React.Fragment>
            <ChatRoom
              messages={messages}
              onSubmit={(text) => {
                const customer = this.props.customer
                const client = this.clientRef.current
                if (client) {
                  const message = {
                    username: `${customer.firstname} ${customer.lastname}`,
                    content: text,
                    customerId: customer.customerId,
                    sentByOperator: false,
                  }
                  client.sendMessage("/chat/messages", JSON.stringify(message))
                }
              }}
            />
          </React.Fragment>
        )}
        {!isConnected && !didTimeout && (
          <Loader active inline="centered">
            Loading
          </Loader>
        )}
        {!isConnected && didTimeout && (
          <Message
            error
            header="Error"
            content={errorMessages.customerManagementBackendNotAvailable}
          />
        )}
      </Segment>
    )
  }
}
