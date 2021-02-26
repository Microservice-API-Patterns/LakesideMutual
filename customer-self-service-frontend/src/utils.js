// @flow

export function extractFormError(
  errorResponse: FormSubmissionError,
  context: Map<string, string>
): ?FormError {
  if (errorResponse.body != null && errorResponse.body.errors != null) {
    const errorFields = []
    const errorMessages = []

    for (const [field, name] of context) {
      const error = errorResponse.body.errors.find(
        error => error.field === field
      )

      if (error) {
        errorFields.push(field)
        const message = error.defaultMessage.replace("null", "empty")
        errorMessages.push(`${name} ${message}`)
      }
    }

    return {
      errorFields,
      errorMessages,
    }
  } else {
    return null
  }
}
