package network;

//A simple class that allows us for advanced address handling

public class Address {
    private String address;
    private String nickName;

    public Address(String address) {
        this.address = address;
    }

    public boolean equals(Object o) {
        return o instanceof Address && address.equals(o.toString());
    }

    public String toString() {
        return address;
    }
}
