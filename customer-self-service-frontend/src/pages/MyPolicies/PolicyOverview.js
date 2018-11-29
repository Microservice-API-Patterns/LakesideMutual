// @flow

import React, { Fragment } from "react"
import { Header, Grid, List } from "semantic-ui-react"
import moment from "moment"

export type Props = {
  policy: Policy,
}

function formatDate(str: string): string {
  return moment(str).format("DD. MMM YYYY")
}

export default function PolicyOverview(props: Props) {
  const policy = props.policy
  return (
    <Fragment>
      <Header as="h3">{policy.policyType}</Header>
      <Grid columns={2} celled="internally">
        <Grid.Row>
          <Grid.Column width="4">
            <b>Period</b>
          </Grid.Column>
          <Grid.Column width="12">
            {`${formatDate(policy.policyPeriod.startDate)} – ${formatDate(
              policy.policyPeriod.endDate
            )}`}
          </Grid.Column>
        </Grid.Row>
        <Grid.Row>
          <Grid.Column width="4">
            <b>Insurance Premium</b>
          </Grid.Column>
          <Grid.Column width="12">
            {`${policy.insurancePremium.amount} ${
              policy.insurancePremium.currency
            }`}
          </Grid.Column>
        </Grid.Row>
        <Grid.Row>
          <Grid.Column width="4">
            <b>Policy Limit</b>
          </Grid.Column>
          <Grid.Column width="12">
            {`${policy.policyLimit.amount} ${policy.policyLimit.currency}`}
          </Grid.Column>
        </Grid.Row>
        <Grid.Row>
          <Grid.Column width="4">
            <b>Insuring Agreement</b>
          </Grid.Column>
          <Grid.Column width="12">
            {policy.insuringAgreement.agreementItems.length === 0 && (
              <span>N/A</span>
            )}
            <List divided relaxed>
              {policy.insuringAgreement.agreementItems.map((item, index) => (
                <List.Item key={index}>
                  <List.Content>
                    <List.Header>{item.title}</List.Header>
                    <List.Description>{item.description}</List.Description>
                  </List.Content>
                </List.Item>
              ))}
            </List>
          </Grid.Column>
        </Grid.Row>
      </Grid>
    </Fragment>
  )
}
