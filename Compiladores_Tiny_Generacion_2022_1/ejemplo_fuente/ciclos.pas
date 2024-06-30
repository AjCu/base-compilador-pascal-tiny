var
	contador, N, i, potencia : Integer
begin
	repeat
		contador:=contador+1;
		write contador
	until contador = N;
	
	potencia := 1 ; i := 0 ;
	
	for anio := i/12 to z+1801 do
		if (anio mod 4) = 0 then
			write anio;
end.