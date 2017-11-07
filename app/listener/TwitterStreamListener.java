package listener;

import akka.actor.ActorRef;
import akka.util.ByteString;
import play.Logger;
import play.api.libs.Comet;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterStreamListener {

    private Configuration config;
    private ConfigurationBuilder configBuilder = new ConfigurationBuilder();
    public StatusListener statusListener;
    public TwitterStream twitterStream;
    FilterQuery query;
    private void initStream() {

        configBuilder.setOAuthConsumerKey("s7Wf2E3uAq5gbQdWl7PMHDho9");
        configBuilder.setOAuthConsumerSecret("kFgD2fjdwL51FqUQMHhelwnx8ndjadjG4SJQOR0FlBMtzUfEad");
        configBuilder.setOAuthAccessToken("86628543-wuxBJVBMLFThiBFGSka4VAqVPdUUVFRjDt808sPnO");
        configBuilder.setOAuthAccessTokenSecret("stdNfRbzEB1SoB39ze24s9mr7u6pTXORE85q8Lkh0CpBu");
        config = configBuilder.build();
        twitterStream = new TwitterStreamFactory(config).getInstance();
    }

    public TwitterStreamListener() {
        initStream();
    }

    public void listenAndStream(String queries, ActorRef socketActor) {
        statusListener = new StatusListener() {

            @Override
            public void onException(Exception ex) {

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {

            }

            @Override
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {

            }

            @Override
            public void onScrubGeo(long userId, long upToStatusId) {

            }

            @Override
            public void onStallWarning(StallWarning warning) {

            }

            public void onStatus(Status status) {

                Logger.info(status.getUser().getName());

                StringBuilder mensagem = new StringBuilder();

                mensagem.append(status.getUser().getName());
                mensagem.append(":");
                mensagem.append(status.getText());
                mensagem.append("\n");

                socketActor.tell(ByteString.fromString(mensagem.toString()),
                        ActorRef.noSender());

            }
        };
        twitterStream.addListener(statusListener);
        query = new FilterQuery().track(new String[]{queries});
        twitterStream.filter(query);

    }

}
