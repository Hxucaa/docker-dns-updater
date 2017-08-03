package dockerDNSUpdater.infrastructure.CloudFlareDNS.interpreter.Http4s

import dockerDNSUpdater.domain.CloudFlareDNS.DNSOp
import dockerDNSUpdater.domain.CloudFlareDNS.DNSOp._
import cats.~>
import fs2.Task

/**
  * Created by Lance on 11/05/2017.
  */
/**
  * Construct an interpreter based on Http4s' PooledHttp1Client.
  */
class Http4sDNSRepoInterpreter(private val requester: DNSRequester) extends (DNSOp ~> Task) {

  override def apply[A](fa: DNSOp[A]): Task[A] = fa match {

    case GetRecordId(credential, zoneId, name) =>
      requester.getRecordId(credential, zoneId, name)

    case Update(credential, zoneId, dnsId, record) =>
      requester.update(credential, zoneId, dnsId, record)
    // TODO: Implementation.
    //    case Create(credential, record) => ???
    //
    //    case Get(credential, zoneId, dnsId) => {
    //
    //      ???
    //    }
    case _ => ???
  }

}

object Http4sDNSRepoInterpreter {}
