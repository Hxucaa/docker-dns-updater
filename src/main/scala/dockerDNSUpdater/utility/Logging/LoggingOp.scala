package dockerDNSUpdater.utility.Logging

sealed trait LoggingOp[A]

object LoggingOp {

  final case class Debug(msg: String) extends LoggingOp[Unit]

  final case class DebugWithCause(msg: String, cause: Throwable) extends LoggingOp[Unit]

  final case class Error(msg: String) extends LoggingOp[Unit]

  final case class ErrorWithCause(msg: String, cause: Throwable) extends LoggingOp[Unit]

  final case class Info(msg: String) extends LoggingOp[Unit]

  final case class InfoWithCause(msg: String, cause: Throwable) extends LoggingOp[Unit]

  final case class Warn(msg: String) extends LoggingOp[Unit]

  final case class WarnWithCause(msg: String, cause: Throwable) extends LoggingOp[Unit]

}
