name := "docker-dns-updater"

version := "1.0"

scalaVersion := "2.12.3"

scalacOptions ++= Seq(
  "-P:splain:implicits:false",
  "-deprecation", // warn about deprecated code
  "-encoding",
  "UTF-8", // UTF-8 should be the default file encoding everywhere
  "-explaintypes", // explain type errors with more details
  "-feature", // should enable Scala features explicitly
  "-language:higherKinds", // higher-kinded types are useful with typeclasses
  "-language:implicitConversions", // implicit parameters and classes are useful
  "-unchecked",                    // static code shouldn't depend on assumptions
//  "-Xfatal-warnings",             // warnings SHOULD be errors - it will help with code smells
  "-Xfuture", // enable future language features
  "-Xlint:delayedinit-select", // selecting member of DelayedInit
  "-Xlint:doc-detached", // warn when a scaladoc is detached from its element
  "-Xlint:infer-any", // Any shouldn't be infered - it's unsafe
  "-Xlint:missing-interpolator", // a variable isn't defined in a string interpolation
  "-Xlint:nullary-override", // warn when 'def f()' overrides 'def f'
  "-Xlint:nullary-unit", // warn when nullary methods return with Unit
  "-Xlint:private-shadow", // something shadows a private member
  "-Xlint:stars-align", // pattern sequence wildcard must align with sequence component
  "-Xlint:type-parameter-shadow", // something local shadows a type parameter
  "-Xlint:unsound-match", // the used pattern matching is unsafe
  "-Ywarn-dead-code", // warn about unused code
  "-Ywarn-extra-implicit", // there should be a max of 1 implicit parameter for each definition
  "-Ywarn-inaccessible", // warn about inaccessible types in method signatures
  "-Ywarn-unused:imports", // warn about unused imports
  "-Ywarn-unused:locals", // warn about unused local variables
  "-Ywarn-unused:params", // warn about unused parameters
  "-Ywarn-unused:patvars", // warn about unused pattern matching variables
  "-Ywarn-unused:privates", // warn about unused private members
  "-Ywarn-value-discard" // warn when a non-Unit expression is unused
)

// Note that the REPL can?t really cope with -Ywarn-unused:imports or -Xfatal-warnings so you should turn them off for
// the console.
scalacOptions in (Compile, console) ~= (_.filterNot(
  Set(
    "-Ywarn-unused:imports",
    "-Xfatal-warnings"
  )))

scalacOptions := Seq("-Ypartial-unification") //if running 2.12

// more at wartremover.org/doc/warts.html
wartremoverErrors ++= Seq(
  Wart.AsInstanceOf, // type conversion hurts typesafety
  Wart.EitherProjectionPartial, // the 'get' method can throw an exception
  Wart.Enumeration, // Scala's enumerations are slow, use ADTs
  Wart.ExplicitImplicitTypes, // implicits must have explicit type annotations
  Wart.FinalCaseClass, // case class must be sealed - they meant to be simple data types
  Wart.FinalVal, // final vals cause inconsistency during incremental compilation
  Wart.ImplicitConversion, // implicit conversions are dangerous
  Wart.IsInstanceOf, // pattern matching is safer
  Wart.JavaConversions, // use java collections explicitly
  Wart.LeakingSealed, // the subclasses of sealed traits must be final to avoid leaking
  Wart.MutableDataStructures, // mutable data structures in Scala are generally useless
  Wart.Null, // null is unsafe and useless in Scala
  Wart.OptionPartial, // don't use Option's get method, it might throw exceptions
  Wart.Return, // return is spaghetti(and breaks referential transparency)
  Wart.StringPlusAny, // concatenate only a String with an other String
  Wart.Throw, // don't throw exceptions, use Either or Option
  Wart.TraversableOps, // get, head, tail etc. are unsafe - possible exceptions
  Wart.TryPartial, // Try's get is unsafe
  Wart.Var,
  Wart.While // these are only useful at micro-optimizations, use tail recursion instead
)

resolvers ++= Seq(
  Resolver.bintrayRepo("projectseptemberinc", "maven"),
  Resolver.sonatypeRepo("releases")
//  Resolver.sonatypeRepo("snapshots")
)

lazy val paradiseVersion = "2.1.0"
//lazy val fetchVersion = "0.6.1"
lazy val catsVersion            = "0.9.0"
lazy val monixVersion           = "2.3.0"
lazy val macwireVersion         = "2.3.0"
lazy val circeVersion           = "0.8.0"
lazy val catbirdVersion         = "0.14.0"
lazy val http4sVersion          = "0.17.0-M3"
lazy val fs2catsVersion         = "0.3.0"
lazy val shapelessVersion       = "2.3.2"
lazy val enumeratumVersion      = "1.5.12"
lazy val enumeratumCirceVersion = "1.5.14"
lazy val refinedVersion         = "0.8.2"
//lazy val freekVersion = "0.6.5"
lazy val kindProjectorVersion = "0.9.4"
lazy val pureconfigVersion    = "0.7.2"

libraryDependencies ++=
  Seq(
    compilerPlugin("org.scalamacros"       % "paradise" % paradiseVersion cross CrossVersion.full),
    compilerPlugin("org.psywerx.hairyfotr" %% "linter"  % "0.1.17")
    //    compilerPlugin("io.tryp"               %% "splain" % "0.2.5")
  ) ++
    Seq(
//    "com.47deg" %% "fetch" % fetchVersion,
      "co.fs2"      %% "fs2-cats"  % fs2catsVersion,
      "com.chuusai" %% "shapeless" % shapelessVersion,
//    "com.projectseptember" %% "freek" % freekVersion,
      "org.spire-math"        %% "kind-projector" % kindProjectorVersion,
      "com.github.pureconfig" %% "pureconfig"     % pureconfigVersion
    ) ++
    Seq(
      "eu.timepit" %% "refined",
      "eu.timepit" %% "refined-pureconfig"
    ).map(_ % refinedVersion) ++
    Seq(
      "com.softwaremill.macwire" %% "macros" % macwireVersion % "provided"
//    "com.softwaremill.macwire" %% "utils"
    ) ++
    Seq(
      "org.http4s" %% "http4s-dsl",
      "org.http4s" %% "http4s-circe",
      "org.http4s" %% "http4s-blaze-client"
    ).map(_ % http4sVersion) ++
//  Seq(
//    "io.monix" %% "monix-eval",
//    "io.monix" %% "monix-cats"
//  ).map(_ % monixVersion) ++
    Seq(
      "io.circe" %% "circe-core",
      "io.circe" %% "circe-generic",
      "io.circe" %% "circe-parser",
      "io.circe" %% "circe-literal",
      "io.circe" %% "circe-java8",
      "io.circe" %% "circe-generic-extras",
      "io.circe" %% "circe-refined"
    ).map(_ % circeVersion) ++
    Seq(
      "com.beachape" %% "enumeratum"       % enumeratumVersion,
      "com.beachape" %% "enumeratum-circe" % enumeratumCirceVersion
    ) ++
    Seq(
      "org.typelevel" %% "cats",
      "org.typelevel" %% "cats-free"
    ).map(_ % catsVersion)
