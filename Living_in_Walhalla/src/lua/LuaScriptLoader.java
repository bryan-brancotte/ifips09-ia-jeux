package lua;
import java.util.ArrayList;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
/** LuaJava javadoc : http://www.keplerproject.org/luajava/API/index.html 
 * Attention : Ajouter en argument VM 
 * 	"-Djava.library.path="${workspace_loc:2009-03_Living_in_Walhalla/ressources/lua/}""
 * */
public class LuaScriptLoader {
	LuaState _myState;
	String _fileName;
	
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
		_fileName = fileName;
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
	public void loadLuaFunction(String functionName){
		// load script file
		int chargement = _myState.LdoFile(_fileName);
		//System.out.println("Result load "+chargement+" :: "+_fileName);
		_myState.getGlobal(functionName);
	}
	
	public void addObjectAsParameter(Object obj){
		_myState.pushJavaObject(obj); // give object to function
	}
	
	public void callFunction(int arg, int result){
		_myState.call(arg, result);// nbArgs, nbResults
	}
	
	public ArrayList<Object> getResults(int nbResult){
	ArrayList<Object> myResult = new ArrayList<Object>(nbResult);
		// get results in the good order
		for(int i=-nbResult; i<=-1; i++){
			if(_myState.isNumber(i)){
				//System.out.println("un nombre !!");
				myResult.add(new Double(_myState.toNumber(i)));
			}
			else if(_myState.isString(i)){
				//System.out.println("un string !!");
				myResult.add(new String(_myState.toString(i)));
			}
			else if(_myState.isObject(i)){
				try {
					//System.out.println("un objet !!");
					myResult.add(_myState.toJavaObject(i));
				} catch (LuaException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}		
		return (myResult);		
	}
	
	
}
