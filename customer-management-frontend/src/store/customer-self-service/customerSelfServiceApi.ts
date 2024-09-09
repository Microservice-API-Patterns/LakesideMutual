import { customerSelfServiceBase as api } from "./customerSelfServiceBase"
const injectedRtkApi = api.injectEndpoints({
  endpoints: (build) => ({
    changeAddress: build.mutation<
      ChangeAddressApiResponse,
      ChangeAddressApiArg
    >({
      query: (queryArg) => ({
        url: `/customers/${queryArg.customerId}/address`,
        method: "PUT",
        body: queryArg.addressDto,
      }),
    }),
    getInsuranceQuoteRequests: build.query<
      GetInsuranceQuoteRequestsApiResponse,
      GetInsuranceQuoteRequestsApiArg
    >({
      query: () => ({ url: `/insurance-quote-requests` }),
    }),
    createInsuranceQuoteRequest: build.mutation<
      CreateInsuranceQuoteRequestApiResponse,
      CreateInsuranceQuoteRequestApiArg
    >({
      query: (queryArg) => ({
        url: `/insurance-quote-requests`,
        method: "POST",
        body: queryArg.insuranceQuoteRequestDto,
      }),
    }),
    registerCustomer: build.mutation<
      RegisterCustomerApiResponse,
      RegisterCustomerApiArg
    >({
      query: (queryArg) => ({
        url: `/customers`,
        method: "POST",
        body: queryArg.customerRegistrationRequestDto,
      }),
    }),
    authenticationRequest: build.mutation<
      AuthenticationRequestApiResponse,
      AuthenticationRequestApiArg
    >({
      query: (queryArg) => ({
        url: `/auth`,
        method: "POST",
        body: queryArg.authenticationRequestDto,
      }),
    }),
    signupUser: build.mutation<SignupUserApiResponse, SignupUserApiArg>({
      query: (queryArg) => ({
        url: `/auth/signup`,
        method: "POST",
        body: queryArg.signupRequestDto,
      }),
    }),
    respondToInsuranceQuote: build.mutation<
      RespondToInsuranceQuoteApiResponse,
      RespondToInsuranceQuoteApiArg
    >({
      query: (queryArg) => ({
        url: `/insurance-quote-requests/${queryArg.id}`,
        method: "PATCH",
        body: queryArg.insuranceQuoteResponseDto,
      }),
    }),
    getCurrentUser: build.query<
      GetCurrentUserApiResponse,
      GetCurrentUserApiArg
    >({
      query: () => ({ url: `/user` }),
    }),
    getInsuranceQuoteRequest: build.query<
      GetInsuranceQuoteRequestApiResponse,
      GetInsuranceQuoteRequestApiArg
    >({
      query: (queryArg) => ({
        url: `/insurance-quote-requests/${queryArg.insuranceQuoteRequestId}`,
      }),
    }),
    getCustomer: build.query<GetCustomerApiResponse, GetCustomerApiArg>({
      query: (queryArg) => ({ url: `/customers/${queryArg.customerId}` }),
    }),
    getInsuranceQuoteRequests1: build.query<
      GetInsuranceQuoteRequests1ApiResponse,
      GetInsuranceQuoteRequests1ApiArg
    >({
      query: (queryArg) => ({
        url: `/customers/${queryArg.customerId}/insurance-quote-requests`,
      }),
    }),
    getCitiesForPostalCode: build.query<
      GetCitiesForPostalCodeApiResponse,
      GetCitiesForPostalCodeApiArg
    >({
      query: (queryArg) => ({ url: `/cities/${queryArg.postalCode}` }),
    }),
    handleError2: build.query<HandleError2ApiResponse, HandleError2ApiArg>({
      query: () => ({ url: `/error` }),
    }),
    handleError1: build.mutation<HandleError1ApiResponse, HandleError1ApiArg>({
      query: () => ({ url: `/error`, method: "PUT" }),
    }),
    handleError5: build.mutation<HandleError5ApiResponse, HandleError5ApiArg>({
      query: () => ({ url: `/error`, method: "POST" }),
    }),
    handleError4: build.mutation<HandleError4ApiResponse, HandleError4ApiArg>({
      query: () => ({ url: `/error`, method: "DELETE" }),
    }),
    handleError6: build.mutation<HandleError6ApiResponse, HandleError6ApiArg>({
      query: () => ({ url: `/error`, method: "OPTIONS" }),
    }),
    handleError: build.mutation<HandleErrorApiResponse, HandleErrorApiArg>({
      query: () => ({ url: `/error`, method: "HEAD" }),
    }),
    handleError3: build.mutation<HandleError3ApiResponse, HandleError3ApiArg>({
      query: () => ({ url: `/error`, method: "PATCH" }),
    }),
  }),
  overrideExisting: false,
})
export { injectedRtkApi as customerSelfServiceApi }
export type ChangeAddressApiResponse = /** status 200 OK */ AddressDto
export type ChangeAddressApiArg = {
  /** the customer's unique id */
  customerId: CustomerId
  addressDto: AddressDto
}
export type GetInsuranceQuoteRequestsApiResponse =
  /** status 200 OK */ InsuranceQuoteRequestDto[]
export type GetInsuranceQuoteRequestsApiArg = void
export type CreateInsuranceQuoteRequestApiResponse =
  /** status 200 OK */ InsuranceQuoteRequestDto
export type CreateInsuranceQuoteRequestApiArg = {
  insuranceQuoteRequestDto: InsuranceQuoteRequestDto
}
export type RegisterCustomerApiResponse = /** status 200 OK */ CustomerDto
export type RegisterCustomerApiArg = {
  customerRegistrationRequestDto: CustomerRegistrationRequestDto
}
export type AuthenticationRequestApiResponse =
  /** status 200 OK */ AuthenticationResponseDto
export type AuthenticationRequestApiArg = {
  authenticationRequestDto: AuthenticationRequestDto
}
export type SignupUserApiResponse = /** status 200 OK */ UserResponseDto
export type SignupUserApiArg = {
  signupRequestDto: SignupRequestDto
}
export type RespondToInsuranceQuoteApiResponse =
  /** status 200 OK */ InsuranceQuoteRequestDto
export type RespondToInsuranceQuoteApiArg = {
  /** the insurance quote request's unique id */
  id: number
  insuranceQuoteResponseDto: InsuranceQuoteResponseDto
}
export type GetCurrentUserApiResponse = /** status 200 OK */ UserResponseDto
export type GetCurrentUserApiArg = void
export type GetInsuranceQuoteRequestApiResponse =
  /** status 200 OK */ InsuranceQuoteRequestDto
export type GetInsuranceQuoteRequestApiArg = {
  /** the insurance quote request's unique id */
  insuranceQuoteRequestId: number
}
export type GetCustomerApiResponse = /** status 200 OK */ CustomerDto
export type GetCustomerApiArg = {
  /** the customer's unique id */
  customerId: CustomerId
}
export type GetInsuranceQuoteRequests1ApiResponse =
  /** status 200 OK */ InsuranceQuoteRequestDto[]
export type GetInsuranceQuoteRequests1ApiArg = {
  /** the customer's unique id */
  customerId: CustomerId
}
export type GetCitiesForPostalCodeApiResponse =
  /** status 200 OK */ CitiesResponseDto
export type GetCitiesForPostalCodeApiArg = {
  /** the postal code */
  postalCode: string
}
export type HandleError2ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError2ApiArg = void
export type HandleError1ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError1ApiArg = void
export type HandleError5ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError5ApiArg = void
export type HandleError4ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError4ApiArg = void
export type HandleError6ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError6ApiArg = void
export type HandleErrorApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleErrorApiArg = void
export type HandleError3ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError3ApiArg = void
export type AddressDto = {
  streetAddress: string
  postalCode: string
  city: string
}
export type CustomerId = {
  id?: string
}
export type RequestStatusChangeDto = {
  date?: string
  status: string
}
export type CustomerInfoDto = {
  customerId: string
  firstname: string
  lastname: string
  contactAddress: AddressDto
  billingAddress: AddressDto
}
export type MoneyAmountDto = {
  amount: number
  currency: string
}
export type InsuranceOptionsDto = {
  startDate: string
  insuranceType: string
  deductible: MoneyAmountDto
}
export type InsuranceQuoteDto = {
  expirationDate: string
  insurancePremium: MoneyAmountDto
  policyLimit: MoneyAmountDto
}
export type InsuranceQuoteRequestDto = {
  id?: number
  date?: string
  statusHistory?: RequestStatusChangeDto[]
  customerInfo: CustomerInfoDto
  insuranceOptions: InsuranceOptionsDto
  insuranceQuote?: InsuranceQuoteDto
  policyId?: string
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
  streetAddress: string
  postalCode: string
  city: string
  email?: string
  phoneNumber?: string
  moveHistory?: AddressDto[]
  links?: Link[]
}
export type CustomerRegistrationRequestDto = {
  firstname: string
  lastname: string
  birthday: string
  city: string
  streetAddress: string
  postalCode: string
  phoneNumber?: string
}
export type AuthenticationResponseDto = {
  email?: string
  token?: string
}
export type AuthenticationRequestDto = {
  email?: string
  password?: string
}
export type UserResponseDto = {
  email?: string
  customerId?: string
}
export type SignupRequestDto = {
  email: string
  password: string
}
export type InsuranceQuoteResponseDto = {
  status: string
  expirationDate?: string
  insurancePremium?: MoneyAmountDto
  policyLimit?: MoneyAmountDto
}
export type CitiesResponseDto = {
  cities?: string[]
}
export const {
  useChangeAddressMutation,
  useGetInsuranceQuoteRequestsQuery,
  useCreateInsuranceQuoteRequestMutation,
  useRegisterCustomerMutation,
  useAuthenticationRequestMutation,
  useSignupUserMutation,
  useRespondToInsuranceQuoteMutation,
  useGetCurrentUserQuery,
  useGetInsuranceQuoteRequestQuery,
  useGetCustomerQuery,
  useGetInsuranceQuoteRequests1Query,
  useGetCitiesForPostalCodeQuery,
  useHandleError2Query,
  useHandleError1Mutation,
  useHandleError5Mutation,
  useHandleError4Mutation,
  useHandleError6Mutation,
  useHandleErrorMutation,
  useHandleError3Mutation,
} = injectedRtkApi
