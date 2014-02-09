import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

  val appName         = "wootstar-server-bl"
  val appVersion      = "1.5"

  val appDependencies = Seq(
    // Add your project dependencies here,
    javaCore,
    javaJdbc,
    javaEbean,
    "redis.clients" % "jedis" % "2.1.0",
    "postgresql" % "postgresql" % "9.1-901-1.jdbc4",
    "com.codahale.metrics" % "metrics-core" % "3.0.1",
    "com.codahale.metrics" % "metrics-healthchecks" % "3.0.1",
    "com.codahale.metrics" % "metrics-jvm" % "3.0.1",
    "com.codahale.metrics" % "metrics-graphite" % "3.0.1",
    "com.codahale.metrics" % "metrics-json" % "3.0.1",
    "com.logentries" % "logentries-appender" % "1.1.20"
  )

  val main = play.Project(appName, appVersion, appDependencies).settings(
    // Add your own project settings here
    //resolvers += "Maven repository" at "http://morphia.googlecode.com/svn/mavenrepo/",
    //resolvers += "MongoDb Java Driver Repository" at "http://repo1.maven.org/maven2/org/mongodb/mongo-java-driver/"

    sources in doc in Compile := List()
  )

}
