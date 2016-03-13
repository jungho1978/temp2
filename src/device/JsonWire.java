package device;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonWire implements Closeable {
	private final Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;

	public JsonWire(Socket socket) throws IOException {
		this.socket = socket;
		this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.writer = new PrintWriter(socket.getOutputStream(), true);
	}

	public String sendCommand(String cmd, String params) {
		sendRequest(cmd, params);
		return readResponse();	// ???
	}
	
	public String sendCommand(String cmd) {
		sendRequest(cmd, null);
		return readResponse();
	}

	private void sendRequest(String action, String params) {
		HashMap<String, String> request = new HashMap<String, String>();
		request.put("cmd", "action");
		request.put("action", action);
		if (params != null) {
			request.put("params", params);
		}
		writer.println(request);
		writer.flush();
	}
	
	private String readResponse() {
		try {
			String json = reader.readLine();
			JSONObject response = new JSONObject(json);
			if (response.getInt("status") != 0) {
				throw new RuntimeException("Response: " + json);
			} else {
				return response.getString("value");
			}
		} catch (IOException e) {
			throw new RuntimeException("Cannot read response: " + e);
		} catch (JSONException e) {
			throw new RuntimeException("Cannot parse json response: " + e);
		}
	}

	@Override
	public void close() throws IOException {
		reader.close();
		writer.close();
		socket.close();
	}

}
