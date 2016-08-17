import java.time._
import java.time.format.DateTimeFormatter
import java.time.ZoneOffset._



LocalDateTime.parse("2011-12-03T10:15:30Z", DateTimeFormatter.ISO_DATE_TIME).toInstant(UTC)

Instant.parse("2011-12-03T10:15:30Z")