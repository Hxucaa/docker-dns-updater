package dockerDNSUpdater.domain.PublicIp

import dockerDNSUpdater.domain.PublicIp.aggregate.IPv4Address
import dockerDNSUpdater.domain.PublicIp.PublicIpOp._
import cats.free.Free.inject
import cats.free.{Free, Inject}

/**
  * Created by Lance on 18/05/2017.
  */
class PublicIpRepo[F[_]](implicit I: Inject[PublicIpOp, F]) {
  def getIp: Free[F, IPv4Address] = inject[PublicIpOp, F](GetIP)
}

object PublicIpRepo {
  implicit def imp[F[_]](implicit I: Inject[PublicIpOp, F]): PublicIpRepo[F] =
    new PublicIpRepo[F]
}

//object PublicIPRepo {
//
//  import dockerDNSUpdater.interpreter.PublicIPOp._
//
//  import cats.free.Free
//  import cats.free.Free.liftF
//
//  type PublicIPF[T] = Free[PublicIPOp, T]
//
//  def getPublicIP: PublicIPF[IPv4Address] = liftF(GetIP)
//}
