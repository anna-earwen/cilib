import AssemblyKeys._

assemblySettings

test in assembly := {}

name := "cilib-simulator"

description := "Simulator environment fo running experiments using the CIlib library"

scalacOptions += "-deprecation"

parallelExecution in Test := false

mainClass := Some("net.sourceforge.cilib.simulator.Main")

libraryDependencies ++= Seq(
    "junit" % "junit" % "4.10" % "test",
    "com.novocode" % "junit-interface" % "0.10-M4" % "test",
    "org.mockito" % "mockito-all" % "1.9.5" % "test",
    "org.hamcrest" % "hamcrest-all" % "1.1" % "test"
)

autoScalaLibrary := false

// Handle the scala compiler dependency
//libraryDependencies <<= (scalaVersion, libraryDependencies) { (sv, deps) =>
//    deps :+ ("org.scala-lang" % "scala-compiler" % sv)
//}

resourceDirectory in Test <<= baseDirectory { _ / "simulator" }

seq(netbeans.NetbeansTasks.netbeansSettings:_*)
