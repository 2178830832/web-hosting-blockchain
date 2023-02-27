let intervalId, lineChart, formData;
let timeouts = [];
let error, running = false;
let number, timeoutCount = 0;
const pauseButton = $('#pause')[0]
const startButton = $('#start')[0]
const stopButton = $('#stop')[0]

function startInterval() {
  intervalId = setInterval(function () {
    const jqXHR =$.ajax({
      type: 'GET',
      url: '/test/result',
      headers: setHeaders('/test/result'),
      complete: function () {
        checkAuth(jqXHR)
      },
      success: function (data) {
        if (error || timeoutCount < 1) {
          stopTest()
        }
        const jsonData = JSON.parse(data);
        console.log(jsonData)
        lineChart.data.datasets[0].data[0] = jsonData['avg'];
        lineChart.update();
      }
    });
  }, 1000);
}

startButton.onclick = function () {
  number = parseInt($('#number').val())
  const url = $('#url').val()
  if (url.length < 1) {
    Swal.fire('Error', 'Website URL must be provided.', 'error')
    return;
  }
  const mode = $('input:radio[name="browser"]:checked').val()

  if (number > 0 && number < 1000) {
    formData = {}
    formData['url'] = url
    formData['mode'] = mode
    formData = JSON.stringify(formData)
    Swal.fire({
      title: 'Confirm test?',
      icon: 'info',
      text: formData.slice(1, -1),
      showCancelButton: true,
    }).then((result) => {
          if (result.isConfirmed) {
            startButton.setAttribute("disabled", "true");
            startButton.innerHTML = 'Test running'
            stopButton.removeAttribute("disabled")
            pauseButton.removeAttribute("disabled")
            Swal.fire('Success!', 'Test sequence has started.', 'success')
            running = true
            timeoutCount = number
            sendRequest()
          }
        }
    )
  } else {
    number = 0
    Swal.fire('Invalid number',
        'The number must be greater than 0 and less than 1000.', 'error')
  }
}

pauseButton.onclick = function () {
  if (running) {
    running = false
    this.innerHTML = "Resume tests";
    for (let timeout of timeouts) {
      clearTimeout(timeout);
    }
    timeouts = []
    clearInterval(intervalId)
  } else {
    running = true
    this.innerHTML = "Pause tests";
    sendRequest()
  }
}

stopButton.onclick = function () {
  stopTest()
}

function stopTest() {
  clearInterval(intervalId);
  for (let timeout of timeouts) {
    clearTimeout(timeout);
  }
  timeouts = []
  number = 0
  timeoutCount = 0
  running = false
  error = false
  pauseButton.innerHTML = "Pause tests";
  pauseButton.setAttribute("disabled", "true");
  startButton.removeAttribute("disabled");
  startButton.innerHTML = "Start tests";
  stopButton.setAttribute("disabled", "true");

  const jqXHR =$.ajax({
    type: 'GET',
    url: '/test/stop',
    headers: setHeaders('/test/stop'),
    complete: function () {
      checkAuth(jqXHR)
    },
  });
}

function sendRequest() {
  startInterval()
  for (let i = 0; i < number; i++) {
    timeouts.push(setTimeout(function () {
      const jqXHR =$.ajax({
        type: 'POST',
        url: '/test/param',
        contentType: 'application/json',
        data: formData,
        headers: setHeaders('/test/param', formData),
        complete: function () {
          checkAuth(jqXHR)
        },
        error: function (response) {
          stopTest()
          Swal.fire('Unable to execute test', response.responseText, 'error')
          error = true
        },
        success: function () {
          timeoutCount--
        }
      });
    }, 1000 * i));
  }
}

window.addEventListener('beforeunload', function (e) {
  stopTest()
  if (running) {
    e.preventDefault()
    Swal.fire({
      title: 'Confirm exiting?',
      icon: 'info',
      text: 'Tests will be stopped',
      showCancelButton: true,
    }).then((result) => {
          if (result.isConfirmed) {
            stopTest()
            window.removeEventListener('beforeunload', arguments.callee);
          }
        }
    )
  }

});

$(document).ready(function () {
  lineChart = new Chart(document.getElementById('lineChart').getContext('2d'), {
    type: 'horizontalBar',
    data: {
      labels: [], // labels for the x-axis
      datasets: [{
        label: '# of Time to First Byte',
        data: [], // data for the chart
        backgroundColor: 'rgb(89,102,219)',
        borderColor: 'rgb(89,102,219)',
        fill: false,
      }]
    },
    options: {
      animation: {
        duration: 0 // general animation time
      },
      hover: {
        animationDuration: 0 // duration of animations when hovering an item
      },
      responsiveAnimationDuration: 0, // animation duration after a resize
      scales: {
        yAxes: [{
          gridLines: {
            display: false
          },
        }],
        xAxes: [{
          ticks: {
            beginAtZero: true
          },
          scaleLabel: {
            display: true,
            labelString: 'Value (ms)',
          },
        }]
      },
    }
  });
});
