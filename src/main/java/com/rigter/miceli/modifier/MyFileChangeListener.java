package com.rigter.miceli.modifier;


import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Set;

@Component
public class MyFileChangeListener implements FileChangeListener {

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        ArrayList<CsvRecord> recordsList = new ArrayList<>();
        String accountNr = "";
        for(ChangedFiles cfiles : changeSet) {
            ArrayList<File> excelFiles = extractExcelFiles(cfiles);
            File csvFile = findNewestCsv();
            for(ChangedFile cfile: cfiles.getFiles()) {
                if(!isLocked(cfile.getFile().toPath())) {
                    try {

                        //TODO file extension check
                        //********* EXCEL READ ******************

                        XSSFWorkbook workbook = new XSSFWorkbook(cfile.getFile());
                        XSSFSheet worksheet = workbook.getSheetAt(2);
                        for(int i=0;i<worksheet.getPhysicalNumberOfRows() ;i++) {

                            XSSFRow row = worksheet.getRow(i);

                            if (row != null && row.getCell(0) != null && row.getCell(0).getStringCellValue().equalsIgnoreCase("Konto-Nummer:")) {
                                accountNr = row.getCell(1).getStringCellValue();
                                System.out.println(accountNr);
                            }
                        }


                        //****** CSV READ ************
                        File file = cfile.getFile();
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
                            recordsList.add(new CsvRecord(tempArr));
                        }
                        System.out.print(recordsList);
                        br.close();
                    } catch(IOException | InvalidFormatException ioe) {
                        ioe.printStackTrace();
                    }


                }
            }
        }

        // Logik mit überschreiben
        for (CsvRecord record : recordsList) {
            if (record.getAccount_From().equalsIgnoreCase(accountNr)) {
                //
                record.setAmount("10000000000000000000000");
                System.out.println("FOUND ACCOUNT!!!!!!!!!!!!!!!!!!!!");
            }
        }


    }
    private ArrayList<File> extractExcelFiles(ChangedFiles cfiles) {
        System.out.println(cfiles);
        ArrayList<File> result = new ArrayList<>();

        for(ChangedFile cfile : cfiles.getFiles()) {
            File file = cfile.getFile();
            if (file.getPath().endsWith("xlsx")) {
                result.add(file);
            }
        }
        return result;
    }

    private File findNewestCsv() {
        File fileList = new File("C:\\scratch");
        File newestFile = null;
        FileTime newestCreation = null;

        if (!fileList.exists()) {
            return null;
        }
        try {
            for (File file : fileList.listFiles()) {
                if (newestFile == null) {
                    newestFile = file;
                    newestCreation = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
                }
                FileTime creationTime = (FileTime) Files.getAttribute(file.toPath(), "creationTime");
                //TODO is this correct??
                if (newestCreation.compareTo(creationTime) < 0) {
                    newestFile = file;
                }
            }
        } catch (IOException e) {
            System.out.print("IO EXCEPTION LOOKING FOR CSV FILES");
        }
        return newestFile;

    }

    private boolean isLocked(Path path) {
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }
}