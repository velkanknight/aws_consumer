package br.com.rfsystems.aws_consumer.models;

import br.com.rfsystems.aws_consumer.enums.EventType;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import org.springframework.data.annotation.Id;

@DynamoDBTable(tableName = "product-events")
public class ProductEventLog {

    public ProductEventLog() {
    }

    //nao podemos ter get e set do campo id no dynamo
    @Id
    private ProductEventKey productEventKey;

    @DynamoDBAttribute
    private long productId;

    @DynamoDBAttribute
    private String username;

    @DynamoDBAttribute
    private long timestamp;

    @DynamoDBAttribute(attributeName = "ttl")
    private long ttl;

    @DynamoDBAttribute(attributeName = "messageId")
    private String messageId;

    @DynamoDBAttribute
    @DynamoDBTypeConvertedEnum
    private EventType eventType;


    //PRECISAMOS TRATAR O GET E SET DO PK DESSA MANEIRA
    @DynamoDBHashKey(attributeName = "pk")
    public String getPk(){
        return this.productEventKey != null ? this.productEventKey.getPk() : null;
    }

    public void setPk(String pk){
        if (this.productEventKey == null){
            this.productEventKey = new ProductEventKey();
        }
        this.productEventKey.setPk(pk);
    }

    //MESMA COISA PRO SK
    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk(){
        return this.productEventKey != null ? this.productEventKey.getSk() : null;
    }

    public void setSk(String sk){
        if (this.productEventKey == null){
            this.productEventKey = new ProductEventKey();
        }
        this.productEventKey.setSk(sk);
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public long getTtl() {
        return ttl;
    }

    public void setTtl(long ttl) {
        this.ttl = ttl;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
