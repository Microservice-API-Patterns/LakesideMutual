// @flow

import { applyMiddleware, compose, createStore } from "redux"
import thunkMiddleware from "redux-thunk"

import reducers from "./reducers"

const configureStore = () =>
  createStore(
    reducers,
    {},
    compose(
      applyMiddleware(thunkMiddleware),
      window.__REDUX_DEVTOOLS_EXTENSION__
        ? window.__REDUX_DEVTOOLS_EXTENSION__()
        : f => f
    )
  )

export default configureStore
