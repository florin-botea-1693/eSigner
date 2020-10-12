package utils;

public class ForceBetween {
	public static int number(int num, int min, int max) {
		int result = num > max ? max : num;
		result = result < min ? min : result;
		return result;
	}
	
	public static float number(float num, float min, float max) {
		float result = num > max ? max : num;
		result = result < min ? min : result;
		return result;
	}
}
