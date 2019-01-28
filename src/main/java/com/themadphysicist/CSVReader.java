package com.themadphysicist;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

class CSVReader {
  static List<Subscriber> parseCSV(String path) throws IOException {
    List<Subscriber> subs = new ArrayList<>();
    Reader reader = Files.newBufferedReader(Paths.get(path));
    CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.withFirstRecordAsHeader()
        .withIgnoreHeaderCase().withTrim());
    for (CSVRecord csvRecord : csvParser) {
      subs.add(new Subscriber(csvRecord.get("id"), csvRecord.get("email"),
          csvRecord.get("created_at"), csvRecord.get("deleted_at")));
    }
    return subs;
  }
}
