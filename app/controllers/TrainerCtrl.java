package controllers;

import models.Assessment;
import models.Member;
import models.Trainer;
import play.Logger;
import play.mvc.Controller;

import java.util.List;

/*
 * Methods for the trainer dashboard
 */
public class TrainerCtrl extends Controller {

  //  method to get the members details using the members id
  public static void getMemberDetails(Long id) {
    Logger.info("Trainer gets members details");
    Assessment latestAssessment;
    Member member = Member.findById(id);
    System.out.println("MEMBER" + member.firstname);
    List<Assessment> assessmentlist = member.sortedList();

    /* if the member has no assessments we need to create a temporary assessment with the members start weight
     in order to pass it to the GymUtility methods */
    if (assessmentlist.size() <= 0) {
      latestAssessment = new Assessment(member.startweight, 0, 0, 0, 0, 0);
    }
//    otherwise we will be passing the latest assessment to the GymUtility methods
    else {
      latestAssessment = assessmentlist.get(0);
    }


    boolean idealWeight = GymUtility.isIdealBodyWeight(member, latestAssessment);
    float bmi = (float) GymUtility.calculateBMI(member,latestAssessment);
    String bmiCategory = GymUtility.determineBMICategory(bmi);

    render("trainerviewmemberdetails.html", member, assessmentlist, bmi, idealWeight, bmiCategory);
  }

  //  method to add a comment to a members assessment
  public static void addComment(Long memberid, Long id, String comment) {
    Logger.info("Trainer adds a comment: " + comment + ", to assessment id: " + id);
    Assessment assessment = Assessment.findById(id);
    Logger.info("id: " + id);
    assessment.setComment(comment);
    assessment.save();
    getMemberDetails(memberid);
  }

  /*  method to delete a member from the system. Each member has zero to many assessments
    these all need to be deleted too so they don't clog up the database */
  public static void deleteMember(Long id) {

    Logger.info("Deleting Member: " + id);

    Trainer trainer = Accounts.getLoggedInTrainer();
    Member member = Member.findById(id);

    List<Assessment> assessmentlist = member.assessmentlist;
    for (int i = assessmentlist.size() - 1; i >= 0; i--) {
      Logger.info("Deleting assessment : " + assessmentlist.get(i).id);
      Assessment assessment = assessmentlist.get(i);
      member.assessmentlist.remove(assessment);
      member.save();
      assessment.delete();
    }
    trainer.memberlist.remove(member);
    trainer.save();
    member.delete();
    redirect("/dashboard");
  }
}