package controllers;

import models.Assessment;
import models.Member;

public class GymUtility {

  /*  Returns the BMI for the member based on the calculation
      BMI is weight divided by the square of the height */
  public static double calculateBMI(Member member, Assessment assessment) {
    float weight = assessment.getWeight();
    float height = member.getHeight();
    double squareOfHeight = Math.pow(height, 2);
    double bmi = weight / squareOfHeight;
    bmi = (int) (bmi * 100) / 100.0;
    return bmi;
  }

  /* Determine the BMI Category
     BMI less than 16(exclusive) is "SEVERELY UNDERWEIGHT"
     BMI Between 16(inclusive) and 18.5(exclusive) is "UNDERWEIGHT"
     BMI Between 18.5(inclusive) and 25(exclusive) is "NORMAL"
     BMI Between 25(inclusive) and 30(exclusive) is "OVERWEIGHT"
     BMI Between 30(inclusive) and 35(exclusive) is "MODERATELY OBESE"
     BMI greater than 35(inclusive) is "SEVERELY OBESE"
   */
  public static String determineBMICategory(float bmiValue) {
    if (bmiValue < 16) {
      return "SEVERELY UNDERWEIGHT";
    } else if (bmiValue >= 16 && bmiValue < 18.5) {
      return "UNDERWEIGHT";
    } else if (bmiValue >= 18.5 && bmiValue < 25) {
      return "NORMAL";
    } else if (bmiValue >= 25 && bmiValue < 30) {
      return "OVERWEIGHT";
    } else if (bmiValue >= 30 && bmiValue < 35) {
      return "MODERATELY OBESE";
    } else if (bmiValue >= 35) {
      return "SEVERELY OBESE";
    } else {
      return "UNKNOWN";
    }
  }

//  Returns a boolean to indicate if the member has and ideal body weight based on the Devine Formula
  public static boolean isIdealBodyWeight(Member member, Assessment assessment) {

    String gender = member.getGender();
    // weight of the member in kgs.
    float weight = assessment.getWeight();
    // heigth of the member in metres
    float height = member.getHeight();

    // convert metres to inches and round it to the nearest int
    double heightInches =(height * 39.370);
    //five foot is equal to 60 inches less a boundry or .2ft(2.4 inches)
    double fiveFoot = 60;
    double inchesAboveFiveFoot;
    double weightForEachInchOverFiveFoot;
    double idealWeight;


//    if the height is greater than 5 ft (with a .2ft boundry (2.4inches)
    if (heightInches >= fiveFoot - 2.4) {

      inchesAboveFiveFoot = heightInches - fiveFoot;
      weightForEachInchOverFiveFoot = inchesAboveFiveFoot * 2.3;

//   if the member is male an ideal body weight is 50kg plus 2.3kg for every inch over 5ft

      if (gender.equalsIgnoreCase("m")) {
        idealWeight = 50 + weightForEachInchOverFiveFoot;
      }
      // otherwise the member is female/unspecified allow an ideal body weight of 45.5kg plus 2.3kg for every inch over 5ft
      else {
        idealWeight = 45.5 + weightForEachInchOverFiveFoot;
      }
//   if the members weight is 2kg either side of the ideal weight return true otherwise return false
      if (weight >= idealWeight - 2 && weight <= idealWeight + 2) {
        return true;
      } else {
        return false;
      }
    }

//    If the member is 5ft or less return true if male is 50kg or less or female is 45.5kg or less otherwise its false
    else {

      if (gender.equalsIgnoreCase("m")) {
        if (weight <= 50 && weight >= 48) {
          return true;
        }
      } else if (weight <= 45.5 && weight >= 43.5) {
        return true;
      }
      return false;
    }
  }
}
