-- Elementos de Sistemas
-- by Luciano Soares
-- zerador16.vhd

library IEEE;
use IEEE.STD_LOGIC_1164.all;

entity xor16 is
  port(
        a   : in STD_LOGIC_VECTOR(15 downto 0);
	    b   : in STD_LOGIC_VECTOR(15 downto 0);
        y   : out STD_LOGIC_VECTOR(15 downto 0)
      );
end entity;

architecture rtl of xor16 is
  -- Aqui declaramos sinais (fios auxiliares)
  -- e componentes (outros módulos) que serao
  -- utilizados nesse modulo.

begin
  -- Implementação vem aqui!
  y <= a xor b;

end architecture;