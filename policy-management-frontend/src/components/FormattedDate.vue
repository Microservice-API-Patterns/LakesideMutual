<template>
  <span>{{formatDate(date)}}</span>
</template>

<script>
import moment from 'moment'

export default {
  name: 'FormattedDate',
  props: { date: String, full: Boolean },
  methods: {
    formatDate(date) {
      const format = this.full ? 'DD. MMM YYYY HH:mm' : 'DD. MMM YYYY'
      return moment(date).format(format)
    }
  }
}
</script>

<style scoped>
</style>
