/*
 * Copyright 2012-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package sample.data.couchbase;

import java.net.ConnectException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.extension.OutputCapture;
import org.springframework.core.NestedCheckedException;

import static org.assertj.core.api.Assertions.assertThat;

class SampleCouchbaseApplicationTests {

	@RegisterExtension
	OutputCapture output = new OutputCapture();

	@Test
	void testDefaultSettings() {
		try {
			new SpringApplicationBuilder(SampleCouchbaseApplication.class)
					.run("--server.port=0");
		}
		catch (RuntimeException ex) {
			if (serverNotRunning(ex)) {
				return;
			}
		}
		assertThat(this.output).contains("firstName='Alice', lastName='Smith'");
	}

	private boolean serverNotRunning(RuntimeException ex) {
		@SuppressWarnings("serial")
		NestedCheckedException nested = new NestedCheckedException("failed", ex) {
		};
		if (nested.contains(ConnectException.class)) {
			Throwable root = nested.getRootCause();
			if (root.getMessage().contains("Connection refused")) {
				return true;
			}
		}
		return false;
	}

}
