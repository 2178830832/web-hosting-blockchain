import { createApp } from 'vue'
import VideoPage from './video.vue'
import * as WebVitals from "web-vitals";
import axios from "axios";

const app = createApp(VideoPage)
app.config.globalProperties.$WebVitals = WebVitals
app.config.globalProperties.$axios = axios
axios.defaults.baseURL = 'http://192.168.5.101:8000'
axios.defaults.headers.post['Content-Type'] = 'application/json'

const globals = app.config.globalProperties
export {globals, axios}


app.mount('#app')