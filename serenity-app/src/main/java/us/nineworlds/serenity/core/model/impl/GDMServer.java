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

package us.nineworlds.serenity.core.model.impl;

import us.nineworlds.serenity.core.model.Server;

/**
 * @author dcarver
 *
 */
public class GDMServer implements Server {

	private String serverName;
	private String ipAddress;
	private String hostName;
	private String discoveryProtocol = "GDM";
	
	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#getServerName()
	 */
	@Override
	public String getServerName() {
		return serverName;
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#setServerName(java.lang.String)
	 */
	@Override
	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#getIPAddress()
	 */
	@Override
	public String getIPAddress() {
		return ipAddress;
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#setIPAddress(java.lang.String)
	 */
	@Override
	public void setIPAddress(String ipaddress) {
		this.ipAddress = ipaddress; 
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#getHostName()
	 */
	@Override
	public String getHostName() {
		return hostName;
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#setHostName(java.lang.String)
	 */
	@Override
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#discoveryProtocol()
	 */
	@Override
	public String discoveryProtocol() {
		return discoveryProtocol;
	}

	/* (non-Javadoc)
	 * @see us.nineworlds.serenity.core.model.Server#setDiscoveryProtocol(java.lang.String)
	 */
	@Override
	public void setDiscoveryProtocol(String protocol) {
		discoveryProtocol = protocol;
	}

}
