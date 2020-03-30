package net.corda.explorer.db;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Iterator;

@Component
public class CityDB {

    private static final String FILE_NAME = "worldcities.xlsx";
    private HashMap<String, CityDetail> database;

    public CityDB() {
        database = new HashMap<>();
        loadCityData();
    }

    private void loadCityData(){
        try {
            Resource resource = new ClassPathResource(FILE_NAME);
            Workbook workbook = new XSSFWorkbook(resource.getInputStream());
            Sheet citySheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = citySheet.iterator();

            // skip header
            iterator.next();

            while (iterator.hasNext()) {
                Row currentRow = iterator.next();

                CityDetail cityDetail = new CityDetail(
                        (float) currentRow.getCell(2).getNumericCellValue(),
                        (float) currentRow.getCell(3).getNumericCellValue(),
                        currentRow.getCell(4).getStringCellValue());
                database.put(currentRow.getCell(0).getStringCellValue() + "_" +
                        currentRow.getCell(5).getStringCellValue(), cityDetail);
                database.put(currentRow.getCell(1).getStringCellValue() + "_" +
                        currentRow.getCell(5).getStringCellValue(), cityDetail);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public CityDetail getCityDetails(String key){
        return database.get(key);
    }

    public static class CityDetail{
        private float lat;
        private float lng;
        private String country;


        public CityDetail(float lat, float lng, String country) {
            this.lat = lat;
            this.lng = lng;
            this.country = country;
        }

        public float getLat() {
            return lat;
        }

        public float getLng() {
            return lng;
        }

        public String getCountry() {
            return country;
        }
    }
}
