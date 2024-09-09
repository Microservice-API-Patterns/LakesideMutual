import { customerManagementBase as api } from "./customerManagementBase";
const injectedRtkApi = api.injectEndpoints({
    endpoints: (build) => ({
        getCustomer: build.query({
            query: (queryArg) => ({ url: `/customers/${queryArg.customerId}` }),
        }),
        updateCustomer: build.mutation({
            query: (queryArg) => ({
                url: `/customers/${queryArg.customerId}`,
                method: "PUT",
                body: queryArg.customerProfileDto,
            }),
        }),
        getInteractionLog: build.query({
            query: (queryArg) => ({
                url: `/interaction-logs/${queryArg.customerId}`,
            }),
        }),
        acknowledgeInteractions: build.mutation({
            query: (queryArg) => ({
                url: `/interaction-logs/${queryArg.customerId}`,
                method: "PATCH",
                body: queryArg.interactionAcknowledgementDto,
            }),
        }),
        getNotifications: build.query({
            query: () => ({ url: `/notifications` }),
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
        }),
        handleError6: build.query({
            query: () => ({ url: `/error` }),
        }),
        handleError: build.mutation({
            query: () => ({ url: `/error`, method: "PUT" }),
        }),
        handleError5: build.mutation({
            query: () => ({ url: `/error`, method: "POST" }),
        }),
        handleError2: build.mutation({
            query: () => ({ url: `/error`, method: "DELETE" }),
        }),
        handleError3: build.mutation({
            query: () => ({ url: `/error`, method: "OPTIONS" }),
        }),
        handleError4: build.mutation({
            query: () => ({ url: `/error`, method: "HEAD" }),
        }),
        handleError1: build.mutation({
            query: () => ({ url: `/error`, method: "PATCH" }),
        }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as customerManagementApi };
export const { useGetCustomerQuery, useUpdateCustomerMutation, useGetInteractionLogQuery, useAcknowledgeInteractionsMutation, useGetNotificationsQuery, useGetCustomersQuery, useHandleError6Query, useHandleErrorMutation, useHandleError5Mutation, useHandleError2Mutation, useHandleError3Mutation, useHandleError4Mutation, useHandleError1Mutation, } = injectedRtkApi;
