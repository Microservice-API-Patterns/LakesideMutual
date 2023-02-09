// @flow

// Adapted from https://github.com/WigoHunter/react-chatapp

import * as React from "react"
import { Fragment } from "react"
import "./chatroom.css"
import MessageBubble from "./MessageBubble"
import { Input, Button, Form } from "semantic-ui-react"
import GroupSeparator from "./GroupSeparator"
import moment from "moment"

type ReactRef<ElementType: React.ElementType> = {
  current: null | React.ElementRef<ElementType>,
}

export type Props = {
  messages: Array<Message>,
  onSubmit: (string) => void,
}

type State = {
  content: string,
}

export default class ChatRoom extends React.Component<Props, State> {
  chatRef: ReactRef<"ul"> = React.createRef()

  state = {
    content: "",
  }

  componentDidMount() {
    this.scrollToBottom()
  }

  componentDidUpdate() {
    this.scrollToBottom()
  }

  scrollToBottom() {
    const chat = this.chatRef.current
    if (chat) {
      chat.scrollTop = chat.scrollHeight
    }
  }

  handleChange = (e: Event, { value }: { value: string }) => {
    this.setState({ content: value })
  }

  submitMessage = () => {
    const { content } = this.state
    if (content !== "") {
      this.props.onSubmit(content)
      this.setState({ content: "" })
    }
  }

  groupMessages(messages: Array<Message>): Array<Array<Message>> {
    const groups: Array<Array<Message>> = []
    for (const message of messages) {
      let group: Array<Message>
      if (groups.length === 0) {
        group = []
        groups.push(group)
      } else {
        const lastGroup = groups[groups.length - 1]
        const lastMessage = lastGroup[lastGroup.length - 1]
        const startDate = moment(lastMessage.date)
        const endDate = moment(message.date)
        const minutes = moment.duration(endDate.diff(startDate)).asMinutes()
        if (minutes > 10) {
          group = []
          groups.push(group)
        } else {
          group = lastGroup
        }
      }

      group.push(message)
    }
    return groups
  }

  render() {
    const { content } = this.state
    const { messages } = this.props
    const groups = this.groupMessages(messages)

    return (
      <div className="chatroom">
        <ul className="chats" ref={this.chatRef}>
          {groups.map((group, groupIndex) => (
            <Fragment key={`group-${groupIndex}`}>
              {group[0].date != null && (
                <GroupSeparator date={new Date(group[0].date)} />
              )}
              {group.map((message, messageIndex) => (
                <MessageBubble
                  message={message}
                  key={`message-${groupIndex}-${messageIndex}`}
                />
              ))}
            </Fragment>
          ))}
          {messages.length === 0 && (
            <p className="nomessages">
              There are no messages yet.
              <br /> Send a message to start a conversation.
            </p>
          )}
        </ul>
        <Form onSubmit={this.submitMessage}>
          <Input
            fluid
            type="text"
            placeholder="Enter a Message"
            action
            value={content}
            onChange={this.handleChange}
            autoFocus
          >
            <input />
            <Button type="submit" color="green">
              Send
            </Button>
          </Input>
        </Form>
      </div>
    )
  }
}
