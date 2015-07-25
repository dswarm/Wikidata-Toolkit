package org.wikidata.wdtk.wikibaseapi;

/*
 * #%L
 * Wikidata Toolkit Wikibase API
 * %%
 * Copyright (C) 2014 - 2015 Wikidata Toolkit Developers
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.wikidata.wdtk.testing.MockStringContentFactory;
import org.wikidata.wdtk.util.CompressionType;

/**
 * Mock implementation of {@link ApiConnection}
 * 
 * @author michael
 * 
 */
public class MockApiConnection extends ApiConnection {

	/**
	 * Mapping from hashs of query parameter maps to request results.
	 */
	final HashMap<Integer, byte[]> webResources;

	/**
	 * Constructor.
	 */
	public MockApiConnection() {
		super("");
		webResources = new HashMap<Integer, byte[]>();
	}

	/**
	 * Adds a new web resource to mock a request.
	 * 
	 * @param parameters
	 * @param result
	 */
	public void setWebResource(Map<String, String> parameters, String result) {
		int hash = parameters.hashCode();
		if (this.webResources.containsKey(hash)) {
			// TODO logger.warn - add logger to this class
		}
		this.webResources.put(hash, result.getBytes(StandardCharsets.UTF_8));
	}

	// TODO this function is very similar to a function in the
	// MockWebResourceFetcher. Sharing this code in some way would be more
	// convenient.
	/**
	 * Defines the contents of a new web resource by taking the string from a
	 * given (Java) resource, possibly using additional compression.
	 * 
	 * @param url
	 *            the URL string
	 * @param resource
	 *            the Java resource name
	 * @param resourceClass
	 *            the Class relative to which the resource should be resolved
	 *            (since resources are stored relative to a classpath); can
	 *            usually be obtained with getClass() from the calling object
	 * @param compressionType
	 *            the compression to use on the mocked contents
	 * @throws IOException
	 *             if the Java resource could not be loaded
	 */
	public void setWebResourceFromPath(Map<String, String> parameters,
			Class<?> resourceClass, String path, CompressionType compressionType)
			throws MalformedURLException, IOException {
		this.webResources.put(parameters.hashCode(), MockStringContentFactory
				.getStringFromUrl(resourceClass.getResource(path)).getBytes());
	}

	@Override
	public InputStream sendRequest(String requestMethod,
			Map<String, String> parameters) {
		return new ByteArrayInputStream(this.webResources.get(parameters
				.hashCode()));
	}

}