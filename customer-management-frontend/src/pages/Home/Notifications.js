// @flow

import * as React from "react"
import {Fragment, useEffect, useRef, useState} from "react"
import {Label, Loader, Message, Table} from "semantic-ui-react"
import errorMessages from "../../errorMessages"
import SockJsClient from "react-stomp"
import {customerManagementBackend} from "../../config"
import {Navigate} from "react-router";
import {useGetNotificationsQuery} from "../../store/customer-management/customerManagementApi";

function Notifications(): React$Element {
    const [didTimeout, setDidTimeout] = useState<boolean>(false)
    const [isConnected, setIsConnected] = useState<boolean>(false)
    const [notifications, setNotifications] = useState<Notification[]>([])
    const [redirectCustomerId, setRedirectCustomerId] = useState<string | null>(null)
    const [timeoutId, setTimeoutId] = useState<TimeoutID>()
    const clientRef: React$Ref<SockJsClient> = useRef()

    const {data} = useGetNotificationsQuery();

    useEffect(() => {
        if (isConnected) return
        const timeout = setTimeout(() => {
            const client = clientRef.current;
            if (!isConnected) {
                setDidTimeout(true)
                client?.disconnect();
            }
        }, 5000)
        setTimeoutId(timeout)
        return () => {
            clearTimeout(timeout);
        }
    }, [isConnected])

    useEffect(() => {
        if (data != null &&
            data.length > 0 &&
            notifications.length === 0) {
            setNotifications(data)
        }
    }, [data, notifications]);

    if (redirectCustomerId != null) {
        return <Navigate to={`/customers/${redirectCustomerId}`} push/>
    }
    return (
        <>
            {isConnected &&
                !didTimeout &&
                notifications.length > 0 && (
                    <Table color="blue" selectable>
                        <Table.Body>
                            {notifications.map((notification, index) => (
                                <Table.Row
                                    key={index}
                                    style={{cursor: "pointer"}}
                                    onClick={() => {
                                        setRedirectCustomerId(notification.customerId)
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
            {!isConnected && !didTimeout && <Loader active inline size="small"/>}
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
                    setNotifications(message)
                }}
                onConnect={() => {
                    if (timeoutId != null) {
                        clearTimeout(timeoutId)
                        setTimeoutId(undefined)
                    }
                    setIsConnected(true)
                }}
                ref={clientRef}
            />
        </>
    )
}

export default Notifications