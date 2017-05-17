package models;

import java.util.ArrayList;
import java.util.List;

public class SimCard {

  private static List<SimCard> simCards;

  static {
    simCards = new ArrayList<SimCard>();
    simCards.add(new SimCard("1234567", "test", "Foo"));
    simCards.add(new SimCard("2234567", "test2", "Bar"));
  }

  public String ean;
  public String name;
  public String description;

  public SimCard() {}

  public SimCard(String ean, String name, String description) {
    this.ean = ean;
    this.name = name;
    this.description = description;
  }

  public String toString() {
    return String.format("%s/%s", ean, name);
  }

  public static List<SimCard> findAll() {
    return new ArrayList<>(simCards);
  }

  public static SimCard findByEan(String ean) {
    for (SimCard candidate : simCards) {
      if (candidate.ean.equals(ean)) {
        return candidate;
      }
    }
    return null;
  }

  public static List<SimCard> findByName(String term) {
    final List<SimCard> results = new ArrayList<>();
    for (SimCard candidate : simCards) {
      if (candidate.name.toLowerCase().contains(term.toLowerCase())) {
        results.add(candidate);
      }
    }
    return results;
  }

  public static boolean remove(SimCard simCard) {
    return simCards.remove(simCard);
  }

  public void save() {
    simCards.remove(findByEan(this.ean));
    simCards.add(this);
  }
}