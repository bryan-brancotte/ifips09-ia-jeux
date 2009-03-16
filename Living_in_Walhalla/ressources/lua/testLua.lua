function initValues(TestLua)
	TestLua:setValue1(100);
	TestLua:setValue2(200);
	TestLua:setValue3(300);
	str = luajava.bindClass("java.lang.String")
	strInstance = luajava.new(str)
	return 1234, 5678, strInstance;
end
