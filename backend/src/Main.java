import entity.Gender;
import entity.Profile;
import services.PasswordStorage;

import java.text.SimpleDateFormat;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        HashMap<String, Profile> profiles = new HashMap<>();

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
            // Enumerar opções começando do 1
            for (int i = 0; i < options.length; i++) {
                System.out.println("[" + (i + 1) + "] " + options[i]);
            }

            System.out.println("Digite uma opção:");
            try {
                option = scanner.nextInt();
            } catch (Exception e) {
                option = -1;
            }
            
        // Repetir enquanto for uma opção inválida (abaixo de 0 ou maior que a quantidade atual)
        } while (0 < option && option > options.length);
        System.out.println();

        return option;
    }

    private static void deleteProfile(HashMap<String, Profile> profiles) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o email do perfil a ser excluido: ");
        String emailToEdit = scanner.nextLine();

        if (!profiles.containsKey(emailToEdit)) {
            System.out.println("Perfil não encontrado");

            return;
        }

        Profile profile = profiles.get(emailToEdit);

        System.out.print("Digite a senha: ");
        String password = scanner.nextLine();

        if (!PasswordStorage.checkPassword(password)) {
            System.out.println("Senha incorreta");
            return;
        }

        System.out.print("Confirme com o CPF: ");
        String cpf = scanner.nextLine();

        if (!Objects.equals(cpf, profile.getCpf())) {
            System.out.println("CPF inválido!");
            return;
        }

        profiles.remove(profile.getEmail());
        System.out.print("Perfil deletado!");
    }

    private static void editProfile(HashMap<String, Profile> profiles) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Digite o email do perfil a ser editado: ");
        String emailToEdit = scanner.nextLine();

        if (!profiles.containsKey(emailToEdit)) {
            System.out.println("Perfil não encontrado");
            return;
        }

        System.out.print("Digite a senha: ");
        String password = scanner.nextLine();

        if (!PasswordStorage.checkPassword(password)) {
            System.out.println("Senha incorreta");
            return;
        }

        while (true) {
            // Atualizar mensagem do perfil enquanto as alterações são feitas
            Profile profile = profiles.get(emailToEdit);
            StringBuilder details = profileDetails(profile, true);

            String[] lines = details.toString().split("\n");
            int lineNumber = menu(lines);

            if (lineNumber == 0) {
                System.out.println("Saindo...");
                return;
            }
            // Pegar a linha correspondente, como por exemplo "Nome - XXX",
            // para fazer a pergunta de inserir novo field
            String fieldName = lines[lineNumber - 1].split("-")[0].replaceFirst("^[0-9]+\\.", "").strip();
            // Repetir enquanto o resultado for inválido
            do {
                System.out.print("Digite o novo " + fieldName.toLowerCase() + ": ");
            } while (setProfileField(profiles.get(emailToEdit), lineNumber - 1, scanner.nextLine(), profiles));

            System.out.print("Alteração salva!");
            System.out.println();
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
            details.append("Nome completo - ").append(profile.getName());
        } else {
            details.append("Nome completo - ").append(profile.getName().strip(), 0, profile.getName().length() / 2);
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
                "Nome completo", "Email", "Senha",
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
                } while (setProfileField(profile, currentField, scanner.nextLine(), profiles));
            }

            currentStep++;
            System.out.println();
        }

        profiles.put(profile.getEmail(), profile);
        System.out.println("Perfil criado com sucesso.");
    }

    private static boolean setProfileField(Profile profile, int fieldIndex, String input, HashMap<String, Profile> profiles) {
        Runnable[] setters = {
                () -> profile.setName(input),
                () -> {
                    if (profiles.containsKey(input))
                        throw new RuntimeException("Email já registrado!");

                    profile.setEmail(input);
                },
                () -> {
                    if (profile.getPassword() != null && Objects.equals(input, profile.getPassword()))
                        throw new RuntimeException("Senha atual identificada!");

                    Scanner scanner = new Scanner(System.in);
                    System.out.print("Confirme a sua senha: ");
                    String confirmation = scanner.nextLine();

                    if (!Objects.equals(input, confirmation))
                        throw new RuntimeException("Senhas não coincidem!");

                    profile.setPassword(input);
                },
                () -> profile.setBirthday(input),
                () -> profile.setCpf(input),
                () -> profile.setAddress(input),
                () -> profile.setUsername(input),
                () -> profile.setGender(input),
                () -> profile.setPhoneNumber(input),
                () -> profile.getPreferences().setBirthdayOption(input.equalsIgnoreCase("s")),
                () -> profile.getPreferences().setFullNameOption(input.equalsIgnoreCase("s")),
                () -> profile.getPreferences().setGenderOption(input.equalsIgnoreCase("s"))
        };

        try {
            setters[fieldIndex].run();
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return true;
        }

        return false;
    }
}