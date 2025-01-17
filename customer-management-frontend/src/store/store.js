// @flow

import {customerManagementApi} from "./customer-management/customerManagementApi";
import {configureStore} from "@reduxjs/toolkit";
import {customerSelfServiceApi} from "./customer-self-service/customerSelfServiceApi";

// See https://redux-toolkit.js.org/rtk-query/overview#configure-the-store
export const store = configureStore({
    reducer: {
        [customerManagementApi.reducerPath]: customerManagementApi.reducer,
        [customerSelfServiceApi.reducerPath]: customerSelfServiceApi.reducer
    },
    middleware: (getDefaultMiddleware) =>
        getDefaultMiddleware()
            .concat(customerManagementApi.middleware)
            .concat(customerSelfServiceApi.middleware)
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
