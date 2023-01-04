import * as WebVitals from 'web-vitals'

function sendReport(axios) {

  function sendToAnalytics(metric) {
    console.log(metric)
    axios.post('/test/metric', JSON.stringify(metric)).then(
        r => console.log(r))
  }

  WebVitals.onTTFB(r => sendToAnalytics(r))
  WebVitals.onFCP(r => sendToAnalytics(r))
  // WebVitals.onLCP(r => sendToAnalytics(r))
  WebVitals.getFID(r => sendToAnalytics(r))
}



export {sendReport}