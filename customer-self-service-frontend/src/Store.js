// @flow

import { Container } from "unstated"
import * as api from "./api"

type State = {
  isSigningUp: boolean,
  signupError: ?Error,
  isLoggingIn: boolean,
  loginError: ?Error,
  isUpdatingAddress: boolean,
  addressUpdateError: ?FormSubmissionError,
  isCompletingRegistration: boolean,
  registrationError: ?Error,
  isFetchingPolicies: boolean,
  policiesFetchError: ?Error,
  isFetchingInteractionLog: boolean,
  interactionLogFetchError: ?Error,
  token: ?string,
  isAuthenticated: boolean,
  user: ?User,
  customer: ?Customer,
  policies: ?[Policy],
  interactionLog: ?InteractionLog,
  isCreatingInsuranceQuoteRequest: boolean,
  insuranceQuoteRequestCreationError: ?Error,
  insuranceQuoteRequests: ?[InsuranceQuoteRequest],
  isFetchingInsuranceQuoteRequests: boolean,
  insuranceQuoteRequest: ?InsuranceQuoteRequest,
  isFetchingInsuranceQuoteRequest: boolean,
  insuranceQuoteRequestFetchError: ?Error,
  isRespondingToInsuranceQuote: boolean,
  insuranceQuoteResponseError: ?Error,
}

const initialState: State = {
  isSigningUp: false,
  signupError: null,
  isLoggingIn: false,
  loginError: null,
  isUpdatingAddress: false,
  addressUpdateError: null,
  isCompletingRegistration: false,
  registrationError: null,
  isFetchingPolicies: false,
  policiesFetchError: null,
  isFetchingInteractionLog: false,
  interactionLogFetchError: null,
  token: null,
  isAuthenticated: false,
  user: null,
  customer: null,
  policies: null,
  interactionLog: null,
  isCreatingInsuranceQuoteRequest: false,
  insuranceQuoteRequestCreationError: null,
  insuranceQuoteRequest: null,
  isFetchingInsuranceQuoteRequest: false,
  insuranceQuoteRequests: null,
  isFetchingInsuranceQuoteRequests: false,
  insuranceQuoteRequestFetchError: null,
  isRespondingToInsuranceQuote: false,
  insuranceQuoteResponseError: null,
}

export default class Store extends Container<State> {
  constructor() {
    super()

    const token = sessionStorage.getItem("token")
    this.state = { ...initialState, token, isAuthenticated: token != null }

    if (token != null) {
      this.fetchAuthenticatedUser(token)
    }
  }

  getActions() {
    return {
      clearErrors: this.clearErrors,
      signup: this.signup,
      login: this.login,
      logout: this.logout,
      completeRegistration: this.completeRegistration,
      changeAddress: this.changeAddress,
      fetchPolicies: this.fetchPolicies,
      lookupCitiySuggestions: this.lookupCitiySuggestions,
      fetchInteractionLog: this.fetchInteractionLog,
      createInsuranceQuoteRequest: this.createInsuranceQuoteRequest,
      resetInsuranceQuoteRequestCreationError: this
        .resetInsuranceQuoteRequestCreationError,
      fetchInsuranceQuoteRequests: this.fetchInsuranceQuoteRequests,
      fetchInsuranceQuoteRequest: this.fetchInsuranceQuoteRequest,
      respondToInsuranceQuote: this.respondToInsuranceQuote,
    }
  }

  clearErrors = () => {
    this.setState({
      signupError: null,
      loginError: null,
      addressUpdateError: null,
      registrationError: null,
      policiesFetchError: null,
    })
  }

  signup = async (email: string, password: string) => {
    this.setState({
      isSigningUp: true,
      signupError: null,
    })

    try {
      await api.signup(email, password)
      this.setState({
        isSigningUp: false,
      })
      return true
    } catch (error) {
      this.setState({ signupError: error, isSigningUp: false })
      return false
    }
  }

  login = async (email: string, password: string) => {
    this.setState({
      isLoggingIn: true,
      loginError: null,
    })

    try {
      const { token } = await api.login(email, password)
      await this.fetchAuthenticatedUser(token)
      this.setState({ isAuthenticated: true, token, isLoggingIn: false })
      sessionStorage.setItem("token", token)
    } catch (error) {
      this.setState({ isLoggingIn: false, loginError: error })
    }
  }

  logout = async () => {
    this.setState({ ...initialState })
    sessionStorage.removeItem("token")
  }

  completeRegistration = async (data: CompleteProfileFormData) => {
    this.setState({
      isCompletingRegistration: true,
      registrationError: null,
    })

    try {
      const token = this.state.token || ""
      await api.completeRegistration(token, data)
      await this.fetchAuthenticatedUser(token)
      this.setState({
        isCompletingRegistration: false,
      })
    } catch (error) {
      this.setState({
        isCompletingRegistration: false,
        registrationError: error,
      })
    }
  }

  changeAddress = async (address: Address) => {
    const token = this.state.token || ""
    const customer = this.state.customer
    if (customer == null) {
      return
    }

    this.setState({
      isUpdatingAddress: true,
      addressUpdateError: null,
    })

    try {
      await api.changeAddress(token, customer, address)
      await this.fetchAuthenticatedUser(token)
      this.setState({
        isUpdatingAddress: false,
      })
    } catch (error) {
      this.setState({
        isUpdatingAddress: false,
        addressUpdateError: error,
      })
    }
  }

  fetchPolicies = async (customerId: CustomerId) => {
    this.setState({
      policies: null,
      isFetchingPolicies: true,
      policiesFetchError: null,
    })

    try {
      const policies = await api.getPolicies(customerId)
      this.setState({
        policies,
        isFetchingPolicies: false,
      })
    } catch (error) {
      this.setState({
        isFetchingPolicies: false,
        policiesFetchError: error,
      })
    }
  }

  // helper methods
  fetchAuthenticatedUser = async (token: string) => {
    try {
      const user = await api.getUserDetails(token)
      let customer = null
      if (user.customerId != null) {
        customer = await api.getCustomer(token, user.customerId)
      }
      this.setState({ user, customer })
    } catch (error) {
      await this.logout()
    }
  }

  lookupCitiySuggestions = (postalCode: string) => {
    const token = this.state.token || ""
    return api.lookupCitiySuggestions(token, postalCode)
  }

  fetchInteractionLog = async (customerId: CustomerId) => {
    this.setState({
      interactionLog: null,
      isFetchingInteractionLog: true,
      interactionLogFetchError: null,
    })

    try {
      const interactionLog = await api.getInteractionLog(customerId)
      this.setState({
        interactionLog,
        isFetchingInteractionLog: false,
      })
    } catch (error) {
      this.setState({
        isFetchingInteractionLog: false,
        interactionLogFetchError: error,
      })
    }
  }

  createInsuranceQuoteRequest = async (data: InsuranceQuoteRequest) => {
    this.setState({
      isCreatingInsuranceQuoteRequest: true,
      insuranceQuoteRequestCreationError: null,
    })

    try {
      const token = this.state.token || ""
      await api.createInsuranceQuoteRequest(token, data)
      this.setState({
        isCreatingInsuranceQuoteRequest: false,
      })
    } catch (error) {
      this.setState({
        isCreatingInsuranceQuoteRequest: false,
        insuranceQuoteRequestCreationError: error,
      })
    }
  }

  resetInsuranceQuoteRequestCreationError = () => {
    this.setState({ insuranceQuoteRequestCreationError: null })
  }

  fetchInsuranceQuoteRequests = async (customerId: CustomerId) => {
    this.setState({
      insuranceQuoteRequests: null,
      isFetchingInsuranceQuoteRequests: true,
      insuranceQuoteRequestFetchError: null,
    })

    try {
      const token = this.state.token || ""
      const insuranceQuoteRequests = await api.getInsuranceQuoteRequests(
        token,
        customerId
      )
      this.setState({
        insuranceQuoteRequests,
        isFetchingInsuranceQuoteRequests: false,
      })
    } catch (error) {
      this.setState({
        isFetchingInsuranceQuoteRequests: false,
        insuranceQuoteRequestFetchError: error,
      })
    }
  }

  fetchInsuranceQuoteRequest = async (id: string) => {
    this.setState({
      insuranceQuoteRequest: null,
      isFetchingInsuranceQuoteRequest: true,
      insuranceQuoteRequestFetchError: null,
    })

    try {
      const token = this.state.token || ""
      const insuranceQuoteRequest = await api.getInsuranceQuoteRequest(
        token,
        id
      )
      this.setState({
        insuranceQuoteRequest,
        isFetchingInsuranceQuoteRequest: false,
      })
    } catch (error) {
      this.setState({
        isFetchingInsuranceQuoteRequest: false,
        insuranceQuoteRequestFetchError: error,
      })
    }
  }

  respondToInsuranceQuote = async (
    insuranceQuoteRequestId: string,
    accepted: boolean
  ) => {
    this.setState({
      isRespondingToInsuranceQuote: true,
      insuranceQuoteResponseError: null,
    })

    try {
      const token = this.state.token || ""
      await api.respondToInsuranceQuote(
        token,
        insuranceQuoteRequestId,
        accepted
      )
      this.setState({
        isRespondingToInsuranceQuote: false,
      })
    } catch (error) {
      this.setState({
        isRespondingToInsuranceQuote: false,
        insuranceQuoteResponseError: error,
      })
    }
  }
}
