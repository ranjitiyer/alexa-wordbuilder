package alexa.skill;

import com.amazon.speech.slu.Intent;
import com.amazon.speech.slu.Slot;
import com.amazon.speech.speechlet.*;
import com.amazon.speech.ui.PlainTextOutputSpeech;
import com.amazon.speech.ui.Reprompt;
import com.amazon.speech.ui.SimpleCard;
import com.amazonaws.util.json.JSONObject;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class WorldBuilderSpeechlet implements Speechlet {

    private static final Logger log = LoggerFactory.getLogger(WorldBuilderSpeechlet.class);

    static Map<String, Boolean> wordsMap = new HashMap<>();
    static Map<Character, List<String>> wordList = new HashMap() {{
        put('a', new ArrayList<>()); put('b', new ArrayList<>()); put('c', new ArrayList<>());
        put('d', new ArrayList<>()); put('e', new ArrayList<>()); put('f', new ArrayList<>());
        put('g', new ArrayList<>()); put('h', new ArrayList<>()); put('i', new ArrayList<>());
        put('j', new ArrayList<>()); put('k', new ArrayList<>()); put('l', new ArrayList<>());
        put('m', new ArrayList<>()); put('n', new ArrayList<>()); put('o', new ArrayList<>());
        put('p', new ArrayList<>()); put('q', new ArrayList<>()); put('r', new ArrayList<>());
        put('s', new ArrayList<>()); put('t', new ArrayList<>()); put('u', new ArrayList<>());
        put('v', new ArrayList<>()); put('w', new ArrayList<>()); put('x', new ArrayList<>());
        put('y', new ArrayList<>()); put('z', new ArrayList<>());
    }};

    static {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("words.txt")));
            String word;
            while ( (word = br.readLine()) != null) {
                if (word.matches("^[a-z]*") && word.length() > 2) {
                    // used words
                    wordsMap.put(word, false);

                    // word list
                    Character c = word.charAt(0);
                    wordList.get(c).add(word);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionStarted requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        // any initialization logic goes here
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        log.info("onLaunch requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        return getLaunchResponse();
    }

    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {

        log.info("onIntent requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());

        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        if (intentName.equals("StartIntent")) {
            return getStartResponse();
        }
        else if (intentName.equals("GotItIntent")) {
            return gotitResponse();
        }
        else if (intentName.equals("WordIntent")) {
            return wordIntentResponse(intent,session);
        }
        else if (intentName.equals("MeaningIntent")) {
            String lastWord = (String) session.getAttribute("lastword");
            String meaning = getMeaning(lastWord);

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
        else {
            throw new SpeechletException("Invalid Intent");
        }
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        log.info("onSessionEnded requestId={}, sessionId={}", request.getRequestId(),
                session.getSessionId());
        // any cleanup logic goes here
    }

    public SpeechletResponse wordIntentResponse(Intent intent, Session session) {
        Map<String, Slot> slots = intent.getSlots();

        // Get the color slot from the list of slots.
        Slot wordSlot = slots.get("Word");
        String theirWord = wordSlot.getValue();

        // check if this is a valid word

        Character c = theirWord.charAt(theirWord.length()-1);

        List<String> words = wordList.get(c);
        String myword = words.get(new Random().nextInt(words.size()));

        session.setAttribute("lastword", myword);

        String speechText = "My word is " + myword;

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
    public SpeechletResponse getStartResponse() {
        String speechText = "anarchy";

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

    public SpeechletResponse gotitResponse() {
        return null;
    }

/*
    private SpeechletResponse getMeaningResponse(Intent intent, String word) {

    }
*/

    /**
     * Creates and returns a {@code SpeechletResponse} with a welcome message.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getLaunchResponse() {
        String speechText = "Ready to play Word Builder. " +
                "I'll say a word then you say another word starting with the last letter of the word I said";

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

    /**
     * Creates a {@code SpeechletResponse} for the hello intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelloResponse() {
        String speechText = "Hello world";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        return SpeechletResponse.newTellResponse(speech, card);
    }

    /**
     * Creates a {@code SpeechletResponse} for the help intent.
     *
     * @return SpeechletResponse spoken and visual response for the given intent
     */
    private SpeechletResponse getHelpResponse() {
        String speechText = "You can say hello to me!";

        // Create the Simple card content.
        SimpleCard card = new SimpleCard();
        card.setTitle("HelloWorld");
        card.setContent(speechText);

        // Create the plain text output.
        PlainTextOutputSpeech speech = new PlainTextOutputSpeech();
        speech.setText(speechText);

        // Create reprompt
        Reprompt reprompt = new Reprompt();
        reprompt.setOutputSpeech(speech);

        return SpeechletResponse.newAskResponse(speech, reprompt, card);
    }

    private String getMeaning(String word) {
        try {
            // These code snippets use an open-source library. http://unirest.io/java
            HttpResponse<JsonNode> response = Unirest.get(
                    "https://wordsapiv1.p.mashape.com/words/" + word + "/definitions")
                    .header("X-Mashape-Key", "AbXsPaHSRgmshQZ6lYRprhFZE28Yp1H10swjsn908AZ0JxBfY0")
                    .header("Accept", "application/json")
                    .asJson();

            org.json.JSONObject definition = response.getBody().getObject().getJSONArray("definitions").getJSONObject(0);
            return definition.getString("definition");
        } catch (UnirestException ex) {
            ex.printStackTrace();
        }
        return null;
    }


}
