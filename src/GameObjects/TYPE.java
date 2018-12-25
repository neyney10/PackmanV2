package GameObjects;

public enum TYPE {P(25),F(24),G(23),B(22),M(21);

    private int value;
    private TYPE(int value) {this.value = value;}
    public int getValue() {
        return value;
    }
}
