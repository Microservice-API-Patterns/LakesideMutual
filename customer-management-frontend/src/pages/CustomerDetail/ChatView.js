// @flow

import * as React from "react"
import {Fragment, useEffect, useRef, useState} from "react"
import SockJsClient from "react-stomp"
import ChatRoom from "../../components/ChatRoom"
import {customerManagementBackend} from "../../config"
import {Loader, Message} from "semantic-ui-react"
import errorMessages from "../../errorMessages"

export type Props = {
    customer: Customer,
    interactionLog: InteractionLog,
    didReceiveMessage: (MessageDto) => void,
}

function ChatView({customer, interactionLog, didReceiveMessage}: Props) {
    const [didTimeout, setDidTimeout] = useState<boolean>(false)
    const [isConnected, setIsConnected] = useState<boolean>(false)
    const [messages, setMessages] = useState<MessageDto[]>([])

    useEffect(() => {
        if (interactionLog == null) return

        setMessages(interactionLog.interactions.map((interaction) => ({
            id: interaction.id,
            username: interactionLog.username,
            content: interaction.content,
            customerId: customer.customerId,
            date: interaction.date,
            sentByOperator: interaction.sentByOperator,
        })))
    }, [interactionLog]);

    const clientRef: React$Ref<SockJsClient> = useRef()
    let timeout: ?TimeoutID;

    useEffect(() => {
        timeout = setTimeout(() => {
            const client = clientRef.current;
            if (!isConnected && client) {
                setDidTimeout(true)
                client.disconnect();
            }
        }, 5000)
        return () => {
            clearTimeout(timeout);
        }
    })

    function appendMessage(message: MessageDto) {
        setMessages(messages.concat([message]))
    }

    return (
        <>
            <SockJsClient
                url={`${customerManagementBackend}/ws`}
                topics={["/topic/messages"]}
                onMessage={(message) => {
                    didReceiveMessage(message)
                    if (customer.customerId === message.customerId) {
                        appendMessage(message)
                    }
                }}
                onConnect={() => {
                    if (timeout != null) {
                        clearTimeout(timeout)
                    }
                    setIsConnected(true)
                }}
                ref={clientRef}
            />
            {isConnected && !didTimeout && (
                <ChatRoom
                    messages={messages}
                    onSubmit={(text) => {
                        const client = clientRef.current
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
                    }}/>
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
        </>
    )
}

export default ChatView