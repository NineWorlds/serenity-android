/**
 * The MIT License (MIT)
 * Copyright (c) 2012 David Carver
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF
 * OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package us.nineworlds.serenity.core.services;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.net.DhcpInfo;
import android.net.wifi.WifiManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

/**
 * GDM intent servervice used to detect Plex Media Servers on the local area
 * network.
 * 
 * https://github.com/NineWorlds/Plex-GDM-IntentService/blob/master/GDMService.
 * java
 * 
 * @author Ewan McEWen
 * 
 */
public class GDMService extends IntentService {
	public static final String MSG_RECEIVED = ".GDMService.MESSAGE_RECEIVED";
	public static final String SOCKET_CLOSED = ".GDMService.SOCKET_CLOSED";
	private static final String multicast = "239.0.0.250";

	public GDMService() {
		super("GDMService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		try {
			DatagramSocket socket = new DatagramSocket(32414);
			socket.setBroadcast(true);
			String data = "M-SEARCH * HTTP/1.1\r\n\r\n";
			DatagramPacket packet = new DatagramPacket(data.getBytes(),
					data.length(), useMultiCastAddress(), 32414);
//			DatagramPacket packet = new DatagramPacket(data.getBytes(),
//					data.length(), getBroadcastAddress(), 32414);
			
			socket.send(packet);
			Log.d("GDMService", "Search Packet Broadcasted");

			byte[] buf = new byte[256];
			packet = new DatagramPacket(buf, buf.length);
			socket.setSoTimeout(2000);
			boolean listening = true;
			while (listening) {
				try {
					socket.receive(packet);
					String packetData = new String(packet.getData());
					if (packetData.contains("HTTP/1.0 200 OK")) {
						Log.d("GDMService", "PMS Packet Received");
						// Broadcast Received Packet
						Intent packetBroadcast = new Intent(
								GDMService.MSG_RECEIVED);
						packetBroadcast.putExtra("data", packetData);
						packetBroadcast.putExtra("ipaddress", packet
								.getAddress().toString());
						LocalBroadcastManager.getInstance(this).sendBroadcast(
								packetBroadcast);
					}
				} catch (SocketTimeoutException e) {
					Log.d("GDMService", "Socket Timeout");
					socket.close();
					listening = false;
					Intent socketBroadcast = new Intent(
							GDMService.SOCKET_CLOSED);
					LocalBroadcastManager.getInstance(this).sendBroadcast(
							socketBroadcast);
				}

			}
		} catch (IOException e) {
			Log.e("GDMService", e.toString());
		}

	}

	// Builds the broadcast address based on the local network
	protected InetAddress getBroadcastAddress() throws IOException {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		DhcpInfo dhcp = wifi.getDhcpInfo();
		// handle null somehow

		int broadcast = (dhcp.ipAddress & dhcp.netmask) | ~dhcp.netmask;
		byte[] quads = new byte[4];
		for (int k = 0; k < 4; k++)
			quads[k] = (byte) (broadcast >> k * 8);
		return InetAddress.getByAddress(quads);
	}
	
	protected InetAddress useMultiCastAddress() throws IOException {
		return InetAddress.getByName(multicast);
	}
}