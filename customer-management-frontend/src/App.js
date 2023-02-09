// @flow

import React from "react"
import { BrowserRouter, Route, Switch } from "react-router-dom"
import { Container, Message } from "semantic-ui-react"

import Home from "./pages/Home"
import NewCustomer from "./pages/NewCustomer"
import CustomerDetail from "./pages/CustomerDetail"
import EditCustomer from "./pages/EditCustomer"
import SiteHeader from "./components/SiteHeader"
import MenuBar from "./components/MenuBar"

import { Provider } from "react-redux"
import configureStore from "./redux/configureStore"
import { customerSelfServiceBackend, customerManagementBackend } from "./config"

const store = configureStore()

export default class App extends React.Component<{}> {
  render() {
    return (
      <Provider store={store}>
        <BrowserRouter>
          <Container text>
            <SiteHeader />

            <MenuBar />

            <Switch>
              <Route exact path="/" render={(props) => <Home {...props} />} />
              <Route
                exact
                path="/customers/new"
                render={(props) => <NewCustomer {...props} />}
              />
              <Route
                exact
                path="/customers/:customerId"
                render={(props) => <CustomerDetail {...props} />}
              />
              <Route
                exact
                path="/customers/:customerId/edit_profile"
                render={(props) => <EditCustomer {...props} />}
              />
            </Switch>
            <Message style={{ color: "#BBB" }} size="mini">
              <Message.Header>
                Environment Variable Debug Information
              </Message.Header>
              <p>
                REACT_APP_CUSTOMER_SELF_SERVICE_BACKEND:{" "}
                {customerSelfServiceBackend}
                <br />
                REACT_APP_CUSTOMER_MANAGEMENT_BACKEND:{" "}
                {customerManagementBackend}
              </p>
            </Message>
          </Container>
        </BrowserRouter>
      </Provider>
    )
  }
}
