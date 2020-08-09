package tests;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;

interface Callback {
	public void onSomeEvent(String foo);
}

public class TestClass {
	public static void main(String[] args) {
		fWithCallback(new Callback() {
			@Override
			public void onSomeEvent(String foo) {
				System.out.println(new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss").format(Calendar.getInstance().getTime()));
				System.out.println(Instant.now().toString());
				System.out.println(OffsetDateTime.now(ZoneOffset.UTC));
				System.out.println(String.valueOf(OffsetDateTime.now(ZoneOffset.UTC).getHour() - Calendar.getInstance().get(Calendar.HOUR_OF_DAY)));
			}
			
		});
	}
	
	public static void fWithCallback(Callback cb) {
		cb.onSomeEvent("value");
	}
}
