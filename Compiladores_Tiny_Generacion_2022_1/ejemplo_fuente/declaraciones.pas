var
  x, y: Integer = 4;
  z, w: Boolean;
  miVector: array[5..10] of Integer;
begin
    miVector[x+y] := 5;
    z := True;
    w := False;
    miVector[6] := 5;
    write x;
    write z;
    write w;
end.