<template>
  <div>
    <template v-for="(audio, index) in audios" v-bind:key="index">
      <audio :src="audio" controls></audio>
    </template>
  </div>
</template>

<script>
import {sendReport} from '@/plugins/plugin'
import {axios} from "@/view/audio/audio"

const audioContext = require.context('@/assets/audio', false, /\.(mp3)$/);
const audios = audioContext.keys().map(audioContext);

export default {
  data() {
    return {
      audios
    }
  },
  mounted() {
    sendReport(axios)
  },
  name: 'AudioPage',
  props: {
    msg: String
  },
}

</script>

<style scoped>

</style>