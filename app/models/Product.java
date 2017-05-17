package models;

import java.util.ArrayList;
import java.util.List;

public class Product {

  private static List<Product> products;

  static {
    products = new ArrayList<Product>();
    products.add(new Product("1234567", "test", "Foo"));
    products.add(new Product("2234567", "test2", "Bar"));
  }

  public String ean;
  public String name;
  public String description;

  public Product() {}

  public Product(String ean, String name, String description) {
    this.ean = ean;
    this.name = name;
    this.description = description;
  }

  public String toString() {
    return String.format("%s/%s", ean, name);
  }

  public static List<Product> findAll() {
    return new ArrayList<>(products);
  }

  public static Product findByEan(String ean) {
    for (Product candidate : products) {
      if (candidate.ean.equals(ean)) {
        return candidate;
      }
    }
    return null;
  }

  public static List<Product> findByName(String term) {
    final List<Product> results = new ArrayList<>();
    for (Product candidate : products) {
      if (candidate.name.toLowerCase().contains(term.toLowerCase())) {
        results.add(candidate);
      }
    }
    return results;
  }

  public static boolean remove(Product product) {
    return products.remove(product);
  }

  public void save() {
    products.remove(findByEan(this.ean));
    products.add(this);
  }
}