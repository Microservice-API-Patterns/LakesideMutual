# ![Lakeside Mutual Logo](../resources/logo-32x32.png) Lakeside Mutual: Policy Management Frontend

The Policy Management frontend allows Lakeside Mutual employees to view and manage the insurance policies
of individual customers.

## Editor

To view and edit the source code, we recommend the cross-platform code editor [Visual Studio Code](https://code.visualstudio.com/). Other IDEs might work as well, but this application has only been tested with VS Code. The code that calls the backend APIs can be found in [src/api.js](src/api.js).

## Installation

The Policy Management frontend is a [Vue.js](https://vuejs.org/) application and its dependencies are managed with an [npm package](https://www.npmjs.com/) as indicated by the `package.json` file. To get started, first install [Node.js](https://nodejs.org) (which includes npm) and then use npm to install the application's dependencies (which includes Vue.js):

1.  Install Node.js (see [https://nodejs.org](https://nodejs.org) for installation instructions)
2.  In the directory where this README is located, run `npm install` to install the application's dependencies into the local `node_modules` folder. Warnings about missing optional dependencies can safely be ignored.

Now you are ready to launch the Policy Management frontend.

## Launch Application

First you need to start the Customer Self-Service backend and the Policy Management backend, because the Policy Management frontend depends on these two services. For instructions on how to start these two services, consult their respective README files.

Run the command `npm start` in order to launch the Policy Management frontend. This will start a development server on port 3010. Open http://localhost:3010/ in the browser to load the application's home page. If port 3010 is already used by a different application, you can change it in the file `vue.config.js`.

To discover the location of the backend services, two variables are used (see [.env](.env)). They can be overridden using environment variables. When running in production, [React Env](https://github.com/andrewmclagan/react-env) can be used to handle this. See the [Dockerfile](Dockerfile) and [entrypoint.sh](entrypoint.sh) for details.

To stop the application press `Ctrl+C` in the shell that was used to start the application and close the corresponding browser tab. Note that this only stops the Policy Management frontend but not the Customer Self-Service backend or the Policy Management backend.
