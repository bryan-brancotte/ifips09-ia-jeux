package belette;

import java.awt.Graphics;

import utils.Vector2d;

/**
 * Interface pour donner une id�e de ce que l'on peut mettre
 * pour g�rer les balles
 * 
 * @author L. Simon, Univ. Paris Sud, 2008
 *
 */
public interface IBelette {

	/**
	 * @return le bot qui a tir� cette balle
	 */
	public IBot firedBy();
	
	/**
	 * @return le vecteur vitesse de la balle (ou de la roquette)
	 */
	public Vector2d getVelocity();
	
	/**
	 * @return force de la balle
	 */
	public float getPower();
	
	/**
	 * @return les coordonn�es de la balle
	 */
	public Vector2d getCoords();
	
	/**
	 * @return le rayon de l'arme, pour les collisions
	 */
	public float getRadius();
	
	/**
	 * Permet de rendre compte des d�gats sur le bot qui est touch�
	 * Suivant votre mod�lisation des bots, vous pouvez la remplacer
	 * par une m�thode IBot.hitBy(IBelette balle) dans les bots... A vous
	 * de voir...
	 * @param bot
	 */
	public void hitBot(IBot bot);
	
	/**
	 * Juste pour dire, au cas o� vous vouliez g�rer d'�ventuelles explosions
	 * @param impactCoords coordonn�es de l'impact
	 */
	public void hitWall(Vector2d impactCoords);
	
	/**
	 * Affichage de la balle (tra�ante, donc)
	 * @param g
	 */
	public void draw(Graphics g);
	
	
	/**
	 * Mise � jour des coordonn�es de la balle, gestion des collisions, ...
	 */
	public void computeNextFrame();
}
