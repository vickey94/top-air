package air.model;

import java.util.List;

import air.util.TimeUtil;
import air.util.WeatherUtil;
import air.config.Modulus;


/**
 * Created by vickey on 2016/7/20.
 * 预测数据，归一化的数据
 */
public class PredictData {

    private double month;
    private double week;
    private double day;
    private double flightTime;        //计划飞行时间
    private double depTime;           //计划出发时间
    private double arrTime;           //计划到达时间

    private double airType;           //机型
    private double distKm;            //飞行距离
    private double depAirport;        //出发机场
    private double arrAirport;        //到达机场

    private double depTemphi;         //出发机场最高温度
    private double depTemplow;        //出发机场最低温度
    private double depDesc;
    private double depWindir;
    private double depWindstrength;   //出发机场风力

    private double arrTemphi;         //到达机场最高温度
    private double arrTemplow;        //到达机场最低温度
    private double arrDesc;
    private double arrWindir;
    private double arrWindstrength;   //到达机场风力

    private double delays;            //离港延误


    private List<?> AIRTYPE_List;
    private List<?> AIRPORT_List;


    public PredictData() {
        this.AIRPORT_List = AIRPORT_List;
        this.AIRTYPE_List = AIRTYPE_List;
    }

    //加载机场机型List
    public PredictData(List<?> AIRTYPE_List, List<?> AIRPORT_List) {
        this.AIRPORT_List = AIRPORT_List;
        this.AIRTYPE_List = AIRTYPE_List;
    }

    /**
     * 将数据归一化，作为可以训练的数据
     * <p>
     * TimeUtil、WeatherUtil
     *
     * @return 返回flight , dep_weather, arr_weather 可以用于生成csv文件
     **/
    public void SetData(Flight flight, Weather dep_weather, Weather arr_weather) {


        this.month = TimeUtil.getMonthByDate(flight.getTimeSeries()) * 0.08;
        this.week = TimeUtil.getWeekByDate(flight.getTimeSeries()) * 0.14;
        this.day = TimeUtil.getWeekByDate(flight.getTimeSeries()) * 0.03;
        this.depTime = TimeUtil.getMinByHHSS(flight.getDepTime()) * 6.9e-4;
        this.arrTime = TimeUtil.getMinByHHSS(flight.getArrTime()) * 6.9e-4;

        /***以下系数可能需要根据实际情况更改***/
        this.airType = Double.parseDouble(AIRTYPE_List.indexOf(flight.getAcft()) + 1 + "") * Modulus.AIRTYPE; //避免从0开始，所以第一个+1
        this.distKm = Double.parseDouble(flight.getDistKm()) * Modulus.DISTKM;   //0.0003由最远距离决定
        this.flightTime = Double.parseDouble(flight.getFlyingTime()) * 0.00285;
        this.depAirport = Double.parseDouble(AIRPORT_List.indexOf(flight.getDepAirport()) + 1 + "") * Modulus.AIRPORT;
        this.arrAirport = Double.parseDouble(AIRPORT_List.indexOf(flight.getArrAirport()) + 1 + "") * Modulus.AIRPORT;

        //出发机场天气
        this.depTemphi = Double.parseDouble(dep_weather.getTemphi()) * Modulus.TEMPHI;
        this.depTemplow = Double.parseDouble(dep_weather.getTemplow()) * Modulus.TEMPLOW;  //数据为 -1 到 1 之间
        this.depDesc = WeatherUtil.getWeaDesc(dep_weather.getDescription());  //获取的数据已经介于 0-1之间
        this.depWindir = WeatherUtil.getWeaWindir(dep_weather.getWindir());
        this.depWindstrength = WeatherUtil.getWindStrength(dep_weather.getWindstrength());

        //到达机场天气
        this.arrTemphi = Double.parseDouble(arr_weather.getTemphi()) * Modulus.TEMPHI;
        this.arrTemplow = Double.parseDouble(arr_weather.getTemplow()) * Modulus.TEMPLOW;
        this.arrDesc = WeatherUtil.getWeaDesc(arr_weather.getDescription());
        this.arrWindir = WeatherUtil.getWeaWindir(arr_weather.getWindir());
        this.arrWindstrength = WeatherUtil.getWindStrength(arr_weather.getWindstrength());

        //离港延误 小于-30的已经在ValidData中排除，如果delays>120则算作120
        this.delays = TimeUtil.timeMinus(flight.getDepTime(), flight.getActDepTime());  //-30到120之间
        if (this.delays > 120.0) {
            delays = 120.0;
        }


    }


    public double getDelays() {
        return delays;
    }

    public void setDelays(double delays) {
        this.delays = delays;
    }


    /**
     * 归一化的数据
     **/
    @Override
    public String toString() {
        return month + "," + week + "," + day + "," + flightTime + "," + depTime + "," + arrTime + ","
                + airType + "," + distKm + "," + depAirport + "," + arrAirport + ","
                + depTemphi + "," + depTemplow + "," + depDesc + "," + depWindir + "," + depWindstrength + ","
                + arrTemphi + "," + arrTemplow + "," + arrDesc + "," + arrWindir + "," + arrWindstrength + ","
                + delays;
    }

    public double getMonth() {
        return month;
    }

    public void setMonth(double month) {
        this.month = month;
    }

    public double getWeek() {
        return week;
    }

    public void setWeek(double week) {
        this.week = week;
    }

    public double getFlightTime() {
        return flightTime;
    }

    public void setFlightTime(double flightTime) {
        this.flightTime = flightTime;
    }

    public double getDepTime() {
        return depTime;
    }

    public void setDepTime(double depTime) {
        this.depTime = depTime;
    }

    public double getArrTime() {
        return arrTime;
    }

    public void setArrTime(double arrTime) {
        this.arrTime = arrTime;
    }

    public double getAirType() {
        return airType;
    }

    public void setAirType(double airType) {
        this.airType = airType;
    }

    public double getDistKm() {
        return distKm;
    }

    public void setDistKm(double distKm) {
        this.distKm = distKm;
    }

    public double getDepAirport() {
        return depAirport;
    }

    public void setDepAirport(double depAirport) {
        this.depAirport = depAirport;
    }

    public double getArrAirport() {
        return arrAirport;
    }

    public void setArrAirport(double arrAirport) {
        this.arrAirport = arrAirport;
    }

    public double getDepTemphi() {
        return depTemphi;
    }

    public void setDepTemphi(double depTemphi) {
        this.depTemphi = depTemphi;
    }

    public double getDepTemplow() {
        return depTemplow;
    }

    public void setDepTemplow(double depTemplow) {
        this.depTemplow = depTemplow;
    }

    public double getDepDesc() {
        return depDesc;
    }

    public void setDepDesc(double depDesc) {
        this.depDesc = depDesc;
    }

    public double getDepWindir() {
        return depWindir;
    }

    public void setDepWindir(double depWindir) {
        this.depWindir = depWindir;
    }

    public double getDepWindstrength() {
        return depWindstrength;
    }

    public void setDepWindstrength(double depWindstrength) {
        this.depWindstrength = depWindstrength;
    }

    public double getArrTemphi() {
        return arrTemphi;
    }

    public void setArrTemphi(double arrTemphi) {
        this.arrTemphi = arrTemphi;
    }

    public double getArrTemplow() {
        return arrTemplow;
    }

    public void setArrTemplow(double arrTemplow) {
        this.arrTemplow = arrTemplow;
    }

    public double getArrDesc() {
        return arrDesc;
    }

    public void setArrDesc(double arrDesc) {
        this.arrDesc = arrDesc;
    }

    public double getArrWindir() {
        return arrWindir;
    }

    public void setArrWindir(double arrWindir) {
        this.arrWindir = arrWindir;
    }

    public double getArrWindstrength() {
        return arrWindstrength;
    }

    public void setArrWindstrength(double arrWindstrength) {
        this.arrWindstrength = arrWindstrength;
    }

    public double getDay() {
        return day;
    }

    public void setDay(double day) {
        this.day = day;
    }

}
