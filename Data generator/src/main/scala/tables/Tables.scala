package tables

import org.joda.time.LocalDateTime
import com.github.tototoshi.slick.PostgresJodaSupport._

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.driver.PostgresDriver
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.driver.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema = Array(Events.schema, Participants.schema, Places.schema, Ratings.schema, Tickets.schema, Users.schema).reduceLeft(_ ++ _)
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Events
   *  @param eventId Database column event_id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(127,true)
   *  @param description Database column description SqlType(varchar), Length(511,true), Default(None)
   *  @param createdAt Database column created_at SqlType(timestamp)
   *  @param startsAt Database column starts_at SqlType(timestamp)
   *  @param endsAt Database column ends_at SqlType(timestamp)
   *  @param userId Database column user_id SqlType(int8)
   *  @param placeId Database column place_id SqlType(int8) */
  case class EventsRow(eventId: Long, name: String, description: Option[String] = None, createdAt: LocalDateTime, startsAt: LocalDateTime, endsAt: LocalDateTime, userId: Long, placeId: Long)

  /** Table description of table events. Objects of this class serve as prototypes for rows in queries. */
  class Events(_tableTag: Tag) extends Table[EventsRow](_tableTag, "events") {
    def * = (eventId, name, description, createdAt, startsAt, endsAt, userId, placeId) <> (EventsRow.tupled, EventsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(eventId), Rep.Some(name), description, Rep.Some(createdAt), Rep.Some(startsAt), Rep.Some(endsAt), Rep.Some(userId), Rep.Some(placeId)).shaped.<>({r=>import r._; _1.map(_=> EventsRow.tupled((_1.get, _2.get, _3, _4.get, _5.get, _6.get, _7.get, _8.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column event_id SqlType(bigserial), AutoInc, PrimaryKey */
    val eventId: Rep[Long] = column[Long]("event_id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(127,true) */
    val name: Rep[String] = column[String]("name", O.Length(127,varying=true))
    /** Database column description SqlType(varchar), Length(511,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(511,varying=true), O.Default(None))
    /** Database column created_at SqlType(timestamp) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column starts_at SqlType(timestamp) */
    val startsAt: Rep[LocalDateTime] = column[LocalDateTime]("starts_at")
    /** Database column ends_at SqlType(timestamp) */
    val endsAt: Rep[LocalDateTime] = column[LocalDateTime]("ends_at")
    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column place_id SqlType(int8) */
    val placeId: Rep[Long] = column[Long]("place_id")

    /** Foreign key referencing Places (database name events_place_id_fkey) */
    lazy val placesFk = foreignKey("events_place_id_fkey", placeId, Places)(r => r.placeId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
    /** Foreign key referencing Users (database name events_user_id_fkey) */
    lazy val usersFk = foreignKey("events_user_id_fkey", userId, Users)(r => r.userId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.NoAction)
  }
  /** Collection-like TableQuery object for table Events */
  lazy val Events = new TableQuery(tag => new Events(tag))

  /** Entity class storing rows of table Participants
   *  @param userId Database column user_id SqlType(int8)
   *  @param eventId Database column event_id SqlType(int8) */
  case class ParticipantsRow(userId: Long, eventId: Long)
  /** GetResult implicit for fetching ParticipantsRow objects using plain SQL queries */
  implicit def GetResultParticipantsRow(implicit e0: GR[Long]): GR[ParticipantsRow] = GR{
    prs => import prs._
    ParticipantsRow.tupled((<<[Long], <<[Long]))
  }
  /** Table description of table participants. Objects of this class serve as prototypes for rows in queries. */
  class Participants(_tableTag: Tag) extends Table[ParticipantsRow](_tableTag, "participants") {
    def * = (userId, eventId) <> (ParticipantsRow.tupled, ParticipantsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(eventId)).shaped.<>({r=>import r._; _1.map(_=> ParticipantsRow.tupled((_1.get, _2.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column event_id SqlType(int8) */
    val eventId: Rep[Long] = column[Long]("event_id")

    /** Primary key of Participants (database name participants_pkey) */
    val pk = primaryKey("participants_pkey", (userId, eventId))

    /** Foreign key referencing Events (database name participants_event_id_fkey) */
    lazy val eventsFk = foreignKey("participants_event_id_fkey", eventId, Events)(r => r.eventId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name participants_user_id_fkey) */
    lazy val usersFk = foreignKey("participants_user_id_fkey", userId, Users)(r => r.userId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Participants */
  lazy val Participants = new TableQuery(tag => new Participants(tag))

  /** Entity class storing rows of table Places
   *  @param placeId Database column place_id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param name Database column name SqlType(varchar), Length(127,true)
   *  @param latitude Database column latitude SqlType(float4)
   *  @param longitude Database column longitude SqlType(float4)
   *  @param description Database column description SqlType(varchar), Length(511,true), Default(None) */
  case class PlacesRow(placeId: Long, name: String, latitude: Float, longitude: Float, description: Option[String] = None)
  /** GetResult implicit for fetching PlacesRow objects using plain SQL queries */
  implicit def GetResultPlacesRow(implicit e0: GR[Long], e1: GR[String], e2: GR[Float], e3: GR[Option[String]]): GR[PlacesRow] = GR{
    prs => import prs._
    PlacesRow.tupled((<<[Long], <<[String], <<[Float], <<[Float], <<?[String]))
  }
  /** Table description of table places. Objects of this class serve as prototypes for rows in queries. */
  class Places(_tableTag: Tag) extends Table[PlacesRow](_tableTag, "places") {
    def * = (placeId, name, latitude, longitude, description) <> (PlacesRow.tupled, PlacesRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(placeId), Rep.Some(name), Rep.Some(latitude), Rep.Some(longitude), description).shaped.<>({r=>import r._; _1.map(_=> PlacesRow.tupled((_1.get, _2.get, _3.get, _4.get, _5)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column place_id SqlType(bigserial), AutoInc, PrimaryKey */
    val placeId: Rep[Long] = column[Long]("place_id", O.AutoInc, O.PrimaryKey)
    /** Database column name SqlType(varchar), Length(127,true) */
    val name: Rep[String] = column[String]("name", O.Length(127,varying=true))
    /** Database column latitude SqlType(float4) */
    val latitude: Rep[Float] = column[Float]("latitude")
    /** Database column longitude SqlType(float4) */
    val longitude: Rep[Float] = column[Float]("longitude")
    /** Database column description SqlType(varchar), Length(511,true), Default(None) */
    val description: Rep[Option[String]] = column[Option[String]]("description", O.Length(511,varying=true), O.Default(None))
  }
  /** Collection-like TableQuery object for table Places */
  lazy val Places = new TableQuery(tag => new Places(tag))

  /** Entity class storing rows of table Ratings
   *  @param userId Database column user_id SqlType(int8)
   *  @param eventId Database column event_id SqlType(int8)
   *  @param value Database column value SqlType(int2) */
  case class RatingsRow(userId: Long, eventId: Long, value: Short)
  /** GetResult implicit for fetching RatingsRow objects using plain SQL queries */
  implicit def GetResultRatingsRow(implicit e0: GR[Long], e1: GR[Short]): GR[RatingsRow] = GR{
    prs => import prs._
    RatingsRow.tupled((<<[Long], <<[Long], <<[Short]))
  }
  /** Table description of table ratings. Objects of this class serve as prototypes for rows in queries. */
  class Ratings(_tableTag: Tag) extends Table[RatingsRow](_tableTag, "ratings") {
    def * = (userId, eventId, value) <> (RatingsRow.tupled, RatingsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(eventId), Rep.Some(value)).shaped.<>({r=>import r._; _1.map(_=> RatingsRow.tupled((_1.get, _2.get, _3.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(int8) */
    val userId: Rep[Long] = column[Long]("user_id")
    /** Database column event_id SqlType(int8) */
    val eventId: Rep[Long] = column[Long]("event_id")
    /** Database column value SqlType(int2) */
    val value: Rep[Short] = column[Short]("value")

    /** Primary key of Ratings (database name ratings_pkey) */
    val pk = primaryKey("ratings_pkey", (userId, eventId))

    /** Foreign key referencing Events (database name ratings_event_id_fkey) */
    lazy val eventsFk = foreignKey("ratings_event_id_fkey", eventId, Events)(r => r.eventId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
    /** Foreign key referencing Users (database name ratings_user_id_fkey) */
    lazy val usersFk = foreignKey("ratings_user_id_fkey", userId, Users)(r => r.userId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Ratings */
  lazy val Ratings = new TableQuery(tag => new Ratings(tag))

  /** Entity class storing rows of table Tickets
   *  @param ticketId Database column ticket_id SqlType(bigserial), AutoInc, PrimaryKey
   *  @param eventId Database column event_id SqlType(int8)
   *  @param soldAmount Database column sold_amount SqlType(int4), Default(0)
   *  @param maxAmount Database column max_amount SqlType(int4), Default(None)
   *  @param price Database column price SqlType(float4) */
  case class TicketsRow(ticketId: Long, eventId: Long, soldAmount: Int = 0, maxAmount: Option[Int] = None, price: Float)
  /** GetResult implicit for fetching TicketsRow objects using plain SQL queries */
  implicit def GetResultTicketsRow(implicit e0: GR[Long], e1: GR[Int], e2: GR[Option[Int]], e3: GR[Float]): GR[TicketsRow] = GR{
    prs => import prs._
    TicketsRow.tupled((<<[Long], <<[Long], <<[Int], <<?[Int], <<[Float]))
  }
  /** Table description of table tickets. Objects of this class serve as prototypes for rows in queries. */
  class Tickets(_tableTag: Tag) extends Table[TicketsRow](_tableTag, "tickets") {
    def * = (ticketId, eventId, soldAmount, maxAmount, price) <> (TicketsRow.tupled, TicketsRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(ticketId), Rep.Some(eventId), Rep.Some(soldAmount), maxAmount, Rep.Some(price)).shaped.<>({r=>import r._; _1.map(_=> TicketsRow.tupled((_1.get, _2.get, _3.get, _4, _5.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column ticket_id SqlType(bigserial), AutoInc, PrimaryKey */
    val ticketId: Rep[Long] = column[Long]("ticket_id", O.AutoInc, O.PrimaryKey)
    /** Database column event_id SqlType(int8) */
    val eventId: Rep[Long] = column[Long]("event_id")
    /** Database column sold_amount SqlType(int4), Default(0) */
    val soldAmount: Rep[Int] = column[Int]("sold_amount", O.Default(0))
    /** Database column max_amount SqlType(int4), Default(None) */
    val maxAmount: Rep[Option[Int]] = column[Option[Int]]("max_amount", O.Default(None))
    /** Database column price SqlType(float4) */
    val price: Rep[Float] = column[Float]("price")

    /** Foreign key referencing Events (database name tickets_event_id_fkey) */
    lazy val eventsFk = foreignKey("tickets_event_id_fkey", eventId, Events)(r => r.eventId, onUpdate=ForeignKeyAction.NoAction, onDelete=ForeignKeyAction.Cascade)
  }
  /** Collection-like TableQuery object for table Tickets */
  lazy val Tickets = new TableQuery(tag => new Tickets(tag))

  case class UsersRow(userId: Long, email: String, password: String, latitude: Option[Float] = None, longitude: Option[Float] = None, createdAt: LocalDateTime, lastActivity: LocalDateTime)

  /** Table description of table users. Objects of this class serve as prototypes for rows in queries. */
  class Users(_tableTag: Tag) extends Table[UsersRow](_tableTag, "users") {
    def * = (userId, email, password, latitude, longitude, createdAt, lastActivity) <> (UsersRow.tupled, UsersRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(userId), Rep.Some(email), Rep.Some(password), latitude, longitude, Rep.Some(createdAt), Rep.Some(lastActivity)).shaped.<>({r=>import r._; _1.map(_=> UsersRow.tupled((_1.get, _2.get, _3.get, _4, _5, _6.get, _7.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column user_id SqlType(bigserial), AutoInc, PrimaryKey */
    val userId: Rep[Long] = column[Long]("user_id", O.AutoInc, O.PrimaryKey)
    /** Database column email SqlType(varchar), Length(255,true) */
    val email: Rep[String] = column[String]("email", O.Length(255,varying=true))
    /** Database column password SqlType(varchar), Length(255,true) */
    val password: Rep[String] = column[String]("password", O.Length(255,varying=true))
    /** Database column latitude SqlType(float4), Default(None) */
    val latitude: Rep[Option[Float]] = column[Option[Float]]("latitude", O.Default(None))
    /** Database column longitude SqlType(float4), Default(None) */
    val longitude: Rep[Option[Float]] = column[Option[Float]]("longitude", O.Default(None))
    /** Database column created_at SqlType(timestamp) */
    val createdAt: Rep[LocalDateTime] = column[LocalDateTime]("created_at")
    /** Database column last_activity SqlType(timestamp) */
    val lastActivity: Rep[LocalDateTime] = column[LocalDateTime]("last_activity")

    /** Uniqueness Index over (email) (database name users_email_key) */
    val index1 = index("users_email_key", email, unique=true)
  }
  /** Collection-like TableQuery object for table Users */
  lazy val Users = new TableQuery(tag => new Users(tag))
}
