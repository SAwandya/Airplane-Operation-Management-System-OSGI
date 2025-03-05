Monorepo Setup for Producers and Consumers

This monorepo contains separate workspaces for different producers and consumers, all managed under a single Git repository. Each module is an independent project with its own functionality but shares common libraries and configurations.

Project Structure

monorepo-root/
|-- producers/
|   |-- producer1/
|   |-- producer2/
|
|-- consumers/
|   |-- consumer1/
|   |-- consumer2/
|
|-- shared-libs/
|   |-- slf4j-api-x.x.x.jar
|   |-- logback-classic-x.x.x.jar
|
|-- .gitignore
|-- README.md

Setup Instructions

1. Cloning the Repository

   git clone <repository-url>
   cd monorepo-root

   2. Importing Projects into Eclipse

Open Eclipse.

Go to File > Import > Existing Projects into Workspace.

Browse to the respective producers/ or consumers/ directory.

Select the project folders and click Finish.

3. Configuring Build Path

For each producer and consumer:

Right-click the project > Build Path > Configure Build Path.

Go to Libraries > Add External JARs.

Add slf4j-api-x.x.x.jar and logback-classic-x.x.x.jar from the libs/ folder.

Click Apply and Close.

4. Running the Projects

Right-click the project > Run As > Java Application.

Make sure the appropriate main() method is selected for execution.

5. Git Workflow

Adding Changes

git add .

Committing Changes

git commit -m "Add/update producer or consumer"

git push origin main

6. Common Issues

SLF4J import not resolved: Ensure the JARs are correctly added to the build path.

Workspace visibility: Make sure all projects belong to the same Eclipse workspace.

7. Future Enhancements

Consider converting the setup to a Maven multi-module project for easier dependency management.

Add unit tests and CI/CD pipelines.
