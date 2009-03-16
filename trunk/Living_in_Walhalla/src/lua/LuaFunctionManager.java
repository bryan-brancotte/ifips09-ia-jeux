package lua;

import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import life.IMover;
import life.ITeam;

public class LuaFunctionManager {
	private static final String _fileNameNode = System.getProperty("java.library.path")+"\\costFunctions.lua";
	private static LuaScriptLoader _myNodeLoader = new LuaScriptLoader(_fileNameNode);
	private static Semaphore _accessNodeFile = new Semaphore(1);  
	
	public static float determineOverCost(ITeam team, Entry<IMover, Float> e){
		_accessNodeFile.acquireUninterruptibly();

		_myNodeLoader.loadLuaFunction("CostTeamToTeam");
		_myNodeLoader.addObjectAsParameter(team);
		_myNodeLoader.addObjectAsParameter(e);
		_myNodeLoader.callFunction(2, 1);
		float result = ((Double)(_myNodeLoader.getResults(1).get(0))).floatValue();
		_accessNodeFile.release();
		return result;
	}
	
	public static float determineOverCost(IMover player, Entry<IMover, Float> e){
		_accessNodeFile.acquireUninterruptibly();

		_myNodeLoader.loadLuaFunction("CostPlayerToTeam");
		_myNodeLoader.addObjectAsParameter(player);
		_myNodeLoader.addObjectAsParameter(e);
		_myNodeLoader.callFunction(2, 1);
		float result = ((Double)(_myNodeLoader.getResults(1).get(0))).floatValue();
		_accessNodeFile.release();
		return result;
	}
	
	public static int callTestFunction(){
		//System.out.println(_fileNameNode);
		_myNodeLoader.loadLuaFunction("TestFunction");
		//System.out.println("Avant load");
		_myNodeLoader.callFunction(0, 1);
		return( ((Double)(_myNodeLoader.getResults(1).get(0))).intValue() );
	}
	
	public static void clean(){
		_myNodeLoader.closeLuaScript();
	}

}
