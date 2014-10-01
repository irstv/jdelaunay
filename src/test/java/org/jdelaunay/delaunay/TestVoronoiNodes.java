/**
 *
 * jDelaunay is a library dedicated to the processing of Delaunay and constrained
 * Delaunay triangulations from PSLG inputs.
 *
 * This library is developed at French IRSTV institute as part of the AvuPur and Eval-PDU project,
 * funded by the French Agence Nationale de la Recherche (ANR) under contract
 * ANR-07-VULN-01 and ANR-08-VILL-0005-01 .
 *
 * jDelaunay is distributed under GPL 3 license. It is produced by the "Atelier SIG" team of
 * the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2010-2012 IRSTV FR CNRS 2488
 *
 * jDelaunay is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * jDelaunay is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * jDelaunay. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.jdelaunay.delaunay;

import java.util.ArrayList;
import java.util.List;
import org.jdelaunay.delaunay.error.DelaunayError;
import org.jdelaunay.delaunay.geometries.DEdge;
import org.jdelaunay.delaunay.geometries.DPoint;
import org.jdelaunay.delaunay.geometries.DTriangle;

/**
 * This class performs miscellaneous tests on the VoronoiNode mechanisms.
 * @author Alexis Guéganno
 */
public class TestVoronoiNodes extends BaseUtility {

	/**
	 * Test the creation of a new VoronoiNode.
	 * @throws DelaunayError
	 */
	public void testCreation()throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn = new VoronoiNode(tri);
		assertTrue(vn.getLinkedNodes().isEmpty());
		DPoint comp = new DPoint(2,2,0);
		assertTrue(vn.getLocation().equals(comp));
		assertTrue(vn.getParent()==tri);
	}

	/**
	 * Test that we retrieve the barycenter when the circumcenter is masked by a constraint.
	 * @throws DelaunayError
	 */
	public void testLocationConstraint() throws DelaunayError {
		DEdge constr = new DEdge(6, 0, 0, 0, 3, 0);
		DEdge other = new DEdge(0, 3, 0, 3, 0, 0);
		constr.setLocked(true);
		DTriangle tri = new DTriangle(new DEdge(3,0,0,6,0,0), constr, other);
		VoronoiNode vn = new VoronoiNode(tri);
		assertTrue(vn.getLocation().equals(tri.getBarycenter()));
		vn.setParent(tri);
		assertTrue(vn.getLocation().equals(tri.getBarycenter()));
		other.setLocked(true);
		constr.setLocked(false);
		vn.setParent(tri);
		assertTrue(vn.getLocation().equals(new DPoint(tri.getBarycenter())));
	}

	public void testLocationBoundary() throws DelaunayError {
		DEdge constr = new DEdge(6, 0, 0, 0, 3, 0);
		DEdge other = new DEdge(0, 3, 0, 3, 0, 0);
		DTriangle tri = new DTriangle(new DEdge(3,0,0,6,0,0), constr, other);
		VoronoiNode vn = new VoronoiNode(tri);
		assertTrue(vn.getLocation().equals(tri.getBarycenter()));
		
	}

	/**
	 * Instanciate a voronoi node and change its parent. Check that everything went well.
	 * @throws DelaunayError
	 */
	public void testUpdateParent() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn = new VoronoiNode(tri);
		tri = new DTriangle(new DEdge(0,0,0,5,0,0), new DEdge(5,0,0,0,5,0) , new DEdge(0,5,0,0,0,0));
		vn.setParent(tri);
		assertTrue(vn.getParent().equals(tri));
		assertTrue(vn.getParent() == tri);
		assertTrue(vn.getLocation().equals(new DPoint(2.5, 2.5, 0)));
	}

	/**
	 * performs a equality test.
	 * @throws DelaunayError
	 */
	public void testEquality() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn = new VoronoiNode(tri);
		DTriangle tri2 = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn2 = new VoronoiNode(tri2);
		assertTrue(vn.equals(vn2));
		assertTrue(vn.hashCode() == vn2.hashCode());
		DPoint dp = new DPoint();
		assertFalse(vn.equals(dp));
	}

	/**
	 * Checks that the compareTo method is implemented as expected.
	 * Note that the last case is not supposed to happen with our triangulation
	 * algorithm : the two neighbour triangles are not delaunay, and are not
	 * separated by a constraint edge.
	 * @throws DelaunayError
	 */
	public void testComparison() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn = new VoronoiNode(tri);
		DTriangle tri2 = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn2 = new VoronoiNode(tri2);
		assertTrue(vn.compareTo(vn2)==0);
		DEdge ed = new DEdge(3,0,0,0,3,0);
		tri = new DTriangle(new DEdge(0,0,0,3,0,0), ed  , new DEdge(0,3,0,0,0,0));
		vn = new VoronoiNode(tri);
		tri2 = new DTriangle(new DEdge(3,3,0,3,0,0), ed, new DEdge(0,3,0,3,3,0));
		vn2 = new VoronoiNode(tri2);
		assertTrue(vn.compareTo(vn2)==-1);
		assertTrue(vn.getLocation().compareTo(vn2.getLocation())==-1);
		ed =  new DEdge(6,0,0,0,3,0);
		tri = new DTriangle(new DEdge(3,0,0,6,0,0), ed, new DEdge(0,3,0,3,0,0));
		vn = new VoronoiNode(tri);
		tri2 = new DTriangle(new DEdge(4,3,0,6,0,0), ed, new DEdge(0,3,0,4,3,0));
		vn2 = new VoronoiNode(tri2);
		assertTrue(vn.compareTo(vn2)==-1);
	}

	/**
	 * Tests that we can't set a null parent to a voronoi node.
	 * @throws DelaunayError
	 */
	public void testSetNullParent() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn = new VoronoiNode(tri);
		try{
			vn.setParent(null);
			assertTrue(false);
		} catch(DelaunayError d){
			assertTrue(true);
		}
	}

	/**
	 * Try to retrieve the neighbours of a VN. Its parent has three neighbour
	 * triangles, and none of the parent's edges are constraints.
	 * @throws DelaunayError
	 */
	public void testRetrieveNeighbours() throws DelaunayError {
		List<DTriangle> tris = getSampleTriangles();
		DTriangle tri = tris.get(9);
		assertTrue(tri.equals(new DTriangle(new DEdge(2,6,0,4,3,0), new DEdge(4,3,0,5,6,0), new DEdge(5,6,0,2,6,0))));
		VoronoiNode vn = new VoronoiNode(tri);
		List<VoronoiNode> neigh = vn.getNeighbourNodes();
		List<DTriangle> parents = new ArrayList<DTriangle>();
		for(VoronoiNode v : neigh){
			parents.add(v.getParent());
		}
		assertTrue(neigh.size()==3);
		assertTrue(parents.contains(new DTriangle(new DEdge(2,6,0,5,6,0), new DEdge(5,6,0,1,9,0), new DEdge(1,9,0,2,6,0))));
		assertTrue(parents.contains(new DTriangle(new DEdge(2,6,0,4,3,0), new DEdge(4,3,0,0,5,0), new DEdge(0,5,0,2,6,0))));
		assertTrue(parents.contains(new DTriangle(new DEdge(4,3,0,5,6,0), new DEdge(5,6,0,8,3,0), new DEdge(8,3,0,4,3,0))));
	}

	/**
	 * this test retrieves the neighbour of a triangle that have only two. Also check
	 * that there is not any duplications between triangles during the graph
	 * computation.
	 * @throws DelaunayError
	 */
	public void testRetrieveNeighEmptySide() throws DelaunayError {
		List<DTriangle> tris = getSampleTriangles();
		DTriangle tri = tris.get(5);
		assertTrue(tri.equals(new DTriangle(new DEdge(8,3,0,4,3,0), new DEdge(4,3,0,4,0,0), new DEdge(4,0,0,8,3,0))));
		VoronoiNode vn = new VoronoiNode(tri);
		List<VoronoiNode> neigh = vn.getNeighbourNodes();
		List<DTriangle> parents = new ArrayList<DTriangle>();
		for(VoronoiNode v : neigh){
			parents.add(v.getParent());
		}
		assertTrue(neigh.size()==2);
		assertTrue(parents.contains(tris.get(4)));
		assertTrue(parents.contains(tris.get(6)));
		assertTrue(parents.get(0)==tris.get(4) || parents.get(0)==tris.get(6));
		assertTrue(parents.get(1)==tris.get(5) || parents.get(1)==tris.get(6));
	}

	/**
	 * Retrieves the neighbours of a triangles that is connected to three other
	 * ones, but that is protected from one of them with a constraint.
	 * @throws DelaunayError
	 */
	public void testRetrieveNeighOneConstraint() throws DelaunayError {
		List<DTriangle> tris = getSampleTriangles();
		DTriangle tri = tris.get(9);
		DEdge[] edges = tri.getEdges();
		for(int i = 0; i<DTriangle.PT_NB; i++){
			if(edges[i].equals(new DEdge(2,6,0,4,3,0))){
				edges[i].setLocked(true);
				break;
			}
		}
		VoronoiNode vn = new VoronoiNode(tri);
		List<VoronoiNode> neigh = vn.getNeighbourNodes();
		List<DTriangle> parents = new ArrayList<DTriangle>();
		for(VoronoiNode v : neigh){
			parents.add(v.getParent());
		}
		assertTrue(neigh.size()==2);
		assertTrue(parents.contains(new DTriangle(new DEdge(2,6,0,5,6,0), new DEdge(5,6,0,1,9,0), new DEdge(1,9,0,2,6,0))));
		assertTrue(parents.contains(new DTriangle(new DEdge(4,3,0,5,6,0), new DEdge(5,6,0,8,3,0), new DEdge(8,3,0,4,3,0))));
	}

	/**
	 * Retrieves the neighbours of a triangles that is connected to three other
	 * ones, but that is protected from one of them with two constraints.
	 * @throws DelaunayError
	 */
	public void testRetrieveNeighTwoConstraints() throws DelaunayError {
		List<DTriangle> tris = getSampleTriangles();
		DTriangle tri = tris.get(9);
		DEdge[] edges = tri.getEdges();
		for(int i = 0; i<DTriangle.PT_NB; i++){
			if(edges[i].equals(new DEdge(2,6,0,4,3,0))){
				edges[i].setLocked(true);
				break;
			}
		}
		for(int i = 0; i<DTriangle.PT_NB; i++){
			if(edges[i].equals(new DEdge(2,6,0,5,6,0))){
				edges[i].setLocked(true);
				break;
			}
		}
		VoronoiNode vn = new VoronoiNode(tri);
		List<VoronoiNode> neigh = vn.getNeighbourNodes();
		List<DTriangle> parents = new ArrayList<DTriangle>();
		for(VoronoiNode v : neigh){
			parents.add(v.getParent());
		}
		assertTrue(neigh.size()==1);
		assertTrue(parents.contains(new DTriangle(new DEdge(4,3,0,5,6,0), new DEdge(5,6,0,8,3,0), new DEdge(8,3,0,4,3,0))));
	}

	/**
	 * Retrieves the neighbours of a triangles that is connected to three other
	 * ones, but that is protected from them with three constraints.
	 * @throws DelaunayError
	 */
	public void testRetrieveNeighThreeConstraints() throws DelaunayError {
		List<DTriangle> tris = getSampleTriangles();
		DTriangle tri = tris.get(9);
		DEdge[] edges = tri.getEdges();
		for(int i = 0; i<DTriangle.PT_NB; i++){
			if(edges[i].equals(new DEdge(2,6,0,4,3,0))){
				edges[i].setLocked(true);
			}
			if(edges[i].equals(new DEdge(2,6,0,5,6,0))){
				edges[i].setLocked(true);
			}
			if(edges[i].equals(new DEdge(4,3,0,5,6,0))){
				edges[i].setLocked(true);
			}
		}
		VoronoiNode vn = new VoronoiNode(tri);
		List<VoronoiNode> neigh = vn.getNeighbourNodes();
		List<DTriangle> parents = new ArrayList<DTriangle>();
		for(VoronoiNode v : neigh){
			parents.add(v.getParent());
		}
		assertTrue(neigh.isEmpty());
	}

	/**
	 * Check that we are able to replace a neighbour of a node.
	 * @throws DelaunayError
	 */
	public void testReplaceWithAlreadySeen() throws DelaunayError {
		List<DTriangle> tris = getSampleTriangles();
		DTriangle tri = tris.get(9);
		VoronoiNode vn = new VoronoiNode(tri);
		List<VoronoiNode> neigh = vn.getNeighbourNodes();
		VoronoiNode seen = null;
		for(VoronoiNode v : neigh){
			if(v.getParent().equals(new DTriangle(new DEdge(0,5,0,2,6,0), new DEdge(2,6,0,4,3,0), new DEdge(4,3,0,0,5,0)))){
				seen = v;
			}
		}
		VoronoiNode next = new VoronoiNode(new DTriangle(new DEdge(0,5,0,2,6,0), new DEdge(2,6,0,4,3,0), new DEdge(4,3,0,0,5,0)));
		vn.replaceNode(next);
		assertTrue(vn.getLinkedNodes().get(vn.getLinkedNodes().indexOf(next))==next);
		assertTrue(vn.getLinkedNodes().get(vn.getLinkedNodes().indexOf(next))!=seen);
		//We check we still find the 3 nodes we attempt.
		assertTrue(vn.getLinkedNodes().size()==3);
		assertTrue(vn.getLinkedNodes().get(2).equals(seen)||vn.getLinkedNodes().get(0).equals(seen)
			||vn.getLinkedNodes().get(1).equals(seen));
		next = new VoronoiNode(new DTriangle(new DEdge(8,3,0,5,6,0), new DEdge(5,6,0,4,3,0), new DEdge(4,3,0,8,3,0)));
		assertTrue(vn.getLinkedNodes().get(2).equals(next)||vn.getLinkedNodes().get(0).equals(next)
			||vn.getLinkedNodes().get(1).equals(next));
		next = new VoronoiNode(new DTriangle(new DEdge(2,6,0,5,6,0), new DEdge(5,6,0,1,9,0), new DEdge(1,9,0,2,6,0)));
		assertTrue(vn.getLinkedNodes().get(2).equals(next)||vn.getLinkedNodes().get(0).equals(next)
			||vn.getLinkedNodes().get(1).equals(next));
		
	}

	/**
	 * Check we catch an exception when using replaceNode in an unexpected way.
	 * @throws DelaunayError
	 */
	public void testReplaceNodeException() throws DelaunayError {
		DTriangle tri1 = new DTriangle(new DEdge(0,0,0,3,3,0), new DEdge(3,3,0,2,5,0), new DEdge(2, 5, 0, 0, 0, 0));
		DTriangle tri2 = new DTriangle(new DEdge(8, 8, 0, 9, 9, 0), new DEdge(9, 9, 0, 5, 7, 0), new DEdge(5, 7, 0, 8, 8, 0));
		VoronoiNode v1 = new VoronoiNode(tri1);
		VoronoiNode v2 = new VoronoiNode(tri2);
		try{
			v1.replaceNode(v2);
			assertTrue(false);
		} catch(DelaunayError d){
			assertTrue(true);
		}
	}

	/**
	 * Test the getRadius method.
	 * @throws DelaunayError
	 */
	public void testGetRadius() throws DelaunayError {
		DEdge constr = new DEdge(6, 0, 0, 0, 3, 0);
		DEdge other = new DEdge(0, 3, 0, 3, 0, 0);
		DTriangle tri = new DTriangle(new DEdge(3,0,0,6,0,0), constr, other);
		DTriangle tri2 = new DTriangle(constr, new DEdge(0,3,0,5,5,0), new DEdge(5,5,0,6,0,0));
		VoronoiNode vn = new VoronoiNode(tri2);
		assertTrue(vn.getRadius() == vn.getParent().getRadius());
		tri = new DTriangle(new DEdge(3,0,0,6,0,0), new DEdge(6, 0, 0, 0, 3, 0), new DEdge(0, 3, 0, 3, 0, 0));
		vn = new VoronoiNode(tri);
		assertTrue(vn.getRadius() != tri.getRadius());
	}

	public void testIsSeen() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,0,4,0) , new DEdge(0,4,0,0,0,0));
		VoronoiNode vn = new VoronoiNode(tri);
		assertFalse(vn.isSeen());
		vn.setSeen(true);
		assertTrue(vn.isSeen());
	}

	/**
	 * Build a sample of connected triangles that share the exact same edges and points.
	 * @return
	 * @throws DelaunayError
	 */
	static List<DTriangle> getSampleTriangles() throws DelaunayError {
		List<DTriangle> ret = new ArrayList<DTriangle>();
		DPoint p1 = new DPoint(0,5,0);
		DPoint p2 = new DPoint(1,2,0);
		DPoint p3 = new DPoint(1,9,0);
		DPoint p4 = new DPoint(2,6,0);
		DPoint p5 = new DPoint(4,0,0);
		DPoint p6 = new DPoint(4,3,0);
		DPoint p7 = new DPoint(5,6,0);
		DPoint p8 = new DPoint(8,3,0);
		DPoint p9 = new DPoint(8,6,0);
		DEdge e1 = new DEdge(p1, p2);
		DEdge e2 = new DEdge(p1, p3);
		DEdge e3 = new DEdge(p1, p4);
		DEdge e4 = new DEdge(p1, p6);
		DEdge e5 = new DEdge(p2, p5);
		DEdge e6 = new DEdge(p2, p6);
		DEdge e7 = new DEdge(p3, p4);
		DEdge e8 = new DEdge(p3, p7);
		DEdge e9 = new DEdge(p3, p9);
		DEdge e10 = new DEdge(p4, p6);
		DEdge e11 = new DEdge(p4, p7);
		DEdge e12 = new DEdge(p5, p6);
		DEdge e13 = new DEdge(p5, p8);
		DEdge e14 = new DEdge(p6, p7);
		DEdge e15 = new DEdge(p6, p8);
		DEdge e16 = new DEdge(p7, p8);
		DEdge e17 = new DEdge(p7, p9);
		DEdge e18 = new DEdge(p8, p9);
		DTriangle t1 = new DTriangle(e2, e3, e7);
		DTriangle t2 = new DTriangle(e11, e8, e7);
		DTriangle t3 = new DTriangle(e8, e17, e9);
		DTriangle t4 = new DTriangle(e16, e17, e18);
		DTriangle t5 = new DTriangle(e14, e15, e16);
		DTriangle t6 = new DTriangle(e15, e12, e13);
		DTriangle t7 = new DTriangle(e5, e12, e6);
		DTriangle t8 = new DTriangle(e1, e4, e6);
		DTriangle t9 = new DTriangle(e10, e4, e3);
		DTriangle t10 = new DTriangle(e10, e11, e14);
		ret.add(t1);
		ret.add(t2);
		ret.add(t3);
		ret.add(t4);
		ret.add(t5);
		ret.add(t6);
		ret.add(t7);
		ret.add(t8);
		ret.add(t9);
		ret.add(t10);
		return ret;
	}

}
