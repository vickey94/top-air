package air.util;

import air.config.Log;
import air.config.Temp;
import air.model.Flight;

public class ValidData {


    //状态17 是否是实际承运航班23  离港延误
    public static boolean validData(Flight flight) {

        String opCar = flight.getOpCar();
        if (opCar.equals("N")) {
            Log.opCar_N_Data++;
            Temp.list_opCar_N_Data.add(flight.toString());

            return false;
        }
        Log.opCar_O_Data++;

        if (flight.getActDepTime().equals("")) {
            Log.miss_ActTime++;
            Temp.list_unusual_Data.add(flight.toString()+","+"miss_ActTime");
            return false;
        }

        String comment = flight.getComment();
        String[] vali_comment = {"取消", "未知", "已備降", "已排班", "n/a", "正在滑行", "途中"};
        for (String str : vali_comment) {
            if (str.equals(comment)) {
                Log.unusual_Comment++;
                Temp.list_unusual_Data.add(flight.toString()+","+"unusual_Comment");
                return false;
            }
        }


        if (TimeUtil.timeMinus(flight.getDepTime(), flight.getActDepTime()) < -30) {
            Log.unusual_ActDepTime++;
            Temp.list_unusual_Data.add(flight.toString()+","+"unusual_ActDepTime");

            return false;
        }


        //按照1200KM/S，即20KM/s来算
        int actFlyTime = Integer.parseInt(flight.getActFlyingTime());
        int distKm = Integer.parseInt(flight.getDistKm());
        if (actFlyTime == 0) {
            Log.unusual_ActFlyTime++;
            Temp.list_unusual_Data.add(flight.toString()+","+"unusual_ActFlyTime");
            return false;
        }
        if ((distKm / actFlyTime) > 20) {
            Log.unusual_DistKm_ActFlyTime++;
            Temp.list_unusual_Data.add(flight.toString()+","+"unusual_DistKm_ActFlyTime");
            return false;
        }

        Temp.list_trainData.add(flight.toString());
        return true;
    }


    public static boolean validData_SHA(Flight flight) {
        String comment = flight.getComment();
        String opCar = flight.getOpCar();
        String[] vali_comment = {"取消", "未知", "已備降", "已排班", "n/a", "正在滑行", "途中"};
        for (String str : vali_comment) {
            if (str.equals(comment)) {

                return false;
            }
        }

        if (!flight.getDepAirport().equals("SHA")) {

            return false;
        }

        if (opCar.equals("N")) {

            return false;
        }

        if (TimeUtil.timeMinus(flight.getDepTime(), flight.getActDepTime()) < -30) {

            return false;
        }


        //按照1200KM/S，即20KM/s来算
        int actFlyTime = Integer.parseInt(flight.getActFlyingTime());
        int distKm = Integer.parseInt(flight.getDistKm());
        if (actFlyTime == 0) {
            return false;
        }
        return distKm / actFlyTime <= 20;

    }



    public static void main(String argsp[]) {
        Flight flight = new Flight();
        //	flight.setComment("已排班");
        flight.setDepAirport("SHA");
        System.out.println(validData(flight));


    }

}
