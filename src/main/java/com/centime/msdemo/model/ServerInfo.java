package com.centime.msdemo.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public final class ServerInfo {
	private String serverStatus;

	public ServerInfo(String serverStatus) {
		super();
		this.serverStatus = serverStatus;
	}
}
