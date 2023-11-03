package entity;

public enum Gender {
    MALE("Masculino"),
    FEMALE("Feminino"),
    NON_BINARY("Não-binário"),
    OTHER("Outro");

    private final String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
