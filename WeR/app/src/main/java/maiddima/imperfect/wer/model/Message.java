package maiddima.imperfect.wer.model;

public class Message {

    private int student_id;
    private double gps_lat;
    private double gps_long;
    private String student_message;
    private int message_id;

    public Message(int student_id, double gps_lat, double gps_long, String student_message, int message_id) {
        this.student_id = student_id;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.student_message = student_message;
        this.message_id = message_id;
    }

    public Message(int student_id, double gps_lat, double gps_long, String student_message) {
        this.student_id = student_id;
        this.gps_lat = gps_lat;
        this.gps_long = gps_long;
        this.student_message = student_message;
    }



    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public double getGps_lat() {
        return gps_lat;
    }

    public void setGps_lat(float gps_lat) {
        this.gps_lat = gps_lat;
    }

    public double getGps_long() {
        return gps_long;
    }

    public void setGps_long(float gps_long) {
        this.gps_long = gps_long;
    }

    public String getStudent_message() {
        return student_message;
    }

    public void setStudent_message(String student_message) {
        this.student_message = student_message;
    }

    public int getMessageId() {
        return message_id;
    }


}
