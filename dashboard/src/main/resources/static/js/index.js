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
  connect('web3', JSON.stringify({address: inputs[3].value}))
}

buttons[2].onclick = function () {
  connect('web3', JSON.stringify({account: inputs[5].value}))
}

buttons[3].onclick = function () {
  connect('web3', JSON.stringify({contract: inputs[7].value}))
}

buttons[4].onclick = function () {
  connect('docker', JSON.stringify({address: inputs[9].value}))
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

function getStatus() {
  const jqXHR = $.ajax({
    url: '/config',
    type: 'GET',
    headers: setHeaders('/config'),
    complete: function () {
      checkAuth(jqXHR)
    },
    success: function (data) {
      const status = JSON.parse(data)
      if (status['ipfs']['Address'] != null) {
        $('#span1').text('Connected').css("color", "green")
        $('#div1').show()
        $('#div1 > div:nth-child(1)').text('Address: ' + status['ipfs']['Address'])
        $('#div1 > div:nth-child(2)').text('Version: ' + status['ipfs']['AgentVersion'])
        $('#div1 > div:nth-child(3)').text('ID: ' + status['ipfs']['ID'])
      } else {
        $('#span2').text('Not connected').css("color", "red")
        $('#div1').hide()
      }

      if (status['docker']['Address'] != null) {
        $('#span3').text('Connected').css("color", "green")
        $('#div2').show()
        $('#div2 > div:nth-child(1)').text('Address: ' + status['web3']['Address'])
        $('#div2 > div:nth-child(2)').text('Client: ' + status['web3']['Client'])
        $('#div2 > div:nth-child(3)').text('Account: ' + status['web3']['Account'])
        $('#div2 > div:nth-child(4)').text('Balance: ' + status['web3']['Balance'] + ' ETH')
        $('#div2 > div:nth-child(5)').text('Contract: ' + status['web3']['Contract'])
      } else {
        $('#span4').text('Not connected').css("color", "red")
        $('#div2').hide()
      }

      if (status['docker']['Address'] != null) {
        $('#span5').text('Connected').css("color", "green")
        $('#div3').show()
        $('#div3 > div:nth-child(1)').text('Address: ' + status['docker']['Address'])
        $('#div3 > div:nth-child(2)').text('Version: ' + status['docker']['serverVersion'])
        $('#div3 > div:nth-child(3)').text('OS: ' + status['docker']['name'])
        $('#div3 > div:nth-child(4)').text('OS Name: ' + status['docker']['operatingSystem'] )
        $('#div3 > div:nth-child(5)').text('Containers: ' + status['docker']['containers'])
        $('#div3 > div:nth-child(6)').text('Running Containers: ' + status['docker']['containers'])
      } else {
        $('#span6').text('Not connected').css("color", "red")
        $('#div3').hide()
      }

    },
    error: function (response) {
      Swal.fire('Unexpected status error', response.responseText,
          'error')
    }
  })
}

$(function () {
  getStatus()
})

