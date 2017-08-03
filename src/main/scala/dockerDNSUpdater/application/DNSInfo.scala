package dockerDNSUpdater.application

import dockerDNSUpdater.domain.util.RefinedTypes.{NonEmptyString, PositiveInt}

final case class DNSInfo(name: NonEmptyString,
                         ttl: Option[PositiveInt] = None,
                         proxied: Option[Boolean] = None)
