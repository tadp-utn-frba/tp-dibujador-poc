// Name of the project
name := "TADP Draw PoC"

// Project version
version := "1.0.0"

// Version of Scala used by the project
scalaVersion := "3.3.1"

// Add dependency on ScalaFX library
libraryDependencies += "org.scalafx" %% "scalafx" % "16.0.0-R24"

resolvers += Resolver.sonatypeRepo("snapshots")

// set the main class for 'sbt run'
mainClass in (Compile, run) := Some("tadp.drawing.TADPDrawingApp")

scalacOptions ++= Seq("-unchecked", "-deprecation", "-Xcheckinit", "-encoding", "utf8", "-feature")

// Fork a new JVM for 'run' and 'test:run', to avoid JavaFX double initialization problems
fork := true

libraryDependencies ++= {
  // Determine OS version of JavaFX binaries
  lazy val osName = System.getProperty("os.name") match {
    case n if n.startsWith("Linux") => "linux"
    case n if n.startsWith("Mac") => "mac"
    case n if n.startsWith("Windows") => "win"
    case _ => throw new Exception("Unknown platform!")
  }
  Seq("base", "controls", "fxml", "graphics", "media", "swing", "web")
    .map(m => "org.openjfx" % s"javafx-$m" % "16" classifier osName)
}

//ScalaTest

resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"

libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.19"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.19" % "test"


