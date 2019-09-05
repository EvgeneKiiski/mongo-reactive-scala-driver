package mongo.reactive.scala


import cats.effect.IO
import com.mongodb.reactivestreams.client.{ MongoClients, MongoCollection }
import de.flapdoodle.embed.mongo.config.{ IMongodConfig, MongodConfigBuilder, Net }
import de.flapdoodle.embed.mongo.distribution.Version
import de.flapdoodle.embed.mongo.{ MongodExecutable, MongodStarter }
import de.flapdoodle.embed.process.runtime.Network
import org.bson.Document
import org.scalatest.{ BeforeAndAfter, FlatSpec, Matchers }

/**
  * @author Eugene Kiyski
  */
class TestMongo extends FlatSpec with Matchers with BeforeAndAfter {

  val starter: MongodStarter = MongodStarter.getDefaultInstance

  val bindIp = "localhost"
  val port = 12345
  val mongodConfig: IMongodConfig = new MongodConfigBuilder()
    .version(Version.Main.PRODUCTION)
    .net(new Net(bindIp, port, Network.localhostIsIPv6))
    .build
  val mongodExecutable: MongodExecutable = starter.prepare(mongodConfig)

  before {
    println("BEFORE")
    mongodExecutable.start
  }

  "Test" should "work" in {
    println("START")
    val mongoClient = MongoClients.create(s"mongodb://$bindIp:$port")
    val database = mongoClient.getDatabase("mydb")
    val collection: MongoCollection[Document] = database.getCollection("test")
    println("START TEST")

    collection.insertOne(new Document("name", "MongoDB")).toAsync[IO].unsafeRunSync()
    collection.insertOne(new Document("name", "Postgres")).toAsync[IO].unsafeRunSync()
    collection.insertOne(new Document("name", "Oracle")).toAsync[IO].unsafeRunSync()
    collection.find().subscribe(PrintSubscriber())
    println(collection.find().toAsyncSeq[IO].unsafeRunSync())
    println("END")
  }

  after {
    println("AFTER")
    mongodExecutable.stop()
  }
}
