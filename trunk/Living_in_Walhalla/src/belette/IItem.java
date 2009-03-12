package belette;

import java.awt.Graphics;

import utils.Vector2d;
/**
 * Interface (a modifier suivant vos envies) permettant de g�rer les items dans le jeu
 * 
 * @author L. Simon, Univ. Paris Sud, 2008
 *
 */
public interface IItem {

	/**
	 * @return le temps en frames de respawn pour cet item
	 */
	public int getRespawnTime();
	/**
	 * Modifie le temps que met un item pour apparaitre sur la carte
	 * @param time temps en frames
	 */
	public void setRespawnTime(int frames); 

	/**
	 * Ajoute des coordonn�es possibles pour apparaitre sur la carte
	 * apr�s getRespawnTime() secondes une fois r�cup�r�
	 * @param x
	 * @param y
	 */
	public void addCoordRespawn(int x, int y);
	
	/**
	 * @return Les coordonn�es de l'item, ou -1,-1 si pas sur la carte par ex.
	 */
	public Vector2d getCoord();
	
	/**
	 * On va repr�senter les item en rond... Pour les collisions sur la carte,
	 * on a besoin du rayon de l'item
	 * @return le rayon (en coordonn�es de cartes) de l'item
	 */
	public float itemRadius();
	
	/**
	 * Permet d'appeler les fonctions ad�quates pour modifier le bot
	 * Vous pouvez le modifier pour appeler cette fonction � l'envers,
	 * depuis bot avec une m�thode captureItem(IItem item) dans bot...
	 * A vous de voir...
	 * Il faut lancer un timer pour faire renaitre l'item au bout d'un certain temps...
	 * @param bot
	 */
	public void capturedByBot(IBot bot);
	
	/**
	 * Affiche l'item sur le dessin... A vos id�es !
	 * @param g
	 */
	public void draw(Graphics g);
	
	/**
	 * @return la chaine representant l'item et son etat, pour debug ou autre
	 */
	public String toString();
	
	/**
	 * Mise � jour �ventuelle apr�s chaque frame (R�cup�r� ? disparaitre ? Apparaitre ? D�grader ?)
	 */
	public void computeNextFrame();
	
}
