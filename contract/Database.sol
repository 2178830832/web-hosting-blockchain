pragma solidity ^0.8.10;
// SPDX-License-Identifier: UNLICENSED

import "@openzeppelin/contracts/utils/Strings.sol";

contract Database {

    struct Website {
        string cid;
        string location;
        bool isOnline;
    }

    struct Cluster {
        string name;
        bool isHealthy;
        uint usedSpace;
        uint totalSpace;
    }

    struct Node {
        string clusterName;
        string name;
        bool isOnline;
        uint usedSpace;
        uint totalSpace;
    }
 
    Website[] private websites;
    Cluster[] private clusters;
    Node[] private nodes;
    
    constructor() {
        for (uint i = 0; i < 3; i++) {
            string memory clusterName = string.concat("cluster", Strings.toString(i));
            Cluster memory cluster = Cluster(clusterName, true, 0, 1000);
            string memory name = string.concat("ipfs", Strings.toString(i));
            Node memory node = Node(clusterName, name, true, 0, 1000);
            clusters.push(cluster);
            nodes.push(node);
        }
    }

    function selectAllWebsites() view external returns (Website[] memory) {
        return websites;
    }

    function selectAllClusters() view external returns (Cluster[] memory) {
        return clusters;
    }

    function selectAllNodes() view external returns (Node[] memory) {
        return nodes;
    }

    function insertWebsite(Website memory website) external {
        websites.push(website);
    }

    function updateClusterBatch(Cluster[] memory upClusters) external {
        for (uint8 i = 0; i < clusters.length; i++) {
            clusters[i] = upClusters[i];
        }
    }

    function updateNodeBatch(Node[] memory upNodes) external {
        for (uint8 i = 0; i < nodes.length; i++) {
            nodes[i] = upNodes[i];
        }
    }

    function updateNodeBatchByCluster(Node[] memory upNodes) external {
        require(upNodes.length >= 0);
        string memory clusterName = upNodes[0].clusterName;
        uint8 j = 0;
        for (uint8 i = 0; i < nodes.length; i++) {
            if (keccak256(bytes(nodes[i].clusterName)) == keccak256(bytes(clusterName))) {
                nodes[i] = upNodes[j++];
                if (j >= upNodes.length) {
                    return;
                }
            }
        }
    }

    function test1(Cluster memory cluster) external {
        
            clusters[0].name = cluster.name;
            clusters[0].isHealthy = cluster.isHealthy;
            clusters[0].usedSpace = cluster.usedSpace;
            clusters[0].totalSpace = cluster.totalSpace;
    } 

    function test2(Website[] memory upWebsites) external {
        
            for (uint8 i = 0; i < websites.length; i++) {
            websites[i] = upWebsites[i];
        }
    } 
    
}