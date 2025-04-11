// Or from '@reduxjs/toolkit/query' if not using the auto-generated hooks
import {createApi, fetchBaseQuery} from '@reduxjs/toolkit/query/react'
import {customerSelfServiceBackend} from "./../../config"

// initialize an empty api service that we'll inject endpoints into later as needed
export const customerSelfServiceBase = createApi({
    reducerPath: "customerSelfServiceApi",
    baseQuery: fetchBaseQuery({
        baseUrl: customerSelfServiceBackend,
        prepareHeaders: (headers, _) => {
            const token = localStorage.getItem("token")
            if (token != null) {
                headers.set('X-Auth-Token', token)
            }
        },
    }),
    endpoints: () => ({}),
})