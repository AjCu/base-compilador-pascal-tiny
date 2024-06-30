Program funciones;
Var
	pi, radio, radio : Integer;
	dias: Integer;
	dia_libre : Integer;
PROCEDURE Circulo ()
BEGIN
	Circulo := pi * radio * radio
END;

PROCEDURE Leer_conjunto ()
BEGIN
	dias:=0;
	dia_libre:=1;
		READ (dia_libre);
		dias := dias + dia_libre
END;

begin
	Circulo;
	Leer_conjunto;
end.