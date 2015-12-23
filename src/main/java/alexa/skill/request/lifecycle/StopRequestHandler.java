package alexa.skill.request.lifecycle;

import com.amazon.speech.speechlet.Session;
import com.amazon.speech.speechlet.SessionEndedRequest;
import com.amazon.speech.speechlet.SpeechletResponse;

public class StopRequestHandler {
    public SpeechletResponse onStop(SessionEndedRequest request, Session session) {
        return null;
    }
}
