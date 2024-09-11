import { customerSelfServiceBase as api } from "./customerSelfServiceBase";
export const addTagTypes = [
    "customer-information-holder",
    "insurance-quote-request-coordinator",
    "authentication-controller",
    "user-information-holder",
    "city-reference-data-holder",
    "error-controller",
];
const injectedRtkApi = api
    .enhanceEndpoints({
    addTagTypes,
})
    .injectEndpoints({
    endpoints: (build) => ({
        changeAddress: build.mutation({
            query: (queryArg) => ({
                url: `/customers/${queryArg.customerId}/address`,
                method: "PUT",
                body: queryArg.addressDto,
            }),
            invalidatesTags: ["customer-information-holder"],
        }),
        getInsuranceQuoteRequests: build.query({
            query: () => ({ url: `/insurance-quote-requests` }),
            providesTags: ["insurance-quote-request-coordinator"],
        }),
        createInsuranceQuoteRequest: build.mutation({
            query: (queryArg) => ({
                url: `/insurance-quote-requests`,
                method: "POST",
                body: queryArg.insuranceQuoteRequestDto,
            }),
            invalidatesTags: ["insurance-quote-request-coordinator"],
        }),
        registerCustomer: build.mutation({
            query: (queryArg) => ({
                url: `/customers`,
                method: "POST",
                body: queryArg.customerRegistrationRequestDto,
            }),
            invalidatesTags: ["customer-information-holder"],
        }),
        authenticationRequest: build.mutation({
            query: (queryArg) => ({
                url: `/auth`,
                method: "POST",
                body: queryArg.authenticationRequestDto,
            }),
            invalidatesTags: ["authentication-controller"],
        }),
        signupUser: build.mutation({
            query: (queryArg) => ({
                url: `/auth/signup`,
                method: "POST",
                body: queryArg.signupRequestDto,
            }),
            invalidatesTags: ["authentication-controller"],
        }),
        respondToInsuranceQuote: build.mutation({
            query: (queryArg) => ({
                url: `/insurance-quote-requests/${queryArg.id}`,
                method: "PATCH",
                body: queryArg.insuranceQuoteResponseDto,
            }),
            invalidatesTags: ["insurance-quote-request-coordinator"],
        }),
        getCurrentUser: build.query({
            query: () => ({ url: `/user` }),
            providesTags: ["user-information-holder"],
        }),
        getInsuranceQuoteRequest: build.query({
            query: (queryArg) => ({
                url: `/insurance-quote-requests/${queryArg.insuranceQuoteRequestId}`,
            }),
            providesTags: ["insurance-quote-request-coordinator"],
        }),
        getCustomer: build.query({
            query: (queryArg) => ({ url: `/customers/${queryArg.customerId}` }),
            providesTags: ["customer-information-holder"],
        }),
        getInsuranceQuoteRequests1: build.query({
            query: (queryArg) => ({
                url: `/customers/${queryArg.customerId}/insurance-quote-requests`,
            }),
            providesTags: ["customer-information-holder"],
        }),
        getCitiesForPostalCode: build.query({
            query: (queryArg) => ({ url: `/cities/${queryArg.postalCode}` }),
            providesTags: ["city-reference-data-holder"],
        }),
        handleError4: build.query({
            query: () => ({ url: `/error` }),
            providesTags: ["error-controller"],
        }),
        handleError1: build.mutation({
            query: () => ({ url: `/error`, method: "PUT" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError3: build.mutation({
            query: () => ({ url: `/error`, method: "POST" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError5: build.mutation({
            query: () => ({ url: `/error`, method: "DELETE" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError2: build.mutation({
            query: () => ({ url: `/error`, method: "OPTIONS" }),
            invalidatesTags: ["error-controller"],
        }),
        handleError6: build.mutation({
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
export { injectedRtkApi as customerSelfServiceApi };
export const { useChangeAddressMutation, useGetInsuranceQuoteRequestsQuery, useCreateInsuranceQuoteRequestMutation, useRegisterCustomerMutation, useAuthenticationRequestMutation, useSignupUserMutation, useRespondToInsuranceQuoteMutation, useGetCurrentUserQuery, useGetInsuranceQuoteRequestQuery, useGetCustomerQuery, useGetInsuranceQuoteRequests1Query, useGetCitiesForPostalCodeQuery, useHandleError4Query, useHandleError1Mutation, useHandleError3Mutation, useHandleError5Mutation, useHandleError2Mutation, useHandleError6Mutation, useHandleErrorMutation, } = injectedRtkApi;
