<template>
  <sui-menu>
    <router-link is="sui-menu-item" icon="file outline" to="/" :active="activeLink === '/'">Policies</router-link>
    <router-link
      is="sui-menu-item"
      icon="handshake outline"
      to="/insurance-quote-requests"
      :active="activeLink === '/insurance-quote-requests'"
    >Insurance Quote Requests</router-link>
    <router-link
      is="sui-menu-item"
      icon="user circle outline"
      to="/customers"
      :active="activeLink === '/customers'"
    >Customers</router-link>
  </sui-menu>
</template>

<script>
export default {
  name: 'Menu',
  data() {
    return {
      activeLink: this.$route.path
    }
  },
  watch: {
    $route(to, from) {
      this.activeLink = to.path
    }
  }
}
</script>

<style scoped>
</style>
