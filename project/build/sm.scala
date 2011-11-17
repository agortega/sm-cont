


import sbt._
import Process._
import java.io._



class SmContProject(info: ProjectInfo) extends DefaultProject(info) with AutoCompilerPlugins {
  
  val cont = compilerPlugin("org.scala-lang.plugins" % "continuations" % "2.9.0")
  override def compileOptions = super.compileOptions ++ compileOptions("-P:continuations:enable")
  
  /* benchmarking support */
  
  def benchcomm(args: String) = "java -Xmx1500m -Xms1500m -server -cp %s:%s:%s %s".format(
    packageTestJar,
    buildScalaInstance.libraryJar,
    jarPath,
    args)
  
  def loginfo(msg: String) = log.log(Level.Info, msg)
  
  def runsync(comm: String) {
    loginfo("running: %s".format(comm))
    comm !;
  }
  
  lazy val bench = task { args =>
    task {
      runsync(benchcomm(args.mkString(" ")))
      None
    } dependsOn (`package`, packageTest)
  }
  
}
