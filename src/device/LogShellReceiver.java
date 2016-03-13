package device;

import com.android.ddmlib.MultiLineReceiver;

public class LogShellReceiver extends MultiLineReceiver {
	private static final String TAG = "[LOG/SHELL]";

	@Override
	public boolean isCancelled() {
		return false;
	}

	@Override
	public void processNewLines(String[] lines) {
		for (String line : lines) {
			System.out.println(TAG + "line");
		}
	}

}
