package com.example.alaze.fileexplorer.browse;

import java.io.File;

/**
 * Created by Alaze on 17.11.2017.
 */

public class DeleteFile {
    public Boolean Delete(File source) {
        if (source.isDirectory()) {
            File[] files = source.listFiles();
            for (File myfile : files) {
                if (!Delete(myfile)) {
                    return false;
                }
            }
            if(source.delete())
                return true;
        } else {
            if (source.delete())
                return true;
        }
        return true;
    }
}
