package com.rigter.miceli.modifier;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.*;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Component
public class MyFileChangeListener implements FileChangeListener {

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {

        ArrayList<CsvRecord> recordsList = new ArrayList<>();

        for(ChangedFiles cfiles : changeSet) {
            for(ChangedFile cfile: cfiles.getFiles()) {
                if(!isLocked(cfile.getFile().toPath())) {
                    try {
                        File file = cfile.getFile();

                        //****** CSV READ ************
                        if (file.getPath().endsWith("csv")) {
                            recordsList = doBuildCsvRecord(file);
                        }

                        //********* EXCEL READ ******************
                        if (file.getPath().endsWith("xlsx") || file.getPath().endsWith("xlx")) {
                            doUpdateFromExcel(file, recordsList);
                        }
                    } catch(IOException | InvalidFormatException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }

    private ArrayList<CsvRecord> doBuildCsvRecord(File file) throws IOException {
        ArrayList<CsvRecord> result = new ArrayList<>();
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        String line = "";
        String[] tempArr;
        while((line = br.readLine()) != null) {
            tempArr = line.split(";");  //,,,,,,,,,,,,,,,,,,,,,,,
            if (tempArr.length == 0 || tempArr[0].equalsIgnoreCase("Transfer_Code")) {
                //this is the header line, do nothing
                continue;
            }
            result.add(new CsvRecord(tempArr));
        }
        br.close();
        return result;
    }

    private void doUpdateFromExcel(File file, ArrayList<CsvRecord> csvRecords) throws IOException, InvalidFormatException {
        String accountNr = "";

        XSSFWorkbook workbook = new XSSFWorkbook(file);
        int sheetAmount = workbook.getNumberOfSheets();

        for (int i = 0; i < sheetAmount; i++) {
            XSSFSheet worksheet = workbook.getSheetAt(i);
            if (worksheet.getSheetName().startsWith("Zahlungsauftrag")) {

                ArrayList<String> amounts = new ArrayList<>();

                for(int j=0;j<worksheet.getLastRowNum() ;j++) {

                    XSSFRow row = worksheet.getRow(j);

                    if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue().equalsIgnoreCase("Konto-Nummer:")) {
                        accountNr = row.getCell(1).getStringCellValue();
                        System.out.println("Account Nr " + accountNr + " found:");
                        accountNr = doCleanAccountNr(accountNr);
                    }
                    if (row != null && row.getCell(7) != null && StringUtils.hasText(String.valueOf(row.getCell(7).getNumericCellValue()))) {
                        amounts.add(String.valueOf(row.getCell(7).getNumericCellValue()));
                    }
                }

                // Logik mit überschreiben
                for (CsvRecord record : csvRecords) {
                    if (record.getAccount_From().equalsIgnoreCase(accountNr)) {
                        if (amounts.size() > 0) {
                            String amount = amounts.get(0);
                            record.setAmount(amount);
                            System.out.println("Updated Transaction '" + record.getAvis_Text() + "' with Amount: " + amount);
                            amounts.remove(0);
                        }
                    }
                }

            } else {
                //do nothing
            }
        }
    }

    private String doCleanAccountNr(String accountNr) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < accountNr.length(); i++) {
            if (!(accountNr.charAt(i) == '.')) {
                result.append(accountNr.charAt(i));
            }
        }
        return result.toString();
    }


    private boolean isLocked(Path path) {
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }
}