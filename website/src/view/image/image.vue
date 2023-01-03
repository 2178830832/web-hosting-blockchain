<template>
  <div>
    <template v-for="(image, index) in images" v-bind:key="index">
      <img :src="image" alt="My image">
    </template>
  </div>
</template>

<script>
import {getMetric} from '@/plugins/plugin'
import {axios} from "@/view/image/image"

const imageContext = require.context('@/assets/image', false, /\.(jpg)$/);
const images = imageContext.keys().map(imageContext);

export default {
  data() {
    return {
      images
    }
  },
  mounted() {
    getMetric(axios)
  },
  name: 'ImagePage',
  props: {
    msg: String
  },
}

</script>

<style scoped>

</style>