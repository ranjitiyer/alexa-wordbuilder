package alexa.skill.request.lifecycle;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class LaunchRequestHandler {
    public SpeechletResponse getResponse(com.amazon.speech.speechlet.LaunchRequest request, Session session) {
        String speechText = "Ready to play Word Builder.";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Game started");
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
