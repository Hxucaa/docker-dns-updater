package dockerDNSUpdater.infrastructure.CloudFlareDNS.interpreter.Http4s

import dockerDNSUpdater.domain.CloudFlareDNS.aggregate._
import dockerDNSUpdater.domain.CloudFlareDNS.command._
import dockerDNSUpdater.domain.CloudFlareDNS.common._
import dockerDNSUpdater.domain.util.RefinedTypes.NonEmptyString
import CFResponse.{CFList, CFUpdate, CFUpdateRequest}
import dockerDNSUpdater.infrastructure.Http4sClient
import eu.timepit.refined.auto._
import fs2.{Strategy, Task}
import org.http4s.MediaType.`application/json`
import org.http4s._
import org.http4s.headers.`Content-Type`
import scala.concurrent.ExecutionContext

/**
  * Created by Lance on 12/06/2017.
  */
private[Http4s] object Param {

  case class DomainName(name: String)

  implicit val queryParam = new QueryParamEncoder[DomainName] with QueryParam[DomainName] {
    def key: QueryParameterKey = QueryParameterKey("name")

    def encode(value: DomainName): QueryParameterValue =
      QueryParameterValue(value.name)
  }
}

class DNSRequester(private val client: Http4sClient)(private implicit val ec: ExecutionContext) {

  import DNSRequester._

  private implicit val strategy = Strategy.fromExecutionContext(ec)

  def getRecordId(credential: Credential, zoneId: ZoneId, name: NonEmptyString): Task[DNSId] = {
    import Param._

    val target = baseDNSUri(zoneId.id)
      .withOptionQueryParam[DomainName](Some(DomainName(name)))
    val req = Request(Method.GET, target, headers = headers(credential.email, credential.key))

    client.client
      .expect[CFList](req)
      .flatMap { v =>
        v.result match {
          case Seq(head) =>
            Task.now {
              head.id
            }
          case _ => Task.fail(new IllegalArgumentException)
        }
      }
  }

  def update(credential: Credential,
             zoneId: ZoneId,
             dnsId: DNSId,
             record: RecordWithContent): Task[DNSRecord] = {

    val payload: CFUpdateRequest =
      CFUpdateRequest(record.name, record.`type`, record.content.ip, record.ttl, record.proxied)
    val target = baseDNSUri(zoneId.id) / dnsId.id
    val req = Request(Method.PUT, target, headers = headers(credential.email, credential.key))
      .withBody[CFUpdateRequest](payload)

    client.client
      .expect[CFUpdate](req)
      .flatMap { v =>
        v.result match {
          case None => Task.fail(new IllegalArgumentException)
          case Some(r) =>
            Task.now(r)
        }
      }
  }
}

object DNSRequester {

  import shapeless.Generic

  private val genResult    = Generic[CFResponse.Result]
  private val genDNSRecord = Generic[DNSRecord]

  @SuppressWarnings(Array("org.wartremover.warts.Throw"))
  private val baseUri                     = Uri.uri("https://api.cloudflare.com/client/v4/zones")
  private val baseDNSUri: Uri.Path => Uri = baseUri / _ / "dns_records"

  private def headers(email: String, key: String): Headers = {
    val contentType = `Content-Type`(`application/json`)
    Headers(contentType, Header("X-Auth-Email", email), Header("X-Auth-Key", key))
  }

  private implicit def CFUpdateToDNSRecord(r: CFResponse.Result): DNSRecord =
    genDNSRecord.from(genResult.to(r))
}
