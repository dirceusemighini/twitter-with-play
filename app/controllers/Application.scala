package controllers

import play.api.mvc._

import play.api.libs.iteratee._
import play.api.libs.concurrent._

import scala.concurrent.duration._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.EventSource
import twitter4j._
import twitter4j.{Status => TwStatus}
import scala.Some
import play.api.libs.ws.WS
import play.api.libs.oauth.{RequestToken, ConsumerKey, OAuthCalculator}
import play.libs.Json

object Application extends Controller {

  /**
   * A String Enumerator producing a formatted Time message every 100 millis.
   * A callback enumerator is pure an can be applied on several Iteratee.
   */
  lazy val clock: Enumerator[String] = {

    //    val twitter = TwitterFactory.getSingleton();

    val twitterStream = new TwitterStreamFactory().getInstance()

    import java.util._
    import java.text._

    //Enumerator.fromStream(data) para o twitter talvez

    val dateFormat = new SimpleDateFormat("HH mm ss")


    Enumerator.generateM {
      Promise.timeout(Some(dateFormat.format(new Date)), 100 milliseconds)
    }
  }

  def stream(keywords: String) = WebSocket.using[String] {
    request =>

      val out: Enumerator[String] = Concurrent.unicast[String](onStart = pushee => {
        val consumerKey = ConsumerKey("", "")
        val accessToken = RequestToken("", "")

        def twitterIteratee = Iteratee.foreach[Array[Byte]] {
          ba =>
            val msg = new String(ba, "UTF-8")
            pushee.push(msg)
            println(msg)
        }

        WS.url("https://stream.twitter.com/1.1/statuses/filter.json?track=" + keywords)
          .sign(OAuthCalculator(consumerKey, accessToken))
          .get(headers => twitterIteratee)
      })
      val in = Iteratee.ignore[String]
      (in, out)
  }


  def index = Action {
    Ok(views.html.index())
  }

  def liveClock = Action {
    Ok.chunked(clock &> EventSource()).as("text/event-stream")
  }




  def tweets(query:String): Enumerator[String] = {
    Concurrent.unicast[String](onStart = pushee => {
      val consumerKey = ConsumerKey("aiuO34OBqnrNhpAYzRlQ", "lUkhaS3We3HAH8pGtldC7fNEHnfZU6eWiWgTWKZ8ObQ")
      val accessToken = RequestToken("86628543-35lewf8PWmE2QTpHvYhpjBITTbMUZZfQSDRGzfgss", "evnf4cDO69YnMfqEqvT7bKld9RjWJUBPCevh9bXx6O3L0")

      def twitterIteratee = Iteratee.foreach[Array[Byte]] {
        ba =>
          val msg = new String(ba, "UTF-8")
          val json = Json.parse(msg)
          pushee.push(msg.replaceAll("'",""))
          println(msg)
      }

      WS.url("https://stream.twitter.com/1.1/statuses/filter.json?track=" + query)
        .sign(OAuthCalculator(consumerKey, accessToken))
        .get(headers => twitterIteratee)
    })
  }

  def liveTweet = Action {
    val keywords = ""


    Ok.chunked(tweets("iphone") &> EventSource()).as("text/event-stream")
  }

}