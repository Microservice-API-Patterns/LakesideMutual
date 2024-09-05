// @flow

import React from "react"
import {BrowserRouter, Route, Routes} from "react-router-dom"
import {Container, Message} from "semantic-ui-react"
import SiteHeader from "./components/SiteHeader"
import MenuBar from "./components/MenuBar"

import {Provider} from "react-redux"
import {store} from "./store/store"
import {customerManagementBackend, customerSelfServiceBackend, policyManagementFrontend,} from "./config"
import Home from "./pages/Home";

export default class App extends React.Component<{}> {
    render() {
        return (
            <Provider store={store}>
                <BrowserRouter>
                    <Container text>
                        <SiteHeader/>

                        <MenuBar/>

            <Routes>
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
            </Routes>
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
                <br />
                REACT_APP_POLICY_MANAGEMENT_FRONTEND: {policyManagementFrontend}
              </p>
            </Message>
          </Container>
        </BrowserRouter>
      </Provider>
    )
  }
}
