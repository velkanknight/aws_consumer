package br.com.rfsystems.aws_consumer.repository;


import br.com.rfsystems.aws_consumer.models.ProductEventKey;
import br.com.rfsystems.aws_consumer.models.ProductEventLog;
import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableScan//essa notação permite q essa classe seja rastrada pela classe de config do dinamydb
public interface ProductEventLogRepository extends CrudRepository<ProductEventLog, ProductEventKey> {

    //AS PESQUISAS NO DYNAMO POR CAMPO COMUM SAO MUITO PESADAS, POIS O BANCO VAI VARRER TODOS OS REGISTROS
    //O IDEAL É FAZER CONSULTAS DENTRO DOS CAMPOS PARTION KEY, QUE FORAM CRIADOS OU PESQUISAS INDEXADAS
    List<ProductEventLog> findAllByPk(String code);

    //essa pesquisa é feita em cima da parton key, é bem amis otimizada em relação a de cima
    List<ProductEventLog> findAllByPkAndSkStartsWith(String code, String eventType);


}
