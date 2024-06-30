var
	n,x,y,z : Integer;
begin
	if n>0 then 
		write (1);
	if n>0 then
		write 0;
	else
		write 1;
		
	if (n>0 and x<10 or not y<30 and z=0) then
		n:= n+1;
		y:=y*5;
	else
		n:=n+2;
		y:=2*x;
    write n;
    write y;
end.