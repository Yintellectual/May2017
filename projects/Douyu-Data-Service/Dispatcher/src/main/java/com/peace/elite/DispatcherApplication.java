package com.peace.elite;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.peace.elite.entities.DouyuMessageType;
import com.peace.elite.redisMQ.RedisMQ;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.CommandLineRunner;
import java.util.*;

import java.util.regex.*;
import java.time.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.logging.*;
import java.util.concurrent.atomic.AtomicBoolean;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Controller
@SpringBootApplication
public class DispatcherApplication {

	public static final int ROOM_ID = 2020877;
	private static final Logger LOGGER = Logger.getLogger(DispatcherApplication.class.getName());
	public Instant lastHeartBeat = Instant.now();
	@Autowired
	SocketContainer sc;
	@Autowired
	private SimpMessagingTemplate webSocket;
	@Autowired
	private RedisMQ redisMQ;

	public static void main(String[] args) {
		SpringApplication.run(DispatcherApplication.class, args);
	}

	private SocketAddress address;
	/*
	 * @ResponseBody
	 * 
	 * @RequestMapping(name="/greeting") public String greeting(RestTemplate
	 * restTemplate) throws Exception{ //String result = new String ( //
	 * restTemplate.getForObject(
	 * "http://api.live.bilibili.com/gift/getTop?roomid=159586&_=1493738109671",
	 * String.class) // , "GBK");
	 * 
	 * //String result = restTemplate.getForObject(
	 * "http://api.live.bilibili.com/gift/getTop?roomid=1", String.class);
	 * String url = "";
	 * 
	 * return "dada";//restTemplate.getForObject(url, String.class); }
	 */

	@Bean
	public RedisTemplate<String, String> redisTemplate() {
		RedisTemplate<String, String> template = new RedisTemplate<>();
		// start redis with --raw, and cmd /K chcp 65001
		template.setDefaultSerializer(new StringRedisSerializer());
		template.setConnectionFactory(jedisConnectionFactory());
		return template;
	}

	@Bean
	JedisConnectionFactory jedisConnectionFactory() {
		JedisConnectionFactory jedisConFactory = new JedisConnectionFactory();
		jedisConFactory.setHostName("localhost");
		jedisConFactory.setPort(6379);
		return jedisConFactory;
	}

	@Bean
	public SocketContainer sc() {
		SocketContainer sc = new SocketContainer(socket());
		sc.connect();
		return sc;
	}

	public class SocketContainer {
		private AtomicBoolean isConnected = new AtomicBoolean();
		private Socket clientSocket;

		public SocketContainer(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		public void setClientSocket(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		public Socket getClientSocket() {
			return clientSocket;
		}

		public boolean isConnected() {
			return isConnected.get();
		}

		public void connect() {
			int roomid = ROOM_ID;
			try {
				// if(!isConnected(clientSocket)){
				clientSocket.connect(address);
				// }
				System.out.println("a");
				// Send the filename via the connection
				OutputStream oS = clientSocket.getOutputStream();
				// oS.write(applyDouYuProtocol("type@=loginreq/roomid@="+"ROOM_ID"+"/"));
				System.out.println("b");
				oS.write(applyDouYuProtocol("type@=loginreq/username@=/ct@=0/password@=/roomid@=" + ROOM_ID
						+ "/devid@=/rt@=" + new Date().getTime() / 1000
						+ "/vk@=/ver@=20150929/aver@=2017060901/ltkid@=/biz@=1/stk@=/"));
				System.out.println("c");
				oS.write(applyDouYuProtocol("type@=joingroup/rid@=" + ROOM_ID + "/gid@=-9999/"));
				System.out.println("d");
				setConnected();
				heartBeatThread(clientSocket);
				printerThread(clientSocket);
			} catch (Exception e) {
				Socket socket = new Socket();
				setClientSocket(socket);
				System.out.println("--Reconnect in 10s");
				try {
					Thread.sleep(10000);
				} catch (Exception ex) {
				}
				connect();
			}
		}

		// return true if this thread is the first one doing so
		public boolean setConnected() {
			return isConnected.compareAndSet(false, true);
		}

		// return true if this thread is the first one doing so
		public boolean setDisconnected() {
			return isConnected.compareAndSet(true, false);
		}
	}

	public Socket socket() {
		Socket result = new Socket();
		// declare a new Socket object and specify the host name and the port
		// number
		try {
			Socket clientSocket = new Socket("openbarrage.douyutv.com", 8601);
			address = clientSocket.getRemoteSocketAddress();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			try {
				Thread.sleep(1000);
			} catch (Exception ex) {
			}
			return socket();
		}
	}

	public class SendHeartBeat implements Runnable {
		private Socket clientSocket;

		public SendHeartBeat(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		public void run() {
			// 目前后台设置为 45 秒
			try {
				while (true) {
					Thread.sleep(30 * 1000);
					System.gc();
					clientSocket.getOutputStream()
							.write(applyDouYuProtocol("type@=keeplive/tick@=" + new Date().getTime() / 1000 + "/"));
					System.out.println("-----------------------------------------heartbeat....");
					lastHeartBeat = Instant.now();
				}
			} catch (Exception e) {
				// if(sc.setDisconnected()){
				// e.printStackTrace();
				// sc.connect();
				// }
			}
		}
	}

	public class PrintData implements Runnable {
		private Socket clientSocket;

		public PrintData(Socket clientSocket) {
			this.clientSocket = clientSocket;
		}

		public void run() {
			try {
				// 目前后台设置为 45 秒
				convertStreamToString(clientSocket.getInputStream());
			} catch (Exception e) {
				if (sc.setDisconnected()) {
					e.printStackTrace();
					sc.connect();
				}
			}
		}
	}

	private DouyuMessageType getType(String message) {
		Pattern p = Pattern.compile("type@=((.)*?)\\/");
		Matcher m = p.matcher(message);
		if (m.find()) {
			try {
				return DouyuMessageType.valueOf(m.group(1));
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void convertStreamToString(java.io.InputStream is) throws IOException {

		int BUFFER_SIZE = 20480;// 0x140000;

		BufferedReader reader = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);

		char[] buffer = new char[BUFFER_SIZE];
		Instant instant = Instant.now();
		// Set<String> richguys= new HashSet<>();
		while (true) {
			reader.read(buffer, 0, BUFFER_SIZE);
			Instant instantRead = Instant.now();
			String data = new String(buffer).trim();
			Arrays.fill(buffer, (char) 0);
			Arrays.stream(data.split("type@=")).map(message -> {
				if (message.matches(".*[^/][^/][^/][^/][^/][^/][^/][^/][^/][^/][^/][^/]rid@=.*")) {
					System.out.println("~~~~~~~~~~~cut:\n"+message);
					return message.split("[^/][^/][^/][^/][^/][^/][^/][^/][^/][^/][^/][^/]rid@=")[0];
				} else if (message.matches(".*\\s.*")) {
					//System.out.println("~~~~~~~~~~~Somehow there is space in the message");
					return message.replaceAll("\\s", "");
				} else
					return message;
			}).filter(message -> message != null).filter(message -> !message.isEmpty())
					.filter(message -> message.length() > 12).map(message -> "type@=" + message).forEach(message -> {
						DouyuMessageType type = getType(message);
						if (type != null) {
							redisMQ.messageQueue(type, message);
						}
						String display = message;
						try {
							if (display != null)
								webSocket.convertAndSend("/topic/greetings", new Greeting(display));
						} catch (NullPointerException ue) {
							// do nothing
						}
					});
			// System.out.println( data.length()+ " : \n" + data);

			System.out.println("---------------------------buffer_size = " + BUFFER_SIZE);
			if (data.length() > BUFFER_SIZE * 4 / 10) {
				System.out
						.println("------------------------------------------------------increasing buffer by once....");
				BUFFER_SIZE *= 2;
				reader = new BufferedReader(new InputStreamReader(is), BUFFER_SIZE);
				buffer = new char[BUFFER_SIZE];
			}
			Instant instantPrint = Instant.now();
			System.out.println("-------------read interval = " + Duration.between(instant, instantRead) + " : \n"
					+ "-------------print interval = " + Duration.between(instantRead, instantPrint) + " : \n"
					+ "---LHB = " + lastHeartBeat + " : \n");

//			try {
//				Thread.sleep(200);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
			instant = instantPrint;
		}
	}

	public List<Map<String, String>> parseMultipleMessage(String[] messages) {
		List<Map<String, String>> data = new ArrayList<>();
		for (String message : messages) {
			data.add(parseMessage(message));
		}
		return data;
	}

	public Map<String, String> parseMessage(String message) {
		Map<String, String> protocal = new LinkedHashMap<>();
		message = "type@=" + message;
		String[] entries = message.split("/");
		for (String entry : entries) {
			entry = entry.replaceAll("@S", "/");
			String[] pair = entry.split("@=");
			for (String str : pair) {
				str = str.replaceAll("@A", "@");
			}
			try {
				protocal.put(pair[0], pair[1]);
			} catch (ArrayIndexOutOfBoundsException e) {// not key-value pair,
														// must be a header or a
														// bad data
				// do nothing
			}
		}
		return protocal;
	}

	public byte[] applyDouYuProtocol(String msg) {

		byte[] message = msg.getBytes(StandardCharsets.UTF_8);
		int length = message.length;
		byte[] result = new byte[length + 13];
		ByteBuffer buffer = ByteBuffer.allocate(length + 13);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putInt(length + 9);
		buffer.putInt(length + 9);
		buffer.putShort((short) 689);
		buffer.put((byte) 0);
		buffer.put((byte) 0);
		buffer.put(message, 0, length);
		buffer.put((byte) '\0');
		buffer.rewind();
		buffer.get(result);
		return result;
	}

	@Component
	public class MyRunner implements CommandLineRunner {
		@Override
		public void run(String... args) throws Exception {
			// connect(clientSocket);
		}
	}

	public Thread heartBeatThread(Socket clientSocket) {

		Thread heartBeatThread = new Thread(new SendHeartBeat(clientSocket));
		heartBeatThread.start();
		return heartBeatThread;

	}

	public Thread printerThread(Socket clientSocket) {
		Thread printerThread = new Thread(new PrintData(clientSocket));
		printerThread.start();
		return printerThread;
	}

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	// 1. The new datahandler methods return no exception (It deal with all its
	// exceptions locals)

	// 2. better filter that allows chat, super chat, super user in, and good
	// gifts

	// "chatmsg" "spbc".equals(map.get("type")) "bc_buy_deserve"

	// 3. an socket web service

	// 4. allows user to change filter settings
}
