import { createApp } from 'vue'
import ImagePage from './image.vue'
import * as WebVitals from "web-vitals";
import axios from "axios";

const app = createApp(ImagePage)
app.config.globalProperties.$WebVitals = WebVitals
app.config.globalProperties.$axios = axios
axios.defaults.baseURL = 'http://localhost:8000'
axios.defaults.headers.post['Content-Type'] = 'application/json'

const globals = app.config.globalProperties
export {globals, axios}


app.mount('#app')