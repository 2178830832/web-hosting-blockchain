pragma solidity ^0.8.10;
// SPDX-License-Identifier: UNLICENSED

contract Database {

    string private websites;

    string private clusters;
   
    string private nodes;

    function getWebsites() view external returns (string memory) {
        return websites;
    }

    function setWebsites(string memory _websites) external {
        websites = _websites;
    }

    function getClusters() view external returns (string memory) {
        return clusters;
    }

    function setClusters(string memory _clusters) external {
        clusters = _clusters;
    }

    function getNodes() view external returns (string memory) {
        return nodes;
    }

    function setNodes(string memory _nodes) external {
        nodes = _nodes;
    }

}