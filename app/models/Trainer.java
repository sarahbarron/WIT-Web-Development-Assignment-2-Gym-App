package models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

/*
The Trainer is a subclass of Person. The Trainer is an employee of the Gym.
The Trainer can have one to many members. The trainer can view members progress and add comments to a members
assessment.
 */
@Entity
public class Trainer extends Person {

  //  Trainer can have one to many members, these members are stored in an array list
  @OneToMany(cascade = CascadeType.ALL)
  public List<Member> memberlist = new ArrayList<Member>();

  //  Constructore method of a Trainer
  public Trainer(String email, String password, String firstName, String lastName, String address, String gender) {
    super(email, password, firstName, lastName, address, gender);
  }

  //  find the Trainer by email
  public static Trainer findByEmail(String email) {
    return find("email", email).first();
  }

  // toString method for Trainer class
  public String toString() {

    return super.toString();
  }
}
