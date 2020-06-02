package com.huqi.admin.entry;

public class Address {

    private Integer id;
    private String name;
    private String phone;
    private String detail;
    private Integer state;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", detail='" + detail + '\'' +
                ", state=" + state +
                '}';
    }

    public Address(Integer id, String name, String phone, String detail, Integer state) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.detail = detail;
        this.state = state;
    }

    public Address() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
