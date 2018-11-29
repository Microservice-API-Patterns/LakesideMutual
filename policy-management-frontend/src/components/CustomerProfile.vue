<template>
  <sui-list size="large">
    <sui-list-item>
      <sui-list-icon name="user" />
      <sui-list-content>{{customer.firstname}} {{customer.lastname}}</sui-list-content>
    </sui-list-item>
    <sui-list-item>
      <sui-list-icon name="marker" />
      <sui-list-content>{{customer.streetAddress}}, {{customer.postalCode}} {{customer.city}}</sui-list-content>
    </sui-list-item>
    <sui-list-item>
      <sui-list-icon name="mail" />
      <sui-list-content><a :href="`mailto:${customer.email}`">{{customer.email}}</a></sui-list-content>
    </sui-list-item>
    <sui-list-item>
      <sui-list-icon name="phone" />
      <sui-list-content>{{customer.phoneNumber}}</sui-list-content>
    </sui-list-item>
  </sui-list>
</template>

<script>
export default {
  name: 'CustomerProfile',
  props: ['customer']
}
</script>

<style scoped>
</style>
