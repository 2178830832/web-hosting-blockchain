import {createApp} from 'vue'
import axios from 'axios'
import App from '@/App.vue'
import * as WebVitals from 'web-vitals'

const app = createApp(App)
app.config.globalProperties.$WebVitals = WebVitals
app.config.globalProperties.$axios = axios
axios.defaults.baseURL = 'http://10.176.43.82:8000'
axios.defaults.headers.post['Content-Type'] = 'application/json'

const globals = app.config.globalProperties
export {globals, axios}

app.mount('#app')