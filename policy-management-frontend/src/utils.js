export function extractFormError(errorResponse, context) {
  if (errorResponse.body && errorResponse.body.errors) {
    const errorFields = []
    const errorMessages = []

    for (const [field, name] of context) {
      const found = errorResponse.body.errors.find(
        error => error.field === field
      )

      if (found) {
        errorFields.push(found.field)
        errorMessages.push(
          `${name || field} ${found.defaultMessage.replace('null', 'empty')}`
        )
      }
    }

    const insuringAgreementErrorMessage =
      'Insuring Agreement must not contain empty fields'
    for (const error of errorResponse.body.errors) {
      if (!context.has(error.field)) {
        errorFields.push(error.field)
        if (!errorMessages.includes(insuringAgreementErrorMessage)) {
          errorMessages.push(insuringAgreementErrorMessage)
        }
      }
    }

    return {
      errorFields,
      errorMessages
    }
  } else if (
    errorResponse.body &&
    errorResponse.body.message &&
    !errorResponse.body.errors
  ) {
    return {
      errorFields: [],
      errorMessages: [errorResponse.body.message]
    }
  } else {
    return null
  }
}
