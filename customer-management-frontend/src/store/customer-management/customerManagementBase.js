// Or from '@reduxjs/toolkit/query' if not using the auto-generated hooks
import { createApi, fetchBaseQuery } from '@reduxjs/toolkit/query/react';
import { customerManagementBackend } from "./../../config";
// initialize an empty api service that we'll inject endpoints into later as needed
export const customerManagementBase = createApi({
    reducerPath: "customerManagementApi",
    baseQuery: fetchBaseQuery({
        baseUrl: customerManagementBackend,
        prepareHeaders: (headers, _) => {
            const token = localStorage.getItem("token");
            if (token != null) {
                headers.set('X-Auth-Token', token);
            }
        },
    }),
    endpoints: () => ({}),
});
