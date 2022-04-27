; Arquivo: sw1.nasm
; Curso: Elementos de Sistemas
; Criado por: Rafael Corsi
; Data: 4/2020

leaw $1,%A
movw (%A), %D
leaw $2, %A
addw (%A), %D, %D
leaw $3, %A
subw %D, %A, %D

leaw $ELSE, %A
jl %D
nop

leaw $1, %A
movw %A, %D
leaw $0, %A
movw %D, (%A)

leaw $END, %A
jmp 
nop

ELSE:

leaw $2, %A
movw %A, %D
leaw $0, %A
movw %D, (%A)

END: