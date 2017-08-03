package dockerDNSUpdater.application

import dockerDNSUpdater.domain.util.RefinedTypes.NonEmptyString

final case class CloudFlareCredential(email: NonEmptyString, key: NonEmptyString)
