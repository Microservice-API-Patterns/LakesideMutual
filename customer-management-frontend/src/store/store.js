// @flow

import {customerManagementApi} from "./customerManagementApi";
import {configureStore} from "@reduxjs/toolkit";

// See https://redux-toolkit.js.org/rtk-query/overview#configure-the-store
export const store = configureStore({
    reducer: { [customerManagementApi.reducerPath]: customerManagementApi.reducer },
    middleware: (getDefaultMiddleware) => 
        getDefaultMiddleware()
            .concat(customerManagementApi.middleware)
})


/*const configureStore = () =>
  createStore(
      { // See https://redux-toolkit.js.org/rtk-query/overview#configure-the-store
          [customerManagementApi.reducerPath]: customerManagementApi.reducer 
      },
    {},
    compose(
      applyMiddleware(thunkMiddleware),
      window.__REDUX_DEVTOOLS_EXTENSION__
        ? window.__REDUX_DEVTOOLS_EXTENSION__()
        : f => f
    )
  )*/

// export default configureStore
