/**
 * Curso: Elementos de Sistemas
 * Arquivo: Parser.java
 */

package assembler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Encapsula o código de leitura. Carrega as instruções na linguagem assembly,
 * analisa, e oferece acesso as partes da instrução  (campos e símbolos).
 * Além disso, remove todos os espaços em branco e comentários.
 */
public class Parser {

    private final BufferedReader fileReader;
    private final BufferedReader fileNop;
    public String inputFile;		        // arquivo de leitura
    public int lineNumber = 0;		     	// linha atual do arquivo (nao do codigo gerado)
    public String currentCommand = "";      // comando atual
    public String currentLine;			    // linha de codigo atual


    /** Enumerator para os tipos de comandos do Assembler. */
    public enum CommandType {
        A_COMMAND,      // comandos LEA, que armazenam no registrador A
        C_COMMAND,      // comandos de calculos
        L_COMMAND       // comandos de Label (símbolos)
        
    }

    public Parser(String file) throws IOException {
        this.inputFile = file;
        this.fileReader = new BufferedReader(new FileReader(file));
        this.fileNop = new BufferedReader(new FileReader(file));
        this.lineNumber = 0;
        // Ao inicializar o parser, ele ira colocar automaticamento os nops faltando!
        handleNop();
    }


    public void handleNop() throws IOException {
        String commandLine;
        boolean nopEsperado = false;
        int linha = 0;
        StringBuilder espaco = new StringBuilder();
        List<String> novoFile = new ArrayList<>();

        while ((commandLine = fileNop.readLine()) != null) {
            linha++;
            String noSpace = commandLine.replaceAll("\\s+", "");
            List<String> tokens = new ArrayList<>(Arrays.asList(noSpace.split(",")));

            if (nopEsperado){
                // Se o ultimo comando for um jmp e o commando atual nao for um nop
                if(!tokens.contains("nop")){
                    System.out.printf("Esperado nop na linha %d%n", linha);
                    System.out.println("Colocando automaticamente!");
                    commandLine = espaco.toString() + "nop" + "\n" + commandLine;
                }
                nopEsperado = false;
            }
            espaco = new StringBuilder();
            if (noSpace.length() > 0){
                // Um nop e esperado quando o commando anterior começar com j -> apenas as operações de jump.
                nopEsperado = Objects.equals(noSpace.charAt(0), 'j');
                // Conta os espacos para que o nop seja colocado na identacao correta no nasm
                for (int i = 0; i < commandLine.length(); i++){
                    if (commandLine.charAt(i) == ' '){
                        espaco.append(" ");
                    } else{
                        break;
                    }
                }
            }
            // Adiciona a linha a lista de geração do novo file.
            novoFile.add(commandLine);
        }
        // Para caso haja um jmp na ultima linha, coloca um Nop apos
        if (nopEsperado){
            System.out.println("Nop esperado na ultima linha!");
            System.out.println("Colocando automaticamente!");
            String ultimo = espaco.toString() + "nop";
            novoFile.add(ultimo);
        }
        fileNop.close();
        // Começa a escrita do novo file com os nops adicionados
        PrintWriter fileWriterNop = new PrintWriter(new FileWriter(inputFile));
        for (int i = 0; i < novoFile.size(); i++){
            // Logica para nao adicionar um espaco em branco a mais no fim do arquivo
            if (i < novoFile.size() - 1){
                fileWriterNop.write(novoFile.get(i) + System.lineSeparator());
            } else {
                fileWriterNop.write(novoFile.get(i));
            }
        }
        // Fecha o writer e termina o método.
        fileWriterNop.close();
    }

    // fecha o arquivo de leitura
    public void close() throws IOException {
        fileReader.close();
    }

    /**
     * Carrega uma instrução e avança seu apontador interno para o próxima
     * linha do arquivo de entrada. Caso não haja mais linhas no arquivo de
     * entrada o método retorna "Falso", senão retorna "Verdadeiro".
     * @return Verdadeiro se ainda há instruções, Falso se as instruções terminaram.
     */
    public Boolean advance() {
        /* ja esta pronto */
        while(true){
            try {
                currentLine = fileReader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            lineNumber++;
            if (currentLine == null)
                return false;  // caso não haja mais comandos
            currentCommand = currentLine.replaceAll(";.*$", "").trim();
            if (currentCommand.equals(""))
                continue;
            return true;   // caso um comando seja encontrado
        }
    }

    /**
     * Retorna o comando "intrução" atual (sem o avanço)
     * @return a instrução atual para ser analilisada
     */
    public String command() {
        /* ja esta pronto */
        return currentCommand;
    }

    /**
     * Retorna o tipo da instrução passada no argumento:
     *  A_COMMAND para leaw, por exemplo leaw $1,%A
     *  L_COMMAND para labels, por exemplo Xyz: , onde Xyz é um símbolo.
     *  C_COMMAND para todos os outros comandos
     * @param  command instrução a ser analisada.
     * @return o tipo da instrução.
     */
    public CommandType commandType(String command) {
    
        if(command.startsWith("leaw")){
            return CommandType.A_COMMAND;
        }
        else if(command.endsWith(":")){
            return CommandType.L_COMMAND;
        }
        else {
            return CommandType.C_COMMAND;
        }
    
    }

    /**
     * Retorna o símbolo ou valor numérico da instrução passada no argumento.
     * Deve ser chamado somente quando commandType() é A_COMMAND.
     * @param  command instrução a ser analisada.
     * @return somente o símbolo ou o valor número da instrução.
     */
    public String symbol(String command) {
        /* TODO: implementar */
        String[] symbol = command.split(" ");
        String[] symbol2 = symbol[1].split(",");
    	return symbol2[0].replace("$","");
    }

    /**
     * Retorna o símbolo da instrução passada no argumento.
     * Deve ser chamado somente quando commandType() é L_COMMAND.
     * @param  command instrução a ser analisada.
     * @return o símbolo da instrução (sem os dois pontos).
     */
    public String label(String command) {
        /* TODO: implementar */

    	return  command.replace(":","");
    }

    /**
     * Separa os mnemônicos da instrução fornecida em tokens em um vetor de Strings.
     * Deve ser chamado somente quando CommandType () é C_COMMAND.
     * @param  command instrução a ser analisada.
     * @return um vetor de string contento os tokens da instrução (as partes do comando).
     */
    public String[] instruction(String command) {
        /* TODO: implementar */
        String[] symbol = command.replace(","," ").split(" ");
    	return symbol;
    }


}
