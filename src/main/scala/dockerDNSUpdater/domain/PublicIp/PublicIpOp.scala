package dockerDNSUpdater.domain.PublicIp

import dockerDNSUpdater.domain.PublicIp.aggregate.IPv4Address

/**
  * Created by Lance on 18/05/2017.
  */
sealed trait PublicIpOp[T]

object PublicIpOp {

  case object GetIP extends PublicIpOp[IPv4Address]

}
