package dockerDNSUpdater.utility.Config

import com.typesafe.config.ConfigFactory

class ConfigLoader {
  import dockerDNSUpdater.application.{CloudFlareCredential, DNSConfig, Zone}
  import dockerDNSUpdater.domain.util.RefinedTypes.{NonEmptyString, PositiveInt}
  import dockerDNSUpdater.infrastructure.Http4sConfig
  import scala.concurrent.duration._
  import pureconfig._
  import eu.timepit.refined.refineMV
  import eu.timepit.refined.numeric.Positive
  import eu.timepit.refined.pureconfig._
  import org.http4s.client.blaze.BlazeClientConfig

  case class CredentialConf(cloudflare: CloudFlareConf)

  case class CloudFlareConf(email: NonEmptyString, key: NonEmptyString, zone: ZoneConf)
  case class ZoneConf(id: NonEmptyString)

  case class AppConf(http4s: Http4sConf)

  case class Http4sConf(maxTotalConnections: PositiveInt = refineMV[Positive](10),
                        blazeClient: Http4sBlazeClientConf = Http4sBlazeClientConf())
  case class Http4sBlazeClientConf(idleTimeout: Duration = 60.seconds,
                                   requestTimeout: Duration = Duration.Inf,
                                   bufferSize: PositiveInt = refineMV[Positive](8 * 1024))

  /**
    * If configuration is not properly loaded, abort program.
    * Load application configurations.
    */
  private val appConf: AppConf = loadConfigOrThrow[AppConf](ConfigFactory.load("application"))

  /**
    * If configuration is not properly loaded, abort program.
    * Load credentials.
    */
  private val credentialConf: CredentialConf =
    loadConfigOrThrow[CredentialConf](ConfigFactory.load("credential"))
  private def cloudflare  = credentialConf.cloudflare
  private def blazeClient = appConf.http4s.blazeClient

  private def email: NonEmptyString  = cloudflare.email
  private def key: NonEmptyString    = cloudflare.key
  private def zoneId: NonEmptyString = cloudflare.zone.id
  def dnsConfig: DNSConfig           = DNSConfig(CloudFlareCredential(email, key), Zone(zoneId))

  private def maxTotalConnections: Int = appConf.http4s.maxTotalConnections.value
  private def blazeClientConfig: BlazeClientConfig =
    BlazeClientConfig.defaultConfig.copy(
      idleTimeout = blazeClient.idleTimeout,
      requestTimeout = blazeClient.requestTimeout,
      bufferSize = blazeClient.bufferSize.value
    )
  def http4sConfig: Http4sConfig = Http4sConfig(maxTotalConnections, blazeClientConfig)
}
