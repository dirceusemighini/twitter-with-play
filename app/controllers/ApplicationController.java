package controllers;


import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.http.impl.util.JavaMapping;
import akka.japi.Option;
import akka.japi.Pair;

import akka.stream.ActorMaterializer;
import akka.util.ByteString;
import listener.TwitterStreamListener;
import play.Logger;
import play.api.libs.EventSource;
import play.mvc.Controller;
import play.mvc.Result;
import org.reactivestreams.Publisher;
import play.mvc.StatusHeader;
import scala.None;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import akka.stream.Materializer;
import akka.stream.OverflowStrategy;
import akka.stream.javadsl.*;

/**
 * Created by dirceu on 05/11/16.
 */
public class ApplicationController extends Controller {

    private static ActorSystem system = ActorSystem.create("mixedTweets");
    private static Materializer mat = ActorMaterializer.create(system);
    static final Pair<ActorRef, Publisher<Object>> ti =
            Source.actorRef(100, OverflowStrategy.fail()).
                    toMat(Sink.asPublisher(AsPublisher.WITHOUT_FANOUT),
                            Keep.both()).run(mat);
    static ActorRef socketActor = ti.first();
    static Publisher<Object> publisher = ti.second();

    public play.mvc.Result index() {
        return ok("bem vindos ao twitter stream");
    }

    TwitterStreamListener list = new TwitterStreamListener();

    public play.mvc.Result stream(String queries) {
        Source<Object, ?> source = Source.fromPublisher(publisher);
        final Source<ByteString, ?> eventSource = source.map(ev ->
        {
            return ((ByteString) ev);
        });
        list.listenAndStream(queries, socketActor);
        list.twitterStream.addListener(list.statusListener);

        return ok().chunked(eventSource);
    }

}
