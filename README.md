# SimpleContentRepo
a simple test project for web content repository

# Get Start
Please clone or download this project to your local. Say C:/git/SimpleContenRepo
Type the command below

<code>cd c:/git</code>

<code>git clone https://github.com/chooli/SimpleContentRepo.git</code>

# Prerequisites 
Please install jdk 1.8 on your local machine. And type to following command to verify it

<code>java -version</code>

# How to Install

1. Install gradle on your machine. For details, please refer to https://gradle.org/install/
   
   Once you finish this step, open a terminal and type the following command
   
   <code>gradle -v</code>
   ------------------------------------------------------------
   Gradle 3.5
   ------------------------------------------------------------

2. Install Elasticsearch on your local machine
  I tested this application with version 5.6.9. Suggest you test it with 5.6.x.
  Once you download Elasticsearch binary and run it successfully, continue install Tomcat server version 8.5.x.
   
3. Build the project
   Got to the project home path in a terminal
   
   <code>cd c:/git/SimpleContentRepo</code>
   
   <code>gradle clean build</code>
   
   Once you finish the build, you should see the following job are done without error
   
   :clean
   
   :compileJava
   
   :processResources
   
   :classes
   
   :war
   
   :assemble
   
   :compileTestJava
   
   :processTestResources NO-SOURCE
   
   :testClasses
   
   :test
   
   :check
   
   :build
  
  It means it build successfully and passed all test cases
    
  4.Copy the simplecontentrepo.war file from build folder to tomcat webapps folder on your local machine. 
   
    Start the tomcat server
    
  5. Open a web browser and access the following link
  
     http://localhost:8080/simplecontentrepo/index.html
     
     From there, you could see a simple introduction and how to use it.

