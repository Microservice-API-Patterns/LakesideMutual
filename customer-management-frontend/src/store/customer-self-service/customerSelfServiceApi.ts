import { customerSelfServiceBase as api } from "./customerSelfServiceBase"
export const addTagTypes = [
  "customer-information-holder",
  "insurance-quote-request-coordinator",
  "authentication-controller",
  "user-information-holder",
  "city-reference-data-holder",
  "error-controller",
] as const
const injectedRtkApi = api
  .enhanceEndpoints({
    addTagTypes,
  })
  .injectEndpoints({
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
        invalidatesTags: ["customer-information-holder"],
      }),
      getInsuranceQuoteRequests: build.query<
        GetInsuranceQuoteRequestsApiResponse,
        GetInsuranceQuoteRequestsApiArg
      >({
        query: () => ({ url: `/insurance-quote-requests` }),
        providesTags: ["insurance-quote-request-coordinator"],
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
        invalidatesTags: ["insurance-quote-request-coordinator"],
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
        invalidatesTags: ["customer-information-holder"],
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
        invalidatesTags: ["authentication-controller"],
      }),
      signupUser: build.mutation<SignupUserApiResponse, SignupUserApiArg>({
        query: (queryArg) => ({
          url: `/auth/signup`,
          method: "POST",
          body: queryArg.signupRequestDto,
        }),
        invalidatesTags: ["authentication-controller"],
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
        invalidatesTags: ["insurance-quote-request-coordinator"],
      }),
      getCurrentUser: build.query<
        GetCurrentUserApiResponse,
        GetCurrentUserApiArg
      >({
        query: () => ({ url: `/user` }),
        providesTags: ["user-information-holder"],
      }),
      getInsuranceQuoteRequest: build.query<
        GetInsuranceQuoteRequestApiResponse,
        GetInsuranceQuoteRequestApiArg
      >({
        query: (queryArg) => ({
          url: `/insurance-quote-requests/${queryArg.insuranceQuoteRequestId}`,
        }),
        providesTags: ["insurance-quote-request-coordinator"],
      }),
      getCustomer: build.query<GetCustomerApiResponse, GetCustomerApiArg>({
        query: (queryArg) => ({ url: `/customers/${queryArg.customerId}` }),
        providesTags: ["customer-information-holder"],
      }),
      getInsuranceQuoteRequests1: build.query<
        GetInsuranceQuoteRequests1ApiResponse,
        GetInsuranceQuoteRequests1ApiArg
      >({
        query: (queryArg) => ({
          url: `/customers/${queryArg.customerId}/insurance-quote-requests`,
        }),
        providesTags: ["customer-information-holder"],
      }),
      getCitiesForPostalCode: build.query<
        GetCitiesForPostalCodeApiResponse,
        GetCitiesForPostalCodeApiArg
      >({
        query: (queryArg) => ({ url: `/cities/${queryArg.postalCode}` }),
        providesTags: ["city-reference-data-holder"],
      }),
      handleError4: build.query<HandleError4ApiResponse, HandleError4ApiArg>({
        query: () => ({ url: `/error` }),
        providesTags: ["error-controller"],
      }),
      handleError1: build.mutation<HandleError1ApiResponse, HandleError1ApiArg>(
        {
          query: () => ({ url: `/error`, method: "PUT" }),
          invalidatesTags: ["error-controller"],
        }
      ),
      handleError3: build.mutation<HandleError3ApiResponse, HandleError3ApiArg>(
        {
          query: () => ({ url: `/error`, method: "POST" }),
          invalidatesTags: ["error-controller"],
        }
      ),
      handleError5: build.mutation<HandleError5ApiResponse, HandleError5ApiArg>(
        {
          query: () => ({ url: `/error`, method: "DELETE" }),
          invalidatesTags: ["error-controller"],
        }
      ),
      handleError2: build.mutation<HandleError2ApiResponse, HandleError2ApiArg>(
        {
          query: () => ({ url: `/error`, method: "OPTIONS" }),
          invalidatesTags: ["error-controller"],
        }
      ),
      handleError6: build.mutation<HandleError6ApiResponse, HandleError6ApiArg>(
        {
          query: () => ({ url: `/error`, method: "HEAD" }),
          invalidatesTags: ["error-controller"],
        }
      ),
      handleError: build.mutation<HandleErrorApiResponse, HandleErrorApiArg>({
        query: () => ({ url: `/error`, method: "PATCH" }),
        invalidatesTags: ["error-controller"],
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
export type HandleError4ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError4ApiArg = void
export type HandleError1ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError1ApiArg = void
export type HandleError3ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError3ApiArg = void
export type HandleError5ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError5ApiArg = void
export type HandleError2ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError2ApiArg = void
export type HandleError6ApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleError6ApiArg = void
export type HandleErrorApiResponse = /** status 200 OK */ {
  [key: string]: object
}
export type HandleErrorApiArg = void
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
  useHandleError4Query,
  useHandleError1Mutation,
  useHandleError3Mutation,
  useHandleError5Mutation,
  useHandleError2Mutation,
  useHandleError6Mutation,
  useHandleErrorMutation,
} = injectedRtkApi
