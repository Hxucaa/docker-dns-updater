package dockerDNSUpdater

/**
  * Created by Lance on 12/06/2017.
  *
  * Centralized compile-time dependency management.
  */
trait DependencyManagerModule {

  import dockerDNSUpdater.utility.Config.ConfigLoader
  import com.softwaremill.macwire.wire

  lazy val config = wire[ConfigLoader]
}

trait Http4sClientModule {

  import dockerDNSUpdater.infrastructure.{Http4sClient, Http4sConfig}
  import com.softwaremill.macwire.wire

  lazy val http4sClient =
    (config: Http4sConfig) => wire[Http4sClient]

}

trait ConfiguredHttp4sClientModule extends DependencyManagerModule with Http4sClientModule {

  lazy val client = http4sClient(config.http4sConfig)
}

trait Http4sEC {

  // TODO: implement a separate execution context for i/o
  implicit val ec = scala.concurrent.ExecutionContext.Implicits.global
}

trait Http4sCloudFlareDNSInterpreterModule extends ConfiguredHttp4sClientModule with Http4sEC {

  import com.softwaremill.macwire._
  import dockerDNSUpdater.infrastructure.CloudFlareDNS.interpreter.Http4s.DNSRequester
  import dockerDNSUpdater.infrastructure.CloudFlareDNS.interpreter.Http4s.Http4sDNSRepoInterpreter

  lazy val dnsRequester   = wire[DNSRequester]
  lazy val dnsinterpreter = wire[Http4sDNSRepoInterpreter]
}

trait Http4sPublicIpInterpreterModule extends ConfiguredHttp4sClientModule with Http4sEC {

  import com.softwaremill.macwire._
  import dockerDNSUpdater.infrastructure.Http4sPublicIpInterpreter.PublicIpRequester
  import dockerDNSUpdater.infrastructure.Http4sPublicIpInterpreter.Http4sPublicIpRepoInterpreter

  lazy val publicIpRequester   = wire[PublicIpRequester]
  lazy val publicIpinterpreter = wire[Http4sPublicIpRepoInterpreter]
}

trait LoggingModule {

  import com.softwaremill.macwire._
  import dockerDNSUpdater.utility.Logging.interpreter.LoggingPrintAsyncI

  lazy val logging = wire[LoggingPrintAsyncI]

}

trait DNSUpdateModule
    extends LoggingModule
    with Http4sCloudFlareDNSInterpreterModule
    with Http4sPublicIpInterpreterModule {

  import dockerDNSUpdater.domain.DNSUpdate.DNSUpdateRepo
  import com.softwaremill.macwire._

  lazy val dnsUpdateRepo = wire[DNSUpdateRepo]
}

trait DNSUpdateServiceModule extends DNSUpdateModule {

  import dockerDNSUpdater.application.DNSConfig
  import dockerDNSUpdater.application.DNSUpdateService
  import com.softwaremill.macwire._

  lazy val service = (config: DNSConfig) => wire[DNSUpdateService]
}

trait ConfiguredDNSUpdateServiceModule extends DependencyManagerModule with DNSUpdateServiceModule {

  import com.softwaremill.macwire._

  lazy val configuredService = service(config.dnsConfig)
}

object DependencyManager extends ConfiguredDNSUpdateServiceModule {}
