package controllers;

import models.Assessment;
import models.Member;
import models.Person;
import models.Trainer;
import play.Logger;
import play.mvc.Controller;

import java.util.List;

// Controller class with methods associated with the dashboard page
public class Dashboard extends Controller {

  //  when a member logs in or registers they are redirected to the member dashboard or the trainer dasboard
  public static void index() {
//    If the person logged in is a member render the members dashboard
    if (session.contains("logged_in_Memberid")) {

      Assessment latestAssessment;
      String memberId = session.get("logged_in_Memberid");
      Member member = Member.findById(Long.parseLong(memberId));
      List<Assessment> assessmentlist = member.assessmentlist;
      assessmentlist = member.sortedList();

      if (assessmentlist.size() <= 0) {
        latestAssessment = new Assessment(member.startweight, 0, 0, 0, 0, 0);
      } else {
        latestAssessment = assessmentlist.get(0);
      }

      double bmi = GymUtility.calculateBMI(member, latestAssessment);
      boolean idealWeight = GymUtility.isIdealBodyWeight(member, latestAssessment);
      String bmiCategory = GymUtility.determineBMICategory(member, latestAssessment);

      render("memberdashboard.html", member, assessmentlist, bmi, idealWeight, bmiCategory);
    }
//    otherwise if the person logged in is a trainer render the trainers dashboard
    else if (session.contains("logged_in_Trainerid")) {
      String trainerId = session.get("logged_in_Trainerid");
      Trainer trainer = Trainer.findById(Long.parseLong(trainerId));
      List<Member> memberlist = trainer.memberlist;
      int numofassessments = memberlist.size();
      render("trainerdashboard.html", trainer, memberlist, numofassessments);
    }
//    otherwise if no-one is logged in render the login page
    else {
      System.out.println("LOGIN");
      redirect("/login");
    }
  }

  //  Add a new assessment using the inputted values from the assessment form and save it to the members assessments
  public static void addAssessment(float weight, float chest, float thigh, float upperarm, float waist, float hip) {
    Member member = Accounts.getLoggedInMember();
    Assessment assessment = new Assessment(weight, chest, thigh, upperarm, waist, hip);
    member.assessmentlist.add(assessment);
    member.save();
    resetTrends(member);
    member.save();
    Logger.info("Adding assessment ");
    redirect("/dashboard");
  }

  //  Delete an assessment get the assessment id and remove it from the members list of assessments and then delete it
  public static void deleteAssessment(Long id) {
    Logger.info("Deleting assessment: " + id);
    Member member = Accounts.getLoggedInMember();
    Assessment assessment = Assessment.findById(id);
    member.assessmentlist.remove(assessment);
    member.save();
    assessment.delete();
    resetTrends(member);
    member.save();
    redirect("/dashboard");
  }

  /* Reset Trends is called any time an assessment is added or deleted. It compares each assessments weight with the
  previous assessment and sets the value to true if it is a positive loss or gain and sets it to false if it is a negative
  loss or gain.
  */
  private static void resetTrends(Member member) {
    List<Assessment> assessments = member.sortedList();
    Assessment previousAssessment;
    float previousWeight;
    float assessmentWeight;
    float weightDifference;

//    iterate through every assessment and compare to the previous assessment
    for (int i = 0; i < assessments.size(); i++) {

//      get the current assessment and its assessment weight
      Assessment thisAssessment = assessments.get(i);
      assessmentWeight = thisAssessment.getWeight();

//      if this is the last assessment on the list, the previous weight is the members start weight
      if (i == assessments.size() - 1) {
        previousWeight = member.startweight;
      }
//      otherwise get the next assessment in the list (which is the previous assessment) and get that assessments weight
      else {
        previousAssessment = assessments.get(i + 1);
        previousWeight = previousAssessment.getWeight();
      }
// subtract the previous weight from the current assessment weight
      weightDifference = assessmentWeight - previousWeight;
//      get the bmi category the member was is at the time of the assessment
      String bmiCategory = GymUtility.determineBMICategory(member, thisAssessment);

//    If the person was not underweight a loss/no-change was good so set to true
      if (weightDifference <= 0 && !bmiCategory.contains("UNDERWEIGHT")) {
        thisAssessment.setTrend(true);
      }
//    If the person was not underweight a gain was bad so set to false
      else if (weightDifference > 0 && !bmiCategory.contains("UNDERWEIGHT")) {
        thisAssessment.setTrend(false);
      }
//      If the person was under weight a loss was bad so set to false
      else if (weightDifference < 0 && bmiCategory.contains("UNDERWEIGHT")) {
        thisAssessment.setTrend(false);
      }
//      If the person was under weight a gain/no change was good so set to true
      else if (weightDifference >= 0 && bmiCategory.contains("UNDERWEIGHT")) {
        thisAssessment.setTrend(true);
      }
    }
  }
}

