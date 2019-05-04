package models;

import play.Logger;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/*
 * Member is a subclass of Person - a Member is a person who is the member of the gym. A member has an email address,
 * password, firstname, lastname, address, gender and they also have a height and starting weight.
 * You can find a member by their email address.
 * Members also have assessments. These assessments hold information about the members measurements and weights at
 * specific dates.
 * */
@Entity
public class Member extends Person {
  //    height is in metres
  public float height;
  //    weight is in kgs
  public float startweight;

  //    A member can have one to many assessments these assessments are stored in an array list
  @OneToMany(cascade = CascadeType.ALL)
  public List<Assessment> assessmentlist = new ArrayList<Assessment>();

  //  Constructor method for a Member
  public Member(String email, String password, String firstname, String lastname, String address,
                String gender, float height, float startweight) {
    super(email, password, firstname, lastname, address, gender);
    this.height = height;
    this.startweight = startweight;
  }

  //    find the member by their email address
  public static Member findByEmail(String email) {
    return find("email", email).first();
  }

  // check if the password matches
  public boolean checkPassword(String password) {
    return this.password.equals(password);
  }

  /*  Sort the list of assessments using Comparator to compare each assessment and sort them by date in reverse order.
  I found the following site helpful when trying to complete this method.
 https://howtodoinjava.com/sort/sort-arraylist-objects-comparable-comparator/
 */
  public List<Assessment> sortedList() {
    this.assessmentlist.sort(Comparator.comparing(Assessment::getDate).reversed());
    return assessmentlist;
  }

  //  get the members latest weight
  public double latestWeight() {
    Logger.info("Get latest weight");
    double latestWeight = startweight;
    List<Assessment> assessments = sortedList();
    if (assessments.size() > 0) {
      Assessment latestAssessment = assessments.get(0);
      latestWeight = latestAssessment.getWeight();
    }
    double weight = (int) (latestWeight * 100) / 100.0;
    return weight;
  }

  public float getHeight() {
    return height;
  }

  // toString method for Member class
  public String toString() {

    return super.toString() + "Height: " + height + " , Start Weight: " + startweight;
  }
}

