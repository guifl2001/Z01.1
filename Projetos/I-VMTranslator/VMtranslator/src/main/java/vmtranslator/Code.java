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
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");
            commands.add("addw (%A), %D, %D");
            commands.add("movw %D, (%A)");
            commands.add("addw $1, %A, %D");
            commands.add("leaw $0, %A");
            commands.add("movw %D, (%A)");

        } else if (command.equals("sub")) {
            commands.add(String.format("; %d - SUB", lineCode++));
            commands.add("leaw $0, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");
            commands.add("subw (%A), %D, %D");
            commands.add("movw %D,(%A)");
            commands.add("addw $1, %A, %D");
            commands.add("leaw $0, %A");
            commands.add("movw %D, (%A)");

        } else if (command.equals("neg")) {
            commands.add(String.format("; %d - NEG", lineCode++));
            commands.add("leaw $SP, %A");
            commands.add("subw (%A), $1, %A");
            commands.add("movw (%A), %D");
            commands.add("negw %D");
            commands.add("movw %D, (%A)");

        } else if (command.equals("eq")) {
            commands.add(String.format("; %d - EQ", lineCode++));
            commands.add("leaw $SP, %A"); //ler o stack pointer
            commands.add("movw (%A), %A"); // movo o que está em SP pra A
            commands.add("decw %A"); // decresci uma linha
            commands.add("movw (%A), %D"); //move o que está no SP-1 para D
            commands.add("decw %A"); // decresci outra linha
            commands.add("movw (%A), %A"); // move o que está no SP-2 para A
            commands.add("subw %A, %D, %D"); //subtrai para ver se dá 0
            String label = String.format("TRUE%d", lineCode);
            commands.add("leaw " + "$" + label + ", %A"); // entra no LOOP se os valores abaixo do stack pointer sejam iguais
            commands.add("je %D");
            commands.add("nop");
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw $0, (%A)"); // retorna false
            commands.add("incw %A");
            commands.add("movw %A, %D");
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)"); // mover o valor do noso SP para ele
            String labelEnd = String.format("END%d", lineCode);
            commands.add("leaw " + "$" + labelEnd + ", %A");
            commands.add("jmp");
            commands.add("nop");

            commands.add(label + ":");
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw $-1, (%A)"); // retorna false
            commands.add("incw %A");
            commands.add("movw %A, %D");
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)"); // mover o valor do noso SP para ele
            commands.add("leaw " + "$" + labelEnd + ", %A");
            commands.add("jmp");
            commands.add("nop");

            commands.add(labelEnd + ":");


        } else if (command.equals("gt")) {
            commands.add(String.format("; %d - GT", lineCode++));
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A"); // movo o que está em SP pra A
            commands.add("decw %A"); // decresci uma linha
            commands.add("movw (%A), %D"); //move o que está no SP-1 para D
            commands.add("decw %A"); // decresci outra linha
            commands.add("movw (%A), %A"); // move o que está no SP-2 para A
            commands.add("subw %A, %D, %D"); //subtrai para ver se dá 0

            commands.add("leaw $TRUE, %A"); //
            commands.add("jg %D");
            commands.add("nop");
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw $0, (%A)"); // retorna false
            commands.add("incw %A");
            commands.add("movw %A, %D");
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)"); // mover o valor do noso SP para ele
            commands.add("leaw $END, %A");
            commands.add("jmp");
            commands.add("nop");

            commands.add("TRUE:");
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw $-1, (%A)"); // retorna false
            commands.add("incw %A");
            commands.add("movw %A, %D");
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)"); // mover o valor do noso SP para ele
            commands.add("leaw $END, %A");
            commands.add("jmp");
            commands.add("nop");

            commands.add("END:");


        } else if (command.equals("lt")) {
            commands.add(String.format("; %d - LT", lineCode++));
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A"); // movo o que está em SP pra A
            commands.add("decw %A"); // decresci uma linha
            commands.add("movw (%A), %D"); //move o que está no SP-1 para D
            commands.add("decw %A"); // decresci outra linha
            commands.add("movw (%A), %A"); // move o que está no SP-2 para A
            commands.add("subw %A, %D, %D"); //subtrai para ver se dá 0

            commands.add("leaw $TRUE, %A"); //
            commands.add("jl %D");
            commands.add("nop");
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw $0, (%A)"); // retorna false
            commands.add("incw %A");
            commands.add("movw %A, %D");
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)"); // mover o valor do noso SP para ele
            commands.add("leaw $END, %A");
            commands.add("jmp");
            commands.add("nop");

            commands.add("TRUE:");
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("decw %A");
            commands.add("movw $-1, (%A)"); // retorna false
            commands.add("incw %A");
            commands.add("movw %A, %D");
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)"); // mover o valor do noso SP para ele
            commands.add("leaw $END, %A");
            commands.add("jmp");
            commands.add("nop");

            commands.add("END:");

        } else if (command.equals("and")) {
            commands.add(String.format("; %d - AND", lineCode++));
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A"); // em A o valor da linha do SP
            commands.add("decw %A"); // decresci uma linha
            commands.add("movw (%A), %D"); // peguei o que estava em SP-1 e foi pra D
            commands.add("decw %A"); // decresci mais uma linha
            commands.add("andw (%A), %D, %D");
            commands.add("movw %D, (%A)"); // movi o valor do and para onde a SP-2 estava
            commands.add("incw %A"); // incrementei uma linha do SP-2, para que agora no SP-1 seja meu novo SP
            commands.add("movw %A, %D"); // movi o valor do novo SP para D
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)"); // salvei esse novo valor no SP

        } else if (command.equals("or")) {
            commands.add(String.format("; %d - OR", lineCode++));
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A");
            commands.add("decw %A");
            commands.add("movw (%A), %D");
            commands.add("decw %A");
            //commands.add("movw (%A), %A");
            commands.add("orw (%A), %D, %D");
            commands.add("movw %D, (%A)");
            commands.add("incw %A");
            commands.add("movw %A, %D");
            commands.add("leaw $SP, %A");
            commands.add("movw %D, (%A)");

        } else if (command.equals("not")) {
            commands.add("leaw $SP, %A");
            commands.add("movw (%A), %A"); //linha do SP em A
            commands.add("decw %A"); // SP-1
            commands.add("movw (%A), %D");
            commands.add("notw %D");
            commands.add("leaw $SP, %A");
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
     * @param  index índice do segkento de memória a ser usado pelo comando.
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
            } else if (segment.equals("local")) {
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %D"); // pega o que está em SP (linha do meu SP)
                commands.add("subw %D, $1, (%A)"); // guarda o novo valor do SP

                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D");
                commands.add("leaw $LCL, %A");
                commands.add("movw (%A), %A"); // linha onde começa o primeiro local
                commands.add("addw %A, %D, %D"); //soma o index com a linha que começa o primeiro local

                commands.add("leaw $R5, %A");
                commands.add("movw %D, (%A)"); // para não perder o valor onde quero salvar meu local, jogo no temp 0.

                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // valor do SP em A
                commands.add("movw (%A), %D"); // move para D o que está no topo da pilha
                commands.add("leaw $R5, %A");
                commands.add("movw (%A), %A");
                commands.add("movw %D, (%A)"); // move o que estava no topo da pilha para local 1

            } else if (segment.equals("argument")) {
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %D"); // pega o que está em SP
                commands.add("subw %D, $1, (%A)"); // guarda o nome valor do SP

                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D");
                commands.add("leaw $ARG, %A");
                commands.add("movw (%A), %A");
                commands.add("addw %A, %D, %D"); //move 260 para D

                commands.add("leaw $R5, %A");
                commands.add("movw %D, (%A)");

                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // valor do SP em A
                commands.add("movw (%A), %D"); // move para D o que está no topo da pilha
                commands.add("leaw $R5, %A");
                commands.add("movw (%A), %A");
                commands.add("movw %D, (%A)"); // move o que estava no topo da pilha para local 1


            } else if (segment.equals("this")) {
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %D"); // pega o que está em SP
                commands.add("subw %D, $1, (%A)"); // guarda o nome valor do SP

                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D");
                commands.add("leaw $THIS, %A");
                commands.add("movw (%A), %A");
                commands.add("addw %A, %D, %D"); //move 260 para D

                commands.add("leaw $R5, %A");
                commands.add("movw %D, (%A)");

                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // valor do SP em A
                commands.add("movw (%A), %D"); // move para D o que está no topo da pilha
                commands.add("leaw $R5, %A");
                commands.add("movw (%A), %A");
                commands.add("movw %D, (%A)"); // move o que estava no topo da pilha para local 1

            } else if (segment.equals("that")) {
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %D"); // pega o que está em SP
                commands.add("subw %D, $1, (%A)"); // guarda o nome valor do SP

                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D");
                commands.add("leaw $THAT, %A");
                commands.add("movw (%A), %A");
                commands.add("addw %A, %D, %D"); //move 260 para D

                commands.add("leaw $R5, %A");
                commands.add("movw %D, (%A)");

                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // valor do SP em A
                commands.add("movw (%A), %D"); // move para D o que está no topo da pilha
                commands.add("leaw $R5, %A");
                commands.add("movw (%A), %A");
                commands.add("movw %D, (%A)"); // move o que estava no topo da pilha para local 1

            } else if (segment.equals("static")) {
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %D");
                commands.add("decw %D");
                commands.add("movw %D, (%A)");
                commands.add("movw (%A), %A");
                commands.add("movw (%A). %D");
                commands.add("leaw $"+ this.filename+"."+ String.valueOf(index)+",%A");
                commands.add("movw %D, (%A)");

            } else if (segment.equals("temp")) {
                commands.add("leaw $SP,%A");
                commands.add("movw (%A),%D");
                commands.add("decw %D");
                commands.add("movw %D,(%A)");
                commands.add("movw (%A),%A");
                commands.add("movw (%A),%D");
                commands.add("leaw $" + String.valueOf(index+5) + ",%A");
                commands.add("movw %D,(%A)");

            } else if (segment.equals("pointer")) {
                if (index.intValue() == 0) {
                    commands.add("leaw $SP,%A");
                    commands.add("movw (%A),%D");
                    commands.add("decw %D");
                    commands.add("movw %D,(%A)");
                    commands.add("movw (%A),%A");
                    commands.add("movw (%A),%D");
                    commands.add("leaw $THIS,%A");
                    commands.add("movw %D,(%A)");
                }else {
                    commands.add("leaw $SP,%A");
                    commands.add("movw (%A),%D");
                    commands.add("decw %D");
                    commands.add("movw %D,(%A)");
                    commands.add("movw (%A),%A");
                    commands.add("movw (%A),%D");
                    commands.add("leaw $THAT,%A");
                    commands.add("movw %D,(%A)");
                }
            }

        } else if (command == Parser.CommandType.C_PUSH) {
            commands.add(String.format("; %d - PUSH %s %d", lineCode++ ,segment, index));

            if (segment.equals("constant")) {
                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D");
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A");
                commands.add("movw %D, (%A)"); // move para SP a constante
                commands.add("incw %A"); // +1 no valor do SP
                commands.add("movw %A, %D");
                commands.add("leaw $SP, %A");
                commands.add("movw %D, (%A)"); // move 259 para SP

            } else if (segment.equals("local")) { // push local!!!
                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D"); //index que mostra qual local que eu quero dar push local 1, 2....
                commands.add("leaw $LCL, %A");
                commands.add("movw (%A), %A");
                commands.add("addw %D, %A, %A");
                commands.add("movw (%A), %D"); // linha do local e estou pegando o que está nela
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // linha do meu SP
                commands.add("movw %D, (%A)"); // move o que estava no local pro SP
                commands.add("incw %A");
                commands.add("movw %A, %D");
                commands.add("leaw $SP, %A");
                commands.add("movw %D, (%A)");


            } else if (segment.equals("argument")) {
                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D"); //index que mostra qual local que eu quero dar push local 1, 2....
                commands.add("leaw $ARG, %A");
                commands.add("movw (%A), %A");
                commands.add("addw %D, %A, %A");
                commands.add("movw (%A), %D"); // linha do local e estou pegando o que está nela
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // linha do meu SP
                commands.add("movw %D, (%A)"); // move o que stava no local pro SP
                commands.add("incw %A");
                commands.add("movw %A, %D");
                commands.add("leaw $SP, %A");
                commands.add("movw %D, (%A)");

            } else if (segment.equals("this")) {
                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D"); //index que mostra qual local que eu quero dar push local 1, 2....
                commands.add("leaw $THIS, %A");
                commands.add("movw (%A), %A");
                commands.add("addw %D, %A, %A");
                commands.add("movw (%A), %D"); //linha do local e estou pegando o que está nela
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // linha do meu SP
                commands.add("movw %D, (%A)"); // move o que estava no local pro SP
                commands.add("incw %A");
                commands.add("movw %A, %D");
                commands.add("leaw $SP, %A");
                commands.add("movw %D, (%A)");

            } else if (segment.equals("that")) {
                commands.add("leaw $"+ String.valueOf(index)+" ,%A");
                commands.add("movw %A, %D"); //index que mostra qual local que eu quero dar push local 1, 2....
                commands.add("leaw $THAT, %A");
                commands.add("movw (%A), %A");
                commands.add("addw %D, %A, %A");
                commands.add("movw (%A), %D"); //linha do local e estou pegando o que está nela
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // linha do meu SP
                commands.add("movw %D, (%A)"); // move o que estava no local pro SP
                commands.add("incw %A");
                commands.add("movw %A, %D");
                commands.add("leaw $SP, %A");
                commands.add("movw %D, (%A)");

            } else if (segment.equals("static")) {
                commands.add("leaw $" + this.filename + "." + String.valueOf(index) + ",%A");
                commands.add("movw (%A),%D");
                commands.add("leaw $SP,%A");
                commands.add("movw (%A),%A");
                commands.add("movw %D,(%A)");
                commands.add("leaw $SP,%A");
                commands.add("movw (%A),%D");
                commands.add("incw %D");
                commands.add("movw %D,(%A)");

            } else if (segment.equals("temp")) {
                commands.add("leaw $"+ String.valueOf(index + 5)+" ,%A");
                commands.add("movw (%A), %D"); //pega o que tá no temp
                commands.add("leaw $SP, %A");
                commands.add("movw (%A), %A"); // linha do meu SP
                commands.add("movw %D, (%A)"); // move o que estava no temp que quero pro SP
                commands.add("incw %A");
                commands.add("movw %A, %D");
                commands.add("leaw $SP, %A");
                commands.add("movw %D, (%A)");


            } else if (segment.equals("pointer")) {
                if(index==0) {
                    commands.add("leaw $THIS,%A");
                    commands.add("movw (%A),%D");
                    commands.add("leaw $SP,%A");
                    commands.add("movw (%A),%A");
                    commands.add("movw %D,(%A)");
                    commands.add("leaw $SP,%A");
                    commands.add("movw (%A),%D");
                    commands.add("incw %D");
                    commands.add("movw %D,(%A)");
                }else {
                    commands.add("leaw $THAT,%A");
                    commands.add("movw (%A),%D");
                    commands.add("leaw $SP,%A");
                    commands.add("movw (%A),%A");
                    commands.add("movw %D,(%A)");
                    commands.add("leaw $SP,%A");
                    commands.add("movw (%A),%D");
                    commands.add("incw %D");
                    commands.add("movw %D,(%A)");
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
        commands.add(label + ":");
        String[] stringArray = new String[commands.size()];
        commands.toArray(stringArray);
        write(stringArray);


    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para gerar as instruções de goto (jumps).
     * Realiza um jump incondicional para o label informado.
     * @param  label define jump a ser realizado para um label (marcador).
     */
    public void writeGoto(String label) {

        List<String> comandos = Arrays.asList(String.format("; %d - Goto Incondicional", new Object[] { Integer.valueOf(this.lineCode++) }),"leaw $"+ label + ",%A","jmp","nop");
        String[] stringArray = new String[comandos.size()];
        comandos.toArray(stringArray);
        write(stringArray);

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para gerar as instruções de goto condicional (jumps condicionais).
     * Realiza um jump condicional para o label informado.
     * @param  label define jump a ser realizado para um label (marcador).
     */
    public void writeIf(String label) {

        List<String> commands = new ArrayList<String>();
        commands.add(String.format("; %d - Goto Condicional", lineCode++));

        // Ultimo valor da pilha.
        commands.add("leaw $SP, %A");
        commands.add("movw (%A), %A");
        commands.add("decw %A");
        // Decrementa o SP
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw %D, (%A)");
        commands.add("movw %D, %A");
        // Coloca o valor a ser comparado no %D
        commands.add("movw (%A), %D");

        // Coloca esse valor do ultimo valor da pilha em %D
        commands.add("notw %D");
        commands.add("leaw $" + label + ", %A");
        commands.add("je %D");
        commands.add("nop");

        // Escreve no arquivo de saida
        String[] stringArray = new String[commands.size()];
        commands.toArray(stringArray);
        write(stringArray);

    }

    /**
     * Grava no arquivo de saida as instruções em Assembly para uma chamada de função (Call).
     * @param  functionName nome da função a ser "chamada" pelo call.
     * @param  numArgs número de argumentos a serem passados na função call.
     */
    public void writeCall(String functionName, Integer numArgs) {

        List<String> commands = new ArrayList<String>();
        commands.add(String.format("; %d - chamada de funcao %s", lineCode++, functionName));
        // DESTINO/LCL/ARG/THIS/THAT
        // salva o destino
        commands.add("Destino: ");
        // Salva o LCL atual
        commands.add("leaw $" + "LCL" + " ,%A");
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw (%A), %A");
        commands.add("movw %D, (%A)");
        commands.add("incw %A"); // +1 no valor do SP
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw %D, (%A)");
        // salva o ARG atual
        commands.add("leaw $"+ "ARG" +" ,%A");
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw (%A), %A");
        commands.add("movw %D, (%A)");
        commands.add("incw %A"); // +1 no valor do SP
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw %D, (%A)");
        // Salva o this atual
        commands.add("leaw $" + "THIS" + " ,%A");
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw (%A), %A");
        commands.add("movw %D, (%A)");
        commands.add("incw %A"); // Incrementa o SP
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw %D, (%A)");
        // Salva o THAT atual
        commands.add("leaw $" + "THAT" + " ,%A");
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw (%A), %A");
        commands.add("movw %D, (%A)");
        commands.add("incw %A"); // +1 no valor do SP
        commands.add("movw %A, %D");
        commands.add("leaw $SP, %A");
        commands.add("movw %D, (%A)");
        // Altera o lcl para that + 1
        // LCL aponta para a posicao seguinte o THAT
        commands.add("leaw $SP, %A");
        commands.add("movw (%A), %D");
        commands.add("leaw $LCL, %A");
        commands.add("movw %D, (%A)");



        // altera o args para o primeiro argumento passado para a funcao.
        commands.add("leaw $" + functionName + ", %A");


        // Salva no arquivo nasm as instrucoes.
        String[] stringArray = new String[commands.size()];
        commands.toArray(stringArray);
        write(stringArray);
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
        commands.add(functionName + ":");
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