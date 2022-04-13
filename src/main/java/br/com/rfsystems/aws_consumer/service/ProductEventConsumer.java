package br.com.rfsystems.aws_consumer.service;

import br.com.rfsystems.aws_consumer.models.Envelope;
import br.com.rfsystems.aws_consumer.models.ProductEvent;
import br.com.rfsystems.aws_consumer.models.ProductEventLog;
import br.com.rfsystems.aws_consumer.models.SnsMessage;
import br.com.rfsystems.aws_consumer.repository.ProductEventLogRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Service
public class ProductEventConsumer {

    private static final Logger LOG = LoggerFactory.getLogger(ProductEventConsumer.class);
    private ObjectMapper objectMapper;
    private ProductEventLogRepository repository;

    @Autowired
    public ProductEventConsumer(ObjectMapper objectMapper, ProductEventLogRepository repository){
        this.objectMapper = objectMapper;
        this.repository = repository;
    }

    @JmsListener(destination = "${aws.sqs.queue.product.events.name}")
    public void receiveProductEvent(TextMessage textMessage) throws IOException, JMSException {

        SnsMessage snsMessage = objectMapper.readValue(textMessage.getText(), SnsMessage.class);
        Envelope envelope = objectMapper.readValue(snsMessage.getMessage(), Envelope.class);
        ProductEvent productEvent = objectMapper.readValue(envelope.getData(), ProductEvent.class);

        LOG.info("Product event received - Event: {} - Product: {} - MessageId: {} ", envelope.getEventType(), productEvent.getProductId(), snsMessage.getMessageId());

        ProductEventLog productEventLog = buildProductEventLogFrom(envelope, productEvent, snsMessage.getMessageId());

        ProductEventLog save = repository.save(productEventLog);

        LOG.info("Product saved- Product: {} ", save.getProductId());

    }
    private ProductEventLog buildProductEventLogFrom(Envelope envelope, ProductEvent productEvent, String messageId){

        long timestamp = Instant.now().toEpochMilli();

        ProductEventLog productEventLog = new ProductEventLog();
        productEventLog.setPk(productEvent.getCode());
        productEventLog.setSk(envelope.getEventType() + "_" + timestamp);
        productEventLog.setEventType(envelope.getEventType());
        productEventLog.setProductId(productEvent.getProductId());
        productEventLog.setUsername(productEvent.getUserName());
        productEventLog.setTimestamp(timestamp);
        productEventLog.setMessageId(messageId);
        //definindo que a partir de 10 min o registro sera deletado do dynamo
        productEventLog.setTtl(Instant.now().plus(Duration.ofMinutes(10)).getEpochSecond());
        return productEventLog;
    }

}
