package alexa.skill;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public class WordBuilderSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        supportedApplicationIds.add("amzn1.echo-sdk-ams.app.6b2e3501-d16f-4bd5-a3a5-7e9dc0f6e7c1");
    }

    public WordBuilderSpeechletRequestStreamHandler() {
        super(new WorldBuilderSpeechlet(), supportedApplicationIds);
    }
}
