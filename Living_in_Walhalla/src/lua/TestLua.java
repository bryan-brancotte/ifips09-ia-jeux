package lua;

public class TestLua {

	private int _myValue1;
	private int _myValue2;
	private int _myValue3;
	private LuaScriptLoader _myscript;
	
	public TestLua() {
		_myscript = new LuaScriptLoader("lua/testLua.lua");
		_myscript.runLuaFunction("initValues", this);
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestLua myTest = new TestLua();
		myTest.displayMyself();
		myTest.clean();
	}

	public void setValue1(int v){
		_myValue1 = v;
	}
	public void setValue2(int v){
		_myValue2 = v;
	}
	public void setValue3(int v){
		_myValue3 = v;
	}
	
	public void displayMyself(){
		System.out.println("Value 1 : "+_myValue1);
		System.out.println("Value 2 : "+_myValue2);
		System.out.println("Value 3 : "+_myValue3);
	}
	
	public void clean(){
		_myscript.closeLuaScript();
	}
}
