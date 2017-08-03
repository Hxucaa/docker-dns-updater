package dockerDNSUpdater.infrastructure

import org.http4s.client.blaze.BlazeClientConfig

case class Http4sConfig(maxTotalConnections: Int = 10,
                        blazeClientConfig: BlazeClientConfig = BlazeClientConfig.defaultConfig)
