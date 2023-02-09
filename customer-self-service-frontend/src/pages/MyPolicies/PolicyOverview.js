// @flow

import React, { Fragment } from "react"
import { Header, Grid, List, Button, Modal } from "semantic-ui-react"
import moment from "moment"

export type Props = {
  policy: Policy,
}

type State = {
  showInfoModal: boolean,
  infoType: string,
}

function formatDate(str: string): string {
  return moment(str).format("DD. MMM YYYY")
}

export default class PolicyOverview extends React.Component<Props, State> {
  state = {
    showInfoModal: false,
    infoType: "",
  }

  showInfoModal = (infoType: string) => {
    this.setState({
      showInfoModal: true,
      infoType,
    })
  }

  hideInfoModal = () => {
    this.setState({
      showInfoModal: false,
    })
  }

  render() {
    const policy = this.props.policy
    return (
      <Fragment>
        <Modal open={this.state.showInfoModal} onClose={this.hideInfoModal}>
          <Modal.Header>
            {this.state.infoType === "deductible" && "Deductible"}
            {this.state.infoType === "insurancePremium" && "Insurance Premium"}
            {this.state.infoType === "policyLimit" && "Policy Limit"}
          </Modal.Header>
          <Modal.Content>
            <Modal.Description>
              {this.state.infoType === "deductible" && (
                <p>
                  The deductible is the amount paid out of pocket by the policy
                  holder (i.e., the customer) before an insurance provider
                  (i.e., Lakeside Mutual) will pay any expenses.
                </p>
              )}
              {this.state.infoType === "insurancePremium" && (
                <p>
                  An insurance premium is the amount of money that the policy
                  holder (i.e., the customer) must pay every month for an
                  insurance policy.
                </p>
              )}
              {this.state.infoType === "policyLimit" && (
                <p>
                  A policy limit refers to the maximum monetary amount that an
                  insurance provider (i.e., Lakeside Mutual) will pay out in
                  relation to an insurance policy claim.
                </p>
              )}
            </Modal.Description>
          </Modal.Content>
          <Modal.Actions>
            <Button color="green" onClick={this.hideInfoModal}>
              OK
            </Button>
          </Modal.Actions>
        </Modal>

        <Grid padded style={{ border: "2px solid #aaaaaa" }}>
          <Grid.Row>
            <Grid.Column width="16">
              <Grid columns={2}>
                <Grid.Row>
                  <Grid.Column width="16">
                    <Header as="h3">{policy.policyType}</Header>
                  </Grid.Column>
                </Grid.Row>
                <Grid.Row>
                  <Grid.Column width="5">
                    <b>Period</b>
                  </Grid.Column>
                  <Grid.Column width="11">
                    {`${formatDate(
                      policy.policyPeriod.startDate
                    )} – ${formatDate(policy.policyPeriod.endDate)}`}
                  </Grid.Column>
                </Grid.Row>
                <Grid.Row>
                  <Grid.Column width="5">
                    <b>Deductible&nbsp;&nbsp;</b>
                    <Button
                      icon="info"
                      size="mini"
                      circular
                      compact
                      onClick={() => this.showInfoModal("deductible")}
                    />
                  </Grid.Column>
                  <Grid.Column width="11">
                    {`${policy.deductible.amount} ${policy.deductible.currency}`}
                  </Grid.Column>
                </Grid.Row>
                <Grid.Row>
                  <Grid.Column width="5">
                    <b>Insurance Premium&nbsp;&nbsp;</b>
                    <Button
                      icon="info"
                      size="mini"
                      circular
                      compact
                      onClick={() => this.showInfoModal("insurancePremium")}
                    />
                  </Grid.Column>
                  <Grid.Column width="11">
                    {`${policy.insurancePremium.amount} ${policy.insurancePremium.currency}`}
                  </Grid.Column>
                </Grid.Row>
                <Grid.Row>
                  <Grid.Column width="5">
                    <b>Policy Limit&nbsp;&nbsp;</b>
                    <Button
                      icon="info"
                      size="mini"
                      circular
                      compact
                      onClick={() => this.showInfoModal("policyLimit")}
                    />
                  </Grid.Column>
                  <Grid.Column width="11">
                    {`${policy.policyLimit.amount} ${policy.policyLimit.currency}`}
                  </Grid.Column>
                </Grid.Row>
                <Grid.Row>
                  <Grid.Column width="5">
                    <b>Insuring Agreement</b>
                  </Grid.Column>
                  <Grid.Column width="11">
                    {policy.insuringAgreement.agreementItems.length === 0 && (
                      <span>N/A</span>
                    )}
                    <List divided relaxed>
                      {policy.insuringAgreement.agreementItems.map(
                        (item, index) => (
                          <List.Item key={index}>
                            <List.Content>
                              <List.Header>{item.title}</List.Header>
                              <List.Description>
                                {item.description}
                              </List.Description>
                            </List.Content>
                          </List.Item>
                        )
                      )}
                    </List>
                  </Grid.Column>
                </Grid.Row>
              </Grid>
            </Grid.Column>
          </Grid.Row>
        </Grid>
      </Fragment>
    )
  }
}
