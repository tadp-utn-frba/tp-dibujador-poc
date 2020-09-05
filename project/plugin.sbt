scalacOptions ++= Seq("-unchecked", "-deprecation")

resolvers += "Artima Maven Repository" at "https://repo.artima.com/releases"
// [https://github.com/sbt/sbteclipse]
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

addSbtPlugin("com.artima.supersafe" % "sbtplugin" % "1.1.10")