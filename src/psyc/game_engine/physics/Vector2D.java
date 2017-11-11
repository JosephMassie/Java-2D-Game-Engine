package psyc.game_engine.physics;

public class Vector2D {
	
	public double x;
	public double y;
	
	public Vector2D(double a_x, double a_y) {
		x = a_x;
		y = a_y;
	}
	
	// Multiply this vector by the given value and return the resulting vector
	public Vector2D multiply(double delta) {
		double nX = x * delta,
				nY = y * delta;
		
		return new Vector2D(nX, nY);
	}
	
	// Add given vector to this vector and return the resulting vector
	public Vector2D addVector(Vector2D a_vec) {
		double nX = x + a_vec.x,
				nY = y + a_vec.y;
		
		return new Vector2D(nX, nY);
	}
	
	// Subtract given vector from this vector and return the resulting vector
	public Vector2D subtractVector(Vector2D a_vec) {
		return addVector(a_vec.multiply(-1.0));
	}
	
	public double dotProduct(Vector2D a_vec) {
		return getMagnitude() * a_vec.getMagnitude() * Math.cos(0.0);
	}
	
	public double getMagnitude() {
		return Math.sqrt(x * x + y * y);
	}
}
