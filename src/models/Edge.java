package models;

public class Edge {
	// Represnts an edge on a graph
	public final int from, to;

	public Edge(int from, int to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Edge))
			return false;

		int oFrom = ((Edge) o).from;
		int oTo = ((Edge) o).to;

		return (from == oFrom) && (to == oTo);
	}

	@Override
	public int hashCode() {
		return from < to ? to * to + from : from * from + from + to;
	}

	@Override
	public String toString() {
		return from + "--" + to;
	}
}
