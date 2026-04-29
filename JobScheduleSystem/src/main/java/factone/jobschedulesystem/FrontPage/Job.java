package factone.jobschedulesystem.FrontPage;

import java.time.LocalDate;

public class Job {
    private int ID;
    private String Customer_Name;
    private String Customer_Mail;
    private String Phone;
    private String VehicalNo;
    private String Times;
    private LocalDate Date;

    public Job(int ID, String customer_Name, String customer_Mail, String phone, String vehicalNo, String times, LocalDate date) {
        this.ID = ID;
        this.Customer_Name = customer_Name;
        this.Customer_Mail = customer_Mail;
        this.Phone = phone;
        this.VehicalNo = vehicalNo;
        this.Times = times;
        this.Date = date;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getCustomer_Name() {
        return Customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        Customer_Name = customer_Name;
    }

    public String getCustomer_Mail() {
        return Customer_Mail;
    }

    public void setCustomer_Mail(String customer_Mail) {
        Customer_Mail = customer_Mail;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getVehicalNo() {
        return VehicalNo;
    }

    public void setVehicalNo(String vehicalNo) {
        VehicalNo = vehicalNo;
    }

    public String getTimes() {
        return Times;
    }

    public void setTimes(String times) {
        Times = times;
    }

    public LocalDate getDate() {
        return Date;
    }
}


