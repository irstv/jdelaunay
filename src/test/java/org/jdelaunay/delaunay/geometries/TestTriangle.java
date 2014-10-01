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
package org.jdelaunay.delaunay.geometries;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static junit.framework.Assert.assertEquals;
import junit.framework.TestCase;
import org.jdelaunay.delaunay.ConstrainedMesh;
import org.jdelaunay.delaunay.error.DelaunayError;
import org.jdelaunay.delaunay.tools.Tools;

/**
 * This class checks the reliability of the methods written in DTriangle
 * @author Alexis Guéganno
 */
public class TestTriangle extends TestCase {

        public void testConstructors() throws DelaunayError {
                DTriangle dt = new DTriangle(new DEdge(0,0,0,4,0,0), new DEdge(4,0,0,2,2,0), new DEdge(2,2,0,0,0,0));
                DTriangle dt2 = new DTriangle(dt);
                assertTrue(dt2.belongsTo(new DPoint(0,0,0)));
                assertTrue(dt2.belongsTo(new DPoint(2,2,0)));
                assertTrue(dt2.belongsTo(new DPoint(4,0,0)));
                assertTrue(dt.checkTopology());
                assertFalse(dt2.checkTopology());
                DTriangle dt3 = new DTriangle (new DPoint(0,0,0), new DPoint(0,2,0), new DPoint(2,0,0));
                for(DEdge ed : dt3.getEdges()){
                        assertTrue(ed.getLeft() == dt3 || ed.getRight() == dt3);
                }
                assertTrue(dt3.belongsTo(new DPoint(0,0,0)));
                assertTrue(dt3.belongsTo(new DPoint(2,0,0)));
                assertTrue(dt3.belongsTo(new DPoint(0,2,0)));
        }
        
	/**
	 * Checks that getArea works well. The test is basic, but important.
	 */
	public void testComputeArea() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,0,0);
		DPoint p3 = new DPoint(3,2,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t = new DTriangle(e1,e2,e3);
		double d = t.getArea();
		assertTrue(d==4);
	}

	/**
	 * Checks that the constructor of DTriangle throws an exception
	 * when edges used for building the triangle are not linked
	 * @throws DelaunayError
	 */
	public void testEdgesIntegrity() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,2,0);
		DPoint p4 = new DPoint(8,0,0);
		DPoint p5 = new DPoint(7,9,0);
		DPoint p6 = new DPoint(3,7,0);
		DEdge e1 = new DEdge(p1,p4);
		DEdge e2 = new DEdge(p2,p5);
		DEdge e3 = new DEdge(p3,p6);
		DTriangle t = null;
		try{
			t= new DTriangle(e1,e2,e3);
		} catch (DelaunayError d){
		}
		assertNull(t);
		p1 = new DPoint(0,0,0);
		p2 = new DPoint(4,5,0);
		p3 = new DPoint(3,2,0);
		p4 = new DPoint(8,0,0);
		p5 = new DPoint(7,9,0);
		e1 = new DEdge(p1,p4);
		e2 = new DEdge(p2,p5);
		e3 = new DEdge(p3,p4);
		t = null;
		try{
			t= new DTriangle(e1,e2,e3);
		} catch (DelaunayError d){
		}
		assertNull(t);
		p1 = new DPoint(0,0,0);
		p2 = new DPoint(4,5,0);
		p3 = new DPoint(3,2,0);
		p4 = new DPoint(8,0,0);
		e1 = new DEdge(p1,p4);
		e2 = new DEdge(p2,p4);
		e3 = new DEdge(p3,p4);
		t = null;
		try{
			t= new DTriangle(e1,e2,e3);
		} catch (DelaunayError d){
		}
		assertNull(t);
		t = null;
		try{
			t= new DTriangle(e1,e3,e2);
		} catch (DelaunayError d){
		}
		assertNull(t);
		t = null;
		try{
			t= new DTriangle(e2,e1,e3);
		} catch (DelaunayError d){
		}
		assertNull(t);
		p1 = new DPoint(0,0,0);
		p2 = new DPoint(4,5,0);
		p3 = new DPoint(3,2,0);
		p4 = new DPoint(8,0,0);
		e1 = new DEdge(p1,p4);
		e2 = new DEdge(p2,p4);
		e3 = new DEdge(p3,p2);
		t = null;
		try{
			t= new DTriangle(e1,e2,e3);
		} catch (DelaunayError d){
		}
		assertNull(t);

	}
       
        /**
         * Test that getPoint and getPoints are consistent.
         * @throws DelaunayError 
         */
        public void testGetPoint() throws DelaunayError{
                DTriangle dt = new DTriangle(new DPoint(0,0,0), new DPoint(2,2,0),new DPoint(5,3,0));
                for(int i=0;i<DTriangle.PT_NB;i++){
                        assertTrue(dt.getPoints().get(i)==dt.getPoint(i));
                }
        }
        
        public void testSetEdge() throws DelaunayError {
                DTriangle dt = new DTriangle(new DPoint(0,0,0), new DPoint(2,2,0),new DPoint(5,3,0));
                assertTrue(dt.setEdge(0, new DEdge(5,3,0,8,9,0)));
                assertFalse(dt.setEdge(8, new DEdge(6,6,6,0,9,6)));
                assertFalse(dt.setEdge(-1, new DEdge(6,6,6,0,9,6)));
        }

	/**
	 * Checks that the center of the triangle (or rather, the center of its circumcircle)
	 * is well computed.
	 * @throws DelaunayError
	 */
	public void testGetAndRecomputeCenter() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(0,3,0);
		DPoint p3 = new DPoint(3,0,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t = null;
		t= new DTriangle(e1,e2,e3);
		DPoint center = t.getCircumCenter();
		assertTrue(center.equals(new DPoint(1.5,1.5,0)));
	}

	/**
	 * Test the sort used to classify the triangles, which is based on the center
	 * of their bounding box
	 */
	public void testTriangleSort() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,2,0);
		DPoint p4 = new DPoint(8,0,0);
		DPoint p5 = new DPoint(7,9,0);
		DPoint p6 = new DPoint(3,7,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t1 = new DTriangle(e1, e2, e3);
		DEdge e4 = new DEdge(p1,p2);
		DEdge e5 = new DEdge(p2,p3);
		DEdge e6 = new DEdge(p3,p1);
		DTriangle t2 = new DTriangle(e4, e5, e6);
		assertTrue(t1.compareTo(t2)==0);
		e4 = new DEdge(p4, p5);
		e5 = new DEdge(p5, p6);
		e6 = new DEdge(p6, p4);
		t2 = new DTriangle(e4, e5, e6);
		assertTrue(t1.compareTo(t2)==-1);
		assertTrue(t2.compareTo(t1)==1);
		e4 = new DEdge(p3, p4);
		e5 = new DEdge(p2, p4);
		t2 = new DTriangle(e2, e4, e5);
		assertTrue(t1.compareTo(t2)==-1);
		assertTrue(t2.compareTo(t1)==1);
	}

	/**
	 * Checks the efficiency of the methods supposed to sort a list of triangle,
	 * and to insert a new element in it while keeping it sorted.
	 * Note that two different triangles may be reported as equal...
	 * This case is supposed not to happen in the delaunay triangulation, as
	 * triangles can't intersect in it but on common edges or points.
	 */
	public void testListTriangleSort() throws DelaunayError{
		List<DTriangle> triangleList = new ArrayList<DTriangle>();
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,2,0);
		DPoint p4 = new DPoint(8,0,0);
		DPoint p5 = new DPoint(7,9,0);
		DPoint p6 = new DPoint(3,7,0);
		DPoint p7 = new DPoint(10,5,0);
		DPoint p8 = new DPoint(3,9,0);
		DPoint p9 = new DPoint(2.2,5.02,0);
		DPoint p10 = new DPoint(4,14,0);
		DPoint p11 = new DPoint(12,6,0);
		DPoint p12 = new DPoint(3.5,24,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p1,p5);
		e2 = new DEdge(p5,p3);
		e3 = new DEdge(p3,p1);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p1,p5);
		e2 = new DEdge(p5,p3);
		e3 = new DEdge(p3,p1);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p7,p5);
		e2 = new DEdge(p5,p6);
		e3 = new DEdge(p6,p7);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p8,p10);
		e2 = new DEdge(p10,p11);
		e3 = new DEdge(p11,p8);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p10,p11);
		e2 = new DEdge(p11,p4);
		e3 = new DEdge(p4,p10);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p4,p6);
		e2 = new DEdge(p6,p8);
		e3 = new DEdge(p4,p8);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p7,p1);
		e2 = new DEdge(p1,p8);
		e3 = new DEdge(p8,p7);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p9,p5);
		e2 = new DEdge(p5,p4);
		e3 = new DEdge(p4,p9);
		triangleList.add(new DTriangle(e1, e2, e3));
		e1 = new DEdge(p12,p2);
		e2 = new DEdge(p2,p8);
		e3 = new DEdge(p8,p12);
		triangleList.add(new DTriangle(e1, e2, e3));
		Collections.sort(triangleList);
		isTriangleListSorted(triangleList);
		e1 = new DEdge(p5,p7);
		e2 = new DEdge(p7,p9);
		e3 = new DEdge(p9,p5);
		DTriangle t1=new DTriangle(e1, e2, e3);
	}

	private void isTriangleListSorted(List<DTriangle> list){
		if(list !=null && !list.isEmpty()){
			DTriangle previous;
			DTriangle current = list.get(0);
			for(int i=1; i<list.size();i++){
				previous = current;
				current = list.get(i);
				assertTrue(previous.compareTo(current)<1);
			}
		} else {
			assertTrue(false);
		}
	}

	/**
	 * Test the isInside method.
	 * @throws DelaunayError
	 */
	public void testIsInside() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,0,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t1 =new DTriangle(e1, e2, e3);
		assertTrue(t1.isInside(p1));
		assertTrue(t1.isInside(p2));
		assertTrue(t1.isInside(p3));
		assertTrue(t1.isInside(new DPoint(2,2,0)));
		assertTrue(t1.isInside(new DPoint(1,0,0)));

	}

	/**
	 * Tests whether a point is on one of the edges that form this triangle or not.
	 * @throws DelaunayError
	 */
	public void testIsOnAnEdge() throws DelaunayError {
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(0,3,0);
		DPoint p3 = new DPoint(3,0,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t1 =new DTriangle(e1, e2, e3);
		assertTrue(t1.isOnAnEdge(new DPoint(1,0,0)));
		assertTrue(t1.isOnAnEdge(new DPoint(0,0,0)));
		assertTrue(t1.isOnAnEdge(new DPoint(1.5,1.5,0)));
		assertTrue(t1.isOnAnEdge(new DPoint(0,2,0)));
		assertFalse(t1.isOnAnEdge(new DPoint(1,8,0)));
		assertFalse(t1.isOnAnEdge(new DPoint(1,1,0)));
	}

	/**
	 * Performs equality tests between triangles.
	 */
	public void testTrianglesEquality() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,0,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t1 =new DTriangle(e1, e2, e3);
		DTriangle t2 =new DTriangle(e1, e2, e3);
		assertEquals(t1,t2);
		t2 =new DTriangle(e2, e1, e3);
		assertEquals(t1,t2);
		t2 =new DTriangle(e2, e1, e3);
		assertEquals(t1,t2);
		t2 =new DTriangle(e2, e3, e1);
		assertEquals(t1,t2);
		t2 =new DTriangle(e1, e3, e2);
		assertEquals(t1,t2);
		t2 =new DTriangle(e3, e2, e1);
		assertEquals(t1,t2);
		t2 =new DTriangle(e3, e1, e2);
		assertEquals(t1,t2);
		DPoint p4 = new DPoint(8,0,0);
		DEdge e4 = new DEdge(p4,p1);
		DEdge e5 = new DEdge(p4,p2);
		t2 =new DTriangle(e4, e1, e5);
		assertFalse(t2.equals(t1));
	}

	/**
	 * The equality between two triangles in the mesh can't be determined just by
	 * using the envelope of two triangles. this test presents a case where
	 * two triangles sharing an edge would be reported as equals if we performed
	 * our equality operation just by using the envelopes. But they are different...
	 */
	public void testTooCloseTrianglesEquality() throws DelaunayError{
		DEdge common = new DEdge(0,2,0,5,6,0);
		DTriangle t1 = new DTriangle(common, new DEdge(0,2,0,3,6,0), new DEdge(3,6,0,5,6,0));
		DTriangle t2 = new DTriangle(common, new DEdge(0,2,0,2,3,0), new DEdge(2,3,0,5,6,0));
		assertFalse(t1.equals(t2));
		assertTrue(t1.compareTo(t2)!=0);
		
	}

	/**
	 * tests that we effectively retrieve the leftmost point of a triangle.
	 * @throws DelaunayError
	 */
	public void testGetLeftMost() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,0,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t1 =new DTriangle(e1, e2, e3);
		DPoint left = t1.getLeftMost();
		assertTrue(left.equals(new DPoint(0,0,0)));
		p2 = new DPoint(0,3,0);
		e1 = new DEdge(p1,p2);
		e2 = new DEdge(p2,p3);
		e3 = new DEdge(p3,p1);
		t1 =new DTriangle(e3, e2, e1);
		left = t1.getLeftMost();
		assertTrue(left.equals(new DPoint(0,0,0)));
		p2 = new DPoint(-1,3,0);
		e1 = new DEdge(p1,p2);
		e2 = new DEdge(p2,p3);
		e3 = new DEdge(p3,p1);
		t1 =new DTriangle(e3, e2, e1);
		left = t1.getLeftMost();
		assertTrue(left.equals(new DPoint(-1,3,0)));

	}

	/**
	 * Tests that we retrieve the good edge when searching the edge opposed
	 * to a point in a triangle.
	 * @throws DelaunayError
	 */
	public void testGetOppositeEdge() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,0,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t1 =new DTriangle(e1, e2, e3);
		DEdge oppos = t1.getOppositeEdge(p1);
		assertTrue(oppos.equals(new DEdge(p2,p3)));
		oppos = t1.getOppositeEdge(p2);
		assertTrue(oppos.equals(new DEdge(p3,p1)));
		oppos = t1.getOppositeEdge(p3);
		assertTrue(oppos.equals(new DEdge(p1,p2)));
		oppos = t1.getOppositeEdge(new DPoint(8,8,8));
		assertNull(oppos);

	}

	/**
	 * Test that we know how to retrieve an edge from a triangle, given the two
	 * other ones that form it.
	 * @throws DelaunayError
	 */
	public void testGetLastEdge() throws DelaunayError{
		DPoint p1 = new DPoint(0,0,0);
		DPoint p2 = new DPoint(4,5,0);
		DPoint p3 = new DPoint(3,0,0);
		DEdge e1 = new DEdge(p1,p2);
		DEdge e2 = new DEdge(p2,p3);
		DEdge e3 = new DEdge(p3,p1);
		DTriangle t1 =new DTriangle(e1, e2, e3);
		DEdge last;
		last = t1.getLastEdge(e1, e2);
		assertTrue(last.equals(e3));
		last = t1.getLastEdge(e2, e1);
		assertTrue(last.equals(e3));
		last = t1.getLastEdge(e1, e3);
		assertTrue(last.equals(e2));
		last = t1.getLastEdge(e3, e1);
		assertTrue(last.equals(e2));
		last = t1.getLastEdge(e3, e2);
		assertTrue(last.equals(e1));
		last = t1.getLastEdge(e3, new DEdge(5,5,5,4,3,0));
		assertNull(last);
		last = t1.getLastEdge(new DEdge(5,5,5,4,3,0), e3);
		assertNull(last);
		last = t1.getLastEdge(new DEdge(5,5,5,4,3,0), new DEdge(9,8,7,6,5,4));
		assertNull(last);

	}

	/**
	 * Tests that we are able to retrieve triangle in a sorted list. The input points
	 * are the same that those used in TestConstrainedMesh.testManyConstraints().
	 * @throws DelaunayError
	 */
	public void testLargerTriangleSort() throws DelaunayError{
		List<DTriangle> triangleList = new ArrayList<DTriangle>();
		triangleList.add(new DTriangle(
			new DEdge(0,3,0,8,3,0),
			new DEdge(0,3,0,5,4,0),
			new DEdge(5,4,0,8,3,0)));
		triangleList.add(new DTriangle(
			new DEdge(0,3,0,8,3,0),
			new DEdge(8,3,0,4,1,0),
			new DEdge(4,1,0,0,3,0)));
		triangleList.add(new DTriangle(
			new DEdge(0,3,0,5,4,0),
			new DEdge(5,4,0,4,5,0),
			new DEdge(4,5,0,0,3,0)));
		triangleList.add(new DTriangle(
			new DEdge(0,3,0,12,12,0),
			new DEdge(12,12,0,4,5,0),
			new DEdge(4,5,0,0,3,0)));
		triangleList.add(new DTriangle(
			new DEdge(8,7,0,12,12,0),
			new DEdge(12,12,0,4,5,0),
			new DEdge(4,5,0,8,7,0)));
		triangleList.add(new DTriangle(
			new DEdge(8,7,0,5,4,0),
			new DEdge(5,4,0,4,5,0),
			new DEdge(4,5,0,8,7,0)));
		triangleList.add(new DTriangle(
			new DEdge(8,7,0,9,6,0),
			new DEdge(9,6,0,8,3,0),
			new DEdge(8,3,0,8,7,0)));
		triangleList.add(new DTriangle(
			new DEdge(8,7,0,5,4,0),
			new DEdge(5,4,0,8,3,0),
			new DEdge(8,3,0,8,7,0)));
		triangleList.add(new DTriangle(
			new DEdge(9,0,0,9,6,0),
			new DEdge(9,6,0,8,3,0),
			new DEdge(8,3,0,9,0,0)));
		triangleList.add(new DTriangle(
			new DEdge(9,0,0,9,6,0),
			new DEdge(9,6,0,10,3,0),
			new DEdge(10,3,0,9,0,0)));
		triangleList.add(new DTriangle(
			new DEdge(9,0,0,12,6,0),
			new DEdge(12,6,0,10,3,0),
			new DEdge(10,3,0,9,0,0)));
		triangleList.add(new DTriangle(
			new DEdge(9,6,0,12,6,0),
			new DEdge(12,6,0,10,3,0),
			new DEdge(10,3,0,9,6,0)));
		triangleList.add(new DTriangle(
			new DEdge(9,6,0,12,6,0),
			new DEdge(12,6,0,8,7,0),
			new DEdge(8,7,0,9,6,0)));
		triangleList.add(new DTriangle(
			new DEdge(12,7,0,12,6,0),
			new DEdge(12,6,0,8,7,0),
			new DEdge(8,7,0,12,7,0)));
		triangleList.add(new DTriangle(
			new DEdge(12,7,0,11,9,0),
			new DEdge(11,9,0,12,12,0),
			new DEdge(12,12,0,12,7,0)));
		triangleList.add(new DTriangle(
			new DEdge(12,12,0,11,9,0),
			new DEdge(11,9,0,8,7,0),
			new DEdge(8,7,0,12,12,0)));
		triangleList.add(new DTriangle(
			new DEdge(9,0,0,8,3,0),
			new DEdge(8,3,0,4,1,0),
			new DEdge(4,1,0,9,0,0)));
		triangleList.add(new DTriangle(
			new DEdge(11,9,0,8,7,0),
			new DEdge(12,7,0,11,9,0),
			new DEdge(8,7,0,12,7,0)));
		ArrayList<DTriangle> sorted = new ArrayList<DTriangle>(triangleList);
		Collections.sort(sorted);
		for(DTriangle tri : triangleList){
			assertTrue(Collections.binarySearch(sorted, tri)>=0);
		}
	}

	/**
	 * getEdgeFromPoints is supposed to retrieve a reference to an edge of the
	 * triangle, if the two points are coordinates that are used to define the triangle.
	 * @throws DelaunayError
	 */
	public void testGetEdgeFromPoints() throws DelaunayError {
		DEdge e1 = new DEdge(0,0,0,2,2,0);
		DEdge e2 = new DEdge(2,2,0,2,0,0);
		DEdge e3 = new DEdge(2,0,0,0,0,0);
		DTriangle dt = new DTriangle(e1, e2, e3);
		DEdge test = dt.getEdgeFromPoints(new DPoint(0,0,0), new DPoint(2,2,0));
		assertTrue(e1 == test);
		test = dt.getEdgeFromPoints(new DPoint(2,2,0), new DPoint(0,0,0));
		assertTrue(e1 == test);
		test = dt.getEdgeFromPoints(new DPoint(2,0,0), new DPoint(2,2,0));
		assertTrue(e2 == test);
		test = dt.getEdgeFromPoints(new DPoint(2,2,0), new DPoint(2,0,0));
		assertTrue(e2 == test);
		test = dt.getEdgeFromPoints(new DPoint(0,0,0), new DPoint(2,0,0));
		assertTrue(e3 == test);
		test = dt.getEdgeFromPoints(new DPoint(2,0,0), new DPoint(0,0,0));
		assertTrue(e3 == test);
		test = dt.getEdgeFromPoints(new DPoint(0,0,0), new DPoint(9,9,9));
		assertNull(test);
		test = dt.getEdgeFromPoints(new DPoint(3,4,0), new DPoint(9,9,9));
		assertNull(test);

	}

	public void testGetAlterPoint() throws DelaunayError {
		DEdge e1 = new DEdge(0,0,0,2,2,0);
		DEdge e2 = new DEdge(2,2,0,2,0,0);
		DEdge e3 = new DEdge(2,0,0,0,0,0);
		DTriangle dt = new DTriangle(e1, e2, e3);
		DPoint pt1 = new DPoint(0,0,0);
		DPoint pt2 = new DPoint(2,2,0);
		DPoint pt3 = new DPoint(2,0,0);
		DPoint pt = dt.getAlterPoint(pt1, pt2);
		assertTrue(pt.equals(pt3));
		pt = dt.getAlterPoint(pt2, pt1);
		assertTrue(pt.equals(pt3));
		pt = dt.getAlterPoint(pt2, pt3);
		assertTrue(pt.equals(pt1));
		pt = dt.getAlterPoint(pt3, pt2);
		assertTrue(pt.equals(pt1));
		pt = dt.getAlterPoint(pt3, pt1);
		assertTrue(pt.equals(pt2));
		pt = dt.getAlterPoint(pt1, pt3);
		assertTrue(pt.equals(pt2));
		pt = dt.getAlterPoint(new DPoint(4,4,4), pt3);
		assertNull(pt);
		pt = dt.getAlterPoint(pt3,new DPoint(4,4,4));
		assertNull(pt);
		pt = dt.getAlterPoint(pt1, pt1);
		assertNull(pt);
		pt = dt.getAlterPoint(pt2, pt2);
		assertNull(pt);
		pt = dt.getAlterPoint(pt3, pt3);
		assertNull(pt);
		pt = dt.getAlterPoint(new DPoint(4,4,4),new DPoint(4,4,4));
		assertNull(pt);
		pt = dt.getAlterPoint(new DPoint(4,5,4),new DPoint(4,4,4));
		assertNull(pt);
	}

	/**
	 *Tests that we retrieve correctly the points that form the given triangle.
	 * @throws DelaunayError
	 */
	public void testGetPoints() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0, 0, 0, 2, 2, 0), new DEdge(2, 2, 0, 1, 3, 0), new DEdge(1, 3, 0, 0, 0, 0));
		List<DPoint> points = tri.getPoints();
		assertTrue(points.contains(new DPoint(0,0,0)));
		assertTrue(points.contains(new DPoint(2,2,0)));
		assertTrue(points.contains(new DPoint(1,3,0)));
		assertTrue(points.size()==3);
	}

	/**
	 * Test that we retrieve a null value when trying to get an edge with number
	 * not 0, 1 or 2.
	 */
	 public void testGetEdgeNullValue() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0, 0, 0, 2, 2, 0), new DEdge(2, 2, 0, 1, 3, 0), new DEdge(1, 3, 0, 0, 0, 0));
		assertNull (tri.getEdge(4));
		assertNull (tri.getEdge(-1));
	 }

	 /**
	  * Test the method that retrieve the angle values at each point of the triangle.
	  * @throws DelaunayError
	  */
	 public void testAngle() throws DelaunayError {
		DTriangle tri = new DTriangle(new DEdge(0, 0, 0, 2, 2, 0), new DEdge(2, 2, 0, 0, 2, 0), new DEdge(0, 2, 0, 0, 0, 0));
		assertTrue(Math.abs(tri.getAngle(0)-45)<Tools.EPSILON);
		assertTrue(Math.abs(tri.getAngle(1)-45)<Tools.EPSILON);
		assertTrue(Math.abs(tri.getAngle(2)-90)<Tools.EPSILON);
	 }

	 /**
	  * Checks that we retrieve the good index when using getEdgeIndex
	  * @throws DelaunayError
	  */
	 public void testGetEdgeIndex() throws DelaunayError {
		DEdge e1 = new DEdge(0, 0, 0, 2, 2, 0);
		DEdge e2 = new DEdge(2, 2, 0, 0, 2, 0);
		DEdge e3 = new DEdge(0, 2, 0, 0, 0, 0);
		DTriangle tri = new DTriangle(e1, e2, e3);
		DEdge e4 = new DEdge(0, 0, 0, 2, 2, 0);
		int index = tri.getEdgeIndex(e4);
		assertTrue(tri.getEdges()[index].equals(e1));
		assertTrue(tri.getEdges()[index]==e1);
		assertTrue(tri.getEdges()[index]!=e4);
		e4 = new DEdge(8,8,8,9,9,9);
		assertTrue(tri.getEdgeIndex(e4)==-1);

	 }

	 public void testGetNormalVector() throws DelaunayError {
		DEdge e1 = new DEdge(0, 0, 0, 2, 2, 0);
		DEdge e2 = new DEdge(2, 2, 0, 0, 2, 0);
		DEdge e3 = new DEdge(0, 2, 0, 0, 0, 0);
		DTriangle tri = new DTriangle(e1, e2, e3);
		assertTrue(tri.getNormalVector().equals(new DPoint(0,0,1)));
		e1 = new DEdge(0, 0, 0, 2, 0, 2);
		e2 = new DEdge(2, 0 ,2, 0, 2, 0);
		e3 = new DEdge(0, 2, 0, 0, 0, 0);
		tri = new DTriangle(e1, e2, e3);
		assertTrue(tri.getNormalVector().equals(new DPoint(-1/Math.sqrt(2),0,1/Math.sqrt(2))));
		 
	 }

	 public void testGetSteepestVector() throws DelaunayError {
		DEdge e1 = new DEdge(0, 0, 0, 2, 2, 0);
		DEdge e2 = new DEdge(2, 2, 0, 0, 2, 0);
		DEdge e3 = new DEdge(0, 2, 0, 0, 0, 0);
		DTriangle tri = new DTriangle(e1, e2, e3);
		assertTrue(tri.getSteepestVector().equals(new DPoint(0,0,0)));
		e1 = new DEdge(0, 0, 0, 2, 0, 2);
		e2 = new DEdge(2, 0 ,2, 0, 2, 0);
		e3 = new DEdge(0, 2, 0, 0, 0, 0);
		tri = new DTriangle(e1, e2, e3);
		assertTrue(tri.getSteepestVector().equals(new DPoint(-1/Math.sqrt(2),0,-1/Math.sqrt(2))));

	 }

	 public void testGetSlope() throws DelaunayError {
		DEdge e1 = new DEdge(0, 0, 0, 2, 0, 2);
		DEdge e2 = new DEdge(2, 0 ,2, 0, 2, 0);
		DEdge e3 = new DEdge(0, 2, 0, 0, 0, 0);
		DTriangle tri = new DTriangle(e1, e2, e3);
		assertEquals(tri.getSlope(), -1, Tools.EPSILON);
		assertEquals(tri.getSlopeInDegree(), -45, Tools.EPSILON);
	 }
         
         public void testGetFlatSlope() throws DelaunayError {
		DEdge e1 = new DEdge(0, 0, 0, 2, 0, 0);
		DEdge e2 = new DEdge(2, 0 ,0, 0, 2, 0);
		DEdge e3 = new DEdge(0, 2, 0, 0, 0, 0);
		DTriangle tri = new DTriangle(e1, e2, e3);
		assertEquals(tri.getSlope(), 0, Tools.EPSILON);
	 }


	 public void testCounterSlope() throws DelaunayError {
		DEdge e1 = new DEdge(0, 0, 0, 4,0,0);
		DEdge e2 = new DEdge(4,0,0, 2,4,10);
		DEdge e3 = new DEdge(2,4,10, 0, 0, 0);
		DTriangle tri = new DTriangle(e1, e2, e3);
		assertEquals(tri.getCounterSteepestIntersection(new DPoint(1,0,0)), new DPoint(1,2,5));
		assertEquals(tri.getCounterSteepestIntersection(new DPoint(3,0,0)), new DPoint(3,2,5));
		assertEquals(tri.getCounterSteepestIntersection(new DPoint(2,0,0)), new DPoint(2,4,10));
		assertNull(tri.getCounterSteepestIntersection(new DPoint(8,8,8)));
		assertEquals(tri.getCounterSteepestIntersection(new DPoint(0,0,0)), new DPoint(0,0,0));
		assertEquals(tri.getCounterSteepestIntersection(new DPoint(4,0,0)), new DPoint(4,0,0));
		assertEquals(tri.getCounterSteepestIntersection(new DPoint(1,2,5)), new DPoint(1,2,5));
	 }
         
         public void testCenter() throws DelaunayError {
                 DTriangle dt = new DTriangle(new DEdge(3,1,0,8,3,0), 
                         new DEdge(8,3,0,5,0,0), 
                         new DEdge(5,0,0,3,1,0));
                 double x = 15.5/3;
                 double y = -x+8;
                 assertEquals(dt.getCircumCenter().getX(), x, 0.00001);
                 assertEquals(dt.getCircumCenter().getY(), y, 0.00001);
                 dt = new DTriangle( 
                         new DEdge(8,3,0,5,0,0), 
                         new DEdge(5,0,0,3,1,0),
                         new DEdge(3,1,0,8,3,0));
                 assertEquals(dt.getCircumCenter().getX(), x, 0.00001);
                 assertEquals(dt.getCircumCenter().getY(), y, 0.00001);
                 dt = new DTriangle(  
                         new DEdge(5,0,0,3,1,0),
                         new DEdge(3,1,0,8,3,0),
                         new DEdge(8,3,0,5,0,0));
                 assertEquals(dt.getCircumCenter().getX(), x, 0.00001);
                 assertEquals(dt.getCircumCenter().getY(), y, 0.00001);
         }
        
         /**
          * test the algorithm that search a point in the mesh, knowing a starting
          * triangle.
          * The configuration here is the same as in testOneConstraintFourPointsExtended.
          * (in ConstrainedMesh)
          * @throws DelaunayError 
          */
        public void testFindCircumCenterContainer() throws DelaunayError {
		ConstrainedMesh mesh = new ConstrainedMesh();
		mesh.addConstraintEdge(new DEdge(0,3,0,8,3,0));
		mesh.addConstraintEdge(new DEdge(10,0,0,10,6,0));
		mesh.addPoint(new DPoint(3,1,0));
		mesh.addPoint(new DPoint(5,0,0));
		mesh.addPoint(new DPoint(4,5,0));
		mesh.addPoint(new DPoint(6,4,0));
		mesh.processDelaunay();
                int indice = mesh.getTriangleList().indexOf(new DTriangle(new DEdge(3,1,0,5,0,0),
                        new DEdge(5,0,0,8,3,0), new DEdge(8,3,0,3,1,0)));
                assertTrue(indice>=0);
                DTriangle ref = mesh.getTriangleList().get(indice);
                DTriangle container = (DTriangle) ref.getCircumCenterContainer();
                assertEquals(new DTriangle(
                        new DEdge(0,3,0,3,1,0),
                        new DEdge(3,1,0,8,3,0),
                        new DEdge(8,3,0,0,3,0)), container);
                indice = mesh.getTriangleList().indexOf(new DTriangle(new DEdge(8,3,0,10,6,0),
                        new DEdge(10,6,0,10,0,0), new DEdge(10,0,0,8,3,0)));
                assertTrue(indice>=0);
                ref = mesh.getTriangleList().get(indice);
                DEdge ed = (DEdge) ref.getCircumCenterContainer();
                assertTrue(ed.equals(new DEdge(10,0,0,10,6,0)));
        }
        
        public void testFindingCCContainerManyConstrains() throws DelaunayError {
		ConstrainedMesh mesh = new ConstrainedMesh();
		DEdge constr = new DEdge(0,3,0,8,3,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(9,0,0,9,6,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(12,6,0,8,7,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(5,4,0,8,7,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(12,6,0,12,7,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(8,3,0,9,6,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(8,7,0,12,12,0);
		mesh.addConstraintEdge(constr);
		mesh.addPoint(new DPoint(4,5,0));
		mesh.addPoint(new DPoint(4,1,0));
		mesh.addPoint(new DPoint(10,3,0));
		mesh.addPoint(new DPoint(11,9,0));
		mesh.processDelaunay();
                int indice = mesh.getTriangleList().indexOf(new DTriangle(
                        new DEdge(8,7,0,11,9,0),
                        new DEdge(11,9,0,12,12,0), 
                        new DEdge(12,12,0,8,7,0)));
                assertTrue(indice>=0);
                DTriangle ref = mesh.getTriangleList().get(indice);
                DEdge container = (DEdge) ref.getCircumCenterContainer();
                assertTrue(container.equals(new DEdge(0,3,0,12,12,0)));
        }
        
        /**
         * search the triangle that contains a point (that is a priori not a 
         * circum center) in the mesh from testManyConstraints
         * @throws DelaunayError 
         */
        public void testSearchPoint() throws DelaunayError {
		ConstrainedMesh mesh = new ConstrainedMesh();
		DEdge constr = new DEdge(0,3,0,8,3,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(9,0,0,9,6,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(12,6,0,8,7,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(5,4,0,8,7,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(12,6,0,12,7,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(8,3,0,9,6,0);
		mesh.addConstraintEdge(constr);
		constr = new DEdge(8,7,0,12,12,0);
		mesh.addConstraintEdge(constr);
		mesh.addPoint(new DPoint(4,5,0));
		mesh.addPoint(new DPoint(4,1,0));
		mesh.addPoint(new DPoint(10,3,0));
		mesh.addPoint(new DPoint(11,9,0));
		mesh.processDelaunay();
                int indice = mesh.getTriangleList().indexOf(new DTriangle(
                        new DEdge(8,7,0,11,9,0),
                        new DEdge(11,9,0,12,12,0), 
                        new DEdge(12,12,0,8,7,0)));
                assertTrue(indice>=0);
                DTriangle ref = mesh.getTriangleList().get(indice);
                DTriangle container = (DTriangle) ref.searchPointContainer(new DPoint(7,4,0));
                assertEquals(new DTriangle(
                        new DEdge(8,3,0,8,7,0),
                        new DEdge(8,7,0,5,4,0),
                        new DEdge(5,4,0,8,3,0)), container);
                DEdge ed =  (DEdge) ref.searchPointContainer(new DPoint(4,12,0));
                assertTrue(ed.equals(new DEdge(0,3,0,12,12,0)));
        }
        
        public void testSteepestIntersection() throws DelaunayError {
                DTriangle dt = new DTriangle (
                        new DEdge(0,0,0,4,0,0),
                        new DEdge(4,0,0,2,2,10),
                        new DEdge(2,2,10,0,0,0));
                DPoint pt = dt.getSteepestIntersectionPoint(new DPoint(2,2,10));
                assertTrue(pt.equals(new DPoint(2,0,0)));
                assertNull(dt.getSteepestIntersectionPoint(new DPoint(50,50,50)));
        }
        
        public void testSlope() throws DelaunayError {
                DTriangle dt = new DTriangle (
                        new DEdge(0,0,0,4,0,0),
                        new DEdge(4,0,0,2,2,10),
                        new DEdge(2,2,10,0,0,0));
                double slop = dt.getSlopeInPercent();
                assertEquals(slop,500,0.01);
                slop = dt.getSlope();
                assertEquals(slop,-5,0.01);
                slop = dt.getSlopeInDegree();
                assertEquals(slop,-78.69,0.01);
                
        }
        
        public void testSlopeAspect() throws DelaunayError {
                DTriangle dt = new DTriangle (
                        new DEdge(0,0,0,4,0,0),
                        new DEdge(4,0,0,2,2,10),
                        new DEdge(2,2,10,0,0,0));
                double asp = dt.getSlopeAspect();
                assertEquals(asp,180, 0.01);
        }

        public void testGetMaxAngle() throws DelaunayError {
                DTriangle dt = new DTriangle(new DEdge(0,0,0,0,4,0),
                                new DEdge(0,4,0,4,0,0),
                                new DEdge(4,0,0,0,0,0));
                double ma = dt.getMaxAngle();
                assertEquals(ma, 90, 0.01); 
        }
        
        public void testSharedByTwoEdges() throws DelaunayError {
                DTriangle dt = new DTriangle(new DEdge(0,0,0,0,4,0),
                                new DEdge(0,4,0,4,0,0),
                                new DEdge(4,0,0,0,0,0));
                assertTrue(dt.sharedByTwoEdge(new DPoint(0,0,0)));
                assertTrue(dt.sharedByTwoEdge(new DPoint(4,0,0)));
                assertTrue(dt.sharedByTwoEdge(new DPoint(0,4,0)));
                assertFalse(dt.sharedByTwoEdge(new DPoint(5,4,0)));
                
        }
        
        public void testCheckTopology() throws DelaunayError {
                DTriangle dt = new DTriangle(new DEdge(0,0,0,0,4,0),
                                new DEdge(0,4,0,4,0,0),
                                new DEdge(4,0,0,0,0,0));
                DEdge ed = new DEdge(50,50,5, 4,4,4);
                dt.setEdge(0, ed);
                assertFalse(dt.checkTopology());
                ed.setLeft(dt);
                assertFalse(dt.checkTopology());
        }
        
        public void testTopoOrientedExc() throws DelaunayError {
                DTriangle dt = new DTriangle(new DEdge(0,0,0,0,4,0),
                                new DEdge(0,4,0,4,0,0),
                                new DEdge(4,0,0,0,0,0));
                DEdge ed = new DEdge(50,50,50,51,51,51);
                try{
                        dt.isTopoOrientedToEdge(ed);
                        assertFalse(false);
                } catch(DelaunayError d){
                        assertTrue(true);
                }
        }
        
        public void testGetContaingEdge() throws DelaunayError {
                DTriangle dt = new DTriangle(new DEdge(0,0,0,0,4,0),
                                new DEdge(0,4,0,4,0,0),
                                new DEdge(4,0,0,0,0,0));
                assertEquals(new DEdge(0,0,0,0,4,0), dt.getContainingEdge(new DPoint(0,2,0)));
                assertEquals(new DEdge(0,0,0,4,0,0), dt.getContainingEdge(new DPoint(2,0,0)));
                assertEquals(new DEdge(4,0,0,0,4,0), dt.getContainingEdge(new DPoint(2,2,0)));
                assertNull(dt.getContainingEdge(new DPoint(50,50,50)));
                
                
        }
        
        public void testIsEdgeOf() throws DelaunayError {
                DTriangle dt = new DTriangle(new DEdge(0,0,0,0,4,0),
                                new DEdge(0,4,0,4,0,0),
                                new DEdge(4,0,0,0,0,0));
                assertTrue(dt.isEdgeOf(new DEdge(0,0,0,0,4,0)));
                assertTrue(dt.isEdgeOf(new DEdge(4,0,0,0,4,0)));
                assertTrue(dt.isEdgeOf(new DEdge(0,0,0,4,0,0)));
                assertFalse(dt.isEdgeOf(new DEdge(0,6,0,4,0,0)));
        }
        
        public void testMinDistance() throws DelaunayError {
                DTriangle dt = new DTriangle(
                        new DEdge(0,2,0,4,0,0), 
                        new DEdge(4,0,0,2,7,0), 
                        new DEdge(2,7,0,0,2,0));
                assertEquals(dt.getMinSquareDistance(new DPoint(1,2,0)),1.0);
                assertEquals(dt.getMinSquareDistance(new DPoint(2,6,0)),1.0);
                assertEquals(dt.getMinSquareDistance(new DPoint(3,1,0)),2.0);
        }
        
        public void testIsCloser() throws DelaunayError {
                DTriangle dt = new DTriangle(
                        new DEdge(0,2,0,4,0,0), 
                        new DEdge(4,0,0,2,7,0), 
                        new DEdge(2,7,0,0,2,0));
                assertTrue(dt.isCloser(new DPoint(1,2,0),2));
                assertTrue(dt.isCloser(new DPoint(2,6,0),2));
                assertTrue(dt.isCloser(new DPoint(3,1,0),2));
                assertFalse(dt.isCloser(new DPoint(1,2,0),0.5));
                assertFalse(dt.isCloser(new DPoint(2,6,0),0.5));
                assertFalse(dt.isCloser(new DPoint(3,1,0),0.5));
        }
        
        public void testForceCoherence() throws DelaunayError {
                DEdge ed = new DEdge(0,2,0,4,0,0);
                DTriangle dt = new DTriangle(
                        ed, 
                        new DEdge(4,0,0,2,7,0), 
                        new DEdge(2,7,0,0,2,0));
                ed.setLeft(new DTriangle(ed, 
                        new DEdge(4,0,0,2,7,0), 
                        new DEdge(2,7,0,0,2,0)));
                assertFalse(ed.getLeft()==dt);
                dt.forceCoherenceWithEdges();
                assertTrue(ed.getLeft()==dt);
        }
        
        /**
         * We test that we are able to retrieve the minimal angle of a triangle
         * @throws org.jdelaunay.delaunay.error.DelaunayError
         */
        public void testGetMinAngle() throws DelaunayError {
                DTriangle tri = new DTriangle(new DEdge(0,0,0,0,2,0), new DEdge(0,2,0,5,0,0), new DEdge(5,0,0,0,0,0));
                double min = tri.getMinAngle();
                assertEquals(21.805, min, 0.01);
        }
        
        /**
         * We must be able to find the point containers only when we don't intersect any constraint.
         * @throws org.jdelaunay.delaunay.error.DelaunayError
         */
        public void testFindCircumCenterContainerSafe() throws DelaunayError {
                ConstrainedMesh mesh = new ConstrainedMesh();
                mesh.addPoint(new DPoint(0,3,0));
                mesh.addPoint(new DPoint(4,3,0));
                mesh.addConstraintEdge(new DEdge(new DPoint(2,0,0), new DPoint(2,6,0)));
                mesh.processDelaunay();
                int index = mesh.getTriangleList().indexOf(new DTriangle(new DPoint(0,3,0), new DPoint(2,6,0),new DPoint(2,0,0)));
                DTriangle tri = mesh.getTriangleList().get(index);
                assertNull(tri.getCircumCenterContainerSafe());
                mesh = new ConstrainedMesh();
                mesh.addPoint(new DPoint(0,3,0));
                mesh.addPoint(new DPoint(4,3,0));
                mesh.addConstraintEdge(new DEdge(new DPoint(2,0,0), new DPoint(2,6,0)));
                mesh.processDelaunay();
                index = mesh.getTriangleList().indexOf(new DTriangle(new DPoint(0,3,0), new DPoint(2,6,0),new DPoint(2,0,0)));
                tri = mesh.getTriangleList().get(index);
                mesh.getConstraintEdges().get(0).swap();
                assertNull(tri.getCircumCenterContainerSafe());
        }
        
        public void testIsProcessed() throws DelaunayError {
                DTriangle dt = new DTriangle(new DPoint(0,0,0), new DPoint(5,3,0), new DPoint(8,7,0));
                assertFalse(dt.isProcessed());
                dt.setProcessed(true);
                assertTrue(dt.isProcessed());
                dt.setProcessed(false);
                assertFalse(dt.isProcessed());
        }
        
        public void test3DArea() throws DelaunayError {
                DTriangle dt = new DTriangle(new DPoint(0,0,0), new DPoint(4,0,0), new DPoint(3,2,0));
                assertEquals(4, dt.getArea3D(), 0);
        }
}
