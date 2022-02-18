# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Customer Self-Service Frontend

The Customer Self-Service frontend allows users to register themselves, view their current insurance policy and change their address.

## Editor

To view and edit the source code, we recommend the cross-platform code editor [Visual Studio Code](https://code.visualstudio.com/). Other IDEs might work as well, but this application has only been tested with VS Code. The code that calls the backend APIs can be found in the source files in [src/api](src/api).

## Installation

The Customer Self-Service frontend is a [React](https://reactjs.org/) application and its dependencies are managed with an [npm package](https://www.npmjs.com/) as indicated by the `package.json` file. To get started, first install [Node.js](https://nodejs.org) (which includes npm) and then use npm to install the application's dependencies (which includes React):

1.  Install Node.js (see [https://nodejs.org](https://nodejs.org) for installation instructions)
2.  In the directory where this README is located, run `npm install` to install the application's dependencies into the local `node_modules` folder. Warnings about missing optional dependencies can safely be ignored.

Now you are ready to launch the Customer Self-Service frontend.

## Launch Application

First you need to start the [Customer Self-Service](../customer-self-service-backend) backend and the [Policy Management backend](../policy-management-backend), because the Customer Self-Service frontend depends on these two services. For instructions on how to start these two services, consult their respective README files.

Run the command `npm start` in order to launch the Customer Self-Service frontend. This will start a development server and automatically loads the application's home page (http://localhost:3000/ by default) in a new browser tab. By default, the application starts on port 3000. If this port is already used by a different application, you can change it in the `.env` file.

To discover the location of the backend services, two variables are used (see [.env](.env)). They can be overridden using environment variables. When running in production, the [React Env](https://github.com/andrewmclagan/react-env) be used to handle this. See the [Dockerfile](Dockerfile) and [entrypoint.sh](entrypoint.sh) for details.

To stop the application press `Ctrl+C` in the shell that was used to start the application and close the corresponding browser tab. Note that this only stops the Customer Self-Service frontend but not the Customer Self-Service backend or the Policy Management backend.
