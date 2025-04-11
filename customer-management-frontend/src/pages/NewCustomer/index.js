// @flow

import React, {useState} from "react"
import {Breadcrumb, Button, Form, Header, Message, Segment,} from "semantic-ui-react"
import {Link} from "react-router-dom"
import errorMessages from "../../errorMessages"
import {Navigate} from "react-router";
import {
    useAuthenticationRequestMutation,
    useRegisterCustomerMutation,
    useSignupUserMutation
} from "../../store/customer-self-service/customerSelfServiceApi";

function extractFormError(error, context: Map<string, string>): ?FormError {
    if (error.status === 409) {
        return {
            errorFields: ["email"],
            errorMessages: ["This email address has already been taken."],
        }
    } else if (error.errors != null) {
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

export type Props = {
    signupUser: (email: string, password: string) => Promise<void>,
    loginUser: (email: string, password: string) => Promise<string>,
    createCustomer: (
        firstname: string,
        lastname: string,
        birthday: string,
        streetAddress: string,
        postalCode: string,
        city: string,
        phoneNumber: string
    ) => Promise<void>,
}

const emailKey = "email"
const passwordKey = "password"
const firstnameKey = "firstname"
const lastnameKey = "lastname"
const birthdayKey = "birthday"
const streetAddressKey = "streetAddress"
const postalCodeKey = "postalCode"
const cityKey = "city"
const phoneNumberKey = "phoneNumber"

const context = new Map([
    [emailKey, "Email Address"],
    [passwordKey, "Password"],
    [firstnameKey, "First Name"],
    [lastnameKey, "Last Name"],
    [birthdayKey, "Date of Birth"],
    [streetAddressKey, "Street Address"],
    [postalCodeKey, "Postal Code"],
    [cityKey, "City"],
    [phoneNumberKey, "Phone Number"],
])

type FormData = {
    email: string,
    password: string,
    firstname: string,
    lastname: string,
    birthday: string,
    streetAddress: string,
    postalCode: string,
    city: string,
    phoneNumber: string
}

function NewCustomer(): React$Element {
    const [isCreatingCustomer, setIsCreatingCustomer] = useState<boolean>(false)
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

    const [signupUser] = useSignupUserMutation()
    const [loginUser] = useAuthenticationRequestMutation()
    const [createCustomer] = useRegisterCustomerMutation()

    function handleChange(event: Event,
                          {name, value}: { name: string, value: string }) {
        setFormData({...formData, [name]: value})
    }

    async function handleSubmit() {
        const {
            email,
            password,
            firstname,
            lastname,
            birthday,
            streetAddress,
            postalCode,
            city,
            phoneNumber,
        } = formData

        try {
            setIsCreatingCustomer(true)
            await signupUser({signupRequestDto: {email, password}}).unwrap()
            const response = await loginUser({authenticationRequestDto: {email, password}}).unwrap()
            localStorage.setItem("token", response.token)
            await createCustomer({
                customerRegistrationRequestDto: {
                    firstname,
                    lastname,
                    birthday,
                    streetAddress,
                    postalCode,
                    city,
                    phoneNumber
                }
            }).unwrap()
            setIsCreatingCustomer(false)
            setRedirectToOverview(true)
        } catch (error) {
            setIsCreatingCustomer(false)
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

    const {
        email,
        password,
        firstname,
        lastname,
        birthday,
        streetAddress,
        postalCode,
        city,
        phoneNumber
    } = formData

    const hasEmptyFields =
        !email ||
        !password ||
        !firstname ||
        !lastname ||
        !birthday ||
        !streetAddress ||
        !postalCode ||
        !city ||
        !phoneNumber
    const hasError = !!formError
    const errorFields = formError ? formError.errorFields : []
    const errorMessages = formError ? formError.errorMessages : []

    if (redirectToOverview) {
        return <Navigate to="/" push/>
    } else {
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
                    <Breadcrumb.Section>New Customer</Breadcrumb.Section>
                </Breadcrumb>
                <br/>
                <br/>
                <Form onSubmit={handleSubmit} error={hasError}>
                    <Header>Login Credentials</Header>
                    <Form.Group widths="equal">
                        {renderTextField(emailKey, email, errorFields)}
                        {renderTextField(
                            passwordKey,
                            password,
                            errorFields,
                            "password"
                        )}
                    </Form.Group>

                    <Header>Customer Profile</Header>
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

                    {renderTextField(phoneNumberKey, phoneNumber, errorFields)}

                    <Button
                        fluid
                        color="blue"
                        disabled={hasEmptyFields || isCreatingCustomer}
                        loading={isCreatingCustomer}>
                        Create Customer
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

export default NewCustomer