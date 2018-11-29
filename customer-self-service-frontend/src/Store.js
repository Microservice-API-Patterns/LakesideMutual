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
  isFetchingActivePolicy: boolean,
  policyFetchError: ?Error,
  isFetchingInteractionLog: boolean,
  interactionLogFetchError: ?Error,
  token: ?string,
  isAuthenticated: boolean,
  user: ?User,
  customer: ?Customer,
  policy: ?Policy,
  interactionLog: ?InteractionLog,
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
  isFetchingActivePolicy: false,
  policyFetchError: null,
  isFetchingInteractionLog: false,
  interactionLogFetchError: null,
  token: null,
  isAuthenticated: false,
  user: null,
  customer: null,
  policy: null,
  interactionLog: null,
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
      fetchActivePolicy: this.fetchActivePolicy,
      lookupCitiySuggestions: this.lookupCitiySuggestions,
      fetchInteractionLog: this.fetchInteractionLog,
    }
  }

  clearErrors = () => {
    this.setState({
      signupError: null,
      loginError: null,
      addressUpdateError: null,
      registrationError: null,
      policyFetchError: null,
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

  fetchActivePolicy = async (customerId: CustomerId) => {
    this.setState({
      policy: null,
      isFetchingActivePolicy: true,
      policyFetchError: null,
    })

    try {
      const policy = await api.getActivePolicy(customerId)
      this.setState({
        policy,
        isFetchingActivePolicy: false,
      })
    } catch (error) {
      this.setState({
        isFetchingActivePolicy: false,
        policyFetchError: error,
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
}
