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
	WHILE dia_libre>0 DO
	BEGIN
		READ (dia_libre);
		dias := dias + dia_libre
	END;
END;

begin
	Circulo;
	Leer_conjunto;
end.