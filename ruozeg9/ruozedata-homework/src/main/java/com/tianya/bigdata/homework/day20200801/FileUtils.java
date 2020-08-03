package com.tianya.bigdata.homework.day20200801;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileUtils {

    public static void delete(Configuration conf,String output)throws Exception {
        FileSystem fileSystem = FileSystem.get(conf);
        Path outputPath = new Path(output);
        if(fileSystem.exists(outputPath)){
            fileSystem.delete(outputPath,true);
        }

    }
}
