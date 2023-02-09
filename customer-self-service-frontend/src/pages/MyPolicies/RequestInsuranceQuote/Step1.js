// @flow

import React, { Fragment } from "react"
import { Button, Form, Header, Checkbox } from "semantic-ui-react"

type FormData = {
  contactAddressStreetAddress: string,
  contactAddressPostalCode: string,
  contactAddressCity: string,
  billingAddressStreetAddress: ?string,
  billingAddressPostalCode: ?string,
  billingAddressCity: ?string,
}

export type Props = {
  onNext: () => void,
  onChange: (FormData) => void,
  formData: FormData,
}

const contactAddressStreetAddressKey = "contactAddressStreetAddress"
const contactAddressPostalCodeKey = "contactAddressPostalCode"
const contactAddressCityKey = "contactAddressCity"
const billingAddressStreetAddressKey = "billingAddressStreetAddress"
const billingAddressPostalCodeKey = "billingAddressPostalCode"
const billingAddressCityKey = "billingAddressCity"

const context = new Map([
  [contactAddressStreetAddressKey, "Street Address"],
  [contactAddressPostalCodeKey, "Postal Code"],
  [contactAddressCityKey, "City"],
  [billingAddressStreetAddressKey, "Street Address"],
  [billingAddressPostalCodeKey, "Postal Code"],
  [billingAddressCityKey, "City"],
])

export default class Step1 extends React.Component<Props> {
  handleChange = (
    e: Event,
    { name, value }: { name: string, value: string }
  ) => {
    const formData = { ...this.props.formData, [name]: value }
    this.props.onChange(formData)
  }

  renderTextField(
    key: string,
    value: string,
    errorFields: Array<string>,
    onBlur: ?(e: Event) => Promise<void> = null
  ) {
    return (
      <Form.Input
        label={context.get(key)}
        placeholder={context.get(key)}
        type="text"
        name={key}
        value={value}
        onChange={this.handleChange}
        onBlur={onBlur}
        error={errorFields.includes(key)}
      />
    )
  }

  render() {
    const {
      contactAddressStreetAddress,
      contactAddressPostalCode,
      contactAddressCity,
      billingAddressStreetAddress,
      billingAddressPostalCode,
      billingAddressCity,
    } = this.props.formData

    const billingAddressIsSameAsContactAddress =
      billingAddressStreetAddress == null &&
      billingAddressPostalCode == null &&
      billingAddressCity == null

    const errorFields = []
    return (
      <Fragment>
        <Form>
          <Header as="h3">Contact Address</Header>
          {this.renderTextField(
            contactAddressStreetAddressKey,
            contactAddressStreetAddress,
            errorFields
          )}

          <Form.Group widths="equal">
            {this.renderTextField(
              contactAddressPostalCodeKey,
              contactAddressPostalCode,
              errorFields
            )}
            {this.renderTextField(
              contactAddressCityKey,
              contactAddressCity,
              errorFields
            )}
          </Form.Group>

          <Header as="h3">Billing Address</Header>
          <Checkbox
            label="Same as Contact Address"
            checked={billingAddressIsSameAsContactAddress}
            onChange={(props, { checked }) => {
              if (checked) {
                this.props.onChange({
                  contactAddressStreetAddress:
                    this.props.formData.contactAddressStreetAddress,
                  contactAddressPostalCode:
                    this.props.formData.contactAddressPostalCode,
                  contactAddressCity: this.props.formData.contactAddressCity,
                  billingAddressStreetAddress: null,
                  billingAddressPostalCode: null,
                  billingAddressCity: null,
                })
              } else {
                this.props.onChange({
                  contactAddressStreetAddress:
                    this.props.formData.contactAddressStreetAddress,
                  contactAddressPostalCode:
                    this.props.formData.contactAddressPostalCode,
                  contactAddressCity: this.props.formData.contactAddressCity,
                  billingAddressStreetAddress: "",
                  billingAddressPostalCode: "",
                  billingAddressCity: "",
                })
              }
            }}
          />
          <br />
          <br />

          {billingAddressStreetAddress != null &&
            billingAddressPostalCode != null &&
            billingAddressCity != null && (
              <Fragment>
                {this.renderTextField(
                  billingAddressStreetAddressKey,
                  billingAddressStreetAddress,
                  errorFields
                )}

                <Form.Group widths="equal">
                  {this.renderTextField(
                    billingAddressPostalCodeKey,
                    billingAddressPostalCode,
                    errorFields
                  )}
                  {this.renderTextField(
                    billingAddressCityKey,
                    billingAddressCity,
                    errorFields
                  )}
                </Form.Group>
              </Fragment>
            )}
        </Form>
        <Button
          floated="right"
          labelPosition="right"
          icon="right chevron"
          content="Next"
          onClick={this.props.onNext}
        />
      </Fragment>
    )
  }
}
