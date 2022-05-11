; Arquivo: Abs.nasm
; Curso: Elementos de Sistemas
; Criado por: Luciano Soares
; Data: 27/03/2017

; Multiplica o valor de RAM[1] com RAM[0] salvando em RAM[3]

LOOP:

leaw $0, %A
movw (%A), %D
leaw $3, %A
addw (%A), %D, %D

leaw $3, %A
movw %D, (%A)

leaw $2, %A
addw $1, (%A), %D
movw %D, (%A)
leaw $1, %A
movw (%A), %D
leaw $2, %A
subw %D, (%A), %D

leaw $END, %A
je %D
nop

leaw $LOOP, %A
jmp
nop
END: