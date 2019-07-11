# Media Server
a simple media server provides media storage on local file system as well as cloud-based file system. It also provide media accessing API and streaming services

# Get Start
Please clone or download this project to your local. Say C:/git/media-server
Type the command below

<code>cd c:/git</code>

<code>git clone https://github.com/chooli/media-server.git</code>

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
   
   <code>cd c:/git/media-server</code>
   
   <code>gradle clean bootJar</code>
   
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
    
  4.Start microservices by typing the following command. 
   
    java -jar build/libs/media-server-0.9.0.jar
    
  5. Open a web browser and access the GraphiQL console
  
     http://localhost:10080/index.html
     
     From there, you could explor the API which it provides.

