package com.ecfront.easybi.restful.test.example;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Project: EasyBI_Framework
 * Person: 震宇
 * Date: 13-9-8
 */
public class Person {

    private String idcard;
    private String name;
    private Date birthday;
    private int age;
    private BigDecimal income;
    private List<Person> parents;

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    public List<Person> getParents() {
        return parents;
    }

    public void setParents(List<Person> parents) {
        this.parents = parents;
    }

    @Override
    public String toString() {
        return "Person{" +
                "idcard='" + idcard + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", age=" + age +
                ", income=" + income +
                ", parents=" + parents +
                '}';
    }
}
