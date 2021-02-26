# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Risk Management server

The Risk Management service allows the staff of the Lakeside Mutual insurance to  download a customer data report which helps them with risk assessments periodically.

## IDE

To view and edit the source code, we recommend the cross-platform code editor [Visual Studio Code](https://code.visualstudio.com/). Other IDEs might
work as well, but this application has only been tested with VS Code.

## Installation

The Risk Management server is implemented in [Node.js](https://nodejs.org). To get started, first install Node.js and then use the
Node Package Manager ([http://npmjs.com/](http://npmjs.com/)) to install the dependencies:

1.  Install Node.js (see [https://nodejs.org](https://nodejs.org) for installation instructions)
2.  In the directory where this README is located, run `npm install` to install the application's dependencies into the local `node_modules` folder. Warnings about missing optional dependencies can safely be ignored.

Now you are ready to launch the Risk Management server.

## Launch Application

The Risk Management server consumes [Document Messages](https://www.enterpriseintegrationpatterns.com/patterns/messaging/DocumentMessage.html) from the [Policy Management backend](../policy-management-backend) through an [ActiveMQ](http://activemq.apache.org/) message queue. Therefore, you should make sure that the Policy Management backend is running while you are using the Risk Management service. Then, in the `risk-management-server` directory, run `npm start`
to start the Risk Management server. <!-- using MOM/EIP terminology -->

The Policy Management backend puts a message on the ActiveMQ queue every time an insurance policy is created, updated or deleted. These messages are persisted in the file `data/data.json`.
If this file does not exist yet, the Risk Management server will load the default data from the file `data/initial-data.json`.
