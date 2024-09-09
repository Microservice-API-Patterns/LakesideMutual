// @flow

import React, {useEffect, useState} from "react"
import {Breadcrumb, Button, Form, Loader, Message, Segment} from "semantic-ui-react"
import {Link} from "react-router-dom"
import moment from "moment"
import errorMessages from "../../errorMessages"
import {Navigate, useParams} from "react-router";
import {
    CustomerDto,
    useGetCustomerQuery,
    useUpdateCustomerMutation
} from "../../store/customer-management/customerManagementApi";

function extractFormError(error, context: Map<string, string>): ?FormError {
    if (error.errors != null) {
        const errorFields = error.errors.map(error => error.field)
        const errorMessages = error.errors.map(
            error =>
                `${context.get(error.field) || error.field} ${error.defaultMessage}`
        )

        return {
            errorFields,
            errorMessages,
        }
    } else {
        return {
            errorFields: [],
            errorMessages: [errorMessages.customerManagementBackendNotAvailable],
        }
    }
}

const firstnameKey = "firstname"
const lastnameKey = "lastname"
const birthdayKey = "birthday"
const streetAddressKey = "streetAddress"
const postalCodeKey = "postalCode"
const cityKey = "city"
const emailKey = "email"
const phoneNumberKey = "phoneNumber"

const context = new Map([
    [firstnameKey, "First Name"],
    [lastnameKey, "Last Name"],
    [birthdayKey, "Date of Birth"],
    [streetAddressKey, "Street Address"],
    [postalCodeKey, "Postal Code"],
    [cityKey, "City"],
    [emailKey, "Email Address"],
    [phoneNumberKey, "Phone Number"],
])

type State = {
    fetchCustomerError: boolean,
    redirectToOverview: boolean,
    firstname: string,
    lastname: string,
    birthday: string,
    streetAddress: string,
    postalCode: string,
    city: string,
    phoneNumber: string,
    email: string,
    formError: ?FormError,
}

function EditCustomer(): React$Element {
    const {customerId} = useParams();
    const [isUpdatingCustomer, setIsUpdatingCustomer] = useState<boolean>(false)
    const [redirectToOverview, setRedirectToOverview] = useState<boolean>(false)
    const [formError, setFormError] = useState<FormError>()
    const [formData, setFormData] = useState<FormData>({
        email: "",
        password: "",
        firstname: "",
        lastname: "",
        birthday: "",
        streetAddress: "",
        postalCode: "",
        city: "",
        phoneNumber: ""
    })

    const {data: customerResponse, isError: fetchCustomerError} = useGetCustomerQuery({customerId})
    const [updateCustomer] = useUpdateCustomerMutation()

    useEffect(() => {
        if (customerResponse == null) return
        updateFormState(customerResponse)
    }, [customerResponse]);

    function updateFormState(customer: CustomerDto) {
        const {
            firstname,
            lastname,
            birthday,
            streetAddress,
            postalCode,
            city,
            email,
            phoneNumber,
        } = customer

        const formattedBirthday = moment(birthday).format("YYYY-MM-DD")
        setFormData({
            firstname,
            lastname,
            birthday: formattedBirthday,
            streetAddress,
            postalCode,
            city,
            email,
            phoneNumber,
        })
    }

    function handleChange(event: Event,
                          {name, value}: { name: string, value: string }) {
        setFormData({...formData, [name]: value})
    }

    async function handleSubmit() {
        if (customerId == null) return

        const {
            firstname,
            lastname,
            birthday,
            streetAddress,
            postalCode,
            city,
            email,
            phoneNumber,
        } = formData
        const updatedCustomer = {
            firstname,
            lastname,
            birthday,
            streetAddress,
            postalCode,
            city,
            email,
            phoneNumber,
        }

        try {
            setIsUpdatingCustomer(true)
            await updateCustomer({customerId, customerProfileDto: updatedCustomer}).unwrap()
            setIsUpdatingCustomer(false)
            setRedirectToOverview(true)
        } catch (error) {
            setIsUpdatingCustomer(false)
            const formError = extractFormError(error, context)
            setFormError(formError)
        }
    }

    function renderTextField(key: string, value: string, errorFields: Array<string>, type: string = "text") {
        return (
            <Form.Input
                label={context.get(key)}
                placeholder={context.get(key)}
                type={type}
                name={key}
                value={value}
                onChange={handleChange}
                error={errorFields.includes(key)}
            />
        )
    }

    if (!customerResponse || !formData) {
        return <Loader active/>
    } else if (redirectToOverview) {
        return <Navigate to={`/customers/${customerId}`} push/>
    } else {
        const {
            firstname,
            lastname,
            birthday,
            streetAddress,
            postalCode,
            city,
            email,
            phoneNumber,
        } = formData

        const hasError = !!formError
        const errorFields = formError ? formError.errorFields : []
        const errorMessages = formError ? formError.errorMessages : []

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
                    <Breadcrumb.Divider icon="right angle"/>
                    <Breadcrumb.Section
                        link
                        as={Link}
                        to={`/customers/${customerId ?? ""}`}
                    >
                        {customerResponse &&
                            !fetchCustomerError &&
                            `${customerResponse.firstname} ${customerResponse.lastname}`}
                        {!customerResponse && !fetchCustomerError && "Loading..."}
                        {!customerResponse && fetchCustomerError && "Error!"}
                    </Breadcrumb.Section>
                    <Breadcrumb.Divider icon="right angle"/>
                    <Breadcrumb.Section>Edit Profile</Breadcrumb.Section>
                </Breadcrumb>
                <br/>
                <br/>
                <Form onSubmit={handleSubmit} error={hasError}>
                    <Form.Group widths="equal">
                        {renderTextField(firstnameKey, firstname, errorFields)}
                        {renderTextField(lastnameKey, lastname, errorFields)}
                    </Form.Group>

                    {renderTextField(birthdayKey, birthday, errorFields, "date")}
                    {renderTextField(streetAddressKey, streetAddress, errorFields)}

                    <Form.Group widths="equal">
                        {renderTextField(postalCodeKey, postalCode, errorFields)}
                        {renderTextField(cityKey, city, errorFields)}
                    </Form.Group>

                    {renderTextField(emailKey, email, errorFields)}
                    {renderTextField(phoneNumberKey, phoneNumber, errorFields)}

                    <Button
                        fluid
                        color="blue"
                        disabled={isUpdatingCustomer}
                        loading={isUpdatingCustomer}
                    >
                        Save Changes
                    </Button>

                    <Message error>
                        <Message.Header>Update failed</Message.Header>
                        <Message.List items={errorMessages}/>
                    </Message>
                </Form>
            </Segment>
        )
    }
}

export default EditCustomer
