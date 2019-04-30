package controllers;

import models.Assessment;
import models.Member;
import models.Trainer;
import play.Logger;
import play.mvc.Controller;

// controller for registering and logging in of members to the gym
public class Accounts extends Controller {

  //  render the signup/registration html page
  public static void signup() {
    render("signup.html");
  }

  // render the login html page
  public static void login() {
    render("login.html");
  }

  // Create a new member with their details received from the registration form and redirect to the members dashboard
  public static void registerMember(String firstname, String lastname, String email,
                                    String password, String gender, String address, float height, float startweight) {

    Member member = new Member(email, password, firstname, lastname, address, gender, height, startweight);
    member.save();
    Logger.info("Authentication successful");
    session.put("logged_in_Memberid", member.id);
    redirect("/dashboard");
  }

  // Create a new trainer with their details received from the registration form and redirect to the trainers dashboard
  public static void registerTrainer(String firstname, String lastname, String email,
                                     String password, String gender, String address) {

    Trainer trainer = new Trainer(email, password, firstname, lastname, address, gender);
    trainer.save();
    Logger.info("Authentication successful");
    session.put("logged_in_Trainerid", trainer.id);
    redirect("/dashboard");
  }

  //  authenticate the member checking their email is associated to a member or a trainer and the password is a match
  public static void authenticate(String email, String password) {
    Logger.info("Attempting to authenticate with " + email + ":" + password);

    Member member = Member.findByEmail(email);
    Trainer trainer = Trainer.findByEmail(email);
    if ((member != null) && (member.checkPassword(password) == true)) {
      Logger.info("Authentication successful");
      session.put("logged_in_Memberid", member.id);
      redirect("/dashboard");
    } else if ((trainer != null) && (trainer.checkPassword(password) == true)) {
      Logger.info("Authentication successful");
      session.put("logged_in_Trainerid", trainer.id);
      redirect("/dashboard");
    } else {
      Logger.info("Authentication failed");
      redirect("/login");
    }
  }

  //  logout clear the session and return to the home page
  public static void logout() {
    Logger.info("Logout ");
    session.clear();
    redirect("/");
  }

  // get the member that is logged in using the session
  public static Member getLoggedInMember() {

    Logger.info("Get logged in member");
    Member member = null;
    if (session.contains("logged_in_Memberid")) {
      String memberId = session.get("logged_in_Memberid");
      member = Member.findById(Long.parseLong(memberId));
    } else {
      login();
    }
    return member;
  }

  //  get the trainer that is logged in using the session
  public static Trainer getLoggedInTrainer() {
    Logger.info("Get logged in Trainer");
    Trainer trainer = null;
    if (session.contains("logged_in_Trainerid")) {
      String trainerId = session.get("logged_in_Trainerid");
      trainer = Trainer.findById(Long.parseLong(trainerId));
    } else {
      login();
    }
    return trainer;
  }

  // if there is a member or trainer logged in render the appropriate profile page otherwise direct back to home page
  public static void profile() {
    Logger.info("Rendering User Profile");

    if (session.contains("logged_in_Memberid")) {
      Member member = getLoggedInMember();
      render("memberprofile.html", member);
    } else if (session.contains("logged_in_Trainerid")) {
      Trainer trainer = getLoggedInTrainer();
      render("trainerprofile.html", trainer);
    } else {
      redirect("/");
    }
  }

  //  updates a members profile details using the user input on the form on the profile page
  public static void updateMemberProfile(String firstname, String lastname, String email, String password, String address,
                                         String gender, float height, float startweight) {
    Logger.info("Update Member Profile ");
    Member member = getLoggedInMember();

    if (!firstname.equals("")) {
      member.firstname = firstname;
    }
    if (!lastname.equals("")) {
      member.lastname = lastname;
    }
    if (!email.equals("")) {
      member.email = email;
    }
    if (!password.equals("")) {
      member.password = password;
    }

    member.gender = gender;

    if (!address.equals("")) {
      member.address = address;
    }
    if (height != 0) {
      member.height = height;
    }
    if (startweight != 0) {
      member.startweight = startweight;
    }
    member.save();
    redirect("/dashboard");
  }

  //  updates a trainers profile details using the user input on the form on the profile page
  public static void updateTrainerProfile(String firstname, String lastname, String email, String password,
                                          String address, String gender) {
    Logger.info("Update Trainers Profile ");
    Trainer trainer = getLoggedInTrainer();

    if (!firstname.trim().equals("")) {
      trainer.firstname = firstname;
    }
    if (!lastname.trim().equals("")) {
      trainer.lastname = lastname;
    }
    if (!email.trim().equals("")) {
      trainer.email = email;
    }
    if (!password.trim().equals("")) {
      trainer.password = password;
    }
    trainer.gender = gender;

    if (!address.trim().equals("")) {
      trainer.address = address;
    }

    trainer.save();
    redirect("/dashboard");
  }
}
