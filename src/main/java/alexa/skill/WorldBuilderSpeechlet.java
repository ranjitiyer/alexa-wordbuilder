package alexa.skill;

import alexa.skill.model.Dictionary;
import alexa.skill.request.intent.MeaningIntentRequestHandler;
import alexa.skill.request.intent.WordIntentIntentRequestHandler;
import alexa.skill.request.lifecycle.LaunchRequestHandler;
import alexa.skill.request.lifecycle.StartRequestHandler;
import alexa.skill.request.lifecycle.StopRequestHandler;
import com.amazon.speech.slu.Intent;
import com.amazon.speech.speechlet.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldBuilderSpeechlet implements Speechlet {
    private static final Logger log = LoggerFactory.getLogger(WorldBuilderSpeechlet.class);
    Dictionary dictionary = new Dictionary();

    @Override
    public void onSessionStarted(final SessionStartedRequest request, final Session session)
            throws SpeechletException {
        dictionary.initialize();
    }

    @Override
    public SpeechletResponse onLaunch(final LaunchRequest request, final Session session)
            throws SpeechletException {
        return new LaunchRequestHandler().getResponse(request,session);
    }

    @Override
    public void onSessionEnded(final SessionEndedRequest request, final Session session)
            throws SpeechletException {
        new StopRequestHandler().onStop(request,session);
    }


    @Override
    public SpeechletResponse onIntent(final IntentRequest request, final Session session)
            throws SpeechletException {
        Intent intent = request.getIntent();
        String intentName = (intent != null) ? intent.getName() : null;

        switch (intentName) {
            case "StartIntent" : {
                dictionary.initialize();

                return new StartRequestHandler(dictionary)
                        .getResponse(request, session);
            }
            case "WordIntent" : {
                return new WordIntentIntentRequestHandler(dictionary)
                        .getResponse(request, session);
            }
            case "MeaningIntent" : {
                return new MeaningIntentRequestHandler(dictionary)
                        .getResponse(request, session);
            }
            default : {
                throw new SpeechletException("Invalid Intent");
            }
        }
    }
}
