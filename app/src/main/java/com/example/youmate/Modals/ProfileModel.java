package com.example.youmate.Modals;

public class ProfileModel {

    private String username;
    private String age;
    private String email;
    private String phoneno;
    private String paymentid;
    private String address;
    private String accountno;
    private String accountholdername;
    private String bankname;
    private String url;
    private int level;
    private int point;

    public ProfileModel( String username, String age, String email, String phoneno, String paymentid,String url,String address, String accountno,String accountholdername,String bankname, int level, int point ) {
        this.username = username;
        this.age = age;
        this.email = email;
        this.phoneno = phoneno;
        this.paymentid = paymentid;
        this.url=url;
        this.address = address;
        this.accountno = accountno;
        this.accountholdername = accountholdername;
        this.bankname = bankname;
        this.level = level;
        this.point = point;
    }

    public ProfileModel() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge( String age ) {
        this.age = age;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno( String phoneno ) {
        this.phoneno = phoneno;
    }

    public String getPaymentid() {
        return paymentid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress( String address ) {
        this.address = address;
    }

    public String getAccountno() {
        return accountno;
    }

    public void setAccountno( String accountno ) {
        this.accountno = accountno;
    }

    public String getAccountholdername() {
        return accountholdername;
    }

    public void setAccountholdername( String accountholdername ) {
        this.accountholdername = accountholdername;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname( String bankname ) {
        this.bankname = bankname;
    }

    public void setPaymentid( String paymentid ) {
        this.paymentid = paymentid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl( String url ) {
        this.url = url;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }
}
