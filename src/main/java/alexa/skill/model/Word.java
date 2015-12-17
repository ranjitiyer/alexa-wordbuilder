package alexa.skill.model;


import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "WordBuilder")
public class Word {
    private String key;
    private boolean hasBeenUsed;

    @DynamoDBHashKey (attributeName = "Key")
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @DynamoDBAttribute(attributeName = "Used")
    public Boolean getHasBeenUsed() {
        return hasBeenUsed;
    }

    public void setHasBeenUsed(Boolean hasBeenUsed) {
        this.hasBeenUsed = hasBeenUsed;
    }

    public static void main(String[] args) {
        DynamoDBMapper mapper = new DynamoDBMapper(new AmazonDynamoDBClient());
        Word wordObj = mapper.load(Word.class, "id1_word");
        if (wordObj == null)
            System.out.println("Not found");
        System.out.println("Found");
    }
}
