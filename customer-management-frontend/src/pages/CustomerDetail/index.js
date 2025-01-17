// @flow

import * as React from "react"
import {Fragment} from "react"
import {Breadcrumb, Button, Header, Icon, Message, Segment,} from "semantic-ui-react"
import {Link} from "react-router-dom"
import CustomerProfileView from "./CustomerProfileView"
import ChatView from "./ChatView"
import errorMessages from "../../errorMessages"
import {policyManagementFrontend} from "../../config"
import {
    useAcknowledgeInteractionsMutation,
    useGetCustomerQuery,
    useGetInteractionLogQuery
} from "../../store/customer-management/customerManagementApi";
import {useParams} from "react-router";

function CustomerDetail(): React$Element {
    const {customerId} = useParams();
    const {data: customer, isError: fetchCustomerError} = useGetCustomerQuery({customerId})
    const {data: interactionLog, isError: fetchInteractionLogError} = useGetInteractionLogQuery({customerId})
    const [acknowledgeInteractions] = useAcknowledgeInteractionsMutation()
    
    async function acknowledgeInteraction(interactionId: string) {
        if (customerId != null) {
            await acknowledgeInteractions({
                customerId,
                interactionAcknowledgementDto: {lastAcknowledgedInteractionId: interactionId}
            }).unwrap()
        }
    }

    return (
        <Segment>
            <Breadcrumb
                style={{
                    fontSize: "1.28571429rem",
                    marginTop: 0,
                    paddingTop: 0,
                }}>
                <Breadcrumb.Section link as={Link} to="/">
                    Customers
                </Breadcrumb.Section>
                <Breadcrumb.Divider icon="right angle"/>
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
                href={`${policyManagementFrontend}/customers/${customerId || ""}`}>
                <Icon name="external"/>
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
                            <Icon name="edit"/>
                            Edit Profile
                        </Button>
                    </Header>
                    <CustomerProfileView customer={customer}/>
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
                            didReceiveMessage={async (message) => {
                                if (!message.sentByOperator && message.id != null) {
                                    await acknowledgeInteraction(message.id)
                                }
                            }}
                        />
                    </Fragment>
                )}
        </Segment>
    )
}

export default CustomerDetail
