
public class User {
    private final String username;
    private final String password;
    private int chips;

    public User(String username, String password, int chips) {
        this.username = username;
        this.password = Utility.getHash(password);
        this.chips = chips;
    }

    public String getUsername() {
        return username;
    }

    public boolean authenticate(String password) {
        return this.password.equals(password);
    }

    public int getChips() {
        return chips;
    }

    public void addChips(int chips) {
        this.chips += chips;
    }

    public void removeChips(int chips) {
        this.chips -= chips;
    }
}
