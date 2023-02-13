const publicKey= '-----BEGIN PUBLIC KEY-----\n'
    + 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnh9kpLwa9jjYGqulNeZhxbAUp\n'
    + 'LrcebQkVbuDUxg9IEGnOXBh+Mn36iMw17AAhY//zUQVQ1eQMoi10sXgNR76OggZr\n'
    + 'zedSY5LoCA2Oy82zFaYet85IOxv4ZcuEVw8iL0F0owbZeBnnTttkxmAVn2XrQaf4\n'
    + 'GpA1sXHh7abCUo0IewIDAQAB\n'
    + '-----END PUBLIC KEY-----'
const jsEncrypt = new JSEncrypt();
jsEncrypt.setPublicKey(publicKey);

function setHeaders(url) {
  const authentication = {
    time: new Date().toISOString(),
    url: url
  }
  return {
    "Authorization": jsEncrypt.encrypt(JSON.stringify(authentication))
  }
}

function checkAuth(jqXHR) {
  // console.log(Base64.decode(jqXHR.getResponseHeader('Authorization')))
  const auth = jqXHR.getResponseHeader('Authorization')
  const isVerified = jsEncrypt.verify("test", auth, CryptoJS.SHA256);
  console.log(isVerified)
  // const authObj = JSON.parse(auth)
  // console.log(authObj['url'])
  // console.log(jqXHR.getResponseHeader('Authorization'))
}

// function addHeader() {
//   // Read the RSA private key from the local file using the FileReader API
//   const fileReader = new FileReader();
//   fileReader.onload = function(e) {
//     const privateKey = e.target.result;
//     // Set the default header for all future AJAX requests
//     $.ajaxSetup({
//       beforeSend: function(xhr) {
//         // Encrypt the URL using the RSA private key
//         const encrypt = new JSEncrypt();
//         encrypt.setKey(privateKey);
//         const encryptedUrl = encrypt.encrypt(this.url);
//         // Add the encrypted URL to the header of the AJAX request
//         xhr.setRequestHeader("Encrypted-Url", encryptedUrl);
//       }
//     });
//   };
//   const file = new Blob(['/encryption/public'], {type: 'text/plain'});
//   fileReader.readAsText(file);
// }