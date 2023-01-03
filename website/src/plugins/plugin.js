import * as WebVitals from 'web-vitals'
import {axios} from '@/main'

let body

function getMetric() {

  function sendToAnalytics(metric) {
    console.log(metric)
    axios.post('/request/index', JSON.stringify(metric)).then(
        r => console.log(r))
  }

  WebVitals.onTTFB(sendToAnalytics)
  return body
}

export {getMetric}