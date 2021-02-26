// @flow

import * as React from "react"
import { Route, Redirect } from "react-router-dom"

export type Props = {
  isAuthenticated: boolean,
  render: () => React.Node,
}

function PrivateRoute({ isAuthenticated, render, ...rest }: Props) {
  if (isAuthenticated) {
    // if the user is authenticated, just render the component
    return <Route {...rest} render={props => render()} />
  } else {
    // otherwise redirect to the login page
    return (
      <Route
        {...rest}
        render={props => (
          <Redirect
            to={{ pathname: "/login", state: { from: props.location } }}
          />
        )}
      />
    )
  }
}

export default PrivateRoute
