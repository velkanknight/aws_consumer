package br.com.rfsystems.aws_consumer.models;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;

public class ProductEventKey {

    public ProductEventKey() {
    }

    private String pk;
    private String sk;

    @DynamoDBHashKey(attributeName = "pk")
    public String getPk() {
        return pk;
    }

    public void setPk(String pk) {
        this.pk = pk;
    }
    //definindo o range de valores q podem estar na partition key
    @DynamoDBRangeKey(attributeName = "sk")
    public String getSk() {
        return sk;
    }

    public void setSk(String sk) {
        this.sk = sk;
    }
}
