package dockerDNSUpdater.domain.PublicIp.aggregate

import java.net.InetAddress

/**
  * Created by Lance on 11/05/2017.
  */
final case class IPv4Address(ip: InetAddress) {
  override def toString: String = s"IPv4Address(${ip.getHostAddress})"
}
