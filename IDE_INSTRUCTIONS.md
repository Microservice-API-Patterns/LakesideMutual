# IDE Instructions

## Import Maven Project into IDE
Note: We assume you already cloned the corresponding application and have the source code on your local file system. We further assume that you installed Java and Maven on your system.

### IntelliJ IDEA
In IDEA you can import an existing Maven project with `File -> New -> Project From Existing Sources...`.

![Create Project from existing sources](./resources/screenshots/intellij-idea-import-maven-project-1.png)

A dialog opens on which you can choose the directory of your project (for example `~/source/LakesideMutual/customer-management-backend`). 

![Select Maven Project in IntelliJ IDEA Import Dialog](./resources/screenshots/intellij-idea-import-maven-project-2.png)

On the following import dialog, choose `Maven` and press `Finish`:

![Choose Maven in IntelliJ IDEA Import Dialog](./resources/screenshots/intellij-idea-import-maven-project-3.png)

### Visual Studio (VS) Code
Preconditions: in VS Code you will need a few extensions for Java development. We recommend to install the [Java Extension Pack](https://marketplace.visualstudio.com/items?itemName=vscjava.vscode-java-pack) which also includes support for Maven.

Use `File -> Open Folder...` to open the folder of the corresponding project in VS Code. 

![Open Folder in Visual Studio Code](./resources/screenshots/vs-code-import-maven-project-1.png)

VS Code asks you if you want to import the Java project; say _yes_ or _always_:

![Import Java Project in VS Code](./resources/screenshots/vs-code-import-maven-project-2.png)

After opening the folder and enabling the Java project you should be ready to work on the application. With the Maven panel (on the left side at the bottom of the explorer) you can also run Maven goals, such as `spring-boot:start`:

![Run Spring Boot Application in VS Code](./resources/screenshots/vs-code-import-maven-project-3.png)

### Eclipse / Spring Tool Suite (STS)
In case you use Eclipse, ensure that you use _Eclipse IDE for Java Developers_, _Eclipse IDE for Enterprise Java Developers_, or _Spring Tools 4 for Eclipse (STS)_. Those versions of Eclipse include required plugins such as the Maven plugin.

You can import a Maven project via `File -> Import...` and then choose `Maven -> Existing Maven Projects`. 

![Import Project in Eclipse](./resources/screenshots/eclipse-import-maven-project-1.png)

![Import Existing Maven Project in Eclipse](./resources/screenshots/eclipse-import-maven-project-2.png)

Set the directory of the project as root path and press `Finish`:

![Choose Existing Maven Project in Eclipse](./resources/screenshots/eclipse-import-maven-project-3.png)



## Import Node App into IDE
Our frontend applications (or the risk management server in LakesideMutual) typically use Node.js. For these apps, we recommend to use Visual Studio Code.

### VS Code
Use `File -> Open Folder...` to open the folder of the corresponding project in VS Code.

![Open Folder in Visual Studio Code](./resources/screenshots/vs-code-import-maven-project-1.png)

### IntelliJ IDEA
In IDEA you can import an existing Node.js project with `File -> New -> Project From Existing Sources...`:

![Create Project from existing sources](./resources/screenshots/intellij-idea-import-maven-project-1.png)

Choose the directory of the Node project you want to import:

![Choose existing node project](./resources/screenshots/intellij-idea-import-node-project-2.png)

Choose to create a _new project_:

![Choose to create a new project](./resources/screenshots/intellij-idea-import-node-project-3.png)

After that, press two times _Next_ and then _Finish_.

Your node project is imported and ready to run. With a right-click to the `package.json` file you can show npm scripts or run `npm install` directly:

![Run npm scripts in IntelliJ IDEA](./resources/screenshots/intellij-idea-import-node-project-4.png)

If you click _Show npm Scripts_, you can also start the application easily:

![Start Node.js application in IntelliJ IDEA](./resources/screenshots/intellij-idea-import-node-project-5.png)

### Eclipse
There might be plugins around to develop Node.js applications in Eclipse, but we do not really recommend this path.
