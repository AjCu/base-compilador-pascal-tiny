var
	fact, x  : Integer;
begin
	read x;
	fact:=1;
	repeat
		fact:=fact*x;
		x:=x-1;
	until x=0;
	write fact;
end.