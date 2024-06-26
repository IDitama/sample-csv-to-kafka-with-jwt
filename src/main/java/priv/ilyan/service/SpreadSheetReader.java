package priv.ilyan.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class SpreadSheetReader {

    @ConfigProperty(name = "bulk.limit", defaultValue = "10")
    int bulkLimit;

    @Inject
    KafkaMessageProducer kafkaMessageProducer;

    Jsonb jsonBuilder = JsonbBuilder.create();

    public void readAndPrepare(File fileInput){
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(fileInput);
            XSSFSheet sheet = workbook.getSheetAt(0);
            List<List<Object>> listData = new ArrayList<>();
            for (int idx = 0; idx < sheet.getPhysicalNumberOfRows(); idx++) {
                XSSFRow row =  sheet.getRow(idx);
                List<Object> temp = new ArrayList<>();
                for (int i = 0; i < row.getPhysicalNumberOfCells(); i++) {
                    temp.add(row.getCell(i).toString());
                }
                listData.add(temp);
                if ((idx + 1) == bulkLimit || (idx + 1) == sheet.getPhysicalNumberOfRows()) {
                    kafkaMessageProducer.sendToKafkal(jsonBuilder.toJson(listData));
                    listData = new ArrayList<>();
                }
            }
            workbook.close();
        } catch (Exception e) {
            log.error(e.getCause().getMessage());
        }
    }
}
