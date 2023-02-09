// @flow
import React, { Fragment } from "react"
import { Button, Form, Input } from "semantic-ui-react"

type FormData = {
  startDate: ?string,
  insuranceType: ?string,
  deductible: ?string,
}

export type Props = {
  onNext: () => void,
  onPrev: () => void,
  onChange: (FormData) => void,
  formData: FormData,
}

const insuranceTypeValues = [
  { key: "life", text: "Life Insurance", value: "Life Insurance" },
  { key: "home", text: "Home Insurance", value: "Home Insurance" },
  { key: "health", text: "Health Insurance", value: "Health Insurance" },
  { key: "car", text: "Car Insurance", value: "Car Insurance" },
]

const deductibleValues = [
  { key: "300", text: "300 CHF", value: "300 CHF" },
  { key: "500", text: "500 CHF", value: "500 CHF" },
  { key: "1000", text: "1000 CHF", value: "1000 CHF" },
  { key: "1500", text: "1500 CHF", value: "1500 CHF" },
  { key: "2000", text: "2000 CHF", value: "2000 CHF" },
  { key: "2500", text: "2500 CHF", value: "2500 CHF" },
]

export default class Step2 extends React.Component<Props> {
  render() {
    const { startDate, insuranceType, deductible } = this.props.formData
    return (
      <Fragment>
        <Form>
          <Form.Field>
            <label>Start Date</label>
            <Input
              type="date"
              value={startDate}
              onChange={(props, context) =>
                this.props.onChange({
                  startDate: context.value,
                  insuranceType,
                  deductible,
                })
              }
            />
          </Form.Field>
          <Form.Select
            label="Insurance Type"
            value={insuranceType}
            options={insuranceTypeValues}
            placeholder="Insurance Type"
            onChange={(props, context) =>
              this.props.onChange({
                startDate,
                insuranceType: context.value,
                deductible,
              })
            }
          />
          <Form.Select
            label="Deductible"
            value={deductible}
            options={deductibleValues}
            placeholder="Deductible"
            onChange={(props, context) =>
              this.props.onChange({
                startDate,
                insuranceType,
                deductible: context.value,
              })
            }
          />
        </Form>
        <br />
        <Button
          floated="left"
          labelPosition="left"
          icon="left chevron"
          content="Previous"
          onClick={this.props.onPrev}
        />
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
