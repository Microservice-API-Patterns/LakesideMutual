# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Risk Management client

The Risk Management service allows the staff of Lakeside Mutual to  download a customer data report periodically to help them during risk assessment.

## IDE

To view and edit the source code, we recommend the cross-platform code editor [Visual Studio Code](https://code.visualstudio.com/). Other IDEs might
work as well, but this application has only been tested with VS Code.

## Installation

The Risk Management client is implemented in [Node.js](https://nodejs.org). To get started,
first install Node.js and then use the Node Package Manager ([http://npmjs.com/](http://npmjs.com/)) to install the dependencies:

1.  Install Node.js (see [https://nodejs.org](https://nodejs.org) for installation instructions)
2.  In the directory where this README is located, run `npm install` to install the application's dependencies into the local `node_modules` folder. Warnings about missing optional dependencies can safely be ignored.

Now you are ready to launch the Risk Management client.

## Launch Application

The Risk Management client will connect to the [gRPC](https://grpc.io/) service provided by the Risk Management server. Therefore, you should make sure that the Risk Management server is running before you start using the Risk Management client. Then,  in the `risk-management-client` directory, you can download a customer data report by running `./riskmanager run /tmp/report.csv` (on Windows, use the `riskmanager.bat` file instead: `.\riskmanager.bat run \Temp\report.csv`).

**Warning:** Note that the customer data report only includes data about customers that have at least one insurance policy. Also, when the address of a customer changes, this update is only reflected in the customer data report once one of the customer's policies has changed or a new policy has been added.