package dockerDNSUpdater.infrastructure.Http4sPublicIpInterpreter

import dockerDNSUpdater.infrastructure.InetAddressCodec._
import dockerDNSUpdater.domain.PublicIp.aggregate.IPv4Address
import io.circe.generic.semiauto._
import io.circe.{Decoder, Encoder}
import org.http4s.EntityDecoder
import org.http4s.circe.jsonOf

/**
  * Created by Lance on 10/06/2017.
  */
private[Http4sPublicIpInterpreter] object IPv4AddressCodec {
  implicit val decodeIPv4Address: Decoder[IPv4Address] =
    deriveDecoder[IPv4Address]
  implicit val encodeIPv4Address: Encoder[IPv4Address] =
    deriveEncoder[IPv4Address]

  implicit val iPv4AddressDecoder: EntityDecoder[IPv4Address] =
    jsonOf[IPv4Address]

}
