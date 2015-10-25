
import org.joda.time.DateTimeZone
import org.joda.time.LocalDateTime
import org.joda.time.Seconds
import org.joda.time.format.DateTimeFormat

import scala.util.Try

object DateHelper {
  val pattern = "yyyy-MM-dd HH:mm:ss"

  def utcNow = new LocalDateTime(DateTimeZone.UTC)
  def convertToDateTime(date: String): Try[LocalDateTime] = Try(parseDateTime(date))
  def formatDateTime(date: LocalDateTime) = date.toString(pattern)
  def formatDateTimeOption(date: Option[LocalDateTime]) = date.map(_.toString(pattern))
  def parseDateTime(date: String) = LocalDateTime.parse(date, DateTimeFormat.forPattern(pattern))
  def timeLeftInSeconds(date: LocalDateTime): Int = Seconds.secondsBetween(utcNow, date).getSeconds

  def descendingOrdering: Ordering[LocalDateTime] = Ordering.fromLessThan(_ isAfter _)

  def ascendingOrdering: Ordering[LocalDateTime] = Ordering.fromLessThan(_ isBefore _)
}
