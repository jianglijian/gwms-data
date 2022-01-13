package com.best.gwms.data.helper;



import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
    public static void main(String[] args) {
        Charset charset = Charset.forName("US-ASCII");
        try (BufferedReader reader = Files.newBufferedReader(Paths.get("C:\\Users\\bg236820\\Desktop\\mysql.log"), charset)) {
            String line = null;
            int i=1;
            while ((line = reader.readLine()) != null) {
              if(line.contains("Query_time")){
                  String[] s = line.split(" ");
                /*  if(Double.valueOf(s[2])>0d){
                      System.out.println("line："+i+" "+s[2]);
                  }*/
                  if(Double.valueOf(s[2])>2d){
                      System.out.println("1 line ："+i+" big:"+s[2]);
                  }
              }
              i++;
            }
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}
