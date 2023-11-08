package entity;

import java.util.Date;


public class Profile {
    private String name;
    private String email;
    private Date birthday;
    private Gender gender;
    private Address address;
    private String username;
    private String cpf;
    private Transactions[] transactions;
    private String password;
    private long phoneNumber;
    private Preferences preferences = new Preferences();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Transactions[] getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions[] transactions) {
        this.transactions = transactions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(long number) {
        this.phoneNumber = number;
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
