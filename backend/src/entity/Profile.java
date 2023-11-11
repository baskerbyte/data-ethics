package entity;

import java.lang.RuntimeException;
import services.PasswordStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;


public class Profile {
    private String name;
    private String email;
    private Date birthday;
    private Gender gender;
    private String address;
    private String username;
    private String cpf;
    private Transactions[] transactions;
    private String password;
    private long phoneNumber;
    private final Preferences preferences = new Preferences();

    public String getName() {
        return name;
    }

    public void setName(String name) throws RuntimeException {
        Pattern namePattern = Pattern.compile("^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$");

        if (namePattern.matcher(name).matches()) {
            this.name = name;
        } else {
            throw new RuntimeException("O nome não respeita as convenções de um nome próprio.");
        }
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        Pattern emailPattern = Pattern.compile("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");

        if (emailPattern.matcher(email).matches()) {
            this.email = email;
        } else {
            throw new RuntimeException("Email inválido, utilize o formato example@example.com");
        }
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) throws RuntimeException {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            this.birthday = dateFormat.parse(birthday);
        } catch (ParseException e) {
            throw new RuntimeException("Data de Nascimento inválida. Use o formato dd/mm/aaaa.");
        }
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(String gender) {
        String genderInput = gender.toLowerCase();

        this.gender = switch (genderInput) {
            case "masculino" -> Gender.MALE;
            case "feminino" -> Gender.FEMALE;
            case "não-binário" -> Gender.NON_BINARY;
            default -> Gender.OTHER;
        };
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) throws RuntimeException {
        Pattern addressPattern = Pattern.compile("^[a-zA-Z0-9\\s]*$");

        if (addressPattern.matcher(address).matches()) {
            this.address = address;
        } else {
            throw new RuntimeException("Endereço inválido, utilize somente letras e números");
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) throws RuntimeException {
        Pattern usernamePattern = Pattern.compile("^(?=.{3,20}$)(?![_.-])(?!.*[_.-]{2})[a-zA-Z0-9_-]+([^._-])$");

        if (usernamePattern.matcher(username).matches()) {
            this.username = username.toLowerCase();
        } else {
            throw new RuntimeException("Apelido inválido, utilize somente letras, números ou _");
        }
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) throws RuntimeException {
        Pattern cpfPattern = Pattern.compile("([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})");

        if (cpfPattern.matcher(cpf).matches()) {
            this.cpf = cpf.replaceAll("[^0-9]", "");
        } else {
            throw new RuntimeException("CPF inválido, utilize o formato 999.999.999-99");
        }
    }

    public Transactions[] getTransactions() {
        return transactions;
    }

    public void setTransactions(Transactions[] transactions) {
        this.transactions = transactions;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) throws RuntimeException {
        Pattern strongPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$");

        if (strongPattern.matcher(password).matches()) {
            this.password = PasswordStorage.hashPassword(password);
        } else {
            throw new RuntimeException("""
                    Senha fraca! Use pelo menos 8 dígitos, dos quais: ao menos uma letra minúscula 
                    e maiúscula e ao menos um caractere especial
                    """);
        }
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String number) throws RuntimeException {
        Pattern pattern = Pattern.compile("(?:([+]\\d{1,4})[-.\\s]?)?(?:[(](\\d{1,3})[)][-.\\s]?)?(\\d{1,4})[-.\\s]?(\\d{1,4})[-.\\s]?(\\d{1,9})");

        if (pattern.matcher(number).matches()) {
            number = number.replaceAll("[^0-9]", "");
            this.phoneNumber = Long.parseLong(number);
        } else {
            throw new RuntimeException("Número de telefone inválido, utilzie o formato +99 9 9999-9999");
        }
    }

    public Preferences getPreferences() {
        return preferences;
    }
}
