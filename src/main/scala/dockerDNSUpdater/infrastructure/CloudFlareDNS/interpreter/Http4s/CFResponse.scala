package dockerDNSUpdater.infrastructure.CloudFlareDNS.interpreter.Http4s

import java.net.InetAddress
import java.time.OffsetDateTime

import dockerDNSUpdater.domain.CloudFlareDNS.aggregate._
import dockerDNSUpdater.domain.CloudFlareDNS.common._
import dockerDNSUpdater.domain.util.RefinedTypes._
import dockerDNSUpdater.infrastructure.InetAddressCodec
import eu.timepit.refined.auto._
import DNSRecordCodec._
import InetAddressCodec._
import dockerDNSUpdater.domain.util.RefinedTypes._
import java.time.OffsetDateTime

import io.circe.refined._
import io.circe.java8.time.{decodeOffsetDateTimeDefault, encodeOffsetDateTimeDefault}
import io.circe.generic.extras._
import org.http4s.circe.{jsonEncoderOf, jsonOf}

/**
  * Created by Lance on 11/05/2017.
  */
private[Http4s] object CFResponse {

  private implicit val config: Configuration =
    Configuration.default.withSnakeCaseKeys

  implicit val resultDecoder           = jsonOf[Result]
  implicit val placeholderErrorDecoder = jsonOf[ResponseError]
  implicit val resultInfoDecoder       = jsonOf[ResultInfo]
  implicit val cfListDecoder           = jsonOf[CFList]
  implicit val cfGetDecoder            = jsonOf[CFGet]
  implicit val cfUpdateDecoder         = jsonOf[CFUpdate]

  implicit val cFUpdateRequestEncoder = jsonEncoderOf[CFUpdateRequest]

  @ConfiguredJsonCodec
  final case class Result(
      id: DNSId,
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
      modifiedOn: OffsetDateTime
      //                    data: Any
  )

  @ConfiguredJsonCodec
  final case class ResponseError(code: Int, description: String)

  sealed trait CFResponse {
    def success: Boolean

    def errors: Seq[ResponseError]

    def messages: Seq[String]

    //  def result: A
    def resultInfo: Option[ResultInfo]
  }

  /**
    * Pagination.
    *
    * @param page
    * @param perPage
    * @param totalPages
    * @param count
    * @param totalCount
    */
  @ConfiguredJsonCodec
  final case class ResultInfo(page: Int, perPage: Int, totalPages: Int, count: Int, totalCount: Int)

  @ConfiguredJsonCodec
  final case class CFList(success: Boolean,
                          errors: Seq[ResponseError],
                          messages: Seq[String],
                          result: Seq[Result],
                          resultInfo: Option[ResultInfo])
      extends CFResponse

  @ConfiguredJsonCodec
  final case class CFGet(success: Boolean,
                         errors: Seq[ResponseError],
                         messages: Seq[String],
                         result: Option[Result],
                         resultInfo: Option[ResultInfo])
      extends CFResponse

  @ConfiguredJsonCodec
  final case class CFUpdate(success: Boolean,
                            errors: Seq[ResponseError],
                            messages: Seq[String],
                            result: Option[Result],
                            resultInfo: Option[ResultInfo])
      extends CFResponse

  sealed trait CFRequest {}

  @ConfiguredJsonCodec
  final case class CFUpdateRequest(name: NonEmptyString,
                                   `type`: DNSType,
                                   content: InetAddress,
                                   ttl: Option[PositiveInt] = None,
                                   proxied: Option[Boolean] = None)
      extends CFRequest

}
