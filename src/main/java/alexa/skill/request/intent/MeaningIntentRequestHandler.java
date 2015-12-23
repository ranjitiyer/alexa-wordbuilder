package alexa.skill.request.intent;

import alexa.skill.model.Dictionary;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

public class MeaningIntentRequestHandler {
    Dictionary dictionary;
    public MeaningIntentRequestHandler(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public SpeechletResponse getResponse(IntentRequest request, Session session) {
        String lastWord = (String) session.getAttribute("lastword");
        String meaning = dictionary.getMeaning(lastWord);

        if (meaning == null)
            meaning = "I could not find the meaning for the word " + lastWord;

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Meaning");
        card.setContent(meaning);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(meaning);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}
