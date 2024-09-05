import {baseApi as api} from "./baseApi"

const injectedRtkApi = api.injectEndpoints({
    endpoints: (build) => ({
        getCustomer: build.query<GetCustomerApiResponse, GetCustomerApiArg>({
            query: (queryArg) => ({url: `/customers/${queryArg.customerId}`}),
        }),
        updateCustomer: build.mutation<
            UpdateCustomerApiResponse,
            UpdateCustomerApiArg
        >({
            query: (queryArg) => ({
                url: `/customers/${queryArg.customerId}`,
                method: "PUT",
                body: queryArg.customerProfileDto,
            }),
        }),
        getInteractionLog: build.query<
            GetInteractionLogApiResponse,
            GetInteractionLogApiArg
        >({
            query: (queryArg) => ({
                url: `/interaction-logs/${queryArg.customerId}`,
            }),
        }),
        acknowledgeInteractions: build.mutation<
            AcknowledgeInteractionsApiResponse,
            AcknowledgeInteractionsApiArg
        >({
            query: (queryArg) => ({
                url: `/interaction-logs/${queryArg.customerId}`,
                method: "PATCH",
                body: queryArg.interactionAcknowledgementDto,
            }),
        }),
        getNotifications: build.query<
            GetNotificationsApiResponse,
            GetNotificationsApiArg
        >({
            query: () => ({url: `/notifications`}),
        }),
        getCustomers: build.query<GetCustomersApiResponse, GetCustomersApiArg>({
            query: (queryArg) => ({
                url: `/customers`,
                params: {
                    filter: queryArg.filter,
                    limit: queryArg.limit,
                    offset: queryArg.offset,
                },
            }),
        }),
        handleError3: build.query<HandleError3ApiResponse, HandleError3ApiArg>({
            query: () => ({url: `/error`}),
        }),
        handleError2: build.mutation<HandleError2ApiResponse, HandleError2ApiArg>({
            query: () => ({url: `/error`, method: "PUT"}),
        }),
        handleError1: build.mutation<HandleError1ApiResponse, HandleError1ApiArg>({
            query: () => ({url: `/error`, method: "POST"}),
        }),
        handleError4: build.mutation<HandleError4ApiResponse, HandleError4ApiArg>({
            query: () => ({url: `/error`, method: "DELETE"}),
        }),
        handleError: build.mutation<HandleErrorApiResponse, HandleErrorApiArg>({
            query: () => ({url: `/error`, method: "OPTIONS"}),
        }),
        handleError6: build.mutation<HandleError6ApiResponse, HandleError6ApiArg>({
            query: () => ({url: `/error`, method: "HEAD"}),
        }),
        handleError5: build.mutation<HandleError5ApiResponse, HandleError5ApiArg>({
            query: () => ({url: `/error`, method: "PATCH"}),
        }),
    }),
    overrideExisting: false,
})
export {injectedRtkApi as customerManagementApi}
export type GetCustomerApiResponse = /** status 200 OK */ CustomerDto
export type GetCustomerApiArg = {
    /** the customer's unique id */
    customerId: CustomerId
}
export type UpdateCustomerApiResponse = /** status 200 OK */ CustomerDto
export type UpdateCustomerApiArg = {
    /** the customer's unique id */
    customerId: CustomerId
    customerProfileDto: CustomerProfileDto
}
export type GetInteractionLogApiResponse =
/** status 200 OK */ InteractionLogAggregateRoot
export type GetInteractionLogApiArg = {
    /** the customer's unique id */
    customerId: CustomerId
}
export type AcknowledgeInteractionsApiResponse =
/** status 200 OK */ InteractionLogAggregateRoot
export type AcknowledgeInteractionsApiArg = {
    /** the customer's unique id */
    customerId: CustomerId
    interactionAcknowledgementDto: InteractionAcknowledgementDto
}
export type GetNotificationsApiResponse = /** status 200 OK */ NotificationDto[]
export type GetNotificationsApiArg = void
export type GetCustomersApiResponse =
/** status 200 OK */ PaginatedCustomerResponseDto
export type GetCustomersApiArg = {
    /** search terms to filter the customers by name */
    filter?: string
    /** the maximum number of customers per page */
    limit?: number
    /** the offset of the page's first customer */
    offset?: number
}
export type HandleError3ApiResponse = /** status 200 OK */ {
    [key: string]: object
}
export type HandleError3ApiArg = void
export type HandleError2ApiResponse = /** status 200 OK */ {
    [key: string]: object
}
export type HandleError2ApiArg = void
export type HandleError1ApiResponse = /** status 200 OK */ {
    [key: string]: object
}
export type HandleError1ApiArg = void
export type HandleError4ApiResponse = /** status 200 OK */ {
    [key: string]: object
}
export type HandleError4ApiArg = void
export type HandleErrorApiResponse = /** status 200 OK */ {
    [key: string]: object
}
export type HandleErrorApiArg = void
export type HandleError6ApiResponse = /** status 200 OK */ {
    [key: string]: object
}
export type HandleError6ApiArg = void
export type HandleError5ApiResponse = /** status 200 OK */ {
    [key: string]: object
}
export type HandleError5ApiArg = void
export type AddressDto = {
    streetAddress?: string
    postalCode?: string
    city?: string
}
export type Link = {
    rel?: string
    href?: string
    hreflang?: string
    media?: string
    title?: string
    type?: string
    deprecation?: string
    profile?: string
    name?: string
}
export type CustomerDto = {
    customerId?: string
    firstname?: string
    lastname?: string
    birthday?: string
    streetAddress?: string
    postalCode?: string
    city?: string
    email?: string
    phoneNumber?: string
    moveHistory?: AddressDto[]
    links?: Link[]
}
export type CustomerId = {
    id?: string
}
export type CustomerProfileDto = {
    firstname?: string
    lastname?: string
    birthday?: string
    streetAddress?: string
    postalCode?: string
    city?: string
    email?: string
    phoneNumber?: string
    moveHistory?: AddressDto[]
}
export type InteractionEntity = {
    id?: string
    date?: string
    content?: string
    sentByOperator?: boolean
}
export type InteractionLogAggregateRoot = {
    customerId?: string
    username?: string
    lastAcknowledgedInteractionId?: string
    interactions?: InteractionEntity[]
    numberOfUnacknowledgedInteractions?: number
}
export type InteractionAcknowledgementDto = {
    lastAcknowledgedInteractionId: string
}
export type NotificationDto = {
    customerId?: string
    username?: string
    count?: number
}
export type PaginatedCustomerResponseDto = {
    filter?: string
    limit?: number
    offset?: number
    size?: number
    customers?: CustomerDto[]
    links?: Link[]
}
export const {
    useGetCustomerQuery,
    useUpdateCustomerMutation,
    useGetInteractionLogQuery,
    useAcknowledgeInteractionsMutation,
    useGetNotificationsQuery,
    useGetCustomersQuery,
    useHandleError3Query,
    useHandleError2Mutation,
    useHandleError1Mutation,
    useHandleError4Mutation,
    useHandleErrorMutation,
    useHandleError6Mutation,
    useHandleError5Mutation,
} = injectedRtkApi
