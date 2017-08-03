package dockerDNSUpdater.infrastructure.CloudFlareDNS.interpreter.Http4s

import dockerDNSUpdater.domain.CloudFlareDNS.common._
import dockerDNSUpdater.domain.util.RefinedTypes._
import io.circe.Decoder.Result
import io.circe.refined._
import io.circe.{Decoder, Encoder, HCursor}

/**
  * Created by Lance on 11/06/2017.
  */
private[Http4s] object DNSRecordCodec {
  implicit val decodeDNSId: Decoder[DNSId] = new Decoder[DNSId] {
    override def apply(c: HCursor): Decoder.Result[DNSId] =
      for {
        id <- c.as[NonEmptyString]
      } yield DNSId(id)
  }
  implicit val encodeDNSId: Encoder[DNSId] =
    Encoder.encodeString.contramap[DNSId](_.toString)

  implicit val decodeZoneId: Decoder[ZoneId] = new Decoder[ZoneId] {
    override def apply(c: HCursor): Result[ZoneId] =
      for {
        id <- c.as[NonEmptyString]
      } yield ZoneId(id)
  }
  implicit val encodeZoneId: Encoder[ZoneId] =
    Encoder.encodeString.contramap[ZoneId](_.toString)
}
