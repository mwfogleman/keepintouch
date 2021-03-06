name := "keepintouch"

version := "3.0.0"

libraryDependencies ++= Seq(
  "org.specs2" %% "specs2" % "1.12.3" % "test")

resolvers ++= Seq("snapshots" at "http://oss.sonatype.org/content/repositories/snapshots",
                  "releases"  at "http://oss.sonatype.org/content/repositories/releases")
