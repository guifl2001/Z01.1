/**
 * Curso: Elementos de Sistemas
 * Arquivo: Code.java
 */

package assembler;

/**
 * Traduz mnemônicos da linguagem assembly para códigos binários da arquitetura Z0.
 */
public class Code {

    public static String valo(Integer j, String[] mnemnonic){
        Integer valor= 0;
        for (int i= j; i < mnemnonic.length; i++){
            if (mnemnonic[i].equals("%A")){
                valor+= 1;
            } else if (mnemnonic[i].equals("%D")){
                valor+= 2;
            } else if (mnemnonic[i].equals("(%A)")){
                valor+= 4;
            }
        }
        return String.format("%04d", Integer.parseInt(Integer.toBinaryString(valor)));
    }

    /**
     * Retorna o código binário do(s) registrador(es) que vão receber o valor da instrução.
     * @param  mnemnonic vetor de mnemônicos "instrução" a ser analisada.
     * @return Opcode (String de 4 bits) com código em linguagem de máquina para a instrução.
     */
    public static String dest(String[] mnemnonic) {
        switch (mnemnonic[0]){
            case "movw"  : return valo(2, mnemnonic);
            case "addw"  : return valo(3, mnemnonic);
            case "incw"  : return valo(1, mnemnonic);
            case "subw"  : return valo(3, mnemnonic);
            case "rsubw" : return valo(3, mnemnonic);
            case "decw"  : return valo(1, mnemnonic);
            case "notw"  : return valo(1, mnemnonic);
            case "negw"  : return valo(1, mnemnonic);
            case "andw"  : return valo(3, mnemnonic);
            case "orw"   : return valo(3, mnemnonic);

            default    : return "0000";
        }
    }

    /**
     * Retorna o código binário do mnemônico para realizar uma operação de cálculo.
     * @param  mnemnonic vetor de mnemônicos "instrução" a ser analisada.
     * @return Opcode (String de 7 bits) com código em linguagem de máquina para a instrução.
     */
    public static String comp(String[] mnemnonic) {
    	switch (mnemnonic[0]){
            case "movw" : switch (mnemnonic[1]) {
                case "%A":
                    return "000110000";
                case "%D":
                    return "000001100";
                case "(%A)":
                    return "001110000";
                case "$1":
                    return "000111111";
                case "$0":
                    return "000101010";
                case "$-1":
                    return "000111010";
            }
            case "addw" :  switch (mnemnonic[1]) {
                case "%D": switch (mnemnonic[2]){
                    case "%A":return "000000010";
                    case "(%A)":return "001000010";
                    case "$1":return "000011111";
                }
                case "%A": switch (mnemnonic[2]){
                    case "%D":return "000000010";
                    case "$1":return "000110111";
                }
                case "(%A)":switch (mnemnonic[2]){
                    case "%D":return "001000010";
                    case "$1":return "001110111";
                }
                case "$1":switch (mnemnonic[2]){
                    case "%D":return "000011111";
                    case "%A":return "000110111";
                    case "(%A)":return "001110111";
                }
                case "$-1":switch (mnemnonic[2]){
                    case "%D":return "000001110";
                    case "%A":return "000110010";
                    case "(%A)":return "001110010";
                }
            }
            case "incw" :  switch (mnemnonic[1]) {
                case "%D"   : return "000011111";
                case "%A"   : return "000110111";
                case "(%A)" : return "001110111";
            }
            case "subw" :  switch (mnemnonic[1]) {
                case "%D":switch (mnemnonic[2]){
                    case "%A":return "000010011";
                    case "(%A)":return "001010011";
                    case "$1":return "000001110";
                }
                case "%A":switch (mnemnonic[2]){
                    case "$1":return "000110010";
                    case "%D":return "000000111";
                }
                case "(%A)":switch (mnemnonic[2]){
                    case "$1":return "001110010";
                    case "%D":return "001000111";
                }
            }
            case "rsubw" : switch (mnemnonic[1]){
                case "%D":switch (mnemnonic[2]){
                    case "%A":return "000000111";
                    case "(%A)":return "001000111";
                }
                case "$1":switch (mnemnonic[2]){
                    case "%D":return "000001110";
                    case "%A":return "000110010";
                    case "(%A)":return "001110010";
                }
                case "%A":
                    if ("%D".equals(mnemnonic[2])) {
                        return "000010011";
                    }
                case "(%A)":
                    if ("%D".equals(mnemnonic[2])) {
                        return "001010011";
                    }
            }
            case "decw" :  switch (mnemnonic[1]) {
                case "%D"   : return "000001110";
                case "%A"   : return "000110010";
                case "(%A)" : return "001110010";
            }
            case "notw" :  switch (mnemnonic[1]) {
                case "%D"   : return "000001101";
                case "%A"   : return "000110001";
                case "(%A)" : return "001110001";
            }
            case "negw" :  switch (mnemnonic[1]) {
                case "%D"   : return "000001111";
                case "%A"   : return "000110011";
                case "(%A)" : return "001110011";
            }
            case "andw" :  switch (mnemnonic[1]) {
                case "%D": switch (mnemnonic[2]){
                    case "%A": return "000000000";
                    case "(%A)": return "001000000";
                }
                case "%A": return "000000000"; /*andw %A,%D,Z*/
                case "(%A)": return "001000000"; /*andw %(%A),%D,Z*/
            }
            case "orw" :  switch (mnemnonic[1]) {
                case "%D": switch (mnemnonic[2]){
                    case "%A": return "000010101";
                    case "(%A)": return "001010101";
                }
                case "%A": return "000010101"; /*andw %A,%D,Z*/
                case "(%A)": return "001010101"; /*andw %(%A),%D,Z*/
            }
            default : return "000001100";
        }
    }

    /**
     * Retorna o código binário do mnemônico para realizar uma operação de jump (salto).
     * @param  mnemnonic vetor de mnemônicos "instrução" a ser analisada.
     * @return Opcode (String de 3 bits) com código em linguagem de máquina para a instrução.
     */
    public static String jump(String[] mnemnonic) {
        switch (mnemnonic[0]){
            case "jmp"  : return "111";
            case "jle"  : return "110";
            case "jne"  : return "101";
            case "jl"   : return "100";
            case "jge"  : return "011";
            case "je"   : return "010";
            case "jg"   : return "001";

            default    : return "000";
        }
    }


    /**
     * Retorna o código binário de um valor decimal armazenado numa String.
     * @param  symbol valor numérico decimal armazenado em uma String.
     * @return Valor em binário (String de 15 bits) representado com 0s e 1s.
     */
    public static String toBinary(String symbol) {
        int num = Integer.valueOf(symbol);
        String binario = "";
        int resto = 0;

        while (num > 0){
            resto = num%2;
            binario=resto+binario;
            num = num/2;
        }

        while (binario.length() != 16){
            binario = '0' + binario;
        }
        return binario;
    }
}
