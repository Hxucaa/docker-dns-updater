package dockerDNSUpdater.domain.CloudFlareDNS.command

import dockerDNSUpdater.domain.CloudFlareDNS.common._
import dockerDNSUpdater.domain.PublicIp.aggregate.IPv4Address
import dockerDNSUpdater.domain.util.RefinedTypes._

/**
  * Created by Lance on 09/06/2017.
  */
final case class Record(`type`: DNSType,
                        name: NonEmptyString,
                        ttl: Option[PositiveInt] = None,
                        proxied: Option[Boolean] = None)

final case class RecordWithContent(`type`: DNSType,
                                   name: NonEmptyString,
                                   content: IPv4Address,
                                   ttl: Option[PositiveInt] = None,
                                   proxied: Option[Boolean] = None)
