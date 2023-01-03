<template>
  <div>{{ fileContents }}</div>
</template>

<script>
import {getMetric} from '@/plugins/plugin'
import {axios} from "@/view/text/text"

export default {
  data() {
    return {
      fileContents: '',
    }
  },
  created() {
    this.fileContents = require('@/assets/text/The Art Of Travel.txt')

  },
  mounted() {
    getMetric(axios)
  },
  methods: {
    async readFile(filePath) {
      const file = new File([filePath], 'The Art Of Travel.txt')
      const fileReader = new FileReader()
      fileReader.onload = (event) => {
        this.fileContents = event.target.result
      }
      fileReader.readAsText(file)
    },
  },
  name: 'TextPage',
  props: {
    msg: String
  },
}
</script>


<style scoped>

</style>