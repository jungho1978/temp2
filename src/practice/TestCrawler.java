package practice;

import com.android.ddmlib.AndroidDebugBridge;
import com.android.ddmlib.IDevice;

public class TestCrawler {
	private static String sAdbLocation = null;
	static {
		sAdbLocation = isWindow() ? "adb" : "/Users/jungho/Library/Android/sdk/platform-tools/adb";
	}
	
	private static boolean isWindow() {
		return System.getProperty("os.name").startsWith("Windows");
	}
	
	public static void main(String[] args) throws InterruptedException {
		AndroidDebugBridge.init(false);
		AndroidDebugBridge bridge = AndroidDebugBridge.createBridge(sAdbLocation, true);
		int retryCnt = 5;
		do {
			Thread.sleep(500);
			System.out.println("Waiting for device...");
		} while (--retryCnt > 0 && bridge.getDevices().length == 0);
		
		if (bridge.getDevices().length == 0) {
			System.out.println("No device found");
			return;
		}
		
		IDevice device = bridge.getDevices()[0];
		System.out.println(device.getName());
	}
}
