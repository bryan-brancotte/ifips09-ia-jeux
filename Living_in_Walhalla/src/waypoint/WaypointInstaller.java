package waypoint;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import surface.Surface;
import aStar2D.Node;
import aStar2D.Node.Link;

public class WaypointInstaller {

	public static Node[] placeProximateGridWaypointTwoAnalyze(Surface map, int nbWaypoint) {
		if (nbWaypoint < 4)
			return null;
		Node[][] nodeGrid = new Node[nbWaypoint][];
		int cptFail = 0;
		int cptRet = 0;
		float stepx = (float) map.getWxsize() / nbWaypoint;
		float stepy = (float) map.getWysize() / nbWaypoint;
		// création de point correct
		for (int i = 0; i < nbWaypoint; i++) {
			nodeGrid[i] = new Node[nbWaypoint];
			for (int j = 0; j < nbWaypoint; j++) {
				nodeGrid[i][j] = new Node(0.5F+i * stepx, 0.5F+j * stepy);
				if (map.isInAnObject(nodeGrid[i][j])) {
					nodeGrid[i][j] = null;
					cptFail++;
				}
			}
		}

		// // retrait des points trop connexe
		// HashSet<Node> toDel = new HashSet<Node>();
		// for (int i = 0; i < nbWaypoint; i++) {
		// for (int j = 0; j < nbWaypoint; j++) {
		// if (countNeighor(map, nodeGrid, i, j) == 8)
		// toDel.add(nodeGrid[i][j]);
		// }
		// }
		// cptFail += toDel.size();
		// for (int i = 0; i < nbWaypoint; i++) {
		// for (int j = 0; j < nbWaypoint; j++) {
		// if (toDel.contains(nodeGrid[i][j]))
		// nodeGrid[i][j] = null;
		// }
		// }
		// toDel.clear();

		// liaison des points
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null) {
					if ((i + 1 < nbWaypoint) && (j > 0) && (nodeGrid[i + 1][j - 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j - 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j - 1]);
					if ((i + 1 < nbWaypoint) && (nodeGrid[i + 1][j] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j]);
					if ((i + 1 < nbWaypoint) && (j + 1 < nbWaypoint) && (nodeGrid[i + 1][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j + 1]);
					if ((j + 1 < nbWaypoint) && (nodeGrid[i][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i][j + 1]);
				}
			}
		}

		// retrait des solitaires
		for (int i = 0; i < nbWaypoint; i++)
			for (int j = 0; j < nbWaypoint; j++)
				if (nodeGrid[i][j] != null)
					if (nodeGrid[i][j].getNeighborCount() == 0) {
						nodeGrid[i][j] = null;
						cptFail++;
					}

		// transcription du grid en 1 tableau
		Node[] retPremierPasse = new Node[nbWaypoint * nbWaypoint - cptFail];
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null)
					retPremierPasse[cptRet++] = nodeGrid[i][j];
			}
		}
		Node[] retSecondePasse = placeProximateGridWaypointAroudObject(map, nbWaypoint << 3, 0);
		// if(true)
		// return retSecondePasse;
		stepx = (stepx + stepy);
		stepy = stepx * 2;
		Random rand = new Random(System.nanoTime());
		for (Node n1 : retPremierPasse)
			for (Node n2 : retSecondePasse)
				if ((n1.distance(n2) < stepx || n1.distance(n2) < stepy && rand.nextInt(10) == 1) && map.canSee(n1, n2))
					Node.linkNode(n1, n2);
		cptRet = 0;
		Node[] ret = new Node[retPremierPasse.length + retSecondePasse.length];
		for (Node n : retPremierPasse)
			ret[cptRet++] = n;
		for (Node n : retSecondePasse)
			ret[cptRet++] = n;
		return ret;
	}

	public static Node[] placeProximateGridWaypointAroudObject(Surface map, int nbWaypoint, final float delta) {
		if (nbWaypoint < 4)
			nbWaypoint = 4;
		Node[][] nodeGrid = new Node[nbWaypoint][];
		int cptFail = 0;
		int cptRet = 0;
		float stepx = (float) map.getWxsize() / nbWaypoint;
		float stepy = (float) map.getWysize() / nbWaypoint;
		// création de point correct
		for (int i = 0; i < nbWaypoint; i++) {
			nodeGrid[i] = new Node[nbWaypoint];
			for (int j = 0; j < nbWaypoint; j++) {
				nodeGrid[i][j] = new Node(i * stepx + delta, j * stepy + delta);
				if (map.isInAnObject(nodeGrid[i][j])) {
					nodeGrid[i][j] = null;
					cptFail++;
				}
			}
		}

		// retrait des points trop connexe
		HashSet<Node> toDel = new HashSet<Node>();
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (countNeighor(map, nodeGrid, i, j) == 8)
					toDel.add(nodeGrid[i][j]);
			}
		}
		cptFail += toDel.size();
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (toDel.contains(nodeGrid[i][j]))
					nodeGrid[i][j] = null;
			}
		}
		toDel.clear();

		// liaison des points
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null) {
					if ((i + 1 < nbWaypoint) && (j > 0) && (nodeGrid[i + 1][j - 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j - 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j - 1]);
					if ((i + 1 < nbWaypoint) && (nodeGrid[i + 1][j] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j]);
					if ((i + 1 < nbWaypoint) && (j + 1 < nbWaypoint) && (nodeGrid[i + 1][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j + 1]);
					if ((j + 1 < nbWaypoint) && (nodeGrid[i][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i][j + 1]);
				}
			}
		}

		// retrait des points trop connexe
		// LinkedList<Node> toIsolate = new LinkedList<Node>();
		// for (int i = 0; i < nbWaypoint; i++)
		// for (int j = 0; j < nbWaypoint; j++)
		// if (nodeGrid[i][j] != null)
		// if (nodeGrid[i][j].getNeighborCount() == 8)
		// toIsolate.add(nodeGrid[i][j]);
		// for (Node n : toIsolate)
		// isolateWaypoint(n);

		// retrait des solitaires
		for (int i = 0; i < nbWaypoint; i++)
			for (int j = 0; j < nbWaypoint; j++)
				if (nodeGrid[i][j] != null)
					if (nodeGrid[i][j].getNeighborCount() == 0) {
						nodeGrid[i][j] = null;
						cptFail++;
					}

		// transcription du grid en 1 tableau
		Node[] ret = new Node[nbWaypoint * nbWaypoint - cptFail];
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null)
					ret[cptRet++] = nodeGrid[i][j];
			}
		}
		return ret;
	}

	public static Node[] placeProximateGridWaypointPseudoAroudObject(Surface map, int nbWaypoint, final float delta) {
		if (nbWaypoint < 4)
			nbWaypoint = 4;
		Node[][] nodeGrid = new Node[nbWaypoint][];
		int cptFail = 0;
		int cptRet = 0;
		float stepx = (float) map.getWxsize() / nbWaypoint;
		float stepy = (float) map.getWysize() / nbWaypoint;
		// création de point correct
		for (int i = 0; i < nbWaypoint; i++) {
			nodeGrid[i] = new Node[nbWaypoint];
			for (int j = 0; j < nbWaypoint; j++) {
				nodeGrid[i][j] = new Node(i * stepx + delta, j * stepy + delta);
				if (map.isInAnObject(nodeGrid[i][j])) {
					nodeGrid[i][j] = null;
					cptFail++;
				}
			}
		}

		// retrait des points trop connexe
		for (int i = 1; i < nbWaypoint - 1; i++) {
			for (int j = 1; j < nbWaypoint - 1; j++) {
				if (countNeighor(map, nodeGrid, i, j) == 8) {
					for (int k = 0; k < 9; k++) {
						if (k != 4)
							nodeGrid[k / 3][k % 3] = null;
					}
					cptFail += 8;
				}
			}
		}

		// liaison des points
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null) {
					if ((i + 1 < nbWaypoint) && (j > 0) && (nodeGrid[i + 1][j - 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j - 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j - 1]);
					if ((i + 1 < nbWaypoint) && (nodeGrid[i + 1][j] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j]);
					if ((i + 1 < nbWaypoint) && (j + 1 < nbWaypoint) && (nodeGrid[i + 1][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j + 1]);
					if ((j + 1 < nbWaypoint) && (nodeGrid[i][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i][j + 1]);
				}
			}
		}

		// retrait des points trop connexe
		// LinkedList<Node> toIsolate = new LinkedList<Node>();
		// for (int i = 0; i < nbWaypoint; i++)
		// for (int j = 0; j < nbWaypoint; j++)
		// if (nodeGrid[i][j] != null)
		// if (nodeGrid[i][j].getNeighborCount() == 8)
		// toIsolate.add(nodeGrid[i][j]);
		// for (Node n : toIsolate)
		// isolateWaypoint(n);

		// retrait des solitaires
		for (int i = 0; i < nbWaypoint; i++)
			for (int j = 0; j < nbWaypoint; j++)
				if (nodeGrid[i][j] != null)
					if (nodeGrid[i][j].getNeighborCount() == 0) {
						nodeGrid[i][j] = null;
						cptFail++;
					}

		// transcription du grid en 1 tableau
		Node[] ret = new Node[nbWaypoint * nbWaypoint - cptFail];
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null)
					ret[cptRet++] = nodeGrid[i][j];
			}
		}
		return ret;
	}

	protected static int countNeighor(final Surface map, final Node[][] nodeGrid, final int i, final int j) {
		if (nodeGrid[i][j] == null)
			return 0;
		int ret = 0;
		ret += ((i - 1) < 0 || (j - 1) < 0 || nodeGrid[i - 1][j - 1] == null || !map.canSee(nodeGrid[i][j],
				nodeGrid[i - 1][j - 1])) ? 0 : 1;
		ret += (i < 0 || (j - 1) < 0 || nodeGrid[i][j - 1] == null || !map.canSee(nodeGrid[i][j], nodeGrid[i][j - 1])) ? 0
				: 1;
		ret += ((i + 1) >= nodeGrid.length || (j - 1) < 0 || nodeGrid[i + 1][j - 1] == null || !map.canSee(
				nodeGrid[i][j], nodeGrid[i + 1][j - 1])) ? 0 : 1;
		ret += ((i - 1) < 0 || j < 0 || nodeGrid[i - 1][j] == null || !map.canSee(nodeGrid[i][j], nodeGrid[i - 1][j])) ? 0
				: 1;
		// ret += (i < 0 || j < 0 || nodeGrid[i ][j ] == null ||
		// !map.canSee(nodeGrid[i][j], nodeGrid[i][j])) ? 0 : 1;
		ret += ((i + 1) >= nodeGrid.length || j < 0 || nodeGrid[i + 1][j] == null || !map.canSee(nodeGrid[i][j],
				nodeGrid[i + 1][j])) ? 0 : 1;
		ret += ((i - 1) < 0 || (j + 1) >= nodeGrid[0].length || nodeGrid[i - 1][j + 1] == null || !map.canSee(
				nodeGrid[i][j], nodeGrid[i - 1][j + 1])) ? 0 : 1;
		ret += (i < 0 || (j + 1) >= nodeGrid[i].length || nodeGrid[i][j + 1] == null || !map.canSee(nodeGrid[i][j],
				nodeGrid[i][j + 1])) ? 0 : 1;
		ret += ((i + 1) >= nodeGrid.length || (j + 1) >= nodeGrid[0].length || nodeGrid[i + 1][j + 1] == null || !map
				.canSee(nodeGrid[i][j], nodeGrid[i + 1][j + 1])) ? 0 : 1;
		return ret;
	}

	public static Node[] placeProximateGridWaypoint(Surface map, int nbWaypoint, final float delta) {
		if (nbWaypoint < 4)
			nbWaypoint = 4;
		Node[][] nodeGrid = new Node[nbWaypoint][];
		int cptFail = 0;
		int cptRet = 0;
		float stepx = (float) map.getWxsize() / nbWaypoint;
		float stepy = (float) map.getWysize() / nbWaypoint;
		// création de point correct
		for (int i = 0; i < nbWaypoint; i++) {
			nodeGrid[i] = new Node[nbWaypoint];
			for (int j = 0; j < nbWaypoint; j++) {
				nodeGrid[i][j] = new Node(i * stepx + delta, j * stepy + delta);
				if (map.isInAnObject(nodeGrid[i][j])) {
					nodeGrid[i][j] = null;
					cptFail++;
				}
			}
		}
		// liaison des points
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null) {
					if ((i + 1 < nbWaypoint) && (j > 0) && (nodeGrid[i + 1][j - 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j - 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j - 1]);
					if ((i + 1 < nbWaypoint) && (nodeGrid[i + 1][j] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j]);
					if ((i + 1 < nbWaypoint) && (j + 1 < nbWaypoint) && (nodeGrid[i + 1][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i + 1][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i + 1][j + 1]);
					if ((j + 1 < nbWaypoint) && (nodeGrid[i][j + 1] != null)
							&& map.canSee(nodeGrid[i][j], nodeGrid[i][j + 1]))
						Node.linkNode(nodeGrid[i][j], nodeGrid[i][j + 1]);
				}
			}
		}

		// retrait des solitaires
		for (int i = 0; i < nbWaypoint; i++)
			for (int j = 0; j < nbWaypoint; j++)
				if (nodeGrid[i][j] != null)
					if (nodeGrid[i][j].getNeighborCount() == 0) {
						nodeGrid[i][j] = null;
						cptFail++;
					}

		// transcription du grid en 1 tableau
		Node[] ret = new Node[nbWaypoint * nbWaypoint - cptFail];
		for (int i = 0; i < nbWaypoint; i++) {
			for (int j = 0; j < nbWaypoint; j++) {
				if (nodeGrid[i][j] != null)
					ret[cptRet++] = nodeGrid[i][j];
			}
		}
		return ret;
	}

	public static Node[] placeMultiLayerGridWaypoint(Surface map, int nbWaypoint) {
		float dst = map.getWxsize() / nbWaypoint;
		float dstMin = dst * 1.8F;
		float dstMax = dst * 3;
		float dstTmp;
		Node[] retPasse1 = placeProximateGridWaypointAroudObject(map, nbWaypoint, 0F);
		Node[] retPasse2 = placeProximateGridWaypointAroudObject(map, nbWaypoint >> 2, 10F);
		// Node[] retPasse3 = placeProximateGridWaypointAroudObject(map,
		// nbWaypoint >> 3, 10F);
		// Node[] retPasse4 = placeProximateGridWaypoint(map, nbWaypoint >> 4,
		// 10F);
		// if (true)
		// return retPasse3;

		for (Node n1 : retPasse2)
			for (Node n2 : retPasse1)
				if (dstMin < (dstTmp = n1.distance(n2)) && (dstTmp < dstMax) && map.canSee(n1, n2))
					Node.linkNode(n1, n2);

		// dstMin *= 4;
		// dstMax *= 4;
		// for (Node n1 : retPasse3)
		// for (Node n2 : retPasse2)
		// if (dstMin < (dstTmp = n1.distance(n2)) && (dstTmp < dstMax) &&
		// map.canSee(n1, n2))
		// Node.linkNode(n1, n2);

		int cptRet = 0;
		cptRet += retPasse1.length;
		cptRet += retPasse2.length;
		// cptRet += retPasse3.length;
		Node[] ret = new Node[cptRet];
		for (Node n : retPasse1)
			ret[--cptRet] = n;
		for (Node n : retPasse2)
			ret[--cptRet] = n;
		// for (Node n : retPasse3)
		// ret[cptRet++] = n;
		return ret;
	}

	public static Node[] placeWaypoint(Surface map) {
		// Node[] ret = placeProximateGridWaypointTwoAnalyze(map, 12);
		 Node[] ret = placeProximateGridWaypoint(map, 40, 0F);
		// Node[] ret = placeMultiLayerGridWaypoint(map, 100);
		// Node[] ret = placeProximateGridWaypointPseudoAroudObject(map, 100,
		// 5);

		// netoyage du réseau
		LinkedList<Node> wayToAdd = new LinkedList<Node>();
		HashSet<Node> wayPointConnexe = new HashSet<Node>();
		int oldSize = 0;
		int i = 0;
		utils.LIFO.Iterator<Link> itN;

		do {
			while (i < ret.length && wayPointConnexe.contains(ret[i]))
				i++;
			wayPointConnexe.clear();
			wayPointConnexe.add(ret[i % ret.length]);
			do {
				oldSize = wayPointConnexe.size();
				for (Node n : wayPointConnexe) {
					if (n != null) {
						itN = n.getNeighbor();
						while (itN.hasNext())
							wayToAdd.add(itN.next().getNode());
					}
				}
				while (!wayToAdd.isEmpty())
					wayPointConnexe.add(wayToAdd.removeFirst());
			} while (oldSize != wayPointConnexe.size());
		} while (i < ret.length && (ret.length >> 2) > wayPointConnexe.size());
		ret = wayPointConnexe.toArray(new Node[0]);

		System.out.println("Link used : " + ret.length);
		return ret;
	}
}
