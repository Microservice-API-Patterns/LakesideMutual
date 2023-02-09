// @flow

import { Fragment } from "react"
import * as React from "react"
import SockJsClient from "react-stomp"
import ChatRoom from "../../components/ChatRoom"
import { customerManagementBackend } from "../../config"
import { Message, Loader } from "semantic-ui-react"
import errorMessages from "../../errorMessages"

type ReactRef<ElementType: React.ElementType> = {
  current: null | React.ElementRef<ElementType>,
}

export type Props = {
  customer: Customer,
  interactionLog: InteractionLog,
  didReceiveMessage: (MessageDto) => void,
}

type State = {
  didTimeout: boolean,
  isConnected: boolean,
  messages: Array<MessageDto>,
}

export default class ChatView extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props)

    const { customer, interactionLog } = props
    const interactions = interactionLog.interactions
    const messages: Array<MessageDto> = interactions.map((interaction) => ({
      id: interaction.id,
      username: interactionLog.username,
      content: interaction.content,
      customerId: customer.customerId,
      date: interaction.date,
      sentByOperator: interaction.sentByOperator,
    }))

    this.state = {
      didTimeout: false,
      isConnected: false,
      messages,
    }
  }

  timeout: ?TimeoutID
  clientRef: ReactRef<SockJsClient> = React.createRef()

  componentDidMount() {
    this.timeout = setTimeout(() => {
      const client = this.clientRef.current
      if (client) {
        this.setState({ didTimeout: true })
        client.disconnect()
      }
    }, 5000)
  }

  appendMessage = (message: MessageDto) => {
    const messages = this.state.messages.concat([message])
    this.setState({ messages })
  }

  render() {
    const { messages, isConnected, didTimeout } = this.state
    const { customer } = this.props

    return (
      <Fragment>
        <SockJsClient
          url={`${customerManagementBackend}/ws`}
          topics={["/topic/messages"]}
          onMessage={(message) => {
            this.props.didReceiveMessage(message)
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
          <ChatRoom
            messages={messages}
            onSubmit={(text) => {
              const client = this.clientRef.current
              if (client) {
                const message: MessageDto = {
                  id: null,
                  date: null,
                  username: `${customer.firstname} ${customer.lastname}`,
                  content: text,
                  customerId: customer.customerId,
                  sentByOperator: true,
                }
                client.sendMessage("/chat/messages", JSON.stringify(message))
              }
            }}
          />
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
      </Fragment>
    )
  }
}
