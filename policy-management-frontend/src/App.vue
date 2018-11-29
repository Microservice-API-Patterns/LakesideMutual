<template>
  <div id="app">
    <router-link to="/">
      <h1 is="sui-header" id="header">Policy Management</h1>
      <p id="subheader">Lakeside Mutual</p>
    </router-link>
    <Menu/>
    <sui-segment>
      <router-view/>
  </sui-segment>
  </div>
</template>

<script>
import Menu from '@/components/Menu'

export default {
  name: 'App',
  components: { Menu }
}
</script>

<style>
#app {
  max-width: 800px;
  margin: 0px auto;
}

#header {
  color: #192a56;
  padding-top: 20px;
  margin-bottom: 0px;
}

#subheader {
  font-size: 22px;
  font-weight: lighter;
  color: #273c75;
  margin: 0px;
  padding: 0px;
}
</style>
