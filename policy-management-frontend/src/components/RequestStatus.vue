<template>
  <span>
    <i v-if="status === 'REQUEST_SUBMITTED'">Request open</i>
    <i v-if="status === 'QUOTE_RECEIVED'">Waiting for Customer response</i>
    <i style="color:red;" v-if="status === 'REQUEST_REJECTED'">Request rejected</i>
    <i style="color:green;" v-if="status === 'QUOTE_ACCEPTED'">Quote accepted</i>
    <i style="color:green;" v-if="status === 'POLICY_CREATED'">Policy created</i>
    <i style="color:red;" v-if="status === 'QUOTE_REJECTED'">Quote rejected</i>
    <i style="color:red;" v-if="status === 'QUOTE_EXPIRED'">Quote expired</i>
  </span>
</template>

<script>
export default {
  name: 'RequestStatus',
  props: ['status']
}
</script>

<style scoped>
</style>
