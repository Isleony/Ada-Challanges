import java.io.*; // Importa classes necessárias para manipulação de arquivos e entrada/saída
import java.util.*; // Importa classes necessárias para manipulação de coleções, como Map
import java.nio.file.*; // Importa classes necessárias para manipulação de arquivos e caminhos 
import java.security.SecureRandom;// Importa a classe SecureRandom para gerar números aleatórios seguros



public class PasswordManager { // Gerenciador de Senhas para Redes Sociais
    // Este programa permite que o usuário gere e armazene senhas fortes para diferentes redes sociais.
    // As senhas são armazenadas em um arquivo local e podem ser recuperadas ou atualizadas conforme necessário.
    private static final String PASSWORDS_FILE = ".passwords"; // Arquivo onde as senhas serão armazenadas
    // O arquivo é criado no diretório atual do usuário.
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+"; // Conjunto de caracteres permitidos para a senha
    // Inclui letras maiúsculas, minúsculas, números e símbolos especiais.
    private static final int PASSWORD_LENGTH = 16; // Comprimento da senha gerada
    // A senha gerada terá 16 caracteres de comprimento.
    private static final SecureRandom random = new SecureRandom();// Gerador de números aleatórios seguro para criar senhas fortes  

    public static void main(String[] args) {
        if (args.length == 0) { // Verifica se o usuário forneceu o nome da rede social
            System.out.println("Por favor, forneça o nome da rede social.");
            System.out.println("Uso: java PasswordManager <nome-da-rede-social>"); // Instruções de uso
            System.out.println("Exemplo: java PasswordManager Facebook");   
            return;
        }

        String socialNetwork = args[0]; // Nome da rede social fornecido pelo usuário
        // O nome da rede social é usado como chave para armazenar a senha correspondente.
        Map<String, String> passwords = loadPasswords(); // Carrega as senhas armazenadas do arquivo    
        
        if (passwords.containsKey(socialNetwork)) { // Verifica se já existe uma senha para a rede social fornecida
            // Se a senha já existir, ela é exibida para o usuário.
            System.out.println("Senha para " + socialNetwork + ": " + passwords.get(socialNetwork)); // Exibe a senha existente
            // O usuário pode optar por gerar uma nova senha ou manter a existente.
        } else {
            String newPassword = generateStrongPassword(); // Gera uma nova senha forte para a rede social
            // A senha é gerada usando um gerador de números aleatórios seguro.
            passwords.put(socialNetwork, newPassword); // Armazena a nova senha no mapa de senhas
            // O mapa de senhas é atualizado com a nova senha para a rede social fornecida
            savePasswords(passwords); // Salva o mapa de senhas atualizado no arquivo
            // As senhas são salvas no arquivo especificado, permitindo que sejam recuperadas posteriormente
            System.out.println("Nova senha gerada para " + socialNetwork + ": " + newPassword); //  Exibe a nova senha gerada
            // O usuário é informado sobre a nova senha gerada para a rede social.
        }
    }

    private static Map<String, String> loadPasswords() { // Carrega as senhas armazenadas do arquivo
        // Este método lê o arquivo de senhas e retorna um mapa contendo as senhas armazenadas. 
        Map<String, String> passwords = new HashMap<>(); // Mapa para armazenar as senhas, onde a chave é o nome da rede social e o valor é a senha correspondente
        
        try { // Tenta ler o arquivo de senhas
            // Se o arquivo não existir, ele será criado automaticamente.
            if (!Files.exists(Paths.get(PASSWORDS_FILE))) { // Verifica se o arquivo de senhas já existe
                // Se o arquivo não existir, ele será criado.
                Files.createFile(Paths.get(PASSWORDS_FILE));// Cria o arquivo de senhas se ele não existir
                // O arquivo será criado no diretório atual do usuário.
                return passwords; // Retorna um mapa vazio se o arquivo não existir
                // Isso significa que ainda não há senhas armazenadas.
            }

            List<String> lines = Files.readAllLines(Paths.get(PASSWORDS_FILE)); // Lê todas as linhas do arquivo de senhas
            // Cada linha deve estar no formato "redeSocial:senha", onde "redeSocial"
            for (String line : lines) { // Itera sobre cada linha do arquivo
                // é o nome da rede social e "senha" é a senha correspondente.
                String[] parts = line.split(":", 2); // Divide a linha em duas partes: a rede social e a senha
                // O limite de divisão é 2 para garantir que apenas a primeira ocorrência de ":" seja usada, permitindo que a senha contenha ":" se necessário.
                // Isso é útil para evitar problemas se a senha contiver o caractere ":".
                if (parts.length == 2) { // Verifica se a linha foi dividida corretamente em duas partes
                    // Se a linha contiver exatamente duas partes, a primeira parte é a rede social e
                    passwords.put(parts[0].trim(), parts[1].trim()); // A segunda parte é a senha correspondente.
                    // As partes são adicionadas ao mapa de senhas, removendo espaços em branco desnecessários.
                }
            }
        } catch (IOException e) { // Trata exceções de entrada/saída ao ler o arquivo
            // Se ocorrer um erro ao ler o arquivo, uma mensagem de erro é exibida.
            System.err.println("Erro ao ler o arquivo de senhas: " + e.getMessage()); // Exibe uma mensagem de erro se houver problemas ao ler o arquivo
            // Isso pode ocorrer se o arquivo estiver corrompido ou se houver problemas de perm
        }
        
        return passwords; // Retorna o mapa de senhas carregado do arquivo
        // Se o arquivo foi lido com sucesso, o mapa conterá as senhas armazenadas.
        // Se o arquivo estiver vazio ou não contiver senhas, o mapa será vazio.    
    }

    private static void savePasswords(Map<String, String> passwords) { // Salva as senhas no arquivo
        // Este método grava o mapa de senhas no arquivo especificado, substituindo o conteúdo
        try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(Paths.get(PASSWORDS_FILE)))) { // Usa PrintWriter para escrever no arquivo de senhas
            // O arquivo é aberto para escrita, e se já existir, seu conteúdo será substituído
            for (Map.Entry<String, String> entry : passwords.entrySet()) { // Itera sobre cada entrada no mapa de senhas
                // Cada entrada contém uma rede social e sua senha correspondente.
                writer.println(entry.getKey() + ":" + entry.getValue()); // Escreve a rede social e a senha no formato "redeSocial:senha"
                // Cada linha no arquivo terá o formato "redeSocial:senha", onde "redeSocial
            }
        } catch (IOException e) { // Trata exceções de entrada/saída ao salvar o arquivo
            // Se ocorrer um erro ao salvar o arquivo, uma mensagem de erro é exibida.
            System.err.println("Erro ao salvar o arquivo de senhas: " + e.getMessage()); // Exibe uma mensagem de erro se houver problemas ao salvar o arquivo
            // Isso pode ocorrer se houver problemas de permissão ou se o disco estiver cheio.  
        }
    }

    private static String generateStrongPassword() { // Gera uma senha forte aleatória
        // Este método cria uma senha aleatória de 16 caracteres usando o conjunto de caracteres permitido
        StringBuilder password = new StringBuilder(PASSWORD_LENGTH); // StringBuilder é usado para construir a senha de forma eficiente
        // O StringBuilder é inicializado com o comprimento da senha desejada.
        for (int i = 0; i < PASSWORD_LENGTH; i++) {  // Itera para gerar cada caractere da senha
            // A senha será composta por 16 caracteres aleatórios.
            password.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length()))); // Seleciona um caractere aleatório do conjunto de caracteres permitido
        }
        return password.toString(); // Retorna a senha gerada como uma String
        // A senha gerada é uma String contendo 16 caracteres aleatórios, garantindo uma boa
    }
}