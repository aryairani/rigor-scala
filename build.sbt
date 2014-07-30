scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scalaz" %% "scalaz-core" % "7.0.6",
  "org.scalanlp" %% "breeze" % "0.8.1",
  "com.chuusai" % "shapeless" % "2.0.0" cross CrossVersion.full
)