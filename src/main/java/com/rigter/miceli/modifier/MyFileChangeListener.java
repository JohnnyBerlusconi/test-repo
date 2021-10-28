package com.rigter.miceli.modifier;


import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Set;

@Component
public class MyFileChangeListener implements FileChangeListener {

    @Override
    public void onChange(Set<ChangedFiles> changeSet) {
        for(ChangedFiles cfiles : changeSet) {
            for(ChangedFile cfile: cfiles.getFiles()) {
                //TODO implement business logic
                if(!isLocked(cfile.getFile().toPath())) {
                    try {
                        File file = cfile.getFile();
                        FileReader fr = new FileReader(file);
                        BufferedReader br = new BufferedReader(fr);
                        String line = "";
                        String[] tempArr;
                        while((line = br.readLine()) != null) {
                            tempArr = line.split(",");
                            for(String tempStr : tempArr) {
                                System.out.print(tempStr + " ");
                            }
                            System.out.println();
                        }
                        br.close();
                    } catch(IOException ioe) {
                        ioe.printStackTrace();
                    }
                }
            }
        }
    }

    private boolean isLocked(Path path) {
        try (FileChannel ch = FileChannel.open(path, StandardOpenOption.WRITE); FileLock lock = ch.tryLock()) {
            return lock == null;
        } catch (IOException e) {
            return true;
        }
    }
}