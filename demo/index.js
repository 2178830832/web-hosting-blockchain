web3 = new Web3(new Web3.providers.HttpProvider("HTTP://127.0.0.1:8545"));
abi = [
  {
    "inputs": [
      {
        "internalType": "string[]",
        "name": "candidateNames",
        "type": "string[]"
      }
    ],
    "stateMutability": "nonpayable",
    "type": "constructor"
  },
  {
    "inputs": [
      {
        "internalType": "string",
        "name": "candidate",
        "type": "string"
      }
    ],
    "name": "voteForCandidate",
    "outputs": [],
    "stateMutability": "nonpayable",
    "type": "function"
  },
  {
    "inputs": [
      {
        "internalType": "string",
        "name": "candidate",
        "type": "string"
      }
    ],
    "name": "totalVotesFor",
    "outputs": [
      {
        "internalType": "uint8",
        "name": "",
        "type": "uint8"
      }
    ],
    "stateMutability": "view",
    "type": "function"
  },
  {
    "inputs": [
      {
        "internalType": "string",
        "name": "candidate",
        "type": "string"
      }
    ],
    "name": "validCandidate",
    "outputs": [
      {
        "internalType": "bool",
        "name": "",
        "type": "bool"
      }
    ],
    "stateMutability": "view",
    "type": "function"
  }
];
address = '0xDAbd503C218a0222e5f5EDD18dD141CD9a076509';
contractInstance = new web3.eth.Contract(abi, address);
 
candidates = {"Alice": "candidate-1","Bob": "candidate-2","Cary":"candidate-3"}
 
// function voteForCandidate() {
//   candidateName = $("#candidate").val();
//   contractInstance.methods.voteForCandidate(candidateName)


//   contractInstance.methods.voteForCandidate(candidateName, {from: web3.eth.accounts[0]}, function() {
//     let div_id = candidates[candidateName];
//     contractInstance.methods.totalVotesFor(name).call((err, res) => {
//     $("#" + div_id).html(res);
//     })
//   })
// }
function voteForCandidate() {
  candidateName = $("#candidate").val();
  contractInstance.methods.voteForCandidate(candidateName)


  contractInstance.methods.voteForCandidate(candidateName).send({from: '0xcB95417EE2124D4e7C5C3Ab56cc8b198BD1E553F'})
  .on("receipt", function(receipt) {
    contractInstance.methods.totalVotesFor(candidateName).call((err, res) => {
    $("#" + candidates[candidateName]).html(res);
    })
  })
}


$(document).ready(function() {
  candidateNames = Object.keys(candidates);
  for (var i = 0; i < candidateNames.length; i++) {
    let name = candidateNames[i];
    contractInstance.methods.totalVotesFor(name).call((err, res) => {
    $("#" + candidates[name]).html(res);
    })
  }
});
 