package tests;

interface Callback {
	public void onSomeEvent(String foo);
}

public class TestClass {
	public static void main(String[] args) {
		fWithCallback(new Callback() {
			@Override
			public void onSomeEvent(String foo) {
				System.out.println("event listened " + foo);
			}
			
		});
	}
	
	public static void fWithCallback(Callback cb) {
		cb.onSomeEvent("value");
	}
}
