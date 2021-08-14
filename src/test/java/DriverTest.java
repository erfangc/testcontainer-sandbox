import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthRequest;
import org.elasticsearch.action.admin.cluster.health.ClusterHealthResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import java.io.IOException;

public class DriverTest {

    private final Logger log = LoggerFactory.getLogger(DriverTest.class);

    ElasticsearchContainer container = new ElasticsearchContainer(
            DockerImageName
                    .parse("docker.elastic.co/elasticsearch/elasticsearch-oss")
                    .withTag("7.10.2")
    ).withExposedPorts(9200);

    @Test
    void name() throws IOException {

        container.start();

        String httpHostAddress = container.getHost();
        int port = container.getMappedPort(9200);

        log.info("Found container: " + httpHostAddress + ":" + port);

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost(httpHostAddress, port, "http"))
        );

        ClusterHealthResponse response = client.cluster().health(new ClusterHealthRequest(), RequestOptions.DEFAULT);

        log.info(response.getStatus().toString());

    }
}
