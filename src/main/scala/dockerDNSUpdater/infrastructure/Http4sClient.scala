package dockerDNSUpdater.infrastructure

import org.http4s.client.blaze.PooledHttp1Client

/**
  * Created by Lance on 12/06/2017.
  */
class Http4sClient(private val config: Http4sConfig) {

  private[infrastructure] val client =
    PooledHttp1Client(config.maxTotalConnections, config.blazeClientConfig)

}
