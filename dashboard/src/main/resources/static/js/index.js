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
  connect('ipfs', JSON.stringify({address: inputs[0].value}))
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
  // addHeader()
  $.ajax({
    url: '/config/' + url,
    type: 'POST',
    contentType: "application/json",
    data: data,
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

$(function () {
  var data = {
    data: 'Base64.encode)'
  };
  // console.log(setHeaders('test'))
  const jqXHR = $.ajax({
    url: '/index/test',
    type: 'POST',
    data: JSON.stringify(data),
    headers: setHeaders('/index/test'),
    contentType: 'application/json',
    // success: function (response, jqXHR) {
    //   var contentType = jqXHR.getAllResponseHeaders();
    //   console.log(contentType);
    // }
  }).done(function (xhr) {
    checkAuth(jqXHR)
  });
  // $.ajax({
  //   url: '/index',
  //   type: 'GET',
  //   complete: function (xhr) {
  //     publicKey = 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnh9kpLwa9jjYGqulNeZhxbAUp'
  //         + 'LrcebQkVbuDUxg9IEGnOXBh+Mn36iMw17AAhY//zUQVQ1eQMoi10sXgNR76OggZr'
  //         + 'zedSY5LoCA2Oy82zFaYet85IOxv4ZcuEVw8iL0F0owbZeBnnTttkxmAVn2XrQaf4'
  //         + 'GpA1sXHh7abCUo0IewIDAQAB'
  //
  //     const via = xhr.getResponseHeader('via')
  //     console.log(via)
  //
  //     var encrypt=new JSEncrypt();
  //     encrypt.setPublicKey(publicKey);
  //     console.log(encrypt.decrypt(via))
  //     // var decryptedData = Encryption.AES.decrypt(data.toString(), key).toString(Encryption.enc.Utf8);
  //
  //   },
  // })
})


