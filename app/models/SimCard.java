package models;

import java.util.ArrayList;
import java.util.List;
import play.Logger;
import play.data.validation.Constraints;

public class SimCard {

  private static List<SimCard> simCards;

  static {
    simCards = new ArrayList<SimCard>();
    simCards.add(new SimCard("1234567", "111111111", "Steffen"));
    simCards.add(new SimCard("2234567", "222222222", "Max"));
  }

  @Constraints.Required
  public String imsi;
  @Constraints.Required
  public String msisdn;
  public String owner;


  public SimCard() {}

  public SimCard(String imsi, String msisdn, String owner) {
    this.imsi = imsi;
    this.msisdn = msisdn;
    this.owner = owner;
  }

  public String getMsisdn() {
    return msisdn;
  }

  public String getImsi() {
    return imsi;
  }

  public String getOwner() {
    return owner;
  }

  public void setImsi(String imsi) {
    this.imsi = imsi;
  }

  public void setMsisdn(String msisdn) {
    this.msisdn = msisdn;
  }

  public void setOwner(String owner) {
    this.owner = owner;
  }

  public String toString() {
    return String.format("[imsi:%s, msisdn:%s, owner:%s]", imsi, msisdn, owner);
  }

  public static List<SimCard> findAll() {
    return new ArrayList<>(simCards);
  }

  public static SimCard findByImsi(String imsi) {
    for (SimCard candidate : simCards) {
      if (candidate.imsi.equals(imsi)) {
        return candidate;
      }
    }
    return null;
  }

  public static List<SimCard> findByName(String name) {
    final List<SimCard> results = new ArrayList<>();
    for (SimCard candidate : simCards) {
      if (candidate.owner.toLowerCase().contains(name.toLowerCase())) {
        results.add(candidate);
      }
    }
    return results;
  }

  public static boolean remove(SimCard simCard) {
    return simCards.remove(simCard);
  }

  public void save() {
    SimCard simcard = findByImsi(this.imsi);
    if (simcard != null) {
      Logger.info("Removing {}", this);
      simCards.remove(simcard.imsi);
    }
    Logger.info("Adding {}", this);
    simCards.add(this);
  }
}