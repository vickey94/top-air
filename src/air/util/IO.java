package air.util;

import air.model.Flight;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by vickey on 2016/7/22.
 */
public class IO {

    public static void main(String args[]) throws IOException {

       IO io = new IO();
        io.read2();

    }


    private void read() throws IOException {
        /******* 读取文件 ********/
        BufferedReader bfread = new BufferedReader(new FileReader("E:\\air\\SHA\\tb_flight.csv"));
        Flight flight;  //一条航班数据

        String line=null;

        while ((line = bfread.readLine()) != null) {


            String[] item = line.split(",");
            System.out.println(item[0]);
            flight = new Flight(Integer.parseInt(item[0]), item[1],
                    item[2], item[3],
                    item[4], item[5],
                    item[6], item[7],
                    item[8], item[9],
                    item[10], item[11],
                    item[12], item[13],
                    item[14], item[15],
                    item[16], item[17],
                    item[18], item[19],
                    item[20], item[21], item[22]
            );
            System.out.println(flight.toString());
        }
        bfread.close();

    }
    private void read2() throws IOException {
        FileInputStream inputStream = null;
        Scanner sc = null;
        try {
            inputStream = new FileInputStream("E:\\air\\SHA\\tb_flight.csv");
            sc = new Scanner(inputStream, "UTF-8");
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                 System.out.println(line);
            }
            // note that Scanner suppresses exceptions
            if (sc.ioException() != null) {
                throw sc.ioException();
            }
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (sc != null) {
                sc.close();
            }
        }

    }


    private void write() throws IOException {
        /******* 写入文件 ********/
        BufferedWriter bfwrite = new BufferedWriter(new FileWriter("E:\\air\\TOP\\a.txt"));

        List list = new ArrayList<>();
        String str = null;
        for (int num = 0; num < 29; num++) {

            str = str + num + ",";
        }
        str += 29;

        for (int i = 0; i < 1000000; i++) {

            list.add(str);

        }


        for (int i = 0; i < 100000; i++) {

            bfwrite.write(list.indexOf(i));


            bfwrite.newLine();
            System.out.println(i);


        }

        bfwrite.flush();

        bfwrite.close();
    }
}
