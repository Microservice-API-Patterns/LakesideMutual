<template>
  <sui-message error>
    <sui-message-header>Error</sui-message-header>
    <p>
      {{ errorMessage }}
    </p>
  </sui-message>
</template>

<script>
import { ApiError } from '../api'
export default {
  name: 'ErrorMessage',
  props: ['error', 'resource'],
  computed: {
    errorMessage() {
      if (this.error instanceof ApiError) {
        if (this.error.code === 404) {
          return `${this.resource} not found.`
        } else if (this.error.code === 502) {
          return `The Customer Self-Service backend is currently not available.
                  Please follow the instructions in the corresponding README to ensure
                  that it is running and listening on the right port.`
        } else {
          return 'Unknown error.'
        }
      } else {
        return `The Policy Management backend is currently not available.
                Please follow the instructions in the corresponding README to ensure
                that it is running and listening on the right port.`
      }
    }
  }
}
</script>

<style scoped>
</style>
