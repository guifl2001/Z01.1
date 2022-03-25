--
-- Elementos de Sistemas - Aula 5 - Logica Combinacional
-- Rafael . Corsi @ insper . edu . br
--
-- Arquivo exemplo para acionar os LEDs e ler os bottoes
-- da placa DE0-CV utilizada no curso de elementos de
-- sistemas do 3s da eng. da computacao

----------------------------
-- Bibliotecas ieee       --
----------------------------
library ieee;
use ieee.std_logic_1164.all;
use ieee.numeric_std.all;

----------------------------
-- Entrada e saidas do bloco
----------------------------
entity TopLevel is
	port(
		SW      : in  std_logic_vector(9 downto 0);
		HEX0    : out std_logic_vector(6 downto 0); -- 7seg0
      HEX1    : out std_logic_vector(6 downto 0); -- 7seg0
      HEX2    : out std_logic_vector(6 downto 0); -- 7seg0
		HEX3    : out std_logic_vector(6 downto 0); -- 7seg0
		LEDR    : out std_logic_vector(9 downto 0)
	);
end entity;

----------------------------
-- Implementacao do bloco --
----------------------------
architecture rtl of TopLevel is

--------------
-- signals
--------------

  signal x : std_logic_vector(15 downto 0) := x"0073"; -- 115
  signal y : std_logic_vector(15 downto 0) := x"005F"; -- 95
  signal saida : std_logic_vector(15 downto 0);
  signal zr, ng : STD_LOGIC;

--------------
-- component
--------------

component ALU is
	port (
			x,y:   in STD_LOGIC_VECTOR(15 downto 0);  -- entradas de dados da ALU
			zx:    in STD_LOGIC;                      -- zera a entrada x
			nx:    in STD_LOGIC;                      -- inverte a entrada x
			zy:    in STD_LOGIC;                      -- zera a entrada y
			ny:    in STD_LOGIC;                      -- inverte a entrada y
			f:     in STD_LOGIC_VECTOR(1 downto 0);   -- se 0 calcula x & y, senão x + y
			no:    in STD_LOGIC;                      -- inverte o valor da saída
			dir:   in STD_LOGIC;                      -- 0=>left 1=>right
			size:  in std_logic_vector(1 downto 0);   -- shift amount
			zr:    out STD_LOGIC;                     -- setado se saída igual a zero
			ng:    out STD_LOGIC;                     -- setado se saída é negativa
			saida: out STD_LOGIC_VECTOR(15 downto 0); -- saída de dados da ALU
			car:   out STD_LOGIC                      -- Carry de saida
	);
end component;

component sevenSeg is
	port (
			bcd : in  STD_LOGIC_VECTOR(3 downto 0);
			leds: out STD_LOGIC_VECTOR(6 downto 0));
end component;

---------------
-- implementacao
---------------
signal carry : STD_LOGIC;
begin

   ALU1 : ALU port map(x => x, y => y, zx => SW(0), nx => SW(1), zy => SW(2), ny => SW(3), f => SW(5 downto 4), no => SW(6), dir => SW(7), size => SW(9 downto 8), zr => zr, ng => ng, saida => saida, car => carry);
  
   SEVEN0 : sevenSeg port map(bcd => saida(3 downto 0), leds => HEX0);
   SEVEN1 : sevenSeg port map(bcd => saida(7 downto 4), leds => HEX1);
   SEVEN2 : sevenSeg port map(bcd => saida(11 downto 8), leds => HEX2);
   SEVEN3 : sevenSeg port map(bcd => saida(15 downto 12), leds => HEX3);
  
   LEDR(0) <= carry;

end rtl;
