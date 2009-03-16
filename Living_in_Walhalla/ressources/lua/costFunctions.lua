function CostTeamToTeam( Team, Entry )
	if Entry:getKey():getTeam():isOpposedTo(Team) then 
		return Entry:getValue();
	else
		return -Entry:getValue();
	end
end

function CostPlayerToTeam( Mover, Entry )
	if Entry:getKey():getTeam():isOpposedTo(Mover:getTeam()) then
		return Entry:getValue();
	elseif Mover ~= Entry:getKey() then
		return -2;
	else 
		return 0;
	end
end

function TestFunction()
	return 12;
end
