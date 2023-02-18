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
  connect('ipfs', JSON.stringify({address: inputs[1].value}))
}

buttons[1].onclick = function () {
  connect('web3', JSON.stringify({address: inputs[1].value}))
}

buttons[2].onclick = function () {
  connect('web3', JSON.stringify({account: inputs[2].value}))
}

buttons[3].onclick = function () {
  connect('web3', JSON.stringify({contract: inputs[3].value}))
}

buttons[4].onclick = function () {
  connect('docker', JSON.stringify({address: inputs[4].value}))
};

function connect(url, data) {
  const jqXHR = $.ajax({
    url: '/config/' + url,
    type: 'POST',
    contentType: "application/json",
    data: data,
    headers: setHeaders('/config/' + url),
    complete: function () {
      checkAuth(jqXHR)
    },
    success: function () {
      Swal.fire('Connection established', data.slice(1, -1), 'success')
      .then(function () {
        location.reload()
      })
    },
    error: function (response) {
      Swal.fire('Unable to establish connection', response.responseText,
          'error')
    }
  })
}

