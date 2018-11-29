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
      window.devToolsExtension ? window.devToolsExtension() : f => f
    )
  )

export default configureStore
