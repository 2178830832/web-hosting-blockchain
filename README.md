# `web-hosting-blockchain`

- [Overview](#overview)
- [Getting Started](#start)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
    - [Docker](#docker)
    - [Ethereum](#ethereum)
    - [IPFS](#ipfs)
    - [Use the Packages](#packages)
    - [Use the IDEA](#idea)
- [Usage](#usage)
- [Demo](demo)
- [API](#api)
- [Limitations](#limitations)
- [License](#license)

## Overview

Web developers generally upload the content of their websites to a central server. The central server serves the requests of all the clients. This approach may raise performance, security, and robustness issues. As well as, the website becomes unavailable during maintenance and heavy workload time. Therefore, in this project, we decided to split website content into granules and distribute it all over the internet and then link them together using a blockchain registry.

The `web-hosting-blockchain` is built using Spring Boot and Thymeleaf. By connecting to the IPFS, Docker and Ethereum server, one can upload a website and host it on multiple distributed nodes. Regarding the data source, this application does not employ traditional SQL databases. Instead, it stores user data on Ethereum Blockchain and uses encryption to prevent it from leakage.

## Getting Started

<a name="start"><a>

### Prerequisites

* [Java SE 8](https://www.oracle.com/java/technologies/javase/javase8-archive-downloads.html)
* (Optional, if you want to manipulate the project) [IntelliJ IDEA](https://www.jetbrains.com.cn/en-us/idea/download)
* An Ethereum server (e.g. [Ganache](https://trufflesuite.com/ganache/))
* An OS that has installed [Docker](https://docs.docker.com/get-docker/)

### Installation

#### Docker

_**Note**: you may need to gain the Root Access first. Check [here](https://www.wikihow.com/Become-Root-in-Linux) for instructions._

This application uses [docker-java](https://github.com/docker-java/docker-java) to control the Docker API, which requires the Docker server to expose its port, if it is running on a remote machine. To do this, take the following steps:

**1. Navigate to the `docker.service` file**

```sh
$ vi /lib/systemd/system/docker.service
```

**2. Find the line `Execstart=/usr/bin/dockerd`, and insert `-H tcp://0.0.0.0:2375`**

![vi](.\img\vi.png)

**3. Execute the following commands to restart the Docker server**

```sh
$ systemctl daemon-reload
$ service docker restart
```

**4. Execute the following command to check if your Docker server is listening on the `2375` port**

```sh
$ systemctl status docker
```

![status](.\img\status.png)

**5. If your firewall is running, then execute the following commands to open the `2375` port**

```sh
$ firewall-cmd --permanent --add-port=2375/tcp
$ firewall-cmd --reload
```

#### Ethereum

**1. Start your Ethereum server, get the server address and account**

In theory, you can use any type of Ethereum server, provided that it is supported by [Web3j](https://github.com/web3j/web3j). The following example uses [Ganache](https://trufflesuite.com/ganache/), which can easily create Ethereum accounts for simulation.

![ganache](.\img\ganache.png)

_**Tip**: you can change the server address (port) to a custom one._

**2. Compile and deploy this [contract](.\contracts\Database.sol) to the Ethereum, get the contract address**

The following example uses [Remix](https://remix.ethereum.org/), a web-based Solidity IDE that does not require installation.

<img src=".\img\remix.png" alt="remix" style="zoom:50%;" />

#### IPFS

**1. Download and install [IPFS](https://docs.ipfs.tech/install/) according to your OS**

**2. Execute the following command to expose IPFS API**

```sh
$ ipfs config Addresses.API /ip4/0.0.0.0/tcp/5001
```

**3. Execute the following commands to start IPFS network**

```sh
$ ipfs init
$ ipfs daemon
```

**4. Pull the IPFS image from [Docker Hub](https://hub.docker.com/r/ipfs/kubo/)**

#### Use the Packages

<a name="packages"><a>

The pre-built packages are available in the `Releases`. You can easily execute them via the following commands:

```sh
$ java -jar .\tester-1.0.2.jar
$ java -jar .\dashboard-1.0.2.jar
```

 **Note**: the two packages are not dependent on each other, so there is no specific execution sequence.

#### Use the IDEA

<a name="idea"><a>

If you want to manually build the application, then take the following steps:

**1. Download / Clone this [repository](https://github.com/2178830832/web-hosting-blockchain) to your disk**

**2. Open IDEA and import this repository as a project,  wait for the Maven resolving the dependencies**

**3. Navigate to the [`application.properties`](.\dashboard\src\main\resources\application.properties) (dashboard) and [`application.properties`](.\dashboard/tester/src/main/resources/application.properties) (tester), set your custom configurations**

- Dashboard
  - `ipfs.address`: IPFS server address, e.g.` /ip4/127.0.0.1/tcp/5001`
  - `web3.address`: Ethereum server address, e.g. `http://127.0.0.1:8545`
  - `web3.contract`: Ethereum contract, e.g. `0x3FA91428faF81CBDE632CB788A9e4673F05dF2dF`
  - `web3.account`: Ethereum account, e.g. `0xa2Cba398E8E4378803b071c68556D24bE51D4B0b`
  - `docker.address`: Docker server address, e.g. `tcp://127.0.0.1:2375`
- Tester
  - `chrome.version`: Chrome Browser version, e.g. `110.0.5481.100`
  - `firefox.version`: Firefox Browser version, e.g. `110.0`

**4. Run the [`DashboardApplication`](./dashboard/src/main/java/pers/yujie/dashboard/DashboardApplication.java) and [`TesterApplication`](./dashboard/tester/src/main/java/pers/yujie/tester/TesterApplication.java)**

**5. Open <u>http//localhost:8000</u> in your browser**

## Usage

The `web-hosting-blockchain` provides five interfaces:

* `Configuration`: set custom addresses for IPFS, Ethereum, and Docker
* `Website`: upload, update and delete websites
* `Node`: create, update and delete nodes
* `Cluster`: display cluster information
* `Test`: run tests on hosted websites

Note: this application uses [`web-vitals`](https://github.com/GoogleChrome/web-vitals) to test websites. To do this, you need to insert the library to the website code:

```javascript
import * as WebVitals from 'web-vitals'

function sendReport(axios) {

  function sendToAnalytics(metric) {
    console.log(metric)
    axios.post('/test/receive', JSON.stringify(metric)).then(
        r => console.log(r))
  }

  WebVitals.onTTFB(r => sendToAnalytics(r))
  WebVitals.onFCP(r => sendToAnalytics(r))
  WebVitals.onLCP(r => sendToAnalytics(r))
}

export {sendReport}
```

_**Note**: the above code snippet is for demonstration only; you can write your own code based on `Node.js` or other languages. There is a `Vue.js` project available in the [website](./website) directory._

## Demo

In the [demo](./demo) directory, you can find a simple `DApp` that includes three separate files:

* `demo.html`: HTML interface
* `index.js`: JavaScript code
* `demo.sol`: sample smart contract

Take the following steps to configure this demo:

**1. Compile and deploy the `demo.sol` with the constructor parameter `["Alice", "Bob", "Cary"]`, get the contract address**

**2. In the `index.js`, change three lines of code accordingly**

```javascript
// set the custom Ethereum server address
web3 = new Web3(new Web3.providers.HttpProvider("HTTP://127.0.0.1:8545"));

// set the Ethereum contract address
address = '0xDAbd503C218a0222e5f5EDD18dD141CD9a076509';

// set the Ethereum account address 
contractInstance.methods.voteForCandidate(candidateName).send({from:'0xcB95417EE2124D4e7C5C3Ab56cc8b198BD1E553F'})
```

**3. Open the `demo.html`, you should be able to see a Voting application**

**4. Upload the `demo` directory using the `web-hosting-blockchain`**

## API

[Javadoc](./docs/index.html) for references

## Limitations

The `web-hosting-blockchain` is primarily a boilerplate project that is used to simulate decentralised web hosting, which means that it is not designed for commercial purposes. Particularly, its performance depends on the OS and network quality, so there is no guarantee that it will work very well under certain environment. After empirical evaluation, apparently the number of nodes in a decentralised network can impact the overall speed. Therefore, having as many nodes as possible is a prerequisite, if you want to have better performance.

Regarding dynamic websites, one needs to manually write and deploy the data source on Blockchain, which is not as easy as SQL databases. Future improvement may focus on creating a command line tool or an interface that allows users to create data tables.

## License

[MIT](/LICENSE)

