<template>
  <div>{{ fileContents }}</div>
</template>

<script>
import {sendReport} from '@/plugins/plugin'
import {axios} from "@/view/text/text"
import content from 'raw-loader!@/assets/text/The Art Of Travel.txt';

export default {
  data() {
    return {
      fileContents: '',
    }
  },
  created() {
    // this.fileContents = require('@/assets/text/The Art Of Travel.txt')
    this.fileContents = content
  },
  mounted() {
    sendReport(axios)
  },
  name: 'TextPage',
  props: {
    msg: String
  },
}
</script>


<style scoped>

</style>