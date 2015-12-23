package alexa.skill.model;

import com.amazon.speech.speechlet.Session;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.apache.commons.lang3.RandomUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Dictionary {
    DynamoDBMapper mapper = new DynamoDBMapper(new AmazonDynamoDBClient());

    Character[] charList;
    Map<String, Boolean> wordMap;
    Map<Character, List<String>> wordList;

    public void initialize() {
        try {
            charList = new Character[] {'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'};
            wordMap = new HashMap<>();
            wordList = new HashMap() {{
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

            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("words.txt")));
            String word;
            while ( (word = br.readLine()) != null) {
                if (word.matches("^[a-z]*") && word.length() > 2) {
                    // used words
                    wordMap.put(word, false);

                    // word list
                    Character c = word.charAt(0);
                    wordList.get(c).add(word);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean exists(String word) {
        return false;
    }

    public String getWordStartWith(Character c) {
        List<String> words = wordList.get(c);
        return words.get(new Random().nextInt(words.size()));
    }

    public String getRandomWord()  {
        List<String> words = wordList.get(charList[RandomUtils.nextInt(0, 26)]);
        return words.get(RandomUtils.nextInt(0, words.size()));
    }

    public String getMeaning(String word) {
        try {
            // These code snippets use an open-source library. http://unirest.io/java
            HttpResponse<JsonNode> response = Unirest.get(
                    "https://wordsapiv1.p.mashape.com/words/" + word + "/definitions")
                    .header("X-Mashape-Key", "AbXsPaHSRgmshQZ6lYRprhFZE28Yp1H10swjsn908AZ0JxBfY0")
                    .header("Accept", "application/json")
                    .asJson();

            org.json.JSONObject definition = response.getBody().getObject().getJSONArray("definitions").getJSONObject(0);
            return definition.getString("definition");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public boolean markAsUsed(String word, Session session) {
        Word wordObj = new Word();
        wordObj.setKey(session.getSessionId() + "_" + word);
        wordObj.setHasBeenUsed(true);
        mapper.save(wordObj);
        return true;
    }

    public boolean hasBeenUsed(String word, Session session) {
        String key = session.getSessionId() + "_" + word;
        Word wordObj = mapper.load(Word.class, key);
        if (wordObj == null)
            return false;
        return wordObj.getHasBeenUsed();
    }
}
