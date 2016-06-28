package com.google.api.translate;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class Auth extends Authenticator {
	private String user, password;
	public Auth(String user, String password)
	{
		this.user = user;
		this.password = password;
	}
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(user, password
				.toCharArray());
	}
};