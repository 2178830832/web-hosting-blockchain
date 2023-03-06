pragma solidity ^0.8.10;
// SPDX-License-Identifier: UNLICENSED
 
contract Voting {
 
    string[] private candidateList;
    mapping (string => uint8) private votesReceived;
    
    constructor(string[] memory candidateNames) {
        candidateList = candidateNames;
    }
 
    function voteForCandidate(string memory candidate) public {
        require(validCandidate(candidate));
        votesReceived[candidate] += 1;
    }
 
    function totalVotesFor(string memory candidate) view public returns (uint8) {
        require(validCandidate(candidate));
        return votesReceived[candidate];
    }
 
    function validCandidate(string memory candidate) view public returns (bool) {
        for(uint8 i = 0; i < candidateList.length; i++) {
            if (bytes(candidateList[i]).length != bytes(candidate).length) {
                continue;
            }
            for (uint j = 0; j < bytes(candidateList[i]).length; j++) {
                if(bytes(candidateList[i])[j] != bytes(candidate)[j]) {
                    break;
                }
                return true;
            }
        }
        return false;
    }
    
}