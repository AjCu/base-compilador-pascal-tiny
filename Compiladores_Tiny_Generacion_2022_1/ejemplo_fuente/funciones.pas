var
	pi, radio, radio : Integer;
	dias: Integer;
	dialibre : Integer;
PROCEDURE Circulo ()
BEGIN
	Circulo := pi * radio * radio;
END;

PROCEDURE LeerConjunto ()
BEGIN
	dias:=0;
	dialibre:=1;
		READ (dialibre);
		dias := dias + dialibre;
END;

begin
	Circulo;
	LeerConjunto;
end.