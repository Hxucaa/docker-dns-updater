package dockerDNSUpdater.infrastructure.Http4sPublicIpInterpreter

import dockerDNSUpdater.domain.PublicIp.aggregate.IPv4Address
import dockerDNSUpdater.infrastructure.Http4sClient
import IPv4AddressCodec._
import fs2.Task
import org.http4s.Uri

import scala.concurrent.ExecutionContext

/**
  * Created by Lance on 18/05/2017.
  */
class PublicIpRequester(private val client: Http4sClient)(private implicit val ec: ExecutionContext) {

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private val uri: Uri = Uri.uri("https://api.ipify.org?format=json")

  def getIp: Task[IPv4Address] =
    client.client
      .expect[IPv4Address](uri)
}
