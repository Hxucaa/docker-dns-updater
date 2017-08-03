package dockerDNSUpdater.domain.CloudFlareDNS.aggregate

import java.net.InetAddress
import dockerDNSUpdater.domain.CloudFlareDNS.common._
import dockerDNSUpdater.domain.util.RefinedTypes._
import java.time.OffsetDateTime

/**
  * Created by Lance on 15/05/2017.
  */
final case class DNSRecord(id: DNSId,
                           name: NonEmptyString,
                           `type`: DNSType,
                           content: InetAddress,
                           proxiable: Boolean,
                           proxied: Boolean,
                           ttl: PositiveInt,
                           locked: Boolean,
                           zoneId: ZoneId,
                           zoneName: NonEmptyString,
                           createdOn: OffsetDateTime,
                           modifiedOn: OffsetDateTime)
