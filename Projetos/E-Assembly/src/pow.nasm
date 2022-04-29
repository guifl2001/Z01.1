; Arquivo: Pow.nasm
; Curso: Elementos de Sistemas
; Criado por: Luciano Soares
; Data: 27/03/2017

; Eleva ao quadrado o valor da RAM[1] e armazena o resultado na RAM[0].
; Só funciona com números positivos
leaw $1, %A
movw (%A), %D
leaw $2, %A
movw %D, (%A)

INICIO:

leaw $1, %A
movw (%A), %D

leaw $FIM, %A
je %D
nop                

leaw $2, %A
movw (%A), %D

leaw $0, %A
addw (%A), %D, %A
movw %A, %D

leaw $0, %A
movw %D, (%A)

; pega o valor de RAM[0] pra continuar diminuindo

leaw $1, %A
movw (%A), %D

decw %D
movw %D, (%A)

leaw $INICIO, %A
jmp
nop

FIM:
