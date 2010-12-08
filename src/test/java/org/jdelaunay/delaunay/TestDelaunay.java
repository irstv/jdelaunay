package org.jdelaunay.delaunay;

import java.util.ArrayList;
import java.util.ListIterator;

public class TestDelaunay extends BaseUtility {
	/**
	 * Test random generation of points
	 * @throws DelaunayError
	 */
	public void testDelaunayRandomPoints() throws DelaunayError {
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);
		aMesh.setMax(1300, 700);
		aMesh.setRandomPoints(10000);
		
		long start = System.currentTimeMillis();
		
		aMesh.processDelaunay();
		
		long end = System.currentTimeMillis();
		System.out.println("Duration " + (end-start)+"ms");
		assertTrue(true);
		System.out.println("end");
	}

	/**
	 * Use identified set of points
	 * @throws DelaunayError
	 */
	public void testDelaunayPoints() throws DelaunayError {
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);
		aMesh.setPoints(getPoints());

		aMesh.processDelaunay();

//		show(aMesh);
		System.out.println();
		assertTrue(true);
	}

	/**
	 * test GIDs
	 * Check GIDs for Point, Edges and Triangles
	 * GIDs must exist for each element (GID >= 0).
	 * GIDs are unique for each kind of element.
	 *
	 * @throws DelaunayError
	 */
	public void testGIDS() throws DelaunayError {
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);
		aMesh.setPoints(getPoints());
		aMesh.processDelaunay();
		
		assertGIDUnicity(aMesh);
	}

	/**
	 * Test points at the same location in 3D
	 * Use a predefined set of points and add the first one
	 * The final set of points must be decremented by 1
	 * 
	 * @throws DelaunayError
	 */
	public void testDelaunayDuplicateXYZPoint() throws DelaunayError {
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);
		
		ArrayList<Point> pts = getPoints();
		Point addedPoint = new Point(pts.get(1));
		pts.add(addedPoint);
		int PtsSize = pts.size();
		
		aMesh.setPoints(pts);
		aMesh.processDelaunay();

		assertTrue(aMesh.getNbPoints() == (PtsSize - 1));
	}

	/**
	 * Test points at the same location in 2D
	 * Use a predefined set of points and add the first one
	 * The final set of points must be decremented by 1
	 * @throws DelaunayError
	 */
	public void testDelaunayDuplicateXYPoint() throws DelaunayError {
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);
		
		ArrayList<Point> pts = getPoints();
		Point addedPoint = new Point(pts.get(1));
		addedPoint.setZ(addedPoint.getZ() + 10);
		pts.add(addedPoint);
		int PtsSize = pts.size();
		
		aMesh.setPoints(pts);
		aMesh.processDelaunay();

		assertTrue(aMesh.getNbPoints() == (PtsSize - 1));
	}

	/**
	 * Check coherence
	 * An edge is made of 2 different points
	 * A DelaunayTriangle is made of 3 different edges
	 * @throws DelaunayError
	 */
	public void testCoherence() throws DelaunayError {
		// Generate Mesh
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);

		aMesh.setMax(1300, 700);
		aMesh.setRandomPoints(5000);

		// process triangularization
		aMesh.processDelaunay();

		// Assert edges correctly defined
		assertCoherence(aMesh);
	}

	/**
	 * Check if each point and each edge is used at least once
	 * Check that each point belongs to an edge
	 * Check that each edge belongs to a triangle
	 * @throws DelaunayError
	 */
	public void testUseEachElement() throws DelaunayError {
		// Generate Mesh
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);

		aMesh.setMax(1300, 700);
		aMesh.setRandomPoints(5000);

		// process triangularization
		aMesh.processDelaunay();

		// Assert
		assertUseEachPoint(aMesh);
		assertUseEachEdge(aMesh);
	}

	/**
	 * Check if 2 points are linked by two different edges 
	 * @throws DelaunayError
	 */
	public void testDelaunayDupplicateEdges() throws DelaunayError {
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);

		aMesh.setMax(1300, 700);
		aMesh.setRandomPoints(5000);

		// process triangularization
		aMesh.processDelaunay();

		boolean correct = true;
		ArrayList<Edge> edgeList = aMesh.getEdges();
		
		for (Edge anEdge:edgeList) {
			Edge myEdge;
			Point start = anEdge.getStart();
			Point end = anEdge.getEnd();
			
			ListIterator<Edge> iterEdge = edgeList.listIterator();
			while ((correct) && (iterEdge.hasNext())) {
				myEdge = iterEdge.next();
				if (anEdge != myEdge) {
					if ((start == myEdge.getStart()) && (end == myEdge.getEnd())) {
						correct = false;
					} else if ((end == myEdge.getStart()) && (start == myEdge.getEnd())) {
						correct = false;
					}
				}
			}
			assertTrue(correct);
		}
	}

	/**
	 * Refine Mesh - test triangles area
	 * @throws DelaunayError
	 */
	public void testDelaunayPointsRefinementMaxArea() throws DelaunayError {
		MyMesh aMesh = new MyMesh();
		aMesh.setPrecision(1.0e-3);
		aMesh.setVerbose(true);
		
		aMesh.setPoints(getPoints());
		
		aMesh.processDelaunay();
		
		aMesh.setMaxArea(1000);
		assertTrue(aMesh.getMaxArea() == 1000);
		
		aMesh.setRefinment(MyMesh.REFINEMENT_MAX_AREA);
		aMesh.refineMesh();
//		show(aMesh);

		for (DelaunayTriangle myTriangle : aMesh.getTriangles()) {
			if (myTriangle.computeArea() > 1000) {
				assertTrue(false);
			}
		}
		System.out.println("finish");
	}
}
