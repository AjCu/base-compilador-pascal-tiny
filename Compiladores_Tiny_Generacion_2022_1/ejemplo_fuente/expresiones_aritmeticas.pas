var
	N:Integer;
	X:Integer;
	Z, Y:Integer;
begin
	N:= 113;
	X := Y*Z;
	Z := X/Y - (N mod Z + 2*X);
	write N;
end.