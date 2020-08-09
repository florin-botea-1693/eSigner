package tests;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

import org.json.JSONObject;

interface Callback {
	public void onSomeEvent(String foo);
}

public class TestClass {
	public static void main(String[] args) {
		fWithCallback(new Callback() {
			@Override
			public void onSomeEvent(String foo) {
				System.out.println(new JSONObject("{'foo': 1}").get("foo"));
			}
			
		});
	}
	
	public static void fWithCallback(Callback cb) {
		cb.onSomeEvent("value");
	}
}
