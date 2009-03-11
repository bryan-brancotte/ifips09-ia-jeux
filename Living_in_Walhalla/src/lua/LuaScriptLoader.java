package lua;
import org.keplerproject.luajava.*;
/** LuaJava javadoc : http://www.keplerproject.org/luajava/API/index.html 
 * Attention : Ajouter en argument VM "-Djava.library.path=lua/"
 * */
public class LuaScriptLoader {
	LuaState _myState;
	
	/**
	 * Constructor of the LuaScriptLoader class.
	 * It initializes LUA environment with the given script 
	 * (its path) for future use. 
	 * @param fileName
	 */
	public LuaScriptLoader(final String fileName) {
		_myState = LuaStateFactory.newLuaState();
		// needed to load LUA libraries 
		_myState.openLibs();
		// load script file 
		_myState.LdoFile(fileName);
	}
	
	/**
	 * Close the LUA environment
	 */
	public void closeLuaScript(){
		_myState.close();
	}
	
	/**
	 * Run the given function name with the given object in parameter
	 * @param functionName
	 * @param object
	 */
	public void runLuaFunction(String functionName, Object object){
		_myState.getGlobal(functionName);
		_myState.pushJavaObject(object); // give object to function
		_myState.call(1, 0);// nbArgs, nbResults
	}
}
