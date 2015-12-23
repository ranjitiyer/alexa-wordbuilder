package alexa.skill.request.intent;

import alexa.skill.model.Dictionary;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.IntentRequest;
import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SpeechletResponse;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;

import java.util.Map;

public class WordIntentIntentRequestHandler {

    Dictionary dictionary;
    public WordIntentIntentRequestHandler(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    public SpeechletResponse getResponse(IntentRequest request, Session session) {
        Map<String, Slot> slots = request.getIntent().getSlots();
        Slot wordSlot = slots.get("Word");

        String theirWord = wordSlot.getValue();

        String cardText;
        String speechText;

        if (dictionary.hasBeenUsed(theirWord,session)) {
            cardText = "Sorry, the word " + theirWord + " has already been used";
            speechText = "Sorry, the word " + theirWord + " has already been used";
        }
        else {
            Character c = theirWord.charAt(theirWord.length() - 1);
            String myWord = dictionary.getWordStartWith(c);
            session.setAttribute("lastword", myWord);

            speechText = "My word is " + myWord;
            cardText = "My word is " + myWord;
        }

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("Word");
        card.setContent(cardText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        // mark the word as used
        Runnable markUsedTask = () -> {
            dictionary.markAsUsed(theirWord,session);
        };
        new Thread(markUsedTask).start();
        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }
}