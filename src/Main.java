import entity.Gender;
import entity.Preferences;
import entity.Profile;
import services.PasswordStorage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Main {
    public static void main(String[] args) throws ParseException {
        HashMap<String, Profile> profiles = new HashMap<>();

        Profile profile1 = new Profile();

        // Preencher os campos
        profile1.setName("João da Silva");
        profile1.setEmail("joao.silva@email.com");

        // Definir a data de nascimento (exemplo)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date birthday = dateFormat.parse("15/05/1990");
        profile1.setBirthday(birthday);

        profile1.setGender(Gender.MALE);

        profile1.setUsername("joaosilva123");
        profile1.setCpf("123.456.789-01");

        profile1.setPassword("senha123");
        profile1.setPhoneNumber(555123456789L);

        profile1.getPreferences().setBirthdayOption(true);
        profile1.getPreferences().setFullNameOption(true);
        profile1.getPreferences().setGenderOption(true);

        profiles.put(profile1.getEmail(), profile1);

        while(true) {
            int option = menu("Criar Perfil", "Editar perfil", "Listar Perfis", "Excluir Perfil");

            switch (option) {
                case 0:
                    System.out.println("Saindo...");
                    return;
                case 1:
                    createProfile(profiles);
                    break;
                case 2:
                    editProfile(profiles);
                    break;
                case 3:
                    listProfiles(profiles);
                    break;
                case 4:
                    deleteProfile(profiles);
                    break;
            }

            System.out.println();
        }
    }

    public static int menu(String... options) {
        Scanner scanner = new Scanner(System.in);
        int option;

        do {
            System.out.println("[0] Sair");
            for (int i = 0; i < options.length; i++) {
                System.out.println("[" + (i + 1) + "] " + options[i]);
            }

            System.out.println("Digite uma opção:");
            option = scanner.nextInt();

        } while (0 < option && option > options.length);
        System.out.println();

        return option;
    }

    private static void deleteProfile(HashMap<String, Profile> profiles) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o email do perfil a ser excluido: ");
        String emailToEdit = scanner.nextLine();

        if (profiles.containsKey(emailToEdit)) {
            Profile profile = profiles.get(emailToEdit);

            System.out.print("Digite a senha: ");
            String password = scanner.nextLine();

            if (PasswordStorage.checkPassword(password)) {
                System.out.print("Confirme com o CPF: ");
                String cpf = scanner.nextLine();

                if (Objects.equals(cpf, profile.getCpf())) {
                    profiles.remove(profile.getEmail());
                } else {
                    System.out.println("CPF inválido");
                }
            } else {
                System.out.println("Senha incorreta");
            }
        } else {
            System.out.println("Perfil não encontrado");
        }
    }

    private static void editProfile(HashMap<String, Profile> profiles) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o email do perfil a ser editado: ");
        String emailToEdit = scanner.nextLine();

        if (profiles.containsKey(emailToEdit)) {
            Profile profile = profiles.get(emailToEdit);

            System.out.print("Digite a senha: ");
            String password = scanner.nextLine();

            if (PasswordStorage.checkPassword(password)) {
                StringBuilder details = profileDetails(profile, true);
                String[] lines = details.toString().split("\n");

                for (int i = 0; i < lines.length; i++) {
                    String line = lines[i];
                    int lineNumber = i + 1; // Número da linha

                    // Adicione o número da linha à frente de cada linha
                    lines[i] = lineNumber + ". " + line;
                }

                System.out.println(String.join("\n", lines));

                System.out.print("Escolha o número da linha que deseja editar: ");
                int lineNumber = scanner.nextInt();
                scanner.nextLine();

                if (lineNumber >= 1 && lineNumber <= 12) {
                    String fieldName = lines[lineNumber - 1].split("-")[0].replaceFirst("^[0-9]+\\.", "").strip();
                    // Repetir enquanto o resultado for inválido
                    do {
                        System.out.print("Digite o novo " + fieldName + ": ");
                    } while (!setProfileField(profile, lineNumber - 1, scanner.nextLine()));
                } else {
                    System.out.println("Linha incorreta");
                }
            } else {
                System.out.println("Senha incorreta");
            }
        } else {
            System.out.println("Perfil não encontrado");
        }
    }

    private static void listProfiles(HashMap<String, Profile> profiles) {
        for (Profile profile : profiles.values()) {
            System.out.println(profileDetails(profile, false));
            System.out.println();
        }
    }

    private static StringBuilder profileDetails(Profile profile, boolean expanded) {
        StringBuilder details = new StringBuilder();

        if (profile.getPreferences().showFullName() || expanded) {
            details.append("Nome - ").append(profile.getName());
        } else {
            details.append("Nome - ").append(profile.getName().strip(), 0, profile.getName().length() / 2);
        }

        if (expanded) {
            details.append("\nEmail - ").append(profile.getEmail());

            details.append("\nSenha - ").append("*".repeat(profile.getPassword().length()));
        }

        if (profile.getPreferences().showBirthday() || expanded) {
            details.append("\nData de Nascimento - ").append(new SimpleDateFormat("dd/MM/yyyy").format(profile.getBirthday()));
        }

        if (expanded) {
            details.append("\nCPF - ").append(profile.getCpf());

            details.append("\nEndereço - ").append(profile.getAddress());
        }

        details.append("\nApelido - ").append(profile.getUsername());

        if (profile.getPreferences().showGender() || expanded) {
            details.append("\nGênero - ").append(profile.getGender().getName());
        }

        if (expanded) {
            details.append("\nTelefone - ").append(profile.getPhoneNumber());
            details.append("\nMostrar aniversário (S/N) - ").append(profile.getPreferences().showBirthday());
            details.append("\nMostrar nome completo (S/N) - ").append(profile.getPreferences().showFullName());
            details.append("\nMostrar gênero (S/N) - ").append(profile.getPreferences().showGender());
        }

        return details;
    }

    private static void createProfile(HashMap<String, Profile> profiles) {
        Scanner scanner = new Scanner(System.in);
        Profile profile = new Profile();
        // Usar lista por serem elementos fixos
        List<String> inputFields = Arrays.asList(
                "Nome", "Email", "Senha",
                "Data de Nascimento (dd/mm/aaaa)", "CPF", "Endereço",
                "Apelido", "Gênero (Masculino, Feminino, Não-Binário, Outro)", "Número de Telefone",
                "Mostrar aniversário (S/N)", "Mostrar nome completo (S/N)", "Mostrar gênero (S/N)"
        );
        // Sistema de paginação começando da página 1
        int currentStep = 1;
        int totalSteps = inputFields.size() / 3;

        while (currentStep <= totalSteps) {
            System.out.println("Etapa " + currentStep + "/" + totalSteps);
            // Mostrar 3 inputs por página
            for (int i = 0; i < 3; i++) {
                // Subtrai 1 para coincidir com o índicie da lista, multiplica por 3
                // pois são 3 elementos por página
                // por fim, soma i para iterar cada elemento no grupo de 3 elementos
                int currentField = (currentStep - 1) * 3 + i;

                if (currentField >= inputFields.size()) break;

                String fieldName = inputFields.get(currentField);
                // Repetir enquanto o resultado for inválido
                do {
                    System.out.print(fieldName + ": ");
                } while (!setProfileField(profile, currentField, scanner.nextLine()));
            }

            currentStep++;
            System.out.println();
        }

        profiles.put(profile.getEmail(), profile);
        System.out.println("Perfil criado com sucesso.");
    }

    private static boolean setProfileField(Profile profile, int fieldIndex, String input) {
        switch (fieldIndex) {
            case 0:
                Pattern namePattern = Pattern.compile("^(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*(?: (?:(?:e|y|de(?:(?: la| las| lo| los))?|do|dos|da|das|del|van|von|bin|le) )?(?:(?:(?:d'|D'|O'|Mc|Mac|al\\-))?(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+|(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+(?:\\-(?:[\\p{Lu}&&[\\p{IsLatin}]])(?:(?:')?(?:[\\p{Ll}&&[\\p{IsLatin}]]))+)*))+(?: (?:Jr\\.|II|III|IV))?$");

                if (namePattern.matcher(input).matches()) {
                    profile.setName(input);
                } else {
                    System.out.println("Nome inválido");
                    return false;
                }

                break;
            case 1:
                Pattern emailPattern = Pattern.compile("^((?!\\.)[\\w\\-_.]*[^.])(@\\w+)(\\.\\w+(\\.\\w+)?[^.\\W])$");

                if (emailPattern.matcher(input).matches()) {
                    profile.setEmail(input);
                } else {
                    System.out.println("Email inválido");
                    return false;
                }
                break;
            case 2:
                Pattern strongPattern = Pattern.compile("^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*\\W).{8,}$");

                if (strongPattern.matcher(input).matches()) {
                    profile.setPassword(PasswordStorage.hashPassword(input));
                } else {
                    System.out.println("""
                    Senha fraca! Use pleo menos 8 dígitos, dos quais: ao menos um dígito, ao menos uma letra minúscula 
                    e maiúscula e ao menos um caractere especial
                    """);
                    return false;
                }
                break;
            case 3:
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                    Date date = dateFormat.parse(input);

                    profile.setBirthday(date);
                } catch (ParseException e) {
                    System.out.println("Data de Nascimento inválida. Use o formato dd/mm/aaaa.");
                    return false;
                }
                break;
            case 4:
                Pattern cpfPattern = Pattern.compile("([0-9]{2}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[\\/]?[0-9]{4}[-]?[0-9]{2})|([0-9]{3}[\\.]?[0-9]{3}[\\.]?[0-9]{3}[-]?[0-9]{2})");

                if (cpfPattern.matcher(input).matches()) {
                    profile.setCpf(input);
                } else {
                    System.out.println("CPF Inválido");
                    return false;
                }
                break;
            case 5:
                break;
            case 6:
                Pattern username = Pattern.compile("^(?=.{3,20}$)(?![_.-])(?!.*[_.-]{2})[a-zA-Z0-9_-]+([^._-])$");

                if (username.matcher(input).matches()) {
                    profile.setUsername(input);
                } else {
                    System.out.println("Apelido inválido");
                    return false;
                }
                break;
            case 7:
                String genderInput = input.toLowerCase();

                Gender selectedGender = switch (genderInput) {
                    case "masculino" -> Gender.MALE;
                    case "feminino" -> Gender.FEMALE;
                    case "não-binário" -> Gender.NON_BINARY;
                    default -> Gender.OTHER;
                };

                profile.setGender(selectedGender);
                break;
            case 8:
                Pattern pattern = Pattern.compile("(?:([+]\\d{1,4})[-.\\s]?)?(?:[(](\\d{1,3})[)][-.\\s]?)?(\\d{1,4})[-.\\s]?(\\d{1,4})[-.\\s]?(\\d{1,9})");

                if (pattern.matcher(input).matches()) {
                    profile.setPhoneNumber(Long.parseLong(input));
                } else {
                    System.out.println("Número de telefone inválido");
                    return false;
                }
                break;
            case 9:
                profile.getPreferences().setBirthdayOption(input.equalsIgnoreCase("s"));
                break;
            case 10:
                profile.getPreferences().setFullNameOption(input.equalsIgnoreCase("s"));
                break;
            case 11:
                profile.getPreferences().setGenderOption(input.equalsIgnoreCase("s"));
                break;
        }

        return true;
    }
}