/**
 * Curso: Elementos de Sistemas
 * Arquivo: Code.java
 * Created by Luciano Soares <lpsoares@insper.edu.br>
 * Date: 2/05/2017
 * Adaptado por Rafael Corsi <rafael.corsi@insper.edu.br>
 * Date: 5/2018
 */

package vmtranslator;

import java.util.*;
import java.io.*;
import java.nio.file.*;

/**
 * Traduz da linguagem vm para códigos assembly.
 */
public class Code {

    PrintWriter outputFile = null;  // arquivo .nasm de saída
    String filename = null;         // arquivo .vm de entrada
    int lineCode = 0;               // Linha do codigo vm que gerou as instrucoes
    int cont = 0;
    /**
     * Abre o arquivo de saida e prepara para escrever
     * @param filename nome do arquivo NASM que receberá o código traduzido.
     */
    public Code(String filename) throws FileNotFoundException,IOException {
        File file = new File(filename);
        this.outputFile = new PrintWriter(new FileWriter(file));
    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para executar o comando aritmético.
     * @param  command comando aritmético a ser analisado.
     */
    public void writeArithmetic(String command) {

        if ( command.equals("")) {
            Error.error("Instrução invalida");
        }

        List<String> commands = new ArrayList<String>();

        if(command.equals("add")) {
            commands.add(String.format("; %d - ADD", lineCode++));

            // Le o SP e seleciona os 2 valores anteriores no REG D e o da posição de memoria (%A)
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");

            // Efetua a soma entre REG D e da posição da (%A)
            commands.add("addw (%A), %D, %D");
            commands.add("movw %D, (%A)");

            // Aumenta 1 e subscreve no endereço em que o SP esta apontando
            commands.add("addw $1, %A, %D");
            commands.add("leaw $0, %A");
            commands.add("movw %D, (%A)");

        } else if (command.equals("sub")) {
            commands.add(String.format("; %d - SUB", lineCode++));
            // IMPLEMENTAR AQUI O LAB
            // LEMBRAR DE USAR A FUNÇÃO commands.add()!

            // Le o SP, e carrega os 2 valores anteriores em REG A e REG D
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");

            // Efetua a subtração entre os Registradores
            commands.add("subw (%A), %D, %D");
            commands.add("movw %D, (%A)");
            commands.add("addw $1, %A, %D");

            //Subscresve no SP o valor da subtração
            commands.add("leaw $0, %A");
            commands.add("movw %D, (%A)");

        } else if (command.equals("neg")) {
            commands.add(String.format("; %d - NEG", lineCode++));
            
            // Le o SP e carrega o valor anterior a ele salvando no REG D
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %D");
            commands.add("decw %D");
            commands.add("movw %D,%A");

            // Nega o valor interno da ram e salva no REG D
            commands.add("negw (%A)");
            commands.add("movw (%A), %D");

            // Subscreve esse valor na posição em que o SP apontava
            commands.add("movw %D, (%A)");



        } else if (command.equals("eq")) {
            commands.add(String.format("; %d - EQ", lineCode++));
            // Le a posição do SP e carrega os dois valores anteriores em REG A e REG D
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");
            commands.add("movw (%A), %A");

            // Efetua a subtração entre os Registradores e salva em D
            commands.add("subw %A, %D, %D");

            // Se a subtração entra REG A e REG D = 0, entao ele desce pro loop TRUE e retorna true no SP (If D = 0 return True) 
            commands.add("leaw $TRUE, %A");
            commands.add("je %D"); // compara
            commands.add("nop");
            commands.add("leaw $0, %A");
            commands.add("movw %A, %D");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw %D, (%A)");

            // else: return False
            commands.add("leaw $END, (%A)");
            commands.add("jmp");
            commands.add("nop");

            // IF x = y: return True
            commands.add("TRUE:");
            commands.add("leaw $0, %A");
            commands.add("subw %A, $1, %D");
            commands.add("movw (%A),%A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw %D, (%A)");

            // Else: return False
            commands.add("END:");
            commands.add("leaw $0, %A");
            commands.add("movw (%A),%D");
            commands.add("decw %D");
            commands.add("movw %D, (%A)");


        } else if (command.equals("gt")) {
            commands.add(String.format("; %d - GT", lineCode++));

            // Le a posição do SP e carrega os dois valores anteriores em REG A e REG D            
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");
            commands.add("movw (%A), %A");

            // Efetua a subtração entre os Registradores e salva em D
            commands.add("subw %A, %D, %D");

            // Compara o resultado da subtração com o zero
            commands.add("leaw $TRUE, %A");
            commands.add("jg %D");
            commands.add("nop");
            commands.add("leaw $0, %A");
            commands.add("movw %A, %D");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw %D, (%A)");
            commands.add("leaw $END, (%A)");
            commands.add("jmp");
            commands.add("nop");

            // Se %D > 0 , entao retorna True
            commands.add("TRUE:");
            commands.add("leaw $0, %A");
            commands.add("subw %A, $1, %D");
            commands.add("movw (%A),%A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw %D, (%A)");

            // Se %D <0, entao retorna false
            commands.add("END:");
            commands.add("leaw $0, %A");
            commands.add("movw (%A),%D");
            commands.add("decw %D");
            commands.add("movw %D, (%A)");

        } else if (command.equals("lt")) {
            commands.add(String.format("; %d - LT", lineCode++));

            // Le a posição do SP e carrega os dois valores anteriores em REG A e REG D           
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");
            commands.add("movw (%A), %A");

            // Efetua a subtração entre os Registradores e salva em D
            commands.add("subw %A, %D, %D");

            // Compara o resultado da subtração com o zero
            commands.add("leaw $TRUE, %A");
            commands.add("jl %D");
            commands.add("nop");
            commands.add("leaw $0, %A");
            commands.add("movw %A, %D");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw %D, (%A)");
            commands.add("leaw $END, (%A)");
            commands.add("jmp");
            commands.add("nop");

            // Se %D <0, entao retorna true
            commands.add("TRUE:");
            commands.add("leaw $0, %A");
            commands.add("subw %A, $1, %D");
            commands.add("movw (%A),%A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw %D, (%A)");

            // Se %D > 0 , entao retorna false
            commands.add("END:");
            commands.add("leaw $0, %A");
            commands.add("movw (%A),%D");
            commands.add("decw %D");
            commands.add("movw %D, (%A)");

        } else if (command.equals("and")) {
            commands.add(String.format("; %d - AND", lineCode++));

            // Carrega os dois ultimos valores do SP e salva nos REG A e REG D
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");

            // (%A) and %D
            commands.add("andw (%A), %D, %D");
            commands.add("movw %D, (%A)");
            commands.add("addw $1, %A, %D");

            // Le o SP e subscreve o valor por (%A) and %D
            commands.add("leaw $0, %A");
            commands.add("movw %D, (%A)");

        } else if (command.equals("or")) {
            commands.add(String.format("; %d - OR", lineCode++));

            // Carrega os dois ultimos valores de SP e salva nos REG A e REG D
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");

            // (%A) or %D
            commands.add("orw (%A), %D, %D");
            commands.add("movw %D, (%A)");
            commands.add("addw $1, %A, %D");

            // Le o SP e subscreve o valor por (%A) and %D
            commands.add("leaw $0, %A");
            commands.add("movw %D, (%A)");

        } else if (command.equals("not")) {
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("notw %D");
            commands.add("movw %D, (%A)");
        }

        String[] stringArray = new String[ commands.size() ];
        commands.toArray( stringArray );
        write(stringArray);

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para executar o comando de Push ou Pop.
     * @param  command comando de push ou pop a ser analisado.
     * @param  segment segmento de memória a ser usado pelo comando.
     * @param  index índice do segmento de memória a ser usado pelo comando.
     */
    public void writePushPop(Parser.CommandType command, String segment, Integer index) {

        if ( command.equals("")) {
            Error.error("Instrução invalida");
        }

        List<String> commands = new ArrayList<String>();

        if(command == Parser.CommandType.C_POP) {
            commands.add(String.format("; %d - POP %s %d", lineCode++ ,segment, index));

            if (segment.equals("constant")) {
                Error.error("Não faz sentido POP com constant");
            } 
            
            else if (segment.equals("local")) {
                

            } 
            
            else if (segment.equals("argument")) {

            }
            
            else if (segment.equals("this")) {

            } 
            
            else if (segment.equals("that")) {

            } 
            
            else if (segment.equals("static")) {

            } 
            
            else if (segment.equals("temp")) {

            } 
            
            else if (segment.equals("pointer")) {
                if(index==0) {

                } else {

                }
            }

        } 
        
        else if (command == Parser.CommandType.C_PUSH) {
            commands.add(String.format("; %d - PUSH %s %d", lineCode++ ,segment, index));

            if (segment.equals("constant")) {
                // carrega a constant em %A e move para %D
                // INDEX é o valor da constante convertido para binário
                commands.add("leaw $"+ index + ", %A"); 
                commands.add("movw %A, %D");

                // carrega o calor do SP e move a constant
                // para o topo da pilha
                commands.add("leaw $0,%A");
                commands.add("movw (%A),%A");
                commands.add("movw %D,(%A)");

                // altera stack pointer: SP = SP + 1
                commands.add("leaw $0,%A");
                commands.add("movw (%A),%D");
                commands.add("incw %D");
                commands.add("movw %D, (%A)");

            } 
            else if (segment.equals("local")) {
                // INDEX representa o valor que vai somar na memoria


                // Carrega o endereço salvo no LCL e salva no REG D
                commands.add("leaw $1, %A");
                commands.add("movw (%A), %D");

                // Carrega o index e soma com o REG D e salva em D
                commands.add("leaw $"+ index + ", %A");
                commands.add("addw %A, %D, %D");
                commands.add("movw %D, %A");
                commands.add("movw (%A), %D");

                // Le a posição do SP carrega a soma no destino
                commands.add("leaw $0, %A");
                commands.add("movw, (%A), %A");
                commands.add("movw %D, (%A)");

                // SP = SP+1
                commands.add("leaw $0, %A");
                commands.add("movw (%A), %D");
                commands.add("incw %D");
                commands.add("movw %D, (%A)");
        
        
            } 
            
            else if (segment.equals("argument")) {
                // Carrega o endereço salvo no LCL e salva no REG D
                commands.add("leaw $2, %A");
                commands.add("movw (%A), %D");

                // Carrega o index e soma com o REG D e salva em D
                commands.add("leaw $"+ index + ", %A");
                commands.add("addw %A, %D, %D");
                commands.add("movw %D, %A");
                commands.add("movw (%A), %D");

                // Le a posição do SP carrega a soma no destino
                commands.add("leaw $0, %A");
                commands.add("movw, (%A), %A");
                commands.add("movw %D, (%A)");

                // SP = SP+1
                commands.add("leaw $0, %A");
                commands.add("movw (%A), %D");
                commands.add("incw %D");
                commands.add("movw %D, (%A)");

            } 
            
            else if (segment.equals("this")) {
                // Carrega o endereço salvo no LCL e salva no REG D
                commands.add("leaw $3, %A");
                commands.add("movw (%A), %D");

                // Carrega o index e soma com o REG D e salva em D
                commands.add("leaw $"+ index + ", %A");
                commands.add("addw %A, %D, %D");
                commands.add("movw %D, %A");
                commands.add("movw (%A), %D");

                // Le a posição do SP carrega a soma no destino
                commands.add("leaw $0, %A");
                commands.add("movw, (%A), %A");
                commands.add("movw %D, (%A)");

                // SP = SP+1
                commands.add("leaw $0, %A");
                commands.add("movw (%A), %D");
                commands.add("incw %D");
                commands.add("movw %D, (%A)");

            } 
            
            else if (segment.equals("that")) {
                // Carrega o endereço salvo no LCL e salva no REG D
                commands.add("leaw $4, %A");
                commands.add("movw (%A), %D");

                // Carrega o index e soma com o REG D e salva em D
                commands.add("leaw $"+ index + ", %A");
                commands.add("addw %A, %D, %D");
                commands.add("movw %D, %A");
                commands.add("movw (%A), %D");

                // Le a posição do SP carrega a soma no destino
                commands.add("leaw $0, %A");
                commands.add("movw, (%A), %A");
                commands.add("movw %D, (%A)");

                // SP = SP+1
                commands.add("leaw $0, %A");
                commands.add("movw (%A), %D");
                commands.add("incw %D");
                commands.add("movw %D, (%A)");


            } 
            
            else if (segment.equals("static")) {
                // Carrega o endereço salvo no LCL e salva no REG D
                commands.add("leaw $16, %A");
                commands.add("movw (%A), %D");

                // Carrega o index e soma com o REG D e salva em D
                commands.add("leaw $"+ index + ", %A");
                commands.add("addw %A, %D, %D");
                commands.add("movw %D, %A");
                commands.add("movw (%A), %D");

                // Le a posição do SP carrega a soma no destino
                commands.add("leaw $0, %A");
                commands.add("movw, (%A), %A");
                commands.add("movw %D, (%A)");

                // SP = SP+1
                commands.add("leaw $0, %A");
                commands.add("movw (%A), %D");
                commands.add("incw %D");
                commands.add("movw %D, (%A)");
            } 
            
            else if (segment.equals("temp")) {
                // Carrega o endereço salvo no LCL e salva no REG D
                commands.add("leaw $5, %A");
                commands.add("movw (%A), %D");

                // Carrega o index e soma com o REG D e salva em D
                commands.add("leaw $"+ index + ", %A");
                commands.add("addw %A, %D, %D");
                commands.add("movw %D, %A");
                commands.add("movw (%A), %D");

                // Le a posição do SP carrega a soma no destino
                commands.add("leaw $0, %A");
                commands.add("movw, (%A), %A");
                commands.add("movw %D, (%A)");

                // SP = SP+1
                commands.add("leaw $0, %A");
                commands.add("movw (%A), %D");
                commands.add("incw %D");
                commands.add("movw %D, (%A)");

            } 
            
            else if (segment.equals("pointer")) {
                if(index==0) {
                    // Carrega o endereço salvo no LCL e salva no REG D
                    commands.add("leaw $3, %A");
                    commands.add("movw (%A), %D");

                    // Carrega o index e soma com o REG D e salva em D
                    commands.add("leaw $"+ index + ", %A");
                    commands.add("addw %A, %D, %D");
                    commands.add("movw %D, %A");
                    commands.add("movw (%A), %D");

                    // Le a posição do SP carrega a soma no destino
                    commands.add("leaw $0, %A");
                    commands.add("movw, (%A), %A");
                    commands.add("movw %D, (%A)");

                    // SP = SP+1
                    commands.add("leaw $0, %A");
                    commands.add("movw (%A), %D");
                    commands.add("incw %D");
                    commands.add("movw %D, (%A)");

                } else {
                    // Carrega o endereço salvo no LCL e salva no REG D
                    commands.add("leaw $4, %A");
                    commands.add("movw (%A), %D");

                    // Carrega o index e soma com o REG D e salva em D
                    commands.add("leaw $"+ index + ", %A");
                    commands.add("addw %A, %D, %D");
                    commands.add("movw %D, %A");
                    commands.add("movw (%A), %D");

                    // Le a posição do SP carrega a soma no destino
                    commands.add("leaw $0, %A");
                    commands.add("movw, (%A), %A");
                    commands.add("movw %D, (%A)");

                    // SP = SP+1
                    commands.add("leaw $0, %A");
                    commands.add("movw (%A), %D");
                    commands.add("incw %D");
                    commands.add("movw %D, (%A)");

                }
            }
        }

        String[] stringArray = new String[ commands.size() ];
        commands.toArray( stringArray );
        write(stringArray);

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para inicializar o processo da VM (bootstrap).
     * Também prepara a chamada para a função Sys.init
     * O código deve ser colocado no início do arquivo de saída.
     */
    public void writeInit(boolean bootstrap, boolean isDir) {

        List<String> commands = new ArrayList<String>();

        if(bootstrap || isDir)
            commands.add( "; Inicialização para VM" );

        if(bootstrap) {
            commands.add("leaw $256,%A");
            commands.add("movw %A,%D");
            commands.add("leaw $SP,%A");
            commands.add("movw %D,(%A)");
        }

        if(isDir){
            commands.add("leaw $Main.main, %A");
            commands.add("jmp");
            commands.add("nop");
        }

        if(bootstrap || isDir) {
            String[] stringArray = new String[commands.size()];
            commands.toArray(stringArray);
            write(stringArray);
        }
    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para gerar o labels (marcadores de jump).
     * @param  label define nome do label (marcador) a ser escrito.
     */
    public void writeLabel(String label) {

        List<String> commands = new ArrayList<String>();
        commands.add( "; Label (marcador)" );

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para gerar as instruções de goto (jumps).
     * Realiza um jump incondicional para o label informado.
     * @param  label define jump a ser realizado para um label (marcador).
     */
    public void writeGoto(String label) {

        List<String> commands = new ArrayList<String>();
        commands.add(String.format("; %d - Goto Incondicional", lineCode++));

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para gerar as instruções de goto condicional (jumps condicionais).
     * Realiza um jump condicional para o label informado.
     * @param  label define jump a ser realizado para um label (marcador).
     */
    public void writeIf(String label) {

        List<String> commands = new ArrayList<String>();
        commands.add(String.format("; %d - Goto Condicional", lineCode++));

     }

    /**
     * Grava no arquivo de saida as instruções em Assembly para uma chamada de função (Call).
     * @param  functionName nome da função a ser "chamada" pelo call.
     * @param  numArgs número de argumentos a serem passados na função call.
     */
    public void writeCall(String functionName, Integer numArgs) {

        List<String> commands = new ArrayList<String>();
        commands.add(String.format("; %d - chamada de funcao %s", lineCode++, functionName));

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para o retorno de uma sub rotina.
     */
    public void writeReturn() {

        List<String> commands = new ArrayList<String>();
        commands.add(String.format("; %d - Retorno de função", lineCode++));

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para a declaração de uma função.
     * @param  functionName nome da função a ser criada.
     * @param  numLocals número de argumentos a serem passados na função call.
     */
    public void writeFunction(String functionName, Integer numLocals) {

        List<String> commands = new ArrayList<String>();
        commands.add(String.format("; %d - Declarando função %s", lineCode++, functionName));

    }

    /**
     * Armazena o nome do arquivo vm de origem.
     * Usado para definir os dados estáticos do código (por arquivo).
     * @param file nome do arquivo sendo tratado.
     */
    public void vmfile(String file) {

        int i = file.lastIndexOf(File.separator);
        int j = file.lastIndexOf('.');
        this.filename = file.substring(i+1,j);

    }

    // grava as instruções em Assembly no arquivo de saída
    public void write(String[] stringArray) {
        // gravando comandos no arquivos
        for (String s: stringArray) {
            this.outputFile.println(s);
        }
    }

    // fecha o arquivo de escrita
    public void close() throws IOException {
        this.outputFile.close();
    }

}
