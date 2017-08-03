package dockerDNSUpdater.utility.Logging

import cats.free.Free.inject
import cats.free.{Free, Inject}

class Logging[F[_]](implicit I: Inject[LoggingOp, F]) {

  import LoggingOp._

  type LoggingF[T] = Free[F, T]

  def debug(msg: String): LoggingF[Unit] =
    inject(Debug(msg))

  def debugWithCause(msg: String, cause: Throwable): LoggingF[Unit] =
    inject(DebugWithCause(msg, cause))

  def error(msg: String): LoggingF[Unit] =
    inject(Error(msg))

  def errorWithCause(msg: String, cause: Throwable): LoggingF[Unit] =
    inject(ErrorWithCause(msg, cause))

  def info(msg: String): LoggingF[Unit] =
    inject(Info(msg))

  def infoWithCause(msg: String, cause: Throwable): LoggingF[Unit] =
    inject(InfoWithCause(msg, cause))

  def warn(msg: String): LoggingF[Unit] =
    inject(Warn(msg))

  def warnWithCause(msg: String, cause: Throwable): LoggingF[Unit] =
    inject(WarnWithCause(msg, cause))
}

object Logging {
  implicit def imp[F[_]](implicit I: Inject[LoggingOp, F]): Logging[F] =
    new Logging[F]
}
