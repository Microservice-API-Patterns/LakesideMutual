<template>
  <div id="app">
    <router-link to="/">
      <div id="siteheader">
        <img :src="`${publicPath}favicon.png`" id="logo" />
        <div>
          <div id="header">Policy Management</div>
          <div id="subheader">Lakeside Mutual</div>
        </div>
      </div>
    </router-link>
    <Menu />
    <sui-segment>
      <router-view />
    </sui-segment>
    <sui-message style="color: #BBB" class="mini">
      <sui-message-header>Environment Variable Debug Information</sui-message-header>
      <p>
        VUE_APP_POLICY_MANAGEMENT_BACKEND: {{ policyManagementBackend }}
      </p>
    </sui-message>
  </div>
</template>

<script>
import Menu from '@/components/Menu'

export default {
  name: 'App',
  components: { Menu },
  data() {
    return {
      publicPath: process.env.BASE_URL,
      policyManagementBackend:
        window.__ENV ? window.__ENV.VUE_APP_POLICY_MANAGEMENT_BACKEND :
        process.env.VUE_APP_POLICY_MANAGEMENT_BACKEND
    }
  }
}
</script>

<style>
#app {
  max-width: 800px;
  margin: 0px auto 100px;
}

#siteheader {
  display: inline-flex;
  margin-top: 20px;
  align-items: center;
}

#logo {
  height: 65px;
  margin-right: 10px;
}

#header {
  font-size: 28px;
  line-height: 28px;
  font-weight: bold;
  color: #192a56;
  margin-bottom: 5px;
}

#subheader {
  font-size: 22px;
  line-height: 22px;
  font-weight: lighter;
  color: #273c75;
  margin: 0px;
  padding: 0px;
}
</style>
