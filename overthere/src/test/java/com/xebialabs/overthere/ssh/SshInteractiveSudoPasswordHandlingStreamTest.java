package com.xebialabs.overthere.ssh;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

public class SshInteractiveSudoPasswordHandlingStreamTest {

	private OutputStream os;

	@Before
	public void init() {
		os = mock(OutputStream.class);
	}

	@Test
	public void shouldSendPasswordOnMatchingOutput() throws IOException {
		InputStream is = new ByteArrayInputStream("Password for user bar:".getBytes());
		SshInteractiveSudoPasswordHandlingStream foo = new SshInteractiveSudoPasswordHandlingStream(is, os, "foo", "[Pp]assword.*:");
		readStream(foo);
		verify(os).write("foo\r\n".getBytes());
		verify(os).flush();
	}

	@Test
	public void shouldNotSendPasswordWhenRegexDoesntMatch() throws IOException {
		InputStream is = new ByteArrayInputStream("Password:".getBytes());
		SshInteractiveSudoPasswordHandlingStream foo = new SshInteractiveSudoPasswordHandlingStream(is, os, "foo", "[Pp]assword.*>");
		readStream(foo);
		verifyZeroInteractions(os);
	}
	
	private void readStream(SshInteractiveSudoPasswordHandlingStream foo) throws IOException {
		while(foo.available() > 0) {
			foo.read();
		}
	}
}