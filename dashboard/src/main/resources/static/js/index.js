// $(document).ajaxStart(function() {
//   Pace.restart();
// });
window.paceOptions = {
  ajax: {
    trackMethods: ['GET', 'POST', 'PUT', 'DELETE', 'REMOVE']
  },
  restartOnRequestAfter: true
};

const buttons = $('.btn')
const inputs = $('input')

$('form').each(function (index) {
  this.addEventListener("keydown", function (event) {
    if (event.key === "Enter") {
      event.preventDefault();
      buttons[index].click();
    }
  })
})

buttons[0].onclick = function () {
  connect('ipfs', inputs[0].value)
}

buttons[1].onclick = function () {
  connect('web3', inputs[1].value)
}

buttons[2].onclick = function () {
  connect('docker', inputs[2].value)
};

function connect(url, data) {
  $.ajax({
    url: 'config/' + url,
    type: 'post',
    contentType: "application/json",
    data: JSON.stringify({address: data}),
    success: function () {
      Swal.fire('Connection established', 'Address: ' + data, 'success')
      .then(function () {
        location.reload()
      })
    },
    error: function (response) {
      Swal.fire({
        icon: 'error',
        title: 'Unable to establish connection',
        text: response.responseText,
      })
    }
  })
}


