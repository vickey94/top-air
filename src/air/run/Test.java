package air.run;


import air.config.Log;
import air.config.Temp;
import air.model.CanData;
import air.model.Flight;
import air.model.Weather;
import air.util.Excel;
import air.util.TimeUtil;
import air.util.ValidData;


import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import java.util.List;

/**
 * Created by vickey on 2016/7/20.
 * 从数据库tb_train表中获得训练数据，tb_weather获得对应的天气
 * 归一化数据，生成训练文件，导出用于神经网络（BP）训练
 * 生成报表分析
 * 数据库air list_airtype表中存放机型对照表 list_airport存放机场对照表
 */
public class Test {

    //数据库配置文件
    public static final String url = "jdbc:mysql://localhost:3306/air";
    public static final String name = "com.mysql.jdbc.Driver";
    public static final String user = "root";
    public static final String password = "123";

    // 机型
    public final static List<String> AIRTYPE_List = new ArrayList<String>();
    // 机场
    public final static List<String> AIRPORT_List = new ArrayList<String>();






    public static void main(String args[]) throws ClassNotFoundException,
            SQLException, IOException {


        /******* 读取文件 ********/
        BufferedReader bfread = new BufferedReader(new FileReader("E:\\air\\SHA\\tb_flight.csv"));

        /******* 写入文件 ********/
        BufferedWriter bfwrite = new BufferedWriter(new FileWriter("E:\\air\\SHA\\flight-0721.csv"));



        /************ 将List加载到CanData用于数据处理 **************/
        CanData canData = new CanData(AIRTYPE_List, AIRPORT_List);
        Test test = new Test();
        test.getChart();

        Class.forName(name);// 指定连接类型
        Connection conn = DriverManager.getConnection(url, user, password);// 获取连接


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
            Log.total++;


            /**************** 数据查询 *******************/

            Weather dep_weather = new Weather(); //出发城市天气
            Weather arr_weather = new Weather(); //到达城市天气


            //验证航班
            if (ValidData.validData(flight)) {
                Log.actTrianData++;
                /****前序航班到港****/

                int adv_delays = 0;  //默认前序航班到港延误为0

                String sql_advf = "SELECT * FROM tb_train WHERE TimeSeries = '" + flight.getTimeSeries() + "'"
                        + " AND ArrAirport='" + flight.getDepAirport() + "'"
                        + " AND Acft='" + flight.getAcft() + "'"
                        + " AND ArrAirport = '" + flight.getDepAirport() + "'"
                        + " AND (FlightNo= '" + flight.getCarrier() + (Integer.parseInt(flight.getFlightNoShort()) - 1) + "'"
                        + " OR FlightNo= '" + flight.getFlightNo() + "')";

                PreparedStatement pst_advf = conn.prepareStatement(sql_advf);
                ResultSet result_adv = pst_advf.executeQuery();
                if (result_adv.next()) {
                    Flight advf = new Flight(result_adv.getInt(1), result_adv.getString(2), result_adv.getString(3), result_adv.getString(4),
                            result_adv.getString(5), result_adv.getString(6), result_adv.getString(7), result_adv.getString(8),
                            result_adv.getString(9), result_adv.getString(10), result_adv.getString(11), result_adv.getString(12),
                            result_adv.getString(13), result_adv.getString(14), result_adv.getString(15), result_adv.getString(16),
                            result_adv.getString(17), result_adv.getString(18), result_adv.getString(19), result_adv.getString(20),
                            result_adv.getString(21), result_adv.getString(22), result_adv.getString(23));

                    int adv = TimeUtil.timeMinus(advf.getArrTime(), flight.getDepTime());

                    if (adv > 29 && adv < 200) {
                        Log.have_AdvFlight++;
                        adv_delays = TimeUtil.timeMinus(advf.getArrTime(), advf.getActArrTime());

                    }

                }
                pst_advf.close();
                /**** 查询天气 ****/

                // 0天气完整，1缺失出发城市天气，2缺失到达城市天气，3两地天气都缺失
                int miss_w = 0;

                String sql_depW = "SELECT * FROM tb_weather WHERE date='"
                        + flight.getTimeSeries() + "' AND city='"
                        + flight.getDepCity() + "'";// 出发城市天气
                String sql_arrW = "SELECT * FROM tb_weather WHERE date='"
                        + flight.getTimeSeries() + "' AND city='"
                        + flight.getArrCity() + "'";// 到达城市天气

                PreparedStatement pst_dw = conn.prepareStatement(sql_depW);
                ResultSet result_dw = pst_dw.executeQuery();
                if (result_dw.next()) {
                    dep_weather.setCity(result_dw.getString(1));
                    dep_weather.setDate(result_dw.getString(3));
                    dep_weather.setTemphi(result_dw.getString(4));
                    dep_weather.setTemplow(result_dw.getString(5));
                    dep_weather.setDescription(result_dw.getString(6));
                    dep_weather.setWindir(result_dw.getString(7));
                    dep_weather.setWindstrength(result_dw.getString(8));
                } else {
                    dep_weather.setCity(null);
                    dep_weather.setDate(null);
                    dep_weather.setTemphi("0.0");
                    dep_weather.setTemplow("0.0");
                    dep_weather.setDescription("0.0");
                    dep_weather.setWindir("0.0");
                    dep_weather.setWindstrength("微风"); // 为减小计算影响，这里设置为微风
                    miss_w = 1;

                }
                pst_dw.close();

                PreparedStatement pst_aw = conn.prepareStatement(sql_arrW);
                ResultSet result_aw = pst_aw.executeQuery();

                if (result_aw.next()) {
                    arr_weather.setCity(result_aw.getString(1));
                    arr_weather.setDate(result_aw.getString(3));
                    arr_weather.setTemphi(result_aw.getString(4));
                    arr_weather.setTemplow(result_aw.getString(5));
                    arr_weather.setDescription(result_aw.getString(6));
                    arr_weather.setWindir(result_aw.getString(7));
                    arr_weather.setWindstrength(result_aw.getString(8));
                } else {
                    arr_weather.setCity(null);
                    arr_weather.setDate(null);
                    arr_weather.setTemphi("0.0");
                    arr_weather.setTemplow("0.0");
                    arr_weather.setDescription("0.0");
                    arr_weather.setWindir("0.0");
                    arr_weather.setWindstrength("微风"); // 为减小计算影响，这里设置为微风
                    if (miss_w == 0) miss_w = 2;
                    if (miss_w == 1) miss_w = 3;

                }
                pst_aw.close();

                if (miss_w == 1) Log.miss_DepWeather++;
                if (miss_w == 2) Log.miss_ArrWeather++;
                if (miss_w == 3) Log.miss_Weather++;

                if (miss_w != 0) Temp.list_incomplete_Data.add(flight.toString()+","+miss_w);

                /******* END *********/

                /***** 调用CanData处理数据 ****/
                canData.SetData(flight, dep_weather, arr_weather, adv_delays);
//                Temp.list_canData.add(canData.toString());
                System.out.println(canData.toString() + "," + flight.getId());
                bfwrite.write(canData.toString() + "," + flight.getId());
                bfwrite.newLine();

            }//if

        }//while
        bfwrite.flush();
        bfread.close();
        bfwrite.close();
        conn.close();


        Log log = new Log();
        System.out.print(log.toString());
        System.out.println("-----FINISH----");

        //   outExcel();


    }


    private void outExcel() throws IOException {
        Excel excel = new Excel();
        String[] sheets = {"trainData","canData","incomplete_Data","unusual_Data","opCar_N_Data"};
        excel.createExcel(sheets);

      //  excel.superAddData("trainData",Temp.list_trainData);
        //  Temp.list_trainData=null;
      //  excel.superAddData("canData",Temp.list_canData);
        //   Temp.list_canData=null;
     //   excel.superAddData("incomplete_Data",Temp.list_incomplete_Data);
        //   Temp.list_incomplete_Data=null;
     //   excel.superAddData("unusual_Data",Temp.list_unusual_Data);
        //   Temp.list_unusual_Data=null;
     //   excel.superAddData("opCar_N_Data",Temp.list_opCar_N_Data);
        //  Temp.list_opCar_N_Data=null;
        System.out.println("Excel表数据导入完成！");
    }

    private void getChart() throws ClassNotFoundException, SQLException {
        /******* 连接数据库 ********/
        Class.forName(name);// 指定连接类型
        Connection conn = DriverManager.getConnection(url, user, password);// 获取连接

        /******* 从数据库读取机型列表 AIRTYPE_List ********/
        String sql_AIRTYPE = "SELECT * FROM list_airtype";

        PreparedStatement pst_AIRTYPE = conn.prepareStatement(sql_AIRTYPE);

        ResultSet r_AIRTYPE = pst_AIRTYPE.executeQuery();
        while (r_AIRTYPE.next()) {
            AIRTYPE_List.add(r_AIRTYPE.getString(1));
        }
        pst_AIRTYPE.close();

        /********* 从数据库读取机场列表 AIRPORT_List ************/
        String sql_AIRPORT = "SELECT * FROM list_airport";

        PreparedStatement pst_AIRPORT = conn.prepareStatement(sql_AIRPORT);

        ResultSet r_AIRPORT = pst_AIRPORT.executeQuery();
        while (r_AIRPORT.next()) {
            AIRPORT_List.add(r_AIRPORT.getString(1));
        }
        pst_AIRPORT.close();
    }
    private void getAdv_delays(){

    }
}
