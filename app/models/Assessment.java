package models;

import controllers.GymUtility;
import play.db.jpa.Model;

import javax.persistence.Entity;
import java.util.Date;
import java.util.List;

/* Assessment model
 * - Members can add assessments to their account to keep track of their progress, each assessment
 * has a date, current weight, chest, thigh, upperarm, waist, and hip measurements.
 * - Trainers can add a comment to a members assessment*/
@Entity
public class Assessment extends Model {
  public Date date;
  public float weight;
  public float chest;
  public float thigh;
  public float upperarm;
  public float waist;
  public float hip;
  public String comment;
  public boolean trend;

  public Assessment(float weight, float chest, float thigh, float upperarm, float waist,
                    float hip) {
    setDate();
    setWeight(weight);
    setChest(chest);
    setThigh(thigh);
    setUpperarm(upperarm);
    setWaist(waist);
    setHip(hip);
  }

  //  retrieve the date of the assessment
  public Date getDate() {
    return date;
  }

  //  set the date the assessment was created on
  public void setDate() {
    this.date = new Date();
  }

  //  get the members weight for this assessment
  public float getWeight() {
    return weight;
  }

  //  set the members current weight
  public void setWeight(float weight) {
    this.weight = weight;
  }

  // set the chest measurements for this assessment
  public void setChest(float chest) {
    this.chest = chest;
  }

  //  set the thigh measurements for this assessment
  public void setThigh(float thigh) {
    this.thigh = thigh;
  }

  // set the upper arm measurements for this assessment
  public void setUpperarm(float upperarm) {
    this.upperarm = upperarm;
  }

  // set the waist measuements for his assessment
  public void setWaist(float waist) {
    this.waist = waist;
  }

  // set the hip measurements for this assessment
  public void setHip(float hip) {
    this.hip = hip;
  }

  //  set the comment for this assessment
  public void setComment(String comment) {
    this.comment = comment;
  }

  //  set the trend of this assessment
  public void setTrend(boolean trend) {
    this.trend = trend;
  }
}
