package alexa.skill;

import com.amazon.speech.speechlet.Speechlet;
import com.amazon.speech.speechlet.lambda.SpeechletRequestStreamHandler;

import java.util.HashSet;
import java.util.Set;

public class WordBuilderSpeechletRequestStreamHandler extends SpeechletRequestStreamHandler {
    private static final Set<String> supportedApplicationIds = new HashSet<String>();
    static {
        supportedApplicationIds.add("amzn1.echo-sdk-ams.app.aa6d79df-d3b4-49d3-9834-aa3bdbc340df");
    }

    public WordBuilderSpeechletRequestStreamHandler() {
        super(new WorldBuilderSpeechlet(), supportedApplicationIds);
    }

}
