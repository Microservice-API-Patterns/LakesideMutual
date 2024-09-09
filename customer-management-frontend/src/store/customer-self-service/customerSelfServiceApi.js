import { customerSelfServiceBase as api } from "./customerSelfServiceBase";
const injectedRtkApi = api.injectEndpoints({
    endpoints: (build) => ({
        changeAddress: build.mutation({
            query: (queryArg) => ({
                url: `/customers/${queryArg.customerId}/address`,
                method: "PUT",
                body: queryArg.addressDto,
            }),
        }),
        getInsuranceQuoteRequests: build.query({
            query: () => ({ url: `/insurance-quote-requests` }),
        }),
        createInsuranceQuoteRequest: build.mutation({
            query: (queryArg) => ({
                url: `/insurance-quote-requests`,
                method: "POST",
                body: queryArg.insuranceQuoteRequestDto,
            }),
        }),
        registerCustomer: build.mutation({
            query: (queryArg) => ({
                url: `/customers`,
                method: "POST",
                body: queryArg.customerRegistrationRequestDto,
            }),
        }),
        authenticationRequest: build.mutation({
            query: (queryArg) => ({
                url: `/auth`,
                method: "POST",
                body: queryArg.authenticationRequestDto,
            }),
        }),
        signupUser: build.mutation({
            query: (queryArg) => ({
                url: `/auth/signup`,
                method: "POST",
                body: queryArg.signupRequestDto,
            }),
        }),
        respondToInsuranceQuote: build.mutation({
            query: (queryArg) => ({
                url: `/insurance-quote-requests/${queryArg.id}`,
                method: "PATCH",
                body: queryArg.insuranceQuoteResponseDto,
            }),
        }),
        getCurrentUser: build.query({
            query: () => ({ url: `/user` }),
        }),
        getInsuranceQuoteRequest: build.query({
            query: (queryArg) => ({
                url: `/insurance-quote-requests/${queryArg.insuranceQuoteRequestId}`,
            }),
        }),
        getCustomer: build.query({
            query: (queryArg) => ({ url: `/customers/${queryArg.customerId}` }),
        }),
        getInsuranceQuoteRequests1: build.query({
            query: (queryArg) => ({
                url: `/customers/${queryArg.customerId}/insurance-quote-requests`,
            }),
        }),
        getCitiesForPostalCode: build.query({
            query: (queryArg) => ({ url: `/cities/${queryArg.postalCode}` }),
        }),
        handleError2: build.query({
            query: () => ({ url: `/error` }),
        }),
        handleError1: build.mutation({
            query: () => ({ url: `/error`, method: "PUT" }),
        }),
        handleError5: build.mutation({
            query: () => ({ url: `/error`, method: "POST" }),
        }),
        handleError4: build.mutation({
            query: () => ({ url: `/error`, method: "DELETE" }),
        }),
        handleError6: build.mutation({
            query: () => ({ url: `/error`, method: "OPTIONS" }),
        }),
        handleError: build.mutation({
            query: () => ({ url: `/error`, method: "HEAD" }),
        }),
        handleError3: build.mutation({
            query: () => ({ url: `/error`, method: "PATCH" }),
        }),
    }),
    overrideExisting: false,
});
export { injectedRtkApi as customerSelfServiceApi };
export const { useChangeAddressMutation, useGetInsuranceQuoteRequestsQuery, useCreateInsuranceQuoteRequestMutation, useRegisterCustomerMutation, useAuthenticationRequestMutation, useSignupUserMutation, useRespondToInsuranceQuoteMutation, useGetCurrentUserQuery, useGetInsuranceQuoteRequestQuery, useGetCustomerQuery, useGetInsuranceQuoteRequests1Query, useGetCitiesForPostalCodeQuery, useHandleError2Query, useHandleError1Mutation, useHandleError5Mutation, useHandleError4Mutation, useHandleError6Mutation, useHandleErrorMutation, useHandleError3Mutation, } = injectedRtkApi;
