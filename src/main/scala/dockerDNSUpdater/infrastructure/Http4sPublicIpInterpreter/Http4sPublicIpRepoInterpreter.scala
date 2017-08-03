package dockerDNSUpdater.infrastructure.Http4sPublicIpInterpreter

import dockerDNSUpdater.domain.PublicIp.PublicIpOp
import dockerDNSUpdater.domain.PublicIp.PublicIpOp._
import cats.~>
import fs2.Task

class Http4sPublicIpRepoInterpreter(private val requester: PublicIpRequester)
    extends (PublicIpOp ~> Task) {

  override def apply[A](fa: PublicIpOp[A]): Task[A] = fa match {
    case GetIP =>
      requester.getIp

  }
}
