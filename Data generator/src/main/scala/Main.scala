import java.util.concurrent.TimeUnit


import faker.{Lorem, Geo}
import org.joda.time.LocalDateTime
import slick.driver.PostgresDriver.api._
import slick.jdbc.meta.MTable
import slick.codegen.SourceCodeGenerator

import scala.collection.mutable.ListBuffer
import scala.collection.mutable
import scala.concurrent.ExecutionContext.Implicits._

import scala.concurrent.duration.Duration
import scala.concurrent.duration._
import scala.concurrent.{Awaitable, Await, Future}
import scala.util.Random

object Main extends App {
  DataGenerator.generate()
}

object DataGenerator {
  import DB._
  import tables.Tables.profile.api._
  import tables.Tables._

  private val UsersNo = 100000
  private val PlacesNo = UsersNo/4
  private val EventsNo = UsersNo/2
  private val ParticipatingIn = 4
  private val ParticipantsNo = UsersNo * ParticipatingIn
  private val RatingsPerUser = 3
  private val RatingsNo = UsersNo * RatingsPerUser
  private val NumberOfTicketsFrom = 20
  private val NumberOfTicketsTo = 100
  val Password = "test123"
  val Words = Set("alias", "consequatur", "aut", "perferendis", "sit", "voluptatem", "accusantium", "doloremque",
    "aperiam", "eaque", "ipsa", "quae", "ab", "illo", "inventore", "veritatis", "et", "quasi", "architecto", "beatae",
    "vitae", "dicta", "sunt", "explicabo", "aspernatur", "aut", "odit", "aut", "fugit", "sed", "quia", "consequuntur",
    "magni", "dolores", "eos", "qui", "ratione", "voluptatem", "sequi", "nesciunt", "neque", "dolorem", "ipsum", "quia",
    "dolor", "sit", "amet", "consectetur", "adipisci", "velit", "sed", "quia", "non", "numquam", "eius", "modi",
    "tempora", "incidunt", "ut", "labore", "et", "dolore", "magnam", "aliquam", "quaerat", "voluptatem", "ut", "enim",
    "ad", "minima", "veniam", "quis", "nostrum", "exercitationem", "ullam", "corporis", "nemo", "enim", "ipsam",
    "voluptatem", "quia", "voluptas", "sit", "suscipit", "laboriosam", "nisi", "ut", "aliquid", "ex", "ea", "commodi",
    "consequatur", "quis", "autem", "vel", "eum", "iure", "reprehenderit", "qui", "in", "ea", "voluptate", "velit",
    "esse", "quam", "nihil", "molestiae", "et", "iusto", "odio", "dignissimos", "ducimus", "qui", "blanditiis",
    "praesentium", "laudantium", "totam", "rem", "voluptatum", "deleniti", "atque", "corrupti", "quos", "dolores",
    "et", "quas", "molestias", "excepturi", "sint", "occaecati", "cupiditate", "non", "provident", "sed", "ut",
    "perspiciatis", "unde", "omnis", "iste", "natus", "error", "similique", "sunt", "in", "culpa", "qui", "officia",
    "deserunt", "mollitia", "animi", "id", "est", "laborum", "et", "dolorum", "fuga", "et", "harum", "quidem", "rerum",
    "facilis", "est", "et", "expedita", "distinctio", "nam", "libero", "tempore", "cum", "soluta", "nobis", "est",
    "eligendi", "optio", "cumque", "nihil", "impedit", "quo", "porro", "quisquam", "est", "qui", "minus", "id", "quod",
    "maxime", "placeat", "facere", "possimus", "omnis", "voluptas", "assumenda", "est", "omnis", "dolor", "repellendus",
    "temporibus", "autem", "quibusdam", "et", "aut", "consequatur", "vel", "illum", "qui", "dolorem", "eum", "fugiat",
    "quo", "voluptas", "nulla", "pariatur", "at", "vero", "eos", "et", "accusamus", "officiis", "debitis", "aut",
    "rerum", "necessitatibus", "saepe", "eveniet", "ut", "et", "voluptates", "repudiandae", "sint", "et", "molestiae",
    "non", "recusandae", "itaque", "earum", "rerum", "hic", "tenetur", "a", "sapiente", "delectus", "ut", "aut",
    "reiciendis", "voluptatibus", "maiores", "doloribus", "asperiores", "repellat")

  def generate(): Unit = {
    val userIds = generateUsers
    val placesIds = generatePlaces
    val eventsIds = generateEvents(userIds, placesIds)
    generateParticipants(userIds, eventsIds)
    generateRatings(userIds, eventsIds)
  }

  private def generateUsers: Array[Long] = {
    val users = ListBuffer[UsersRow]()
    val geo = Geo.coordsInArea(Geo.CoordsRange(49.98, 53.97, 14.18, 22.34))
    for (i <- 1 to UsersNo) {
      val (lat, lon) = geo()
      val email = s"test$i@co.uk"
      users += UsersRow(
        userId = -1,
        email = email,
        password = Password,
        latitude = Some(lat.toFloat),
        longitude = Some(lon.toFloat),
        createdAt = oneTwoMonthsAgo,
        lastActivity = DateHelper.utcNow)

      if (i % 10000 == 0) {
        await(db.run(Users ++= users))
        users.clear()
      }
    }
    await(db.run(Users ++= users))
    println("users inserted successfully")
    await(db.run(Users.map(_.userId).result)).toArray
  }

  private def generatePlaces: Array[Long] = {
    val places = ListBuffer[PlacesRow]()
    val geo = Geo.coordsInArea(Geo.CoordsRange(49.98, 53.97, 14.18, 22.34))
    for (i <- 1 to PlacesNo) {
      val (lat, lon) = geo()
      places += PlacesRow(
        placeId = -1,
        name = Lorem.words(2).mkString(" ").capitalize,
        latitude = lat.toFloat,
        longitude = lon.toFloat,
        description = Some(Lorem.sentence(2)))

      if (i % 10000 == 0) {
        await(db.run(Places ++= places))
        places.clear()
      }
    }
    await(db.run(Places ++= places))
    println("places inserted successfully")
    await(db.run(Places.map(_.placeId).result)).toArray
  }

  private def generateEvents(userIds: Array[Long], placesIds: Array[Long]) = {
    val events = ListBuffer[EventsRow]()
    for (i <- 1 to EventsNo) {
      val randomUserId = randomElem(userIds)
      val randomPlaceId = randomElem(placesIds)
      val created = upToMonthAgo
      val startsAt = fromUpToMonthAhead(created)
      events += EventsRow(
        eventId = -1,
        name = Lorem.words(2).mkString(" ").capitalize,
        description = Some(Lorem.sentence(2)),
        createdAt = created,
        startsAt = startsAt,
        endsAt = upToTwelveHours(startsAt),
        userId = randomUserId,
        placeId = randomPlaceId)

      if (i % 10000 == 0) {
        await(db.run(Events ++= events))
        events.clear()
      }
    }
    await(db.run(Events ++= events))
    println("events inserted successfully")
    await(db.run(Events.map(_.eventId).result)).toArray
  }

  private def generateParticipants(userIds: Array[Long], eventsIds: Array[Long]) = {
    val participants = ListBuffer[ParticipantsRow]()
    var counter = 0
    for (userId <- userIds) {
      val alreadyIn = mutable.Set[Long]()
      var i = 0
      while (i < ParticipatingIn) {
        val randomEventId = randomElem(eventsIds)
        if (!alreadyIn(randomEventId)) {
          participants += ParticipantsRow(
            eventId = randomEventId,
            userId = userId)

          alreadyIn += randomEventId
          counter += 1
          i += 1
          if (counter % 10000 == 0) {
            await(db.run(Participants ++= participants))
            participants.clear()
            counter = 0
          }
        }
      }
    }
    await(db.run(Participants ++= participants))
    println("participants inserted successfully")
  }

  private def generateRatings(userIds: Array[Long], eventsIds: Array[Long]) = {
    val ratings = ListBuffer[RatingsRow]()
    var counter = 0
    for (userId <- userIds) {
      val alreadyIn = mutable.Set[Long]()
      var i = 0
      while (i < RatingsPerUser) {
        val randomEventId = randomElem(eventsIds)
        if (!alreadyIn(randomEventId)) {
          ratings += RatingsRow(
            eventId = randomEventId,
            userId = userId,
            value = (1 + Random.nextInt(5)).toShort // ratings are from 1 to 5
          )
          alreadyIn += randomEventId
          counter += 1
          i += 1
          if (counter % 10000 == 0) {
            await(db.run(Ratings ++= ratings))
            ratings.clear()
            counter = 0
          }
        }
      }
    }
    await(db.run(Ratings ++= ratings))
    println("ratings inserted successfully")
  }

  private def oneTwoMonthsAgo: LocalDateTime = upToMonthAgo minusDays 30

  private def upToMonthAgo: LocalDateTime = DateHelper.utcNow minusDays Random.nextInt(30)

  private def fromUpToMonthAhead(from: LocalDateTime) = from plusDays Random.nextInt(30)

  private def upToTwelveHours(from: LocalDateTime) = from plusHours (1 + Random.nextInt(11))

  private def randomElem[T](array: Array[T]): T = array(Random.nextInt(array.length))

  private def await[T](awaitable: Awaitable[T]): T = Await.result(awaitable, 15 seconds)
}

object DB {
  val slickDriver = "slick.driver.PostgresDriver"
  val jdbcDriver = "org.postgresql.Driver"
  val url = "jdbc:postgresql://localhost:5432/advanced"
  val outputDir = "/Users/paweliwanow/nauka/Advanced Databases/advanced_databases/Data generator/tmp/"
  val pkg = "tables"
  val username = "postgres"
  val password = "postgres"
  lazy val db = Database.forURL(url, username, password)
}


object TablesGenerator {
  import DB._

  def generate(): Unit = {
//    val dbio = PostgresDriver.createModel(Some(MTable.getTables(None, None, None, Some(Seq("TABLE", "VIEW")))))
//    val model = db.run(dbio)
//    val future : Future[SourceCodeGenerator] = model.map(model => new SourceCodeGenerator(model))
//    val codegen : SourceCodeGenerator = Await.result(future, Duration.create(5, TimeUnit.MINUTES))
//    codegen.writeToFile(slickDriver, outputDir, pkg, "Tables", "Tables.scala")
  }
}