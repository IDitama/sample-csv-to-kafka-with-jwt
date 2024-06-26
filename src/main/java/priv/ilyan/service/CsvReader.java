package priv.ilyan.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;


@Singleton
public class CsvReader {

    @ConfigProperty(name = "csv.delimiter", defaultValue = ",")
    String delimiter;

    @ConfigProperty(name = "bulk.limit", defaultValue = "10")
    int bulkLimit;

    @Inject
    KafkaMessageProducer kafkaMessageProducer;

    Jsonb jsonBuilder = JsonbBuilder.create();

    public void readAndPrepare(String content) {
        if (content != null && !content.isEmpty()) {
            String[] rows = content.split(System.lineSeparator());
            List<List<Object>> listData = new ArrayList<>();
            for (int idx = 0; idx < rows.length; idx++) {
                String[] tempListData = rows[idx].split(delimiter);
                List<Object> tempList = new ArrayList<>();

                for (int i = 0; i < tempListData.length; i++) {
                    tempList.add(tempListData[i]);
                }
                listData.add(tempList);
                if ((idx + 1) == bulkLimit || (idx + 1) == rows.length) {
                    kafkaMessageProducer.sendToKafkal(jsonBuilder.toJson(listData));
                    listData = new ArrayList<>();
                }
            }
        }
    }
}
