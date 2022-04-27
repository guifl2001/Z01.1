; Arquivo: led1.nasm
; Curso: Elementos de Sistemas
; Criado por: Rafael Corsi
; Data: 4/2020
;
; Faça o primeiro LED acender
; OFF OFF OFF OFF OFF OFF OFF OFF ON

leaw $1, %A
movw %A, %D
leaw $21184, %A
movw %D, (%A)