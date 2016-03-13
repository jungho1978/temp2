package device;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.android.ddmlib.AdbCommandRejectedException;
import com.android.ddmlib.IDevice;
import com.android.ddmlib.ShellCommandUnresponsiveException;
import com.android.ddmlib.SyncException;
import com.android.ddmlib.TimeoutException;
import com.google.common.base.Preconditions;
import com.google.common.util.concurrent.ThreadFactoryBuilder;

public class Device {
	private static final int PORT_UI_AUTOMATOR = 4724;
	
	private String adbLocation;
	private IDevice device;
	
	private final ExecutorService uiAutomatorExecutor = createExecutor("uiautomator");
	
	public Device(String adbLocation, IDevice device) {
		this.adbLocation = adbLocation;
		this.device = device;
	}
	
	private JsonWire openUiAutomator(/* params needed */) {
		uiAutomatorExecutor.execute(new Runnable() {
			private static final String UIAUTOMATOR_JAR_PATH = "/install/uiautomator.jar";
			@Override
			public void run() {
				try {
					device.pushFile(UIAUTOMATOR_JAR_PATH, "/sdcard/uiautomator.jar");
					device.executeShellCommand("/system/bin/uiautomator runtest /sdcard/uiautomator.jar",
							new LogShellReceiver());
				} catch (SyncException | IOException | AdbCommandRejectedException | TimeoutException | ShellCommandUnresponsiveException e) {
					throw new RuntimeException(e);
				}
			}
		});
		
		try {
			device.createForward(PORT_UI_AUTOMATOR, PORT_UI_AUTOMATOR);
		} catch (TimeoutException | AdbCommandRejectedException | IOException e) {
			throw new RuntimeException(e);
		}
		return openWire(PORT_UI_AUTOMATOR);
	}
	
	private JsonWire openWire(int port) {
		long timeout = now() + (60 * 1000);
		while (now() < timeout) {
			try {
				Thread.sleep(500);
				return connectTo(port);
			} catch (InterruptedException | IOException e) {
				e.printStackTrace();
			}
		}
		throw new IllegalStateException("Cannot connect to toolkit");
	}
	
	private JsonWire connectTo(int port) throws UnknownHostException, IOException {
		JsonWire wire = new JsonWire(new Socket("localhost", port));
		String response = wire.sendCommand("ping");
		Preconditions.checkArgument(response.equals("poing"));
		return wire;
	}
	
	private static ExecutorService createExecutor(String name) {
		ThreadFactory threadFactory = new ThreadFactoryBuilder()
				.setNameFormat(name)
				.setUncaughtExceptionHandler(null)	// TODO: register exception handler
				.build();
		
		return Executors.newSingleThreadExecutor(threadFactory);
	}
	
	private static long now() {
		return System.currentTimeMillis();
	}
}
