const publicKey = '-----BEGIN PUBLIC KEY-----\n'
    + 'MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDnh9kpLwa9jjYGqulNeZhxbAUp\n'
    + 'LrcebQkVbuDUxg9IEGnOXBh+Mn36iMw17AAhY//zUQVQ1eQMoi10sXgNR76OggZr\n'
    + 'zedSY5LoCA2Oy82zFaYet85IOxv4ZcuEVw8iL0F0owbZeBnnTttkxmAVn2XrQaf4\n'
    + 'GpA1sXHh7abCUo0IewIDAQAB\n'
    + '-----END PUBLIC KEY-----'
const jsEncrypt = new JSEncrypt();
jsEncrypt.setPublicKey(publicKey);
const signature = 'WEB_CHAIN'

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
  const auth = jqXHR.getResponseHeader('Authorization')
  const isVerified = jsEncrypt.verify(signature, auth, CryptoJS.SHA256);
  if (!isVerified) {
    Swal.fire('Insecure connection',
        'Your connection with the server might be intercepted', 'error')
  }
}
