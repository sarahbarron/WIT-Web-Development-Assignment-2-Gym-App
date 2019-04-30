package models;

import play.db.jpa.Model;

import javax.persistence.Entity;

/*
A Person - Is a super class for member and trainer. both member and trainer are registered with a Gym. The super Person
class contains the persons firstname, lastname, email, password, address and gender.
 */
@Entity
public class Person extends Model {
  public String firstname;
  public String lastname;
  public String email;
  public String password;
  public String address;
  public String gender;

  //constructor method for a person
  public Person(String email, String password, String firstname, String lastname, String address, String gender) {

    this.firstname = firstname;
    this.lastname = lastname;
    this.email = email;
    this.password = password;
    this.address = address;
    this.gender = gender;
  }

  //    sets the email
  public void setEmail(String email) {
    this.email = email;
  }

  //    sets the password
  public void setPassword(String password) {
    this.password = password;
  }

  // checks the password
  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  // sets the first name
  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  //   get the email address
  public String getEmail() {
    return email;
  }

  //   get the persons gender
  public String getGender() {
    return gender;
  }


  // toString method for Person class
  public String toString() {
    return "Name: " + firstname + " " + lastname + ", Address: " + address + ", Email: " + email + ", Gender: " + gender + " , ";
  }
}
