import * as WebVitals from 'web-vitals'

let body

function getMetric(axios) {

  function sendToAnalytics(metric) {
    console.log(metric)
    axios.post('/request/index', JSON.stringify(metric)).then(
        r => console.log(r))
  }

  WebVitals.onTTFB(sendToAnalytics)
  return body
}

export {getMetric}