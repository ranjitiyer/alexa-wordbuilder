package alexa.skill.request.lifecycle;

import alexa.skill.model.Dictionary;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionStartedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class StartRequestHandler {
    Dictionary dictionary;

    public StartRequestHandler(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public SpeechletResponse getResponse(IntentRequest request, Session session) {
        String speechText = dictionary.getRandomWord();

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Word");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);

    }
}
