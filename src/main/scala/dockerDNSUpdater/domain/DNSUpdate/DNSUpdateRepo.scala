package dockerDNSUpdater.domain.DNSUpdate

import dockerDNSUpdater.domain.CloudFlareDNS.{DNSOp, DNSRepo}
import dockerDNSUpdater.domain.CloudFlareDNS.aggregate.DNSRecord
import dockerDNSUpdater.domain.CloudFlareDNS.command._
import dockerDNSUpdater.domain.CloudFlareDNS.common._
import dockerDNSUpdater.domain.PublicIp.{PublicIpOp, PublicIpRepo}
import dockerDNSUpdater.domain.PublicIp.aggregate.IPv4Address
import dockerDNSUpdater.infrastructure.CloudFlareDNS.interpreter.Http4s.Http4sDNSRepoInterpreter
import dockerDNSUpdater.infrastructure.Http4sPublicIpInterpreter.Http4sPublicIpRepoInterpreter
import dockerDNSUpdater.utility.Logging._
import dockerDNSUpdater.utility.Logging.interpreter.LoggingPrintAsyncI
import cats.data.Coproduct
import cats.free.Free
import cats.~>
import fs2.Task
import fs2.interop.cats._

final class DNSUpdateRepo(private val loggingI: LoggingPrintAsyncI,
                          private val publicIpInter: Http4sPublicIpRepoInterpreter,
                          private val dNSInter: Http4sDNSRepoInterpreter) {

  import DNSUpdateRepo._

  type RepoOps[A]     = Coproduct[PublicIpOp, DNSOp, A]
  type Application[A] = Coproduct[LoggingOp, RepoOps, A]

  private lazy val interpreter: Application ~> Task = {
    loggingI or (publicIpInter or dNSInter)
  }

  private def program(credential: Credential, zone: ZoneId, record: Record)(
      implicit L: Logging[Application],
      D: DNSRepo[Application],
      P: PublicIpRepo[Application]): Free[Application, DNSRecord] = {

    import L._, D._, P._

    for {
      _     <- info("Acquire public IPv4 address.")
      ip    <- getIp
      _     <- info(s"Acquire ID of ${record.name}")
      dnsId <- getRecordId(credential, zone, record.name)
      _     <- info(s"Update (DNS record: ${record.name}, ID: ${dnsId}) with ${ip}.")
      n     <- update(credential, zone, dnsId, convertRecordWithContent(record, ip))
      _     <- info("DNS record is updated!")
    } yield n
  }

  def updateDNS(credential: Credential, zone: ZoneId, record: Record): Task[DNSRecord] = {

    program(credential, zone, record).foldMap(interpreter)
  }
}

object DNSUpdateRepo {

  import dockerDNSUpdater.utility.mapping.Field
  import shapeless._
  import syntax.singleton._
  import ops.hlist._

  private lazy val genRecordWithContent = LabelledGeneric[RecordWithContent]
  private lazy val genRecord            = LabelledGeneric[Record]

  private def convertRecordWithContent(record: Record, content: IPv4Address): RecordWithContent = {

    // TODO: Could optimize further by extracting Align and Field. First, figure out how to declare field type

    val contentField = Field('content ->> content)
    val align =
      Align[contentField.F :: genRecord.Repr, genRecordWithContent.Repr]
    val hlist = contentField.value :: genRecord.to(record)

    genRecordWithContent.from(align(hlist))
  }
}
