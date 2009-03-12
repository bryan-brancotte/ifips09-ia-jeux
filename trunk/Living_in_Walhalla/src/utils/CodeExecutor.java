package utils;

/**
 * Wrapper permettant de fournir un morceau de vode a executer ultérieurement
 * 
 * @author "iGo"
 * 
 */
public interface CodeExecutor<T> {
	/**
	 * La fonction englobant le code a exécuter ultérieurement
	 */
	public void execute(T param);
	/**
	 * La fonction englobant le code a exécuter ultérieurement
	 */
	public boolean keepIterat();
}