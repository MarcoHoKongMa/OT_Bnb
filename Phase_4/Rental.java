package Phase_4;

public class Rental {
    private String id;
    private String owner;
    private String city;
    private int num_of_bedrooms;
    private float price;
    private boolean rented;
    private int nights_remain;

    public Rental(String id, String owner, String city, int num_of_bedrooms, float price, Boolean rented, int nights_remain) {
        this.id = id; this.owner = owner; this.city = city; this.num_of_bedrooms = num_of_bedrooms; this.price = price; this.rented = rented; this.nights_remain = nights_remain;
    }

    public String getId() { return id; }

    public String getOwner() { return owner; }

    public String getCity() { return city; }

    public int getNumOfBedrooms() { return num_of_bedrooms; }

    public float getPrice() { return price; }

    public boolean getRented() { return rented; }

    public int getNightRemain() { return nights_remain; }
}
