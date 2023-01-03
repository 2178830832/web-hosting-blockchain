<template>
  <div>
    <template v-for="(video,index) in videos" v-bind:key="index">
      <video :src="video" controls></video>
    </template>
  </div>
</template>

<script>
import {getMetric} from '@/plugins/plugin'
import {axios} from "@/view/video/video"

const videoContext = require.context('@/assets/video', false, /\.(mp4)$/);
const videos = videoContext.keys().map(videoContext);

export default {
  data() {
    return {
      videos
    }
  },
  mounted() {
    getMetric(axios)
  },
  name: 'VideoPage',
  props: {
    msg: String
  },
}

</script>

<style scoped>

</style>