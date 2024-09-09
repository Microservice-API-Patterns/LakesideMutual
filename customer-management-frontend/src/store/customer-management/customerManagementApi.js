import { customerManagementBase as api } from "./customerManagementBase";
export const addTagTypes = [
    "customer-information-holder",
    "interaction-log-information-holder",
    "notification-information-holder",
    "error-controller",
];
const injectedRtkApi = api
    .enhanceEndpoints({
    addTagTypes,
})
    .injectEndpoints({
    endpoints: (build) => ({
        getCustomer: build.query({
            query: (queryArg) => ({ url: `/customers/${queryArg.customerId}` }),
            providesTags: ["customer-information-holder"],
        }),
        updateCustomer: build.mutation({
            query: (queryArg) => ({
                url: `/customers/${queryArg.customerId}`,
                method: "PUT",
                body: queryArg.customerProfileDto,
            }),
            invalidatesTags: ["customer-information-holder"],
        }),
        getInteractionLog: build.query({
            query: (queryArg) => ({
                url: `/interaction-logs/${queryArg.customerId}`,
            }),
            providesTags: ["interaction-log-information-holder"],
        }),
        acknowledgeInteractions: build.mutation({
            query: (queryArg) => ({
                url: `/interaction-logs/${queryArg.customerId}`,
                method: "PATCH",
                body: queryArg.interactionAcknowledgementDto,
            }),
            invalidatesTags: ["interaction-log-information-holder"],
        }),
        getNotifications: build.query({
            query: () => ({ url: `/notifications` }),
            providesTags: ["notification-information-holder"],
        }),
        getCustomers: build.query({
            query: (queryArg) => ({
                url: `/customers`,
                params: {
                    filter: queryArg.filter,
                    limit: queryArg.limit,
                    offset: queryArg.offset,
                },
            }),
            providesTags: ["customer-information-holder"],
        }),
        handleError3: build.query({
            query: () => ({ url: `/error` }),
            providesTags: ["error-controller"],
        }),
        handleError6: build.mutation({
            query: () => ({ url: `/error`, method: "PUT" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError4: build.mutation({
            query: () => ({ url: `/error`, method: "POST" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError2: build.mutation({
            query: () => ({ url: `/error`, method: "DELETE" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError1: build.mutation({
            query: () => ({ url: `/error`, method: "OPTIONS" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError5: build.mutation({
            query: () => ({ url: `/error`, method: "HEAD" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError: build.mutation({
            query: () => ({ url: `/error`, method: "PATCH" }),
            invalidatesTags: ["error-controller"],
        }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as customerManagementApi };
export const { useGetCustomerQuery, useUpdateCustomerMutation, useGetInteractionLogQuery, useAcknowledgeInteractionsMutation, useGetNotificationsQuery, useGetCustomersQuery, useHandleError3Query, useHandleError6Mutation, useHandleError4Mutation, useHandleError2Mutation, useHandleError1Mutation, useHandleError5Mutation, useHandleErrorMutation, } = injectedRtkApi;
