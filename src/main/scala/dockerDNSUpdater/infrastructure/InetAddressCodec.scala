package dockerDNSUpdater.infrastructure

import java.net.InetAddress
import cats.syntax.either._
import io.circe.{Decoder, Encoder}

/**
  * Created by Lance on 11/06/2017.
  */
object InetAddressCodec {
  private[infrastructure] implicit val encodeInetAddress: Encoder[InetAddress] =
    Encoder.encodeString.contramap[InetAddress](_.getHostAddress)

  private[infrastructure] implicit val decodeInetAddress: Decoder[InetAddress] =
    Decoder.decodeString.emap { str =>
      Either
        .catchNonFatal(InetAddress.getByName(str))
        .leftMap(_ => "InetAddress")
    }
}
