// @flow

import React from "react"
import { BrowserRouter, Route } from "react-router-dom"
import { Container } from "semantic-ui-react"
import { Provider, Subscribe } from "unstated"

import Home from "./pages/Home"
import Login from "./pages/Login"
import Signup from "./pages/Signup"
import CompleteRegistration from "./pages/CompleteRegistration"
import MyPolicies from "./pages/MyPolicies"
import MyProfile from "./pages/MyProfile"
import Contact from "./pages/Contact"
import SiteHeader from "./components/SiteHeader"
import MenuBar from "./components/MenuBar"
import PrivateRoute from "./components/PrivateRoute"
import Store from "./Store"

const store = new Store()

export default class App extends React.Component<{}> {
  render() {
    return (
      <Provider inject={[store]}>
        <Subscribe to={[Store]}>
          {store => {
            const {
              isAuthenticated,
              user,
              customer,
              isLoggingIn,
              loginError,
              isSigningUp,
              signupError,
              policy,
              interactionLog,
              isCompletingRegistration,
              registrationError,
              isFetchingActivePolicy,
              policyFetchError,
              isUpdatingAddress,
              addressUpdateError,
              isFetchingInteractionLog,
              interactionLogFetchError,
            } = store.state
            return (
              <BrowserRouter>
                <Container text>
                  <SiteHeader isAuthenticated={isAuthenticated} />

                  <MenuBar
                    actions={store.getActions()}
                    isAuthenticated={isAuthenticated}
                    user={user}
                    customer={customer}
                  />

                  <Route
                    exact
                    path="/"
                    render={props => (
                      <Home
                        actions={store.getActions()}
                        isAuthenticated={isAuthenticated}
                      />
                    )}
                  />

                  <Route
                    path="/login"
                    render={props => (
                      <Login
                        {...props}
                        actions={store.getActions()}
                        isLoggingIn={isLoggingIn}
                        loginError={loginError}
                      />
                    )}
                  />

                  <Route
                    path="/signup"
                    render={props => {
                      return (
                        <Signup
                          actions={store.getActions()}
                          isSigningUp={isSigningUp}
                          signupError={signupError}
                        />
                      )
                    }}
                  />

                  <PrivateRoute
                    path="/complete-registration"
                    isAuthenticated={isAuthenticated}
                    render={() => (
                      <CompleteRegistration
                        actions={store.getActions()}
                        user={user}
                        customer={customer}
                        isCompletingRegistration={isCompletingRegistration}
                        registrationError={registrationError}
                      />
                    )}
                  />

                  <PrivateRoute
                    path="/policies"
                    isAuthenticated={isAuthenticated}
                    render={() => (
                      <MyPolicies
                        actions={store.getActions()}
                        user={user}
                        customer={customer}
                        policy={policy}
                        isFetchingActivePolicy={isFetchingActivePolicy}
                        policyFetchError={policyFetchError}
                      />
                    )}
                  />

                  <PrivateRoute
                    path="/profile"
                    isAuthenticated={isAuthenticated}
                    render={() => (
                      <MyProfile
                        actions={store.getActions()}
                        user={user}
                        customer={customer}
                        isUpdatingAddress={isUpdatingAddress}
                        addressUpdateError={addressUpdateError}
                      />
                    )}
                  />

                  <PrivateRoute
                    path="/contact"
                    isAuthenticated={isAuthenticated}
                    render={() => (
                      <Contact
                        actions={store.getActions()}
                        user={user}
                        customer={customer}
                        interactionLog={interactionLog}
                        isFetchingInteractionLog={isFetchingInteractionLog}
                        interactionLogFetchError={interactionLogFetchError}
                      />
                    )}
                  />
                </Container>
              </BrowserRouter>
            )
          }}
        </Subscribe>
      </Provider>
    )
  }
}
