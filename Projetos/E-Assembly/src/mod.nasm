; ------------------------------------------------------------
; Arquivo: Mod.nasm
; Curso: Elementos de Sistemas
; Criado por: Luciano Soares
; Data: 27/03/2017
;
; Calcula o resto da divisão (modulus) entre RAM[0] por RAM[1]
; e armazena o resultado na RAM[2].
;
; 4  % 3 = 1
; 10 % 7 = 3
; ------------------------------------------------------------
LOOP:

leaw $0, %A
movw (%A), %D
leaw $1, %A
subw %D, (%A), %D

leaw $END, %A
jl %D
nop

leaw $0, %A
movw %D, (%A)

leaw $LOOP, %A
jmp
nop

END:

leaw $0, %A
movw (%A), %D
leaw $2, %A
movw %D, (%A)